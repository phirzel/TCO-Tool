package ch.softenvironment.jomm;

/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * Database Transaction Handler This class is adapting JDO (and ODMG) Specification.
 *
 * @author Peter Hirzel
 */
@Slf4j
public class DbTransaction implements javax.jdo.Transaction {

    // enabling Nested Transactions
    private static final HashMap<DbObjectServer, List<?>> openTransactions = new HashMap<>();

    private final transient DbConnection connection;
    private volatile int transactionCounter = 0;
    private volatile int openCursors = 0;
    private transient boolean optimistic = true;

    // JDO Flags
    private boolean restoreValues = false;
    private boolean retainValues = false;
    private boolean nontransactionalWrite = false;
    private boolean nontransactionalRead = false;

    /**
     * Start a new transaction with the given database. There might be several connections (sessions) with the given database -> next available.
     *
     * @param server Database
     */
    private DbTransaction(DbObjectServer server /* String dbURL */) {
        super();

        // do the join
        connection = server.getConnection().pop(server.getPersistenceManagerFactory().getConnectionURL() /* dbURL */);
        setRestoreValues(connection.getObjectServer().getPersistenceManagerFactory().getRestoreValues());
        setRetainValues(connection.getObjectServer().getPersistenceManagerFactory().getRetainValues());
        setNontransactionalRead(connection.getObjectServer().getPersistenceManagerFactory().getNontransactionalRead());
        setNontransactionalWrite(connection.getObjectServer().getPersistenceManagerFactory().getNontransactionalWrite());
        setOptimistic(connection.getObjectServer().getPersistenceManagerFactory().getOptimistic());
    }

    /**
     * Start a Transaction.
     *
     * @see javax.jdo.Transaction
     */
    @Override
    public void begin() {
        try {
            begin(getConnection().getJdbcConnection().getTransactionIsolation());
        } catch (SQLException e) {
            log.error("could not BEGIN Transaction", e);
            throw new javax.jdo.JDOUserException(getResourceString("CE_begin"), e);
        }
    }

    /**
     * Start a Transaction with a desired Isolation-Level. The Isolation-Level must not be set within a current Transaction.
     *
     * @param isolationLevel Not every DBMS supports Isolation-Levels
     * see java.sql.Connection.TRANSACTION_*
     */
    protected synchronized void begin(final int isolationLevel) {
        if (!isActive()) {
            ((DbDomainNameServer) getConnection().getObjectServer().getPersistenceManagerFactory()).needsReconnect();
        }

        if (!checkNestedTransaction(1, "BEGIN")) {
            try {
                getConnection().getJdbcConnection().setTransactionIsolation(isolationLevel);
                /*
                 * switch (isolationLevel) { case
                 * Connection.TRANSACTION_READ_COMMITTED: { // dirty reads are
                 * prevented; non-repeatable reads and phantom reads can occur.
                 * Tracer.getInstance().logBackendCommand(
                 * "Isolation-Level: READ_COMMITTED"); break; } case
                 * Connection.TRANSACTION_READ_UNCOMMITTED: { // dirty reads,
                 * non-repeatable reads and phantom reads can occur
                 * Tracer.getInstance
                 * ().logBackendCommand("Isolation-Level: READ_UNCOMMITTED");
                 * break; } case Connection.TRANSACTION_REPEATABLE_READ: { //
                 * dirty reads and non-repeatable reads are prevented; phantom
                 * reads can occur. Tracer.getInstance().logBackendCommand(
                 * "Isolation-Level: REPEATABLE_READ"); break; } case
                 * Connection.TRANSACTION_SERIALIZABLE: {
                 * Tracer.getInstance().logBackendCommand
                 * ("Isolation-Level: SERIALIZABLE"); break; } case
                 * Connection.TRANSACTION_NONE: { // transactions are not
                 * supported
                 * Tracer.getInstance().logBackendCommand("Isolation-Level: NONE"
                 * ); break; } default: { Tracer.getInstance().runtimeWarning(
                 * "Isolation-Level: <undefined>"); } }
                 */
            } catch (SQLException e) {
                log.warn("Developer warning: #setTransactionIsolation(" + isolationLevel + ") failed or not supported: " + e.getLocalizedMessage());
            }
        }
    }

    /**
     * Check whether nested or not.
     *
     * @return true->nested; false->not nested
     */
    private boolean checkNestedTransaction(int increment, String action) {
        boolean nested;
        int level;

        if (increment > 0) {
            // do the BEGIN and increment the current Counter afterwards
            nested = isActive();
            level = transactionCounter;
            transactionCounter = transactionCounter + increment;
        } else {
            // decrement the counter first and do ABORT or COMMIT
            transactionCounter = transactionCounter + increment;
            if (transactionCounter < 0) {
                throw new ch.softenvironment.util.DeveloperException("No Transactions open!");
            }
            level = transactionCounter;
            nested = isActive();
        }

        if (nested) {
            // Tracer.getInstance().debug("[Transaction-level = " + level +
            // " (nested -> " + action + " supressed)]");
        } else {
            log.info("[Transaction-level = {} ({})]", level, action);
        }
        return nested;
    }

    /**
     * Commit a transaction (suppress if nested). Write the changes to all persistent objects which have been modified within the context of the transaction to the database.
     *
     * @see javax.jdo.Transaction
     */
    @Override
    public synchronized void commit() {
        try {
            if (!checkNestedTransaction(-1, "COMMIT")) {
                if (!getConnection().getJdbcConnection().getAutoCommit()) {
                    /*
                     * Tracer.getInstance().developerWarning(
                     * "NO COMMIT because AutoCommit=true"); } else {
                     */
                    getConnection().getJdbcConnection().commit();
                }
                if (openCursors > 0) {
                    log.error("Developer error: Open Cursors were not closed correctly!");
                }
            }
        } catch (SQLException e) {
            log.error("could not COMMIT Transaction", e);
            throw new javax.jdo.JDOUserException(getResourceString("CE_commit"), e);
        }
    }

    /**
     * Get the raw jdbc connection object
     */
    protected final DbConnection getConnection() {
        return connection;
    }

    /**
     * Return an active Transaction for given dbURL, null if none is active. Ignore the number of joins.
     */
    protected static DbTransaction getCurrentTransaction(DbObjectServer server /*
     * String
     * dbURL
     */) {
        List transaction = openTransactions.get(server /* dbURL */);

        if (transaction == null) {
            // none open yet
            return null;
        } else {
            return (DbTransaction) transaction.get(0);
        }
    }

    /**
     * If <code>true</code>, allows persistent instances to be read without a transaction active.
     *
     * @return the value of the nontransactionalRead property
     * @see javax.jdo.Transaction Interface
     */
    @Override
    public boolean getNontransactionalRead() {
        return nontransactionalRead;
    }

    /**
     * If <code>true</code>, allows persistent instances to be written without a transaction active.
     *
     * @return the value of the nontransactionalWrite property
     * @see javax.jdo.Transaction Interface
     */
    @Override
    public boolean getNontransactionalWrite() {
        return nontransactionalWrite;
    }

    /**
     * Optimistic transactions do not hold data store locks until commit time.
     *
     * @return the value of the Optimistic property.
     * @see javax.jdo.Transaction
     */
    @Override
    public boolean getOptimistic() {
        return optimistic;
    }

    @Override
    public String getIsolationLevel() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public void setIsolationLevel(String s) {
        //TODO HIP just added to compile
    }

    /**
     * The <code>Transaction</code> instance is always associated with exactly one <code>PersistenceManager</code>.
     *
     * @return the <code>PersistenceManager</code> for this
     *     <code>Transaction</code> instance
     * @see javax.jdo.Transaction
     */
    @Override
    public javax.jdo.PersistenceManager getPersistenceManager() {
        return DbDomainNameServer.getServer(getConnection().getObjectServer().getPersistenceManagerFactory().getConnectionURL());
    }

    @Override
    public void setSerializeRead(Boolean aBoolean) {
        //TODO HIP just added to compile
    }

    @Override
    public Boolean getSerializeRead() {
        //TODO HIP just added to compile
        return null;
    }

    private String getResourceString(String property) {
        return ch.softenvironment.client.ResourceManager.getResource(DbTransaction.class, property);
    }

    /**
     * Return the current value of the restoreValues property.
     *
     * @return the value of the restoreValues property
     * @see javax.jdo.Transaction
     */
    @Override
    public boolean getRestoreValues() {
        return restoreValues;
    }

    /**
     * If <code>true</code>, at commit time instances retain their field values.
     *
     * @return the value of the retainValues property
     * @see javax.jdo.Transaction
     */
    @Override
    public boolean getRetainValues() {
        return retainValues;
    }

    /**
     * The user-specified <code>Synchronization</code> instance for this
     * <code>Transaction</code> instance.
     *
     * @return the user-specified <code>Synchronization</code> instance.
     * @see javax.jdo.Transaction
     */
    @Override
    public javax.transaction.Synchronization getSynchronization() {
        throw new javax.jdo.JDOUnsupportedOptionException("NYI");
    }

    /**
     * Increment the Open-Cursor counter. Some Target-Systems (for e.g. DBMS) are limited by several open-Cursors. This may happen in case of complex Relationship-Patterns (Composites and/or
     * Aggregations).
     */
    protected synchronized void incrementOpenCursor(boolean open) {
        if (open) {
            openCursors++;
            // Tracer.getInstance().debug("#openCursor+1 => " + openCursors);
        } else {
            if (openCursors > 0) {
                openCursors--;
            } else {
                log.warn("Developer warning: openCursors == 0! (possible for failed DbQuery#closeAll() within exception-handler)");
            }
            // Tracer.getInstance().debug("#openCursor-1 => " + openCursors);
        }
    }

    /**
     * Returns whether there is a transaction currently active.
     *
     * @return <code>true</code> if the transaction is active.
     * @see javax.jdo.Transaction
     */
    @Override
    public boolean isActive() {
        // Return whether this Transaction for specific URL is nested right now.
        return transactionCounter > 0;
    }

    @Override
    public boolean getRollbackOnly() {
        //TODO HIP just added to compile
        return false;
    }

    @Override
    public void setRollbackOnly() {
        //TODO HIP just added to compile
    }

    /**
     * Register current thread to transaction. (ODMG-Specification). Synchronization of #join() and #leave() is to be done by caller.
     *
     * @see #leave(DbObjectServer)
     * @deprecated should be done by PersistenceManager
     */
    protected static DbTransaction join(DbObjectServer server /* String dbURL */) {
        List transaction = openTransactions.get(server /* dbURL */);

        if (transaction == null) {
            // go for new Transaction
            List newTransaction = new ArrayList(2);
            // Transaction
            newTransaction.add(new DbTransaction(server /* dbURL */));
            // joins-Counter
            newTransaction.add(Integer.valueOf(1));
            openTransactions.put(server /* dbURL */, newTransaction);
            return (DbTransaction) newTransaction.get(0);
        } else {
            // go for nested Transaction (increment join-Count)
            int joins = ((Integer) transaction.get(1)).intValue();
            transaction.set(1, Integer.valueOf(joins + 1));
            return (DbTransaction) transaction.get(0);
        }
    }

    /**
     * Unregister current thread from transaction (ODMG-Specification). Synchronization of #join() and #leave() is to be done by caller.
     *
     * @see #join()
     */
    protected static void leave(DbObjectServer server /* String url */) {
        //TODO should be done by PersistencManager
        List transaction = openTransactions.get(server /* url */);

        if (transaction == null) {
            throw new ch.softenvironment.util.DeveloperException("there should be a joined Transaction");
        } else {
            // Check-In Transaction (decrement join-Count)
            int joins = ((Integer) transaction.get(1)).intValue();
            if (joins > 1) {
                transaction.set(1, Integer.valueOf(joins - 1));
            } else {
                server.getConnection().push(((DbTransaction) transaction.get(0)).getConnection());
                openTransactions.remove(server /* url */);
            }
        }
    }

    /**
     * Abort a transaction. This causes the transaction to end and all changes to persistent objects made within the context of that transaction will be rolled back in the database.
     *
     * @see javax.jdo.Transaction
     */
    @Override
    public synchronized void rollback() {
        try {
            if (!checkNestedTransaction(-1, "ROLLBACK")) {
                if (getConnection().getJdbcConnection().getAutoCommit()) {
                    log.warn("Developer warning: NO ROLLBACK because AutoCommit=true");
                } else {
                    getConnection().getJdbcConnection().rollback();
                }
                if (openCursors > 0) {
                    log.warn("Developer error: Open Cursors were not closed correctly!");
                }
            }
        } catch (SQLException e) {
            log.error("could not ROLLBACK Transaction", e);
            throw new javax.jdo.JDOUserException(getResourceString("CE_rollback"), e);
        }
    }

    /**
     * If <code>true</code>, allow persistent instances to be read without a transaction active. If an implementation does not support this option, a
     * <code>JDOUnsupportedOptionException</code> is thrown.
     *
     * @param nontransactionalRead the value of the nontransactionalRead property
     * @see javax.jdo.Transaction
     */
    @Override
    public void setNontransactionalRead(boolean nontransactionalRead) {
        this.nontransactionalRead = nontransactionalRead;
    }

    /**
     * If <code>true</code>, allow persistent instances to be written without a transaction active. If an implementation does not support this option, a
     * <code>JDOUnsupportedOptionException</code> is thrown.
     *
     * @param nontransactionalWrite the value of the nontransactionalRead property
     * @see javax.jdo.Transaction
     */
    @Override
    public void setNontransactionalWrite(boolean nontransactionalWrite) {
        this.nontransactionalWrite = nontransactionalWrite;
    }

    /**
     * Optimistic transactions do not hold data store locks until commit time. If an implementation does not support this option, a
     * <code>JDOUnsupportedOptionException</code> is thrown.
     *
     * @param optimistic the value of the Optimistic flag.
     * @see javax.jdo.Transaction
     */
    @Override
    public void setOptimistic(boolean optimistic) {
        if (!optimistic) {
            // TODO NYI:
            // "Multi-User-Environment=>unexpected persistency behaviour!"
        }
        this.optimistic = optimistic;
    }

    /**
     * If <code>true</code>, at rollback, fields of newly persistent instances are restored to their values as of the beginning of the transaction, and the instances revert to transient. Additionally,
     * fields of modified instances of primitive types and immutable reference types are restored to their values as of the beginning of the transaction.
     * <p>
     * If <code>false</code>, at rollback, the values of fields of newly persistent instances are unchanged and the instances revert to transient. Additionally, dirty instances transition to hollow.
     * If an implementation does not support this option, a
     * <code>JDOUnsupportedOptionException</code> is thrown.
     *
     * @param restoreValues the value of the restoreValues property
     * @see javax.jdo.Transaction
     */
    @Override
    public void setRestoreValues(boolean restoreValues) {
        this.restoreValues = restoreValues;
    }

    /**
     * If <code>true</code>, at commit instances retain their values and the instances transition to persistent-nontransactional. If an implementation does not support this option, a
     * <code>JDOUnsupportedOptionException</code> is thrown.
     *
     * @param retainValues the value of the retainValues property
     * @see javax.jdo.Transaction
     */
    @Override
    public void setRetainValues(boolean retainValues) {
        this.retainValues = retainValues;
    }

    /**
     * The user can specify a <code>Synchronization</code> instance to be notified on transaction completions. The <code>beforeCompletion</code> method is called prior to flushing instances to the
     * data store.
     *
     * <p>
     * The <code>afterCompletion</code> method is called after performing state transitions of persistent and transactional instances, following the data store commit or rollback operation.
     * <p>
     * Only one <code>Synchronization</code> instance can be registered with the
     * <code>Transaction</code>. If the application requires more than one
     * instance to receive synchronization callbacks, then the single application instance is responsible for managing them, and forwarding callbacks to them.
     *
     * @param sync the <code>Synchronization</code> instance to be notified;
     *     <code>null</code> for none
     * @see javax.jdo.Transaction
     */
    @Override
    public void setSynchronization(javax.transaction.Synchronization sync) {
        throw new javax.jdo.JDOUnsupportedOptionException("NYI");
    }

    /**
     * Reset Transaction for a specific URL in case of a Session-close.
     */
    protected static synchronized void tryReset(DbObjectServer server /*
     * String
     * dbURL
     */) throws ch.softenvironment.util.DeveloperException {
        if ((openTransactions != null) && openTransactions.containsKey(server /* dbURL */)) {
            List<?> list = openTransactions.get(server /* dbURL */);
            if (((Integer) list.get(1)).intValue() > 0) {
                throw new ch.softenvironment.util.DeveloperException("Reset impossible => uncommited Transactions");
            }
        }
    }
}

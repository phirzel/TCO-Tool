package ch.softenvironment.jomm.target.xml;
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

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Properties;
import java.util.concurrent.Executor;
import javax.jdo.JDOUnsupportedOptionException;
import lombok.extern.slf4j.Slf4j;

/**
 * This is just a dummy implementation to stay compatible with the basic ch.softenvironment.jomm Framework.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.5 $ $Date: 2007-06-05 20:40:14 $
 * @deprecated (should be replaced by a real XML - Database driver)
 */
@Slf4j
class XmlJdbcConnection implements java.sql.Connection {

    private boolean autoCommit = true;

    /**
     * XmlJdbcConnection constructor comment.
     */
    public XmlJdbcConnection() {
        super();
    }

    /**
     * Clears all warnings reported for this <code>Connection</code> object. After a call to this method, the method <code>getWarnings</code> returns null until a new warning is reported for this
     * Connection.
     *
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void clearWarnings() {
    }

    /**
     * Releases a Connection's database and JDBC resources immediately instead of waiting for them to be automatically released.
     *
     * <P><B>Note:</B> A Connection is automatically closed when it is
     * garbage collected. Certain fatal errors also result in a closed Connection.
     *
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void close() throws java.sql.SQLException {
    }

    /**
     * Makes all changes made since the previous commit/rollback permanent and releases any database locks currently held by the Connection. This method should be used only when auto-commit mode has
     * been disabled.
     *
     * @throws SQLException if a database access error occurs
     * @see #setAutoCommit
     */
    @Override
    public void commit() {
        //TODO
        log.warn("Developer warning: NO Commit done!");
    }

    /**
     * Creates a <code>Statement</code> object for sending SQL statements to the database. SQL statements without parameters are normally executed using Statement objects. If the same SQL statement is
     * executed many times, it is more efficient to use a PreparedStatement
     * <p>
     * JDBC 2.0
     * <p>
     * Result sets created using the returned Statement will have forward-only type, and read-only concurrency, by default.
     *
     * @return a new Statement object
     * @throws SQLException if a database access error occurs
     */
    @Override
    public java.sql.Statement createStatement() {
        return null;
    }

    /**
     * JDBC 2.0
     * <p>
     * Creates a <code>Statement</code> object that will generate
     * <code>ResultSet</code> objects with the given type and concurrency.
     * This method is the same as the <code>createStatement</code> method above, but it allows the default result set type and result set concurrency type to be overridden.
     *
     * @param resultSetType a result set type; see ResultSet.TYPE_XXX
     * @param resultSetConcurrency a concurrency type; see ResultSet.CONCUR_XXX
     * @return a new Statement object
     * @throws SQLException if a database access error occurs
     */
    @Override
    public java.sql.Statement createStatement(int resultSetType, int resultSetConcurrency) {
        return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#createStatement(int, int, int)
     */
    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
        return null;
    }

    /**
     * Gets the current auto-commit state.
     *
     * @return the current state of auto-commit mode
     * @throws SQLException if a database access error occurs
     * @see #setAutoCommit
     */
    @Override
    public boolean getAutoCommit() {
        return autoCommit;
    }

    /**
     * Returns the Connection's current catalog name.
     *
     * @return the current catalog name or null
     * @throws SQLException if a database access error occurs
     */
    @Override
    public java.lang.String getCatalog() {
        return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#getHoldability()
     */
    @Override
    public int getHoldability() {
        return 0;
    }

    /**
     * Gets the metadata regarding this connection's database. A Connection's database is able to provide information describing its tables, its supported SQL grammar, its stored procedures, the
     * capabilities of this connection, and so on. This information is made available through a DatabaseMetaData object.
     *
     * @return a DatabaseMetaData object for this Connection
     * @throws SQLException if a database access error occurs
     */
    @Override
    public java.sql.DatabaseMetaData getMetaData() {
        return null;
    }

    /**
     * Gets this Connection's current transaction isolation level.
     *
     * @return the current TRANSACTION_* mode value
     */
    @Override
    public int getTransactionIsolation() {
        return Connection.TRANSACTION_NONE;
    }

    /**
     * JDBC 2.0
     * <p>
     * Gets the type map object associated with this connection. Unless the application has added an entry to the type map, the map returned will be empty.
     *
     * @return the <code>java.util.Map</code> object associated with this <code>Connection</code> object
     */
    @Override
    public java.util.Map getTypeMap() {
        return null;
    }

    /**
     * Returns the first warning reported by calls on this Connection.
     *
     * <P><B>Note:</B> Subsequent warnings will be chained to this
     * SQLWarning.
     *
     * @return the first SQLWarning or null
     * @throws SQLException if a database access error occurs
     */
    @Override
    public java.sql.SQLWarning getWarnings() {
        return null;
    }

    /**
     * Tests to see if a Connection is closed.
     *
     * @return true if the connection is closed; false if it's still open
     * @throws SQLException if a database access error occurs
     */
    @Override
    public boolean isClosed() {
        return false;
    }

    /**
     * Tests to see if the connection is in read-only mode.
     *
     * @return true if connection is read-only and false otherwise
     * @throws SQLException if a database access error occurs
     */
    @Override
    public boolean isReadOnly() {
        return false;
    }

    /**
     * Converts the given SQL statement into the system's native SQL grammar. A driver may convert the JDBC sql grammar into its system's native SQL grammar prior to sending it; this method returns
     * the native form of the statement that the driver would have sent.
     *
     * @param sql a SQL statement that may contain one or more '?' parameter placeholders
     * @return the native form of this statement
     * @throws SQLException if a database access error occurs
     */
    @Override
    public java.lang.String nativeSQL(java.lang.String sql) {
        return null;
    }

    /**
     * Creates a <code>CallableStatement</code> object for calling database stored procedures. The CallableStatement provides methods for setting up its IN and OUT parameters, and methods for
     * executing the call to a stored procedure.
     *
     * <P><B>Note:</B> This method is optimized for handling stored
     * procedure call statements. Some drivers may send the call statement to the database when the method <code>prepareCall</code> is done; others may wait until the CallableStatement is executed.
     * This has no direct effect on users; however, it does affect which method throws certain SQLExceptions.
     * <p>
     * JDBC 2.0
     * <p>
     * Result sets created using the returned CallableStatement will have forward-only type and read-only concurrency, by default.
     *
     * @param sql a SQL statement that may contain one or more '?' parameter placeholders. Typically this  statement is a JDBC function call escape string.
     * @return a new CallableStatement object containing the pre-compiled SQL statement
     * @throws SQLException if a database access error occurs
     */
    @Override
    public java.sql.CallableStatement prepareCall(java.lang.String sql) {
        return null;
    }

    /**
     * JDBC 2.0
     * <p>
     * Creates a <code>CallableStatement</code> object that will generate
     * <code>ResultSet</code> objects with the given type and concurrency.
     * This method is the same as the <code>prepareCall</code> method above, but it allows the default result set type and result set concurrency type to be overridden.
     *
     * @param resultSetType a result set type; see ResultSet.TYPE_XXX
     * @param resultSetConcurrency a concurrency type; see ResultSet.CONCUR_XXX
     * @return a new CallableStatement object containing the pre-compiled SQL statement
     * @throws SQLException if a database access error occurs
     */
    @Override
    public java.sql.CallableStatement prepareCall(java.lang.String sql, int resultSetType, int resultSetConcurrency) {
        return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
     */
    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Creates a <code>PreparedStatement</code> object for sending parameterized SQL statements to the database.
     * <p>
     * A SQL statement with or without IN parameters can be pre-compiled and stored in a PreparedStatement object. This object can then be used to efficiently execute this statement multiple times.
     *
     * <P><B>Note:</B> This method is optimized for handling
     * parametric SQL statements that benefit from precompilation. If the driver supports precompilation, the method <code>prepareStatement</code> will send the statement to the database for
     * precompilation. Some drivers may not support precompilation. In this case, the statement may not be sent to the database until the <code>PreparedStatement</code> is executed.  This has no
     * direct effect on users; however, it does affect which method throws certain SQLExceptions.
     * <p>
     * JDBC 2.0
     * <p>
     * Result sets created using the returned PreparedStatement will have forward-only type and read-only concurrency, by default.
     *
     * @param sql a SQL statement that may contain one or more '?' IN parameter placeholders
     * @return a new PreparedStatement object containing the pre-compiled statement
     * @throws SQLException if a database access error occurs
     */
    @Override
    public java.sql.PreparedStatement prepareStatement(java.lang.String sql) {
        return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
     */
    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareStatement(java.lang.String, int)
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * JDBC 2.0
     * <p>
     * Creates a <code>PreparedStatement</code> object that will generate
     * <code>ResultSet</code> objects with the given type and concurrency.
     * This method is the same as the <code>prepareStatement</code> method above, but it allows the default result set type and result set concurrency type to be overridden.
     *
     * @param resultSetType a result set type; see ResultSet.TYPE_XXX
     * @param resultSetConcurrency a concurrency type; see ResultSet.CONCUR_XXX
     * @return a new PreparedStatement object containing the pre-compiled SQL statement
     * @throws SQLException if a database access error occurs
     */
    @Override
    public java.sql.PreparedStatement prepareStatement(java.lang.String sql, int resultSetType, int resultSetConcurrency) {
        return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
     */
    @Override
    public void releaseSavepoint(Savepoint savepoint) {
        // TODO Auto-generated method stub
    }

    /**
     * Drops all changes made since the previous commit/rollback and releases any database locks currently held by this Connection. This method should be used only when auto- commit has been
     * disabled.
     *
     * @throws SQLException if a database access error occurs
     * @see #setAutoCommit
     */
    @Override
    public void rollback() {
        //TODO Auto-generated method stub
        log.warn("Developer warning: NO Rollback done!");
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#rollback(java.sql.Savepoint)
     */
    @Override
    public void rollback(Savepoint savepoint) {
        // TODO Auto-generated method stub

    }

    /**
     * Sets this connection's auto-commit mode. If a connection is in auto-commit mode, then all its SQL statements will be executed and committed as individual transactions.  Otherwise, its SQL
     * statements are grouped into transactions that are terminated by a call to either the method <code>commit</code> or the method <code>rollback</code>. By default, new connections are in
     * auto-commit mode.
     * <p>
     * The commit occurs when the statement completes or the next execute occurs, whichever comes first. In the case of statements returning a ResultSet, the statement completes when the last row of
     * the ResultSet has been retrieved or the ResultSet has been closed. In advanced cases, a single statement may return multiple results as well as output parameter values. In these cases the
     * commit occurs when all results and output parameter values have been retrieved.
     *
     * @param autoCommit true enables auto-commit; false disables auto-commit.
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void setAutoCommit(boolean autoCommit) {
        if (!autoCommit) {
            throw new JDOUnsupportedOptionException("only non-committed transactions supported by XML");
        }
        this.autoCommit = autoCommit;
    }

    /**
     * Sets a catalog name in order to select a subspace of this Connection's database in which to work. If the driver does not support catalogs, it will silently ignore this request.
     *
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void setCatalog(java.lang.String catalog) {
        //TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#setHoldability(int)
     */
    @Override
    public void setHoldability(int holdability) {
        // TODO Auto-generated method stub
    }

    /**
     * Puts this connection in read-only mode as a hint to enable database optimizations.
     *
     * <P><B>Note:</B> This method cannot be called while in the
     * middle of a transaction.
     *
     * @param readOnly true enables read-only mode; false disables read-only mode.
     * @throws SQLException if a database access error occurs
     */
    @Override
    public void setReadOnly(boolean readOnly) {
        //TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#setSavepoint()
     */
    @Override
    public Savepoint setSavepoint() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see java.sql.Connection#setSavepoint(java.lang.String)
     */
    @Override
    public Savepoint setSavepoint(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Attempts to change the transaction isolation level to the one given. The constants defined in the interface <code>Connection</code> are the possible transaction isolation levels.
     *
     * <P><B>Note:</B> This method cannot be called while
     * in the middle of a transaction.
     *
     * @param level one of the TRANSACTION_* isolation values with the exception of TRANSACTION_NONE; some databases may not support other values
     * @throws SQLException if a database access error occurs
     * @see java.sql.DatabaseMetaData#supportsTransactionIsolationLevel(int)
     */
    @Override
    public void setTransactionIsolation(int level) {
        if (level != Connection.TRANSACTION_NONE) {
            //TODO
            throw new JDOUnsupportedOptionException("only Connection.TRANSACTION_NONE");
        }
    }

    /**
     * JDBC 2.0
     * <p>
     * Installs the given type map as the type map for this connection.  The type map will be used for the custom mapping of SQL structured types and distinct types.
     *
     * @param map <code>java.util.Map</code> object to install as the replacement for this <code>Connection</code> object's default type map
     */
    @Override
    public void setTypeMap(java.util.Map map) {
        //TODO Auto-generated method stub
    }

    @Override
    public Array createArrayOf(String arg0, Object[] arg1) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Blob createBlob() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Clob createClob() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public NClob createNClob() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Struct createStruct(String arg0, Object[] arg1) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setSchema(String schema) throws SQLException {

    }

    @Override
    public String getSchema() throws SQLException {
        return null;
    }

    @Override
    public void abort(Executor executor) throws SQLException {

    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {

    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getClientInfo(String arg0) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isValid(int arg0) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setClientInfo(Properties arg0) throws SQLClientInfoException {
        // TODO Auto-generated method stub

    }

    @Override
    public void setClientInfo(String arg0, String arg1)
        throws SQLClientInfoException {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isWrapperFor(Class arg0) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Object unwrap(Class arg0) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }
}

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

import lombok.extern.slf4j.Slf4j;

/**
 * User TransactionBlock. This is a Convenience Class for standard Transaction behavior in Applications.
 * <p>
 * Each UserTransactionBlock will be started in an own Target-Thread (be aware whether a Target supports multi-threading or not).
 *
 * @author Peter Hirzel
 * @see DbObjectServer#createUserTransactionBlock(boolean)
 */
@Slf4j
public class DbUserTransactionBlock {

    private final DbObjectServer server;
    private DbTransaction transaction = null;
    private Exception exception = null;
    private Object returnValue = null;
    private boolean ownThread = true;

    /**
     * DbTransactionBlock constructor comment.
     */
    protected DbUserTransactionBlock(DbObjectServer server, boolean ownThread) {
        super();

        this.server = server;
        this.ownThread = ownThread;
    }

    /**
     * Terminate current TransactionBlock. Call this method within Runnable#run() of execute(Runnable) to abort robustly.
     *
     * @param message any explanation about the abortion
     * @param exception original exception happened
     * @see #execute(Runnable)
     */
    public void abort(String message, Exception exception) {
        this.exception = exception;
        log.error("{}", message, exception);
    }

    /**
     * Execute the block in a safe, atomar Transaction-Thread. Any failures happened within given block will be forwarded after handling the transaction in a robust way, say after begin/end
     * Transaction.
     * <p>
     * Call #abort() in an Exception-Handler of Runnable#run() in case of any failures.
     *
     * @throws ch.softenvironment.util.UserException
     * @see #abort(String, Exception)
     * @see #setReturnValue(Object)
     */
    public void execute(Runnable block) throws Exception {
        try {
            transaction = server.openTransactionThread(ownThread);
            block.run();
        } catch (Exception e) {
            exception = e;
        }
        server.closeTransactionThread(exception, transaction);

        if (exception != null) {
            // forward exception
            log.error("Block failed", exception);
            throw exception;
        }
    }

    /**
     * Return the server this transactionBlock is meant to run on.
     */
    public DbObjectServer getObjectServer() {
        return server;
    }

    /**
     * @see #setReturnValue(Object)
     */
    public Object getReturnValue() {
        return returnValue;
    }

    /**
     * Return the server this transactionBlock is meant to run on.
     */
    public DbTransaction getTransaction() {
        return transaction;
    }

    /**
     * Set the eventual ReturnValue manipulated in a Runnable block.
     *
     * @see #execute(Runnable block)
     */
    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }
}

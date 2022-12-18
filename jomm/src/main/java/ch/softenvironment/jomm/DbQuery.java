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

import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.util.Statistic;
import ch.softenvironment.util.StringUtils;
import ch.softenvironment.util.UserException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Map;
import javax.jdo.FetchPlan;
import javax.jdo.Query;
import lombok.extern.slf4j.Slf4j;

/**
 * Maintain and execute a Database Query (by means an SQL-Statement).
 *
 * @author Peter Hirzel
 */

@Slf4j
public class DbQuery implements javax.jdo.Query {

    public static final int INCOMPLETE = 0;
    public static final int COMPLETE = 1;

    protected transient DbTransaction transaction = null;
    protected transient String query = null;
    protected transient ResultSet result = null;
    protected transient Statistic statistic = null;
    protected transient SQLWarning status = null;
    protected transient DbQueryBuilder builder = null;
    private transient Statement dynamicStatement = null;
    private transient int resultSetType = -1; // ResultSet.TYPE_FORWARD_ONLY;
    private transient int resultSetConcurrency = -1;// ResultSet.CONCUR_READ_ONLY;
    private final transient boolean batch = false;

    // JDO flags
    private boolean ignoreCache = false;

    /**
     * Create a new DbQuery to be executed within the given transaction.
     *
     * @param transaction to commit query
     * @param builder Containing ONE specific database query
     * @see Statistic
     */
    public DbQuery(DbTransaction transaction, DbQueryBuilder builder) {
        this(transaction, builder.getQueryDescription());

        this.builder = builder;
    }

    /**
     * Create a new DbQuery for a SQL DQL (SELECT) according to JDBC2.0 with different Scroll- and Concurrency-Type.
     *
     * @param transaction to commit query
     * @param builder containing the SELECT query
     * @param resultSetType a result set type; see ResultSet.TYPE_XXX
     * @param resultSetConcurrency a concurrency type; see ResultSet.CONCUR_XXX
     * @see Statistic
     */
    public DbQuery(DbTransaction transaction, DbQueryBuilder builder, final int resultSetType, final int resultSetConcurrency) {
        this(transaction, builder);

        if (!builder.isSelection()) {
            throw new DeveloperException("Use this Constructor for SELECT's only!", "Bad Usage");
        }

        this.resultSetType = resultSetType;
        this.resultSetConcurrency = resultSetConcurrency;
    }

    /**
     * Create a new DbQuery.
     *
     * @param transaction to commit query
     * @param useCase Description for Query
     * @see Statistic
     */
    private DbQuery(DbTransaction transaction, String useCase) {
        super();

        this.transaction = transaction;
        this.statistic = Statistic.createEntry(useCase);
    }

    /**
     * Close a query result and release any resources associated with it. The parameter is the return from <code>execute(...)</code> and might have iterators open on it. Iterators associated with the
     * query result are invalidated: they return <code>false</code> to <code>hasNext()</code> and throw <code>NoSuchElementException</code> to <code>next()</code>.
     *
     * @param queryResult the result of <code>execute(...)</code> on this
     *     <code>Query</code> instance.
     * @see javax.jdo.Query
     */
    @Override
    public void close(java.lang.Object queryResult) {
        if ((queryResult != null) && (queryResult instanceof ResultSet)) {
            transaction.incrementOpenCursor(false);
            try {
                ((ResultSet) queryResult).close();
            } catch (SQLException e) {
                throw new UserException(getResourceString("CW_CursorError"), getResourceString("CT_CursorError"), e);
            }
        } // else { "NYI"; }
    }

    /**
     * Close all query results associated with this <code>Query</code> instance, and release all resources associated with them. The query results might have iterators open on them. Iterators
     * associated with the query results are invalidated: they return <code>false</code> to <code>hasNext()</code> and throw <code>NoSuchElementException</code> to <code>next()</code>.
     *
     * @see javax.jdo.Query
     */
    @Override
    public void closeAll() {
        UserException closeEx = null;
        try {
            close(result);
        } catch (UserException ex) {
            closeEx = ex;
        }
        result = null;
        try {
            if (dynamicStatement != null) {
                dynamicStatement.close();
            }
        } catch (SQLException e) {
            if (closeEx == null) {
                throw new UserException(getResourceString("CW_CursorError"), getResourceString("CT_CursorError"), e);
            } else {
                log.error("Follow-up error", e);
                throw new UserException(getResourceString("CW_CursorError"), getResourceString("CT_CursorError"), closeEx);
            }
        }
    }

    @Override
    public void setGrouping(String s) {

    }

    @Override
    public void setUnique(boolean b) {

    }

    @Override
    public void setResult(String s) {

    }

    @Override
    public void setResultClass(Class aClass) {

    }

    @Override
    public void setRange(long l, long l1) {

    }

    @Override
    public void setRange(String s) {

    }

    @Override
    public void addExtension(String s, Object o) {

    }

    @Override
    public void setExtensions(Map map) {

    }

    @Override
    public FetchPlan getFetchPlan() {
        return null;
    }

    @Override
    public long deletePersistentAll(Object... objects) {
        return 0;
    }

    @Override
    public long deletePersistentAll(Map map) {
        return 0;
    }

    @Override
    public long deletePersistentAll() {
        return 0;
    }

    @Override
    public void setUnmodifiable() {

    }

    @Override
    public boolean isUnmodifiable() {
        return false;
    }

    @Override
    public void addSubquery(Query query, String s, String s1) {

    }

    @Override
    public void addSubquery(Query query, String s, String s1, String s2) {

    }

    @Override
    public void addSubquery(Query query, String s, String s1, String... strings) {

    }

    @Override
    public void addSubquery(Query query, String s, String s1, Map map) {

    }

    @Override
    public void setDatastoreReadTimeoutMillis(Integer integer) {

    }

    @Override
    public Integer getDatastoreReadTimeoutMillis() {
        return null;
    }

    @Override
    public void setDatastoreWriteTimeoutMillis(Integer integer) {

    }

    @Override
    public Integer getDatastoreWriteTimeoutMillis() {
        return null;
    }

    @Override
    public void cancelAll() {

    }

    @Override
    public void cancel(Thread thread) {

    }

    @Override
    public void setSerializeRead(Boolean aBoolean) {

    }

    @Override
    public Boolean getSerializeRead() {
        return null;
    }

    /**
     * Verify the elements of the query and provide a hint to the query to prepare and optimize an execution plan.
     *
     * @see javax.jdo.Query
     */
    @Override
    public void compile() {
        throw new javax.jdo.JDOUnsupportedOptionException("DbQuery#compile()");
    }

    /**
     * Create a dynamic SQL-Query. (@see org.odmg.OQLQuery)
     */
    private void create(java.lang.String query) throws SQLException {
        this.query = query;
        closeAll();
        if ((builder != null) && builder.isSelection() && (resultSetType != -1) && (resultSetConcurrency != -1)) {
            try {
                dynamicStatement = transaction.getConnection().getJdbcConnection().createStatement(resultSetType, resultSetConcurrency);
            } catch (UnsupportedOperationException e) {
                // not supported before JDBC2.0
                log.warn("ResulSetTyp and -Concurrency not supported by DBMS-Driver!");
                dynamicStatement = transaction.getConnection().getJdbcConnection().createStatement();
            }
        } else {
            dynamicStatement = transaction.getConnection().getJdbcConnection().createStatement();
        }
        log.info(query);
    }

    /**
     * Set the import statements to be used to identify the fully qualified name of variables or parameters. Parameters and unbound variables might come from a different class from the candidate
     * class, and the names need to be declared in an import statement to eliminate ambiguity. Import statements are specified as a <code>String</code> with semicolon-separated statements.
     * <p>
     * The <code>String</code> parameter to this method follows the syntax of the import statement of the Java language.
     *
     * @param imports import statements separated by semicolons.
     * @see javax.jdo.Query
     */
    @Override
    public void declareImports(java.lang.String imports) {
        throw new javax.jdo.JDOUnsupportedOptionException("DbQuery#declareImports()");
    }

    /**
     * Declare the list of parameters query execution.
     * <p>
     * The parameter declaration is a <code>String</code> containing one or more query parameter declarations separated with commas. Each parameter named in the parameter declaration must be bound to
     * a value when the query is executed.
     * <p>
     * The <code>String</code> parameter to this method follows the syntax for formal parameters in the Java language.
     *
     * @param parameters the list of parameters separated by commas.
     * @see javax.jdo.Query
     */
    @Override
    public void declareParameters(java.lang.String parameters) {
        throw new javax.jdo.JDOUnsupportedOptionException("DbQuery#declareParameters()");
    }

    /**
     * Declare the unbound variables to be used in the query. Variables might be used in the filter, and these variables must be declared with their type. The unbound variable declaration is a
     * <code>String</code> containing one or more unbound variable declarations separated with semicolons. It follows the syntax for local variables in the Java language.
     *
     * @param variables the variables separated by semicolons.
     * @see javax.jdo.Query
     */
    @Override
    public void declareVariables(java.lang.String variables) {
        throw new javax.jdo.JDOUnsupportedOptionException("DbQuery#declareVariables()");
    }

    /**
     * Execute a dynamic SELECT query. The execution time of the query will be measured.
     *
     * @see ch.softenvironment.util.Statistic
     */
    private void evaluate() throws SQLException {
        long t0 = Statistic.getTimeMeasure();
        dynamicStatement.execute(query);
        long t1 = Statistic.getTimeMeasure();
        statistic.add(t1 - t0);

        result = dynamicStatement.getResultSet();
        transaction.incrementOpenCursor(true);
        getStatus();
    }

    /**
     * Execute a dynamic DML statement (for e.g. SQL INSERT, UPDATE or DELETE) query and replace its paramters with the given ones.
     */
    private void evaluateUpdate() throws SQLException {
        long t0 = Statistic.getTimeMeasure();
        dynamicStatement.executeUpdate(query);
        long t1 = Statistic.getTimeMeasure();
        statistic.add(t1 - t0);

        getStatus();
        closeAll(); // auto-close of statement
    }

    /**
     * Execute the query and return the filtered Collection.
     *
     * @return the filtered <code>Collection</code>.
     * @see #executeWithArray(Object[] parameters)
     * @see javax.jdo.Query
     * see org.odmg.OQLQuery
     */
    @Override
    public Object execute() {
        try {
            String targetQuery = builder.getQuery();
            if (StringUtils.isNullOrEmpty(targetQuery)) {
                log.info("WARNING: Query suppressed because not supported by DBMS");
                return null;
            } else {
                if (batch && (!transaction.getConnection().getJdbcConnection().getAutoCommit())) {
                    // for JDBC2.0
                    // TODO untested yet
                    dynamicStatement = transaction.getConnection().getJdbcConnection().createStatement();
                    dynamicStatement.addBatch(targetQuery);
                    dynamicStatement.executeBatch();
                } else {
                    create(targetQuery);
                    if (builder.isSelection()) {
                        evaluate();
                        return result;
                    } else {
                        if (builder.isDangerous()) {
                            throw new DeveloperException("Critical Statement for Target-System suppressed because Multi-Data might by corrupted!");
                        } else {
                            evaluateUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            log.error("SQLState={}", e.getSQLState(), e);
            throw new UserException(getResourceString("CW_QueryError"), getResourceString("CT_QueryError"), e);
        }

        return null;
    }

    /**
     * Execute the query and return the filtered <code>Collection</code>.
     *
     * @param p1 the value of the first parameter declared.
     * @return the filtered <code>Collection</code>.
     * @see #executeWithArray(Object[] parameters)
     * @see javax.jdo.Query
     */
    @Override
    public java.lang.Object execute(java.lang.Object p1) {
        throw new javax.jdo.JDOUnsupportedOptionException("DbQuery#execute()");
    }

    /**
     * Execute the query and return the filtered <code>Collection</code>.
     *
     * @param p1 the value of the first parameter declared.
     * @param p2 the value of the second parameter declared.
     * @return the filtered <code>Collection</code>.
     * @see #executeWithArray(Object[] parameters)
     * @see javax.jdo.Query
     */
    @Override
    public java.lang.Object execute(java.lang.Object p1, java.lang.Object p2) {
        throw new javax.jdo.JDOUnsupportedOptionException("DbQuery#execute()");
    }

    /**
     * Execute the query and return the filtered <code>Collection</code>.
     *
     * @param p1 the value of the first parameter declared.
     * @param p2 the value of the second parameter declared.
     * @param p3 the value of the third parameter declared.
     * @return the filtered <code>Collection</code>.
     * @see #executeWithArray(Object[] parameters)
     * @see javax.jdo.Query
     */
    @Override
    public java.lang.Object execute(java.lang.Object p1, java.lang.Object p2, java.lang.Object p3) {
        throw new javax.jdo.JDOUnsupportedOptionException("DbQuery#execute()");
    }

    /**
     * Execute the query and return the filtered <code>Collection</code>.
     *
     * <p>
     * The execution of the query obtains the values of the parameters and matches them against the declared parameters in order. The names of the declared parameters are ignored. The type of the
     * declared parameters must match the type of the passed parameters, except that the passed parameters might need to be unwrapped to get their primitive values.
     *
     * <p>
     * The filter, import, declared parameters, declared variables, and ordering statements are verified for consistency.
     *
     * <p>
     * Each element in the candidate <code>Collection</code> is examined to see that it is assignment compatible to the <code>Class</code> of the query. It is then evaluated by the Boolean expression
     * of the filter. The element passes the filter if there exist unique values for all variables for which the filter expression evaluates to <code>true</code>.
     *
     * @param parameters the <code>Object</code> array with all of the parameters.
     * @return the filtered <code>Collection</code>.
     * @see javax.jdo.Query
     */
    @Override
    public java.lang.Object executeWithArray(java.lang.Object[] parameters) {
        throw new javax.jdo.JDOUnsupportedOptionException("DbQuery#execute()");
    }

    /**
     * Execute the query and return the filtered <code>Collection</code>. The query is executed with the parameters set by the <code>Map</code> values. Each <code>Map</code> entry consists of a key
     * which is the name of the parameter in the <code>declareParameters</code> method, and a value which is the value used in the <code>execute</code> method. The keys in the
     * <code>Map</code> and the declared parameters must exactly match or a
     * <code>JDOUserException</code> is thrown.
     *
     * @param parameters the <code>Map</code> containing all of the parameters.
     * @return the filtered <code>Collection</code>.
     * @see #executeWithArray(Object[] parameters)
     * @see javax.jdo.Query
     */
    @Override
    public java.lang.Object executeWithMap(java.util.Map parameters) {
        throw new javax.jdo.JDOUnsupportedOptionException("DbQuery#execute()");
    }

    /**
     * Get the ignoreCache option setting.
     *
     * @return the ignoreCache option setting.
     * @see #setIgnoreCache
     * @see javax.jdo.Query
     */
    @Override
    public boolean getIgnoreCache() {
        return ignoreCache;
    }

    /**
     * Get the <code>PersistenceManager</code> associated with this
     * <code>Query</code>.
     *
     * <p>
     * If this <code>Query</code> was restored from a serialized form, it has no
     * <code>PersistenceManager</code>, and this method returns
     * <code>null</code>.
     *
     * @return the <code>PersistenceManager</code> associated with this
     *     <code>Query</code>.
     * @see javax.jdo.Query
     */
    @Override
    public javax.jdo.PersistenceManager getPersistenceManager() {
        return transaction.getPersistenceManager();
    }

    /**
     * @deprecated
     */
    private String getResourceString(String property) {
        return ResourceManager.getResource(DbQuery.class, property);
    }

    /**
     * Return the status of last executed query (such as In/Complete).
     *
     * @deprecated
     */
    private int getStatus() throws SQLException {
        if (dynamicStatement != null) {
            SQLWarning warning = dynamicStatement.getWarnings();
            if (warning != null) {
                log.info("Backend-Warning: {}", warning.getLocalizedMessage());
                return warning.getErrorCode();
            }
        }
        if (result != null) {
            SQLWarning warning = result.getWarnings();
            if (warning != null) {
                log.info("Backend-Warning: {}", warning.getLocalizedMessage());
                return warning.getErrorCode();
            }
        }

        return COMPLETE;
    }

    /**
     * Set the candidate <code>Collection</code> to query.
     *
     * @param pcs the candidate <code>Collection</code>.
     * @see javax.jdo.Query
     */
    @Override
    public void setCandidates(java.util.Collection pcs) {
        throw new javax.jdo.JDOUnsupportedOptionException("DbQuery#setCandiates()");
    }

    /**
     * Set the candidate <code>Extent</code> to query.
     *
     * @param pcs the candidate <code>Extent</code>.
     * @see javax.jdo.Query
     */
    @Override
    public void setCandidates(javax.jdo.Extent pcs) {
        throw new javax.jdo.JDOUnsupportedOptionException("DbQuery#setCandiates()");
    }

    /**
     * Set the class of the candidate instances of the query.
     * <p>
     * The class specifies the class of the candidates of the query. Elements of the candidate collection that are of the specified class are filtered before being put into the result
     * <code>Collection</code>.
     *
     * @param cls the <code>Class</code> of the candidate instances.
     * @see javax.jdo.Query
     */
    @Override
    public void setClass(java.lang.Class cls) {
        throw new javax.jdo.JDOUnsupportedOptionException("DbQuery#setClass()");
    }

    /**
     * Set the filter for the query.
     *
     * <p>
     * The filter specification is a <code>String</code> containing a Boolean expression that is to be evaluated for each of the instances in the candidate collection. If the filter is not specified,
     * then it defaults to "true", which has the effect of filtering the input
     * <code>Collection</code> only for class type.
     * <p>
     * An element of the candidate collection is returned in the result if:
     * <ul>
     * <li>it is assignment compatible to the candidate <code>Class</code> of
     * the <code>Query</code>; and
     * <li>for all variables there exists a value for which the filter
     * expression evaluates to <code>true</code>.
     * </ul>
     * <p>
     * The user may denote uniqueness in the filter expression by explicitly
     * declaring an expression (for example, <code>e1 != e2</code>).
     * <p>
     * Rules for constructing valid expressions follow the Java language, except
     * for these differences:
     * <ul>
     * <li>Equality and ordering comparisons between primitives and instances of
     * wrapper classes are valid.
     * <li>Equality and ordering comparisons of <code>Date</code> fields and
     * <code>Date</code> parameters are valid.
     * <li>White space (non-printing characters space, tab, carriage return, and
     * line feed) is a separator and is otherwise ignored.
     * <li>The assignment operators <code>=</code>, <code>+=</code>, etc. and
     * pre- and post-increment and -decrement are not supported. Therefore,
     * there are no side effects from evaluation of any expressions.
     * <li>Methods, including object construction, are not supported, except for
     * <code>Collection.contains(Object o)</code>,
     * <code>Collection.isEmpty()</code>,
     * <code>String.startsWith(String s)</code>, and
     * <code>String.endsWith(String e)</code>. Implementations might choose to
     * support non-mutating method calls as non-standard extensions.
     * <li>Navigation through a <code>null</code>-valued field, which would
     * throw <code>NullPointerException</code>, is treated as if the filter
     * expression returned <code>false</code> for the evaluation of the current
     * set of variable values. Other values for variables might still qualify
     * the candidate instance for inclusion in the result set.
     * <li>Navigation through multi-valued fields (<code>Collection</code>
     * types) is specified using a variable declaration and the
     * <code>Collection.contains(Object o)</code> method.
     * </ul>
     * <p>
     * Identifiers in the expression are considered to be in the name space of
     * the specified class, with the addition of declared imports, parameters
     * and variables. As in the Java language, <code>this</code> is a reserved
     * word which means the element of the collection being evaluated.
     * <p>
     * Navigation through single-valued fields is specified by the Java language
     * syntax of <code>field_name.field_name....field_name</code>.
     * <p>
     * A JDO implementation is allowed to reorder the filter expression for
     * optimization purposes.
     *
     * @param filter the query filter.
     * @see javax.jdo.Query
     */
    @Override
    public void setFilter(java.lang.String filter) {
        throw new javax.jdo.JDOUnsupportedOptionException("Use DbQueryBuilder#addFilter() instead");
    }

    /**
     * Set the ignoreCache option. The default value for this option was set by the <code>PersistenceManagerFactory</code> or the
     * <code>PersistenceManager</code> used to create this <code>Query</code>.
     * <p>
     * The ignoreCache option setting specifies whether the query should execute entirely in the back end, instead of in the cache. If this flag is set to
     * <code>true</code>, an implementation might be able to optimize the query
     * execution by ignoring changed values in the cache. For optimistic transactions, this can dramatically improve query response times.
     *
     * @param ignoreCache the setting of the ignoreCache option.
     * @see javax.jdo.Query
     */
    @Override
    public void setIgnoreCache(boolean ignoreCache) {
        this.ignoreCache = ignoreCache;
    }

    /**
     * Set the ordering specification for the result <code>Collection</code>. The ordering specification is a <code>String</code> containing one or more ordering declarations separated by commas.
     *
     * <p>
     * Each ordering declaration is the name of the field on which to order the results followed by one of the following words: "<code>ascending</code> " or "<code>descending</code>".
     *
     * <p>
     * The field must be declared in the candidate class or must be a navigation expression starting with a field in the candidate class.
     *
     * <p>
     * Valid field types are primitive types except <code>boolean</code>; wrapper types except <code>Boolean</code>; <code>BigDecimal</code>;
     * <code>BigInteger</code>; <code>String</code>; and <code>Date</code>.
     *
     * @param ordering the ordering specification.
     * @see javax.jdo.Query
     */
    @Override
    public void setOrdering(java.lang.String ordering) {
        throw new javax.jdo.JDOUnsupportedOptionException("Use DbQueryBuilder#addOrder()");
    }
}

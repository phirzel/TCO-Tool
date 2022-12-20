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
 */

import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.jomm.datatypes.DbNlsString;
import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbDescriptorEntry;
import ch.softenvironment.jomm.implementation.DbCache;
import ch.softenvironment.jomm.implementation.DbPropertyChange;
import ch.softenvironment.jomm.mvc.model.*;
import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.util.UserException;
import lombok.extern.slf4j.Slf4j;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * DbObjectServer treating all persistent Objects of a specific URL. A DbObjectServer serves as middleware between application and a Target-System (for e.g. DBMS).
 * <p>
 * see org.odmg.Database
 *
 * @author Peter Hirzel
 * @see javax.jdo.PersistenceManager
 */
@Slf4j
public abstract class DbObjectServer implements javax.jdo.PersistenceManager {

	private transient javax.jdo.PersistenceManagerFactory factory;
	private transient DbConnection connection;
	private final transient Class queryBuilder;
	private final transient DbMapper mapper;
	protected transient long temporaryId = -1;

	// cache
	protected transient DbCache codeCache = new DbCache(); // empty yet
	protected transient HashMap nlsCache = new HashMap(); // empty yet
	protected transient DbCache objectCache = new DbCache();// empty yet

	// handle a Transaction-Cycle
	private transient DbCache transactionObjects = null; // objects of one
	// atomar DbTransaction

	// JDO Flags
	private transient boolean ignoreCache = false;
	private transient boolean multithreaded = false;
	private transient Object userObject = null;

	// Data Block fetchsize
	// public static int FETCH_SIZE = 16;

	/**
	 * DbObjectServer constructor.
	 */
	protected DbObjectServer(javax.jdo.PersistenceManagerFactory factory, String password, DbMapper mapper, java.lang.Class queryBuilder,
		java.lang.Class dbConnection) throws Exception {
		super();

		this.factory = factory;
		this.mapper = mapper;
		this.queryBuilder = queryBuilder;

		Class[] types = {DbObjectServer.class};
		Object[] args = {this};
		java.lang.reflect.Constructor constructor = dbConnection.getConstructor(types);
		connection = (DbConnection) constructor.newInstance(args);

		connection.open(factory.getConnectionUserName(), password);
	}

	/**
	 * Return the DescriptorEntry for the property defined by dbObject's DbDescriptor. The property may be defined in any hierarchical level of dbObject.
	 *
	 * @return null if no DbDescriptorEntry found
	 */
	public DbDescriptorEntry getDescriptorEntry(Class<? extends DbObject> dbObject, final String property) {
		Class<? extends DbObject> current = dbObject;

		while ((!current.equals(DbEntityBean.class)) && (!current.equals(DbRelationshipBean.class)) && (!current.equals(DbChangeableBean.class))
			&& (!current.equals(DbCode.class)) && (!current.equals(DbSessionBean.class))) {
			DbDescriptor descriptor = getDescriptor(current);
			DbDescriptorEntry entry = descriptor.getEntry(property);
			if (entry != null) {
				// should not be implemented twicely in hierarchy!
				return entry;
			}

			current = (Class<? extends DbObject>) current.getSuperclass();
		}
		return null;
	}

	/**
	 * Add an additional DbCode to Cache.
	 */
	public synchronized void cacheCode(DbCodeType code) {
		// cache the code
		Collection codes = codeCache.getAll(code.getClass());
		if (codes == null) {
			codes = new ArrayList<DbCodeType>();
		}
		if (!codes.contains(code)) {
			codes.add(code);
			// cacheNlsString(code.getName());
			codeCache.addAll(code.getClass(), codes);
			log.info("cache #" + codes.size() + " of code <" + code + ">");
		}
	}

	/**
	 * Check whether given collection has a next entry.
	 *
	 * @param collection Query-resulSet
	 * @param error if True collection's next is expected
	 * @param method where the expectation of next happened
	 * @return boolean
	 */
	public boolean checkNext(Object collection, boolean error, String method) {
		boolean next = getMapper().hasNext(collection);
		if (!next) {
			if (error) {
				throw new DeveloperException("in: " + method + "=>result expected");
			} // else { /* might be the end of SQL-Cursor*/ }
		}

		return next;
	}

	/**
	 * Check whether the connection to the target-server is still ok. Sometimes timeout-conditions by the server may close an existing session, therefore it might be helpful to check whether the
	 * server is still reachable, before creating any queries.
	 * <p>
	 * This method should be implemented in a fast manner and not be called to often for performance reasons.
	 *
	 * @return true->session ok.
	 */
	public abstract boolean checkConnection();

	/**
	 * Close this <code>PersistenceManager</code> so that no further requests may be made on it. A <code>PersistenceManager</code> instance can be used only until it is closed.
	 *
	 * <p>
	 * Closing a <code>PersistenceManager</code> might release it to the pool of available <code>PersistenceManager</code>s, or might be garbage collected, at the option of the JDO implementation.
	 * Before being used again to satisfy a <code>getPersistenceManager()</code> request, the default values for options will be restored to their values as specified in the
	 * <code>PersistenceManagerFactory</code>.
	 *
	 * <p>
	 * This method closes the <code>PersistenceManager</code>.
	 *
	 * @see javax.jdo.PersistenceManager
	 */
	@Override
	public synchronized final void close() {
		try {
			if (!isClosed()) {
				log.info("closing Target-Server...");

				if (getPersistenceManagerFactory() == null) {
					log.warn("Developer warning: factory is null!");
				} else {
					DbTransaction.tryReset(this /*
					 * getPersistenceManagerFactory()
					 * .getConnectionURL()
					 */);
				}
				if (getConnection() == null) {
					log.warn("Developer warning: connection is null!");
				} else {
					getConnection().close();
				}
				if (getPersistenceManagerFactory() != null) {
					getPersistenceManagerFactory().close();
				}
			}
		} catch (javax.jdo.JDOException e) {
			throw new UserException(ResourceManager.getResource(DbObjectServer.class, "CW_DisconnectionError"),
					ResourceManager.getResource(DbObjectServer.class, "CT_ConnectionError"), e);
		} finally {
			connection = null;
			factory = null;
		}
	}

	/**
	 * Commit or rollback an Open Transaction Thread.
	 * <p>
	 * The given exception must be treated further on by caller!
	 *
	 * @param exception Eventually happened Exception to treat
	 * @param transaction Critical TransactionBlock
	 * @see #openTransactionThread(boolean)
	 * @see DbUserTransactionBlock#execute(Runnable)
	 */
	public final synchronized void closeTransactionThread(Exception exception, DbTransaction transaction) {
		// TODO make protected; use DbUserTransactionBlock
		if (transaction != null) {
			if (exception == null) {
				transaction.commit();
				DbTransaction.leave(this /*
				 * getPersistenceManagerFactory().
				 * getConnectionURL()
				 */);
			} else {
				transaction.rollback();
				DbTransaction.leave(this /*
				 * getPersistenceManagerFactory().
				 * getConnectionURL()
				 */);
				log.warn("Transaction aborted: {}", exception.getClass().getName(), exception);
			}

			if (!transaction.isActive()) {
				/*
				 * if (transactionObjects.getTraceContents().length() > 0) {
				 * Tracer.getInstance().debug(this,
				 * "closeTransactionThread(..)",
				 * "Release Transactional-Objects: " +
				 * transactionObjects.getTraceContents()); }
				 */
				transactionObjects = null;
			}
		}
	}

	/**
	 * Close open Transaction and throw a UserException in case of a forced Rollback.
	 *
	 * @param method Source of possible error
	 * @throws UserException
	 * @see #openTransactionThread(boolean))
	 * @see #closeTransactionThread(Exception, DbTransaction)
	 */
	@Deprecated
	protected void closeTransactionThread(Exception exception, DbTransaction transaction, String method) throws UserException {
		// 1) handle Transaction Cycle
		closeTransactionThread(exception, transaction);

		// 2) forward the exception happened
		if (exception != null) {
			log.warn("", exception);
			if (exception instanceof UserException) {
				// do not nest further
				throw (UserException) exception;
			} else {
				throw new UserException(ResourceManager.getResource(DbObjectServer.class, "CW_TransactionError"),
						ResourceManager.getResource(
								DbObjectServer.class, "CE_TransactionError"), exception);
			}
		}
	}

	/**
	 * @see #createInstance(Class, boolean)
	 */
	public final DbObject createInstance(Class<? extends DbObject> dbClass) throws NoSuchMethodException, InstantiationException, IllegalAccessException,
		java.lang.reflect.InvocationTargetException {
		return createInstance(dbClass, false);
	}

	/**
	 * Return a brand new instance of the given Class in state NEW. Make sure any DbObject's to be created have a protected or private Constructor with DbObjectServer as argument, for e.g. "protected
	 * MyPersistentObject(DbObjectServer server)".
	 *
	 * @param dbClass The type to be instantiated
	 * @param shouldExist true=>id available (potentially persistent); false=>only transient yet
	 */
	public synchronized final DbObject createInstance(Class<? extends DbObject> dbClass, boolean shouldExist) throws NoSuchMethodException,
		InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException {
		Class[] types = {DbObjectServer.class};
		Object[] args = {this};
		java.lang.reflect.Constructor constructor = dbClass.getDeclaredConstructor(types); // Constructor(types);
		constructor.setAccessible(true); // allow access to protected
		// constructor
		DbObject object = ((DbObject) constructor.newInstance(args));

		if ((!shouldExist) && (object instanceof DbChangeableBean)) {
			// TODO NYI: evtl. keep new transient DbObject's in mind
			setUniqueId(object, getTemporaryNewId());
			/*
			 * if (dbClass.equals(DbCodeType.class)) {
			 *
			 * }
			 */
		}
		return object;
	}

	/**
	 * Factory Method to create new DbQueryBuilder instance.
	 *
	 * @return specific QueryBuilder
	 */
	public DbQueryBuilder createQueryBuilder(final int queryType, String useCase) {
		if (queryBuilder == null) {
			throw new DeveloperException("no DbQueryBuilder registered in concrete DbDomainNameServer");
		}
		try {
			Class[] types = {DbObjectServer.class, Integer.class, String.class};
			Object[] args = {this, Integer.valueOf(queryType), useCase};
			java.lang.reflect.Constructor constructor = queryBuilder.getConstructor(types);
			DbQueryBuilder builder = ((DbQueryBuilder) constructor.newInstance(args));

			return builder;
		} catch (Exception e) {
			throw new DeveloperException("DbQueryBuilder problem", null, e);
		}
	}

	/**
	 * Return a new TransactionBlock.
	 *
	 * @param ownThread true->open a new parallel Thread; false allow nesting if open Transaction exists
	 */
	public synchronized final DbUserTransactionBlock createUserTransactionBlock(boolean ownThread) {
		return new DbUserTransactionBlock(this, ownThread);
	}

	/**
	 * Return the <code>Transaction</code> instance associated with a
	 * <code>PersistenceManager</code>. There is one <code>Transaction</code>
	 * instance associated with each <code>PersistenceManager</code> instance. The <code>Transaction</code> instance supports options as well as transaction completion requests.
	 *
	 * @return the <code>Transaction</code> associated with this
	 *     <code>PersistenceManager</code>.
	 * @see javax.jdo.PersistenceManager
	 */
	@Override
	public synchronized javax.jdo.Transaction currentTransaction() {
		return DbTransaction.getCurrentTransaction(this /*
		 * getPersistenceManagerFactory
		 * ().getConnectionURL()
		 */);
	}

	/**
	 * Delete an array of instances from the data store.
	 *
	 * @param pcs a <code>Collection</code> of persistent instances
	 * @see #deletePersistent(Object pc)
	 */
	@Override
	public void deletePersistentAll(java.lang.Object[] pcs) {
		try {
			DbTransaction trans = null;
			Exception exception = null;
			try {
				trans = openTransactionThread(false);
				for (int i = 0; i < pcs.length; i++) {
					deletePersistent(pcs[i]);
				}
			} catch (Exception e) {
				exception = e;
			}
			closeTransactionThread(exception, trans, "execute(DbQueryBuilder)");
		} catch (Exception e) {
			throw new UserException(ResourceManager.getResource(DbObjectServer.class, "CW_DeletionErrorMany"), ResourceManager.getResource(
				DbObjectServer.class, "CT_DeletionError"), e);
		}
	}

	/**
	 * Delete a <code>Collection</code> of instances from the data store.
	 *
	 * @param pcs a <code>Collection</code> of persistent instances
	 * @see #deletePersistent(Object pc)
	 */
	@Override
	public final void deletePersistentAll(java.util.Collection pcs) {
		deletePersistentAll(pcs.toArray());
	}

	/**
	 * Mark an instance as no longer needed in the cache. Eviction is normally done automatically by the <code>PersistenceManager</code> at transaction completion. This method allows the application
	 * to explicitly provide a hint to the <code>PersistenceManager</code> that the instance is no longer needed in the cache.
	 *
	 * @param pc the instance to evict from the cache.
	 * @see javax.jdo.PersistenceManager
	 */
	@Override
	public synchronized void evict(java.lang.Object pc) {
		DbObject object = (DbObject) pc;

		// if (!object.jdoIsTransactional()) {
		if (pc instanceof DbEnumeration) {
			log.warn("Developer warning: Cannot uncache DbEnumeration=>needs a restart to change");
		} else if (pc instanceof DbCode /* Type */) {
			// uncache all codes of this type
			codeCache.uncache(object.getClass());
		} else if (pc instanceof DbNlsString) {
			nlsCache.remove(((DbNlsString) pc).getId());
		} else {
			objectCache.uncache(object);
		}
		// }
	}

	/**
	 * Mark all persistent-nontransactional instances as no longer needed in the cache. It transitions all persistent-nontransactional instances to hollow. Transactional instances are subject to
	 * eviction based on the RetainValues setting.
	 *
	 * @see #evict(Object pc)
	 * @see javax.jdo.PersistenceManager
	 */
	@Override
	public synchronized void evictAll() {
		// if (currentTransaction() == null) {
		// TODO NYI: transition po from <persistent-nontransactional> to
		// <hollow>
		codeCache = new DbCache();
		nlsCache = new HashMap();
		objectCache = new DbCache();
		// }
	}

	/**
	 * Mark an array of instances as no longer needed in the cache.
	 *
	 * @param pcs the array of instances to evict from the cache.
	 * @see #evict(Object pc)
	 * @see javax.jdo.PersistenceManager
	 */
	@Override
	public void evictAll(java.lang.Object[] pcs) {
		for (int i = 0; i < pcs.length; i++) {
			evict(pcs[i]);
		}
	}

	/**
	 * Mark a <code>Collection</code> of instances as no longer needed in the cache.
	 *
	 * @param pcs the <code>Collection</code> of instances to evict from the cache.
	 * @see #evict(Object pc)
	 * @see javax.jdo.PersistenceManager
	 */
	@Override
	public void evictAll(java.util.Collection pcs) {
		evictAll(pcs.toArray());
	}

	/**
	 * Execute a Query given by builder in an own Transaction-Block.
	 */
	public void execute(DbQueryBuilder builder) {
		DbTransaction trans = null;
		Exception exception = null;
		try {
			trans = openTransactionThread(false);
			execute(trans, builder);
		} catch (Exception e) {
			// Tracer.getInstance().runtimeError(this,
			// "execute(DbQueryBuilder)", "Statement failed: [" +
			// builder.getQuery() + "]");
			exception = e;
		}
		closeTransactionThread(exception, trans, "execute(DbQueryBuilder)");
	}

	/**
	 * Execute a Query defined by given builder.
	 *
	 * @param trans TransactionBlock to execute Query in.
	 */
	protected void execute(DbTransaction trans, DbQueryBuilder builder) {
		DbQuery query = new DbQuery(trans, builder);
		query.execute();

		if (builder.isSelection()) {
			log.warn("Developer warning: Cursor closing not guaranteed by Db-Framework!");
		}
	}

	/**
	 * Execute a list of Query-Statements against a backend-server (for e.g. SQL DML or DDL). The whole list is executed in ONE Transaction-Block.
	 * <p>
	 * Do not add SQL DQL statements to the list, otherwise open Cursor State will fall into a bad mood.
	 *
	 * @param useCase Description of list-queries over all
	 * @param list containing proper Statements defined by Strings (for e.g. "DELETE FROM X WHERE ...")
	 */
	public void execute(String useCase, List<String> list) throws Exception {
		if (list.size() > 0) {
			Exception exception = null;
			DbTransaction trans = null;
			try {
				trans = openTransactionThread(false);

				Iterator<String> it = list.iterator();
				int index = 0;
				while (it.hasNext()) {
					// convert String-queries to builder-queries
					DbQueryBuilder builder = createQueryBuilder(DbQueryBuilder.RAW, useCase + "[" + index++ + "]");
					if (builder.isSelection()) {
						throw new DeveloperException("SQL DQL statements not allowed in list");
					}
					builder.setRaw(it.next());
					execute(trans, builder);
				}
			} catch (Exception e) {
				exception = e;
			}
			closeTransactionThread(exception, trans, "execute(UseCase=" + useCase + ", List)");
		}
	}

	/**
	 * Return the first nrOfColumns resulted by a Query. The resultSet is assumed to contain at [1..nrOfColumns] answer Columns with [1..*] rows.
	 * <p>
	 * For e.g. SELECT SUM(FieldA), SUM(FieldB) FROM ...
	 *
	 * @return List each item represent a List itself with n column-values
	 */
	public java.util.List<List<Object>> getAllValues(DbQueryBuilder builder, int nrOfColumns) {
		java.util.List<List<Object>> values = new ArrayList<List<Object>>();
		DbTransaction trans = null;
		try {
			// TODO use DbUserTransactionBlock
			trans = DbTransaction.join(this /*
			 * getPersistenceManagerFactory().
			 * getConnectionURL()
			 */);
			trans.begin();
			DbQuery query = new DbQuery(trans, builder);
			ResultSet queryResult = (ResultSet) query.execute();
			while (queryResult.next()) {
				List<Object> entry = new ArrayList<Object>(nrOfColumns);
				for (int i = 0; i < nrOfColumns; i++) {
					entry.add(queryResult.getObject(i + 1));
				}
				values.add(entry);
			}
			query.closeAll();
		} catch (Exception e) {
			// resultSet will be closed by GarbageCollector
			log.error("Returned Values might be incorrect!", e);
		} finally {
			if (trans != null) {
				trans.commit(); // nothing to Rollback here
			}
			DbTransaction.leave(this /*
			 * getPersistenceManagerFactory().
			 * getConnectionURL()
			 */);
		}
		return values;
	}

	/**
	 * Return the target-name (for e.g. a Database Attribute) corresponding to given Java property (=> name mapping between Java and Target-System).
	 *
	 * @return String
	 * @see DbDescriptor#getAttribute(DbObjectServer, String)
	 */
	public final String getAttribute(Class<? extends DbObject> dbObject, String property) {
		return getDescriptor(dbObject).getAttribute(this, property);
	}

	/**
	 * Return Object if cached, otherwise null.
	 */
	protected DbObject getCachedObject(java.lang.Class<? extends DbObject> dbObject, Long id) {
		return objectCache.get(dbObject, id);
	}

	/**
	 * Return Connection.
	 *
	 * @return The established Session.
	 */
	public DbConnection getConnection() {
		// HACK: should be abstract here
		return connection;
	}

	/**
	 * @see DbConnection#getDbObject(String)
	 */
	public final Class getDbObject(String targetName) throws ClassNotFoundException {
		try {
			return getConnection().getDbObject(targetName);
		} catch (DeveloperException e) {
			return Class.forName(targetName);
		}
	}

	/**
	 * Convenience method.
	 *
	 * @see DbConnection
	 */
	public DbDescriptor getDescriptor(java.lang.Class<? extends DbObject> dbObject) {
		return getConnection().getDescriptor(dbObject);
	}

	/**
	 * The <code>PersistenceManager</code> manages a collection of instances in the data store based on the class of the instances. This method returns an <code>Extent</code> of instances in the data
	 * store that might be iterated or given to a <code>Query</code>. The <code>Extent</code> itself might not reference any instances, but only hold the class name and an indicator as to whether
	 * subclasses are included in the
	 * <code>Extent</code>.
	 * <p>
	 * Note that the <code>Extent</code> might be very large.
	 *
	 * @param persistenceCapableClass <code>Class</code> of instances
	 * @param subclasses whether to include instances of subclasses
	 * @return an <code>Extent</code> of the specified <code>Class</code>
	 * @see javax.jdo.Query
	 */
	@Override
	public javax.jdo.Extent getExtent(java.lang.Class persistenceCapableClass, boolean subclasses) {
		// @see getParentDescriptor(..)
		throw new javax.jdo.JDOUnsupportedOptionException("DbObjectServer#getExtent()");
	}

	/**
	 * Return the first value of a Query. The resultSet is assumed to contain at least 1 answer Column with 1 row.
	 * <p>
	 * For e.g. SELECT T_Type FROM SEBULegalEntity WHERE T_Id=13 or SELECT COUNT(*) FROM...
	 */
	public Object getFirstValue(DbQueryBuilder builder) {
		java.util.List<Object> values = getFirstValues(builder);
		if (values.size() > 0) {
			return values.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Return the first values resulted by a Query. The resultSet is assumed to contain at least 1 answer Column with [1..*] rows.
	 * <p>
	 * For e.g. SELECT T_Id FROM T_Map...
	 */
	public java.util.List<Object> getFirstValues(DbQueryBuilder builder) {
		java.util.List<Object> values = new ArrayList<Object>();
		DbTransaction trans = null;
		DbQuery query = null;
		try {
			// TODO use DbUserTransactionBlock
			trans = DbTransaction.join(this /*
			 * getPersistenceManagerFactory().
			 * getConnectionURL()
			 */);
			trans.begin();
			query = new DbQuery(trans, builder);
			ResultSet queryResult = (ResultSet) query.execute();
			while (queryResult.next()) {
				values.add(queryResult.getObject(1));
			}
			query.closeAll();
			query = null;
		} catch (Exception e) {
			try {
				if (query != null) {
					query.closeAll();
				}
			} catch (Exception qe) {
				log.error("Query not closable!", qe);
			}
			log.error("Returned Values might be incorrect!", e);
		} finally {
			trans.commit(); // nothing to Rollback here
			DbTransaction.leave(this /*
			 * getPersistenceManagerFactory().
			 * getConnectionURL()
			 */);
		}
		return values;
	}

	/**
	 * Get the ignoreCache setting for queries.
	 *
	 * <p>
	 * IgnoreCache set to <code>true</code> specifies that for all
	 * <code>Query</code> instances created by this
	 * <code>PersistenceManager</code>, the default is the cache should be
	 * ignored for queries.
	 *
	 * @return the ignoreCache setting.
	 * @see javax.jdo.PersistenceManager
	 */
	@Override
	public boolean getIgnoreCache() {
		return ignoreCache;
	}

	/**
	 * Return a DbEnumeration with specific IliCode.
	 *
	 * @see DbEnumeration#isIliCode(DbCodeType, String)
	 */
	public final DbEnumeration getIliCode(Class<? extends DbEnumeration> dbEnumertion, final String ilicode) {
		Iterator<? extends DbCodeType> iterator = retrieveCodes(dbEnumertion).iterator();
		while (iterator.hasNext()) {
			DbCodeType enumeration = iterator.next();
			if (DbEnumeration.isIliCode(enumeration, ilicode)) {
				return (DbEnumeration) enumeration;
			}
		}
		log.warn("DbEnumeration: {} with IliCode={} not found", dbEnumertion.getName(), ilicode);
		return null;
	}

	/**
	 * Return the next free incremental value for a key.
	 *
	 * @see #getTemporaryNewId()
	 */
	public synchronized Long getIncrementalId(String key) throws Exception {
		Long id = null;

		DbTransaction trans = null;
		Exception exception = null;
		try {
			trans = openTransactionThread(false);

			id = getMapper().getNewId(this, trans, key);
		} catch (Exception e) {
			exception = e;
		}
		closeTransactionThread(exception, trans, "getIncrementalId(String)");

		return id;
	}

	/**
	 * Return the Mapper for the involved Target-System.
	 */
	public DbMapper getMapper() {
		return mapper;
	}

	/**
	 * Return Database Schema-Metadata.
	 */
	public DatabaseMetaData getMetaData() throws SQLException {
		return getConnection().getJdbcConnection().getMetaData();
	}

	/**
	 * Get the current Multithreaded flag for this
	 * <code>PersistenceManager</code>.
	 *
	 * @return the Multithreaded setting.
	 * @see #setMultithreaded
	 * @see javax.jdo.PersistenceManager
	 */
	@Override
	public boolean getMultithreaded() {
		return multithreaded;
	}

	/**
	 * Return NLS-String with given id from NLS-Table for the Locale-Default Language.
	 */
	public DbNlsString getNlsString(Long id) {
		return getNlsString(id, Locale.getDefault());
	}

	/**
	 * Return NLS-String with given id from NLS-Table for the given Locale.
	 */
	public DbNlsString getNlsString(Long id, Locale locale) {
		if (id == null) {
			// no Instance aggregated yet
			return null;
		} else {
			// try cache
			return (DbNlsString) nlsCache.get(id);
		}
	}

	/**
	 * This method locates a persistent instance in the cache of instances managed by this <code>PersistenceManager</code>. The
	 * <code>getObjectById</code> method attempts to find an instance in the
	 * cache with the specified JDO identity. The <code>oid</code> parameter object might have been returned by an earlier call to
	 * <code>getObjectId</code> or <code>getTransactionalObjectId</code>, or
	 * might have been constructed by the application.
	 * <p>
	 * If the <code>PersistenceManager</code> is unable to resolve the
	 * <code>oid</code> parameter to an ObjectId instance, then it throws a
	 * <code>JDOUserException</code>.
	 * <p>
	 * If the <code>validate</code> flag is <code>false</code>, and there is already an instance in the cache with the same JDO identity as the
	 * <code>oid</code> parameter, then this method returns it. There is no
	 * change made to the state of the returned instance.
	 * <p>
	 * If there is not an instance already in the cache with the same JDO identity as the <code>oid</code> parameter, then this method creates an instance with the specified JDO identity and returns
	 * it. If there is no transaction in progress, the returned instance will be hollow or persistent-nontransactional, at the choice of the implementation.
	 * <p>
	 * If there is a transaction in progress, the returned instance will be hollow, persistent-nontransactional, or persistent-clean, at the choice of the implementation.
	 * <p>
	 * It is an implementation decision whether to access the data store, if required to determine the exact class. This will be the case of inheritance, where multiple <code>PersistenceCapable</code>
	 * classes share the same ObjectId class.
	 * <p>
	 * If the validate flag is <code>false</code>, and the instance does not exist in the data store, then this method might not fail. It is an implementation choice whether to fail immediately with
	 * a
	 * <code>JDODataStoreException</code>. But a subsequent access of the fields
	 * of the instance will throw a <code>JDODataStoreException</code> if the instance does not exist at that time. Further, if a relationship is established to this instance, then the transaction in
	 * which the association was made will fail.
	 * <p>
	 * If the <code>validate</code> flag is <code>true</code>, and there is already a transactional instance in the cache with the same JDO identity as the <code>oid</code> parameter, then this method
	 * returns it. There is no change made to the state of the returned instance.
	 * <p>
	 * If there is an instance already in the cache with the same JDO identity as the <code>oid</code> parameter, but the instance is not transactional, then it must be verified in the data store. If
	 * the instance does not exist in the datastore, then a <code>JDODataStoreException</code> is thrown.
	 * <p>
	 * If there is not an instance already in the cache with the same JDO identity as the <code>oid</code> parameter, then this method creates an instance with the specified JDO identity, verifies
	 * that it exists in the data store, and returns it. If there is no transaction in progress, the returned instance will be hollow or persistent-nontransactional, at the choice of the
	 * implementation.
	 * <p>
	 * If there is a data store transaction in progress, the returned instance will be persistent-clean. If there is an optimistic transaction in progress, the returned instance will be
	 * persistent-nontransactional.
	 * <p>
	 * For e.g. DbCodeType code = getObjectById(new DbObjectId(MyCodeType.class, Long.valueOf(15)), false); MyConcreteClass mc = (MyConcreteClass)getObjectById(new DbObjectId(MyAbstract.class, new
	 * Long(15)), false); // returns real inherited instance
	 *
	 * @param oid an ObjectId
	 * @param validate if the existence of the instance is to be validated
	 * @return the <code>PersistenceCapable</code> instance with the specified ObjectId
	 * @see #getObjectId(Object pc)
	 * @see #getTransactionalObjectId(Object pc)
	 * @see javax.jdo.PersistenceManager
	 */
	@Override
	public java.lang.Object getObjectById(java.lang.Object oid, boolean validate) {
		return getObjectById(oid, validate, false);
	}

	/**
	 * Tune re-Reading by forcing read Objects being put into cache if (cached is true).
	 *
	 * @deprecated
	 */
	public java.lang.Object getObjectById(java.lang.Object oid, boolean validate, boolean cached) {
		// HACK: only relevant for SQL-Target
		throw new DeveloperException("overwrite this method");
	}

	/**
	 * The ObjectId returned by this method represents the JDO identity of the instance. The ObjectId is a copy (clone) of the internal state of the instance, and changing it does not affect the JDO
	 * identity of the instance.
	 * <p>
	 * The <code>getObjectId</code> method returns an ObjectId instance that represents the object identity of the specified JDO instance. The identity is guaranteed to be unique only in the context
	 * of the JDO
	 * <code>PersistenceManager</code> that created the identity, and only for
	 * two types of JDO Identity: those that are managed by the application, and those that are managed by the data store.
	 * <p>
	 * If the object identity is being changed in the transaction, by the application modifying one or more of the application key fields, then this method returns the identity as of the beginning of
	 * the transaction. The value returned by <code>getObjectId</code> will be different following <code>afterCompletion</code> processing for successful transactions.
	 * <p>
	 * Within a transaction, the ObjectId returned will compare equal to the ObjectId returned by only one among all JDO instances associated with the
	 * <code>PersistenceManager</code> regardless of the type of ObjectId.
	 * <p>
	 * The ObjectId does not necessarily contain any internal state of the instance, nor is it necessarily an instance of the class used to manage identity internally. Therefore, if the application
	 * makes a change to the ObjectId instance returned by this method, there is no effect on the instance from which the ObjectId was obtained.
	 * <p>
	 * The <code>getObjectById</code> method can be used between instances of
	 * <code>PersistenceManager</code> of different JDO vendors only for
	 * instances of persistence capable classes using application-managed (primary key) JDO identity. If it is used for instances of classes using datastore identity, the method might succeed, but
	 * there are no guarantees that the parameter and return instances are related in any way.
	 *
	 * @param pc the <code>PersistenceCapable</code> instance
	 * @return the ObjectId of the instance
	 * @see #getTransactionalObjectId(Object pc)
	 * @see #getObjectById(Object oid, boolean validate)
	 * @see javax.jdo.PersistenceManager
	 */
	@Override
	public java.lang.Object getObjectId(java.lang.Object pc) {
		return javax.jdo.JDOHelper.getObjectId(pc);
	}

	/**
	 * Return the <code>Class</code> that implements the JDO Identity for the specified <code>PersistenceCapable</code> class. The application can use the returned <code>Class</code> to construct a
	 * JDO Identity instance for application identity <code>PersistenceCapable</code> classes. This JDO Identity instance can then be used to get an instance of the
	 * <code>PersistenceCapable</code> class for use in the application.
	 *
	 * <p>
	 * In order for the application to construct an instance of the ObjectId class it needs to know the class being used by the JDO implementation.
	 *
	 * @param cls the <code>PersistenceCapable Class</code>
	 * @return the <code>Class</code> of the ObjectId of the parameter
	 * @see #getObjectById
	 */
	@Override
	public java.lang.Class getObjectIdClass(java.lang.Class cls) {
		return DbObjectId.class;
	}

	/**
	 * Return the Descriptor of the parent class mapped by given descriptor.
	 */
	public DbDescriptor getParentDescriptor(DbDescriptor descriptor) {
		return getConnection().getDescriptor((Class<? extends DbObject>) descriptor.getMappedClass().getSuperclass());
	}

	/**
	 * This method returns the <code>PersistenceManagerFactory</code> used to create this <code>PersistenceManager</code>.
	 *
	 * @return the <code>PersistenceManagerFactory</code> that created this
	 *     <code>PersistenceManager</code>
	 */
	@Override
	public javax.jdo.PersistenceManagerFactory getPersistenceManagerFactory() {
		return factory;
	}

	/**
	 * @see DbConnection#getTargetNameFor(Class)
	 */
	public final java.lang.String getTargetName(Class dbObject) {
		return getConnection().getTargetNameFor(dbObject);
	}

	/**
	 * Even DbObject's in persistencyState==NEW (transient) should uniquely be identified, therefore set a transient temporary-ID until saved persistently. Temporary-Id's are given by a certain
	 * DbObjectServer and are by default within a negative range here, on the contrary to really persistent Id's which are in positive range.
	 *
	 * @see DbObjectServer#setUniqueId(Object, Long)
	 * @deprecated
	 */
	protected final Long getTemporaryNewId() {
		// TODO use the very same id instead of state-dependend id's for same
		// DbObject
		return Long.valueOf(temporaryId--);
	}

	/**
	 * The ObjectId returned by this method represents the JDO identity of the instance. The ObjectId is a copy (clone) of the internal state of the instance, and changing it does not affect the JDO
	 * identity of the instance.
	 * <p>
	 * If the object identity is being changed in the transaction, by the application modifying one or more of the application key fields, then this method returns the current identity in the
	 * transaction.
	 * <p>
	 * If there is no transaction in progress, or if none of the key fields is being modified, then this method will return the same value as
	 * <code>getObjectId</code>.
	 *
	 * @param pc a <code>PersistenceCapable</code> instance
	 * @return the ObjectId of the instance
	 * @see #getObjectId(Object pc)
	 * @see #getObjectById(Object oid, boolean validate)
	 */
	@Override
	public java.lang.Object getTransactionalObjectId(java.lang.Object pc) {
		return javax.jdo.JDOHelper.getTransactionalObjectId(pc);
	}

	/**
	 * Return the LoginId of the database connection. Shortcut.
	 */
	public String getUserId() {
		if (getPersistenceManagerFactory() != null) {
			return getPersistenceManagerFactory().getConnectionUserName();
		} else {
			log.warn("Developer warning: PersistenceManager lost!");
			return "<UNKNOWN>";
		}
	}

	/**
	 * The application can manage the <code>PersistenceManager</code> instances more easily by having an application object associated with each
	 * <code>PersistenceManager</code> instance.
	 *
	 * @return the user object associated with this
	 *     <code>PersistenceManager</code>
	 * @see #setUserObject
	 * @see javax.jdo.PersistenceManager
	 */
	@Override
	public java.lang.Object getUserObject() {
		return userObject;
	}

	/**
	 * A <code>PersistenceManager</code> instance can be used until it is closed.
	 *
	 * @return <code>true</code> if this <code>PersistenceManager</code> has
	 *     been closed.
	 * @see #close()
	 * @see javax.jdo.PersistenceManager
	 */
	@Override
	public boolean isClosed() {
		return getConnection() == null;
	}

	/**
	 * Return whether given Object is treated by a current Transaction.
	 */
	public boolean isTransactional(DbObject object) {
		if (transactionObjects != null) {
			return transactionObjects.get(object.getClass(), object.getId()) != null;
		}
		return false;
	}

	/**
	 * Make an instance non-transactional after commit.
	 *
	 * <p>
	 * Normally, at transaction completion, instances are evicted from the cache. This method allows an application to identify an instance as not being evicted from the cache at transaction
	 * completion. Instead, the instance remains in the cache with nontransactional state.
	 *
	 * @param pc the instance to make nontransactional.
	 */
	@Override
	public void makeNontransactional(java.lang.Object pc) {
		new javax.jdo.JDOUnsupportedOptionException("DbObjectServer#makeNonTransactional(Object)");
	}

	/**
	 * Make an array of instances non-transactional after commit.
	 *
	 * @param pcs the array of instances to make nontransactional.
	 * @see #makeNontransactional(Object pc)
	 */
	@Override
	public void makeNontransactionalAll(java.lang.Object[] pcs) {
		for (int i = 0; i < pcs.length; i++) {
			makeNontransactional(pcs[i]);
		}
	}

	/**
	 * Make a <code>Collection</code> of instances non-transactional after commit.
	 *
	 * @param pcs the <code>Collection</code> of instances to make nontransactional.
	 * @see #makeNontransactional(Object pc)
	 */
	@Override
	public void makeNontransactionalAll(java.util.Collection pcs) {
		makeNontransactionalAll(pcs.toArray());
	}

	/**
	 * Called by {@link #makePersistent(Object)}. Implement the definitive save to target code here.
	 *
	 * @param trans
	 * @param po
	 */
	protected abstract void allocate(DbTransaction trans, DbChangeableBean po) throws Exception;

	/**
	 * Make the transient instance persistent in this
	 * <code>PersistenceManager</code>. This method must be called in an active
	 * transaction. The <code>PersistenceManager</code> assigns an ObjectId to the instance and transitions it to persistent-new. The instance will be managed in the <code>Extent</code> associated
	 * with its <code>Class</code> . The instance will be put into the data store at commit. The closure of instances of <code>PersistenceCapable</code> classes reachable from persistent fields will
	 * be made persistent at commit. [This is known as persistence by reachability.]
	 *
	 * @param pc a transient instance of a <code>Class</code> that implements
	 *     <code>PersistenceCapable</code>
	 * @see javax.jdo.PersistenceManager#makePersistent(Object)
	 * see org.odmg.Database#makePersistent(Object)
	 */
	@Override
	public final Object makePersistent(Object pc) throws UserException {
		DbChangeableBean object = (DbChangeableBean) pc;

		DbTransaction trans = null;
		Exception exception = null;
		try {
			trans = openTransactionThread(false);

			if (transactionObjects.get(object.getClass(), object.getId()) == null) {
				// PREVENT recursive allocations if associations point back to
				// given object
				transactionObjects.put(object.getClass(), object);

				allocate(trans, object);

				object.reset(true);
			} // else
			// Tracer.getInstance().debug("suppress recursive allocation for <"
			// + object.getClass().getName() + "->Id=" + object.getId());
		} catch (Exception e) {
			exception = e;
		}

		closeTransactionThread(exception, trans, "makePersistent(Object)");

		//TODO HIP just added to compile
		return pc;
	}

	/**
	 * Make an array of instances persistent.
	 *
	 * @param pcs an array of transient instances
	 * @see #makePersistent(Object pc)
	 */
	@Override
	public java.lang.Object[] makePersistentAll(java.lang.Object[] pcs) {
		java.util.List list = new ArrayList(pcs.length);
		Collections.addAll(list, pcs);
		makePersistentAll(list);

		//TODO HIP just added to compile
		return pcs;
	}

	/**
	 * Make a <code>Collection</code> of instances persistent.
	 *
	 * @param pcs a <code>Collection</code> of transient instances
	 * @see #makePersistent(Object pc)
	 */
	@Override
	public Collection makePersistentAll(java.util.Collection pcs) {
		try {
			DbTransaction trans = null;
			Exception exception = null;
			try {
				trans = openTransactionThread(false);
				Iterator iterator = pcs.iterator();
				while (iterator.hasNext()) {
					makePersistent(iterator.next());
				}
			} catch (Exception e) {
				exception = e;
			}
			closeTransactionThread(exception, trans, "makePersistentAll(Collection)");
		} catch (Exception e) {
			throw new UserException(ResourceManager.getResource(DbObjectServer.class, "CW_SaveError"), ResourceManager.getResource(DbObjectServer.class,
				"CE_SaveError"), e);
		}

		//TODO HIP just added to compile
		return pcs;
	}

	/**
	 * Make an instance subject to transactional boundaries.
	 *
	 * <p>
	 * Transient instances normally do not observe transaction boundaries. This method makes transient instances sensitive to transaction completion. If an instance is modified in a transaction, and
	 * the transaction rolls back, the state of the instance is restored to the state before the first change in the transaction.
	 *
	 * <p>
	 * For persistent instances read in optimistic transactions, this method allows the application to make the state of the instance part of the transactional state. At transaction commit, the state
	 * of the instance in the cache is compared to the state of the instance in the data store. If they are not the same, then an exception is thrown.
	 *
	 * @param pc the instance to make transactional.
	 */
	@Override
	public void makeTransactional(java.lang.Object pc) {
		new javax.jdo.JDOUnsupportedOptionException("DbObjectServer#makeTransactional(Object)");
	}

	/**
	 * Make an array of instances subject to transactional boundaries.
	 *
	 * @param pcs the array of instances to make transactional.
	 * @see #makeTransactional(Object pc)
	 */
	@Override
	public void makeTransactionalAll(java.lang.Object[] pcs) {
		for (int i = 0; i < pcs.length; i++) {
			makeTransactional(pcs[i]);
		}
	}

	/**
	 * Make a <code>Collection</code> of instances subject to transactional boundaries.
	 *
	 * @param pcs the <code>Collection</code> of instances to make transactional.
	 * @see #makeTransactional(Object pc)
	 */
	@Override
	public void makeTransactionalAll(java.util.Collection pcs) {
		makeTransactional(pcs.toArray());
	}

	/**
	 * Make an instance transient, removing it from management by this
	 * <code>PersistenceManager</code>.
	 *
	 * <p>
	 * The instance loses its JDO identity and it is no longer associated with any <code>PersistenceManager</code>. The state of fields is preserved unchanged.
	 *
	 * @param pc the instance to make transient.
	 */
	@Override
	public void makeTransient(java.lang.Object pc) {
		new javax.jdo.JDOUnsupportedOptionException("DbObjectServer#makeTransient(Object)");
	}

	/**
	 * Make an array of instances transient, removing them from management by this <code>PersistenceManager</code>.
	 *
	 * <p>
	 * The instances lose their JDO identity and they are no longer associated with any <code>PersistenceManager</code>. The state of fields is preserved unchanged.
	 *
	 * @param pcs the instances to make transient.
	 */
	@Override
	public void makeTransientAll(java.lang.Object[] pcs) {
		for (int i = 0; i < pcs.length; i++) {
			makeTransient(pcs[i]);
		}
	}

	/**
	 * Make a <code>Collection</code> of instances transient, removing them from management by this <code>PersistenceManager</code>.
	 *
	 * <p>
	 * The instances lose their JDO identity and they are no longer associated with any <code>PersistenceManager</code>. The state of fields is preserved unchanged.
	 *
	 * @param pcs the instances to make transient.
	 */
	@Override
	public void makeTransientAll(java.util.Collection pcs) {
		makeTransactionalAll(pcs.toArray());
	}

	/**
	 * This method returns an object id instance corresponding to the
	 * <code>Class</code> and <code>String</code> arguments. The
	 * <code>String</code> argument might have been the result of executing
	 * <code>toString</code> on an object id instance.
	 *
	 * @param pcClass the <code>Class</code> of the persistence-capable instance
	 * @param str the <code>String</code> form of the object id
	 * @return an instance of the object identity class
	 */
	public java.lang.Object newObjectIdInstance(java.lang.Class pcClass, java.lang.String str) {
		return new DbObjectId(pcClass, Long.valueOf(str));
	}

	/**
	 * Create a new <code>Query</code> with no elements.
	 *
	 * @return the new <code>Query</code>.
	 */
	@Override
	public javax.jdo.Query newQuery() {
		throw new javax.jdo.JDOUnsupportedOptionException("newQuery(*)");
	}

	/**
	 * Create a new <code>Query</code> specifying the <code>Class</code> of the candidate instances.
	 *
	 * @param cls the <code>Class</code> of the candidate instances
	 * @return the new <code>Query</code>
	 */
	@Override
	public javax.jdo.Query newQuery(java.lang.Class cls) {
		throw new javax.jdo.JDOUnsupportedOptionException("newQuery(*)");
	}

	/**
	 * Create a new <code>Query</code> with the <code>Class</code> of the candidate instances and filter.
	 *
	 * @param cls the <code>Class</code> of results
	 * @param filter the filter for candidate instances
	 * @return the new <code>Query</code>
	 */
	@Override
	public javax.jdo.Query newQuery(java.lang.Class cls, java.lang.String filter) {
		throw new javax.jdo.JDOUnsupportedOptionException("newQuery(*)");
	}

	/**
	 * Create a new <code>Query</code> with the candidate <code>Class</code> and
	 * <code>Collection</code>.
	 *
	 * @param cls the <code>Class</code> of results
	 * @param cln the <code>Collection</code> of candidate instances
	 * @return the new <code>Query</code>
	 */
	@Override
	public javax.jdo.Query newQuery(java.lang.Class cls, java.util.Collection cln) {
		throw new javax.jdo.JDOUnsupportedOptionException("newQuery(*)");
	}

	/**
	 * Create a new <code>Query</code> with the <code>Class</code> of the candidate instances, candidate <code>Collection</code>, and filter.
	 *
	 * @param cls the <code>Class</code> of candidate instances
	 * @param cln the <code>Collection</code> of candidate instances
	 * @param filter the filter for candidate instances
	 * @return the new <code>Query</code>
	 */
	@Override
	public javax.jdo.Query newQuery(java.lang.Class cls, java.util.Collection cln, java.lang.String filter) {
		throw new javax.jdo.JDOUnsupportedOptionException("newQuery(*)");
	}

	/**
	 * Create a new <code>Query</code> using elements from another
	 * <code>Query</code>. The other <code>Query</code> must have been created
	 * by the same JDO implementation. It might be active in a different
	 * <code>PersistenceManager</code> or might have been serialized and
	 * restored.
	 * <p>
	 * All of the settings of the other <code>Query</code> are copied to this
	 * <code>Query</code>, except for the candidate <code>Collection</code> or
	 * <code>Extent</code>.
	 *
	 * @param compiled another <code>Query</code> from the same JDO implementation
	 * @return the new <code>Query</code>
	 */
	@Override
	public javax.jdo.Query newQuery(java.lang.Object compiled) {
		throw new javax.jdo.JDOUnsupportedOptionException("newQuery(*)");
	}

	/**
	 * Create a new <code>Query</code> using the specified language.
	 *
	 * @param language the language of the query parameter
	 * @param query the query, which is of a form determined by the language
	 * @return the new <code>Query</code>
	 */
	@Override
	public javax.jdo.Query newQuery(java.lang.String language, java.lang.Object query) {
		throw new javax.jdo.JDOUnsupportedOptionException("newQuery(*)");
	}

	/**
	 * Create a new <code>Query</code> with the <code>Class</code> of the candidate instances and candidate <code>Extent</code>.
	 *
	 * @param cln the <code>Extent</code> of candidate instances
	 * @return the new <code>Query</code>
	 */
	@Override
	public javax.jdo.Query newQuery(javax.jdo.Extent cln) {
		throw new javax.jdo.JDOUnsupportedOptionException("newQuery(*)");
	}

	/**
	 * Create a new <code>Query</code> with the candidate <code>Extent</code> and filter; the class is taken from the <code>Extent</code>.
	 *
	 * @param cln the <code>Extent</code> of candidate instances
	 * @param filter the filter for candidate instances
	 * @return the new <code>Query</code>
	 */
	@Override
	public javax.jdo.Query newQuery(javax.jdo.Extent cln, java.lang.String filter) {
		throw new javax.jdo.JDOUnsupportedOptionException("newQuery(*)");
	}

	/**
	 * @param ownThread open a new parallel Thread for a Transaction
	 * @see DbUserTransactionBlock#execute(Runnable)
	 */
	public final synchronized DbTransaction openTransactionThread(boolean ownThread) {
		// TODO use DbUserTransactionBlock
		DbTransaction trans = DbTransaction.join(this /*
		 * getPersistenceManagerFactory
		 * ().getConnectionURL()
		 */);
		if (trans.isActive()) {
			if (ownThread) {
				throw new javax.jdo.JDOUserException(
					"Z.Z. koennen keine weiteren Transaktion parallel gestartet werden.\nVersuchen sie ihre Aktion bitte spaeter nochmal.");
			}
		} else {
			// start recording all DbObject's being treated within this
			// allocation cycle
			transactionObjects = new DbCache();
		}
		trans.begin();
		return trans;
	}

	/**
	 * Reconnect Target-System. This method should not be called during active Transactions.
	 *
	 * @see DbTransaction#isActive()
	 */
	public abstract void reconnect() throws javax.jdo.JDOException;

	/**
	 * Refresh the state of the instance from the data store.
	 *
	 * <p>
	 * In an optimistic transaction, the state of instances in the cache might not match the state in the data store. This method is used to reload the state of the instance from the data store so
	 * that a subsequent commit is more likely to succeed.
	 * <p>
	 * Outside a transaction, this method will refresh nontransactional state.
	 *
	 * @param pc the instance to refresh.
	 * @see javax.jdo.PersistenceManager
	 */
	@Override
	public void refresh(java.lang.Object pc) {
		if (pc instanceof DbObject) {
			DbObject object = (DbObject) pc;

			if (object.getPersistencyState().isNew()) {
				// NEW Object's cannot really be refreshed from Target-System
				try {
					if (!((object instanceof DbEnumeration) || (object instanceof DbNlsString) || (object instanceof DbSessionBean))) {
						DbDescriptor descriptor = object.getObjectServer().getDescriptor(object.getClass());
						while (descriptor != null) {
							// instantiate all properties of type DbNlsString in
							// state NEW as well
							Iterator iterator = descriptor.iteratorNlsStringProperties();
							while ((iterator != null) && iterator.hasNext()) {
								// @see DbMapper#mapNlsStrings()
								String property = (String) iterator.next();
								DbPropertyChange change = new DbPropertyChange(object, property);
								if (change.getPrivateField().get(object) == null) { // change.getValue()
									// =>
									// causes
									// recursive
									// Refreshing!
									DbNlsString nlsString = (DbNlsString) createInstance(DbNlsString.class);
									nlsString.setChange(change);
									change.setProperty(nlsString);
								}
							}
							// get parent of Object's inherited child as well
							descriptor = getParentDescriptor(descriptor);
						}
					}
				} catch (Exception e) {
					throw new UserException(ResourceManager.getResource(DbObjectServer.class, "CW_RefreshError"), ResourceManager.getResource(
						DbObjectServer.class, "CE_RefreshError"), e);
				}
			}
		}
	}

	/**
	 * Refresh the state of all applicable instances from the data store.
	 * <p>
	 * If called with an active transaction, all transactional instances will be refreshed. If called outside an active transaction, all nontransactional instances will be refreshed.
	 *
	 * @see #refresh(Object pc)
	 */
	@Override
	public void refreshAll() {
		if (transactionObjects != null) {
			// active Transaction
			refreshAll(transactionObjects.getAll());
		} else {
			// outside active Transaction
			throw new javax.jdo.JDOUnsupportedOptionException("DbObjectServer#refreshAll()->nonTransactional Objects");
		}
	}

	/**
	 * Refresh the state of an array of instances from the data store.
	 *
	 * @param pcs the array of instances to refresh.
	 * @see #refresh(Object pc)
	 */
	@Override
	public void refreshAll(java.lang.Object[] pcs) {
		for (int i = 0; i < pcs.length; i++) {
			refresh(pcs[i]);
		}
	}

	/**
	 * Refresh the state of a <code>Collection</code> of instances from the data store.
	 *
	 * @param pcs the <code>Collection</code> of instances to refresh.
	 * @see #refresh(Object pc)
	 */
	@Override
	public void refreshAll(java.util.Collection pcs) {
		refreshAll(pcs.toArray());
	}

	/**
	 * Dynamic registration/binding of Persistence Object, for e.g. adding Descriptors of Plugin-Objects later during runtime.
	 * <p>
	 * Typically any Model/Persistence Object to be used within an application should be registered after server initialization.
	 *
	 * <example> MyRegistryUtility { public void registerClasses(DbObjectServer
	 * server) { server.register(Person.class, "BusinessPerson"); } } </example>
	 *
	 * @param dbObjectClass
	 * @param schemaName
	 * @see DbConnection#bind(Class, String)
	 */
	public final void register(java.lang.Class dbObjectClass, String schemaName) {
		getConnection().bind(dbObjectClass, schemaName);
	}

	/**
	 * Try to remove an existing DbCode. Usually Codes are aggregated by a [0..1]:[0..n] Relationship, therefore it is very likely, that the Code cannot be deleted in case there are still existing
	 * Referential-Integrity-Constraints (depends on a proper Target-Model).
	 * <p>
	 * This method must be used carefully, because Codes are usually defined in a final non-changeable Set of elements.
	 * <p>
	 * Therefore this method is rather provided for Application-Administrators for convenience reasons.
	 *
	 * @see #cacheCode(DbCodeType)
	 */
	public abstract void removeCode(DbCode code);

	/**
	 * Retrieve field values of an instance from the store. This tells the
	 * <code>PersistenceManager</code> that the application intends to use the
	 * instance, and its field values must be retrieved.
	 * <p>
	 * The <code>PersistenceManager</code> might use policy information about the class to retrieve associated instances.
	 *
	 * @param pc the instance
	 * @see javax.jdo.PersistenceManager
	 */
	@Override
	public void retrieve(java.lang.Object pc) {
		refresh(pc);
	}

	/**
	 * Retrieve field values of instances from the store. This tells the
	 * <code>PersistenceManager</code> that the application intends to use the
	 * instances, and all field values must be retrieved.
	 * <p>
	 * The <code>PersistenceManager</code> might use policy information about the class to retrieve associated instances.
	 *
	 * @param pcs the instances
	 */
	@Override
	public void retrieveAll(java.lang.Object[] pcs) {
		retrieveAll(pcs, false);
	}

	/**
	 * Retrieve field values of instances from the store. This tells the
	 * <code>PersistenceManager</code> that the application intends to use the
	 * instances, and their field values should be retrieved. The fields in the default fetch group must be retrieved, and the implementation might retrieve more fields than the default fetch group.
	 * <p>
	 * The <code>PersistenceManager</code> might use policy information about the class to retrieve associated instances.
	 *
	 * @param pcs the instances
	 * @param dFGOnly whether to retrieve only the default fetch group fields
	 * @since 1.0.1
	 */
	@Override
	public void retrieveAll(java.lang.Object[] pcs, boolean dFGOnly) {
		List list = new ArrayList(pcs.length);
		Collections.addAll(list, pcs);
		retrieveAll(list, dFGOnly);
	}

	/**
	 * Convenience method.
	 *
	 * @param <T>
	 * @see #retrieveAll(Class, DbQueryBuilder, boolean)
	 */
	public <T extends DbObject> List<T> retrieveAll(Class<T> dbObject, DbQueryBuilder builder) throws Exception {
		return retrieveAll(dbObject, builder, false);
	}

	/**
	 * Read all DbObject's resulting from the given Query (encapsulated by builder) and create for each record a proper instance. Make sure builder returns a ResultSet to instantiate objects of type
	 * dbObject.
	 *
	 * @param dbObject BaseClass to instantiate
	 * @param builder (for e.g. containing a SQL DQL statement)
	 * @param cached cache retrieved objects if true
	 * @return List containing DbObject's.
	 */
	public abstract <T extends DbObject> List<T> retrieveAll(Class<T> dbObject, DbQueryBuilder builder, boolean cached) throws Exception;

	/**
	 * Retrieve field values of instances from the store. This tells the
	 * <code>PersistenceManager</code> that the application intends to use the
	 * instances, and all field values must be retrieved.
	 * <p>
	 * The <code>PersistenceManager</code> might use policy information about the class to retrieve associated instances.
	 *
	 * @param pcs the instances
	 */
	@Override
	public void retrieveAll(java.util.Collection pcs) {
		retrieveAll(pcs, false);
	}

	/**
	 * Retrieve field values of instances from the store. This tells the
	 * <code>PersistenceManager</code> that the application intends to use the
	 * instances, and their field values should be retrieved. The fields in the default fetch group must be retrieved, and the implementation might retrieve more fields than the default fetch group.
	 * <p>
	 * The <code>PersistenceManager</code> might use policy information about the class to retrieve associated instances.
	 *
	 * @param pcs the instances
	 * @param dFGOnly whether to retrieve only the default fetch group fields
	 * @since 1.0.1
	 */
	@Override
	public void retrieveAll(java.util.Collection pcs, boolean dFGOnly) {
		// TODO NYI: dFGonly
		Iterator iterator = pcs.iterator();
		while (iterator.hasNext()) {
			retrieve(iterator.next());
		}
	}

	/**
	 * @see #retrieveCodes(Class, Locale)
	 */
	public List<? extends DbCodeType> retrieveCodes(Class<? extends DbCodeType> dbCodeType) {
		return retrieveCodes(dbCodeType, Locale.getDefault());
	}

	/**
	 * Retrieve all instances of a given DbCodeType. If the DbCodeType is ordered (which must be defined by DbDescriptor#setOrdered() the ordering is maintained at very first reading from the
	 * Target-System otherwise no ordering (for e.g. by DbCodeType#getName() is guaranteed.
	 *
	 * @param dbCodeType Implementor of DbCodeType
	 * @param locale Attribute name is NLS specific
	 */
	public abstract List<? extends DbCodeType> retrieveCodes(Class<? extends DbCodeType> dbCodeType, Locale locale);

	/**
	 * Retrieve a DbEnumeration of the given class identified by a unique INTERLIS-Code.
	 *
	 * @return DbEnumeration new Instance.
	 */
	public abstract DbEnumeration retrieveEnumeration(Class<? extends DbEnumeration> dbEnumeration, final String iliCode) throws Exception;

	/**
	 * Set the ignoreCache parameter for queries.
	 *
	 * <p>
	 * IgnoreCache set to <code>true</code> specifies that for all
	 * <code>Query</code> instances created by this
	 * <code>PersistenceManager</code>, the default is the cache should be
	 * ignored for queries.
	 *
	 * @param flag the ignoreCache setting.
	 * @see javax.jdo.PersistenceManager
	 */
	@Override
	public void setIgnoreCache(boolean flag) {
		this.ignoreCache = flag;
	}

	/**
	 * Set the Multithreaded flag for this <code>PersistenceManager</code>. Applications that use multiple threads to invoke methods or access fields from instances managed by this
	 * <code>PersistenceManager</code> must set this flag to <code>true</code>. Instances managed by this
	 * <code>PersistenceManager</code> include persistent or transactional
	 * instances of <code>PersistenceCapable</code> classes, as well as helper instances such as <code>Query</code>, <code>Transaction</code>, or
	 * <code>Extent</code>.
	 *
	 * @param flag the Multithreaded setting.
	 */
	@Override
	public void setMultithreaded(boolean flag) {
		if (flag) {
			throw new javax.jdo.JDOUnsupportedOptionException("DbObjectServer=>multithreading");
		}
		this.multithreaded = flag;
	}

	/**
	 * Only DbObjectServer may influence id of given object! Given id's may only be changed if they are transient (temporary NEW).
	 */
	public void setUniqueId(Object object, Long id) {
		if ((object != null) && (id != null) && (object instanceof DbObject)
			&& ((((DbObject) object).getId() == null) || (((DbObject) object).getId().equals(id)) ||
			// TODO temporaryId is deprecated
			(((DbObject) object).getId().longValue() < 0 /*
			 * replace
			 * temporaryId with
			 * persistent one
			 */))) {
			DbPropertyChange change = new DbPropertyChange(object, DbObject.PROPERTY_ID);
			change.setProperty(id);
		} else {
			throw new DeveloperException("invalid parameters given");
		}
	}

	/**
	 * The application can manage the <code>PersistenceManager</code> instances more easily by having an application object associated with each
	 * <code>PersistenceManager</code> instance.
	 *
	 * @param o the user instance to be remembered by the
	 *     <code>PersistenceManager</code>
	 * @see #getUserObject
	 * @see javax.jdo.PersistenceManager
	 */
	@Override
	public void setUserObject(java.lang.Object o) {
		this.userObject = o;
	}
}

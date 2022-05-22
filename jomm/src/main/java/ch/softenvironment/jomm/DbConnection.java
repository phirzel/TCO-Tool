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

import ch.softenvironment.jomm.datatypes.DbNlsString;
import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.mvc.model.DbChangeableBean;
import ch.softenvironment.jomm.mvc.model.DbEntityBean;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.jomm.mvc.model.DbSessionBean;
import ch.softenvironment.util.StringUtils;
import ch.softenvironment.util.Tracer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/**
 * Database Connection (encapsulates a java.sql.Connection).
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public abstract class DbConnection {

    private transient java.sql.Connection connection = null;
    private transient DbObjectServer objectServer = null;
    private final transient Map<Class<? extends DbObject>, String> tables = new HashMap<Class<? extends DbObject>, String>();
    private final transient Map/* <? extends DbObject, DbDescriptor> */descriptors = new HashMap();
    private final transient Map<Class<? extends DbObject>, Class<? extends DbObject>> hierarchy = new HashMap<Class<? extends DbObject>, Class<? extends DbObject>>();

    // Pool of DbConnection for synchronization reasons (though 1 DbObjectServer
    // has 0..1 DbConnection only)
    private final Map<String, Stack<DbConnection>> sessionPool = new HashMap<String, Stack<DbConnection>>();

    /**
     * Register persistence objects handled by a given objectServer.
     *
     * @param objectServer
     */
    public DbConnection(DbObjectServer objectServer) {
        super();

        this.objectServer = objectServer;

        bind(DbNlsString.class, getObjectServer().getMapper().getTargetNlsTranslationName());

        // register(DbObjectRegistry.getRegistry());
    }

    /**
     * Add a DbDescriptor for given DbObject. All instances of the same DbObject-Class type have the same Descriptor.
     * <p>
     * Make sure all concrete DbObject subclasses incl. subclasses of DbObject & DbEnumeration have the method - public static DbDescriptor createDescriptor()
     */
    protected final void addDescriptor(Class<? extends DbObject> dbObject) {
        try {
            if (DbObject.isCode(dbObject)) {
                Class[] parameterTypes = { /*Class.class*/};
                Object[] parameters = { /*dbObject*/};
                java.lang.reflect.Method method = dbObject.getMethod("createDescriptor", parameterTypes);
                descriptors.put(dbObject, (method).invoke(dbObject, parameters));
            } else {
                Class[] parameterTypes = {};
                Object[] parameters = {};
                java.lang.reflect.Method method = dbObject.getMethod("createDescriptor", parameterTypes);
                descriptors.put(dbObject, (method).invoke(dbObject, parameters));
            }
        } catch (Exception e) {
            Tracer.getInstance().developerError("failed for Class <" + dbObject + "> " + e.getLocalizedMessage());
        }
    }

    /**
     * Bind persistent Class representing persistent objects (by analyzing inheritance hierarchy and maintaining its Descriptor).
     *
     * @param dbObject DbObject
     * @see org.odmg.Database#bind(Object, String)
     * @see org.odmg.Database#lookup(String)
     * @see #getRootClassFor(Class)
     */
    protected final void bind(Class<? extends DbObject> dbObject, final String targetInstance) {
        addDescriptor(dbObject);

        if (hierarchy.containsKey(dbObject)) {
            // throw new
            // DeveloperException("Duplicate entry for (make sure Base classes are mapped before their Children!): "
            // + dbObject);
            Tracer.getInstance().developerWarning("Duplicate entry for: " + dbObject + " => make sure Base classes are mapped before their Children!");
        }

        // determine inherited Class's and keep them in a hierarchy-map
        Class<? extends DbObject> rootClass = dbObject;
        while (!(rootClass.getSuperclass().equals(DbObject.class) ||
            // TODO DbCode's and DbRelationshipBean's might be inherited too in
            // future
            rootClass.getSuperclass().equals(DbEntityBean.class))) {
            rootClass = (Class<? extends DbObject>) rootClass.getSuperclass();
        }
        if (rootClass.getSuperclass().equals(DbEntityBean.class) && (!rootClass.equals(dbObject))) {
            // there is an Inheritance-Chain!

            // 1) map child-leave and its base parent
            hierarchy.put(dbObject, rootClass);

            // register Descriptors of inheritance Chain as well
            Class<? extends DbObject> temp = dbObject;
            while (!temp.getSuperclass().equals(DbEntityBean.class)) {
                temp = (Class<? extends DbObject>) temp.getSuperclass();
                if (hierarchy.containsKey(temp)) {
                    Tracer.getInstance().developerWarning("Root-class <" + temp + "> for concrete object <" + dbObject + "> already bound");
                }
                // 2) map other leaves in chain (might be abstract)
                hierarchy.put(temp, rootClass);

                // 3) add Descriptor of inheritance chain
                addDescriptor(temp);
            }
        }
        if (!StringUtils.isNullOrEmpty(targetInstance)) {
            tables.put(dbObject, targetInstance.trim());
        }
    }

    /**
     * Close this database-connection.
     */
    public abstract void close() throws javax.jdo.JDOException;

    /**
     * Return the matching Class-Type for a registered given Target-Type.
     *
     * @see #DbConnection()
     */
    public final Class<? extends DbObject> getDbObject(final String targetName) {
        if (DbNlsString.class.getName().equals(targetName)) {
            // implementory DbObject
            return DbNlsString.class;
        }
        if (tables.containsValue(targetName)) {
            Iterator<Class<? extends DbObject>> iterator = tables.keySet().iterator();
            while (iterator.hasNext()) {
                Class<? extends DbObject> key = iterator.next();
                if (tables.get(key).equals(targetName)) {
                    return key;
                }
            }
        }
        throw new ch.softenvironment.util.DeveloperException("TargetName <" + targetName + "> not mapped to DbObject yet");
    }

    /**
     * Return a DbDescriptor mapped to a given DbObject.
     */
    public final DbDescriptor getDescriptor(Class<? extends DbObject> dbObject) {
        if (descriptors.containsKey(dbObject)) {
            return (DbDescriptor) descriptors.get(dbObject);
        } else {
            if (!dbObject.getSuperclass().equals(DbChangeableBean.class)) {
                Tracer.getInstance().developerWarning("No DbDescriptor registered for: " + dbObject.getName());
            }
            return null;
        }
    }

    /**
     * @see #setJdbcConnection()
     */
    public final java.sql.Connection getJdbcConnection() {
        return connection;
    }

    /**
     * Return the ObjectServer.
     */
    protected final DbObjectServer getObjectServer() {
        return objectServer;
    }

    /**
     * Return the Base Class mapped to a Database-Table for the given one.
     *
     * @see #bind(Class, String)
     */
    public final Class<? extends DbObject> getRootClassFor(Class<? extends DbObject> dbEntityBean) {
        if (hierarchy.containsKey(dbEntityBean)) {
            return hierarchy.get(dbEntityBean);
        } else {
            return dbEntityBean;
        }
    }

    /**
     * Return a TargetName for a registered given Class. Each instance of a DbObject Class is mapped to the same Table by one DbConnection.
     *
     * @see #DbConnection()
     */
    protected final String getTargetNameFor(Class<? extends DbObject> dbObject) {
        if (tables.containsKey(dbObject)) {
            return tables.get(dbObject);
        } else if (dbObject.getSuperclass().equals(DbSessionBean.class)) {
            return tables.get(DbSessionBean.getEntityBeanClass(dbObject));
        } else {
            throw new ch.softenvironment.util.DeveloperException("Class <" + dbObject.getName() + "> not mapped to Table yet");
        }
    }

    /**
     * Open a database connection.
     *
     * @param userId
     * @param password
     */
    public abstract void open(String userId, String password) throws javax.jdo.JDOException;

    /**
     * Pop the Database Connection with given URL from Session-Pool.
     */
    protected final DbConnection pop(final String url) {
        // TODO move to DbObjectServer
        Stack<DbConnection> stack = sessionPool.get(url);
        synchronized (stack) {
            while (stack.empty()) {
                try {
                    stack.wait();
                } catch (InterruptedException e) {
                    Tracer.getInstance().runtimeWarning("pop(String): " + e.getLocalizedMessage());
                }
            }
            // Tracer.getInstance().debug("DbConnection->going to pop: size=" +
            // sessionPool.size());
            return stack.pop();
        }
    }

    /**
     * Push the Database Connection with given URL to SessionPool.
     */
    protected final void push(DbConnection c) {
        // TODO move to DbObjectServer
        String url = c.getObjectServer().getPersistenceManagerFactory().getConnectionURL();
        if (!sessionPool.containsKey(url)) {
            sessionPool.put(url, new Stack<DbConnection>());
        }
        Stack<DbConnection> stack = sessionPool.get(url);

        synchronized (stack) {
            stack.push(c);
            // Tracer.getInstance().debug("DbConnection->pushed: size=" +
            // sessionPool.size());
            stack.notify();
        }
    }

    /**
     * Set the JDBC-Driver specific Connection to Target-System.
     */
    protected final void setJdbcConnection(java.sql.Connection connection) {
        this.connection = connection;
    }
}
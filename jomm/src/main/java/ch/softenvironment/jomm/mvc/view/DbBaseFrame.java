package ch.softenvironment.jomm.mvc.view;

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
import ch.softenvironment.jomm.DbDomainNameServer;
import ch.softenvironment.jomm.DbMapper;
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.DbQueryBuilder;
import ch.softenvironment.jomm.implementation.DbPropertyChange;
import ch.softenvironment.jomm.mvc.controller.ConsistencyController;
import ch.softenvironment.jomm.mvc.controller.DbObjectListener;
import ch.softenvironment.jomm.mvc.controller.DbTableModel;
import ch.softenvironment.jomm.mvc.model.DbChangeableBean;
import ch.softenvironment.jomm.mvc.model.DbEntityBean;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.jomm.mvc.model.DbRelationshipBean;
import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.view.BaseDialog;
import ch.softenvironment.view.BaseFrame;
import ch.softenvironment.view.ListMenuChoice;
import ch.softenvironment.view.ViewOptions;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * Template parent Frame for Database Oriented GUI's. This class provides many convenience methods for the database & business Framework.
 *
 * @author Peter Hirzel
 */
@Deprecated
@Slf4j
public abstract class DbBaseFrame extends BaseFrame {

    private static final long serialVersionUID = 1L;

    /**
     * Create a View and pass DbObjects to be treated by it.
     *
     * @see ch.softenvironment.view.SearchView
     */
    public DbBaseFrame(ViewOptions viewOptions) {
        super(viewOptions);
    }

    /**
     * Create a View and pass DbObjects to be treated by it.
     *
     * @see ch.softenvironment.view.DetailView
     */
    public DbBaseFrame(ViewOptions viewOptions, java.util.List<? extends DbObject> objects) {
        super(viewOptions, objects);
        /*
         * if ((objects != null) && (objects.size() > 0) && (objects.get(0)
         * instanceof DbObject)) { // set a default server
         * setObjectServer(((DbObject)objects.get(0)).getObjectServer()); }
         */
    }

    /**
     * Helper Method to assign selected objects to a given List. Case: A:B=n:n mapped via C (where A or B are AssociationEnd's and C is the n:n Map).
     *
     * @param ownerType Type of A or B
     * @param owner Either A or B AssociationEnd referencing the opposite List via the C
     * @param propertyOther Property-Name of other AssociationEnd (same Property in AssociationEnd and Map C but different type)
     * @param objects Selection of AssociationEnd's of B or A to be assigned
     * @param listener to be informed on adding or removing elements
     */
    protected void assignAssociations(java.lang.Class ownerType, DbEntityBean owner, final String propertyOther, java.util.List<? extends DbObject> objects,
        DbObjectListener listener) throws Exception {
        DbPropertyChange assocEnd = new DbPropertyChange(owner, propertyOther);
        // @see JavaBeans Specification chapter "7.3 indexed arrays"
        // => getList returns a copy of the items
        java.util.List ownedOthers = (java.util.List) assocEnd.getValue(); // propertyOther
        // is
        // a
        // List

        java.util.Iterator<? extends DbObject> selection = objects.iterator();
        while (selection.hasNext()) {
            Long otherId = selection.next().getId();

            java.util.Iterator iteratorCurrent = ownedOthers.iterator();
            boolean duplicateEntry = false;
            while (iteratorCurrent.hasNext()) {
                DbRelationshipBean association = (DbRelationshipBean) iteratorCurrent.next();
                // the pair of FK's to A.T_Id and B.T_Id are always UNIQUE
                DbPropertyChange mapElement = new DbPropertyChange(association, propertyOther);
                if (mapElement.getValue().equals(otherId)) {
                    if (association.getPersistencyState().isRemovedPending()) {
                        // "undo removal"
                        association.undoRemove();
                        duplicateEntry = true;
                    } else {
                        BaseDialog.showWarning(this, ResourceManager.getResource(DbBaseFrame.class, "CTAssignDuplicate"),
                            ResourceManager.getResource(DbBaseFrame.class, "CEAssignDuplicate"));
                        duplicateEntry = true;
                        break;
                    }
                }
            }
            if (!duplicateEntry) {
                // add a new one
                java.lang.Class mapC = owner.getAssociationClass(ownerType, propertyOther);
                DbRelationshipBean association = (DbRelationshipBean) owner.getObjectServer().createInstance(mapC);
                association.addPropertyChangeListener(listener);
                DbPropertyChange mapElement = new DbPropertyChange(association, propertyOther);
                mapElement.setValue(otherId); // must exist => SAVED
                // association.set<Owner>Id(owner.getId()); // in case NEW =>
                // not existing yet

                // !!! the relationship is added, NOT the other AssociationEnd
                ownedOthers.add(association);
            }
        }
        // keep change in mind
        assocEnd.setValue(ownedOthers);
    }

    /**
     * Helper Method to assign selected objects to a given List belonging to a given owner. Case: A:B=1:n
     *
     * @param owner Either A having many B's
     * @param propertyOther Property-Name of ManyEnd from view of owner
     * @param objects assignables of B
     * @param listener to be informed on adding
     */
    protected void assignOneToMany(DbChangeableBean owner, final String propertyOther, java.util.List<? extends DbObject> objects, DbObjectListener listener)
        throws Exception {
        DbPropertyChange ownerAssocEnd = new DbPropertyChange(owner, propertyOther);
        // @see JavaBeans Specification chapter "7.3 indexed arrays"
        // => getList returns a copy of the items
        java.util.List ownedOthers = (java.util.List) ownerAssocEnd.getValue(); // propertyOther
        // is
        // a
        // List

        java.util.Iterator<? extends DbObject> assignables = objects.iterator();
        while (assignables.hasNext()) {
            DbObject manyAssocEnd = assignables.next();

            ownedOthers.add(manyAssocEnd);
            if (!manyAssocEnd.getPersistencyState().isSaved()) {
                // keep change in mind
                ownerAssocEnd.setValue(ownedOthers);
            }

            if (manyAssocEnd instanceof DbChangeableBean) {
                ((DbChangeableBean) manyAssocEnd).addPropertyChangeListener(listener);
            }
        }
    }

    /**
     * Disconnect Db-Session.
     *
     * @see BaseFrame#disposeApplication()
     * @see #dispose()
     */
    protected void disposeApplication(DbObjectServer server) {
        try {
            ConsistencyController.setCascaded(false);

            if (server != null) {
                // javax.jdo.PersistenceManagerFactory factory =
                // server.getPersistenceManagerFactory();
                server.close();
                // factory.close();
            }
        } catch (Exception e) {
            log.error("Database-disconnect() failed", e);//$NON-NLS-2$//$NON-NLS-1$
            BaseDialog.showWarning(this, ResourceManager.getResource(DbBaseFrame.class, "CTTerminationError"),
                ResourceManager.getResource(DbBaseFrame.class, "CETerminationError") + "\n[" + e.getLocalizedMessage() + "]");
        }
        super.disposeApplication();
    }

    /**
     * Convenience Method to return the default persistence server.
     */
    protected DbObjectServer getDefaultServer(DbObject object) {
        if (object == null) {
            return DbDomainNameServer.getDefaultServer();
        } else {
            return object.getObjectServer();
        }
    }

    @Override
    protected void handleException(Throwable error) {
        Throwable exception = error;
        if (error instanceof javax.jdo.JDOException) {
            if (error.getCause() != null) {
                log.error("{}", error.getClass(), error);
                // JDO implements Throwable as cause!
                exception = new Exception(error.getCause());
            }
        }

        SQLException sqlException = null;
        if (error instanceof java.sql.SQLException) {
            sqlException = (SQLException) exception;
        } else if (error.getCause() instanceof SQLException) {
            log.error("", error);
            sqlException = (SQLException) error.getCause();
        }
        if (sqlException != null) {
            String title = ResourceManager.getResource(DbBaseFrame.class, "CTSQLError");
            String message = sqlException.getLocalizedMessage() + " (" + ResourceManager.getResource(DbBaseFrame.class, "CESQLState")
                + sqlException.getSQLState() + ")";

            if (getDefaultServer(null) != null) {
                DbMapper mapper = getDefaultServer(null).getMapper();
                if (mapper != null) {
                    String explanation = mapper.describeTargetException(sqlException);
                    if (explanation != null) {
                        message = message + "\n" + ResourceManager.getResource(DbBaseFrame.class, "CESQLReasons") + "\n" + explanation;
                    }
                }
            }

            while ((sqlException = sqlException.getNextException()) != null) {
                message = message + "\n" + sqlException.getLocalizedMessage();
            }

            showException(this, new DeveloperException(message, title, exception));
        } else {
            super.handleException(exception);
        }
    }

    /**
     * Build a List of SessionBeans representing an n:n case.
     * <p>
     * Case A:B=n:n mapped via C (where A or B are AssociationEnd's and C is the n:n Map).
     * <p>
     * Create a Query to read all elements associated by a Relationship where all Attributes in B are qualified by the Alias "B.*" as follows: SELECT * FROM TableB AS B WHERE (B.T_Id IN (513, 612))
     *
     * @param change A pointing to List of B (or vice versa)
     * @param sessionBeanClass Target-Class to be returned as List
     * @param otherElements (Pseudo)-Representation of B's (or vice versa)
     * @return List of sessionBeanClass-Instances representing the n:n maps
     */
    protected java.util.List refreshAssociations(DbPropertyChange change, java.lang.Class sessionBeanClass, java.util.List otherElements) throws Exception {
        java.util.List<DbObject> sbs = new java.util.ArrayList<>();

        java.util.Set mapC = change.getAssocations();
        java.util.Iterator assocIterator = mapC.iterator();
        while (assocIterator.hasNext()) {
            DbRelationshipBean association = (DbRelationshipBean) assocIterator.next();
            java.util.Iterator otherEndIterator = otherElements.iterator();
            while (otherEndIterator.hasNext()) {
                DbObject otherEnd = (DbObject) otherEndIterator.next();
                DbPropertyChange oppositeElement = new DbPropertyChange(association, change.getProperty());
                if (otherEnd.getId().equals(oppositeElement.getValue())) {
                    // create a SessionBean without reading from Database
                    Class[] types = {DbChangeableBean.class, DbObject.class};
                    Object[] args = {association, otherEnd};
                    java.lang.reflect.Constructor constructor = sessionBeanClass.getConstructor(types);
                    sbs.add((DbObject) constructor.newInstance(args));
                    break;
                }
            }
        }

        if (sbs.size() != mapC.size()) {
            log.warn("Developer warning: data problem => AssociationEnds not equal to Associated-Set");
        }

        return sbs;
    }

    /**
     * Use this Helper Method to display <b>other ends of n:n Relationships if Association is non-Attributed</b> and therefore completely representable by opposite end-Elements.
     * <p>
     * Case A:B=n:n mapped via C (where A or B are AssociationEnd's and C is the n:n Map) from the view of A.
     * <p>
     * Create a Query to read all elements associated by a Relationship where all Attributes in B are qualified by the Alias "B.*" as follows: SELECT * FROM TableB AS B WHERE (B.T_Id IN (513, 612))
     *
     * @param ownerType Class implementing propertyOther (might be anywhere in inheritance Chain)
     * @param owner AssociationEnd A referencing the opposite List of B's via the Mapping Table C
     * @param propertyOther Property-Name in A pointing to other AssociationEnd B (same Property in AssociationEnd B and Map C but different type)
     */
    protected DbQueryBuilder refreshAssociations(java.lang.Class ownerType, DbEntityBean owner, final String propertyOther) throws Exception {
        DbPropertyChange change = new DbPropertyChange(owner, propertyOther);
        java.util.Set fkB = change.getAssocationsId();
        if (fkB.size() == 0) {
            return null;
        } else {
            DbQueryBuilder builder = owner.getObjectServer().createQueryBuilder(DbQueryBuilder.SELECT, "Refresh n:n-Map");
            builder.setTableList(owner.getObjectServer().getTargetName(owner.getAssociatedClass(ownerType, propertyOther)) + " AS B");
            // search by (B.T_Id IN (..)) because (MapC.T_Id_A=A.T_Id) might not
            // exist yet if A or Map-Assignment is new!
            builder.addFilter("B." + owner.getObjectServer().getMapper().getTargetIdName(), fkB);
            return builder;
        }
    }

    /**
     * Helper Method to register Bean-Changes for this view.
     */
    protected void registerPropertyChangeListener(List objects, boolean register) throws ClassCastException {
        Iterator iterator = objects.iterator();
        while (iterator.hasNext()) {
            if (register) {
                ((DbChangeableBean) iterator.next()).addPropertyChangeListener((java.beans.PropertyChangeListener) this);
            } else {
                ((DbChangeableBean) iterator.next()).removePropertyChangeListener((java.beans.PropertyChangeListener) this);
            }
        }
    }

    protected void removeAssociations(java.util.List selection, DbEntityBean owner, final String propertyOther, DbObjectListener listener) throws Exception {
        removeAssociations(selection, owner, propertyOther, listener, false);
    }

    /**
     * Helper Method to remove selected Items in JTable's DbTableModel. Case A:B=n:n mapped via C (where A or B are AssociationEnd's and C is the n:n Map).
     *
     * @param selection list to be removed
     * @param owner Either A or B AssociationEnd referencing the opposite List via the C
     * @param propertyOther Property-Name of other AssociationEnd (same Property in AssociationEnd and Map C but different type)
     */
    protected void removeAssociations(java.util.List selection, DbEntityBean owner, final String propertyOther, DbObjectListener listener, boolean historize)
        throws Exception {
        DbPropertyChange assocEnd = new DbPropertyChange(owner, propertyOther);
        // HACK @see JavaBeans Specification chapter "7.3 indexed arrays"
        // => getList returns a copy of the items
        java.util.List ownedOthers = /* new ArrayList( */(java.util.List) assocEnd.getValue()/* ) */; // propertyOther
        // is
        // a
        // List

        java.util.Iterator selectionIterator = selection.iterator();
        while (selectionIterator.hasNext()) {
            DbObject selectedObject = (DbObject) selectionIterator.next();
            Long selectionId = null;
            if (selectedObject instanceof DbRelationshipBean) {
                // in case n:n List is represented by DbRelationship-SessionBean
                // itself (for e.g. if having associated Attributes)
                DbPropertyChange selectionChange = new DbPropertyChange(selectedObject, propertyOther);
                selectionId = (Long) selectionChange.getValue();
            } else {
                // in simple cases, the Relationship is represented by
                // AssociationEnd itself (no associated Attributes)
                selectionId = selectedObject.getId();
            }
            java.util.Iterator iterator = ownedOthers.iterator();
            while (iterator.hasNext()) {
                DbRelationshipBean association = (ch.softenvironment.jomm.mvc.model.DbRelationshipBean) iterator.next();
                DbPropertyChange mapElement = new DbPropertyChange(association, propertyOther); // other
                // Id
                // is
                // Long-Reference
                if (mapElement.getValue().equals(selectionId)) {
                    if (association.getPersistencyState().isNew()) {
                        // remove definitively from list
                        ownedOthers.remove(association);
                    }

                    association.removePropertyChangeListener(listener);
                    association.remove(false, historize);
                    break; // resolves concurrency problem at objects.remove(..)
                }
            }
        }

        // keep change in mind
        // TODO BUG will not trigger changed-event in assocEnd because
        // ownedOthers-List is the same with same size and elements! Only the
        // DbState of a ownedOther-Element changed to REMOVED ||
        // REMOVED_PENDING!
        // PATCH: @see DbChangeableBean#firePropertyChange() or call
        // getPnlStandardToolbar().setTbbSaveEnabled(true); after this method
        assocEnd.setValue(ownedOthers /*
         * copy to new ArrayList does not help
         * either
         */);
    }

    /**
     * Helper Method to remove selected Items in JTable's DbTableModel and keeping Track if selected Items are owned by a Parent. Case: A:B=1:n
     *
     * @param table where selection happened
     * @param owner A having many B's
     * @param propertyOther Property-Name of B's many-End
     */
    protected void removeOneToMany(javax.swing.JTable table, DbChangeableBean owner, final String propertyOther, DbObjectListener listener) throws Exception {
        List removedItems = DbTableModel.removeSelectedItems(table, getViewOptions(), false);

        DbPropertyChange ownerAssocEnd = new DbPropertyChange(owner, propertyOther);
        // @see JavaBeans Specification chapter "7.3 indexed arrays"
        // => getList returns a copy of the items
        java.util.List ownedOthers = (java.util.List) ownerAssocEnd.getValue(); // propertyOther
        // is
        // a
        // List
        ownedOthers.removeAll(removedItems);
        // ownerAssocEnd.setValue(ownedOthers);

        ((DbTableModel) table.getModel()).setAll(ownedOthers);
    }

    /**
     * Convenience method. Remove selected Items in JTable's DbTableModel. Any owner-Parent's list is not updated.
     */
    protected List removeSelectedItems(javax.swing.JTable table) throws Exception {
        List removedItems = DbTableModel.removeSelectedItems(table, getViewOptions(), false);
        /*
         * ((DbTableModel)table.getModel()).remove(table.getSelectedRows());
         * table.clearSelection();
         *
         * if ((getViewOptions() != null) && (getViewOptions().getViewManager()
         * != null)) { Tracer.getInstance().developerWarning(
         * "View's should be closed before deletion of their model-Object's");
         * getViewOptions().getViewManager().closeAll(removedItems); }
         */
        if (this instanceof ListMenuChoice) {
            ((ListMenuChoice) this).adaptUserAction(null, null);
        }

        return removedItems;
    }

    /**
     * Critical Error. Application must be shut down.
     */
    public final void transactionError(javax.swing.JFrame frame, Exception exception) {
        fatalError(frame, ResourceManager.getResource(DbBaseFrame.class, "CTTransactionError"),
            ResourceManager.getResource(DbBaseFrame.class, "CETransactionError"), exception);
        System.exit(-1);
    }
}
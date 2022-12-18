package ch.softenvironment.jomm.mvc.model;
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
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.datatypes.DbNlsString;
import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbDescriptorEntry;
import ch.softenvironment.jomm.descriptor.DbIdFieldDescriptor;
import ch.softenvironment.util.BeanReflector;
import lombok.extern.slf4j.Slf4j;

/**
 * <b>EntityBean according to Enterprise Java Bean (EJB) Specification:</b>
 * <quote>Entity beans are server-side components that are persistent and
 * transactional. They are used to model persistent data objects. An entity bean can manage its own persistent state in a data-store and its own relationships, in which case it is a bean-managed
 * persistence (BMP) entity bean. Or, it can let the EJB container manage its persistent state and relationships, in which case it is a container-managed persistence (CMP) entity bean.
 * Container-managed persistence has changed significantly in the 2.0 specification from the earlier specifications.</quote>
 * <p>
 * An EntityBean represents a persistent Object which is mapped 1:1 to a Database Table Entity.
 *
 * @author Peter Hirzel softEnvironment
 */
@Slf4j
public abstract class DbEntityBean extends DbChangeableBean /* ,javax.ejb.EJBObject */ {

    /**
     * @see DbObject(DbObjectServer)
     */
    protected DbEntityBean(DbObjectServer objectServer) {
        super(objectServer);
    }

    /**
     * Creates and returns a <b>deep-copy</b> of this object.
     *
     * @see Object#clone()
     */
    @Override
    public synchronized Object clone() throws CloneNotSupportedException {
        DbEntityBean copy = null;

        try {
            // force Object-faulting before cloning
            //		this.refresh(true);

            // do NOT use super.clone() because aggregates (and List's) are not cloned properly
            copy = (DbEntityBean) getObjectServer().createInstance(this.getClass());
            DbDescriptor descriptor = getObjectServer().getDescriptor(this.getClass());
            while (descriptor != null) {
                // Flat-Properties
                java.util.Iterator<String> iterator = descriptor.iteratorFlatProperties();
                while (iterator.hasNext()) {
                    String property = iterator.next();
                    if (descriptor.getEntry(property).getFieldType() instanceof DbIdFieldDescriptor) {
                        // @see DbDescriptor#addOneToOneReferenceId
                        //TODO NYI: Flat-Property NOT CLONED <" + property + "> because 1:1 References are managed as flat-property yet
                    } else {
                        BeanReflector source = new BeanReflector(this, property);
                        if (source.hasProperty() == BeanReflector.GETTER_AND_SETTER) {
                            BeanReflector target = new BeanReflector(copy, property);
                            target.setValue(source.cloneValue());
                        }
                        //					else Tracer.getInstance().debug(this, "clone()", "Property <" + property + "> skipped because no proper SETTER AND GETTER");
                    }
                }
                // DbNlsString's
                iterator = descriptor.iteratorNlsStringProperties();
                while ((iterator != null) && iterator.hasNext()) {
                    String property = iterator.next();
                    BeanReflector source = new BeanReflector(this, property);
                    DbNlsString value = (DbNlsString) source.getValue();
                    if (value != null) {
                        BeanReflector target = new BeanReflector(copy, property);
                        if (value.hasChangeableOwner()) {
                            //						target.setValue(source.getValue().clone());
                            //TODO NYI: DbNlsString-Property NOT CLONED <" + property + "> because owned Strings must be duplicated
                        } else {
                            target.setValue(source.getValue());
                        }
                    }
                }
                // {1:0..1} Aggregated-Objects
                iterator = descriptor.iteratorAggregatedProperties();
                while ((iterator != null) && iterator.hasNext()) {
                    String property = iterator.next();
                    BeanReflector source = new BeanReflector(this, property);
                    Object value = source.getValue();
                    if (value != null) {
                        BeanReflector target = new BeanReflector(copy, property);
                        if ((value instanceof DbEnumeration) || (value instanceof DbCode)) {
                            // always single instances of each specific Code
                            target.setValue(source.getValue());
                        } else if (value instanceof DbEntityBean) {
                            DbDescriptorEntry entry = descriptor.getEntry(property);
                            if (entry.isLocal() && (entry.getAssociationType() == DbDescriptor.AGGREGATION)) {
                                //TODO NYI: check other side of aggregation if range is ok with one more element
                                // suppose one more aggregation is the right thing
                                target.setValue(source.getValue());
                            } else {
                                // clone recursively
                                //							target.setValue(((DbEntityBean)source.getValue()).clone());
                                //TODO NYI: DbEntityBean-Aggregation NOT CLONED for <" + property + "> (might depend on aggregationType)
                            }
                        } else {
                            //TODO NYI: Non-DbEntityBean-Aggregation NOT CLONED for <" + property + ">"
                        }
                    }
                }
                // {1:0..*} Aggregations
                iterator = descriptor.iteratorOneToManyProperties();
                while ((iterator != null) && iterator.hasNext()) {
                    String property = iterator.next();
                    BeanReflector source = new BeanReflector(this, property);
                    java.util.List sourceList = (java.util.List) source.getValue();
                    if (sourceList != null) {
                        BeanReflector target = new BeanReflector(copy, property);
                        java.util.List targetList = (java.util.List) target.getValue();
                        java.util.Iterator sources = sourceList.iterator();
                        while (sources.hasNext()) {
                            Object element = sources.next();
                            if ((element instanceof DbEnumeration) || (element instanceof DbCode)) {
                                // always single instances of each specific Code
                                targetList.add(element);
                            } else if (element instanceof DbEntityBean) {
                                // clone recursively
                                //							targetList.add(((DbEntityBean)element).clone());
                                //TODO NYI: DbEntityBean-Aggregation NOT CLONED for <" + property + "> (might depend on aggregationType)
                            } else {
                                //TODO NYI: 1:n-Aggregation NOT CLONED <" + property + ">"
                            }
                        }
                    }
                }

                // try parent as well
                descriptor = getObjectServer().getParentDescriptor(descriptor);
            }
        } catch (Exception e) {
            log.error("", e);
            throw new CloneNotSupportedException(ResourceManager.getResource(DbEntityBean.class, "CENotCloneable") + "\n[" + e.getLocalizedMessage() + "]");
        }
    
    /*
    	
    	DbChangeableBean newInstance = (DbChangeableBean)super.clone();
    	
    	newInstance.persistencyState = new DbState();
    	newInstance.getPersistencyState().setNext(DbState.NEW);
    
    	newInstance.setTemporaryNewId(getObjectServer().getTemporaryNewId());
    	newInstance.fieldId = null; // don't use #setId() & DbObjectServer#setUniqueId()
    	newInstance.setCreateDate(DbObjectServer.createTimestamp());
    	newInstance.setLastChange(newInstance.getCreateDate());
    	newInstance.setUserId(getObjectServer().getUserId());
    
    ch.softenvironment.util.Tracer.getInstance().nyi(this, "clone()", "aggregations are not cloned properly");
    
    ch.softenvironment.util.Tracer.getInstance().developerWarning(this, "clone()", "INSERT will save all attributes anyway but actully changedProperties are not correct now!");
    newInstance.changedProperties = new HashSet();
    */
        return copy;
    }
}
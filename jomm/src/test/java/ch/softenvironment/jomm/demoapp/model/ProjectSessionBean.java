package ch.softenvironment.jomm.demoapp.model;

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

import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.mvc.model.DbSessionBean;

/**
 * Logical VIEW of a Project.
 *
 * @author Peter Hirzel
 */
public class ProjectSessionBean extends DbSessionBean {

    private Boolean fieldActive;
    private java.lang.String fieldName;
    private java.util.Date fieldStart;

    // private String fieldRealizerString;

    /**
     * ProjectSB constructor.
     *
     * @param objectServer
     */
    protected ProjectSessionBean(DbObjectServer objectServer) {
        super(objectServer);
        // state.setNext(DbState.SAVED);
    }

    /**
     * Return the database mappings for this Object.
     * <p>
     * Overwrite resp. extend Descriptor for subclasses.
     *
     * @return DbDescriptor
     */
    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(ProjectSessionBean.class);

        descriptor.cloneEntry(Project.class, PROPERTY_NAME);
        /*
         * descriptor.cloneEntry(Project.class, PROPERTY_ACTIVE);
         * descriptor.cloneEntry(Project.class, "start");
         */
        // descriptor.addAlias("customerString", "CUSTOMER");
        // descriptor.addAlias("realizerString", "REALIZER");

        return descriptor;
    }

    /**
     * Gets the name property (java.lang.String) value.
     *
     * @return The name property value.
     */
    public Boolean getActive() {
        return fieldActive;
    }

    /**
     * Gets the name property (java.lang.String) value.
     *
     * @return The name property value.
     */
    public java.lang.String getCustomerString() {
        return "<NYI>";
    }

    /**
     * Overwrites.
     */
    public static Class<Project> getEntityBeanClass() {
        return Project.class;
    }

    /**
     * Gets the name property (java.lang.String) value.
     *
     * @return The name property value.
     */
    public java.lang.String getName() {
        return fieldName;
    }

    /**
     * Gets the name property (java.lang.String) value.
     *
     * @return The name property value.
     */
    public java.lang.String getRealizerString() {
        return "<NYI>";
    }

    /**
     * Gets the name property (java.lang.String) value.
     *
     * @return The name property value.
     */
    public java.util.Date getStart() {
        return fieldStart;
    }

    @Override
    public Object jdoGetVersion() {
        return null;
    }

    @Override
    public boolean jdoIsDetached() {
        return false;
    }

    @Override
    public Object jdoNewObjectIdInstance(Object o) {
        return null;
    }
}

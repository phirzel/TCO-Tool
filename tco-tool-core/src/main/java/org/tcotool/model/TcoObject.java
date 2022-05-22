package org.tcotool.model;

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

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbNumericFieldDescriptor;
import ch.softenvironment.jomm.descriptor.DbTextFieldDescriptor;
import ch.softenvironment.jomm.mvc.model.DbEntityBean;

/**
 * Generic TCO-object.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public abstract class TcoObject extends DbEntityBean {

    /*
     * private java.util.List fieldTagId = new java.util.ArrayList(); public
     * java.util.List getTag() { refresh(false); // read lazy initialized
     * objects return fieldTagId; } public void setTag(java.util.List tag) {
     * java.util.List oldValue=fieldTagId; fieldTagId=tag;
     * firePropertyChange("tag", oldValue, fieldTagId); }
     */
    public TcoObject(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(TcoObject.class);
        descriptor.add("name", "name", new DbTextFieldDescriptor(250), new DbMultiplicityRange(1, 1));
        descriptor.add("documentation", "documentation", new DbTextFieldDescriptor(1024), new DbMultiplicityRange(0, 1));
        descriptor.add("multitude", "multitude", new DbNumericFieldDescriptor(java.lang.Double.class, 0.0, 9999999.0, 2), new DbMultiplicityRange(1, 1));
        descriptor.addAssociationAttributed(DbDescriptor.AGGREGATION, "clientId", new DbMultiplicityRange(0, DbMultiplicityRange.UNBOUND), new DbMultiplicityRange(0, DbMultiplicityRange.UNBOUND),
            Dependency.class, "supplierId");
        descriptor.addAssociationAttributed(DbDescriptor.ASSOCIATION, "supplierId", new DbMultiplicityRange(0, DbMultiplicityRange.UNBOUND), new DbMultiplicityRange(0, DbMultiplicityRange.UNBOUND),
            Dependency.class, "clientId");
        // TODO comparable configurations
        // descriptor.addAssociationAttributed(DbDescriptor.AGGREGATION,
        // "objectId", new DbMultiplicityRange(0,DbMultiplicityRange.UNBOUND),
        // new DbMultiplicityRange(0,DbMultiplicityRange.UNBOUND),
        // ConfigurationTag.class, "tagId");
        return descriptor;
    }

    private java.util.List<Dependency> fieldClientId = new java.util.ArrayList<Dependency>();

    public java.util.List<Dependency> getClientId() {
        refresh(false); // read lazy initialized objects
        return fieldClientId;
    }

    public void setClientId(java.util.List<Dependency> client) {
        java.util.List<Dependency> oldValue = fieldClientId;
        fieldClientId = client;
        firePropertyChange("clientId", oldValue, fieldClientId);
    }

    private String fieldDocumentation;

    public String getDocumentation() {
        refresh(false);
        return fieldDocumentation;
    }

    public void setDocumentation(String documentation) {
        String oldValue = fieldDocumentation;
        fieldDocumentation = documentation;
        firePropertyChange("documentation", oldValue, fieldDocumentation);
    }

    private java.lang.Double fieldMultitude; // NOT NULL

    public java.lang.Double getMultitude() {
        refresh(false);
        return fieldMultitude;
    }

    public void setMultitude(java.lang.Double multitude) {
        java.lang.Double oldValue = fieldMultitude;
        fieldMultitude = multitude;
        firePropertyChange("multitude", oldValue, fieldMultitude);
    }

    private String fieldName; // NOT NULL

    public String getName() {
        refresh(false);
        return fieldName;
    }

    public void setName(String name) {
        String oldValue = fieldName;
        fieldName = name;
        firePropertyChange("name", oldValue, fieldName);
    }

    private java.util.List<Dependency> fieldSupplierId = new java.util.ArrayList<Dependency>();

    public java.util.List<Dependency> getSupplierId() {
        refresh(false);
        return fieldSupplierId;
    }

    public void setSupplierId(java.util.List<Dependency> supplier) {
        java.util.List<Dependency> oldValue = fieldSupplierId;
        fieldSupplierId = supplier;
        firePropertyChange("supplierId", oldValue, fieldSupplierId);
    }
}

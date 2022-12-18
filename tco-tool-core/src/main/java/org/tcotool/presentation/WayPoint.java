package org.tcotool.presentation;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbNumericFieldDescriptor;
import ch.softenvironment.jomm.mvc.model.DbEntityBean;

/**
 * A WayPoint is a constraint on the geometry of the actual presentation of this Edge (the "zick-zack position of a line").
 *
 * @author Peter Hirzel
 */
public class WayPoint extends DbEntityBean {

    public WayPoint(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    public java.lang.Long fieldEast;

    public java.lang.Long getEast() {
        refresh(false); // read lazy initialized objects
        return fieldEast;
    }

    public void setEast(java.lang.Long east) {
        java.lang.Long oldValue = fieldEast;
        fieldEast = east;
        firePropertyChange("east", oldValue, fieldEast);
    }

    public java.lang.Long fieldSouth;

    public java.lang.Long getSouth() {
        refresh(false); // read lazy initialized objects
        return fieldSouth;
    }

    public void setSouth(java.lang.Long south) {
        java.lang.Long oldValue = fieldSouth;
        fieldSouth = south;
        firePropertyChange("south", oldValue, fieldSouth);
    }

    public java.lang.Long fieldEdgeId;

    public java.lang.Long getEdgeId() {
        refresh(false); // read lazy initialized objects
        return fieldEdgeId;
    }

    public void setEdgeId(java.lang.Long edge) {
        java.lang.Long oldValue = fieldEdgeId;
        fieldEdgeId = edge;
        firePropertyChange("edgeId", oldValue, fieldEdgeId);
    }

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(WayPoint.class);
        descriptor.add("east", "east", new DbNumericFieldDescriptor(java.lang.Long.class, 0.0, 999999.0, 0), new DbMultiplicityRange(0, 1));
        descriptor.add("south", "south", new DbNumericFieldDescriptor(java.lang.Long.class, 0.0, 999999.0, 0), new DbMultiplicityRange(0, 1));
        descriptor.addManyToOneReferenceId(DbDescriptor.ASSOCIATION, "edgeId", "T_Id_edge", new DbMultiplicityRange(1, 1));
        return descriptor;
    }
}

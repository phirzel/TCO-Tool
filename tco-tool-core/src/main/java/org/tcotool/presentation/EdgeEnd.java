package org.tcotool.presentation;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.mvc.model.DbRelationshipBean;

/**
 * Specifies an end of an edge.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class EdgeEnd extends DbRelationshipBean {

    public EdgeEnd(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
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

    public java.lang.Long fieldEndPointId;

    public java.lang.Long getEndPointId() {
        refresh(false); // read lazy initialized objects
        return fieldEndPointId;
    }

    public void setEndPointId(java.lang.Long endPoint) {
        java.lang.Long oldValue = fieldEndPointId;
        fieldEndPointId = endPoint;
        firePropertyChange("endPointId", oldValue, fieldEndPointId);
    }

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(EdgeEnd.class);
        descriptor.addAssociationEnd(PresentationEdge.class, "edgeId", "T_Id_edge");
        descriptor.addAssociationEnd(PresentationElement.class, "endPointId", "T_Id_endPoint");
        return descriptor;
    }
}

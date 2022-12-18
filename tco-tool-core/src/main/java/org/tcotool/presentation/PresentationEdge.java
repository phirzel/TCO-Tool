package org.tcotool.presentation;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;

/**
 * The root of line-style presentations.
 *
 * @author Peter Hirzel
 */
public class PresentationEdge extends PresentationElement {

    public PresentationEdge(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    public java.util.List<EdgeEnd> fieldEndPointId = new java.util.ArrayList<>();

    public java.util.List<EdgeEnd> getEndPointId() {
        refresh(false); // read lazy initialized objects
        return fieldEndPointId;
    }

    public void setEndPointId(java.util.List<EdgeEnd> endPoint) {
        java.util.List<EdgeEnd> oldValue = fieldEndPointId;
        fieldEndPointId = endPoint;
        firePropertyChange("endPointId", oldValue, fieldEndPointId);
    }

    public java.util.List<WayPoint> fieldWayPoint = new java.util.ArrayList<>();

    public java.util.List<WayPoint> getWayPoint() {
        refresh(false); // read lazy initialized objects
        return fieldWayPoint;
    }

    public void setWayPoint(java.util.List<WayPoint> wayPoint) {
        java.util.List<WayPoint> oldValue = fieldWayPoint;
        fieldWayPoint = wayPoint;
        firePropertyChange("wayPoint", oldValue, fieldWayPoint);
	}

	public static DbDescriptor createDescriptor() {
		DbDescriptor descriptor = new DbDescriptor(PresentationEdge.class);
		descriptor.addAssociationAttributed(DbDescriptor.ASSOCIATION, "endPointId", new DbMultiplicityRange(0, DbMultiplicityRange.UNBOUND), new DbMultiplicityRange(2, DbMultiplicityRange.UNBOUND),
			EdgeEnd.class, "edgeId");
		descriptor.addOneToMany(DbDescriptor.COMPOSITION, "wayPoint", "edgeId", new DbMultiplicityRange(0, DbMultiplicityRange.UNBOUND), WayPoint.class, false);
		return descriptor;
	}
}

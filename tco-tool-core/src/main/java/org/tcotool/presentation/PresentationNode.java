package org.tcotool.presentation;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbNumericFieldDescriptor;

/**
 * Represents a visual node in a Diagram (symbol-style presentation).
 *
 * @author Peter Hirzel
 */
public class PresentationNode extends PresentationElement {

    public PresentationNode(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    public java.lang.Long fieldWidth;

    public java.lang.Long getWidth() {
        refresh(false); // read lazy initialized objects
        return fieldWidth;
    }

    public void setWidth(java.lang.Long width) {
        java.lang.Long oldValue = fieldWidth;
        fieldWidth = width;
        firePropertyChange("width", oldValue, fieldWidth);
    }

    public java.lang.Long fieldHeight;

    public java.lang.Long getHeight() {
        refresh(false); // read lazy initialized objects
        return fieldHeight;
    }

    public void setHeight(java.lang.Long height) {
        java.lang.Long oldValue = fieldHeight;
        fieldHeight = height;
        firePropertyChange("height", oldValue, fieldHeight);
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
        // ch.softenvironment.util.Tracer.getInstance().debug(((TcoObject)getSubject()).getName()
        // + "->east=" + east.intValue());
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
        // ch.softenvironment.util.Tracer.getInstance().debug(((TcoObject)getSubject()).getName()
        // + "->south=" + south.intValue());
    }

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(PresentationNode.class);
        descriptor.add("width", "width", new DbNumericFieldDescriptor(java.lang.Long.class, 0.0, 999999.0, 0), new DbMultiplicityRange(0, 1));
        descriptor.add("height", "height", new DbNumericFieldDescriptor(java.lang.Long.class, 0.0, 999999.0, 0), new DbMultiplicityRange(0, 1));
        descriptor.add("east", "east", new DbNumericFieldDescriptor(java.lang.Long.class, 0.0, 999999.0, 0), new DbMultiplicityRange(0, 1));
        descriptor.add("south", "south", new DbNumericFieldDescriptor(java.lang.Long.class, 0.0, 999999.0, 0), new DbMultiplicityRange(0, 1));
        return descriptor;
    }
}

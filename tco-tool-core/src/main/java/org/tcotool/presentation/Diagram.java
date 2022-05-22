package org.tcotool.presentation;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbTextFieldDescriptor;
import ch.softenvironment.jomm.mvc.model.DbEntityBean;

/**
 * A Diagram groups different PresentationElement's in graphical manner.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class Diagram extends DbEntityBean {

    public Diagram(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    public String fieldName;

    public String getName() {
        refresh(false); // read lazy initialized objects
        return fieldName;
    }

    public void setName(String name) {
        String oldValue = fieldName;
        fieldName = name;
        firePropertyChange("name", oldValue, fieldName);
    }

    public java.util.List<PresentationElement> fieldPresentationElement = new java.util.ArrayList<PresentationElement>();

    public java.util.List<PresentationElement> getPresentationElement() {
        refresh(false); // read lazy initialized objects
        return fieldPresentationElement;
    }

    public void setPresentationElement(java.util.List<PresentationElement> presentationElement) {
        java.util.List<PresentationElement> oldValue = fieldPresentationElement;
        fieldPresentationElement = presentationElement;
        firePropertyChange("presentationElement", oldValue, fieldPresentationElement);
    }

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(Diagram.class);
        descriptor.add("name", "name", new DbTextFieldDescriptor(255), new DbMultiplicityRange(0, 1));
        descriptor.addOneToMany(DbDescriptor.COMPOSITION, "presentationElement", "diagramId",
            new DbMultiplicityRange(0, DbMultiplicityRange.UNBOUND), PresentationElement.class, false);
        return descriptor;
    }
}

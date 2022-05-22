package org.tcotool.presentation;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbNumericFieldDescriptor;
import ch.softenvironment.jomm.descriptor.DbTextFieldDescriptor;
import ch.softenvironment.jomm.mvc.model.DbEntityBean;

/**
 * A presentation element is a textual or graphical presentation of a model element. (To be found in UML-core 1.4 model.)
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public abstract class PresentationElement extends DbEntityBean {

	public PresentationElement(ch.softenvironment.jomm.DbObjectServer objectServer) {
		super(objectServer);
	}

	public String fieldFont;

	public String getFont() {
		refresh(false); // read lazy initialized objects
		return fieldFont;
	}

	public void setFont(String font) {
		String oldValue = fieldFont;
		fieldFont = font;
		firePropertyChange("font", oldValue, fieldFont);
	}

	public java.lang.Long fieldForeground;

	public java.lang.Long getForeground() {
		refresh(false); // read lazy initialized objects
		return fieldForeground;
	}

	public void setForeground(java.lang.Long foreground) {
		java.lang.Long oldValue = fieldForeground;
		fieldForeground = foreground;
		firePropertyChange("foreground", oldValue, fieldForeground);
	}

	public java.lang.Long fieldBackground;

	public java.lang.Long getBackground() {
		refresh(false); // read lazy initialized objects
		return fieldBackground;
	}

	public void setBackground(java.lang.Long background) {
		java.lang.Long oldValue = fieldBackground;
		fieldBackground = background;
		firePropertyChange("background", oldValue, fieldBackground);
	}

	public java.lang.Object fieldSubject;

	public java.lang.Object getSubject() {
		refresh(false); // read lazy initialized objects
		return fieldSubject;
	}

	public void setSubject(java.lang.Object subject) {
		java.lang.Object oldValue = fieldSubject;
		fieldSubject = subject;
		firePropertyChange("subjectId", oldValue, fieldSubject);
	}

	public java.util.List<EdgeEnd> fieldEdgeId = new java.util.ArrayList<EdgeEnd>();

	public java.util.List<EdgeEnd> getEdgeId() {
		refresh(false); // read lazy initialized objects
		return fieldEdgeId;
	}

	public void setEdgeId(java.util.List<EdgeEnd> edge) {
		java.util.List<EdgeEnd> oldValue = fieldEdgeId;
		fieldEdgeId = edge;
		firePropertyChange("edgeId", oldValue, fieldEdgeId);
	}

	public java.lang.Long fieldDiagramId;

	public java.lang.Long getDiagramId() {
		refresh(false); // read lazy initialized objects
		return fieldDiagramId;
	}

	public void setDiagramId(java.lang.Long diagram) {
		java.lang.Long oldValue = fieldDiagramId;
		fieldDiagramId = diagram;
		firePropertyChange("diagramId", oldValue, fieldDiagramId);
	}

	public static DbDescriptor createDescriptor() {
		DbDescriptor descriptor = new DbDescriptor(PresentationElement.class);
		descriptor.add("font", "font", new DbTextFieldDescriptor(255), new DbMultiplicityRange(0, 1));
		descriptor.add("foreground", "foreground", new DbNumericFieldDescriptor(java.lang.Long.class, 0.0, 1.6777215E7, 0), new DbMultiplicityRange(0, 1));
		descriptor.add("background", "background", new DbNumericFieldDescriptor(java.lang.Long.class, 0.0, 1.6777215E7, 0), new DbMultiplicityRange(0, 1));
		descriptor.addManyToOneReference(DbDescriptor.AGGREGATION, "subject", "T_Id_subject", new DbMultiplicityRange(1, 1));
		descriptor.addAssociationAttributed(DbDescriptor.ASSOCIATION, "edgeId", new DbMultiplicityRange(2, DbMultiplicityRange.UNBOUND), new DbMultiplicityRange(0, DbMultiplicityRange.UNBOUND),
			EdgeEnd.class, "endPointId");
		descriptor.addManyToOneReferenceId(DbDescriptor.ASSOCIATION, "diagramId", "T_Id_diagram", new DbMultiplicityRange(1, 1));
		return descriptor;
	}
}

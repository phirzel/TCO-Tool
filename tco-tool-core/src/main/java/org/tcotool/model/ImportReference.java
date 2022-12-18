package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbNumericFieldDescriptor;
import ch.softenvironment.jomm.descriptor.DbTextFieldDescriptor;
import ch.softenvironment.jomm.mvc.model.DbEntityBean;

/**
 * Manage a reference of an imported TCO file.
 *
 * @author Peter Hirzel
 */
@Deprecated
public class ImportReference extends DbEntityBean {

    public ImportReference(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    private String fieldImportFile;

    public String getImportFile() {
        refresh(false); // read lazy initialized objects
        return fieldImportFile;
    }

    public void setImportFile(String importFile) {
        String oldValue = fieldImportFile;
        fieldImportFile = importFile;
        firePropertyChange("importFile", oldValue, fieldImportFile);
    }

    private java.lang.Long fieldExternalId;

    public java.lang.Long getExternalId() {
        refresh(false); // read lazy initialized objects
        return fieldExternalId;
    }

    public void setExternalId(java.lang.Long externalId) {
        java.lang.Long oldValue = fieldExternalId;
        fieldExternalId = externalId;
        firePropertyChange("externalId", oldValue, fieldExternalId);
    }

    private java.lang.Long fieldImporterId;

    public java.lang.Long getImporterId() {
        refresh(false); // read lazy initialized objects
        return fieldImporterId;
    }

    public void setImporterId(java.lang.Long importer) {
        java.lang.Long oldValue = fieldImporterId;
        fieldImporterId = importer;
        firePropertyChange("importerId", oldValue, fieldImporterId);
    }

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(ImportReference.class);
        descriptor.add("importFile", "importFile", new DbTextFieldDescriptor(
            1023), new DbMultiplicityRange(0, 1));
        descriptor.add("externalId", "externalId",
            new DbNumericFieldDescriptor(java.lang.Long.class, 0.0,
                9999999.0, 0), new DbMultiplicityRange(0, 1));
        descriptor.addOneToOneReferenceId(DbDescriptor.ASSOCIATION,
            "importerId", "T_Id_importer", new DbMultiplicityRange(1, 1),
            false);
        return descriptor;
    }
}

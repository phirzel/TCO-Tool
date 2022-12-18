package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbTextFieldDescriptor;
import ch.softenvironment.jomm.mvc.model.DbEntityBean;

/**
 * Import/Export data for split models (external spreadsheet treatment for example).
 * <p>
 * (Mapped to "tcotool.model.ImportReference")
 *
 * @author Peter Hirzel
 * @see TcoObject
 * @deprecated experimental trial
 */
@Deprecated
public class ConfigurationTag extends DbEntityBean {

    public ConfigurationTag(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    private String fieldName;

    public String getName() {
        refresh(false); // read lazy initialized objects
        return fieldName;
    }

    public void setName(String name) {
        String oldValue = fieldName;
        fieldName = name;
        firePropertyChange("name", oldValue, fieldName);
    }

    /*
     * private String fieldImportFile; public String getImportFile() {
     * refresh(false); // read lazy initialized objects return fieldImportFile;
     * } public void setImportFile(String importFile){ String
     * oldValue=fieldImportFile; fieldImportFile=importFile;
     * firePropertyChange("importFile", oldValue, fieldImportFile); }
     *
     * private java.lang.Long fieldExternalId; public java.lang.Long
     * getExternalId() { refresh(false); // read lazy initialized objects return
     * fieldExternalId; } public void setExternalId(java.lang.Long externalId){
     * java.lang.Long oldValue=fieldExternalId; fieldExternalId=externalId;
     * firePropertyChange("externalId", oldValue, fieldExternalId); }
     */
    
    /*
    private java.util.List fieldObjectId;

    public java.util.List getObjectId() {
	refresh(false); // read lazy initialized objects
	return fieldObjectId;
    }

    public void setObjectId(java.util.List id) {
	java.util.List oldValue = fieldObjectId;
	fieldObjectId = id;
	firePropertyChange("objectId", oldValue, fieldObjectId);
    }
    */

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(ConfigurationTag.class);
        descriptor.add("name", "name", new DbTextFieldDescriptor(50), new DbMultiplicityRange(1, 1));
        // descriptor.add("importFile","importFile",new
        // DbTextFieldDescriptor(1023),new DbMultiplicityRange(0,1));
        // descriptor.add("externalId","externalId",new
        // DbNumericFieldDescriptor(java.lang.Long.class,0.0,9999999.0,0),new
        // DbMultiplicityRange(0,1));
        // TODO Comparable Configurations
        // descriptor.addAssociationAttributed(DbDescriptor.ASSOCIATION,
        // "tagId", new DbMultiplicityRange(0,DbMultiplicityRange.UNBOUND), new
        // DbMultiplicityRange(0,DbMultiplicityRange.UNBOUND),
        // Configuration.class, "objectId");
        return descriptor;
    }
}

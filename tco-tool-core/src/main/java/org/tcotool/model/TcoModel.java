package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbTextFieldDescriptor;

/**
 * The ROOT-Element of a TCO-Configuration.
 *
 * @author Peter Hirzel
 */
public class TcoModel extends TcoPackage {

    public TcoModel(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    /*
      private Branch fieldBranch;
      public Branch getBranch() {
          refresh(false); // read lazy initialized objects
          return fieldBranch;
        }
        public void setBranch(Branch branch){
          Branch oldValue=fieldBranch;
          fieldBranch=branch;
          firePropertyChange("branch", oldValue, fieldBranch);
        }
    */
    private String fieldAuthor;

    public String getAuthor() {
        refresh(false); // read lazy initialized objects
        return fieldAuthor;
    }

    public void setAuthor(String author) {
        String oldValue = fieldAuthor;
        fieldAuthor = author;
        firePropertyChange("author", oldValue, fieldAuthor);
    }

    private String fieldVersion;

    public String getVersion() {
        refresh(false); // read lazy initialized objects
        return fieldVersion;
    }

    public void setVersion(String version) {
        String oldValue = fieldVersion;
        fieldVersion = version;
        firePropertyChange("version", oldValue, fieldVersion);
    }

    private SystemParameter fieldSystemParameter;

    public SystemParameter getSystemParameter() {
        refresh(false); // read lazy initialized objects
        return fieldSystemParameter;
    }

    public void setSystemParameter(SystemParameter systemParameter) {
        SystemParameter oldValue = fieldSystemParameter;
        fieldSystemParameter = systemParameter;
        firePropertyChange("systemParameter", oldValue, fieldSystemParameter);
    }

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(TcoModel.class);
        descriptor.add("author", "author", new DbTextFieldDescriptor(100), new DbMultiplicityRange(0, 1));
        descriptor.add("version", "version", new DbTextFieldDescriptor(20), new DbMultiplicityRange(0, 1));
        descriptor.addOneToOne(DbDescriptor.COMPOSITION, "systemParameter", "modelId", new DbMultiplicityRange(0, 1), false);
        //TODO add Branch
        return descriptor;
    }
}

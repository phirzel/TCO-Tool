package test.model.assoc;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.mvc.model.DbEntityBean;

/**
 * @author generated by the umleditor
 */
public class ClassA_1 extends DbEntityBean {

  public ClassA_1(ch.softenvironment.jomm.DbObjectServer objectServer) {
    super(objectServer);
  }

  public java.util.List fieldBId = new java.util.ArrayList();

  public java.util.List getBId() {
    refresh(false); // read lazy initialized objects
    return fieldBId;
  }

  public void setBId(java.util.List b) {
    java.util.List oldValue = fieldBId;
    fieldBId = b;
    firePropertyChange("bId", oldValue, fieldBId);
  }

  public static DbDescriptor createDescriptor() {
    DbDescriptor descriptor = new DbDescriptor(ClassA_1.class);
    descriptor.addAssociationAttributed(DbDescriptor.ASSOCIATION, "bId", new DbMultiplicityRange(0, DbMultiplicityRange.UNBOUND), new DbMultiplicityRange(0, DbMultiplicityRange.UNBOUND), A2b_1.class,
        "aId");
    return descriptor;
  }
}

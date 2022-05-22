package test.model.structattr;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.mvc.model.DbEntityBean;

/**
 * @author generated by the umleditor
 */
public class ClassB_2 extends DbEntityBean {

  public ClassB_2(ch.softenvironment.jomm.DbObjectServer objectServer) {
    super(objectServer);
  }

  public java.lang.Long fieldClassA_2_bId;

  public java.lang.Long getClassA_2_bId() {
    refresh(false); // read lazy initialized objects
    return fieldClassA_2_bId;
  }

  public void setClassA_2_bId(java.lang.Long classA_2_b) {
    java.lang.Long oldValue = fieldClassA_2_bId;
    fieldClassA_2_bId = classA_2_b;
    firePropertyChange("classA_2_bId", oldValue, fieldClassA_2_bId);
  }

  public static DbDescriptor createDescriptor() {
    DbDescriptor descriptor = new DbDescriptor(ClassB_2.class);
    descriptor.addManyToOneReferenceId(DbDescriptor.ASSOCIATION, "classA_2_bId", "ClassA_2_b", new DbMultiplicityRange(1, 1));
    return descriptor;
  }
}

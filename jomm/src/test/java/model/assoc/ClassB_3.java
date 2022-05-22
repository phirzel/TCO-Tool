package test.model.assoc;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.mvc.model.DbEntityBean;

/**
 * @author generated by the umleditor
 */
public class ClassB_3 extends DbEntityBean {

  public ClassB_3(ch.softenvironment.jomm.DbObjectServer objectServer) {
    super(objectServer);
  }

  public java.lang.Long fieldAId;

  public java.lang.Long getAId() {
    refresh(false); // read lazy initialized objects
    return fieldAId;
  }

  public void setAId(java.lang.Long a) {
    java.lang.Long oldValue = fieldAId;
    fieldAId = a;
    firePropertyChange("aId", oldValue, fieldAId);
  }

  public static DbDescriptor createDescriptor() {
    DbDescriptor descriptor = new DbDescriptor(ClassB_3.class);
    descriptor.addManyToOneReferenceId(DbDescriptor.ASSOCIATION, "aId", "T_Id_a", new DbMultiplicityRange(1, 1));
    return descriptor;
  }
}

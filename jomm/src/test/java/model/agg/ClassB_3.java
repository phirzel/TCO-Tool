package test.model.agg;

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

  public java.util.List fieldA = new java.util.ArrayList();

  public java.util.List getA() {
    refresh(false); // read lazy initialized objects
    return fieldA;
  }

  public void setA(java.util.List a) {
    java.util.List oldValue = fieldA;
    fieldA = a;
    firePropertyChange("a", oldValue, fieldA);
  }

  public static DbDescriptor createDescriptor() {
    DbDescriptor descriptor = new DbDescriptor(ClassB_3.class);
    descriptor.addOneToMany(DbDescriptor.ASSOCIATION, "a", "bId", new DbMultiplicityRange(0, DbMultiplicityRange.UNBOUND), ClassA_3.class, false);
    return descriptor;
  }

  @Override
  public Object jdoGetVersion() {
    //TODO HIP just added to compile
    return null;
  }

  @Override
  public boolean jdoIsDetached() {
    //TODO HIP just added to compile
    return false;
  }

  @Override
  public Object jdoNewObjectIdInstance(Object o) {
    //TODO HIP just added to compile
    return null;
  }
}

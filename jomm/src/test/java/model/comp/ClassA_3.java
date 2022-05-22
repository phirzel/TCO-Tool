package test.model.comp;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.mvc.model.DbEntityBean;

/**
 * @author generated by the umleditor
 */
public class ClassA_3 extends DbEntityBean {

  public ClassA_3(ch.softenvironment.jomm.DbObjectServer objectServer) {
    super(objectServer);
  }

  public ClassB_3 fieldB;

  public ClassB_3 getB() {
    refresh(false); // read lazy initialized objects
    return fieldB;
  }

  public void setB(ClassB_3 b) {
    ClassB_3 oldValue = fieldB;
    fieldB = b;
    firePropertyChange("b", oldValue, fieldB);
  }

  public static DbDescriptor createDescriptor() {
    DbDescriptor descriptor = new DbDescriptor(ClassA_3.class);
    descriptor.addOneToOne(DbDescriptor.COMPOSITION, "b", "aId", new DbMultiplicityRange(1, 1));
    return descriptor;
  }
}

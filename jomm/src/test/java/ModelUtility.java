import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.util.Tracer;

/**
 * @author generated by the umleditor
 */
public final class ModelUtility {

  public static void main(java.lang.String[] args) {
    Tracer.start(Tracer.ALL);

    javax.jdo.PersistenceManagerFactory pmFactory = new ch.softenvironment.jomm.target.xml.XmlObjectServerFactory();
    pmFactory.setConnectionURL("");
    pmFactory.setNontransactionalRead(false);    // NO autoCommit while reading
    pmFactory.setNontransactionalWrite(false);    // NO autoCommit while writing

    DbObjectServer server = (DbObjectServer) pmFactory.getPersistenceManager("", "");
    registerClasses(server);
  }

  private ModelUtility() {
  }

  public static void registerClasses(DbObjectServer server) {
    server.register(test.model.agg.ClassA_2.class, "ModelAgg_ClassA_2");
    server.register(test.model.structattr.ClassA_2.class, "ModelStructAttr_ClassA_2");
    server.register(test.model.comp.ClassA_4.class, "ModelComp_ClassA_4");
    server.register(test.model.assoc.ClassB_4.class, "ModelAssoc_ClassB_4");
    server.register(test.model.agg.ClassA_4.class, "ModelAgg_ClassA_4");
    server.register(test.model.comp.ClassA_1.class, "ModelComp_ClassA_1");
    server.register(test.model.comp.ClassB_3.class, "ModelComp_ClassB_3");
    server.register(test.model.agg.ClassA_3.class, "ModelAgg_ClassA_3");
    server.register(test.model.assoc.ClassA_4.class, "ModelAssoc_ClassA_4");
    server.register(test.model.agg.ClassB_3.class, "ModelAgg_ClassB_3");
    server.register(test.model.comp.ClassA_2.class, "ModelComp_ClassA_2");
    server.register(test.model.assoc.ClassA_1.class, "ModelAssoc_ClassA_1");
    server.register(test.model.structattr.ClassB_1.class, "ModelStructAttr_ClassB_1");
    server.register(test.model.comp.ClassB_1.class, "ModelComp_ClassB_1");
    server.register(test.model.agg.ClassB_2.class, "ModelAgg_ClassB_2");
    server.register(test.model.assoc.ClassB_3.class, "ModelAssoc_ClassB_3");
    server.register(test.model.comp.ClassB_2.class, "ModelComp_ClassB_2");
    server.register(test.model.assoc.A2b_1.class, "ModelAssoc_a2b_1");
    server.register(test.model.comp.ClassB_4.class, "ModelComp_ClassB_4");
    server.register(test.model.assoc.ClassA_3.class, "ModelAssoc_ClassA_3");
    server.register(test.model.agg.A2b_1.class, "ModelAgg_a2b_1");
    server.register(test.model.agg.ClassB_4.class, "ModelAgg_ClassB_4");
    server.register(test.model.agg.ClassA_1.class, "ModelAgg_ClassA_1");
    server.register(test.model.agg.ClassB_1.class, "ModelAgg_ClassB_1");
    server.register(test.model.assoc.ClassB_1.class, "ModelAssoc_ClassB_1");
    server.register(test.model.structattr.ClassA_1.class, "ModelStructAttr_ClassA_1");
    server.register(test.model.assoc.ClassB_2.class, "ModelAssoc_ClassB_2");
    server.register(test.model.assoc.ClassA_2.class, "ModelAssoc_ClassA_2");
    server.register(test.model.structattr.ClassB_2.class, "ModelStructAttr_ClassB_2");
    server.register(test.model.comp.ClassA_3.class, "ModelComp_ClassA_3");
  }
}

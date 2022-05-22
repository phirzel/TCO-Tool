package ch.softenvironment.jomm.target.xml;

import ch.softenvironment.jomm.DbMapper;
import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbFieldType;
import ch.softenvironment.jomm.implementation.DbPropertyChange;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.jomm.serialize.InterlisSerializer;
import ch.softenvironment.util.DeveloperException;
import java.math.BigDecimal;
import java.util.Date;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

/**
 * Java<->XML-Target Adapter.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class XmlMapper implements DbMapper {

    // TODO should not be protected
    protected static final String SE_NLS = "softEnvironment.Nls";
    protected static final String SE_NLS_TRANSLATION = "softEnvironment.Translation";

    protected XmlMapper() {
        super();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.softenvironment.jomm.DbMapper#describeTargetException(java.lang.Exception
     * )
     */
    @Override
    public String describeTargetException(Exception exception) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.softenvironment.jomm.DbMapper#getNewId(javax.jdo.PersistenceManager,
     * javax.jdo.Transaction, java.lang.String)
     */
    @Override
    public Long getNewId(PersistenceManager objectServer,
        Transaction transaction, String key) {
        // TODO Auto-generated method stub
        throw new DeveloperException("HACK: @see XMLObjectServer#getNewId()");
        // return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.jomm.DbMapper#hasNext(java.lang.Object)
     */
    @Override
    public boolean hasNext(Object collection) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.softenvironment.jomm.DbMapper#mapFromTarget(ch.softenvironment.jomm
     * .DbObject, ch.softenvironment.jomm.DbDescriptor, java.lang.Object)
     */
    @Override
    public void mapFromTarget(DbObject instance, DbDescriptor descriptor,
        Object collection) throws Exception {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.softenvironment.jomm.DbMapper#mapFromTargetBigDecimal(java.lang.Object
     * , java.lang.String)
     */
    @Override
    public BigDecimal mapFromTargetBigDecimal(Object collection,
        String attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.softenvironment.jomm.DbMapper#mapFromTargetBoolean(java.lang.Object,
     * java.lang.String)
     */
    @Override
    public Boolean mapFromTargetBoolean(Object collection, String attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.jomm.DbMapper#mapFromTargetDate(java.lang.Object,
     * java.lang.String, int)
     */
    @Override
    public Date mapFromTargetDate(Object collection, String attribute, int type) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.softenvironment.jomm.DbMapper#mapFromTargetDbFieldType(java.lang.Object
     * , java.lang.String)
     */
    @Override
    public DbFieldType mapFromTargetDbFieldType(Object collection,
        String attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.softenvironment.jomm.DbMapper#mapFromTargetDouble(java.lang.Object,
     * java.lang.String)
     */
    @Override
    public Double mapFromTargetDouble(Object collection, String attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.softenvironment.jomm.DbMapper#mapFromTargetInteger(java.lang.Object,
     * java.lang.String)
     */
    @Override
    public Integer mapFromTargetInteger(Object collection, String attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.jomm.DbMapper#mapFromTargetLong(java.lang.Object,
     * java.lang.String)
     */
    @Override
    public Long mapFromTargetLong(Object collection, String attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.softenvironment.jomm.DbMapper#mapFromTargetNativeBoolean(java.lang
     * .Object, java.lang.String)
     */
    @Override
    public boolean mapFromTargetNativeBoolean(Object collection,
        String attribute) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.softenvironment.jomm.DbMapper#mapFromTargetNativeInt(java.lang.Object,
     * java.lang.String)
     */
    @Override
    public int mapFromTargetNativeInt(Object collection, String attribute) {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.softenvironment.jomm.DbMapper#mapFromTargetString(java.lang.Object,
     * java.lang.String)
     */
    @Override
    public String mapFromTargetString(Object collection, String attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.softenvironment.jomm.DbMapper#mapToTarget(ch.softenvironment.jomm.
     * DbFieldType)
     */
    @Override
    public Object mapToTarget(DbFieldType value) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.softenvironment.jomm.DbMapper#mapToTarget(ch.softenvironment.jomm.
     * DbObject)
     */
    @Override
    public Object mapToTarget(DbObject value) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.softenvironment.jomm.DbMapper#mapToTarget(ch.softenvironment.jomm.
     * DbPropertyChange)
     */
    @Override
    public Object mapToTarget(DbPropertyChange change) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.jomm.DbMapper#mapToTarget(java.lang.Boolean)
     */
    @Override
    public Object mapToTarget(Boolean value) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.jomm.DbMapper#mapToTarget(java.lang.Number)
     */
    @Override
    public Object mapToTarget(Number value) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.jomm.DbMapper#mapToTarget(java.lang.String)
     */
    @Override
    public Object mapToTarget(String value) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.jomm.DbMapper#mapToTarget(java.util.Date, int)
     */
    @Override
    public Object mapToTarget(Date value, int type) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.softenvironment.jomm.DbMapper#mapToTargetQualified(java.lang.String,
     * java.lang.String)
     */
    @Override
    public String mapToTargetQualified(String qualifier, String attribute) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.jomm.DbMapper#getTargetIdName()
     */
    @Override
    public String getTargetIdName() {
        return InterlisSerializer.TECHNICAL_ID;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.jomm.DbMapper#getTargetNlsName()
     */
    @Override
    public String getTargetNlsName() {
        return SE_NLS;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.jomm.DbMapper#getTargetNlsTranslationName()
     */
    @Override
    public String getTargetNlsTranslationName() {
        return SE_NLS_TRANSLATION;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.jomm.DbMapper#getTargetRemoveHistoryName()
     */
    @Override
    public String getTargetRemoveHistoryName() {
        return "T_RemoveHistory";
    }
}

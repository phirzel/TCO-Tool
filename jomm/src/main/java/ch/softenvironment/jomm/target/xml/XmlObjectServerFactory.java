package ch.softenvironment.jomm.target.xml;

/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import ch.softenvironment.jomm.DbObjectServer;
import java.util.Collection;
import java.util.Set;
import javax.jdo.FetchGroup;
import javax.jdo.PersistenceManager;
import javax.jdo.datastore.DataStoreCache;
import javax.jdo.listener.InstanceLifecycleListener;
import javax.jdo.metadata.JDOMetadata;
import javax.jdo.metadata.TypeMetadata;

/**
 * @author Peter Hirzel, softEnvironment GmbH
 */
@SuppressWarnings("serial")
public class XmlObjectServerFactory extends ch.softenvironment.jomm.DbDomainNameServer {

    /**
     * Default XMLReader. Contained in JAXP resp. JRE (means smaller jar but might differ in JRE!)
     */
    @Deprecated
    public final static String XML_READER_CRIMSON = "org.apache.crimson.parser.XMLReaderImpl";
    /**
     * Needs Xerces-J_V1_4_4 (or higher): - Xerces-J 1.4.4 => needs "xerces.jar" - Xerces-J 2.9.0 => needs "xercesImpl.jar" & "xml-apis.jar"
     */
    @Deprecated
    public final static String XML_READER_XERCES = "org.apache.xerces.parsers.SAXParser";

    private String xmlReaderImpl = null;

    /**
     * Constructor. Use the default XML-Parser of Java.
     */
    public XmlObjectServerFactory() {
        this(null/* XML_READER_CRIMSON */);
    }

    /**
     * Use a specific XML Parser.
     *
     * @param xmlReaderImpl (for e.g. XML_READER_CRIMSON or XML_READER_XERCES)
     */
    public XmlObjectServerFactory(final String xmlReaderImpl) {
        super();

        // do not map against a real Database
        setConnectionDriverName(null /* "sun.jdbc.odbc.JdbcOdbcDriver" */);
        this.xmlReaderImpl = xmlReaderImpl;
    }

    /**
     * Create an implementory ObjectServer. Overwrite for any specific DBMS.
     */
    @Override
    protected javax.jdo.PersistenceManager createPersistenceManager() {
        DbObjectServer objectServer = null;
        try {
            objectServer = new XmlObjectServer(this, connectionPassword, new XmlMapper() /*
             * @
             * see
             * XmlEncoder
             * /
             * XmlDecoder
             */,
                // TODO XmlQueryBuilder.class
                null, XmlConnection.class);
        } catch (Exception e) {
            throw new ch.softenvironment.util.DeveloperException("creation error", null, e);
        }

        return objectServer;
    }

    /**
     * Return the XMLReader class name.
     *
     * @return
     */
    public String getXMLReaderImpl() {
        return xmlReaderImpl;
    }

    @Override
    public boolean isClosed() {
        //TODO HIP just added to compile
        return false;
    }

    @Override
    public PersistenceManager getPersistenceManagerProxy() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public void setMapping(String s) {
        //TODO HIP just added to compile
    }

    @Override
    public String getMapping() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public boolean getDetachAllOnCommit() {
        //TODO HIP just added to compile
        return false;
    }

    @Override
    public void setDetachAllOnCommit(boolean b) {
        //TODO HIP just added to compile
    }

    @Override
    public boolean getCopyOnAttach() {
        //TODO HIP just added to compile
        return false;
    }

    @Override
    public void setCopyOnAttach(boolean b) {
        //TODO HIP just added to compile
    }

    @Override
    public void setName(String s) {
        //TODO HIP just added to compile
    }

    @Override
    public String getName() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public void setPersistenceUnitName(String s) {
        //TODO HIP just added to compile
    }

    @Override
    public String getPersistenceUnitName() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public void setServerTimeZoneID(String s) {
        //TODO HIP just added to compile
    }

    @Override
    public String getServerTimeZoneID() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public void setTransactionType(String s) {
        //TODO HIP just added to compile
    }

    @Override
    public String getTransactionType() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public boolean getReadOnly() {
        //TODO HIP just added to compile
        return false;
    }

    @Override
    public void setReadOnly(boolean b) {
        //TODO HIP just added to compile
    }

    @Override
    public String getTransactionIsolationLevel() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public void setTransactionIsolationLevel(String s) {
        //TODO HIP just added to compile
    }

    @Override
    public void setDatastoreReadTimeoutMillis(Integer integer) {
        //TODO HIP just added to compile
    }

    @Override
    public Integer getDatastoreReadTimeoutMillis() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public void setDatastoreWriteTimeoutMillis(Integer integer) {
        //TODO HIP just added to compile
    }

    @Override
    public Integer getDatastoreWriteTimeoutMillis() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public DataStoreCache getDataStoreCache() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public void addInstanceLifecycleListener(InstanceLifecycleListener instanceLifecycleListener, Class[] classes) {
        //TODO HIP just added to compile
    }

    @Override
    public void removeInstanceLifecycleListener(InstanceLifecycleListener instanceLifecycleListener) {
        //TODO HIP just added to compile
    }

    @Override
    public void addFetchGroups(FetchGroup... fetchGroups) {
        //TODO HIP just added to compile
    }

    @Override
    public void removeFetchGroups(FetchGroup... fetchGroups) {
        //TODO HIP just added to compile
    }

    @Override
    public void removeAllFetchGroups() {
        //TODO HIP just added to compile
    }

    @Override
    public FetchGroup getFetchGroup(Class aClass, String s) {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public Set getFetchGroups() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public void registerMetadata(JDOMetadata jdoMetadata) {
        //TODO HIP just added to compile
    }

    @Override
    public JDOMetadata newMetadata() {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public TypeMetadata getMetadata(String s) {
        //TODO HIP just added to compile
        return null;
    }

    @Override
    public Collection<Class> getManagedClasses() {
        //TODO HIP just added to compile
        return null;
    }
}

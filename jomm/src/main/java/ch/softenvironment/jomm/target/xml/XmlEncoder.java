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

import ch.softenvironment.jomm.mvc.model.DbCode;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.jomm.serialize.InterlisSerializer;
import ch.softenvironment.jomm.serialize.VisitorCallback;
import ch.softenvironment.util.StringUtils;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * Encodes a Java-Object to an XML-Stream.
 *
 * @author ce
 * @author Peter Hirzel
 */
@Slf4j
class XmlEncoder implements VisitorCallback {

    protected static final String ELEMENT_CODE = "CODE";
    protected static final String ATTRIBUTE_TYPE = "TYPE";

    private final XmlObjectServer server;
    private InterlisSerializer ser = null;

    private final Map objectMap = new HashMap(); // key=aDbObject;
    // entry=aDbObjectMapper
    private final Set pendingObjects = new HashSet(); // of DbObject's

    protected XmlEncoder(XmlObjectServer server) {
        super();
        this.server = server;
    }

    /**
     * Keep objects to write to XML-instance in mind. Pending objects are later visited, written and removed from pending list.
     */
    @Override
    public void addPendingObject(Object obj) {
        if ((obj != null) && (!objectMap.containsKey(obj)) && (!pendingObjects.contains(obj))) {
            DbObject object = (DbObject) obj;

            if (!object.getObjectServer().equals(server)) {
                log.warn("Developer warning: object maintained by other ObjectServer is out of responsibility");
                return;
            }

            pendingObjects.add(obj);
        }
    }

    /**
     * The rootObj and its whole subtree will be encoded to XML-Format. The Objects are written according: 1) to their Bean-Attributes (public setX() & getX() Methods) 2) setX(java.util.List)
     * attributes are iterated for each contained element
     */
    protected void encode(java.util.Iterator<IliBasket> basketIterator, Writer out) throws IOException {
        try {
            XmlUserObject userObject = (XmlUserObject) server.getUserObject();

            ser = new InterlisSerializer(out); // rootObj.getObjectServer().getMapper();
            ser.version("1.0", StringUtils.CHARSET_UTF8); // MUST!
            if (!StringUtils.isNullOrEmpty(userObject.getXsl())) {
                ser.stylesheet(userObject.getXsl());
            }
            ser.startElement(InterlisSerializer.ELEMENT_TRANSFER, InterlisSerializer.getIliNamespace(userObject.getXsd()));
            // TODO schema validation compatibility
            ser.comment("SCHEMA-VALIDATION: This XML-Instance does not validate completely to given 'schemaLocation' yet (Future Use)!");
            ser.startElement(InterlisSerializer.ELEMENT_HEADERSECTION, InterlisSerializer.getIliVersion(((XmlUserObject) server.getUserObject()).getSender()));
            ser.element(InterlisSerializer.ELEMENT_ALIAS);
            ser.endElement(InterlisSerializer.ELEMENT_HEADERSECTION);
            ser.startElement(InterlisSerializer.ELEMENT_DATASECTION);

            ser.comment("UNIQUE-ID of contained Object's is maintained by: " + ch.softenvironment.jomm.serialize.InterlisSerializer.TECHNICAL_ID);
            ser.comment("   => Please allocate new " + ch.softenvironment.jomm.serialize.InterlisSerializer.TECHNICAL_ID + "'s above MAX-"
                + InterlisSerializer.TECHNICAL_ID + " in File");

            while (basketIterator.hasNext()) {
                encodeBasket(basketIterator.next());
            }

            ser.endElement(InterlisSerializer.ELEMENT_DATASECTION);
            ser.endElement(InterlisSerializer.ELEMENT_TRANSFER);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            log.error("serious error", e);
        }
    }

    /**
     * Encode an INTERLIS-Basket (BID...).
     *
     * @param basket
     */
    private void encodeBasket(IliBasket basket) throws IOException {
        // start INTERLIS-Basket
        ser.startElement(basket.getElementName(), ser.getIliDataSectionAttributes(ser.getIliID(server.getNewId(null)), null, null, null, null));
        if (!StringUtils.isNullOrEmpty(basket.getComment())) {
            ser.comment(basket.getComment());
        }

        if ((basket.getCodes() != null) && (basket.getCodes().size() > 0)) {
            ser.comment("Codes: global declaration => will be referenced in Model-Tree (see further below)");
            Iterator<Class<? extends DbCodeType>> iterator = basket.getCodes().iterator();
            while (iterator.hasNext()) {
                Class<? extends DbCodeType> type = iterator.next();
                List<? extends DbCodeType> codes = server.retrieveCodes(type);
                if (codes.size() > 0) {
                    ch.softenvironment.jomm.serialize.AttributeList attrs = new ch.softenvironment.jomm.serialize.AttributeList(ATTRIBUTE_TYPE,
                        server.getTargetName(type));
                    ser.startElement(ELEMENT_CODE, attrs);
                    Iterator<? extends DbCodeType> enums = codes.iterator();
                    while (enums.hasNext()) {
                        DbCode code = (ch.softenvironment.jomm.mvc.model.DbCode) enums.next();
                        if (code.getPersistencyState().isPersistent()) {
                            addPendingObject(code); // always encode code here
                            while (pendingObjects.size() > 0) {
                                Object next = pendingObjects.iterator().next();
                                analyzeObject(code); // in case DbCode
                                // aggregates other
                                // DbOjects
                                ((DbObjectMapper) objectMap.get(next)).writeObject(next, ser);
                                pendingObjects.remove(next);
                            }
                        } else {
                            log.warn("Developer warning: Code <" + code + " [" + ch.softenvironment.jomm.serialize.InterlisSerializer.TECHNICAL_ID + "=" + code.getId()
                                + "]> not saved because in state: " + code.getPersistencyState());
                        }
                    }
                    ser.endElement(ELEMENT_CODE);
                } else {
                    log.warn("Developer warning: Code <{}> has no entries", type);
                }
            }
        }

        if (basket.getModelRoot() != null) {
            ser.comment("Model-Tree");
            addPendingObject(basket.getModelRoot());
            while (pendingObjects.size() > 0) {
                Object next = pendingObjects.iterator().next();
                analyzeObject(next);
                if (next != basket.getModelRoot()) {
                    // spare for last entry
                    ((DbObjectMapper) objectMap.get(next)).writeObject(next, ser);

                }
                pendingObjects.remove(next);
            }

            // rootObj should be the last written object
            ser.comment("THE Root-Element of Model-Tree (at last!)");
            ((DbObjectMapper) objectMap.get(basket.getModelRoot())).writeObject(basket.getModelRoot(), ser);
        }

        // end INTERLIS-Basket
        ser.endElement(basket.getElementName());
    }

    /**
     * Return whether given type is a common java.lang.* type
     *
     * @deprecated
     */
    protected static boolean isBuiltinClass(Class<?> type) {
        return (type.equals(java.lang.String.class) || type.equals(java.lang.Double.class) || type.equals(java.lang.Long.class)
            || type.equals(java.lang.Integer.class) || type.equals(java.lang.Boolean.class) || type.equals(java.lang.Character.class)
            || type.equals(java.lang.Byte.class) || type.equals(java.lang.Short.class) || type.equals(java.lang.Float.class));
        // TODO || isPrimitive(); would make sense here
    }

    /**
     * Analyse Object and prepare it before writing.
     */
    private void analyzeObject(Object obj) {
        if (!objectMap.containsKey(obj.getClass()) /* already visited */ && (obj instanceof DbObject)) {
            DbObjectMapper mapper = new DbObjectMapper();
            objectMap.put(obj, mapper);
            mapper.visitObject(obj, this);
        }
    }
}
package ch.softenvironment.jomm.serialize;

import ch.softenvironment.util.DeveloperException;

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
 */

/**
 * XML-Serializer for specific INTERLIS 2.2 data-structures.
 *
 * @author Peter Hirzel
 * @see <a href="http://www.interlis.ch/INTERLIS2.2>INTERLIS 2.2</a>
 * @see <a href="http://www.w3.org/2001/XMLSchema-instance">XML Schema</a>
 */
public class InterlisSerializer extends XmlSerializer {

    public static final String TECHNICAL_ID = "TID";
    public static final String TECHNICAL_ID_PREFIX = "x";
    public static final String BASKET_ID = "BID";
    public static final String ATTRIBUTE_REFERENCE = "REF";
    public static final String ELEMENT_TRANSFER = "TRANSFER";
    public static final String ELEMENT_HEADERSECTION = "HEADERSECTION";
    public static final String ELEMENT_ALIAS = "ALIAS";
    public static final String ELEMENT_DATASECTION = "DATASECTION";

    /**
     * InterlisSerializer constructor comment.
     *
     * @param out java.io.Writer
     */
    public InterlisSerializer(java.io.Writer out) {
        super(out);
    }

    /**
     * Write 2D-Coordinates.
     */
    public void iliCoordValue(
        ch.softenvironment.jomm.datatypes.interlis.IliCoord2d coord)
        throws Exception {
        startElement("COORD");
        element("C1", coord.getLength());
        element("C2", coord.getWidth());
        // element("C3");
        endElement(/* COORD */);
    }

    /**
     * <xsd:attribute name="BID" type="IliID" use="required"/> <xsd:attribute name="TOPICS" type="xsd:string"/> <xsd:attribute name="KIND" type="xsd:string"/> <xsd:attribute name="STARTSTATE"
     * type="xsd:string"/> <xsd:attribute name="ENDSTATE" type="xsd:string"/>
     */
    public AttributeList getIliDataSectionAttributes(final String bid,
        final String topics, final String kind, final String startState,
        final String endState) {
        AttributeList attrs = new AttributeList();
        attrs.add(BASKET_ID, bid);
        if (!ch.softenvironment.util.StringUtils.isNullOrEmpty(topics)) {
            attrs.add("TOPICS", topics);
        }
        if (!ch.softenvironment.util.StringUtils.isNullOrEmpty(kind)) {
            attrs.add("KIND", kind);
        }
        if (!ch.softenvironment.util.StringUtils.isNullOrEmpty(startState)) {
            attrs.add("STARTSTATE", startState);
        }
        if (!ch.softenvironment.util.StringUtils.isNullOrEmpty(endState)) {
            attrs.add("ENDSTATE", endState);
        }
        return attrs;
    }

    /**
     *
     */
    public AttributeList getIliHeaderAttributes(final Long tid,
        final String bid, final String operation) {
        AttributeList attrs = new AttributeList();
        attrs.add(TECHNICAL_ID, getIliID(tid));
        if (!ch.softenvironment.util.StringUtils.isNullOrEmpty(bid)) {
            attrs.add(BASKET_ID, bid);
        }
        if (!ch.softenvironment.util.StringUtils.isNullOrEmpty(operation)) {
            attrs.add("OPERATION", operation);
        }

        return attrs;
    }

    /**
     * Return an IliID according to the following XSD type definition: <xsd:simpleType name="IliID"> <xsd:restriction base="xsd:string"> <xsd:pattern value="x[0-9a-zA-Z]*"/> </xsd:restriction>
     * </xsd:simpleType>
     */
    public String getIliID(Long object) {
        if (object == null) {
            throw new DeveloperException("tid-value expected!");
        } else {
            return InterlisSerializer.TECHNICAL_ID_PREFIX + object.toString();
        }
    }

    /**
     * Return attributes for a TRANSFER element.
     */
    public static AttributeList getIliNamespace(final String schemaLocationURL) {
        AttributeList attrs = new AttributeList();
        attrs.add("xmlns", "http://www.interlis.ch/INTERLIS2.2");
        attrs.add("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        attrs.add("xsi:schemaLocation", schemaLocationURL);
        return attrs;
    }

    /**
     * Generate a Reference to another Object. Keep the Relationship in mind.
     *
     * @param tid TID of Owner
     * @param extRef EXTREF
     * @param bid BID Basket-ID
     * @param nextTid NEXT_TID case ORDERED
     */
    public AttributeList getIliObjectRefAttributes(Long tid, Long extRef,
        Long bid, Long nextTid) {
        AttributeList attrs = new AttributeList();
        attrs.add(ATTRIBUTE_REFERENCE, getIliID(tid));
        if (extRef != null) {
            attrs.add("EXTREF", getIliID(extRef));
        }
        if (bid != null) {
            attrs.add(BASKET_ID, getIliID(bid));
        }
        if (nextTid != null) {
            attrs.add("NEXT_TID", getIliID(nextTid));
        }

        return attrs;
    }

    /**
     * Return attributes for a HEADERSECTION element.
     */
    public static AttributeList getIliVersion(final String sender) {
        AttributeList attrs = new AttributeList();
        attrs.add("VERSION", "2.2");
        attrs.add("SENDER", sender);
        return attrs;
    }
}

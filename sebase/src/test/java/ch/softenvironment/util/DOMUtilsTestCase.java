package ch.softenvironment.util;
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

import java.io.FileOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * TestCase for DOMUtils.
 *
 * @author Peter Hirzel <i>soft</i>Environment GmbH
 * @version $Revision: 1.2 $ $Date: 2006-05-07 14:53:51 $
 * @see ch.softenvironment.util.DOMUtils
 */
public class DOMUtilsTestCase extends TestCase {

    private static final String text = "<äöü{}[]ÄÖÜ çé @ '$\" &  \n\r\t>";
    private static final String path = System.getProperty("java.io.tmpdir");

    private DocumentBuilder docBuilder = null;

    @Override
    public void setUp() {
        try {
            docBuilder = DOMUtils.createDocumentBuilder(false);
        } catch (ParserConfigurationException e) {
            fail(e.getLocalizedMessage());
        }
    }

    @Override
    public void tearDown() {
    }

    /**
     * Example Test-Document:
     * <p>
     * <?xml version="1.0" encoding="UTF-8"?> <soap:Envelope xmlns:soap="http://www.w3.org/2001/12/soap-envelope" soap:encodingStyle="http://www.w3.org/2001/12/soap-encoding"> <soap:Header><TargetApp
     * soap:mustUnderstand="1"/></soap:Header> <soap:Body> <!--START Data area-->
     * <HOST>Sandflyer</HOST>
     * <DATA>[@see text]</DATA>
     * <soap:Fault> <soap:faultstring>Dummy Error</soap:faultstring> </soap:Fault> </soap:Body> </soap:Envelope>
     *
     * @throws TransformerException
     */
    private Document createDomDocument() throws TransformerException {
        Document doc = docBuilder.newDocument();

        Element root = doc.createElement("soap:Envelope");
        root.setAttributeNS("http://www.w3.org/2001/12/soap-envelope", "soap:encodingStyle", "http://www.w3.org/2001/12/soap-encoding");
        doc.appendChild(root);

        Element header = doc.createElement("soap:Header");
        root.appendChild(header);
        Element child = doc.createElement("TargetApp");
        header.appendChild(child);
        child.setAttribute("soap:mustUnderstand", "1");

        Element body = doc.createElement("soap:Body");
        root.appendChild(body);
        body.appendChild(doc.createComment("START Data area"));

        // create children
        child = doc.createElement("HOST");
        body.appendChild(child);
        child.appendChild(doc.createTextNode("Sandflyer")); //XmlUtils.encodeUTF8("Sandflyer")));

        child = doc.createElement("DATA");
        body.appendChild(child);
        child.appendChild(doc.createTextNode(text));

        Element fault = doc.createElement("soap:Fault");
        body.appendChild(fault);

        child = doc.createElement("soap:faultstring");
        fault.appendChild(child);
        child.appendChild(doc.createTextNode("Dummy Error")); //XmlUtils.encodeUTF8("Dummy Error")));

        return doc;
    }

    private static String getTagValue(Document doc, String tag) {
        NodeList nodes = doc.getElementsByTagName(tag);
        Node node = nodes.item(0);  // there is only one entry expected
        Text result = (Text) node.getFirstChild();
        return result.getNodeValue();
    }

    public void testReadWrite() {
        String filename = path + "XmlUtilsTestCase.xml";
        try {
            FileOutputStream out = new FileOutputStream(filename);
            DOMUtils.writeXML(createDomDocument(), out);
            out.flush();
            out.close();
        } catch (Throwable e) {
            fail("writeXML: " + e.getLocalizedMessage());
        }

        try {
            Document doc = DOMUtils.read(filename, docBuilder);
            assertTrue("Sandflyer".equals(getTagValue(doc, "HOST")));
            System.out.println("DATA=" + getTagValue(doc, "DATA"));
            assertTrue(text.equals(getTagValue(doc, "DATA")));
            assertTrue("Dummy Error".equals(getTagValue(doc, "soap:faultstring")));
        } catch (Throwable e) {
            fail("read: " + e.getLocalizedMessage());
        }
    }
}

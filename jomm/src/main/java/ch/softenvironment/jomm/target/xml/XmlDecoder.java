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

import ch.softenvironment.util.StringUtils;
import ch.softenvironment.util.Tracer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Decodes an XML-Stream to an Object using an XMLReader. Make sure there is a XMLReader-Implementation in your build.
 *
 * @author ce
 * @author Peter Hirzel, softEnvironment GmbH
 */
class XmlDecoder {

	private XMLReader parser = null;
	private InputSource inputSource = null;
	private InputStream inputStream = null;

	private XmlObjectServer server = null;
	private String xmlReaderImpl = null;

	/**
	 * Create the decoder.
	 *
	 * @param server
	 * @param xmlReaderImpl optional
	 */
	public XmlDecoder(XmlObjectServer server, final String xmlReaderImpl) {
		super();

		this.server = server;
		this.xmlReaderImpl = xmlReaderImpl;
	}

	/**
	 * Do the parsing.
	 *
	 * @param baskets List of IliBasket
	 * @param path
	 * @return Object (built from XML instance)
	 */
	public void decode(java.util.Collection baskets, final String path)
		throws Exception {
		try {
			if (StringUtils.isNullOrEmpty(xmlReaderImpl)) {
				parser = XMLReaderFactory.createXMLReader();
			} else {
				parser = XMLReaderFactory.createXMLReader(xmlReaderImpl);
			}
			// parser.setFeature("http://xml.org/sax/features/validation",true);
			Tracer.getInstance().logBackendCommand(
				"XMLReader instantiated: " + parser.getClass().getName());
		} catch (SAXException exSax) {
			throw new IOException("Unable to create the desired XML-Parser: "
				+ xmlReaderImpl);
		}

		XmlDecoderHandler myHandler = new XmlDecoderHandler(server, baskets);
		XmlDecoderErrorListener errorListener = new XmlDecoderErrorListener();
		// register to Content Handler
		parser.setContentHandler(myHandler);
		// register to Error Handler
		parser.setErrorHandler(errorListener);
		myHandler.setErrorHandler(errorListener);
		// parser.setDocumentHandler(new MyHandler());

		try {
			// first Pass
			myHandler.setSecondPass(false);
			inputStream = new FileInputStream(new File(path));
			inputSource = new InputSource(inputStream);
			parser.parse(inputSource);
			// object = myHandler.getModel();
			inputStream.close();

			// second Pass
			myHandler.setSecondPass(true);
			inputStream = new FileInputStream(new File(path));
			inputSource = new InputSource(inputStream);
			parser.parse(inputSource);
			inputStream.close();

			// check failures
			/*
			 * Map unref = myHandler.getUnreferencedObjects();
			 * java.util.Iterator it = unref.keySet().iterator(); while
			 * (it.hasNext()) { String tid = (String)it.next(); // Long Object
			 * unrefObject = unref.get(tid); if (!((object == null) ||
			 * (unrefObject.equals(object)) || (unrefObject instanceof DbCode)
			 * || (unrefObject instanceof DbNlsString))) {
			 * Tracer.getInstance().runtimeWarning(this, "decode()",
			 * "Object with <TID="+tid+"> is never used"); // log(-1, "Object <"
			 * + tid + "> is never used", null); } }
			 */
			// Tracer.getInstance().debug("Root-Object: " + object + ".TID=" +
			// ((DbObject)object).getId());
		} catch (SAXParseException exSax) {
			Exception ex = exSax;
			if (exSax.getException() != null) {
				ex = exSax.getException();
			}
			Tracer.getInstance().runtimeError(
				"Err@line: " + exSax.getLineNumber() + " System ID: "
					+ exSax.getSystemId() + " Msg: "
					+ ex.getLocalizedMessage());
			throw new IOException(ex.getMessage());
		} catch (SAXException exSax) {
			Exception ex = exSax;
			if (exSax.getException() != null) {
				ex = exSax.getException();
				Tracer.getInstance().runtimeError(
					"SAXException: " + ex.getLocalizedMessage());
			}
			if (ex instanceof java.lang.reflect.InvocationTargetException) {
				ex = ex;
				Tracer.getInstance().runtimeError(
					"InvocationTargetException: "
						+ ex.getLocalizedMessage());
			}

			// ex.printStackTrace();
			throw ex;
		}
		// return object;
	}
}

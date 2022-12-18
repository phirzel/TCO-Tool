package ch.softenvironment.jomm.serialize;

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

import ch.softenvironment.util.NlsUtils;
import ch.softenvironment.util.StringUtils;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * Generic Utility to write an XML-Stream.
 *
 * @author Peter Hirzel
 */
@Slf4j
public class XmlSerializer extends SimpleSerializer {

	public static final String XSD_DATE_FORMAT = "yyyy-MM-dd";

	// Unicode special Characters
	public static final char COPYRIGHT = (char) 0xA9; // (c)
	public static final char REGISTERED = (char) 0xAE; // (R)
	public static final char PERMILLE = (char) 0x2030; // 0/00
	public static final char EURO = (char) 0x20AC; // Euro currency
	public static final char INFINITY = (char) 0x221E; // 8 "* 90°"
	public static final char TRADEMARK = (char) 0x2122; // TM
	public static final char SIGMA = (char) 0x2211; // Greek Sigma (statistical
	// Sum-sign)
	public static final char ELIPSIS = (char) 0x5B5; // ..

	// XML syntax
	protected static final String APOSTROPH = "\"";
	protected static final String START_TAG = "<";
	protected static final String END_TAG = ">";
	protected static final String EQUAL = "=";
	protected static final String SLASH = "/";

	private final java.util.Stack<String> stack = new java.util.Stack<String>();

	// monitor schema violations
	private final StringBuffer violations = new StringBuffer();

	// private org.apache.xml.serialize.XMLSerializer serializer = null;

	/**
	 * Encoder constructor comment.
	 *
	 * @param out
	 */
	public XmlSerializer(java.io.Writer out) {
		super(out);

		// serializer = new org.apache.xml.serialize.XMLSerializer(out, null);
	}

	/**
	 * Write comment (non-Model content) into XML-Stream. For e.g. <!-- My Comment -->
	 *
	 * @param comment
	 * @throws java.io.IOException
	 */
	public final void comment(String comment) throws java.io.IOException {
		indent();
		out.write(START_TAG + "!-- " + comment + " --" + END_TAG);
		newline();
		// serializer.comment(comment);
	}

	/**
	 * Write an Element to XML-Stream. For e.g. <MyValue/>
	 *
	 * @param tagName
	 * @throws java.io.IOException
	 */
	public final void element(String tagName) throws java.io.IOException {
		element(tagName, null);
	}

	/**
	 * Write an Element to XML-Stream. For e.g. <MyValue>Hello World</MyValue>
	 *
	 * @param tagName
	 * @param value
	 * @throws java.io.IOException
	 */
	public final void element(String tagName, Object value)
		throws java.io.IOException {
		element(tagName, value, null);
	}

	/**
	 * Write an Element to XML-Stream. For e.g. <MyValue attr1="value1" attr2="value2">Hello World</MyValue>
	 *
	 * @param tagName
	 * @param value
	 * @throws java.io.IOException
	 */
	public final void element(String tagName, Object value,
		AttributeList attributes) throws java.io.IOException {
		element(tagName, value, attributes, true);
	}

	/**
	 * Write an Element to XML-Stream. For e.g. <MyValue attr1="value1" attr2="value2">Hello World</MyValue>
	 *
	 * @param tagName
	 * @param value
	 * @throws java.io.IOException
	 */
	protected void element(String tagName, Object value,
		AttributeList attributes, boolean inlined)
		throws java.io.IOException {
		if (inlined) {
			indent();
		}

		out.write(START_TAG + tagName);
		writeAttributes(attributes);
		if (value == null) {
			out.write(SLASH + END_TAG);
		} else {
			out.write(END_TAG);
			simpleContent(value);
			out.write(START_TAG + SLASH + tagName + END_TAG);
		}

		if (inlined) {
			newline();
		}
	}

	/**
	 * Validate a String tag and print it into Stream. Further on log any schema violations.
	 */
	protected void elementValidatedBoolean(String reference, String element,
		Boolean value, long minOccurs, long maxOccurs) throws Exception {
		// validate value
		if (value == null) {
			if (minOccurs > 0) {
				logViolation(reference, element, "Obligatorisch");
				element(element); // print empty tag
			} // else suppress empty tag
		} else {
			element(element, encodeBoolean(value));
		}
	}

	protected void elementValidatedNumber(String reference, String element,
		Number value, long minOccurs, long maxOccurs, Number minRange,
		Number maxRange) throws Exception {
		// validate value
		if (value == null) {
			if (minOccurs > 0) {
				logViolation(reference, element, "Obligatorisch");
				element(element);
			} // else suppress value
		} else {
			if (value.doubleValue() < minRange.doubleValue()) {
				logViolation(reference, element, "zu klein");
			}
			if (value.doubleValue() > maxRange.doubleValue()) {
				logViolation(reference, element, "zu gross");
			}
			element(element, value.toString());
		}
	}

	/**
	 * Validate a String tag and print it into Stream. Further on log any schema violations.
	 */
	protected void elementValidatedString(String reference, String element,
		String value, long minOccurs, long maxOccurs, long maxLength)
		throws Exception {
		// validate value
		if (ch.softenvironment.util.StringUtils.isNullOrEmpty(value)) {
			if (minOccurs > 0) {
				// TODO NLS
				logViolation(reference, element, "mandatory");
				element(element); // print empty tag
			} // else suppress empty tag
		} else {
			if (value.length() > maxLength) {
				// TODO NLS
				logViolation(reference, element, "too long ("
					+ (value.length() - maxLength) + ")");
			}
			element(element, value);
		}
	}

	/**
	 * Map Boolean to XML-content.
	 *
	 * @param value
	 * @return
	 * @see <a href="http ://www.w3.org/TR/2004/REC-xmlschema-2-20041028/datatypes.html#built -in-primitive-datatypes">primitive db types</a>
	 */
	@Override
	public String encodeBoolean(Boolean value) {
		if (value == null) {
			return "";
		} else if (value.booleanValue()) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * Map Date to XML-content.
	 *
	 * @param value
	 * @return
	 * @see <a href="http ://www.w3.org/TR/2004/REC-xmlschema-2-20041028/datatypes.html#built -in-primitive-datatypes>db primitives</a>
	 */
	@Override
	public String encodeDate(java.util.Date value) {
		if (value == null) {
			return "";
		} else {
			java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat(
				XSD_DATE_FORMAT);
			return sf.format(value);
		}
	}

	/**
	 * Encode given String to XML-text (value of a Text-Node). <b>Special Characters</b> will be prepared according to UTF-8.
	 *
	 * @param value
	 * @return String (transformed for XML and UTF-8)
	 * @see <a href="http ://www.w3.org/TR/2004/REC-xmlschema-2-20041028/datatypes.html#built -in-primitive-datatypes">db primitives</a>
	 */
	@Override
	public String encodeString(String value) {
		/*
		 * try { // this will not mask special XML characters and
		 * non-transformable characters throw an Exception byte[] str =
		 * s.getBytes(Serializer.UTF8); return new String(str); }
		 * catch(UnsupportedEncodingException ex) {
		 * Tracer.getInstance().runtimeError(XmlEncoder.class, "encodeString()",
		 * "encoding to UTF-8 failed: " + ex.getLocalizedMessage()); return s; }
		 */
		StringBuffer str = new StringBuffer();
		int len = (value != null) ? value.length() : 0;
		for (int i = 0; i < len; i++) {
			// mask special-characters within XML-instance
			char ch = value.charAt(i);
			if (ch == '<') {
				// strictly illegal in XML
				str.append("&lt;");
			} else if (ch == '>') {
				str.append("&gt;");
			} else if (ch == '&') {
				// strictly illegal in XML
				str.append("&amp;");
			} else if (ch == '\'') {
				str.append("&apos;");
			} else if (ch == '"') {
				str.append("&quot;");
			} else if ((ch >= 0x80) || (ch == '\r') || (ch == '\n')) {
				// ch.softenvironment.util.Tracer.getInstance().patch(XmlSerializer.class,
				// "encodeString()",
				// "use NLS-Transformer for all special ASCII-chars & CHARACTER-SET");
				str.append("&#");
				str.append(Integer.toString(ch));
				str.append(';');
			} else {
				str.append(ch);
			}
		}

		return str.toString();
	}

	/**
	 * End an started XML-Element in XML-Stream. For e.g. "</tagName>"
	 *
	 * @throws throws java.io.IOException
	 * @see #startElement(String)
	 * @see #endElement(String)
	 */
	public final void endElement() throws java.io.IOException {
		endElement(null);
	}

	/**
	 * This is a quality-assurance method to check whether element on Stack matches developer expectation.
	 *
	 * @param expectedElement The expected closing element (for validating reasons only)
	 * @throws java.io.IOException
	 * @throws ch.softenvironment.util.DeveloperException (if expectedElement does not match interal suggestion)
	 * @see #startElement(String)
	 * @see #endElement()
	 */
	public final void endElement(String expectedElement)
		throws java.io.IOException,
		ch.softenvironment.util.DeveloperException {
		/*
		 * if (inline > 0) { inline--; indent(); }
		 */
		String closingElement = stack.pop();
		if ((expectedElement == null) || closingElement.equals(expectedElement)) {
			indent();
			out.write(START_TAG + SLASH + closingElement + END_TAG);
			newline();
		} else {
			throw new ch.softenvironment.util.DeveloperException(
				"XmlSerializer suggests </" + closingElement
					+ "> as closing Element");
		}
	}

	/**
	 * Return Encoding-Violations after building the XML-Instance
	 */
	public String getViolations() {
		// TODO NLS
		if (violations.length() > 0) {
			return ("Generation violations:\n"
				+ "================================================\n" + violations
				.toString());
		} else {
			return "XML generated (no faults recognized)!";
		}
	}

	protected void logViolation(String reference, String element, String message) {
		violations.append(reference + " in <" + element + ">: " + message
			+ "\n");
	}

	/**
	 * Write simple content to XML-Stream. String's are encoded according to their type and W3c xsd-Standard.
	 *
	 * @param value (plain text within element-tags
	 * @throws java.io.IOException
	 */
	protected void simpleContent(Object value) throws java.io.IOException {
		if (value instanceof String) {
			nativeContent(encodeString((String) value));
		} else if (value instanceof Boolean) {
			nativeContent(encodeBoolean((Boolean) value));
		} else if (value instanceof Number) {
			out.write(encodeNumber((Number) value));
		} else if (value instanceof java.util.Date) {
			nativeContent(encodeDate((java.util.Date) value));
		} else {
			nativeContent(value.toString());
		}
	}

	/**
	 * @see #startElement(String, AttributeList)
	 */
	public final void startElement(String tagName) throws java.io.IOException {
		startElement(tagName, null);

		// serializer.startElement(tagName);
	}

	/**
	 * Write a beginning Element to XML-Stream. For e.g. "<tagName>"
	 *
	 * @param tagName
	 * @param attributes (list of attributes for given XML-element)
	 * @throws throws java.io.IOException
	 * @see #endElement()
	 */
	public final void startElement(String tagName, AttributeList attributes)
		throws java.io.IOException {
		indent();

		stack.push(tagName);

		out.write(START_TAG + tagName);
		writeAttributes(attributes);
		out.write(END_TAG);

		newline();

		// inline++;
		// serializer.startElement(tagName, AttrList);
	}

	/**
	 * Set a default XSLT (*.xsl) transformation to represent an XML-Instance. For e.g. <?xml-stylesheet type="text/xsl" href="MyTransformation.xsl"?>
	 *
	 * @param xsl
	 * @throws java.io.IOException
	 */
	public final void stylesheet(String xsl) throws java.io.IOException {
		nativeContent(START_TAG + "?xml-stylesheet type=\"text/xsl\" href=\""
			+ xsl + "\"?" + END_TAG);
		newline();
	}

	/**
	 * Set the version of the XML-Standard in XML-Instance. For e.g. <?xml version="1.0" encoding="UTF-8"?>
	 *
	 * @param number
	 * @throws java.io.IOException
	 */
	public final void version(String number, String charSet)
		throws java.io.IOException {
		nativeContent(START_TAG + "?xml version=\"" + number + "\" encoding=\""
			+ (charSet == null ? StringUtils.CHARSET_UTF8 : charSet)
			+ "\"?" + END_TAG);
		newline();
	}

	/**
	 * Write an Element with a single Attribute to XML-Stream. For e.g.
	 * <Translation language="de">Morgen</Entry>
	 *
	 * @param attributes
	 * @throws java.io.IOException
	 */
	protected void writeAttributes(AttributeList attributes)
		throws java.io.IOException {
		if (attributes != null) {
			java.util.Iterator<List<String>> iterator = attributes
				.iteratorAttributes();
			while (iterator.hasNext()) {
				java.util.List<String> attribute = iterator.next();
				nativeContent(" " + attribute.get(0) + EQUAL + APOSTROPH
					+ attribute.get(1) + APOSTROPH);
			}
		}
	}

	/**
	 * Indent cursor at current Position n times.
	 *
	 * @throws throws java.io.IOException
	 */
	protected final void indent() throws java.io.IOException {
		super.indent(stack.size());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ch.softenvironment.w3.Serializer#encodeTime(java.util.Date)
	 */
	@Override
	public String encodeTime(Date value) {
		// TODO verify correctness (UTC resp. GMT)
		return NlsUtils.formatTime24Hours(value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ch.softenvironment.w3.Serializer#encodeTimestamp(java.util.Date)
	 */
	@Override
	public String encodeDateTime(Date value) {
		// TODO verify correctness (UTC resp. GMT)
		return NlsUtils.formatDateTime(value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ch.softenvironment.w3.Serializer#encodeNumber(java.lang.Number)
	 */
	@Override
	public String encodeNumber(Number value) {
		if (value == null) {
			return "";
		} else {
			return value.toString();
		}
	}

	/**
	 * Save the contents of the serializer into an ASCII-file.
	 *
	 * @param filename
	 */
	public void saveContentsToFile(final String filename) throws IOException {
		FileOutputStream out = null;
		PrintStream stream = null;
		IOException exception = null;
		try {
			out = new FileOutputStream(filename);
			stream = new PrintStream(out);
			stream.print(getWriter().toString());

			stream.flush();
			stream.close();
			stream = null;
			out.close();
			out = null;
		} catch (IOException e) {
			exception = e;
			log.error("Writing context of file: {}", filename, e);
		} finally {
			if (stream != null) {
				stream.close();
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				log.error("closing file: {}", filename, e);
			}
		}
		if (exception != null) {
			throw exception;
		}
	}
}

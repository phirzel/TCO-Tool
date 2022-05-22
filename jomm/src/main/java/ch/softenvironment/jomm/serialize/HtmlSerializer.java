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

import ch.softenvironment.util.StringUtils;
import ch.softenvironment.util.Tracer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * HTML-Generator.
 *
 * @author Peter Hirzel, softEnvironment GmbHs
 */
public class HtmlSerializer extends XmlSerializer {

	// HTML masked characters
	public final static String SPACE = "&nbsp;";
	public final static String AMPERS_AND = "&amp;"; // "&"
	public final static String LESS_THAN = "&lt;"; // "<"
	public final static String GREATER_THAN = "&gt;"; // "<"
	public final static String QUOT = "&quot;"; // "\""
	// HTML elements
	public final static String TAG_PARAGRAPH = "p";
	public final static String TAG_HTML = "html";
	public final static String TAG_BODY = "body";
	public final static String TAG_TABLE = "table";
	public final static String TAG_TABLE_ROW = "tr";
	public final static String TAG_UNORDERED_LIST = "ul";
	public final static String ATTRIBUTE_ALIGN = "align";
	public final static String ATTRIBUTE_BORDER = "border";
	public final static String ATTRIBUTE_WIDTH = "width";
	public final static String ATTRIBUTE_HEIGHT = "height";
	public final static String ATTRIBUTE_BACKGROUND_COLOR = "bgcolor";

	private final boolean isXHTML = false;

	/**
	 * Validate a an existing Web-Document by W3C-Validator: <quote>The W3C Markup Validation Service is a free service that checks Web documents in formats like HTML and XHTML for conformance to W3C
	 * Recommendations and other standards.</quote>
	 *
	 * @param uri (for e.g. "http://www.softenvironment.ch")
	 * @return String (well formated HTML)
	 * @see http://validator.w3.org/
	 */
	public static String validate(final String uri) throws IOException {
		StringBuffer buffer = new StringBuffer();
		BufferedReader in = null;
		try {
			URL url = new URL("http://validator.w3.org/check?uri=" + uri);
			URLConnection conn = url.openConnection();

			in = new BufferedReader(
				new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null) {
				buffer.append(line);
			}
		} catch (MalformedURLException mue) {
			Tracer.getInstance().runtimeWarning(
				"Invalid URL: " + mue.getLocalizedMessage());
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return buffer.toString();
	}

	/**
	 * HtmlSerializer constructor comment.
	 *
	 * @param out java.io.Writer
	 */
	public HtmlSerializer(java.io.Writer out) {
		super(out);
	}

	/**
	 * Write a text as BOLD.
	 *
	 * @param text
	 * @see #encodeString()
	 */
	public void bold(final String text) throws java.io.IOException {
		element("b", encodeString(text), null, false);
	}

	/**
	 * Write a br-tag.
	 */
	public void breakLine() throws java.io.IOException {
		element("br");
	}

	@Override
	protected void element(final String tagName, Object value,
		AttributeList attributes, boolean inlined)
		throws java.io.IOException {
		if (isXHTML) {
			super.element(tagName, value, attributes, inlined);
		} else {
			if (inlined) {
				indent();
			}

			nativeContent(START_TAG + tagName);
			writeAttributes(attributes);
			if (value == null) {
				nativeContent(/* SLASH + */END_TAG);
			} else {
				nativeContent(END_TAG);
				nativeContent(value.toString()); // allow HTML formatted tags
				// here
				nativeContent(START_TAG + SLASH + tagName + END_TAG);
			}

			if (inlined) {
				newline();
			}
		}
	}

	/**
	 * Special characters will be encoded according to HTML rules.
	 */
	@Override
	public String encodeString(final String value) {
		StringBuffer str = new StringBuffer();
		int len = (value != null) ? value.length() : 0;
		for (int i = 0; i < len; i++) {
			char ch = value.charAt(i);
			if (ch == '<') {
				// strictly illegal in XML
				str.append("&lt;");
			} else if (ch == '>') {
				str.append("&gt;");
			} else if (ch == '&') {
				// strictly illegal in XML
				str.append("&amp;");
				/*
				 * NOT in HTML (necessary in XML) } else if (ch == '\'') {
				 * str.append("&apos;");
				 */
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
	 * Write an ending body-Tag.
	 */
	public void endBody() throws java.io.IOException {
		endElement(TAG_BODY);
	}

	/**
	 * Write an ending html-Tag.
	 */
	public void endHtml() throws java.io.IOException {
		endElement(TAG_HTML);
	}

	/**
	 * Write an ending p-Tag.
	 */
	public void endParagraph() throws java.io.IOException {
		endElement(TAG_PARAGRAPH);
	}

	/**
	 * Write an ending ul-Tag.
	 */
	public void endUnorderedList() throws java.io.IOException {
		endElement(TAG_UNORDERED_LIST);
	}

	/**
	 * Write an ending table-Tag.
	 *
	 * @throws java.io.IOException
	 */
	public void endTable() throws java.io.IOException {
		endElement(TAG_TABLE);
	}

	/**
	 * Write an ending tr-Tag.
	 */
	public void endTableRow() throws java.io.IOException {
		endElement(TAG_TABLE_ROW);
	}

	/**
	 * Write a text in a Header (h1-h6).
	 *
	 * @param level h1..hn
	 * @param text (special Characters will be transformed)
	 */
	public void header(final int level, final String text)
		throws java.io.IOException {
		if (level <= 6) {
			element("h" + level, encodeString(text));
		} else {
			// <h7> does not exist
			nativeContent("<p><b><i>" + encodeString(text) + "</i></b></p>");
		}
	}

	/**
	 * Write a hr-tag.
	 * <p>
	 * In HTML5, this tag defines a thematic break.
	 * <p>
	 * In HTML 4.01, the tag represents a horizontal rule.
	 * <p>
	 * However, the tag may still be displayed as a horizontal rule in visual browsers, but is now defined in semantic terms, rather than presentational terms.
	 * <p>
	 * All layout attributes are deprecated in HTML 4.01, and are not supported in HTML5. Use CSS to style the
	 * <hr>
	 * element instead.
	 */
	public void horizontalRule() throws java.io.IOException {
		element("hr");
	}

	/**
	 * Add an img-tag.
	 *
	 * @param url of ImageIcon
	 * @param alternativeDescription
	 * @param border
	 * @throws IOException
	 */
	public void image(URL url, String alternativeDescription, int border)
		throws IOException {
		/*
		 * String className = aClass.getName(); int index =
		 * className.lastIndexOf('.'); String file = className.substring(0,
		 * index).replace('.', '/') + "/" + "resources" + "/" + fileName; //
		 * from IDE with relative FileSystem or compiled within Jar java.net.URL
		 * url = aClass.getName("/" + file);
		 */
		nativeContent("<img src='" + url + "' border='" + border + "' alt='"
			+ alternativeDescription + "'>");
	}

	/**
	 * Write a text as ITALIC.
	 *
	 * @param text
	 * @see #encodeString()
	 */
	public void italic(String text) throws java.io.IOException {
		element("i", encodeString(text), null, false);
	}

	/**
	 * Write a listItem li-tag in an unordered List.
	 *
	 * @param text
	 * @see #encodeString()
	 */
	public void listItem(String text) throws java.io.IOException {
		element("li", encodeString(text), null, false);
	}

	/**
	 * Write text within a paragraph.
	 *
	 * @param text
	 * @see #encodeString()
	 */
	public void paragraph(String text) throws java.io.IOException {
		if (StringUtils.isNullOrEmpty(text)) {
			startParagraph();
			// simpleContent("");
			endParagraph();
		} else {
			startParagraph();
			simpleContent(text);
			endParagraph();
		}
	}

	/**
	 * Write a HTML reference to a Download file.
	 */
	public void referenceDownloadLink(String path, String fileName)
		throws java.io.IOException {
		nativeContent("<a href='" + path + fileName + "'>" + fileName + "</a>");
	}

	/**
	 * @see #referenceNewWindowLink(String, String)
	 */
	public void referenceNewWindowLink(String url) throws java.io.IOException {
		referenceNewWindowLink(url, url);
	}

	/**
	 * Write a JavaScript embedded in HTML Tag to open Link by new Browser Instance.
	 *
	 * @param url pointing to website
	 * @param text speaking name of referenced url
	 */
	public void referenceNewWindowLink(String url, String text)
		throws java.io.IOException {
		nativeContent("<a href='#' onClick='javascript:window.open(\"" + url
			+ "\")'>" + text + "</a>");
	}

	/**
	 * Write a body-Tag.
	 */
	public void startBody(AttributeList attrs) throws java.io.IOException {
		startElement(TAG_BODY, attrs);
	}

	/**
	 * Write a head-Tag.
	 */
	public void startHead(String title, String language, String charSet)
		throws java.io.IOException {
		startElement("head");
		if (title != null) {
			element("title", title);
		}

		AttributeList attrs = null;
		if (language != null) {
			attrs = new AttributeList();
			attrs.add("http-equiv", "content-language");
			attrs.add("content", language);
			element("meta", null, attrs);
		}

		attrs = new AttributeList();
		attrs.add("http-equiv", "content-type");
		attrs.add("content", charSet == null ? "text/html;charset=iso-8859-1"
			: charSet);
		element("meta", null, attrs);

		attrs = new AttributeList();
		attrs.add("name", "author");
		attrs.add("content", "generated by www.softenvironment.ch W3-Tools");
		element("meta", null, attrs);

		attrs = new AttributeList();
		attrs.add("name", "date");
		attrs.add("content", ch.softenvironment.util.NlsUtils
			.formatDateTime(new java.util.Date()));
		element("meta", null, attrs);
	}

	/**
	 * Write a html-Tag.
	 */
	public void startHtml() throws java.io.IOException {
		startElement(TAG_HTML);
	}

	/**
	 * Write a p-Tag.
	 */
	public void startParagraph() throws java.io.IOException {
		startElement(TAG_PARAGRAPH);
	}

	/**
	 * Write a table-Tag.
	 */
	public void startTable(int borderWidth) throws java.io.IOException {
		startTable(new AttributeList(ATTRIBUTE_BORDER, "" + borderWidth));
	}

	public void startTable(AttributeList attrs) throws java.io.IOException {
		startElement(TAG_TABLE, attrs);
	}

	/**
	 * Write a tr-Tag.
	 */
	public void startTableRow() throws java.io.IOException {
		startElement(TAG_TABLE_ROW);
	}

	/**
	 * Write a ul-Tag.
	 */
	public void startUnorderedList() throws java.io.IOException {
		startElement(TAG_UNORDERED_LIST);
	}

	/**
	 * For e.g. to strip-off tags within head-Tags.
	 */
	public static String suppressTag(String htmlContents, String tagName) {
		int startHead = htmlContents.indexOf(START_TAG + tagName + END_TAG);
		int endHead = htmlContents.indexOf(START_TAG + SLASH + tagName
			+ END_TAG, tagName.length());

		if ((startHead >= 0) && (endHead >= 0)) {
			return htmlContents.substring(0, startHead)
				+ htmlContents
				.substring(endHead + tagName.length() + 3 /* </> */);
		} else {
			return htmlContents;
		}
	}

	/**
	 * @see #tableData(String, AttributeList)
	 */
	public void tableData(String text) throws java.io.IOException {
		tableData(text, null);
	}

	/**
	 * Write a tableData-Element.
	 *
	 * @param text
	 * @see #encodeString()
	 */
	public void tableData(String text, AttributeList attrs)
		throws java.io.IOException {
		element("td", encodeString(text), attrs);
	}

	/**
	 * @see #tableHeader(String, AttributeList)
	 */
	public void tableHeader(String text) throws java.io.IOException {
		tableHeader(text, null);
	}

	/**
	 * Write a tableHeader-Element.
	 *
	 * @param text
	 * @see #encodeString()
	 */
	public void tableHeader(String text, AttributeList attrs)
		throws java.io.IOException {
		element("th", encodeString(text), attrs);
	}
}

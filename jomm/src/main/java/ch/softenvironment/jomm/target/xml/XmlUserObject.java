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

/**
 * User definable Object to be passed to XmlObjectServer.
 *
 * @author Peter Hirzel
 * @since 1.3 (2005-10-17)
 */
public class XmlUserObject {

    public static final int MODE_XML = 1;
    public static final int MODE_DUMP = 2;  // serialized

    private int mode = MODE_XML;
    private transient String filename = null;
    private transient String sender = "Persistency-Framework (www.softenvironment.ch)";
    private transient String xsd = null;
    private transient String xsl = null;

    /**
     * XmlUserObject constructor comment.
     */
    public XmlUserObject(final String filename) {
        super();
        setFilename(filename);
    }

    /**
     * @see #setFilename()
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @see #setMode()
     */
    public int getMode() {
        return mode;
    }

    /**
     * @see #setSender()
     */
    public String getSender() {
        return sender;
    }

    /**
     * @see #setXsd()
     */
    public String getXsd() {
        return xsd;
    }

    /**
     * @see #setXsl()
     */
    public String getXsl() {
        return xsl;
    }

    /**
     * Set the destination file to read/write Model.
     */
    public void setFilename(final String filename) {
        this.filename = filename;
    }

    /**
     * Either mode: - MODE_XML - MODE_DUMP (Serialized Object dumping)
     */
    public void setMode(final int mode) {
        this.mode = mode;
    }

    /**
     * Set the Sender/Creator of XML-Instance.
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Set the Schema (*.xsd) to validate an XML-Instance against. (A Schema for Elements in the Namespace http://www.interlis.ch/INTERLIS2.2 might be assumed in the same location as the XML-File to
     * be validated.)
     */
    public void setXsd(String xsd) {
        this.xsd = xsd;
    }

    /**
     * Set the default XSLT (*.xsl) to transformate an XML-Instance.
     */
    public void setXsl(String xsl) {
        this.xsl = xsl;
    }
}

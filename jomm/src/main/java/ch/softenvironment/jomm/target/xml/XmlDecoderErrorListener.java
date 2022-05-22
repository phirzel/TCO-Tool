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

import ch.softenvironment.util.Tracer;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * ErrorHandler for XmlDecoder.
 *
 * @author ce
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.2 $ $Date: 2006-07-05 16:32:25 $
 */
class XmlDecoderErrorListener implements org.xml.sax.ErrorHandler {

    @Override
    public void error(SAXParseException err) throws SAXException {
        Tracer.getInstance().runtimeWarning("Parsing Error - Line: " + err.getLineNumber() + ", URI: " + err.getSystemId() + ", Message: " + err.getMessage());
        //	throw err;
    }

    @Override
    public void fatalError(SAXParseException fatalErr) throws SAXException {
        Tracer.getInstance().runtimeWarning("Parsing FatalError - Line: " + fatalErr.getLineNumber() + ", URI: " + fatalErr.getSystemId() + ", Message: " + fatalErr.getMessage());
        throw fatalErr;
    }

    @Override
    public void warning(SAXParseException warning) /*throws SAXException*/ {
        Tracer.getInstance().runtimeWarning("Parsing Warning - Line: " + warning.getLineNumber() + ", URI: " + warning.getSystemId() + ", Message: " + warning.getMessage());
    }
}

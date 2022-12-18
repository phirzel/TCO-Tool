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

import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * ErrorHandler for XmlDecoder.
 *
 * @author ce
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.2 $ $Date: 2006-07-05 16:32:25 $
 */
@Slf4j
class XmlDecoderErrorListener implements org.xml.sax.ErrorHandler {

    @Override
    public void error(SAXParseException ex) throws SAXException {
        log.warn("Parsing Error", ex);
        //	throw err;
    }

    @Override
    public void fatalError(SAXParseException ex) throws SAXException {
        log.warn("Parsing Error", ex);
        throw ex;
    }

    @Override
    public void warning(SAXParseException ex) /*throws SAXException*/ {
        log.warn("Parsing Error", ex);
    }
}

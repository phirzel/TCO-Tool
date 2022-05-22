package ch.softenvironment.jomm.mvc.model;

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

import ch.softenvironment.jomm.datatypes.DbNlsString;

/**
 * Structure for reusable Codes. They must have at least an NLS "name" property.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.2 $ $Date: 2005-12-28 11:45:34 $
 */
public interface DbCodeType {

    String PROPERTY_ILICODE = "iliCode";

    /**
     * @return NLS-Property
     */
    DbNlsString getName();

    /**
     * @return NLS-Property in default Locale
     */
    String getNameString();

    /**
     * @param locale
     * @return NLS-Property in given Locale
     */
    String getNameString(java.util.Locale locale);

    /**
     * Return the UNIQUE ILI-Code constant for this code-type independent of its naming.
     */
    String getIliCode();
}

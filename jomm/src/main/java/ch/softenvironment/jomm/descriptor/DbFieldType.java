package ch.softenvironment.jomm.descriptor;
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
 * Implement this Interface for any Class-Type representing a specific DBMS-Type. For e.g. - ch.softenvironment.jomm.interlis.IliSurface corresponds to ORACLE's MDSYS.SDO_GEOMETRY
 *
 * @author Peter Hirzel
 * @since 1.1.1.1 (2005-04-25)
 * @see DbFieldTypeDescriptor(Class baseType)
 */
public interface DbFieldType {

    /**
     * Represent the value of this type by a User Interface (for e.g. in a JTextField).
     *
     * @return String Formatted User-Readable value-String
     */
    String toDisplayString();
}

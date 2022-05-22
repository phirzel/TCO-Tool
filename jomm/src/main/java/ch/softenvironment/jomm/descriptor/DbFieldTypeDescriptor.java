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
 * General Target-System Attribute-Type Descriptor.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class DbFieldTypeDescriptor {

    /*
     * class DbGenericTypeDescriptor extends DbFieldTypeDescriptor { protected
     * DbGenericTypeDescriptor() { super(); } }
     */
    public static final DbFieldTypeDescriptor genericType = new DbFieldTypeDescriptor();

    protected transient Class<?> baseType = null;

    protected DbFieldTypeDescriptor() {
        super();
    }

    /**
     * @param baseType
     * @see DbFieldType baseType should implement DbFieldType if non-standard Types
     */
    public DbFieldTypeDescriptor(Class<?> baseType) {
        super();

        this.baseType = baseType;
    }

    public Class<?> getBaseType() {
        return baseType;
    }
}

package ch.softenvironment.jomm;
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
 * JDO-Instances are managed by a certain JDO-Implementation. A JDO-Instance must therefore be found by given mechanisms. JDO defines 3 types of Identities: - application - datastore - nondurable The
 * definition are kept in an XML-Configuration file "outside" of the Java-source-code. An entry might look like follows: <class name="MyDataObject" identity-type="application"
 * objectid-class=DbObjectId
 * <field name="id" primary-key="true"/>
 * </class>
 * <p>
 * A JDO-ObjectId must further be transmittable and therefore Serializable and cloneable for certain methods by PersistenceManager.
 *
 * @author Peter Hirzel
 * @since 1.1.1.1 (2005-04-25)
 */
public interface DbIdentity extends java.io.Serializable, Cloneable {

    // identity-type
    String APPLICATION = "application";
    String DATASTORE = "datastore";
    String NONDURABLE = "nondurable";

    /**
     * Return the type of identity.
     */
    java.lang.String getIdentityType();

    /**
     * Return the Class-name to build an Instance.
     */
    java.lang.String getName();
}

package ch.softenvironment.jomm.descriptor;

import ch.softenvironment.jomm.mvc.model.DbCode;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.jomm.mvc.model.DbRelationshipBean;

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
 * Content of one Descriptor mapping entry.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class DbDescriptorEntry implements Cloneable {

    private transient boolean ordered = false;
    private transient boolean cached = false;
    private transient boolean local = true;
    private transient int associationType = DbDescriptor.IRRELEVANT;
    private transient String attributeName = null;
    private transient DbMultiplicityRange range = null;
    private transient java.lang.Class<? extends DbObject> baseClass = null;
    private transient DbFieldTypeDescriptor fieldType = null;
    private transient String mapName = null;
    // for cloned Entries
    private transient java.lang.Class<? extends DbObject> dbObjectOriginal = null;
    private transient String propertyOwnerId = null;

    /**
     * DbDescriptorEntry constructor.
     *
     * @see DbDescriptor#addNlsString(..)
     */
    protected DbDescriptorEntry(final String attributeName, DbMultiplicityRange range) {
        super();

        this.attributeName = attributeName;
        this.range = range;
    }

    /**
     * DbDescriptorEntry constructor for n:n relationship.
     *
     * @see DbDescriptor#addAssociationAttributed(..)
     */
    protected DbDescriptorEntry(final String referenceProperty, DbMultiplicityRange rangeA, final int associationType, Class<? extends DbRelationshipBean> dbRelationshipBean,
        DbMultiplicityRange rangeB) {
        super();

        // ForeignKey is in n:n Map
        this.local = false;

        this.attributeName = referenceProperty;
        this.range = rangeA;
        this.baseClass = dbRelationshipBean;
        this.associationType = associationType;
        // TODO NYI: rangeB not supported
    }

    /**
     * DbDescriptorEntry constructor.
     *
     * @see DbDescriptor#addOneToMany(..)
     */
    protected DbDescriptorEntry(final String attributeRoleName, DbMultiplicityRange range, final int associationType, Class<? extends DbObject> aggregationBaseClass, boolean ordered) {
        super();

        // ForeignKey is on other side of association
        this.local = false;

        this.attributeName = attributeRoleName;
        this.range = range;
        this.baseClass = aggregationBaseClass;
        this.associationType = associationType;
        this.ordered = ordered;
    }

    /**
     * DbDescriptorEntry constructor.
     *
     * @see DbDescriptor#addOneToOne(..)
     */
    protected DbDescriptorEntry(final String otherPropertyName, DbMultiplicityRange range, final int associationType, boolean cached) {
        super();

        // ForeignKey is on other side of association
        this.local = false;

        this.attributeName = otherPropertyName;
        this.range = range;
        this.associationType = associationType;
        this.cached = cached;
    }

    /**
     * DbDescriptorEntry constructor.
     *
     * @see DbDescriptor#add(..)
     */
    protected DbDescriptorEntry(final String attributeName, DbMultiplicityRange range, DbFieldTypeDescriptor fieldType) {
        super();

        this.attributeName = attributeName;
        this.range = range;
        this.fieldType = fieldType;
    }

    /**
     * DbDescriptorEntry constructor.
     *
     * @see DbDescriptor#addOneToOneReference(..)
     */
    protected DbDescriptorEntry(final String attributeRoleName, DbMultiplicityRange range, DbFieldTypeDescriptor fieldType, final int associationType, boolean cached) {
        super();

        this.attributeName = attributeRoleName;
        this.range = range;
        this.fieldType = fieldType;
        this.associationType = associationType;
        this.cached = cached;
    }

    /**
     * DbDescriptorEntry constructor.
     *
     * @see DbDescriptor#add("many")
     */
    protected DbDescriptorEntry(final String attributeName, DbMultiplicityRange range, DbFieldTypeDescriptor fieldType, final String mapName) {
        super();

        // ForeignKey is on other side of association
        this.local = false;

        this.attributeName = attributeName;
        this.range = range;
        this.fieldType = fieldType;
        this.mapName = mapName;
    }

    /**
     * DbDescriptorEntry constructor.
     *
     * @see DbDescriptor#addCode("many")
     */
    protected DbDescriptorEntry(final String attributeName, DbMultiplicityRange range, java.lang.Class<? extends DbCode> dbCode, final String mapName) {
        super();

        // ForeignKey is on other side of association
        this.local = false;

        this.attributeName = attributeName;
        this.range = range;
        this.baseClass = dbCode;
        this.mapName = mapName;
    }

    /**
     * Allow Descriptor-Entries to be copied for DbSessionBeans.
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Object newInstance = super.clone();

        // ((DbDescriptorEntry)newInstance).cloned = true;
        return newInstance;
    }

    /**
     * Return the AssociationType mapping to local Property.
     */
    public final int getAssociationType() {
        /*
         * if (!isLocal()) { throw new
         * ch.softenvironment.util.DeveloperException(this,
         * "getAssociationType()", "call #getOtherPropertyName() instead"); }
         */
        return associationType;
    }

    /**
     * Return the AttributName mapping to local Property.
     *
     * @see DbDescriptor#getAttribute(ch.softenvironment.jomm.DbObjectServer, String)
     */
    protected String getAttributeName() {
        if (!isLocal()) {
            throw new ch.softenvironment.util.DeveloperException("call #getOtherPropertyName() instead");
        }
        return attributeName;
    }

    /**
     * Case: 1:[0..*]
     *
     * @return
     */
    public Class<? extends DbObject> getBaseClass() {
        return baseClass;
    }

    /**
     * Return the original class where this Descriptor was cloned from.
     *
     * @see DbDescriptor#isCloned()
     */
    public java.lang.Class<? extends DbObject> getCloned() {
        return dbObjectOriginal;
    }

    /**
     * Case: 1:[0..1]
     *
     * @eturn DbFieldTypeDescriptor
     */
    public DbFieldTypeDescriptor getFieldType() {
        return fieldType;
    }

    /**
     * Return name of T_Map-Table.
     */
    public String getMapName() {
        return mapName;
    }

    /**
     * Return the Property on other side of Association pointing to this side.
     *
     * @see DbDescriptor#addOneToOne(..)
     */
    public String getOtherPropertyName() {
        if (isLocal()) {
            throw new ch.softenvironment.util.DeveloperException("call #getAtributeName() instead");
        }
        return attributeName;
    }

    /**
     * Return property mapping id of T_MAP_myTable.T_Id_OwnerFor for Cloned entries.
     *
     * @see #setCloned(..)
     */
    public String getOwnerIdPropertyName() {
        return propertyOwnerId;
    }

    /**
     * DbDescriptorField constructor for a flat-Attribute Mapping.
     */
    public DbMultiplicityRange getRange() {
        return range;
    }

    /**
     * Case: A.B=1:[0..1] where B-Elements are cached.
     *
     * @return boolean
     */
    protected boolean isCached() {
        return cached;
    }

    /**
     * Return whether Property must be saved by owning DbObject or associated DbObject.
     *
     * @return boolean
     */
    public boolean isLocal() {
        return local;
    }

    /**
     * Return whether Value for Property is required mandatory.
     */
    public boolean isMandatory() {
        if (getRange() != null) {
            return getRange().getLower() > 0;
        }
        return false;
    }

    /**
     * Case: A.B=1:[0..*] where B-Elements are ordered.
     *
     * @return
     */
    public boolean isOrdered() {
        return ordered;
    }

    /**
     *
     */
    protected void setBaseClass(Class<? extends DbObject> dbObject) {
        this.baseClass = dbObject;
    }

    /**
     * Set Cloned.
     *
     * @see DbDescriptor#cloneEntry(Class, String)
     */
    protected void setCloned(java.lang.Class<? extends DbObject> dbObject, final String propertyOwnerId) {
        this.dbObjectOriginal = dbObject;
        this.propertyOwnerId = propertyOwnerId;
    }
}

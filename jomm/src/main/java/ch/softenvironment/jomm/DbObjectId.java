package ch.softenvironment.jomm;

import ch.softenvironment.jomm.mvc.model.DbObject;
import lombok.extern.slf4j.Slf4j;

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
 * Simple Unique Object-Id Specification for DbObject's within this framework context. Identity-type => "application"
 *
 * @author Peter Hirzel
 */
@Slf4j
public final class DbObjectId implements DbIdentity {

    private static final long serialVersionUID = 1L;

    private final transient java.lang.Long id;
    private final transient java.lang.Class<? extends DbObject> dbObject;

    /**
     * Create a DbIdentity for a specific Instance of given type dbObject by its id.
     *
     * @param dbObject type of instantiable DbObject
     * @param id DbObject#id by convention of type Long in JOMM!
     */
    public DbObjectId(Class<? extends DbObject> dbObject, Long id) {
        super();

        if ((dbObject == null) || (id == null)) {
            throw new IllegalArgumentException("Invalid ObjectId paramters");
        }

        this.dbObject = dbObject;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if ((o != null) && (o instanceof DbObjectId)) {
            return this.getModelClass().equals(((DbObjectId) o).getModelClass()) && this.getId().equals(((DbObjectId) o).getId());
        }

        return false;
    }

    /**
     * Return the Primary Key value
     */
    public final java.lang.Long getId() {
        return id;
    }

    /**
     * Return the type of identity.
     */
    @Override
    public final java.lang.String getIdentityType() {
        return APPLICATION;
    }

    /**
     * Return the Class to build an Instance.
     */
    public final java.lang.Class<? extends DbObject> getModelClass() {
        return dbObject;
    }

    /**
     * Return the Class-name to build an Instance.
     */
    @Override
    public java.lang.String getName() {
        return getModelClass().getName();
    }

    @Override
    public final int hashCode() {
        log.warn("Developer warning: might be different for EQUAL DbObjectId's with same ID and Classname if instantiated severally");

        return super.hashCode();
    }

    @Override
    public java.lang.String toString() {
        return getName() + "#" + getId();
    }
}

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

import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.DbQueryBuilder;

/**
 * <b>EntityBean Relationships according to Enterprise Java Bean (EJB)
 * Specification:</b> <quote>Container-managed relationships--represented by the cmr fields in the deployment descriptor--support both one-to-one relationships and one-to-many relationships. With
 * one-to-many relationships, the entity bean uses a Java Collection to represent the "many" side. This ability to manage relationships is closely aligned to relationship modeling in relational
 * databases.</quote>
 * <p>
 * Association in Case A:B=n:n where the Association attributes are kept in a Map C.
 * <p>
 * The identities of A AND B both together are UNIQUE within C.
 *
 * @author Peter Hirzel
 */
public abstract class DbRelationshipBean extends DbChangeableBean {

    /**
     * @see DbObject(DbObjectServer)
     */
    protected DbRelationshipBean(DbObjectServer objectServer) {
        super(objectServer);
    }

    /**
     * Return a DbQueryBuilder. For e.g.: "SELECT SEBUJob.PersonID FROM SEBUJob WHERE CompanyID=1" where targetAttributeKey => PersonID and whereAttributKey => CompanyID
     */
    public static DbQueryBuilder getSelectQuery(DbObjectServer objectServer,
        Class dbManyToManyMap, String targetAttributeKey,
        String whereAttributKey, Long id) {
        DbQueryBuilder builder = objectServer.createQueryBuilder(
            DbQueryBuilder.SELECT, "Target-Object of n:n in: "
                + dbManyToManyMap.getName());
        builder.setTableList(dbManyToManyMap);
        builder.setAttributeList(targetAttributeKey);
        builder.addFilter(whereAttributKey, id);

        return builder;
    }
}

package ch.softenvironment.jomm.target.sql.oracle;

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
 * QueryBuilder for ORACLE 9i Specialities.
 *
 * @author Peter Hirzel
 */
public class OracleQueryBuilder extends ch.softenvironment.jomm.target.sql.SqlQueryBuilder {

    /**
     * OracleQueryBuilder constructor comment.
     *
     * @param objectServer ch.softenvironment.jomm.DbObjectServer
     * @param queryType java.lang.Integer
     * @param useCase java.lang.String
     */
    public OracleQueryBuilder(ch.softenvironment.jomm.DbObjectServer objectServer, Integer queryType, String useCase) {
        super(objectServer, queryType, useCase);
    }

    /**
     * ORACLE does not support the "AS" of an Alias, for e.g. SELECT Field AS MyAlias,... => SELECT Field MyAlias,...
     */
    @Override
    protected String checkAlias(String sqlString) {
        return ch.softenvironment.util.StringUtils.replace(sqlString, " AS ", " ");
    }
}

package ch.softenvironment.jomm.target.sql.msaccess;
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
 * QueryBuilder for MS Access Specialities.
 *
 * @author Peter Hirzel
 */
public class MsAccessQueryBuilder extends ch.softenvironment.jomm.target.sql.SqlQueryBuilder {

    /**
     * MsAccessQueryBuilder constructor comment.
     *
     * @param objectServer ch.softenvironment.jomm.DbObjectServer
     * @param queryType java.lang.Integer
     * @param useCase java.lang.String
     */
    public MsAccessQueryBuilder(ch.softenvironment.jomm.DbObjectServer objectServer, Integer queryType, String useCase) {
        super(objectServer, queryType, useCase);
    }

    /**
     * Return the built SQL-Query.
     */
    @Override
    public String getQuery() {
        if ((getType() == LOCK) || (getType() == UNLOCK)) {
            return null;
        } else {
            return super.getQuery();
        }
    }

    /**
     * MS Access Fields/Tablenames with whitespaces must be put in brackets [].
     */
    @Override
    protected String tagTargetExpression(String value) {
        if ((value != null) && (value.indexOf(" ") >= 0)) {
            return "[" + value + "]";
        } else {
            return value;
        }
    }
}

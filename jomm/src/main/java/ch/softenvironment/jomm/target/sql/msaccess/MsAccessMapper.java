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
 * Non-Standard-SQL Mappings for MS Access 2000.
 * <p>
 * Design Pattern: <b>Adapter</b>
 *
 * @author Peter Hirzel
 */
public class MsAccessMapper extends ch.softenvironment.jomm.target.sql.SqlMapper {

	/**
	 * MsAccessMapper constructor.
	 */
	protected MsAccessMapper() {
		super();
	}

	@Override
	public java.lang.String describeTargetException(java.lang.Exception exception) {
		if (exception instanceof java.sql.SQLException) {
			java.sql.SQLException sqlEx = (java.sql.SQLException) exception;

			if (sqlEx.getSQLState() != null) {
				if (sqlEx.getSQLState().equals("S1000" /* $NON-NLS$ */)) {
					// TODO NLS get proper message from DLL or anywhere else!
					return "- Doppelte Schluessel (z.B. Feldwerte schon in anderen Datenobjekten vorhanden)" + "\n"
						+ "- Sperrverletzung (z.B. jemand anders arbeitet gerade mit dem Datensatz)" + "\n"
						+ "- Abhaengigkeiten (z.B. Konsistenzbedingungen zu anderen Datenobjekten)";
				}
			}
			return null;
		} else {
			return super.describeTargetException(exception);
		}
	}

	@Override
	public java.lang.Object mapToTarget(ch.softenvironment.jomm.descriptor.DbFieldType value) {
		// TODO subclass must implement
		return null;
	}

	@Override
	public Object mapToTarget(java.util.Date value, final int type) {
		if (value == null) {
			return super.mapToTarget(value, type);
		} else if (type == ch.softenvironment.jomm.descriptor.DbDateFieldDescriptor.DATE) {
			// special Format
			java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("#MM/dd/yyyy#");
			return sf.format(value);
		} else {
			return super.mapToTarget(value, type);
		}
	}
}

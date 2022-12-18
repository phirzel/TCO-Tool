package ch.softenvironment.jomm.mvc.controller;

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
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.util.BeanReflector;
import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.util.Evaluator;
import java.util.Locale;

/**
 * Evaluator for JOMM specific class properties
 *
 * @author Peter Hirzel
 */
public class DbObjectEvaluator implements Evaluator {

    private final java.util.Locale locale;

    public DbObjectEvaluator() {
        this(Locale.getDefault());
    }

    public DbObjectEvaluator(java.util.Locale locale) {
        super();
        this.locale = locale;
    }

    /**
     * Evaluate localized text value according to generic JOMM types, such as - DbCodeType - DbNlsString
     */
    @Override
    public Object evaluate(Object owner, final String property) {
        if (owner != null) {
            try {
                Object value = (new BeanReflector(owner, property)).getValue();
                if (value != null) {
                    if (value instanceof DbCodeType) {
                        // typically looking for "name" property
                        return evaluate((DbNlsString) (new BeanReflector<DbCodeType>((DbCodeType) value, DbObject.PROPERTY_NAME)).getValue());
                    } else if (value instanceof DbNlsString) {
                        return evaluate((DbNlsString) value);
                    } else {
                        return value;
                    }
                }
            } catch (Throwable e) {
                throw new DeveloperException("generic property error: " + e.getLocalizedMessage());
            }
        }
        return null;
    }

    /**
     * Return translation for default locale.
     *
     * @param nls
     * @return
     */
    private String evaluate(DbNlsString nls) {
        return nls.getValue(locale);
    }
}

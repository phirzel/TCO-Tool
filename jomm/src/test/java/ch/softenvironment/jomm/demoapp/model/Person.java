package ch.softenvironment.jomm.demoapp.model;
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

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbTextFieldDescriptor;
import ch.softenvironment.jomm.mvc.model.DbEntityBean;

/**
 * @author generated by the umleditor
 * @version $Revision: 1.3 $ $Date: 2006-07-05 16:35:34 $
 */
public abstract class Person extends DbEntityBean {

    protected Person(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    private String fieldName;

    public String getName() {
        refresh(false); // read lazy initialized objects
        return fieldName;
    }

    public void setName(String name) {
        String oldValue = fieldName;
        fieldName = name;
        firePropertyChange("name", oldValue, fieldName);
    }

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(Person.class);
        descriptor.add("name", "Name", new DbTextFieldDescriptor(255), new DbMultiplicityRange(0, 1));
        return descriptor;
    }
}

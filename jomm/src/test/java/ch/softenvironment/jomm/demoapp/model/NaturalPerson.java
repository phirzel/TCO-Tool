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

import ch.softenvironment.jomm.descriptor.DbDateFieldDescriptor;
import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbFieldTypeDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbTextFieldDescriptor;
import java.util.Date;

/**
 * @author generated by the umleditor
 */
public class NaturalPerson extends Person {

    protected NaturalPerson(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    private String fieldFirstName;

    public String getFirstName() {
        refresh(false); // read lazy initialized objects
        return fieldFirstName;
    }

    public void setFirstName(String firstName) {
        String oldValue = fieldFirstName;
        fieldFirstName = firstName;
        firePropertyChange("firstName", oldValue, fieldFirstName);
    }

    private Date fieldBirthday;

    public Date getBirthday() {
        refresh(false); // read lazy initialized objects
        return fieldBirthday;
    }

    public void setBirthday(Date birthday) {
        Date oldValue = fieldBirthday;
        fieldBirthday = birthday;
        firePropertyChange("birthday", oldValue, fieldBirthday);
    }

    private java.lang.Boolean fieldSex;

    public java.lang.Boolean getSex() {
        refresh(false); // read lazy initialized objects
        return fieldSex;
    }

    public void setSex(java.lang.Boolean sex) {
        java.lang.Boolean oldValue = fieldSex;
        fieldSex = sex;
        firePropertyChange("sex", oldValue, fieldSex);
    }

    private java.util.List<Long> fieldProjectId = new java.util.ArrayList<Long>();

    public java.util.List<Long> getProjectId() {
        refresh(false); // read lazy initialized objects
        return fieldProjectId;
    }

    public void setProjectId(java.util.List<Long> project) {
        java.util.List<Long> oldValue = fieldProjectId;
        fieldProjectId = project;
        firePropertyChange("projectId", oldValue, fieldProjectId);
    }

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(NaturalPerson.class);
        descriptor.add("firstName", "FirstName",
            new DbTextFieldDescriptor(100), new DbMultiplicityRange(0, 1));
        descriptor.add("birthday", "birthday", new DbDateFieldDescriptor(
            DbDateFieldDescriptor.DATE), new DbMultiplicityRange(0, 1));
        descriptor.add("sex", "sex", new DbFieldTypeDescriptor(
            java.lang.Boolean.class), new DbMultiplicityRange(0, 1));
        descriptor
            .addAssociationAttributed(
                DbDescriptor.ASSOCIATION,
                "projectId",
                new DbMultiplicityRange(0, DbMultiplicityRange.UNBOUND),
                new DbMultiplicityRange(0, DbMultiplicityRange.UNBOUND),
                Role.class, "memberId");
        return descriptor;
    }

    @Override
    public Object jdoGetVersion() {
        return null;
    }

    @Override
    public boolean jdoIsDetached() {
        return false;
    }

    @Override
    public Object jdoNewObjectIdInstance(Object o) {
        return null;
    }
}

package ch.softenvironment.jomm.demoapp.xml;

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
 */

import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.demoapp.model.Project;
import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.mvc.model.DbEntityBean;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Pseudo DbObject, just to maintain all Projects within a root container.
 *
 * @author Peter Hirzel
 */
public class XmlDemoAppModel extends DbEntityBean {

    protected XmlDemoAppModel(@NonNull DbObjectServer objectServer) {
        super(objectServer);
    }

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(XmlDemoAppModel.class);
        descriptor.addOneToMany(DbDescriptor.COMPOSITION, "projects", "modelId", new DbMultiplicityRange(0, DbMultiplicityRange.UNBOUND), Project.class, false);
        return descriptor;
    }

    private List<Project> fieldProjects = new ArrayList<>();

    public List<Project> getProjects() {
        refresh(isModified());
        return fieldProjects;
    }

    public void setProjects(List<Project> projects) {
        java.util.List<Project> oldValue = fieldProjects;
        fieldProjects = projects;
        firePropertyChange("projects", oldValue, fieldProjects);
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

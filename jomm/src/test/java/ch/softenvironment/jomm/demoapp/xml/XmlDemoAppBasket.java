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

import ch.softenvironment.jomm.DbObjectId;
import ch.softenvironment.jomm.demoapp.XmlMapperSuite;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.jomm.target.xml.IliBasket;

import java.util.Set;

/**
 * @author Peter Hirzel
 */
class XmlDemoAppBasket implements IliBasket {

    private Object root = null;

    @Override
    public Set<Class<? extends DbCodeType>> getCodes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getComment() {
        return XmlMapperSuite.SCHEMA + " TestCases";
    }

    @Override
    public String getElementName() {
        return "ch.softenvironment.demoapp.testsuite." + XmlMapperSuite.SCHEMA;
    }

    @Override
    public Object getModelRoot() {
        return root;
    }

    @Override
    public DbObjectId getObjectId() {
        /*Unknown*/
        return new DbObjectId(XmlDemoAppModel.class, (long) -1);
    }

    @Override
    public void setModelRoot(Object root) {
        this.root = root;
    }
}

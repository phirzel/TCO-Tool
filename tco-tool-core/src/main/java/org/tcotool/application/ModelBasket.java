package org.tcotool.application;

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
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.jomm.target.xml.IliBasket;
import ch.softenvironment.jomm.target.xml.XmlObjectServer;
import java.util.Set;
import org.tcotool.model.TcoModel;

/**
 * Encapsulates the org.tcotool.model.* Classes in an INTERLIS Basket.
 *
 * @author Peter Hirzel
 */
public class ModelBasket implements IliBasket {

    private DbObjectServer server = null;
    private Object modelRoot = null;

    public ModelBasket(DbObjectServer server) {
        super();
        this.server = server;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.jomm.target.xml.IliBasket#getElementName()
     */
    @Override
    public String getElementName() {
        return server.getPersistenceManagerFactory().getConnectionURL();
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.jomm.target.xml.IliBasket#getInitialComment()
     */
    @Override
    public String getComment() {
        return "Basket of Configuration-Objects of org.tcotool.model.*";
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.jomm.target.xml.IliBasket#getCodes()
     */
    @Override
    public Set<Class<? extends DbCodeType>> getCodes() {
        return ((XmlObjectServer) server).getCodeTypes();
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.jomm.target.xml.IliBasket#getModelRoot()
     */
    @Override
    public Object getModelRoot() {
        return modelRoot;
    }

    /*
     * (non-Javadoc)
     *
     * @see ch.softenvironment.jomm.target.xml.IliBasket#getObjectId()
     */
    @Override
    public DbObjectId getObjectId() {
        /* Unknown */
        return new DbObjectId(TcoModel.class, (long) -1);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ch.softenvironment.jomm.target.xml.IliBasket#setModelRoot(java.lang.Object
     * )
     */
    @Override
    public void setModelRoot(Object root) {
        this.modelRoot = root;
    }
}

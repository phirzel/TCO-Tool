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
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.jomm.target.xml.IliBasket;
import java.util.Set;
import org.tcotool.presentation.Diagram;

/**
 * Encapsulates the org.tcotool.presentation.* Classes in an INTERLIS Basket.
 *
 * @author Peter Hirzel
 */
public class PresentationBasket implements IliBasket {

    private Object diagram = null;

    public PresentationBasket() {
        super();
    }

    /* (non-Javadoc)
     * @see ch.softenvironment.jomm.target.xml.IliBasket#getElementName()
     */
    @Override
    public String getElementName() {
        return "tcotool.presentation";
    }

    /* (non-Javadoc)
     * @see ch.softenvironment.jomm.target.xml.IliBasket#getInitialComment()
     */
    @Override
    public String getComment() {
        return "Basket of DependencyGraph-Objects of org.tcotool.presentation.*";
    }

    /* (non-Javadoc)
     * @see ch.softenvironment.jomm.target.xml.IliBasket#getCodes()
     */
    @Override
    public Set<Class<? extends DbCodeType>> getCodes() {
        // no codes here
        return null;
    }

    /* (non-Javadoc)
     * @see ch.softenvironment.jomm.target.xml.IliBasket#getModelRoot()
     */
    @Override
    public Object getModelRoot() {
        return diagram;
    }

    /* (non-Javadoc)
     * @see ch.softenvironment.jomm.target.xml.IliBasket#getObjectId()
     */
    @Override
    public DbObjectId getObjectId() {
        /*Unknown*/
        return new DbObjectId(Diagram.class, (long) -1);
    }

    /* (non-Javadoc)
     * @see ch.softenvironment.jomm.target.xml.IliBasket#setModelRoot(java.lang.Object)
     */
    @Override
    public void setModelRoot(Object root) {
        this.diagram = root;
    }
}

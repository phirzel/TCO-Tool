package ch.softenvironment.jomm.target.xml;

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

/**
 * Encapsulate an INTERLIS Basket structure to be mapped to an XML-instance.
 *
 * @author Peter Hirzel <i>soft</i>Environment
 * @version $Revision: 1.2 $ $Date: 2006-07-05 16:32:25 $
 * @see XmlEncoder#encodeBasket()
 */
public interface IliBasket {

    /**
     * Return the Basket name resp. the name of the XML-tag.
     *
     * @return
     */
    String getElementName();

    /**
     * Define an initial comment for Basket.
     *
     * @return
     */
    String getComment();

    /**
     * Return the list of DbCode's to be mapped in XML-instance.
     *
     * @return
     */
    java.util.Set<Class<? extends DbCodeType>> getCodes();

    /**
     * Return the root of the model-tree within the Basket.
     *
     * @return
     * @see #getObjectId()
     */
    Object getModelRoot();

    /**
     * Set the root of the model-tree within the Basket.
     */
    void setModelRoot(Object root);

    /**
     * Return the Object-Id of modelRoot.
     *
     * @return
     * @see #getModelRoot()
     */
    DbObjectId getObjectId();
}

package ch.softenvironment.jomm.serialize;

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

/**
 * Utility to encapsulate element-Attributes within an XML-Stream. For e.g. <MyElement attrName="attrValue"...
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class AttributeList {

	// ordered map of attribute name/value pairs
	private final java.util.List<java.util.List<String>> attributes = new java.util.ArrayList<java.util.List<String>>();

	/**
	 * AttributeList constructor comment.
	 */
	public AttributeList() {
		super();
	}

	/**
	 * AttributeList constructor comment.
	 */
	public AttributeList(final String name, final String value) {
		super();

		add(name, value);
	}

	/**
	 * Add an attribute name/value pair to ordered Attribute-List.
	 */
	public void add(final String attributeName, final String attributeValue) {
		java.util.List<String> entry = new java.util.ArrayList<String>(2);
		entry.add(attributeName);
		entry.add((attributeValue == null) ? "" : attributeValue);
		attributes.add(entry);
	}

	/**
	 * Return value of attribute at given Index.
	 */
	public String getValue(final int index) {
		java.util.List<String> entry = attributes.get(index);
		return entry.get(1);
	}

	/**
	 * Return an iterator to a List of AttributeName/Value Pair, where list.get(0) => AttributeName list.get(1) => AttributeValue
	 */
	protected java.util.Iterator<java.util.List<String>> iteratorAttributes() {
		return attributes.iterator();
	}
}

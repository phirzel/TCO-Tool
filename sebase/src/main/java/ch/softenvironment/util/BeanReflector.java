package ch.softenvironment.util;

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
 * Reflection-Utility for JavaBeans. Encapsulates an Object's property according to Java Beans Specification, by means a Property is defined usually as follows, if MyObject has a Property
 * "myProperty": -> MyObject#fieldMyProperty // field/property itself -> MyObject#setMyProperty(Object any) // the setter-Method -> MyObject#getMyProperty() // the getter-Method
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class BeanReflector<T> {

	private transient T source = null;
	private transient String property = null;

	public static final int NONE = 0;
	public static final int GETTER = 1;
	public static final int SETTER = 2;
	public static final int GETTER_AND_SETTER = GETTER + SETTER;

	/**
	 * Determine whether given son inherits from predecessor somewhere in inheritance chain.
	 */
	public static final boolean isInherited(java.lang.Class<?> son, Class<?> predecessor) {
		java.lang.Class<?> current = son;
		while ((current != Object.class) && (current != null /*
		 * simple types such
		 * as boolean
		 */)) {
			if (current.equals(predecessor)) {
				return true;
			}
			current = current.getSuperclass();
		}
		return false;
	}

	/**
	 * ObjectChange constructor.
	 *
	 * @param source Object to be changed
	 * @param property property of interest of given source (starts with lowercase)
	 *     <p>
	 *     Example: change = new ObjectProperty(Locale.getDefault, "language") if (change.getValue().equals("de")) { change.setValue("fr"); // switch default Language }
	 */
	public BeanReflector(T source, final String property) {
		this.source = source;
		this.property = convertPropertyName(property);
	}

	/**
	 * Return captured instance.
	 *
	 * @return
	 */
	public T getSource() {
		return source;
	}

	/**
	 * Return the Property-Name.
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * Transform .
	 */
	protected String getPropertyUpper() {
		return StringUtils.firstLetterToUppercase(property);
	}

	/**
	 * Return the Field for given Property-Name.
	 */
	public java.lang.reflect.Field getField() {
		try {
			return getSource().getClass().getField("field" + getPropertyUpper());
		} catch (NoSuchFieldException e) {
			throw new DeveloperException("<" + getSource().getClass().getName() + "> must implement <" + "public field" + getPropertyUpper() + ">", null, e);
		}
	}

	/**
	 * Return the Getter Method for the property.
	 */
	private java.lang.reflect.Method getGetterMethod() throws NoSuchMethodException {
		Class<?>[] parameterTypes = {};
		return getSource().getClass().getMethod("get" + getPropertyUpper(), parameterTypes);
	}

	/**
	 * Return the setter-Method for given Object and Property.
	 */
	private java.lang.reflect.Method getSetterMethod() throws NoSuchMethodException {
		Class<?>[] parameterTypes = {getGetterReturnType()};
		return getSource().getClass().getMethod("set" + getPropertyUpper(), parameterTypes);
	}

	/**
	 * Return the Getter Method return type.
	 */
	public java.lang.Class<?> getGetterReturnType() throws NoSuchMethodException {
		return getGetterMethod().getReturnType();
	}

	/**
	 * Return the Source's Property-value by calling the sources getter-Method. Primitive types (such as boolean, int, etc) will be returned as their Class Type (such as Boolean, Integer, etc)!
	 */
	public Object getValue() throws IllegalAccessException, java.lang.reflect.InvocationTargetException {
		try {
			Object[] parameters = {};
			return getGetterMethod().invoke(getSource(), parameters);
		} catch (NoSuchMethodException e) {
			throw new DeveloperException("<" + getSource().getClass().getName() + "> must implement <" + "get" + getPropertyUpper() + "()", null, e);
		}
	}

	/**
	 * Return how the encapsulated Object implements the given property.
	 */
	public int hasProperty() {
		int implementationDegree = NONE;

		try {
			getGetterMethod();
			implementationDegree = GETTER;
		} catch (NoSuchMethodException e) {
			// ignore
			// Tracer.getInstance().debug(this, "hasProperty()",
			// "NoSuchMethodException ignored: no public getter-Method for property <"
			// + getProperty() + "> implemented");
		}

		try {
			getSetterMethod();
			implementationDegree = implementationDegree + SETTER;
		} catch (NoSuchMethodException e) {
			// ignore
			// Tracer.getInstance().debug(this, "hasProperty()",
			// "NoSuchMethodException ignored: no public setter-Method for property <"
			// + getProperty() + "> implemented");
		}

		return implementationDegree;
	}

	/**
	 * Set the Property by its <b>Field</b> with given value => no change-event. Property-Field is assumed to be <b>public</b>.
	 */
	public void setField(Object value) {
		try {
			getField().set(getSource(), value);
		} catch (IllegalAccessException e) {
			throw new DeveloperException("<" + getSource().getClass().getName() + "#field" + getPropertyUpper() + ">  must be PUBLIC!", null, e);
		}
	}

	/**
	 * Set the Property by its <b>setter-Method</b> with given value. The Setter-Method must be public. For e.g.: source.setProperty(Object value) Primitive types (such as boolean, int, etc) will be
	 * returned as their Class Type (such as Boolean, Integer, etc)!
	 */
	public void setValue(Object value) throws java.lang.reflect.InvocationTargetException, IllegalAccessException {
		try {
			Object[] args = {value};
			getSetterMethod().invoke(getSource(), args);
		} catch (NoSuchMethodException e) {
			throw new DeveloperException("<" + getSource().getClass().getName() + "> must implement <" + "set" + getPropertyUpper() + "(<type>)", null, e);
		}
	}

	/**
	 * Create an instance of the given Class by the default constructor. MyObject object = (MyObject)BeanReflector.createInstance(MyObject.class);
	 *
	 * @param target The Target-Object's Type
	 */
	public static java.lang.Object createInstance(java.lang.Class<?> target) throws InstantiationException, java.lang.reflect.InvocationTargetException {
		try {
			Class<?>[] types = {};
			Object[] args = {};
			java.lang.reflect.Constructor<?> constructor = target.getConstructor(types);
			return constructor.newInstance(args);
		} catch (NoSuchMethodException e) {
			throw new DeveloperException("Class <" + target + "> must implement: Constructor()!", null, e);
		} catch (IllegalAccessException e) {
			throw new DeveloperException("Class <" + target + ".Constructor()> must be: PUBLIC!", null, e);
		}
	}

	/**
	 * Clone the value of given Source.
	 *
	 * @see Object#clone() definitions
	 */
	public Object cloneValue() throws IllegalAccessException, java.lang.reflect.InvocationTargetException {
		Object value = getValue();
		if (value == null) {
			return null;
		} else if (value instanceof String) {
			return value.toString();
		} else if (value instanceof Long) {
			return Long.valueOf(value.toString());
		} else if (value instanceof Double) {
			return new Double(value.toString());
		} else if (value instanceof Integer) {
			return Integer.valueOf(value.toString());
		} else if (value instanceof Float) {
			return new Float(value.toString());
		} else if (value instanceof Boolean) {
			// unique instances of true/false in JRE 1.4
			return value;
		} else if (value instanceof java.util.Date) {
			return new java.util.Date(((java.util.Date) value).getTime());
		} else {
			// TOD NYI: type not cloneable yet => value.getClass()
			return null;
		}
	}

	/**
	 * Transform: - "MyProperty"=>"myProperty" - "myProperty"=> as is
	 */
	public static String convertPropertyName(final String property) {
		return property.substring(0, 1).toLowerCase() + property.substring(1);
	}
}

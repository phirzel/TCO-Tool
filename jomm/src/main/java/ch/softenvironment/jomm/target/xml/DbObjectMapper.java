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

import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.datatypes.DbNlsString;
import ch.softenvironment.jomm.descriptor.DbDateFieldDescriptor;
import ch.softenvironment.jomm.descriptor.DbDescriptorEntry;
import ch.softenvironment.jomm.descriptor.DbIdFieldDescriptor;
import ch.softenvironment.jomm.implementation.DbState;
import ch.softenvironment.jomm.mvc.model.DbChangeableBean;
import ch.softenvironment.jomm.mvc.model.DbEnumeration;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.jomm.serialize.InterlisSerializer;
import ch.softenvironment.jomm.serialize.ObjectWriter;
import ch.softenvironment.jomm.serialize.Serializer;
import ch.softenvironment.jomm.serialize.Visitor;
import ch.softenvironment.jomm.serialize.VisitorCallback;
import ch.softenvironment.util.BeanReflector;
import ch.softenvironment.util.DeveloperException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * Mapping-Utility to encode and decode a DbObject to/from an XML-Instance.
 *
 * @author Peter Hirzel
 */
@Slf4j
class DbObjectMapper implements ObjectWriter, Visitor {

	protected static final String ROLE_ID_SUFFIX = "Id";

	private static final Map getterMap = new HashMap(); // List<GetterEntry>
	private static final Map iteratorMap = new HashMap(); // List<Methods>

	private static class GetterEntry {

		public String name = null;
		public Method get = null;
		public Method contains = null;

		GetterEntry(final String name, Method get, Method contains) {
			this.name = name;
			this.get = get;
			this.contains = contains;
		}
	}

	/* (non-Javadoc)
	 * @see ch.softenvironment.w3.ObjectWriter#writeObject(java.lang.Object, ch.softenvironment.w3.Serializer)
	 */
	@Override
	public void writeObject(Object object, Serializer serializer) throws IOException {
		if (object instanceof DbEnumeration) {
			// IGNORE: might happen if Enum is assigned to a code-Attribute
			// (should have been written before)
		} else if (object instanceof DbNlsString) {
			// IGNORE: DbNlsString are written as attributes of a DbObject -> @see below
		} else {
			DbObject obj = (DbObject) object;
			DbObjectServer server = obj.getObjectServer();
			InterlisSerializer ser = (InterlisSerializer) serializer;
			Method method = null;
			//    		 value = null;
			//    		 @see Descriptor-Registry
			ser.startElement(server.getTargetName(obj.getClass()), ser.getIliHeaderAttributes(obj.getId(), null, null));

			// write flat elements
			Iterator it = getGetters(obj).iterator();
			while (it.hasNext()) {
				GetterEntry getter = (GetterEntry) it.next();
				method = getter.get;
/*    			if (method.getParameterTypes().length > 0) {
    			    // ignore getters(with params)
    			    it.remove();
    			    continue;
    			}
*/
				String element = ch.softenvironment.util.BeanReflector.convertPropertyName(getter.name);
				Object value = null;
				try {
					value = method.invoke(obj, null);
				} catch (Exception e) {
					log.error("Developer error: " + obj.getClass() + "." + method.toString() + "=>" + e.getLocalizedMessage());
				}
				if (value != null) {
					// encode value
					if (method.getReturnType() == java.util.Date.class) {
						DbDescriptorEntry entry = obj.getObjectServer().getDescriptorEntry(obj.getClass(), element);
						int type = ((DbDateFieldDescriptor) entry.getFieldType()).getType();
						if (type == DbDateFieldDescriptor.TIME) {
							ser.element(element, ser.encodeTime((java.util.Date) value));
						} else if (type == DbDateFieldDescriptor.DATETIME) {
							ser.element(element, ser.encodeDateTime((java.util.Date) value));
						} else /* type == DbDateFieldDescriptor.DATE */ {
							ser.element(element, ser.encodeDate((java.util.Date) value));
						}
					} else if (XmlEncoder.isBuiltinClass(method.getReturnType()) /*|| method.getReturnType().isPrimitive()*/) {
						DbDescriptorEntry entry = obj.getObjectServer().getDescriptorEntry(obj.getClass(), element);
						if ((entry != null) && (entry.getFieldType() instanceof DbIdFieldDescriptor)) {
							// in DbRelationshipBean => pointing on FK
							ser.element(element.substring(0, element.length() - ROLE_ID_SUFFIX.length()), null, ser.getIliObjectRefAttributes(Long.valueOf(value.toString()), null, null, null));
						} else {
							ser.element(element, value);
						}
					} else if (value instanceof DbObject) {
						if (value instanceof DbEnumeration) {
							if (((DbEnumeration) value).getIliCode() != null) {
								ser.element(element, ((DbEnumeration) value).getIliCode());
							} else {
								log.error("Developer error: DbEnumeration has no IliCode: {}", value);
							}
						} else if (value instanceof DbNlsString) {
							ser.startElement(element);
							(new DbNlsStringMapper()).writeObject(value, ser);
							ser.endElement();
						} else {
							// 1:[0..1]
							if (((DbObject) value).getId() == null) {
								log.error("Developer error: Suppress reference to <{}]> no TID", value);
							} else {
								ser.element(element, null, ser.getIliObjectRefAttributes(((DbObject) value).getId(), null, null, null));
							}
						}
					}
				}
			}

			// List-elements
			try {
				it = getIterators(obj).iterator();
				while (it.hasNext()) {
					method = (Method) it.next();
					Iterator thisit = ((List) method.invoke(obj, null)).iterator();
					while (thisit.hasNext()) {
						// 1:[0..*]
						Object value = thisit.next();

						// should be a getter such as getMyList()
						//    				element(method.getName().substring(3 /*"get".length()*/), object2Id.get(value)); //<tag>id</tag>
						ser.element(ch.softenvironment.util.BeanReflector.convertPropertyName(method.getName().substring(3 /*"get".length()*/)), null,
							ser.getIliObjectRefAttributes(((DbObject) value).getId(), null, null, null));
					}
				}
			} catch (Exception e) {
				throw new DeveloperException("Error invoking method: " + method.toString(), null, e);
			}

			ser.endElement(/*server.getTableName(obj.getClass())*/);
		}

	}

	/* (non-Javadoc)
	 * @see ch.softenvironment.w3.Visitor#visitObject(java.lang.Object, ch.softenvironment.w3.VisitorCallback)
	 */
	@Override
	public void visitObject(Object object, VisitorCallback callback) {
		try {
			List getters = getGetters(object);
			List iterators = getIterators(object);
			Method method = null;

			// prepare writing of all objects referenced by this object
			Iterator it = getters.iterator();
			while (it.hasNext()) {
				GetterEntry getter = (GetterEntry) it.next();
				method = getter.get;

				if (!(method.getReturnType().equals(DbNlsString.class)) &&
					BeanReflector.isInherited(method.getReturnType(), DbChangeableBean.class)) {
					DbState state = ((DbObject) object).getPersistencyState();
					if (!(state.isNew() || state.isPersistent())) {
						log.warn("Developer warning: DbObject <" + object + "> not in persistent state to be saved");
						return;
					}
					Object value = method.invoke(object, null);
					if ((value != null) && !((DbObject) object).getObjectServer().equals(((DbObject) value).getObjectServer())) {
						log.warn("Developer warning: NOT SAVED: obj maintained by other Server");
						return;
					}
					callback.addPendingObject(value);
				}
			}

			it = iterators.iterator();
			while (it.hasNext()) {
				method = (Method) it.next();
				Iterator thisit = ((List) method.invoke(object, null)).iterator();

				while (thisit.hasNext()) {
					Object value = thisit.next();

					if (!XmlEncoder.isBuiltinClass(value.getClass())) {
						callback.addPendingObject(value);
					}
				}
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			log.warn("Developer warning", e);
		}
	}

	protected static List getGetters(Object object) {
		if (!getterMap.containsKey(object.getClass())) {
			findGetter(object);
		}
		return (List) getterMap.get(object.getClass());
	}

	protected static List getIterators(Object object) {
		if (!getterMap.containsKey(object.getClass())) {
			findGetter(object);
		}
		return (List) iteratorMap.get(object.getClass());
	}

	/**
	 * Determine getter-Methods for Bean-API. Method-set remains the same for all instances => cache internally for performance reasons.
	 *
	 * @param object
	 */
	private static void findGetter(Object object) {
		//TODO Tune nasty coding
		//      Map<method.getName, method>
		java.util.HashMap<String, Method> methodSet = new java.util.HashMap<String, Method>();
		Method[] methods = object.getClass().getMethods();
		for (int a = 0; a < methods.length; a++) {
			methodSet.put(methods[a].getName(), methods[a]);
		}

		List<GetterEntry> getters = new ArrayList<GetterEntry>();
		List<Method> iterators = new ArrayList<Method>();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().startsWith("get")) {
				if (methods[i].getParameterTypes().length == 0 /*IGNORE: getters(with params)*/) {
					String attrName = methods[i].getName().substring(3 /*"get".length()*/);
					if (methodSet.containsKey("set" + attrName)) {
						// public setter AND getter exist
						if (methods[i].getReturnType().equals(java.util.List.class)) {
							iterators.add(methods[i]);
						} else {
							getters.add(new GetterEntry(attrName, methods[i], null));
						}
					}
				}
			}
		}
		getterMap.put(object.getClass(), getters);
		iteratorMap.put(object.getClass(), iterators);
	}
}

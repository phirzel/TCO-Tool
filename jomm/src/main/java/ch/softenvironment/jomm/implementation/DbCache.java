package ch.softenvironment.jomm.implementation;

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

import ch.softenvironment.jomm.mvc.model.DbCode;
import ch.softenvironment.jomm.mvc.model.DbCodeType;
import ch.softenvironment.jomm.mvc.model.DbEntityBean;
import ch.softenvironment.jomm.mvc.model.DbEnumeration;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.jomm.mvc.model.DbRelationshipBean;
import ch.softenvironment.jomm.mvc.model.DbSessionBean;
import ch.softenvironment.util.Tracer;

/**
 * Cache already read Persistent-Objects in a HashMap. Key is a reference to Class and value is a Map of instances of this type. Saves performance during runtime.
 * <p>
 * HashMap classes (key => DbObject.class (parentClass)) (values => HashMap) (key => aDbObject.Id) (value => aDbObject)
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class DbCache {

	private final transient java.util.Map classes = new java.util.HashMap();
	private final transient java.util.Map groups = new java.util.HashMap();

	/**
	 * DbCache constructor.
	 */
	public DbCache() {
		super();
	}

	/**
	 * Cache all objects for a certain class and keep the incoming Order. Add the given objects to already cached codes of that type.
	 *
	 * @param code of type DbEnumeration or DbCode
	 * @param objects Instances of code-type
	 */
	public synchronized void addAll(Class code, java.util.Collection codes) {
		if (!DbObject.isCode(code)) {
			throw new ch.softenvironment.util.DeveloperException("DbEnumeration/DbCode-Objects only!");
		} else {
			// complete the list
			java.util.Collection objects = (java.util.Collection) groups.get(getKey(code));
			if (objects == null) {
				groups.put(getKey(code), codes);
			} else {
				java.util.Iterator iterator = codes.iterator();
				while (iterator.hasNext()) {
					DbObject enumeration = (DbObject) iterator.next();
					if (!objects.contains(enumeration)) {
						objects.add(enumeration);
					}
				}
			}
		}
	}

	/**
	 * Return Object of given type matching dbId if already cached or null if not cached.
	 *
	 * @param dbObject Classtype (parent class will be determined => Polymorphism)
	 * @param dbId UNIQUE-Id for given dbObject
	 * @see #put(Class, Long)
	 */
	public DbObject get(Class dbObject, final Long dbId) {
		if (DbObject.isCode(dbObject)) {
			// try group-cache first
			java.util.Collection codes = (java.util.Collection) groups.get(getKey(dbObject));

			if (codes != null) {
				java.util.Iterator iterator = codes.iterator();
				while (iterator.hasNext()) {
					DbObject object = (DbObject) iterator.next();
					if (object.getId().equals(dbId)) {
						return object;
					}
				}
				ch.softenvironment.util.Tracer.getInstance().developerWarning(
					"Code <" + dbObject.getName() + "->Key=" + dbId + "> not contained in cached Set!");
			}
		}

		// 2nd try general cache
		java.util.Map objects = getObjects(dbObject);

		if ((objects != null) && objects.containsKey(dbId)) {
			return (DbObject) objects.get(dbId);
		} else {
			return null;
		}
	}

	/**
	 * Return all currently cached Objects.
	 */
	public java.util.Collection getAll() {
		java.util.Collection objects = groups.values();
		objects.addAll(classes.values());
		return objects;
	}

	/**
	 * Find all Elements of given type.
	 */
	public java.util.Collection<? extends DbCodeType> getAll(Class<? extends DbCodeType> dbCode) {
		/*
		 * if (!DbObject.isCode(dbCode)) { throw new
		 * ch.softenvironment.util.DeveloperException
		 * ("DbEnumeration-Objects only!"); }
		 */

		return (java.util.Collection) groups.get(getKey(dbCode));
	}

	/**
	 * Store parent Class as Key in case of Inheritance.
	 */
	private java.lang.Class<?> getKey(Class<?> dbObject) {
		java.lang.Class<?> entityBeanClass = dbObject.getSuperclass();
		java.lang.Class<?> parentClass = dbObject;

		while ((entityBeanClass != DbEntityBean.class) && (entityBeanClass != DbRelationshipBean.class) && (entityBeanClass != DbEnumeration.class)
			&& (entityBeanClass != DbCode.class) && (entityBeanClass != DbSessionBean.class)) {
			parentClass = entityBeanClass;
			entityBeanClass = entityBeanClass.getSuperclass();
		}

		return parentClass;
	}

	/**
	 * Store parent Class as Key in case of Inheritance.
	 */
	private java.util.Map getObjects(Class<? extends DbObject> dbObject) {
		return (java.util.Map) classes.get(getKey(dbObject));
	}

	/**
	 * Store parent Class as Key in case of Inheritance.
	 */
	private java.util.Map getObjectsKey(Class<? extends DbObject> dbObject) {
		return (java.util.Map) classes.get(getKey(dbObject));
	}

	/**
	 * Return String of contained Objects.
	 */
	public String getTraceContents() {
		StringBuffer contents = new StringBuffer();

		java.util.Iterator<String> iterator = classes.entrySet().iterator();
		while (iterator.hasNext()) {
			contents.append(iterator.next() + "\n");
		}
		return contents.toString();
	}

	/**
	 * Cache an Object of this Class type.
	 *
	 * @param dbObject Classtype to cache (parent class will be determined => Polymorphism)
	 * @param dbId UNIQUE-Id for given dbObject
	 * @see #get(Class, Long)
	 */
	public synchronized void put(Class<? extends DbObject> dbObject, DbObject object) {
		if (object.getId() != null) {
			java.util.Map objects = getObjects(dbObject);
			if (objects == null) {
				objects = new java.util.HashMap();
				classes.put(getKey(dbObject), objects);
			}

			objects.put(object.getId(), object);
		} else {
			ch.softenvironment.util.Tracer.getInstance().developerWarning("DbObject's without ID cannot be cached");
		}
	}

	/**
	 * Cache all objects for a certain code-type (keeps the Order of given codes). If the cache previously contained a mapping for given code, the old entries are replaced.
	 *
	 * @param code of type DbEnumeration
	 * @param codes Instances of code-type
	 */
	public synchronized void putAll(Class code, java.util.Collection codes) {
		if (!DbObject.isCode(code)) {
			throw new ch.softenvironment.util.DeveloperException("DbEnumeration-Objects only!");
		} else {
			// replace the list
			groups.put(getKey(code), codes);
		}
	}

	/**
	 * Remove object from cache.
	 */
	public synchronized void uncache(DbObject dbClass) {
		if (DbObject.isCode(dbClass.getClass())) {
			Tracer.getInstance().debug("uncaching all codes for: " + dbClass);
			uncache(dbClass);
		}

		java.util.Map objects = getObjects(dbClass.getClass());

		if ((objects != null)) {
			objects.remove(dbClass.getId());
		}
	}

	/**
	 * Remove all objects of a specific Class type from cache.
	 */
	public synchronized void uncache(Class<? extends DbObject> dbObject) {
		classes.remove(getKey(dbObject));
		groups.remove(getKey(dbObject));
	}
}

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
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import ch.softenvironment.jomm.datatypes.DbNlsString;
import ch.softenvironment.jomm.descriptor.DbDateFieldDescriptor;
import ch.softenvironment.jomm.descriptor.DbDescriptorEntry;
import ch.softenvironment.jomm.descriptor.DbIdFieldDescriptor;
import ch.softenvironment.jomm.implementation.DbPropertyChange;
import ch.softenvironment.jomm.implementation.DbState;
import ch.softenvironment.jomm.mvc.model.DbCode;
import ch.softenvironment.jomm.mvc.model.DbEnumeration;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.jomm.serialize.InterlisSerializer;
import ch.softenvironment.util.BeanReflector;
import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.util.StringUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * ContentHandler for XmlDecoder.
 *
 * @author ce
 * @author Peter Hirzel
 */
@Slf4j
class XmlDecoderHandler implements org.xml.sax.ContentHandler {

	// TODO refactor as part of DbObjectMapper soon
	private XmlObjectServer server = null;
	private DbNlsStringMapper nlsHandler = null;

	private final java.util.Map objMap = new java.util.HashMap(); // map<String T_Id,
	// Object obj>
	private final java.util.Set<String> usedObjsTID = new java.util.HashSet<String>();
	private final java.util.Set<String> missingObjsTID = new java.util.HashSet<String>();
	private DbObject actualObject = null;
	private String currentElementTag = null;
	private boolean secondPass = false;
	private Collection<IliBasket> baskets = null; // IliBasket's
	private IliBasket currentBasket = null;

	// @deprecated (supports old fashioned identified DbNlsString's in
	// XML-Instance)
	// private boolean inNlsString = false;
	// private String nlsStringID = null;

	// map<Class class,map<String attrName,Method setAddValueMethod>>
	private final java.util.HashMap setValues = new java.util.HashMap();
	// map<Class class,map<String roleName,Method setAddObjectMethod>>
	private final java.util.HashMap setObjects = new java.util.HashMap();
	// map<String attrName,Method setAddValueMethod>
	private java.util.HashMap currentObjValueSets = null;
	// map<String roleName,Method setAddObjectMethod>
	private java.util.HashMap currentObjObjectAdds = null;

	private StringBuffer content = null;
	private int level = 0;

	private Locator currentLocation = null;
	private ErrorHandler eh = null;

	/**
	 * Constructor.
	 *
	 * @param server
	 * @param baskets
	 */
	protected XmlDecoderHandler(XmlObjectServer server, Collection<IliBasket> baskets) {
		super();
		if (server == null) {
			throw new IllegalArgumentException("server must not be null");
		}
		this.server = server;
		this.baskets = baskets;
	}

	private void analyseClass(Class aclass) {
		if (setValues.containsKey(aclass) || setObjects.containsKey(aclass)) {
			// class already analyzed?
			return;
		}

		// map<String attrName,Method setAddObjectMethod>
		Map<String, Method> values = new HashMap<>();
		// map<String roleName,Method setAddValueMethod>
		Map<String, Method> objects = new HashMap<>();

		Method[] methods = aclass.getMethods();
		for (int i = 0; i < methods.length; i++) {
			String methodName = methods[i].getName();
			if (methodName.startsWith("set") && (methods[i].getReturnType() == void.class)) {
				// probably a Bean-Setter
				BeanReflector reflector = new BeanReflector(actualObject, methodName.substring(3 /* "set.length() */));
				if (reflector.hasProperty() == BeanReflector.GETTER_AND_SETTER) {
					String propertyName = reflector.getProperty(); // methodName.substring(3
					// /*
					// "set.length()*/));
					Class[] parameterTypes = methods[i].getParameterTypes();
					// if (parameterTypes.length == 1) {
					if (parameterTypes[0].isPrimitive() || XmlEncoder.isBuiltinClass(parameterTypes[0]) || (parameterTypes[0].equals(java.util.Date.class))) {
						values.put(propertyName, methods[i]);
					} else {
						// for e.g. java.util.List
						objects.put(propertyName, methods[i]);
					}
					// }
				} // else ignore method
			}
		}
		setValues.put(aclass, values);
		setObjects.put(aclass, objects);
		return;
	}

	/**
	 * Establish Relationship to another DbObject pointed by AssociationEnd's TID.
	 *
	 * @param refTid Object-Identity (according to INTERLIS) or IliCode of an Enumeration
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private void associateObject(final String refTid) throws IllegalAccessException, InvocationTargetException {
		// @deprecated (before REF-Attribute)
		// Tracer.getInstance().patch(this, "endElement()",
		// "OBSOLETE: backward compatibility up to V0.9.2");

		if (StringUtils.isNullOrEmpty(refTid)) {
			return;
		}
		// get object that this value references
		if (!objMap.containsKey(refTid /* FK */)) {
			if (!missingObjsTID.contains(refTid)) {
				if (secondPass) {
					DbPropertyChange change = new DbPropertyChange(actualObject, currentElementTag);
					Class type = null;
					try {
						type = change.getGetterReturnType();
					} catch (NoSuchMethodException e) {
						log.error("no Type: {}", e.getLocalizedMessage());
					}

					if (DbObject.isCode(type)) {
						// try whether an Enum might exist for refTid
						DbEnumeration enumeration = server.getIliCode(type, refTid);
						if (enumeration == null) {
							log.error("Developer error: DbEnumeration for type <" + type + "." + refTid + "> not existing");
							log("Enumeration <IliCode=" + refTid + "> kann nicht referenziert werden (existiert nicht)!", null);
						} else {
							change.setValue(enumeration);
							return;
						}
					} else {
						if ((actualObject instanceof DbObject) && (DbObject.PROPERTY_NAME.equals(currentElementTag))) {
							// IGNORE: value is not a refTid
						} else {
							log.warn("Object <{}> not existing", refTid);
							log("Object <" + refTid + "> kann nicht referenziert werden (existiert nicht)!", null);
						}
					}
				}
				missingObjsTID.add(refTid);
			}
		} else {
			Object dest = objMap.get(refTid); // get from firstPass
			usedObjsTID.add(refTid);
			// get setXX() method of current object
			Method setter = (Method) currentObjObjectAdds.get(currentElementTag);
			if (setter == null) {
				String techPropertyName = currentElementTag + DbObjectMapper.ROLE_ID_SUFFIX;
				setter = (Method) currentObjValueSets.get(techPropertyName);
				DbDescriptorEntry entry = server.getDescriptorEntry(actualObject.getClass(), techPropertyName);
				if ((setter != null) && (entry != null) && (entry.getFieldType() instanceof DbIdFieldDescriptor)) {
					// for e.g. DbRelationshipBean n:1
					BeanReflector reflector = new BeanReflector(actualObject, techPropertyName);
					reflector.setValue(((DbObject) dest).getId());
				}
			} else if (setter.getName().startsWith("set")) {
				if (setter.getParameterTypes()[0].equals(List.class)) {
					// 1:n
					BeanReflector reflector = new BeanReflector(actualObject, currentElementTag);
					List list = (List) reflector.getValue();
					list.add(dest);
				} else {
					// 1:[0..1]
					// add.getParameterTypes()[0] is some DbObject type
					BeanReflector reflector = new BeanReflector(actualObject, currentElementTag);
					reflector.setValue(dest);
				}
			}
		}
	}

	@Override
	public void characters(char[] text, int start, int length) {
		if (content != null) {
			content.append(text, start, length);
		}
	}

	@Override
	public void endDocument() {
	}

	@Override
	public void endElement(String namespaceURI, String localName, String rawName) throws SAXException {
		// Tracer.getInstance().debug("END-Element: " + rawName);
		if ((currentBasket != null) && rawName.equals(currentBasket.getElementName())) {
			currentBasket = null;
			// Tracer.getInstance().debug(this, "endElement()", "[" +
			// (secondPass ? "2" : "1") + "] STOP reading Basket: " + rawName);
		}
		if (skipElement(rawName)) {
			return;
		}
		if (nlsHandler != null) {
			DbNlsString nlsString = nlsHandler.handleEnd(rawName, content);
			if (nlsString != null) {
				// actualObject
				try {
					// Method set =
					// (Method)currentObjValueSets.get(currentElementTag);
					// set.invoke(actualObject, new Object[]{nlsString});
					BeanReflector reflector = new BeanReflector(actualObject, currentElementTag);
					reflector.setValue(nlsString);
				} catch (IllegalAccessException e) {
					log.error("Developer error: IllegalAccess", e);
				} catch (InvocationTargetException e) {
					log.error("Developer error: InvocationTarget", e);
				}
				nlsHandler = null; // reset
			}
			return;
		}

		level--;

		/*
		 * if (actualObject instanceof DbNlsString) { //@deprecated => remove
		 * code inNlsString = false; nlsStringID = null; if (content != null) {
		 * ((DbNlsString)actualObject).setValue(content.toString()); } } else
		 */
		if ((level == 1) /* && !inNlsString */) {
			if (secondPass) {
				if (actualObject instanceof DbCode) {
					server.cacheCode((DbCode) actualObject);
				}
				try {
					if ((content == null) || StringUtils.isNullOrEmpty(content.toString())) {
						// possible for <tag REF="x01"/>
						content = null;
						return;
					}
					String value = content.toString();
					if (currentObjObjectAdds.containsKey(currentElementTag)) {
						associateObject(value);
					} else if (currentObjValueSets.containsKey(currentElementTag)) {
						// get setXX() method of current object
						Method set = (Method) currentObjValueSets.get(currentElementTag);
						Class parameterType = set.getParameterTypes()[0];
						Object valueObject = null;
						if (parameterType.equals(java.lang.Boolean.class)) {
							valueObject = Boolean.valueOf(value);
						} else if (parameterType.equals(java.lang.Long.class)) {
							valueObject = Long.valueOf(value);
						} else if (parameterType.equals(java.lang.Integer.class)) {
							valueObject = Integer.valueOf(value);
						} else if (parameterType.equals(java.lang.Double.class)) {
							valueObject = Double.valueOf(value);
						} else if (parameterType.equals(java.lang.String.class)) {
							// Java works in Unicode (UTF-8 Character-Set)
							// => no transformation necessary
							// @see XmlSerializer#encodeString()
							valueObject = value;
						} else if (parameterType.equals(java.util.Date.class)) {
							DbDescriptorEntry entry = server.getDescriptorEntry(actualObject.getClass(), currentElementTag);
							try {
								if (((DbDateFieldDescriptor) entry.getFieldType()).getType() == DbDateFieldDescriptor.DATE) {
									java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat(
										ch.softenvironment.jomm.serialize.XmlSerializer.XSD_DATE_FORMAT);
									valueObject = sf.parse(value);
								} else {
									// TODO Descriptor: TIME or TIMESTAMP
								}
							} catch (ParseException e) {
								log("Falsches DATUM-Format [" + value + "]!", e);
							}
						} else if (parameterType.equals(java.lang.Character.class)) {
							valueObject = new Character(value.charAt(0));
						} else if (parameterType.equals(java.lang.Byte.class)) {
							valueObject = Byte.valueOf(value);
						} else if (parameterType.equals(java.lang.Short.class)) {
							valueObject = Short.valueOf(value);
						} else if (parameterType.equals(java.lang.Float.class)) {
							valueObject = Float.valueOf(value);
						}
						// invoke setXX() method on current object to set value
						set.invoke(actualObject, valueObject);
					} else {
						if (!currentObjValueSets.containsKey(currentElementTag + DbObjectMapper.ROLE_ID_SUFFIX)) {
							log.error("Developer error: IGNORED: unexpected Element: {}", currentElementTag);
							log("Unerwartetes Element wird ignoriert <" + currentElementTag + ">", null);
						}
					}
				} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
					log.debug("Parsing Error - Line: {}, Message: ", currentLocation.getLineNumber(), ex.getMessage());
					throw new SAXException(ex);
				}
			}
		}
		content = null;
	}

	@Override
	public void endPrefixMapping(String prefix) {
	}

	public java.util.Set getMissingObjects() {
		return missingObjsTID;
	}

	public java.util.Map getUnreferencedObjects() {
		java.util.HashMap ret = new java.util.HashMap(objMap);
		java.util.Iterator<String> it = new java.util.HashSet(ret.keySet()).iterator();
		while (it.hasNext()) {
			String /* Long */tid = it.next();
			if (usedObjsTID.contains(tid)) {
				ret.remove(tid);
			}
		}
		return ret;
	}

	@Override
	public void ignorableWhitespace(char[] text, int start, int length) {
	}

	/**
	 * Use this method to provide an Error/Warning-log to the user. Any Schema violations should be given as Feedback.
	 *
	 * @param userMessage
	 * @param ex
	 */
	private void log(String userMessage, Exception ex) {
		// server.log(..);
		// eh.error(ex);
		log.warn("Parser-Fehler in Zeile: {}", currentLocation.getLineNumber(), ex);
	}

	@Override
	public void processingInstruction(String target, String data) {
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		currentLocation = locator;
	}

	public void setErrorHandler(ErrorHandler handler) {
		eh = handler;
	}

	protected void setSecondPass(boolean secondPass) {
		this.secondPass = secondPass;
	}

	/**
	 * Skip a parsed Element (might be there for formatting or other reasons only).
	 */
	private boolean skipElement(String element) {
		return (currentBasket == null) ||
			/*
			 * element.equals(InterlisSerializer.ELEMENT_TRANSFER) ||
			 * element.equals(InterlisSerializer.ELEMENT_HEADERSECTION) ||
			 * element.equals(InterlisSerializer.ELEMENT_ALIAS) ||
			 * element.equals(InterlisSerializer.ELEMENT_DATASECTION) ||
			 */
			// non INTERLIS-defined tags
			element.equals(XmlEncoder.ELEMENT_CODE);
	}

	@Override
	public void skippedEntity(String name) {
	}

	@Override
	public void startDocument() {
	}

	/**
	 * Start parsing a new Element.
	 */
	@Override
	public void startElement(String namespaceURI, String localName, String rawName, Attributes atts) throws SAXException {
		// Tracer.getInstance().debug("START-Element: " + rawName);
		if ((atts.getLength() > 0) && atts.getQName(0).equals(InterlisSerializer.BASKET_ID)) {
			if (currentBasket != null) {
				throw new DeveloperException("INTERLIS-Basket's must NOT BE NESTED!");
			}

			Iterator<IliBasket> iterator = baskets.iterator();
			while (iterator.hasNext()) {
				IliBasket tmpBasket = iterator.next();
				if (tmpBasket.getElementName().equals(rawName)) {
					currentBasket = tmpBasket;
					break;
				}
			}
			/*
			 * if (currentBasket == null) { Tracer.getInstance().debug(this,
			 * "startElement()", "[" + (secondPass ? "2" : "1") +
			 * "] Skip Basket: " + rawName); } else {
			 * Tracer.getInstance().debug(this, "startElement()", "[" +
			 * (secondPass ? "2" : "1") + "] Start reading Basket: " + rawName);
			 * }
			 */
			return; // skip basket tag
		}
		if (skipElement(rawName)) {
			// contents not of intereset
			return;
		}

		// check for DbNlsString
		if (DbNlsStringMapper.isNlsString(rawName)) {
			nlsHandler = new DbNlsStringMapper(server);
			return;
		}
		if (nlsHandler != null) {
			nlsHandler.handleStart(rawName);
			content = new StringBuffer();
			return;
		}

		DbObject attsObj = null;
		// Class objClass = null;
		currentElementTag = // rawName;
			// @deprecated in V0.9.2 properties start as Uppercase
			BeanReflector.convertPropertyName(rawName);

		level++;
		if (level == 2) {
			content = new StringBuffer();
		}

		if (atts.getLength() == 1) {
			if (atts.getLocalName(0).equals(InterlisSerializer.ATTRIBUTE_REFERENCE)) {
				if (secondPass) {
					// Foreign Key to Unique T_Id of a DbObject
					String /* Long */refTid = unformatIliId(atts.getValue(0));
					try {
						associateObject(refTid);
					} catch (Exception e) {
						log.error("Developer error", e);
					}
				}
				return;
			}

			// determine Class type to instantiate
			if (!atts.getLocalName(0).equals(InterlisSerializer.TECHNICAL_ID)) {
				log("Attribut <" + atts.getLocalName(0) + "> unerwartet!", null);
			}
			String qualifiedClassName = localName;
			/*
			 * if (localName.equals("ch.softenvironment.database.DbNlsString"))
			 * { //@deprecated (old NLS-Handling) qualifiedClassName =
			 * DbNlsString.class.getName(); }
			 */
			String /* Long */tid = unformatIliId(atts.getValue(0)); // UNIQUE
			// TID

			try {
				if (!objMap.containsKey(tid)) {
					// create DbObject
					attsObj = server.createInstance(server.getDbObject(qualifiedClassName));
					server.setUniqueId(attsObj, Long.valueOf(tid));
					attsObj.getPersistencyState().setNext(DbState.SAVED);
					objMap.put(tid, attsObj); // keep read Objects in mind
					/*
					 * //TODO move to #endElement() if (attsObj instanceof
					 * DbCode) { server.cacheCode((DbCode)attsObj); }
					 */

					actualObject = attsObj;
					if (actualObject.getClass().getName().equals(currentBasket.getObjectId().getName()) /*
					 * instanceof
					 * modelRoot
					 */)
						/* && (tid.equals(oid.getId())) */ {
						// SET the modelRoot!
						currentBasket.setModelRoot(actualObject);
						// TODO NYI: InterlisSerializer.TECHNICAL_ID + " of
						// wanted Root-Element is not compared
						// TODO NYI: in case XML-instance contains several model
						// classes => returned one might be wrong!
					}
				} else {
					// get object from mapping table
					actualObject = (DbObject) objMap.get(tid);
				}

				/*
				 * if (qualifiedClassName.equals(DbNlsString.class.getName())) {
				 * //@deprecated (old Nls-Handling) => remove code inNlsString =
				 * true; nlsStringID = unformatIliId(atts.getValue(0)); } else {
				 */
				analyseClass(actualObject.getClass());
				currentObjValueSets = (HashMap) setValues.get(actualObject.getClass());
				currentObjObjectAdds = (HashMap) setObjects.get(actualObject.getClass());
				// }
			} catch (NoSuchMethodException | InvocationTargetException ex) {
				log.error("tid <{}> tag <{}> class <{}>", tid, localName, qualifiedClassName, ex);
			} catch (IllegalAccessException ex) {
				log.error("tid <{}> tag <{}> class <{}>", tid, localName, qualifiedClassName, ex);
				log("Zugriffsfehler auf eine Methode der Klasse <" + qualifiedClassName + ">", ex);
				throw new SAXException(ex);
			} catch (java.lang.InstantiationException ex) {
				log.error("tid <{}> tag <{}> class <{}>", tid, localName, qualifiedClassName, ex);
				log("Klasse <" + qualifiedClassName + "> konnte nicht instanziert werden.", ex);
				throw new SAXException(ex);
			} catch (ClassNotFoundException ex) {
				log.error("tid <{}> tag <{}> class <{}>", tid, localName, qualifiedClassName, ex);
				log("Klasse <" + qualifiedClassName + "> existiert nicht im Modell und wurde deshalb beim Einlesen der XML-Datei ignoriert!", ex);
			}

		} /*
		 * else if (inNlsString && (atts.getLength() == 2)) { if (secondPass) {
		 * // content = new StringBuffer(); } else { //@deprecated => remove
		 * code DbNlsString old = (DbNlsString)actualObject; String language =
		 * atts.getValue(0); String country = atts.getValue(1); //String nlsText
		 * = atts.getValue(2); server.setUniqueId(old,
		 * Long.valueOf(nlsStringID)); old.setLanguage(language); if
		 * (!StringUtils.isNullOrEmpty(country)) { old.setCountry(country); }
		 * //old.setValue(nlsText); objMap.put(nlsStringID, old); } }
		 */ else if (atts.getLength() > 1) {
			eh.error(new SAXParseException("You can't have more then 1 Attribute!", currentLocation));
		}
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) {
	}

	/**
	 * Check correct IliId.
	 */
	private String unformatIliId(String iliId) {
		if (ch.softenvironment.util.StringUtils.isNullOrEmpty(iliId)) {
			log("Fehlende ILI-Id", null);
			return "-1";
		} else if (!iliId.startsWith(InterlisSerializer.TECHNICAL_ID_PREFIX)) {
			log("Ungueltige ILI-Id <" + iliId + ">", null);
			return iliId;
		} else {
			// strip off ILI-Prefix
			return iliId.substring(InterlisSerializer.TECHNICAL_ID_PREFIX.length());
		}
	}
}

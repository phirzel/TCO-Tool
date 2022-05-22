package ch.softenvironment.jomm.datatypes;

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

import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.DbQuery;
import ch.softenvironment.jomm.DbQueryBuilder;
import ch.softenvironment.jomm.DbTransaction;
import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbDescriptorEntry;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbTextFieldDescriptor;
import ch.softenvironment.jomm.implementation.DbPropertyChange;
import ch.softenvironment.jomm.implementation.DbState;
import ch.softenvironment.jomm.mvc.model.DbChangeableBean;
import ch.softenvironment.jomm.mvc.model.DbEnumeration;
import ch.softenvironment.jomm.mvc.model.DbSessionBean;
import ch.softenvironment.jomm.target.xml.XmlObjectServer;
import ch.softenvironment.util.DeveloperException;
import ch.softenvironment.util.StringUtils;
import ch.softenvironment.util.Tracer;
import java.util.Iterator;
import java.util.Locale;

/**
 * Structure for an NLS-String, by means a translatable String according to different Locale-Settings.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public final class DbNlsString extends DbChangeableBean {

	public static final String NO_COUNTRY = "NO_COUNTRY";
	public static final String ATTRIBUTE_TRANSLATION_ID = "T_Id_NLS";
	public static final String ATTRIBUTE_LANGUAGE = "Language";
	public static final String ATTRIBUTE_COUNTRY = "Country";
	public static final String ATTRIBUTE_NLS_TEXT = "NlsText";

	private transient DbPropertyChange ownerChange = null;
	// all translations for mapped Locale's
	private final transient java.util.Map<String, Translation> cachedLocaleValues = new java.util.HashMap<String, Translation>();

	/**
	 * Keep change of translations in mind.
	 */
	private static class Translation {

		protected int state = DbState.UNDEFINED;
		protected String text = null;

		/**
		 * @param state DbState constant
		 * @param translation the nls-text
		 */
		protected Translation(final int state, final String translation) {
			this.state = state;
			this.text = translation;
		}
	}

	/**
	 * @see #DbObject(DbObjectServer)
	 */
	protected DbNlsString(DbObjectServer objectServer) {
		super(objectServer);
		getPersistencyState().setNext(DbState.NEW);
	}

	/**
	 * Creates a persistent Instance with values read from Database.
	 */
	@Deprecated
	public DbNlsString(DbObjectServer objectServer, final Long id, java.util.Locale locale, String value, java.util.Date createDate, java.util.Date lastChange,
		String userId) {
		super(objectServer);

		objectServer.setUniqueId(this, id);

		fieldCreateDate = createDate;
		fieldLastChange = lastChange;
		fieldUserId = userId;

		// setValue(value, locale); => will allocate as DbState.NEW
		cachedLocaleValues.put(createKey(locale), new Translation(DbState.SAVED, value));

		reset(true);
	}

	/**
	 * Return the database descriptor for this Object.
	 *
	 * @return DbDescriptor
	 */
	public static DbDescriptor createDescriptor() {
		DbDescriptor descriptor = new DbDescriptor(DbNlsString.class);

		descriptor.addManyToOneReferenceId(DbDescriptor.ASSOCIATION, PROPERTY_ID, ATTRIBUTE_TRANSLATION_ID, new DbMultiplicityRange(1));
		descriptor.addUniqueProperty(PROPERTY_ID);
		// special case: these attributes will not really be mapped 1:1 because
		// they exist in 1:n table Translation only!
		/*
		 * descriptor.add(PROPERTY_LANGUAGE, ATTRIBUTE_LANGUAGE, new
		 * DbTextFieldDescriptor(2), new DbMultiplicityRange(1));
		 * descriptor.addUniqueProperty(PROPERTY_LANGUAGE);
		 * descriptor.add(PROPERTY_COUNTRY, ATTRIBUTE_COUNTRY, new
		 * DbTextFieldDescriptor(2), new DbMultiplicityRange(0, 1));
		 * descriptor.addUniqueProperty(PROPERTY_COUNTRY);
		 */
		// actually "value" should always be set, but owner descriptor must
		// decide whether whole DbNlsString is mandatory or not!
		descriptor.add("value", ATTRIBUTE_NLS_TEXT, new DbTextFieldDescriptor(DbTextFieldDescriptor.NLS), new DbMultiplicityRange(0 /*
		 * effectively
		 * 1
		 */, 1));

		return descriptor;
	}

	public final DbDescriptorEntry getDescriptorEntry(final String property) {
		return createDescriptor().getEntry(property);
	}

	/**
	 * Return the Language.
	 *
	 * @deprecated (wrong place here)
	 */
	public java.lang.String getLanguage() {
		return Locale.getDefault().getLanguage();
	}

	/**
	 * Return the property of aggregating owner instantiating this DbNlsString (null if unknown).
	 */
	public String getOwnerProperty() {
		if (ownerChange == null) {
			return null;
		} else {
			return ownerChange.getProperty();
		}
	}

	/**
	 * Return Translation for default Locale.
	 *
	 * @see #getValue(java.util.Locale)
	 */
	public java.lang.String getValue() {
		return getValue(java.util.Locale.getDefault());
	}

	/**
	 * Return a Map of <b>all Translations</b> currently cached for this NLS-String.
	 *
	 * @return Map (of Translations)
	 * @see #createKey(Locale)
	 */
	public java.util.Map<String, Translation> getAllValuesByLanguage() {
		return cachedLocaleValues;
	}

	public static DbQueryBuilder createQueryBuilderSelect(DbObjectServer server, final Long id, Locale locale) {
		DbQueryBuilder builder = server.createQueryBuilder(DbQueryBuilder.SELECT, "DbNlsString");
		builder.setTableList(DbNlsString.class);
		builder.addFilter(ATTRIBUTE_TRANSLATION_ID, id);
		builder.addFilter(ATTRIBUTE_LANGUAGE, locale.getLanguage(), DbQueryBuilder.STRICT);
		// TODO NYI: Country not considered yet!
		// builder.addFilter(DbNlsString.ATTRIBUTE_COUNTRY, locale.getCountry(),
		// DbQueryBuilder.STRICT);
		return builder;
	}

	/**
	 * Return Translation for given Locale.
	 *
	 * @param locale
	 */
	public String getValue(java.util.Locale locale) {
		String value = getCachedValue(locale);
		if (value != null) {
			// use cached value
			return value;
		} else {
			// TODO implement refresh from XML instance (currently going for
			// alternate translation)
			if (!(getObjectServer() instanceof XmlObjectServer)) {
				// refresh Translations
				Exception exception = null;
				DbTransaction trans = null;
				DbQuery query = null;
				try {
					DbQueryBuilder builder = createQueryBuilderSelect(getObjectServer(), getId(), locale);

					trans = getObjectServer().openTransactionThread(false);
					query = new DbQuery(trans, builder);
					java.sql.ResultSet queryResult = (java.sql.ResultSet) query.execute();
					while (getObjectServer().getMapper().hasNext(queryResult)) {
						cachedLocaleValues.put(createKey(locale), new Translation(DbState.SAVED, queryResult.getString(DbNlsString.ATTRIBUTE_NLS_TEXT)));
						// TODO evtl. multi response for same language in
						// different countries => now it will be overwritten
					}
					query.closeAll();
					query = null;
				} catch (Exception e) {
					exception = e;
					if (query != null) {
						try {
							query.closeAll();
						} catch (Exception ex) {
							Tracer.getInstance().runtimeError("ignored (follow-up error)", ex);
						}
					}
				}
				getObjectServer().closeTransactionThread(exception, trans);
				value = getCachedValue(locale);
			}

			if (value != null) {
				return value;
			} else {
				if ((cachedLocaleValues.values() == null) || (cachedLocaleValues.values().size() == 0)) {
					ch.softenvironment.util.Tracer.getInstance().runtimeWarning(
						"No NLS-Translation for id=" + getId() + " and Language=" + locale.getLanguage());
					return null;
				} else {
					// try other language
					ch.softenvironment.util.Tracer.getInstance().runtimeWarning(
						"Alternate NLS-Translation for id=" + getId() + " and Language=" + locale.getLanguage());
					return cachedLocaleValues.values().iterator().next().text;
				}
			}
		}
	}

	/**
	 * Return NLS-Text for locale without refreshing from database.
	 *
	 * @param locale
	 * @return
	 */
	private String getCachedValue(Locale locale) {
		if (cachedLocaleValues.containsKey(createKey(locale))) {
			return cachedLocaleValues.get(createKey(locale)).text;
		}
		return null;
	}

	/**
	 * @param locale
	 * @return
	 * @see #getAllValuesPerLanguage()
	 */
	private String createKey(Locale locale) {
		return locale.getLanguage();
		// TODO NYI: + ["_" + locale.getCountry()];
	}

	/**
	 * Return whether this Object belongs to a DbChangeableBean uniquely.
	 *
	 * @return boolean (false->property of DbEnumeration; true->property of DbChangeableBean)
	 */
	public boolean hasChangeableOwner() {
		return ownerChange != null;
	}

	/**
	 * @see #addChange()
	 */
	@Override
	public boolean isModified() {
		// no "change" => ReadOnly NlsString
		return hasChangeableOwner() && super.isModified();
	}

	/**
	 * @see #setChange()
	 */
	@Deprecated
	protected void removeChange(DbPropertyChange change) {
		this.ownerChange = null;
	}

	@Override
	public void save() {
		try {
			// TODO Tune: Use SQL Prepared Statements
			if (isModified()) {
				java.util.List<String> queries = new java.util.ArrayList<String>();
				Iterator<String> it = cachedLocaleValues.keySet().iterator();
				while (it.hasNext()) {
					String localeKey = it.next();
					Translation tr = cachedLocaleValues.get(localeKey);
					if ((tr.state == DbState.NEW) && ch.softenvironment.util.StringUtils.isNullOrEmpty(tr.text)) {
						// do not save empty translations
						continue;
					}

					if (getPersistencyState().isNew()) {
						if (queries.size() == 0) {
							// 1) INSERT T_NLS entry
							getObjectServer().setUniqueId(this, getObjectServer().getIncrementalId(getObjectServer().getMapper().getTargetNlsName()));
							DbQueryBuilder builder = getObjectServer().createQueryBuilder(DbQueryBuilder.INSERT, "DbNlsString");
							builder.setTableList(getObjectServer().getMapper().getTargetNlsName());
							builder.append(getObjectServer().getMapper().getTargetIdName(), getId());
							builder.append("Symbol", ownerChange.getDescription());
							builder.appendInternal((String) null);
							queries.add(builder.getQuery());
						}

						// 2) INSERT T_Translation entries
						queries.add(createQueryBuilderInsertTranslation(localeKey/*
						 * Language
						 * ONLY
						 * YET
						 */, null/*
						 * NYI
						 * :
						 * Country
						 */, tr.text).getQuery());
						tr.state = DbState.SAVED; // @see expception-handler
					} else if (getPersistencyState().isChanged()) {
						// UPDATE T_Translation entries
						if (tr.state == DbState.NEW /*
						 * emptiness checked above
						 * for state NEW
						 */) {
							queries.add(createQueryBuilderInsertTranslation(localeKey/*
							 * Language
							 * ONLY
							 * YET
							 */, null/*
							 * NYI
							 * :
							 * Country
							 */, tr.text).getQuery());
							tr.state = DbState.SAVED; // @see expception-handler
						} else if (tr.state == DbState.CHANGED) {
							if (StringUtils.isNullOrEmpty(tr.text)) {
								// => DELETE Translation
								DbQueryBuilder builder = getObjectServer().createQueryBuilder(DbQueryBuilder.DELETE, "DbNlsString-Translation");
								builder.setTableList(DbNlsString.class);
								builder.addFilter(ATTRIBUTE_TRANSLATION_ID, getId());
								builder.addFilter(ATTRIBUTE_LANGUAGE, localeKey, DbQueryBuilder.STRICT);
								// TODO builder.addFilter(ATTRIBUTE_COUNTRY,
								// localeKey, DbQueryBuilder.STRICT);
								queries.add(builder.getQuery());
								tr.state = DbState.REMOVED;
							} else {
								DbQueryBuilder builder = getObjectServer().createQueryBuilder(DbQueryBuilder.UPDATE, "DbNlsString-Translation");
								builder.setTableList(DbNlsString.class);
								builder.append(ATTRIBUTE_NLS_TEXT, tr.text);
								builder.appendInternal((String) null);
								builder.addFilter(ATTRIBUTE_TRANSLATION_ID, getId());
								builder.addFilter(ATTRIBUTE_LANGUAGE, localeKey, DbQueryBuilder.STRICT);
								// TODO builder.addFilter(ATTRIBUTE_COUNTRY,
								// country, DbQueryBuilder.STRICT);
								queries.add(builder.getQuery());
								tr.state = DbState.SAVED; // @see
								// expception-handler
							}
						}
					}
				}
				getObjectServer().execute("Save DbNlsString", queries);

				reset(true);
			}
		} catch (Exception e) {
			// TODO CRITICAL => tr.state is not reset, means potential data loss
			throw new ch.softenvironment.util.UserException(e.getLocalizedMessage(), ResourceManager.getResource(DbObjectServer.class, "CE_SaveError"), e);
		}
	}

	/**
	 * Create a query to INSERT a T_Translation record.
	 *
	 * @param language (for e.g. "de")
	 * @param country (for e.g. "CH")
	 * @param nlsText
	 * @return
	 */
	private DbQueryBuilder createQueryBuilderInsertTranslation(final String language, final String country, final String nlsText) {
		DbQueryBuilder builder = getObjectServer().createQueryBuilder(DbQueryBuilder.INSERT, "DbNlsString->Translation");
		builder.setTableList(DbNlsString.class);
		builder.append(ATTRIBUTE_TRANSLATION_ID, getId());
		builder.append(ATTRIBUTE_LANGUAGE, language);
		if (!ch.softenvironment.util.StringUtils.isNullOrEmpty(country)) {
			builder.append(ATTRIBUTE_COUNTRY, country);
		}
		builder.append(ATTRIBUTE_NLS_TEXT, nlsText);
		builder.appendInternal((String) null);

		return builder;
	}

	/**
	 * Set the owner of this DbNlsString in case of saving to inform the owner. A DbNlsString may only be owned by a single Owner!
	 *
	 * @param change (contains owner and property pointing to this DbNlsString)
	 * @see #removeChange()
	 */
	public void setChange(DbPropertyChange change) {
		if (change == null) {
			ownerChange = null;
		} else {
			if (change.getSource() instanceof DbSessionBean) {
				// SessionBeans cannot change and therefore not own a
				// DbNlsString
				return;
			}
			if (hasChangeableOwner()) {
				if (!(ownerChange.getSource().equals(change.getSource()) && ownerChange.getProperty().equals(change.getProperty()))) {
					throw new DeveloperException("DbNlsString (id=" + getId() + ") already assigned to an Onwer (cannot be moved)!");
				} // else reassignment is ok
			} else {
				ownerChange = change;
				if (change.getSource() instanceof DbEnumeration) {
					// do not allow change of Enumeration's
					getPersistencyState().setNext(DbState.READ_ONLY);
				}
			}
		}
	}

	/**
	 * Set the Translation for default Locale.
	 *
	 * @see #setValue(String, java.util.Locale)
	 */
	public void setValue(java.lang.String value) {
		setValue(value, java.util.Locale.getDefault());
	}

	/**
	 * Set the Translation for a specific Locale.
	 *
	 * @param value
	 * @param locale
	 */
	public void setValue(java.lang.String value, java.util.Locale locale) {
		String oldValue = getCachedValue(locale);
		if (value == null) {
			if (oldValue == null) {
				// no change
				return;
			}
		} else if (value.equals(oldValue)) {
			// no change
			return;
		}

		if (cachedLocaleValues.containsKey(createKey(locale))) {
			// existing translation
			cachedLocaleValues.put(createKey(locale), new Translation(getPersistencyState().isPersistent() ? DbState.CHANGED : DbState.NEW, value));
		} else {
			// new translation
			cachedLocaleValues.put(createKey(locale), new Translation(DbState.NEW, value));
		}
		firePropertyChange("value", oldValue, value);
		// historize("value");

		if (hasChangeableOwner()) {
			try {
				// let Composite know aggregate changed
				((DbChangeableBean) ownerChange.getSource()).firePropertyChange(ownerChange.getProperty(), null /* cheat */, this);
			} catch (Exception e) {
				ch.softenvironment.util.Tracer.getInstance().developerError(e.getLocalizedMessage());
			}
		}
	}

	public String getTranslation(final String key) {
		return cachedLocaleValues.get(key).text;
	}
}

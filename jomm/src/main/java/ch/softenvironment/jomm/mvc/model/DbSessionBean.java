package ch.softenvironment.jomm.mvc.model;

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

import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.implementation.DbPropertyChange;

/**
 * <b>SessionBean according to Enterprise Java Bean (EJB) Specification:</b>
 * <quote>Session beans represent non-persistent server-side components that may
 * be transaction aware. Typically, session beans model processes, services, and client-side sessions. As before, a session bean may be stateful or stateless. A stateful session bean maintains session
 * data for its client between operations. Thus, it is often used to model services such as shopping carts and bank tellers--services that usually require multiple steps or operations. Stateless
 * session beans are lightweight, scalable components that are used to model services that do not maintain session data for the client.</quote>
 * <p>
 * A SessionBean id is defined as follows: - is usually tightly adapted to a View (for e.g. SearchView). - represents mainly one EntityBean with joins of aggregated EntityBeans, like an SQL-View -
 * must not be updated -> use DbEntityBean instead
 *
 * @author Peter Hirzel
 */
public abstract class DbSessionBean extends DbObject {

	private DbChangeableBean eb = null;
	private transient java.util.Locale defaultLocale = null;

	/**
	 * Morph a given eb into a SessionBean. Copy all values making Sense in this case for efficiency reasons.
	 */
	protected DbSessionBean(DbChangeableBean eb) {
		this(eb.getObjectServer());
		this.eb = eb;

		eb.getObjectServer().setUniqueId(this, eb.getId());
		// setCreateDate(eb.getCreateDate());
		// setLastChange(eb.getLastChange());
		// setUserId(eb.getUserId());
	}

	/**
	 * @see DbObject(DbObjectServer)
	 */
	protected DbSessionBean(DbObjectServer objectServer) {
		super(objectServer);
	}

	/**
	 * Return whether clone was successful. Assuming parent and child of eb do not have same Property.
	 */
	private boolean cloneField(DbChangeableBean eb, DbDescriptor ebDescriptor, String property) throws NoSuchMethodException,
		java.lang.reflect.InvocationTargetException, IllegalAccessException {
		if (ebDescriptor.contains(property)) {
			// target
			DbPropertyChange ebChange = new DbPropertyChange(eb, property);
			java.lang.Class ebType = ebChange.getGetterReturnType();
			// source
			DbPropertyChange sbChange = new DbPropertyChange(this, property);
			java.lang.Class sbType = sbChange.getGetterReturnType();
			// copy source => target
			if (ebType.equals(sbType)) {
				ebChange.setProperty(sbChange.getValue());
				return true;
			}
		}
		return false;
	}

	/**
	 * Return List where entries are converted from SessionBean to EntityBean.
	 *
	 * @see #getEntityBean()
	 */
	public final static java.util.List<DbChangeableBean> convert(java.util.List<DbSessionBean> sessionBeans) throws Exception {
		java.util.List<DbChangeableBean> ebs = new java.util.ArrayList<DbChangeableBean>(sessionBeans.size());

		java.util.Iterator<DbSessionBean> iterator = sessionBeans.iterator();
		while (iterator.hasNext()) {
			ebs.add(iterator.next().getEntityBean());
		}
		return ebs;
	}

	/**
	 * Transform this SessionBean into its corresponding EntityBean. This method tries to set properties as far as known in the SessionBean itself for performance reasons.
	 * <p>
	 * No Database Read will be done for efficiency reasons! Therefore no refresh is guaranteed.
	 * <p>
	 * The cloning <b>from SessionBean-Properties to EntityBean-Properties does perform a shallow clone only</b>, be means Aggregations are reused as is and not copied separately. For SessionBeans
	 * this is a reasonable behavior, because they really mean the same Objects indeed.
	 *
	 * @see #getEntityBeanClass()
	 * @see #refresh(boolean)
	 */
	public final DbChangeableBean getEntityBean() throws Exception {
		if (eb == null) {
			eb = (DbChangeableBean) getObjectServer().createInstance(getEntityBeanClass(this.getClass()), true);
			getObjectServer().setUniqueId(eb, getId());

			java.util.Iterator<String> iterator = getObjectServer().getDescriptor(this.getClass()).iteratorProperties();
			while (iterator.hasNext()) {
				String property = iterator.next();
				DbDescriptor descriptor = eb.getObjectServer().getDescriptor(eb.getClass());
				while ((descriptor != null) && (!cloneField(eb, descriptor, property))) {
					// try parent as well
					descriptor = getObjectServer().getParentDescriptor(descriptor);
				}
			}

			eb.reset(false);
		}
		return eb;
	}

	/**
	 * Return the main class represented by this SessionBean.
	 *
	 * @see #getEntityBean()
	 * see DbConnection#getTableFor(Class)
	 */
	public static Class<? extends DbChangeableBean> getEntityBeanClass() {
		throw new ch.softenvironment.util.DeveloperException("Overwrite this method in each SessionBean-Specialization!");
	}

	/**
	 * Return the DbEntityBean class represented by this SessionBean.
	 *
	 * @see #getEntityBean()
	 * see DbConnection#getTableFor(Class)
	 */
	public static final Class getEntityBeanClass(java.lang.Class dbSessionBean) {
		try {
			Class[] parameterTypes = {};
			Object[] args = {};
			return (java.lang.Class) dbSessionBean.getMethod("getEntityBeanClass", parameterTypes).invoke(null /*
			 * no
			 * instance
			 * method
			 */, args);
		} catch (Throwable e) {
			throw new ch.softenvironment.util.DeveloperException("Class <" + dbSessionBean.getName() + "> not properly mapped", null, e);
		}
	}

	/**
	 * Since SessionBeans are provided for a specific session (as a throw away bean), its NLS mappings may be defined for that session resp. user language (for e.g. if SessionBean contains Date's or
	 * DbNlsString's).
	 *
	 * @param locale
	 */
	public final void setDefaultLocale(java.util.Locale locale) {
		defaultLocale = locale;
	}

	protected final java.util.Locale getDefaultLocale() {
		return defaultLocale;
	}
}

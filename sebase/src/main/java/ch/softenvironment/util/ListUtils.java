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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * Provides some often used features in using java.util.List.
 *
 * @author Peter Hirzel
 */
@Slf4j
public abstract class ListUtils {

	/**
	 * Comparator for sorting Object's by a defined property.
	 */
	private static class ObjectPropertyComparator<T> implements java.util.Comparator<T> {

		private final Evaluator evaluator;
		private final String property;
		private final java.text.Collator collator;

		/**
		 * @param property Attribute of Objects in list to sort by.
		 */
		protected ObjectPropertyComparator(Evaluator evaluator, final String property) {
			super();

			if (evaluator == null) {
				throw new IllegalArgumentException("evaluator must not be null");
			}
			if (property == null) {
				throw new IllegalArgumentException("property must not be null");
			}

			this.evaluator = evaluator;
			this.property = property;

			collator = java.text.Collator.getInstance(Locale.getDefault());
		}

		/**
		 * @param o1 ValueWrapper
		 * @param o1 ValueWrapper
		 * @see java.util.Comparator
		 */
		@Override
		public int compare(java.lang.Object o1, java.lang.Object o2) {
			try {
				if (o1 == null) {
					if (o2 == null) {
						return 0;
					} else {
						return -1; // make sure null-element comes first
					}
				} else if (o2 == null) {
					return 1;
				} else {
					Object v1 = evaluator.evaluate(o1, property);
					Object v2 = evaluator.evaluate(o2, property);
					if (v1 == null) {
						if (v2 == null) {
							return 0;
						} else {
							return -1; // make sure null-element comes first
						}
					} else if (v2 == null) {
						return 1;
					} else {
						if ((v1 instanceof String) && (v2 instanceof String)) {
							String s1 = (v1 == null ? "" : v1.toString());
							String s2 = (v1 == null ? "" : v2.toString());
							return collator.compare(s1, s2);
						} else {
							if ((v1 instanceof Long) && (v2 instanceof Long)) {
								return ((Long) v1).compareTo((Long) v2);
							}
							if ((v1 instanceof Integer) && (v2 instanceof Integer)) {
								return ((Integer) v1).compareTo((Integer) v2);
							} else if ((v1 instanceof Double) && (v2 instanceof Double)) {
								return ((Double) v1).compareTo((Double) v2);
							} else {
								// TODO compare might not be correct for any
								// other type
								return v1.toString().compareTo(v2.toString());
							}
						}
					}
				}
			} catch (Throwable e) {
				log.warn("sort failed", e);
				return 0; // as is
			}
		}
	}

	/**
	 * Return an ArrayList containing the given item.
	 *
	 * @return java.util.ArrayList
	 */
	public static <T> java.util.List<T> createList(T item) {
		java.util.List<T> list = new java.util.ArrayList<>(1);
		list.add(item);
		return list;
	}

	/**
	 * Sort the given items according to their objects property determined by evaluator.
	 *
	 * @param items list to sorted
	 * @param evaluator
	 * @param property
	 * @return cloned and sorted list
	 */
	public static <T> java.util.List<T> sort(java.util.List<T> items, Evaluator evaluator, final String property) {
		if ((items == null) || (items.size() == 0)) {
			return items;
		}
		if (StringUtils.isNullOrEmpty(property)) {
			throw new IllegalArgumentException("property must not be null");
		}

		// clone the list -> do not influence original items-list
		java.util.List<T> list = new ArrayList<>(items.size());
		Iterator<T> it = items.iterator();
		while (it.hasNext()) {
			list.add(it.next());
		}

		// sort list
		java.util.Collections.sort(list, new ObjectPropertyComparator(evaluator, property));
		return list;
	}

	/**
	 * Remove duplicate elements in given list according to equal property value of list-elements.
	 *
	 * @param list
	 * @param property
	 */
	public static <T> void eliminateDuplicates(java.util.List<T> list, final String property) throws Exception {
		Set<Object> set = new HashSet<Object>();
		Iterator<T> it = list.iterator();
		while (it.hasNext()) {
			Object value = (new BeanReflector<T>(it.next(), property)).getValue();
			if (set.contains(value)) {
				it.remove();
			} else {
				set.add(value);
			}
		}
	}

	/**
	 * Convert elements of the list to a String expression by given method, where elements are separated by given separator. <example> list.add(myObject); // which has a method
	 * #getMyListRepresentation() ListUtils.convertToString(list, "myListRepresentation", ';') </example>
	 *
	 * @param method non-argument method which must evaluate to String
	 * @param list
	 * @return
	 */
	public static <T> String convertToString(java.util.List<T> list, final String method, final char separator) {
		if ((list == null) || (list.size() == 0)) {
			return "";
		}
		if (StringUtils.isNullOrEmpty(method)) {
			throw new IllegalArgumentException("method must not be null");
		}

		StringBuffer contents = new StringBuffer();
		Iterator<T> it = list.iterator();
		while (it.hasNext()) {
			T element = it.next();
			/*
			 * if (method == null) { contents.append(element.toString()); } else
			 * {
			 */
			BeanReflector<T> reflector = new BeanReflector<>(element, method);
			try {
				contents.append(reflector.getValue());
			} catch (Throwable e) {
				log.warn("Developer warning: method buggy: " + element + "#" + method + "=>" + e.getLocalizedMessage());
				contents.append(element.toString());
			}
			// }
			contents.append(separator);
		}
		return contents.toString();
	}

	/**
	 * Build intersection between given sets.
	 *
	 * @param set0
	 * @param set1
	 * @return
	 */
	public static <T> java.util.Set<T> createIntersection(java.util.Set<T> set0, java.util.Set<T> set1) {
		if ((set0 == null) || (set1 == null)) {
			throw new IllegalArgumentException("given sets must not be null");
		}
		java.util.Set<T> intersection = new java.util.HashSet<>();
		Iterator<T> it = set0.iterator();
		while (it.hasNext()) {
			T element = it.next();
			if (set1.contains(element)) {
				intersection.add(element);
			}
		}
		return intersection;
	}
}

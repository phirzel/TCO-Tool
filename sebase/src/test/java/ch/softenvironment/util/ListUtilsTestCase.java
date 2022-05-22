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

import java.util.HashSet;
import java.util.Set;
import junit.framework.TestCase;

/**
 * TestCase for ListUtils.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class ListUtilsTestCase extends TestCase {

	private static final String PROPERTY = "Id";

	/**
	 * Used for BeanReflector test
	 */
	public static class ListObject {

		public ListObject(Long id) {
			this.id = id;
		}

		private final Long id;

		/**
		 * @return
		 * @see PROPERTY
		 */
		public Long getId() {
			return id;
		}

		public String getMyToString() {
			return "sample";
		}
	}

	public static class ListObjectEvaluator implements Evaluator {

		@Override
		public Object evaluate(Object owner, String property) {
			try {
				return (new BeanReflector<ListObject>((ListObject) owner, property)).getValue();
			} catch (Exception ex) {
				fail(ex.getMessage());
				return null;
			}
		}

	}

	public void testCreateList() {
		java.util.List<String> list = ListUtils.createList("hello");
		assertTrue(list.get(0).equals("hello"));
	}

	public void testConvertToString() {
		ListObject obj = new ListObject(null);
		java.util.List<ListObject> list = new java.util.ArrayList<ListObject>();
		list.add(obj);
		assertTrue("sample;".equals(ListUtils.convertToString(list, "myToString", ';')));
	}

	public void testCreateIntersection() {
		Set<Long> set0 = new HashSet<Long>(4);
		set0.add(Long.valueOf(12));
		set0.add(Long.valueOf(15));
		set0.add(Long.valueOf(-101));
		set0.add(Long.valueOf(45));
		Set<Long> set1 = new HashSet<Long>(4);
		set1.add(Long.valueOf(121));
		set1.add(Long.valueOf(15));
		set1.add(Long.valueOf(-102));
		set1.add(Long.valueOf(45));
		Set<Long> result = ListUtils.createIntersection(set0, set1);
		assertTrue("same type of elements", result.size() == 2);
		assertTrue(result.contains(Long.valueOf(15)));
		assertTrue(result.contains(Long.valueOf(45)));
		assertFalse(result.contains(Long.valueOf(12)));
		assertFalse(result.contains(Long.valueOf(-102)));
	}

	public void testEliminateDuplicates() {
		try {
			java.util.List<ListObject> list = new java.util.ArrayList<ListObject>();
			ListUtils.eliminateDuplicates(list, PROPERTY);
			assertTrue("empty", list.size() == 0);

			ListObject lo = new ListObject(Long.valueOf(12));
			list.add(lo);
			ListUtils.eliminateDuplicates(list, PROPERTY);
			assertTrue("no elimination", list.size() == 1);

			lo = new ListObject(Long.valueOf(13));
			list.add(lo);
			ListUtils.eliminateDuplicates(list, PROPERTY);
			assertTrue("no elimination", list.size() == 2);

			list.add(lo);
			ListUtils.eliminateDuplicates(list, PROPERTY);
			assertTrue("real elimination", list.size() == 2);
		} catch (Throwable ex) {
			fail(ex.getLocalizedMessage());
		}
	}

	public void testSort() {
		java.util.List<ListObject> items = new java.util.ArrayList<ListObject>();
		Evaluator evaluator = new ListObjectEvaluator();
		java.util.List<ListObject> sorted = ListUtils.sort(items, evaluator, PROPERTY);
		assertTrue("nothing to sort", items.size() == sorted.size());

		items.add(new ListObject(Long.valueOf(23)));
		sorted = ListUtils.sort(items, evaluator, PROPERTY);
		assertTrue("nothing to sort", items.size() == sorted.size());

		items.add(new ListObject(Long.valueOf(12)));
		assertTrue(items.get(0).getId().longValue() == 23);
		assertTrue(items.get(1).getId().longValue() == 12);
		sorted = ListUtils.sort(items, evaluator, PROPERTY);
		assertTrue(items.get(0).getId().longValue() == 23);
		assertTrue(items.get(1).getId().longValue() == 12);
		assertTrue(sorted.get(0).getId().longValue() == 12);
		assertTrue(sorted.get(1).getId().longValue() == 23);
		assertTrue("after sorting", items.size() == sorted.size());
	}
}

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

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author Peter Hirzel
 */
public class ListUtilsTest {

	private static final String PROPERTY = "Id";

	/**
	 * Used for BeanReflector test
	 */
	public static class ListObject {

		public ListObject(Long id) {
			this.id = id;
		}

		private final Long id;

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
				return (new BeanReflector<>((ListObject) owner, property)).getValue();
			} catch (Exception ex) {
				fail(ex.getMessage());
				return null;
			}
		}

	}

	@Test
	@Deprecated(since = "1.6.0")
	public void createList() {
		java.util.List<String> list = ListUtils.createList("hello");
		assertEquals("hello", list.get(0));
	}

	@Test
	public void convertToString() {
		ListObject obj = new ListObject(null);
		java.util.List<ListObject> list = new java.util.ArrayList<>();
		list.add(obj);
		assertEquals("sample;", ListUtils.convertToString(list, "myToString", ';'));
	}

	@Test
	public void createIntersection() {
		Set<Long> set0 = new HashSet<>(4);
		set0.add(12L);
		set0.add(15L);
		set0.add(-101L);
		set0.add(45L);
		Set<Long> set1 = new HashSet<>(4);
		set1.add(121L);
		set1.add(15L);
		set1.add(-102L);
		set1.add(45L);
		Set<Long> result = ListUtils.createIntersection(set0, set1);
		assertEquals("same type of elements", 2, result.size());
		assertTrue(result.contains(15L));
		assertTrue(result.contains(45L));
		assertFalse(result.contains(12L));
		assertFalse(result.contains(-102L));
	}

	@Test
	public void eliminateDuplicates() {
		try {
			java.util.List<ListObject> list = new java.util.ArrayList<>();
			ListUtils.eliminateDuplicates(list, PROPERTY);
			assertEquals("empty", 0, list.size());

			ListObject lo = new ListObject(12L);
			list.add(lo);
			ListUtils.eliminateDuplicates(list, PROPERTY);
			assertEquals("no elimination", 1, list.size());

			lo = new ListObject(13L);
			list.add(lo);
			ListUtils.eliminateDuplicates(list, PROPERTY);
			assertEquals("no elimination", 2, list.size());

			list.add(lo);
			ListUtils.eliminateDuplicates(list, PROPERTY);
			assertEquals("real elimination", 2, list.size());
		} catch (Throwable ex) {
			fail(ex.getLocalizedMessage());
		}
	}

	@Test
	public void sort() {
		java.util.List<ListObject> items = new java.util.ArrayList<>();
		Evaluator evaluator = new ListObjectEvaluator();
		java.util.List<ListObject> sorted = ListUtils.sort(items, evaluator, PROPERTY);
		assertEquals("nothing to sort", items.size(), sorted.size());

		items.add(new ListObject(23L));
		sorted = ListUtils.sort(items, evaluator, PROPERTY);
		assertEquals("nothing to sort", items.size(), sorted.size());

		items.add(new ListObject(12L));
		assertEquals(23, items.get(0).getId().longValue());
		assertEquals(12, items.get(1).getId().longValue());
		sorted = ListUtils.sort(items, evaluator, PROPERTY);
		assertEquals(23, items.get(0).getId().longValue());
		assertEquals(12, items.get(1).getId().longValue());
		assertEquals(12, sorted.get(0).getId().longValue());
		assertEquals(23, sorted.get(1).getId().longValue());
		assertEquals("after sorting", items.size(), sorted.size());
	}
}

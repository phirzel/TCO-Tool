package ch.softenvironment.controller;

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
import java.util.List;
import junit.framework.TestCase;

/**
 * TestCase for DataBrowser.
 *
 * @author Peter Hirzel
 */
public class DataBrowserTestCase extends TestCase {

    public void testEmptyBrowsing() {
        DataBrowser<Object> browser = new DataBrowser<>();
        assertFalse(browser.isScrollNextAllowed());
        assertFalse(browser.isScrollPreviousAllowed());
        assertFalse(browser.isScrollLastAllowed());
        assertFalse(browser.isScrollFirstAllowed());
        assertTrue(browser.getCurrentObject() == null);
        assertTrue(browser.getCurrentIndex() == -1);
        // though not expected
        assertTrue(browser.getNext() == null);
        assertTrue(browser.getPrevious() == null);
        assertTrue(browser.getFirst() == null);
        assertTrue(browser.getLast() == null);

        browser.setObjects(new ArrayList<>());
        assertFalse(browser.isScrollNextAllowed());
        assertFalse(browser.isScrollPreviousAllowed());
        assertFalse(browser.isScrollLastAllowed());
        assertFalse(browser.isScrollFirstAllowed());
        assertTrue(browser.getCurrentObject() == null);
        assertTrue(browser.getCurrentIndex() == -1);
        // though not expected
        assertTrue(browser.getNext() == null);
        assertTrue(browser.getPrevious() == null);
        assertTrue(browser.getFirst() == null);
        assertTrue(browser.getLast() == null);
    }

    public void testSingleBrowsing() {
        DataBrowser<String> browser = new DataBrowser<>();
        List<String> list = new ArrayList<>();
        list.add("Hello");
        browser.setObjects(list);
        assertFalse(browser.isScrollNextAllowed());
        assertFalse(browser.isScrollPreviousAllowed());
        assertFalse(browser.isScrollLastAllowed());
        assertFalse(browser.isScrollFirstAllowed());

        assertTrue("Hello".equals(browser.getCurrentObject()));
        assertTrue(0 == browser.getCurrentIndex());
        assertTrue("1/1".equals(browser.getScrollIndexString()));

        // though not expected
        assertTrue(browser.getNext().equals("Hello"));
        assertTrue(browser.getPrevious().equals("Hello"));
        assertTrue(browser.getFirst().equals("Hello"));
        assertTrue(browser.getLast().equals("Hello"));
    }

    public void testMultiBrowsing() {
        DataBrowser<String> browser = new DataBrowser<>();
        List<String> list = new ArrayList<>();
        list.add("Hello");
        list.add("World");
        list.add("today");
        browser.setObjects(list);
        assertTrue(browser.isScrollNextAllowed());
        assertFalse(browser.isScrollPreviousAllowed());
        assertTrue(browser.isScrollLastAllowed());
        assertFalse(browser.isScrollFirstAllowed());

        assertTrue("Hello".equals(browser.getCurrentObject()));
        assertTrue(0 == browser.getCurrentIndex());
        assertTrue("1/3".equals(browser.getScrollIndexString()));

        assertTrue("World".equals(browser.getNext()));
        assertTrue(1 == browser.getCurrentIndex());
        assertTrue(browser.isScrollNextAllowed());
        assertTrue(browser.isScrollPreviousAllowed());
        assertTrue(browser.isScrollLastAllowed());
        assertTrue(browser.isScrollFirstAllowed());
        assertTrue("2/3".equals(browser.getScrollIndexString()));

        assertTrue("today".equals(browser.getNext()));
        assertTrue(2 == browser.getCurrentIndex());
        assertFalse(browser.isScrollNextAllowed());
        assertTrue(browser.isScrollPreviousAllowed());
        assertFalse(browser.isScrollLastAllowed());
        assertTrue(browser.isScrollFirstAllowed());
        assertTrue("3/3".equals(browser.getScrollIndexString()));

        assertTrue("World".equals(browser.getPrevious()));
        assertTrue(1 == browser.getCurrentIndex());
        assertTrue(browser.isScrollNextAllowed());
        assertTrue(browser.isScrollPreviousAllowed());
        assertTrue(browser.isScrollLastAllowed());
        assertTrue(browser.isScrollFirstAllowed());

        assertTrue("Hello".equals(browser.getFirst()));
        assertTrue(0 == browser.getCurrentIndex());
        assertTrue(browser.isScrollNextAllowed());
        assertFalse(browser.isScrollPreviousAllowed());
        assertTrue(browser.isScrollLastAllowed());
        assertFalse(browser.isScrollFirstAllowed());

        assertTrue("today".equals(browser.getLast()));
        assertTrue(2 == browser.getCurrentIndex());
        assertFalse(browser.isScrollNextAllowed());
        assertTrue(browser.isScrollPreviousAllowed());
        assertFalse(browser.isScrollLastAllowed());
        assertTrue(browser.isScrollFirstAllowed());

        assertTrue("World".equals(browser.getPrevious()));
        assertTrue(1 == browser.getCurrentIndex());
        assertTrue(browser.isScrollNextAllowed());
        assertTrue(browser.isScrollPreviousAllowed());
        assertTrue(browser.isScrollLastAllowed());
        assertTrue(browser.isScrollFirstAllowed());
    }

    public void testStepEven() {
        DataBrowser<Integer> browser = new DataBrowser<>();
        browser.setStep(10);
        assertTrue(browser.getCurrentIndex() == -1);
        assertTrue(browser.getCurrentObject() == null);

        List<Integer> objects = new java.util.ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            objects.add(Integer.valueOf(i));
        }
        browser.setObjects(objects);
        browser.setCurrentIndex(0);
        browser.getLast();
        assertTrue("step is one behind last => correction", browser.getCurrentIndex() == 19);
        browser.getPrevious();
        assertTrue(browser.getCurrentIndex() == 9);
        browser.getPrevious();
        assertTrue("no previous", browser.getCurrentIndex() == 0);
        browser.getFirst();
        assertTrue(browser.getCurrentIndex() == 0);

        for (int i = 20; i < 40; i++) {
            // add another 20 objects
            objects.add(i);
        }
        browser.getFirst();
        assertTrue(browser.getCurrentIndex() == 0);
        browser.getNext();
        assertTrue(browser.getCurrentIndex() == 10);
        browser.getNext();
        assertTrue(browser.getCurrentIndex() == 20);
        browser.getNext();
        assertTrue(browser.getCurrentIndex() == 30);
        browser.getNext();
        assertTrue("no next step possible", browser.getCurrentIndex() == 30);
        browser.getLast();
        assertTrue("step is one behind last => correction", browser.getCurrentIndex() == 39);

        objects = new java.util.ArrayList<>();
        for (int i = 1; i < 106; i++) {
            objects.add(i);
        }
        browser.setObjects(objects);
        assertTrue(browser.isScrollNextAllowed());
        assertFalse(browser.isScrollPreviousAllowed());
        assertTrue(browser.isScrollLastAllowed());
        assertFalse(browser.isScrollFirstAllowed());
        assertTrue(browser.getCurrentIndex() == 0);
        assertTrue(browser.getCurrentObject().equals(1));

        assertTrue(browser.getNext().equals(11));
        assertTrue(browser.isScrollNextAllowed());
        assertTrue(browser.isScrollPreviousAllowed());
        assertTrue(browser.isScrollLastAllowed());
        assertTrue(browser.isScrollFirstAllowed());
        assertTrue(browser.getCurrentIndex() == 10);
        assertTrue(browser.getCurrentObject().equals(11));

        assertTrue(browser.getPrevious().equals(1));
        assertTrue(browser.isScrollNextAllowed());
        assertFalse(browser.isScrollPreviousAllowed());
        assertTrue(browser.isScrollLastAllowed());
        assertFalse(browser.isScrollFirstAllowed());
        assertTrue(browser.getCurrentIndex() == 0);
        assertTrue(browser.getCurrentObject().equals(1));

        assertTrue(browser.getNext().equals(11));
        assertTrue(browser.getCurrentIndex() == 10);
        assertTrue(browser.getNext().equals(21));
        assertTrue(browser.getCurrentIndex() == 20);
        assertTrue(browser.getNext().equals(Integer.valueOf(31)));
        assertTrue(browser.getCurrentIndex() == 30);
        assertTrue(browser.getPrevious().equals(Integer.valueOf(21)));
        assertTrue(browser.getCurrentIndex() == 20);

        assertTrue("beginning of last block", browser.getLast().equals(Integer.valueOf(101)));
        assertTrue(browser.getCurrentIndex() == 100);
        assertFalse("though index not on last element, there is no next block", browser.isScrollNextAllowed());
        assertFalse("no next block", browser.isScrollLastAllowed());

        assertTrue("first block", browser.getFirst().equals(Integer.valueOf(1)));
        assertTrue(browser.getCurrentIndex() == 0);
    }

    public void testStepOdd5() {
        DataBrowser<Integer> browser = new DataBrowser<Integer>();
        browser.setStep(5);
        assertTrue(browser.getCurrentIndex() == -1);
        assertTrue(browser.getCurrentObject() == null);

        List<Integer> objects = new java.util.ArrayList<Integer>(10);
        for (int i = 0; i < 10; i++) {
            objects.add(Integer.valueOf(i));
        }
        browser.setObjects(objects);
        browser.setCurrentIndex(0);
        browser.getLast();
        assertTrue("step is one behind last => correction", browser.getCurrentIndex() == 9);
        for (int i = 10; i < 20; i++) {
            // add another 10 objects
            objects.add(Integer.valueOf(i));
        }
        browser.getFirst();
        assertTrue(browser.getCurrentIndex() == 0);
        browser.getNext();
        assertTrue(browser.getCurrentIndex() == 5);
        browser.getNext();
        assertTrue(browser.getCurrentIndex() == 10);
        browser.getNext();
        assertTrue(browser.getCurrentIndex() == 15);
        browser.getNext();
        assertTrue("no next step possible", browser.getCurrentIndex() == 15);
        browser.getLast();
        assertTrue("step is one behind last => correction", browser.getCurrentIndex() == 19);
    }

    public void testStepOdd3() {
        DataBrowser<Integer> browser = new DataBrowser<Integer>();
        browser.setStep(3);
        assertTrue(browser.getCurrentIndex() == -1);
        assertTrue(browser.getCurrentObject() == null);

        java.util.List<Integer> objects = new java.util.ArrayList<Integer>();
        for (int i = 1; i < 101; i++) {
            objects.add(Integer.valueOf(i));
        }
        browser.setObjects(objects);
        assertTrue(browser.isScrollNextAllowed());
        assertFalse(browser.isScrollPreviousAllowed());
        assertTrue(browser.isScrollLastAllowed());
        assertFalse(browser.isScrollFirstAllowed());
        assertTrue(browser.getCurrentIndex() == 0);
        assertTrue(browser.getCurrentObject().equals(Integer.valueOf(1)));

        assertTrue(browser.getNext().equals(Integer.valueOf(4)));
        assertTrue(browser.isScrollNextAllowed());
        assertTrue(browser.isScrollPreviousAllowed());
        assertTrue(browser.isScrollLastAllowed());
        assertTrue(browser.isScrollFirstAllowed());
        assertTrue(browser.getCurrentIndex() == 3);
        assertTrue(browser.getCurrentObject().equals(Integer.valueOf(4)));

        assertTrue(browser.getPrevious().equals(Integer.valueOf(1)));
        assertTrue(browser.isScrollNextAllowed());
        assertFalse(browser.isScrollPreviousAllowed());
        assertTrue(browser.isScrollLastAllowed());
        assertFalse(browser.isScrollFirstAllowed());
        assertTrue(browser.getCurrentIndex() == 0);
        assertTrue(browser.getCurrentObject().equals(Integer.valueOf(1)));

        assertTrue(browser.getNext().equals(Integer.valueOf(4)));
        assertTrue(browser.getCurrentIndex() == 3);
        assertTrue(browser.getNext().equals(Integer.valueOf(7)));
        assertTrue(browser.getCurrentIndex() == 6);
        assertTrue(browser.getNext().equals(Integer.valueOf(10)));
        assertTrue(browser.getCurrentIndex() == 9);
        assertTrue(browser.getPrevious().equals(Integer.valueOf(7)));
        assertTrue(browser.getCurrentIndex() == 6);

        assertTrue("beginning of last block", browser.getLast().equals(Integer.valueOf(99)));
        assertTrue(browser.getCurrentIndex() == 98);
        assertFalse("though index not on last element, there is no next block", browser.isScrollNextAllowed());
        assertFalse("no next block", browser.isScrollLastAllowed());

        assertTrue("first block", browser.getFirst().equals(Integer.valueOf(1)));
        assertTrue(browser.getCurrentIndex() == 0);
    }

    public void testSetCurrentIndex() {
        DataBrowser<Integer> browser = new DataBrowser<Integer>();
        browser.setStep(10);
        assertTrue(browser.getCurrentIndex() == -1);
        assertTrue(browser.getCurrentObject() == null);

        java.util.List<Integer> objects = new java.util.ArrayList<Integer>(100);
        for (int i = 1; i < 106; i++) {
            objects.add(Integer.valueOf(i));
        }
        browser.setObjects(objects);

        browser.setCurrentIndex(89);
        assertTrue(browser.getCurrentObject().equals(Integer.valueOf(90)));

        //TODO what happens at #getNext/Previous() if step>1 (stay in step or next/previous from currentIndex?)
    }
}

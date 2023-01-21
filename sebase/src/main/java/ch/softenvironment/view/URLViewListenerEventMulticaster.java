package ch.softenvironment.view;

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
 * This is the event multicaster class to support the ch.softenvironment.view.URLViewListenerEventMulticaster interface.
 *
 * @author Peter Hirzel
 * @since 1.2 (2004-02-05) $ $Date: 2004-02-05 11:33:00 $
 */
public class URLViewListenerEventMulticaster extends java.awt.AWTEventMulticaster implements ch.softenvironment.view.URLViewListener {

	/**
	 * Constructor to support multicast events.
	 *
	 * @param a java.util.EventListener
	 * @param b java.util.EventListener
	 */
	protected URLViewListenerEventMulticaster(java.util.EventListener a, java.util.EventListener b) {
		super(a, b);
	}

	/**
	 * Add new listener to support multicast events.
	 *
	 * @param a ch.softenvironment.view.URLViewListener
	 * @param b ch.softenvironment.view.URLViewListener
	 * @return ch.softenvironment.view.URLViewListener
	 */
	public static ch.softenvironment.view.URLViewListener add(ch.softenvironment.view.URLViewListener a, ch.softenvironment.view.URLViewListener b) {
		return (ch.softenvironment.view.URLViewListener) addInternal(a, b);
	}

	/**
	 * Add new listener to support multicast events.
	 *
	 * @param a java.util.EventListener
	 * @param b java.util.EventListener
	 * @return java.util.EventListener
	 */
	protected static java.util.EventListener addInternal(java.util.EventListener a, java.util.EventListener b) {
		if (a == null) {
			return b;
		}
		if (b == null) {
			return a;
		}
		return new URLViewListenerEventMulticaster(a, b);
	}

	/**
	 * @param oldl ch.softenvironment.view.URLViewListener
	 * @return java.util.EventListener
	 */
	protected java.util.EventListener remove(ch.softenvironment.view.URLViewListener oldl) {
		if (oldl == a) {
			return b;
		}
		if (oldl == b) {
			return a;
		}
		java.util.EventListener a2 = removeInternal(a, oldl);
		java.util.EventListener b2 = removeInternal(b, oldl);
		if (a2 == a && b2 == b) {
			return this;
		}
		return addInternal(a2, b2);
	}

	/**
	 * Remove listener to support multicast events.
	 *
	 * @param l ch.softenvironment.view.URLViewListener
	 * @param oldl ch.softenvironment.view.URLViewListener
	 * @return ch.softenvironment.view.URLViewListener
	 */
	public static ch.softenvironment.view.URLViewListener remove(ch.softenvironment.view.URLViewListener l, ch.softenvironment.view.URLViewListener oldl) {
		if (l == oldl || l == null) {
			return null;
		}
		if (l instanceof URLViewListenerEventMulticaster) {
			return (ch.softenvironment.view.URLViewListener) ((ch.softenvironment.view.URLViewListenerEventMulticaster) l).remove(oldl);
		}
		return l;
	}

	/**
	 * @param newEvent java.util.EventObject
	 */
	@Override
	public void urlKeyReleased(java.util.EventObject newEvent) {
		((ch.softenvironment.view.URLViewListener) a).urlKeyReleased(newEvent);
		((ch.softenvironment.view.URLViewListener) b).urlKeyReleased(newEvent);
	}
}

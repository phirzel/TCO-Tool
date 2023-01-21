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
 * This is the event multicaster class to support the ch.softenvironment.view.FileNamePanelListenerEventMulticaster interface.
 *
 * @author Peter Hirzel
 * @since 1.2 (2004-02-05) $ $Date: 2004-02-05 11:32:59 $
 */
public class FileNamePanelListenerEventMulticaster extends java.awt.AWTEventMulticaster implements ch.softenvironment.view.FileNamePanelListener {

	/**
	 * Constructor to support multicast events.
	 *
	 * @param a java.util.EventListener
	 * @param b java.util.EventListener
	 */
	protected FileNamePanelListenerEventMulticaster(java.util.EventListener a, java.util.EventListener b) {
		super(a, b);
	}

	/**
	 * Add new listener to support multicast events.
	 *
	 * @param a ch.softenvironment.view.FileNamePanelListener
	 * @param b ch.softenvironment.view.FileNamePanelListener
	 * @return ch.softenvironment.view.FileNamePanelListener
	 */
	public static ch.softenvironment.view.FileNamePanelListener add(ch.softenvironment.view.FileNamePanelListener a, ch.softenvironment.view.FileNamePanelListener b) {
		return (ch.softenvironment.view.FileNamePanelListener) addInternal(a, b);
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
		return new FileNamePanelListenerEventMulticaster(a, b);
	}

	/**
	 * @param oldl ch.softenvironment.view.FileNamePanelListener
	 * @return java.util.EventListener
	 */
	protected java.util.EventListener remove(ch.softenvironment.view.FileNamePanelListener oldl) {
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
	 * @param l ch.softenvironment.view.FileNamePanelListener
	 * @param oldl ch.softenvironment.view.FileNamePanelListener
	 * @return ch.softenvironment.view.FileNamePanelListener
	 */
	public static ch.softenvironment.view.FileNamePanelListener remove(ch.softenvironment.view.FileNamePanelListener l, ch.softenvironment.view.FileNamePanelListener oldl) {
		if (l == oldl || l == null) {
			return null;
		}
		if (l instanceof FileNamePanelListenerEventMulticaster) {
			return (ch.softenvironment.view.FileNamePanelListener) ((ch.softenvironment.view.FileNamePanelListenerEventMulticaster) l).remove(oldl);
		}
		return l;
	}

	/**
	 * @param newEvent java.util.EventObject
	 */
	@Override
	public void textKeyReleased(java.util.EventObject newEvent) {
		((ch.softenvironment.view.FileNamePanelListener) a).textKeyReleased(newEvent);
		((ch.softenvironment.view.FileNamePanelListener) b).textKeyReleased(newEvent);
	}
}

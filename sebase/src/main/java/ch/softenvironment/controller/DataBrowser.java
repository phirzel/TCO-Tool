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

import ch.softenvironment.util.UserException;
import java.util.HashSet;
import java.util.Iterator;
import lombok.extern.slf4j.Slf4j;

/**
 * Browser to walk through a List of Objects by '<<' '<' '>' '>>'. Step of walksize might be configured.
 *
 * @author Peter Hirzel
 * @see ch.softenvironment.view.DataSelectorPanel for NLS-properties
 */
@Slf4j
public class DataBrowser<T> {

	private java.util.List<T> objects = null;
	private int currentIndex = -1;
	private int step = 1; // default
	private java.util.Set<DataBrowserListener<T>> listeners = null;

	/**
	 * Create a Browser with an empty list.
	 */
	public DataBrowser() {
		super();
		objects = new java.util.ArrayList<>();
		listeners = new HashSet<>();
	}

	/**
	 * Register a DataBrowserListener.
	 *
	 * @param listener
	 */
	public final void addListener(DataBrowserListener<T> listener) {
		this.listeners.add(listener);
	}

	/**
	 * Remove a registered DataBrowserListener.
	 *
	 * @param listener
	 */
	public final void removeListener(DataBrowserListener<T> listener) {
		if (this.listeners.contains(listener)) {
			this.listeners.remove(listener);
		} else {
			log.warn("Developer warning: trying to remove a non-existant listener: {}", listener);
		}
	}

	/**
	 * Return the first of all objects.
	 */
	public synchronized T getFirst() {
		if (getObjects().size() > 0) {
			// @see #setObjects(java.util.List)
			if (currentIndex != 0) {
				setCurrentIndex(0);
			}
		}
		return getCurrentObject();
	}

	/**
	 * Set previous Object as currentObject.
	 */
	public synchronized Object getPrevious() {
		if (currentIndex > 0) {
			if ((currentIndex - step) < 0) {
				setCurrentIndex(0);
			} else {
				setCurrentIndex(currentIndex - step);
			}
		}
		return getCurrentObject();
	}

	/**
	 * Set next Object as currentObject.
	 */
	public synchronized T getNext() {
		if ((objects.size() > 0) && ((currentIndex + step) < objects.size())) {
			setCurrentIndex(currentIndex + step);
		}
		return getCurrentObject();
	}

	/**
	 * Return the last block of all Objects. If (step==1) then "show very last element" else "show last block of elements
	 */
	public synchronized T getLast() {
		if (objects.size() > 1) {
			int slices = objects.size() / getStep();
			int index = getStep() * slices;
			if (getStep() % 2 != 0) {
				// correct uneven Step
				index--;
			}
			if (index >= objects.size()) {
				// for e.g. 20 objects with step 10 index==0; getLast()
				// positions on 20th instead of 19th
				index = objects.size() - 1;
			}
			setCurrentIndex(index);
		}
		return getCurrentObject();
	}

	public final java.util.List<T> getObjects() {
		return objects;
	}

	/**
	 * Set the list of Objects to treat by Selector and setCurrentObject to the first object in list.
	 *
	 * @param objects A list of Object's
	 */
	public synchronized final void setObjects(java.util.List<T> objects) {
		if (objects == null) {
			throw new IllegalArgumentException("objects must not be empty");
		}

		this.objects = objects;
		currentIndex = -1; // invalidate
		getFirst();
	}

	/**
	 * Inform listeners that current object has changed.
	 *
	 * @see DataBrowserListener#setCurrentObject(Object)
	 */
	private void signalCurrentObject() {
		Iterator<DataBrowserListener<T>> it = listeners.iterator();
		while (it.hasNext()) {
			it.next().setCurrentObject(getCurrentObject());
		}
	}

	/**
	 * Gets the currentObject property (java.lang.Object) value.
	 *
	 * @return The currentObject property value.
	 */
	public final T getCurrentObject() {
		if (currentIndex > -1) {
			return objects.get(currentIndex); // fieldCurrentObject;
		} else {
			return null;
		}
	}

	/**
	 * Add a new Object to list at the very end and move setCurrentIndex() to it.
	 */
	public synchronized void addObject(T object) {
		objects.add(object);
		getLast();
	}

	/**
	 * Remove the currentObject from list.
	 *
	 * @see DataBrowserListener#removeObject(Object)
	 */
	public void removeCurrentObject() {
		if (getCurrentObject() != null) {
			Iterator<DataBrowserListener<T>> it = listeners.iterator();
			while (it.hasNext()) {
				// inform listeners
				if (it.next().removeObject(getCurrentObject())) {
					throw new UserException("Veto was intervened by one of the listeners.", "Remove not possible");
				}
			}

			// remove from current list only
			objects.remove(getCurrentObject());

			// update browser
			if (currentIndex > 0) {
				// step back to very previous
				currentIndex--; // do not call #setCurrentIndex() to avoid
				// #saveChanges()
			} else {
				// very first and only Object was deleted in list
				if (objects.size() == 0) {
					currentIndex = -1; // do not call #setCurrentIndex() to
					// avoid #saveChanges()
				} // else keep index, though object is different
			}
			signalCurrentObject();
		}
	}

	/**
	 * Allow Listener to save any changes.
	 */
	private void saveChanges() {
		if (getCurrentObject() != null) {
			Iterator<DataBrowserListener<T>> it = listeners.iterator();
			while (it.hasNext()) {
				objects.set(currentIndex, it.next().saveChanges(getCurrentObject()));
			}
		}
	}

	/**
	 * Enable Scroll-Buttons "<<".
	 */
	public final boolean isScrollFirstAllowed() {
		return isScrollPreviousAllowed();
	}

	/**
	 * Enable Scroll-Buttons "<".
	 */
	public final boolean isScrollPreviousAllowed() {
		return currentIndex > 0;
	}

	/**
	 * Enable Scroll-Buttons ">".
	 */
	public final boolean isScrollNextAllowed() {
		return ((objects.size() > 0) && (currentIndex + getStep()) < objects.size());
	}

	/**
	 * Enable Scroll-Buttons ">>".
	 */
	public final boolean isScrollLastAllowed() {
		return isScrollNextAllowed();
	}

	/**
	 * Enable Remove-Button "<<".
	 */
	public boolean isRemoveAllowed() {
		return (getCurrentObject() != null);
	}

	/**
	 * Show current-index of list-count. For e.g. "3/5" means 3rd of 5 object is the current one.
	 *
	 * @return
	 */
	public final String getScrollIndexString() {
		try {
			if (objects.size() > 0) {
				return (getCurrentIndex() + 1) + "/" + objects.size();
			} else {
				return "0/0";
			}
		} catch (Throwable e) {
			// should not happen
			log.warn("Developer warning", e);
			return " ";
		}
	}

	/**
	 * Return the currentObject's index in ObjectList. This index starts at 0 for the very first Element.
	 */
	public final int getCurrentIndex() {
		return currentIndex;
	}

	/**
	 * Set the index of current object in the given browsing list absolute to this position. (Independent of step-size!)
	 *
	 * @param index
	 */
	public synchronized void setCurrentIndex(int index) {
		if ((index < 0) || (index > objects.size() - 1)) {
			throw new IndexOutOfBoundsException("desired index=" + index + " of max. " + objects.size());
		}
		if (currentIndex != index) {
			saveChanges();
			currentIndex = index;
			signalCurrentObject();
		}
	}

	/**
	 * Set the size of the step for #getNext() or #getPrevious()
	 * <p>
	 * Default is 1.
	 *
	 * @param step >=1
	 */
	public synchronized void setStep(int step) {
		if (step < 1) {
			log.warn("Auto-correction: step must be >= 1!");
			this.step = 1;
		}
		this.step = step;
	}

	public final int getStep() {
		return step;
	}
}

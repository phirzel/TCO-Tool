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
 * Manage a Set of GUI-Options which are valid for all Views in a (Client)-Application. An instance of the ViewOptions should be initialized as Singleton by an Application-Launcher and passed to any
 * View opened from there.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 * @see DetailView(..)
 * @see SearchView(..)
 */
public class ViewOptions {

    private boolean closeOnSave = true;
    private final java.util.Map<String, Boolean> options = new java.util.HashMap<String, Boolean>();
    private final ch.softenvironment.client.ViewManager viewManager = new ch.softenvironment.client.ViewManager();

    /**
     * ViewOptions constructor comment.
     */
    public ViewOptions() {
        super();
    }

    /**
     * @see #setCloseOnSave()
     */
    public boolean getCloseOnSave() {
        return closeOnSave;
    }

    /**
     * Design Pattern: Singleton.
     *
     * @return ViewManager
     */
    public ch.softenvironment.client.ViewManager getViewManager() {
        return viewManager;
    }

    /**
     * Return whether Option with given Name is Configured YES or NO.
     *
     * @return boolean Default: false
     */
    public boolean isSet(String name) {
        if (options.containsKey(name)) {
            return options.get(name).booleanValue();
        }

        return false;
    }

    /**
     * Set whether saveObject() in DetailView's shall close GUI if only one object is represented.
     */
    public void setCloseOnSave(boolean closeOnSave) {
        this.closeOnSave = closeOnSave;
    }

    /**
     * @see #setOption(String, boolean)
     */
    public void setOption(String name) {
        setOption(name, true);
    }

    /**
     * Set a certain Option. For e.g. if a UserFunction shall be suppressed (setOption("MyFunction", false) and the appropriate view may react according to defined options.
     *
     * @param name
     * @param allow
     */
    public void setOption(String name, boolean allow) {
        options.put(name, Boolean.valueOf(allow));
    }
}

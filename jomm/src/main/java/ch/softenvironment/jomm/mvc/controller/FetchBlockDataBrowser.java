package ch.softenvironment.jomm.mvc.controller;

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

import ch.softenvironment.controller.DataBrowser;
import ch.softenvironment.jomm.DbQueryBuilder;
import ch.softenvironment.jomm.mvc.model.DbObject;
import ch.softenvironment.jomm.mvc.model.DbSessionBean;
import ch.softenvironment.util.Tracer;
import java.util.ArrayList;

/**
 * Browsing tool to scroll through a ResultSet defined by a DbQuerybuilder. This Browser extends DataBrowser by limited size of objects to search by using a fetch-block. This part is therefore meant
 * for long result-sets as for e.g. in database search.
 *
 * @author Peter Hirzel, softEnvironment GmbH
 * @see ch.softenvironment.view.DataSelectorPanel for NLS-properties
 */
public class FetchBlockDataBrowser<T extends DbObject> extends DataBrowser<T> {

    private DbQueryBuilder builder = null;
    private Class<T> dbObject = null;
    private int maxStep = 50; // default

    /**
     * Initialize Browser with empty search-list and show max 10 hits (fetch-block) by default.
     */
    public FetchBlockDataBrowser() {
        super();

        setStep(10);
    }

    /**
     * Execute the query abstracted by given builder and initialize scroll-buttons.
     *
     * @param builder
     * @param dbObject
     * @param cached Suppress query if it is the same as in last query
     * @see #getModel()
     */
    public void setQuery(Class<T> dbObject, DbQueryBuilder builder, boolean cached) throws Exception {
        this.builder = builder;
        this.dbObject = dbObject;
        this.builder.setFetchBlockSize(getStep()); // initially like step
        doQuery();
    }

    /**
     * Execute the real query against DBMS.
     *
     * @see #setQuery(Class, DbQueryBuilder, boolean)
     */
    private synchronized void doQuery() throws Exception {
        if (builder == null) {
            // should not happen
            setObjects(new ArrayList<T>());
        } else {
            try {
                // 1) query the wanted result
                java.util.List<T> objects = builder.getObjectServer().retrieveAll(dbObject, builder);
                try {
                    if (builder.getDefaultLocale() != null) {
                        for (int i = 0; i < objects.size(); i++) {
                            ((DbSessionBean) objects.get(i)).setDefaultLocale(builder.getDefaultLocale());
                        }
                    }
                } catch (ClassCastException cce) {
                    Tracer.getInstance().developerWarning("Only DbSessionBeans expected: " + cce.getLocalizedMessage());
                }

                if (builder.getFetchBlockSize() != DbQueryBuilder.FETCHBLOCK_UNLIMITED) {
                    // 2) estimate the number of records
                    int count = builder.getCount(dbObject); //(new Integer(builder.getObjectServer().getFirstValue(builder.toCountBuilder()).toString())).intValue();
                    // 3) fill with dummy objects which will be refreshed by #scrollNext() or #scrollLast()
                    if (objects.size() < count) { // count might differ from count in case someone else added/removed Objects inbetween
                        for (int i = objects.size(); i < count; i++) {
                            objects.add(null);
                        }
                    }
                }
                setObjects(objects);
            } catch (Exception e) {
                Tracer.getInstance().runtimeError("Query error", e);
                throw e;
            }
        }
    }

    /**
     * Define filter-limit between [0..STEP_MAX] of results to be shown.
     *
     * @see DbQueryBuilder#setFetchBlockSize(int)
     */
    @Override
    public void setStep(int step) {
        if (step > maxStep) {
            Tracer.getInstance().runtimeWarning("row max reduced to " + maxStep + "!");
            super.setStep(maxStep);
        } else {
            super.setStep(step);
        }
    }

    /**
     * Define max size of fetch-block.
     *
     * @param maxStep
     */
    public void setMaxStep(int maxStep) {
        if (maxStep < 1) {
            Tracer.getInstance().developerWarning("maxStep must be > 0");
            this.maxStep = 50;
        }
        this.maxStep = maxStep;
    }

    /**
     * Show range of objects currently displayed. Depends on defined step.
     *
     * @return
     * @see #setStep(int)
     */
    public String getRangeString() {
        try {
            if (getObjects().size() == 0) {
                return "--";
            } else {
                return (getCurrentIndex() + 1) + ".." + (getCurrentIndex() + getStep());
            }
        } catch (Exception e) {
            // should not happen
            Tracer.getInstance().developerWarning(e.getLocalizedMessage());
            return " ";
        }
    }

    /**
     * If current fetch-block size is unlimited, then make sure all hits are to be found.
     */
    @Override
    public synchronized T getLast() {
        try {
            if (builder.getFetchBlockSize() != DbQueryBuilder.FETCHBLOCK_UNLIMITED) {
                builder.setFetchBlockSize(DbQueryBuilder.FETCHBLOCK_UNLIMITED);
                doQuery();
            }
            return super.getLast();
        } catch (Exception e) {
            Tracer.getInstance().runtimeError("query with FETCHBLOCK_UNLIMITED failed", e);
            return null;
        }
    }

    /**
     * Make sure fetch-block is extended to really read next block (step) of objects from database.
     */
    @Override
    public synchronized T getNext() {
        try {
            int nextCurrentIndex = getCurrentIndex() + getStep();
            if ((builder.getFetchBlockSize() != DbQueryBuilder.FETCHBLOCK_UNLIMITED) && (builder.getFetchBlockSize() < (nextCurrentIndex + 1))) {
                builder.setFetchBlockSize(builder.getFetchBlockSize() + getStep());
                doQuery();
                // in a multiuser system, the expected count of data to be found might change at any time
                if ((getObjects().size() + 1) > nextCurrentIndex) {
                    setCurrentIndex(nextCurrentIndex); // browser.scrollNext();
                    return getCurrentObject();
                } else {
                    // let DataBrowser decide, which means 0 or -1
                    return getCurrentObject();
                }
            } else {
                // next must have been cached before already
                return super.getNext();
            }
        } catch (Exception e) {
            Tracer.getInstance().runtimeError("query with fetchblock+step limit failed", e);
            return null;
        }
    }

    /**
     * Force all objects to be read according to given builder.
     * <p>
     * On the comparison to {@link #getObjects()} this method reads all possible results, where {@link #getObjects()} returns only objects within limited fetch-block.
     *
     * @return
     */
    public java.util.List<T> getAllObjects() throws Exception {
        if ((builder != null) && (builder.getFetchBlockSize() != DbQueryBuilder.FETCHBLOCK_UNLIMITED)) {
            builder.setFetchBlockSize(DbQueryBuilder.FETCHBLOCK_UNLIMITED);
            doQuery();
        }
        return getObjects();
    }
}

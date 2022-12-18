package ch.softenvironment.jomm.implementation;

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

import ch.softenvironment.client.ResourceManager;
import ch.softenvironment.jomm.DbObjectServer;
import ch.softenvironment.util.DeveloperException;
import lombok.extern.slf4j.Slf4j;

/**
 * Persistence State-Machine. Manage the internal state of a DbObject during its Life-Cycle.
 *
 * @author Peter Hirzel
 */
@Slf4j
public final class DbState {

    // possible states for a DbObject
    public final static int UNDEFINED = -1;
    public final static int NEW = 10;
    public final static int NEW_FORWARDED = 11; // should be called from
    // DbObject#forward() only
    public final static int SAVED = 20;
    public final static int READ_ONLY = 30;
    public final static int CHANGED = 40;
    public final static int REMOVED = 50;
    public final static int REMOVED_PENDING = 51;

    // current state
    private int state = UNDEFINED;

    /**
     * Create DbState with initial State UNDEFINED.
     */
    public DbState() {
        super();
    }

    /**
     * Return speaking name for given state.
     */
    private static String getString(final int state) {
        switch (state) {
            case NEW:
                return ResourceManager.getResource(DbState.class, "CI_new");
            case SAVED:
                return ResourceManager.getResource(DbState.class, "CI_saved");
            case CHANGED:
                return ResourceManager.getResource(DbState.class, "CI_changed");
            case REMOVED:
                return ResourceManager.getResource(DbState.class, "CI_removed");
            case REMOVED_PENDING:
                return ResourceManager.getResource(DbState.class, "CI_removedPending");
            case READ_ONLY:
                return ResourceManager.getResource(DbState.class, "CI_readOnly");
            case UNDEFINED:
                return ResourceManager.getResource(DbState.class, "CI_undefined");
            default:
                return ResourceManager.getResource(DbState.class, "CI_default");
        }
    }

    /**
     * State of a CHANGED persistent Object, by means change is not yet persistent. DbObject is visible in Database for others!
     *
     * @return boolean true->Object changed after loading
     */
    public boolean isChanged() {
        return state == CHANGED;
    }

    /**
     * State of new still transient only Objects. DbObject is NOT visible in Database for others!
     *
     * @return boolean true->NEW; false->otherwise.
     */
    public boolean isNew() {
        return state == NEW;
    }

    /**
     * State of new still transient only Objects which were forwarded from one DbObjectServer to another. DbObject is NOT visible in Database for others!
     *
     * @return boolean true->NEW; false->otherwise.
     * @see ch.softenvironment.jomm.mvc.model.DbChangeableBean#forward(DbObjectServer, boolean)
     */
    public boolean isNewForwarded() {
        return state == NEW_FORWARDED;
    }

    /**
     * State if Object already existed persistently in target-System.
     *
     * @return boolean true->persistent; false->transient only
     */
    public boolean isPersistent() {
        return (state == SAVED) || (state == CHANGED) || (state == READ_ONLY) || (state == REMOVED_PENDING);
    }

    /**
     * State of Object if persistent and READ_ONLY. DbObject is visible in Database for others!
     *
     * @return boolean true->loaded as READ_ONLY
     */
    public boolean isReadOnly() {
        return state == READ_ONLY;
    }

    /**
     * State in case Deletion of Object is persistent. DbObject is NOT visible in Database for others!
     *
     * @return boolean true->really removed
     */
    public boolean isRemoved() {
        return state == REMOVED;
    }

    /**
     * State in case Deletion of Object should happen sooner or later, but since the COMMIT of deletion has not been transacted yet, the object remains persistent in Target-System.
     * <p>
     * DbObject is STILL visible in Target-System for others!
     *
     * @return boolean true->treat as deleted but not committed to database
     */
    public boolean isRemovedPending() {
        return state == REMOVED_PENDING;
    }

    /**
     * State of an already SAVED Object in database. DbObject is visible in Database for others!
     *
     * @return boolean true->saved persistently
     */
    public boolean isSaved() {
        return state == SAVED;
    }

    /**
     * State of an UNDEFINED Object usually the initial state. DbObject is NOT visible in Database for others!
     *
     * @return true->unclear state
     */
    public boolean isUndefined() {
        return state == UNDEFINED;
    }

    /**
     * Force a State-Transition of current-State to next-State within a DbObject. Design Pattern: State Machine
     *
     * @throws DeveloperException in case of illegal State-Transition
     */
    public final void setNext(final int nextState) throws DeveloperException {
        if (state == nextState) {
            // state may always remain in the current state (as is)
            return;
        }

        if ((nextState == NEW_FORWARDED) /* || (nextState == UNDEFINED) */) {
            state = nextState;
            return;
        }

        if (state == UNDEFINED) {
            if ((nextState == NEW) || (nextState == SAVED) || (nextState == READ_ONLY)) {
                state = nextState;
                return;
            }
        }

        if ((state == NEW) || (state == NEW_FORWARDED)) {
            if (nextState == CHANGED) {
                // remains NEW even if CHANGED because of missing ID
                return;
            }
            if ((nextState == SAVED) || (nextState == READ_ONLY) || (nextState == REMOVED) || (nextState == REMOVED_PENDING)) {
                state = nextState;
                return;
            }
        }

        if ((state == SAVED) || (state == CHANGED)) {
            if ((nextState == CHANGED) || (nextState == SAVED) || (nextState == READ_ONLY) || (nextState == REMOVED) || (nextState == REMOVED_PENDING)
                || (nextState == UNDEFINED /* in case a refresh failed */)) {
                state = nextState;
                return;
            }
        }

        if (state == READ_ONLY) {
            if ((nextState == SAVED) || (nextState == REMOVED)) {
                // TODO DbCode/DbEnumeration-Hack: remove this behavior
                log.warn("Developer warning: critical state-transition");
                state = nextState;
                return;
            }
        }

        if (state == REMOVED_PENDING) {
            if ((nextState == REMOVED) || (nextState == CHANGED /* undoRemove */)) {
                state = nextState;
                return;
            }
        }

        throw new DeveloperException("Illegal State-Transition <" + toString() + "> to <" + getString(nextState) + ">");
    }

    @Override
    public String toString() {
        return getString(state);
    }
}

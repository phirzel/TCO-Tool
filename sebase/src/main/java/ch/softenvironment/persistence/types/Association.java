package ch.softenvironment.persistence.types;

/**
 * Implement for any [n:n] Class.
 *
 * @author Peter Hirzel
 */
public interface Association {

    /**
     * Make sure in binary [0..*]:[0..*] associations the other ends are cleaned up, by means do not point to the "intermediate" association-class any more.
     * <p>
     * By convention the ObjectHistory is written like that at Association removal: 1) complete dump of this association-class 2) Reference-removal for both ends on each end (other changes on either
     * end will be written, when either end is saved finally.
     */
    void removeEnds();
}

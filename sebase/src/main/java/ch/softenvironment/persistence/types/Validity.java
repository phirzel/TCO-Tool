package ch.softenvironment.persistence.types;

/**
 * Model classes implementing the <<interface>> "Validity" will have an interval[ValidFrom..ValidTo].
 *
 * @author Peter Hirzel
 */
public interface Validity {

    /**
     * Start of interval.
     *
     * @return
     */
    java.util.Date getValidFrom();

    void setValidFrom(java.util.Date validFrom);

    /**
     * End of interval.
     *
     * @return
     */
    java.util.Date getValidTo();

    void setValidTo(java.util.Date validFrom);
}
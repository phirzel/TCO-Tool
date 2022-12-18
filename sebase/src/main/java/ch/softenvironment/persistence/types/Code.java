package ch.softenvironment.persistence.types;

/**
 * Code type (like Branch, Country,..).
 *
 * @author Peter Hirzel
 */
public interface Code {

    //TODO StringNls getName();
    java.lang.String /*len=15*/ getShortName();
}

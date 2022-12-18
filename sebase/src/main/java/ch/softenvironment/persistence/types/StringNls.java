package ch.softenvironment.persistence.types;

import ch.softenvironment.util.DeveloperException;
import java.io.Serializable;

/**
 * Translatable String type (i18n).
 *
 * @author Peter Hirzel
 */
public class StringNls implements Serializable {

    private static final long serialVersionUID = 1L;

    private Object owner = null;

    /**
     * The owning instance having this StringNls property as an attribute.
     *
     * @return
     */
    public Object getOwner() {
        return owner;
    }

    public void setOwner(Object owner) {
        if (owner == null) {
            throw new IllegalArgumentException("owner");
        }
        if (this.owner != null) {
            throw new DeveloperException("StringNls is already bound to owner");
        }
        this.owner = owner;
    }

    private String propertyName = "";

    /**
     * The name of the owning instance property to point on StringNls value.
     *
     * @return
     * @see #getOwner()
     */
    public String getPropertyName() {
        return this.propertyName;
    }

    public void setPropertyName(String propertyName) {
        if (propertyName == null) {
            throw new IllegalArgumentException("propertyName");
        }
        if (this.propertyName == null) {
            throw new DeveloperException("propertyName is already set");
        }
        this.propertyName = propertyName;
    }
}
package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbFieldTypeDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;
import ch.softenvironment.jomm.descriptor.DbNumericFieldDescriptor;
import ch.softenvironment.jomm.descriptor.DbTextFieldDescriptor;
import ch.softenvironment.jomm.mvc.model.DbCode;

/**
 * Code for any articles to be found in a manufacturers catalogue.
 *
 * @author Peter Hirzel
 */
public final class Catalogue extends DbCode {

    /**
     * Return the database mappings for this persistence object.
     * <p>
     * see ch.softenvironment.jomm.DbConnection.addDescriptor()
     *
     * @see ch.softenvironment.jomm.DbObjectServer#register(Class, String)
     */
    public static DbDescriptor createDescriptor(/*Class<DbCode> dbCode*/) {
        DbDescriptor descriptor = DbCode.createDefaultDescriptor(Catalogue.class);

        descriptor.add("price", "price", new DbNumericFieldDescriptor(
                java.lang.Double.class, 0.0, 9.99999999999E11, 2),
            new DbMultiplicityRange(1, 1));
        descriptor.addCode("currency", "currency",
            new DbMultiplicityRange(1, 1));
        descriptor.add("producer", "producer", new DbTextFieldDescriptor(1024),
            new DbMultiplicityRange(0, 1));
        descriptor.add("orderNumber", "orderNumber", new DbTextFieldDescriptor(
            20), new DbMultiplicityRange(0, 1));
        descriptor.add("documentation", "documentation",
            new DbTextFieldDescriptor(1024), new DbMultiplicityRange(0, 1));
        descriptor.add("depreciationDuration", "depreciationDuration",
            new DbNumericFieldDescriptor(java.lang.Long.class, 0.0,
                9999999.0, 0), new DbMultiplicityRange(1, 1));
        descriptor.add("usageDuration", "usageDuration",
            new DbNumericFieldDescriptor(java.lang.Long.class, 0.0,
                9999999.0, 0), new DbMultiplicityRange(1, 1));
        descriptor.add("expendable", "expendable", new DbFieldTypeDescriptor(
            java.lang.Boolean.class), new DbMultiplicityRange(1, 1));

        return descriptor;
    }

    public Catalogue(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    private java.lang.Double fieldPrice;

    public java.lang.Double getPrice() {
        refresh(false); // read lazy initialized objects
        return fieldPrice;
    }

    public void setPrice(java.lang.Double price) {
        java.lang.Double oldValue = fieldPrice;
        fieldPrice = price;
        firePropertyChange("price", oldValue, fieldPrice);
    }

    private org.tcotool.model.Currency fieldCurrency;

    public org.tcotool.model.Currency getCurrency() {
        refresh(false); // read lazy initialized objects
        return fieldCurrency;
    }

    public void setCurrency(org.tcotool.model.Currency currency) {
        org.tcotool.model.Currency oldValue = fieldCurrency;
        fieldCurrency = currency;
        firePropertyChange("currency", oldValue, fieldCurrency);
    }

    private String fieldProducer;

    public String getProducer() {
        refresh(false); // read lazy initialized objects
        return fieldProducer;
    }

    public void setProducer(String producer) {
        String oldValue = fieldProducer;
        fieldProducer = producer;
        firePropertyChange("producer", oldValue, fieldProducer);
    }

    private String fieldOrderNumber;

    public String getOrderNumber() {
        refresh(false); // read lazy initialized objects
        return fieldOrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        String oldValue = fieldOrderNumber;
        fieldOrderNumber = orderNumber;
        firePropertyChange("orderNumber", oldValue, fieldOrderNumber);
    }

    private String fieldDocumentation;

    public String getDocumentation() {
        refresh(false); // read lazy initialized objects
        return fieldDocumentation;
    }

    public void setDocumentation(String documentation) {
        String oldValue = fieldDocumentation;
        fieldDocumentation = documentation;
        firePropertyChange("documentation", oldValue, fieldDocumentation);
    }

    private java.lang.Long fieldDepreciationDuration;

    public java.lang.Long getDepreciationDuration() {
        refresh(false); // read lazy initialized objects
        return fieldDepreciationDuration;
    }

    public void setDepreciationDuration(java.lang.Long depreciationDuration) {
        java.lang.Long oldValue = fieldDepreciationDuration;
        fieldDepreciationDuration = depreciationDuration;
        firePropertyChange("depreciationDuration", oldValue,
            fieldDepreciationDuration);
    }

    private java.lang.Long fieldUsageDuration;

    public java.lang.Long getUsageDuration() {
        refresh(false); // read lazy initialized objects
        return fieldUsageDuration;
    }

    public void setUsageDuration(java.lang.Long usageDuration) {
        java.lang.Long oldValue = fieldUsageDuration;
        fieldUsageDuration = usageDuration;
        firePropertyChange("usageDuration", oldValue, fieldUsageDuration);
    }

    @Deprecated
    private Boolean fieldExpendable;

    /**
     * @see FactCost#setExpendable(Boolean)
     */
    @Deprecated // wrong design: makes no sense and is not supported/mapped any more on Catalogue
    public void setExpendable(Boolean expendable) {
        Boolean oldValue = fieldExpendable;
        fieldExpendable = expendable;
        firePropertyChange("expendable", oldValue, expendable);
    }

    @Deprecated // wrong design: makes no sense here
    public final Boolean getExpendable() {
        return fieldExpendable;
    }
}

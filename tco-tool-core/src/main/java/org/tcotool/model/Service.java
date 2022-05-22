package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;

/**
 * Service (de: Kostenträger).
 *
 * @author Peter Hirzel, softEnvironment GmbH
 */
public class Service extends TcoObject {

    public Service(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(Service.class);
        descriptor.addCode("costCentre", "costCentre", new DbMultiplicityRange(0, 1));
        descriptor.addCode("category", "category", new DbMultiplicityRange(0, 1));
        // descriptor.addCode("catalogue", "catalogue", new
        // DbMultiplicityRange(0,1));
        descriptor.addCode("responsibility", "responsibility", new DbMultiplicityRange(0, 1));
        descriptor.addManyToOneReferenceId(DbDescriptor.ASSOCIATION, "groupId", "T_Id_group", new DbMultiplicityRange(1, 1));
        descriptor.addOneToMany(DbDescriptor.COMPOSITION, "driver", "serviceId", new DbMultiplicityRange(0, DbMultiplicityRange.UNBOUND), CostDriver.class, false);
        return descriptor;
    }

    public org.tcotool.model.ServiceCategory fieldCategory;

    public org.tcotool.model.ServiceCategory getCategory() {
        refresh(false); // read lazy initialized objects
        return fieldCategory;
    }

    public void setCategory(org.tcotool.model.ServiceCategory category) {
        org.tcotool.model.ServiceCategory oldValue = fieldCategory;
        fieldCategory = category;
        firePropertyChange("category", oldValue, fieldCategory);
    }

    public org.tcotool.model.CostCentre fieldCostCentre;

    public org.tcotool.model.CostCentre getCostCentre() {
        refresh(false); // read lazy initialized objects
        return fieldCostCentre;
    }

    public void setCostCentre(org.tcotool.model.CostCentre costCentre) {
        org.tcotool.model.CostCentre oldValue = fieldCostCentre;
        fieldCostCentre = costCentre;
        firePropertyChange("costCentre", oldValue, fieldCostCentre);
    }

    public java.util.List<CostDriver> fieldDriver = new java.util.ArrayList<CostDriver>();

    public java.util.List<CostDriver> getDriver() {
        refresh(false); // read lazy initialized objects
        return fieldDriver;
    }

    public void setDriver(java.util.List<CostDriver> driver) {
        java.util.List<CostDriver> oldValue = fieldDriver;
        fieldDriver = driver;
        firePropertyChange("driver", oldValue, fieldDriver);
    }

    public java.lang.Long fieldGroupId;

    public java.lang.Long getGroupId() {
        refresh(false); // read lazy initialized objects
        return fieldGroupId;
    }

    public void setGroupId(java.lang.Long group) {
        java.lang.Long oldValue = fieldGroupId;
        fieldGroupId = group;
        firePropertyChange("groupId", oldValue, fieldGroupId);
    }

    public org.tcotool.model.Responsibility fieldResponsibility;

    public org.tcotool.model.Responsibility getResponsibility() {
        refresh(false); // read lazy initialized objects
        return fieldResponsibility;
    }

    public void setResponsibility(org.tcotool.model.Responsibility responsibility) {
        org.tcotool.model.Responsibility oldValue = fieldResponsibility;
        fieldResponsibility = responsibility;
        firePropertyChange("responsibility", oldValue, fieldResponsibility);
    }
}

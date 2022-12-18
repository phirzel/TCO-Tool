package org.tcotool.model;

import ch.softenvironment.jomm.descriptor.DbDescriptor;
import ch.softenvironment.jomm.descriptor.DbMultiplicityRange;

/**
 * A cost driver (de: Kostentreiber) is something that generates costs. However in this TCO model a CostDriver accumulates a set of Cost's.
 *
 * @author Peter Hirzel
 */
public class CostDriver extends TcoObject {

    public CostDriver(ch.softenvironment.jomm.DbObjectServer objectServer) {
        super(objectServer);
    }

    public static DbDescriptor createDescriptor() {
        DbDescriptor descriptor = new DbDescriptor(CostDriver.class);
        descriptor.addCode("phase", "phase", new DbMultiplicityRange(0, 1));
        descriptor.addCode("cycle", "cycle", new DbMultiplicityRange(0, 1));
        descriptor.addCode("process", "process", new DbMultiplicityRange(0, 1));
        descriptor.addOneToMany(DbDescriptor.COMPOSITION, "cost", "driverId",
            new DbMultiplicityRange(0, DbMultiplicityRange.UNBOUND),
            Cost.class, false);
        descriptor.addManyToOneReferenceId(DbDescriptor.ASSOCIATION,
            "serviceId", "T_Id_service", new DbMultiplicityRange(1, 1));
        descriptor.addOneToMany(DbDescriptor.COMPOSITION, "occurrance",
            "driverId", new DbMultiplicityRange(0,
                DbMultiplicityRange.UNBOUND), Occurance.class, false);
        return descriptor;
    }

    private java.util.List<Cost> fieldCost = new java.util.ArrayList<>();

    public java.util.List<Cost> getCost() {
        refresh(false); // read lazy initialized objects
        return fieldCost;
    }

    public void setCost(java.util.List<Cost> cost) {
        java.util.List<Cost> oldValue = fieldCost;
        fieldCost = cost;
        firePropertyChange("cost", oldValue, fieldCost);
    }

    private org.tcotool.model.LifeCycle fieldCycle;

    public org.tcotool.model.LifeCycle getCycle() {
        refresh(false); // read lazy initialized objects
        return fieldCycle;
    }

    public void setCycle(org.tcotool.model.LifeCycle cycle) {
        org.tcotool.model.LifeCycle oldValue = fieldCycle;
        fieldCycle = cycle;
        firePropertyChange("cycle", oldValue, fieldCycle);
    }

    private java.util.List<Occurance> fieldOccurrance = new java.util.ArrayList<>();

    public java.util.List<Occurance> getOccurrance() {
        refresh(false); // read lazy initialized objects
        return fieldOccurrance;
    }

    public void setOccurrance(java.util.List<Occurance> occurrance) {
        java.util.List<Occurance> oldValue = fieldOccurrance;
        fieldOccurrance = occurrance;
        firePropertyChange("occurrance", oldValue, fieldOccurrance);
    }

    private org.tcotool.model.ProjectPhase fieldPhase;

    public org.tcotool.model.ProjectPhase getPhase() {
        refresh(false); // read lazy initialized objects
        return fieldPhase;
    }

    public void setPhase(org.tcotool.model.ProjectPhase phase) {
        org.tcotool.model.ProjectPhase oldValue = fieldPhase;
        fieldPhase = phase;
        firePropertyChange("phase", oldValue, fieldPhase);
    }

    private org.tcotool.model.Process fieldProcess;

    public org.tcotool.model.Process getProcess() {
        refresh(false); // read lazy initialized objects
        return fieldProcess;
    }

    public void setProcess(org.tcotool.model.Process process) {
        org.tcotool.model.Process oldValue = fieldProcess;
        fieldProcess = process;
        firePropertyChange("process", oldValue, fieldProcess);
    }

    private java.lang.Long fieldServiceId;

    public java.lang.Long getServiceId() {
        refresh(false); // read lazy initialized objects
        return fieldServiceId;
    }

    public void setServiceId(java.lang.Long service) {
        java.lang.Long oldValue = fieldServiceId;
        fieldServiceId = service;
        firePropertyChange("serviceId", oldValue, fieldServiceId);
    }
}

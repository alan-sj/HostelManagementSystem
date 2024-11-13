package com.example.newproj.Models;

import javafx.beans.property.*;

public class Staff {
    private final StringProperty name;
    private final StringProperty staffId;
    private final StringProperty role;
    private final DoubleProperty salary;
    private final StringProperty dateOfJoin;
    private final IntegerProperty contact;
    private final BooleanProperty selected;

    public Staff(String name, String staffId, String role, double salary, String dateOfJoin, int contact) {
        this.name = new SimpleStringProperty(name);
        this.staffId = new SimpleStringProperty(staffId);
        this.role = new SimpleStringProperty(role);
        this.salary = new SimpleDoubleProperty(salary);
        this.dateOfJoin = new SimpleStringProperty(dateOfJoin);
        this.contact = new SimpleIntegerProperty(contact);
        this.selected = new SimpleBooleanProperty(false);
    }

    // Getters
    public String getName() { return name.get(); }
    public String getStaffId() { return staffId.get(); }
    public String getRole() { return role.get(); }
    public double getSalary() { return salary.get(); }
    public String getDateOfJoin() { return dateOfJoin.get(); }
    public int getContact() { return contact.get(); }
    public boolean isSelected() { return selected.get(); }

    // Setters
    public void setName(String name) { this.name.set(name); }
    public void setStaffId(String staffId) { this.staffId.set(staffId); }
    public void setRole(String role) { this.role.set(role); }
    public void setSalary(double salary) { this.salary.set(salary); }
    public void setDateOfJoin(String dateOfJoin) { this.dateOfJoin.set(dateOfJoin); }
    public void setContact(int contact) { this.contact.set(contact); }
    public void setSelected(boolean selected) { this.selected.set(selected); }

    // Property getters for TableView bindings
    public StringProperty nameProperty() { return name; }
    public StringProperty staffIdProperty() { return staffId; }
    public StringProperty roleProperty() { return role; }
    public DoubleProperty salaryProperty() { return salary; }
    public StringProperty dateOfJoinProperty() { return dateOfJoin; }
    public IntegerProperty contactProperty() { return contact; }
    public BooleanProperty selectedProperty() { return selected; }
}

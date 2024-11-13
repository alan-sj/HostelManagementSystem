package com.example.newproj.Models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Hosteller {
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private final StringProperty name = new SimpleStringProperty("");
    private final IntegerProperty roomNo = new SimpleIntegerProperty(0);
    private final StringProperty admissionNumber = new SimpleStringProperty("");
    private final StringProperty dateOfAdmission = new SimpleStringProperty("");
    private final IntegerProperty contact = new SimpleIntegerProperty(0);



    public Hosteller(String name, int roomNo, String admissionNumber, String dateOfAdmission, int contact) {
        this.name.set(name);
        this.roomNo.set(roomNo);
        this.admissionNumber.set(admissionNumber);
        this.dateOfAdmission.set(dateOfAdmission);
        this.contact.set(contact);
    }

    // Getters and setters for each property
    public boolean isSelected() {
        return selected.get();
    }


    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getRoomNo() {
        return roomNo.get();
    }

    public IntegerProperty roomNoProperty() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo.set(roomNo);
    }

    public String getAdmissionNumber() {
        return admissionNumber.get();
    }

    public StringProperty admissionNumberProperty() {
        return admissionNumber;
    }

    public void setAdmissionNumber(String admissionNumber) {
        this.admissionNumber.set(admissionNumber);
    }

    public String getDateOfAdmission() {
        return dateOfAdmission.get();
    }

    public StringProperty dateOfAdmissionProperty() {
        return dateOfAdmission;
    }

    public void setDateOfAdmission(String dateOfAdmission) {
        this.dateOfAdmission.set(dateOfAdmission);
    }

    public int getContact() {
        return contact.get();
    }

    public IntegerProperty contactProperty() {
        return contact;
    }

    public void setContact(int contact) {
        this.contact.set(contact);
    }
}

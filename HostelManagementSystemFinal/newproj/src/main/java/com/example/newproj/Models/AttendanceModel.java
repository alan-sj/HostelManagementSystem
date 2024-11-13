package com.example.newproj.Models;

import javafx.beans.property.*;

public class AttendanceModel {
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private final StringProperty name = new SimpleStringProperty("");
    private final StringProperty roomNo = new SimpleStringProperty("");
    private final StringProperty admissionNumber = new SimpleStringProperty("");
    private final IntegerProperty noDaysPresent = new SimpleIntegerProperty(0);

    public AttendanceModel(String name, String roomNo, String admissionNumber, int noDaysPresent) {
        this.name.set(name);
        this.roomNo.set(roomNo);
        this.admissionNumber.set(admissionNumber);
        this.noDaysPresent.set(noDaysPresent);
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

    public String getRoomNo() {
        return roomNo.get();
    }

    public int getNoDaysPresent() {
        return noDaysPresent.get();
    }

    public StringProperty roomNoProperty() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
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

    public IntegerProperty getNoDaysPresentProperty() {
        return noDaysPresent;
    }

    public void setNoDaysPresent(Integer noDaysPresent) {
        this.noDaysPresent.set(noDaysPresent);
    }
}

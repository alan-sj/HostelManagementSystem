package com.example.newproj.Models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class fee {
    private SimpleStringProperty name;
    private SimpleStringProperty admissionNo;
    private SimpleIntegerProperty fees;
    private SimpleStringProperty dueDate;

    public fee(String name, String admissionNo, int fees, String dueDate) {
        this.name = new SimpleStringProperty(name);
        this.admissionNo = new SimpleStringProperty(admissionNo);
        this.fees = new SimpleIntegerProperty(fees);
        this.dueDate = new SimpleStringProperty(dueDate);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getAdmissionNo() {
        return admissionNo.get();
    }

    public void setAdmissionNo(String admissionNo) {
        this.admissionNo.set(admissionNo);
    }

    public int getFees() {
        return fees.get();
    }

    public void setFees(int fees) {
        this.fees.set(fees);
    }
    public String getDueDate() {
        return dueDate.get();
    }
    public void setDueDate(String dueDate) {
        this.dueDate.set(dueDate);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty admissionNoProperty() {
        return admissionNo;
    }

    public SimpleIntegerProperty feesProperty() {
        return fees;
    }

    public SimpleStringProperty dueDateProperty() {
        return dueDate;
    }
}
package com.example.newproj.Models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ComplaintsModel {
    private String title = "";
    private String des = "";
    private int isResolved = 0;

    public ComplaintsModel(String title, String des, int isResolved) {
        this.title = title;
        this.des = des;
        this.isResolved = isResolved;
    }

    // Getters and setters for each property
    public boolean isResolved() {
        return isResolved != 0;
    }

    public void setIsResolved(boolean value) {
        this.isResolved = value?1:0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDes() {
        return des;
    }

}

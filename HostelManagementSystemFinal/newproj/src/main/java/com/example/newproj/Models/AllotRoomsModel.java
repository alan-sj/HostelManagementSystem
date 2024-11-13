package com.example.newproj.Models;

import javafx.scene.layout.AnchorPane;

public class AllotRoomsModel {
    private Boolean selected = false;
    private String roomNo = "";
    private AnchorPane anchorPane = new AnchorPane();

    public AllotRoomsModel(Boolean selected, String name, AnchorPane anchorPane) {
        this.selected = selected;
        this.roomNo = roomNo;
        this.anchorPane = anchorPane;
    }

    // Getters and setters for each property
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public void setAnchorPane(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }
}

package com.example.newproj.controllers;

import com.example.newproj.Main;
import javafx.fxml.FXML;

public class ComplaintsController {

    @FXML
    public void onDashboardClick(){
        Main MainApp = new Main();
        try {
            MainApp.loadAllotRoomsScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onHostellerInfoClick(){
        Main MainApp = new Main();
        try {
            MainApp.loadHostellerScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onFeeManagementClick(){
        Main MainApp = new Main();
        try {
            MainApp.loadFeeScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onComplaintsClick(){
        Main MainApp = new Main();
        try {
            MainApp.loadComplaintsScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onStaffManagementClick(){
        Main MainApp = new Main();
        try {
            MainApp.loadStaffScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onAllotRoomsClick(){
        Main MainApp = new Main();
        try {
            MainApp.loadAllotRoomsScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onAttendanceClick(){
        Main MainApp = new Main();
        try {
            MainApp.loadAttendanceScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onLogOutClick(){
        Main MainApp = new Main();
        try {
            MainApp.loadAllotRoomsScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

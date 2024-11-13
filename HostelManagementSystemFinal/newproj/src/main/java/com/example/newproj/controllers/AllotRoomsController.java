package com.example.newproj.controllers;

import com.example.newproj.Main;
import com.example.newproj.Models.AllotRoomsModel;
import com.example.newproj.dao.AllotRoomsDOA;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AllotRoomsController {
    private List<AllotRoomsModel> rooms = new ArrayList<>();
    private final AllotRoomsDOA allotRoomsDOA = new AllotRoomsDOA();
    private int admNo;
    @FXML private Text studentName;
    @FXML private TextField searchField;
    @FXML private Text infoText;
    @FXML public Text selRoomText;
    @FXML public HBox roomsRow;

    public AllotRoomsController() throws SQLException {
    }

    @FXML
    public void onDashboardClick(){
        Main MainApp = new Main();
        try {
            MainApp.loadDashboardScene();
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
            MainApp.loadLoginScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onAddClick(){
        System.out.println("Add room");
    }

    private boolean checkMatch(String[] lst, String item){
        for (String s : lst) {
            if (s.equals(item)) {
                return true;
            }
        }
        return false;
    }

    @FXML
    public void initialize() {
        loadRooms();
    }

    private void loadRooms() {
        try {
            roomsRow.getChildren().clear();
            rooms.clear();
            rooms = allotRoomsDOA.getRoomModels();
            for (AllotRoomsModel room : rooms) {
                roomsRow.getChildren().add(room.getAnchorPane());
            }
            allotRoomsDOA.selectedRoom = rooms.getFirst().getRoomNo();
        } catch (SQLException e) {
            showError("Data Loading Error", "Failed to load hosteller data: " + e.getMessage());
        }
    }

    public void onSearchClick() throws SQLException {
        admNo = Integer.parseInt(searchField.getText());
        String name = allotRoomsDOA.getStudentName(admNo);
        studentName.setText(name);
        infoText.setText("Allot Rooms for: "+name);
    }

    public void onAllotClicked() throws SQLException{
        if(infoText.getText() != "Allot Rooms for: " || infoText.getText() != "Allot Rooms for: ") {
            allotRoomsDOA.updateRoom(admNo);
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
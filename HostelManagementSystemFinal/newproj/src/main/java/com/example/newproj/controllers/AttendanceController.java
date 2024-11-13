package com.example.newproj.controllers;

import com.example.newproj.Main;
import com.example.newproj.Models.AttendanceModel;
import com.example.newproj.dao.AttendanceDOA;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class AttendanceController {
    private final AttendanceDOA attendanceDOA = new AttendanceDOA();
    @FXML private TableView<AttendanceModel> attendenceTable;
    @FXML private CheckBox selectAllCheckBox;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private TableColumn<AttendanceModel, Boolean> selectColumn;
    @FXML private TableColumn<AttendanceModel, String> nameColumn;
    @FXML private TableColumn<AttendanceModel, String> roomNoColumn; // Changed to Integer
    @FXML private TableColumn<AttendanceModel, String> admissionNumberColumn;
    @FXML private TableColumn<AttendanceModel, Integer> noDaysPresentColumn;
    @FXML private TextField searchField;

    private final ObservableList<AttendanceModel> studentList = FXCollections.observableArrayList();
    private FilteredList<AttendanceModel> filteredData;

    public AttendanceController() throws SQLException {
    }

    @FXML
    private void initialize() {
        initializeFilteredList();
        setupTable();
        setupListeners();
        loadData();
        attendenceTable.setEditable(true);
    }

    private void initializeFilteredList() {
        filteredData = new FilteredList<>(studentList, p -> true);
        attendenceTable.setItems(filteredData);
    }

    private void setupTable() {
        // Set cell value factories
        selectColumn.setCellValueFactory(data -> data.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        nameColumn.setEditable(false);

        roomNoColumn.setCellValueFactory(data -> data.getValue().roomNoProperty());
        roomNoColumn.setEditable(false);

        admissionNumberColumn.setCellValueFactory(data -> data.getValue().admissionNumberProperty());
        admissionNumberColumn.setEditable(false);

        noDaysPresentColumn.setCellValueFactory(data -> data.getValue().getNoDaysPresentProperty().asObject());
        noDaysPresentColumn.setEditable(false);
    }

    private void setupListeners() {
        // Search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(hosteller -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                // Handle string and integer comparisons
                return hosteller.getName().toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(hosteller.getRoomNo()).contains(lowerCaseFilter) || // Convert Integer to String
                        hosteller.getAdmissionNumber().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Select all checkbox
        selectAllCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.forEach(hosteller -> hosteller.setSelected(newValue));
            updateButtonStates();
        });

        // Update button states when selection changes
        studentList.addListener((javafx.collections.ListChangeListener<AttendanceModel>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(hosteller ->
                            hosteller.selectedProperty().addListener((obs, old, newVal) -> updateButtonStates())
                    );
                }
            }
            updateButtonStates();
        });
    }

    private void updateButtonStates() {
        boolean anySelected = studentList.stream().anyMatch(AttendanceModel::isSelected);
        cancelButton.setDisable(!anySelected);

        // Update select all checkbox state
        boolean allSelected = studentList.stream().allMatch(AttendanceModel::isSelected);
        selectAllCheckBox.setSelected(allSelected);
    }

    private void loadData() {
        try {
            studentList.clear(); // Clear the list to avoid duplicates
            List<AttendanceModel> hostellersFromDB = attendanceDOA.getAllAttendanceModels();
            System.out.println("Loading data: " + hostellersFromDB.size() + " records found."); // Debugging line
            studentList.addAll(hostellersFromDB); // Add all records from the database
        } catch (SQLException e) {
            showError("Data Loading Error", "Failed to load hosteller data: " + e.getMessage());
        }
    }

    public void updateDayCount() throws SQLException {
        long selectedCount = filteredData.stream().filter(AttendanceModel::isSelected).count();
        if (selectedCount == 0) {
            showError("No Selection", "No Students selected.");
            return;
        }
        List<AttendanceModel> selected = filteredData.stream()
                .filter(AttendanceModel::isSelected)
                .collect(Collectors.toList());
        attendanceDOA.updateDayCount(selected);
        updateButtonStates();
    }

    public void deselectAll() throws SQLException {
        List<AttendanceModel> selected = filteredData.stream()
                .filter(AttendanceModel::isSelected)
                .collect(Collectors.toList());
        attendanceDOA.deselectAll(selected);
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
            Main.loadHostellerScene();
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

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

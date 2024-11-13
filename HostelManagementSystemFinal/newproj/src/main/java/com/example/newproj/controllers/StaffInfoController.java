package com.example.newproj.controllers;

import com.example.newproj.dao.StaffDAO;
import com.example.newproj.Main;
import com.example.newproj.Models.Staff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class StaffInfoController {

    private final StaffDAO staffDAO = new StaffDAO();
    @FXML private TextField searchField;
    @FXML private Button addStaffButton;
    @FXML private CheckBox selectAllCheckBox;
    @FXML private Button editSelectedButton;
    @FXML private Button deleteSelectedButton;
    @FXML private TableView<Staff> staffTable;
    @FXML private TableColumn<Staff, Boolean> selectColumn;
    @FXML private TableColumn<Staff, String> nameColumn;
    @FXML private TableColumn<Staff, String> staffIdColumn;
    @FXML private TableColumn<Staff, String> roleColumn;
    @FXML private TableColumn<Staff, Double> salaryColumn;
    @FXML private TableColumn<Staff, String> dateOfJoinColumn;
    @FXML private TableColumn<Staff, Integer> contactColumn;

    private final ObservableList<Staff> staffList = FXCollections.observableArrayList();
    private FilteredList<Staff> filteredData;

    public StaffInfoController() throws SQLException {}

    @FXML
    private void initialize() {
        initializeFilteredList();
        setupTable();
        setupListeners();
        loadData();
        staffTable.setEditable(true);
    }

    private void initializeFilteredList() {
        filteredData = new FilteredList<>(staffList, p -> true);
        staffTable.setItems(filteredData);
    }

    private void setupTable() {
        // Set up columns
        selectColumn.setCellValueFactory(data -> data.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        staffIdColumn.setCellValueFactory(data -> data.getValue().staffIdProperty());
        staffIdColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        roleColumn.setCellValueFactory(data -> data.getValue().roleProperty());
        roleColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        salaryColumn.setCellValueFactory(data -> data.getValue().salaryProperty().asObject());
        salaryColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        dateOfJoinColumn.setCellValueFactory(data -> data.getValue().dateOfJoinProperty());
        dateOfJoinColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        contactColumn.setCellValueFactory(data -> data.getValue().contactProperty().asObject());
        contactColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        // Edit commit event handlers
        setupEditCommitHandlers();
    }

    private void setupEditCommitHandlers() {
        setupColumnEditCommit(nameColumn, (staff, newValue) -> staff.setName(newValue));
        setupColumnEditCommit(staffIdColumn, (staff, newValue) -> staff.setStaffId(newValue));
        setupColumnEditCommit(roleColumn, (staff, newValue) -> staff.setRole(newValue));
        setupColumnEditCommit(salaryColumn, (staff, newValue) -> staff.setSalary(newValue));
        setupColumnEditCommit(dateOfJoinColumn, (staff, newValue) -> staff.setDateOfJoin(newValue));
        setupColumnEditCommit(contactColumn, (staff, newValue) -> staff.setContact(newValue));
    }

    private <T> void setupColumnEditCommit(TableColumn<Staff, T> column, BiConsumer<Staff, T> updateFunction) {
        column.setOnEditCommit(event -> {
            Staff staff = event.getRowValue();
            T newValue = event.getNewValue();
            updateFunction.accept(staff, newValue);
            try {
                staffDAO.updateStaff(staff); // Update in the database
                System.out.println("Updated staff: " + staff); // Debugging line
            } catch (SQLException e) {
                showError("Database Error", "Failed to update staff: " + e.getMessage());
            }
        });
    }

    private void setupListeners() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(staff -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return staff.getName().toLowerCase().contains(lowerCaseFilter) ||
                        staff.getStaffId().toLowerCase().contains(lowerCaseFilter) ||
                        staff.getRole().toLowerCase().contains(lowerCaseFilter);
            });
        });

        selectAllCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.forEach(staff -> staff.setSelected(newValue));
            updateButtonStates();
        });

        staffList.addListener((javafx.collections.ListChangeListener<Staff>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(staff -> staff.selectedProperty().addListener((obs, old, newVal) -> updateButtonStates()));
                }
            }
            updateButtonStates();
        });

        addStaffButton.setOnAction(event -> addStaff());
        editSelectedButton.setOnAction(event -> editSelectedStaff());
        deleteSelectedButton.setOnAction(event -> {
            deleteSelectedStaff();
        });
    }

    private void editSelectedStaff() {
        List<Staff> selectedStaff = staffList.stream()
                .filter(Staff::isSelected)
                .collect(Collectors.toList());

        if (selectedStaff.isEmpty()) {
            showError("Selection Error", "No staff selected for editing.");
            return;
        }

        // Iterate over each selected staff and enable editing
        for (Staff staff : selectedStaff) {
            int index = staffList.indexOf(staff);

            // Start editing each selected staff's fields
            staffTable.edit(index, nameColumn);  // Starts editing with the name column
        }
    }

    private void updateButtonStates() {
        boolean anySelected = staffList.stream().anyMatch(Staff::isSelected);
        editSelectedButton.setDisable(!anySelected);
        deleteSelectedButton.setDisable(!anySelected);
        boolean allSelected = staffList.stream().allMatch(Staff::isSelected);
        selectAllCheckBox.setSelected(allSelected);
    }

    private void addStaff() {
        Staff newStaff = new Staff("", "", "", 0.0, LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE), 0);

        // Add the new staff to the observable list before saving to the database
        staffList.add(newStaff);

        // Select the new staff and scroll to it
        staffTable.getSelectionModel().select(newStaff);
        staffTable.scrollTo(newStaff);

        // Start editing the first editable cell
        int newIndex = staffList.indexOf(newStaff);
        staffTable.edit(newIndex, nameColumn); // Start editing the name cell

        // Define edit commit actions for each field with validation
        setupNewStaffEditCommitHandlers(newStaff);
    }

    private void setupNewStaffEditCommitHandlers(Staff newStaff) {
        setupNewStaffEditCommit(nameColumn, newStaff, (staff, newValue) -> staff.setName(newValue));
        setupNewStaffEditCommit(staffIdColumn, newStaff, (staff, newValue) -> staff.setStaffId(newValue));
        setupNewStaffEditCommit(roleColumn, newStaff, (staff, newValue) -> staff.setRole(newValue));
        setupNewStaffEditCommit(salaryColumn, newStaff, (staff, newValue) -> staff.setSalary(newValue));
        setupNewStaffEditCommit(dateOfJoinColumn, newStaff, (staff, newValue) -> staff.setDateOfJoin(newValue));
        setupNewStaffEditCommit(contactColumn, newStaff, (staff, newValue) -> staff.setContact(newValue));
    }

    private <T> void setupNewStaffEditCommit(TableColumn<Staff, T> column, Staff newStaff, BiConsumer<Staff, T> updateFunction) {
        column.setOnEditCommit(event -> {
            T newValue = event.getNewValue();
            updateFunction.accept(newStaff, newValue);
            validateAndSaveStaff(newStaff); // Validate and save
        });
    }

    private void validateAndSaveStaff(Staff staff) {
        StringBuilder errorMessage = new StringBuilder();

        if (staff.getStaffId() == null || staff.getStaffId().trim().isEmpty()) {
            errorMessage.append("Staff ID cannot be empty.\n");
        }
        // If there are validation errors, show the error message and return
        if (errorMessage.length() > 0) {
            showError("Validation Error", errorMessage.toString());
            return;
        }

        try {
            boolean exists = staffDAO.checkIfStaffExists(staff.getStaffId());

            if (exists) {
                showError("Duplicate Staff ID", "A staff member with ID " + staff.getStaffId() + " already exists. Please use a different ID.");
                return;
            } else {
                staffDAO.addStaff(staff);
                showConfirmation("Success");
            }

            loadData();
        } catch (SQLException e) {
            showError("Database Error", "Failed to save staff: " + e.getMessage());
        }
    }

    private void showConfirmation(String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText("Staff member added successfully.");
        alert.showAndWait();
    }


    private void loadData() {
        try {
            List<Staff> staffMembers = staffDAO.getAllStaff();
            staffList.clear();
            staffList.addAll(staffMembers);
        } catch (SQLException e) {
            showError("Database Error", "Failed to load staff: " + e.getMessage());
        }
    }

    private void deleteSelectedStaff() {
        List<Staff> selectedStaff = staffList.stream().filter(Staff::isSelected).collect(Collectors.toList());
        if (selectedStaff.isEmpty()) {
            showError("Selection Error", "No staff selected for deletion.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Delete Confirmation");
        confirmationAlert.setHeaderText("Are you sure you want to delete the selected staff?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                for (Staff staff : selectedStaff) {
                    staffDAO.deleteStaff(staff.getStaffId());
                    staffList.remove(staff);
                }
                loadData();
            } catch (SQLException e) {
                showError("Database Error", "Failed to delete selected staff: " + e.getMessage());
            }
        }
    }


    @FXML
    private void goToHostellerInfo() throws Exception {
        Main.loadHostellerScene();
    }

    @FXML
    private void goToFeesManagement() {
        //Main.showFeesManagementScene();
    }

    @FXML
    private void goToComplaints() {
        //Main.showComplaintsScene();
    }

    @FXML
    private void goToAttendance() {
        //Main.showAttendanceScene();
    }

    @FXML
    private void logout() {
        //Main.showLoginScene();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void onAddClick(MouseEvent mouseEvent) {
    }

    public void onStaffManagementClick(MouseEvent mouseEvent) throws Exception {

        Main.loadStaffScene();
    }

    public void onDashBoardClick(MouseEvent mouseEvent) {

        Main.loadDashboardScene();

    }

    public void onHostellerInfoClick(MouseEvent mouseEvent) throws Exception {

        Main.loadHostellerScene();
    }

    public void onFeesManagementClick(MouseEvent mouseEvent) throws IOException {

        Main.loadFeeScene();
    }

    public void onComplaintsClick(MouseEvent mouseEvent) {

        Main.loadComplaintsScene();
    }

    public void onAllotRoomsClick(MouseEvent mouseEvent) {

        Main.loadAllotRoomsScene();
    }

    public void onAttendanceClick(MouseEvent mouseEvent) {

        Main.loadAttendanceScene();
    }

    public void onLogout(MouseEvent mouseEvent) {

        Main.loadLoginScene();
    }

}
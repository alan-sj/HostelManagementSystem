package com.example.newproj.controllers;

import com.example.newproj.dao.HostellerDAO;
import com.example.newproj.Main;
import com.example.newproj.Models.Hosteller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HostellerInfoController {
    private final HostellerDAO hostellerDAO = new HostellerDAO();
    @FXML private TextField searchField;
    @FXML private Button addStudentButton;
    @FXML private CheckBox selectAllCheckBox;
    @FXML private Button editSelectedButton;
    @FXML private Button deleteSelectedButton;
    @FXML private TableView<Hosteller> hostelerTable;
    @FXML private TableColumn<Hosteller, Boolean> selectColumn;
    @FXML private TableColumn<Hosteller, String> nameColumn;
    @FXML private TableColumn<Hosteller, Integer> roomNoColumn; // Changed to Integer
    @FXML private TableColumn<Hosteller, String> admissionNumberColumn;
    @FXML private TableColumn<Hosteller, String> dateOfAdmissionColumn;
    @FXML private TableColumn<Hosteller, Integer> contactColumn; // Changed to Integer

    private final ObservableList<Hosteller> hostellerList = FXCollections.observableArrayList();
    private FilteredList<Hosteller> filteredData;

    public HostellerInfoController() throws SQLException {
    }

    @FXML
    private void initialize() {
        initializeFilteredList();
        setupTable();
        setupListeners();
        loadData();
        hostelerTable.setEditable(true);
    }

    private void initializeFilteredList() {
        filteredData = new FilteredList<>(hostellerList, p -> true);
        hostelerTable.setItems(filteredData);
    }

    private void setupTable() {
        // Set cell value factories
        selectColumn.setCellValueFactory(data -> data.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        nameColumn.setCellValueFactory(data -> data.getValue().nameProperty());
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        roomNoColumn.setCellValueFactory(data -> data.getValue().roomNoProperty().asObject());
        roomNoColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        admissionNumberColumn.setCellValueFactory(data -> data.getValue().admissionNumberProperty());
        admissionNumberColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        dateOfAdmissionColumn.setCellValueFactory(data -> data.getValue().dateOfAdmissionProperty());
        dateOfAdmissionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        contactColumn.setCellValueFactory(data -> data.getValue().contactProperty().asObject());
        contactColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        nameColumn.setOnEditCommit(event -> {
            Hosteller hosteller = event.getRowValue();
            String newName = event.getNewValue();
            hosteller.setName(newName);
            System.out.println("Updating name to " + newName);
            try {
                hostellerDAO.updateHosteller(hosteller);
            } catch (SQLException e) {
                showError("Database Error", "Failed to update hosteller name: " + e.getMessage());
            }
        });

        roomNoColumn.setOnEditCommit(event -> {
            Hosteller hosteller = event.getRowValue();
            int newRoomNo = event.getNewValue();
            hosteller.setRoomNo(newRoomNo);
            System.out.println("Updating room number to " + newRoomNo);
            try {
                hostellerDAO.updateHosteller(hosteller);
            } catch (SQLException e) {
                showError("Database Error", "Failed to update room number: " + e.getMessage());
            }
        });

        admissionNumberColumn.setOnEditCommit(event -> {
            Hosteller hosteller = event.getRowValue();
            String newAdmissionNumber = event.getNewValue();
            hosteller.setAdmissionNumber(newAdmissionNumber);
            System.out.println("Updating admission number to " + newAdmissionNumber); // Debugging line
            try {
                hostellerDAO.updateHosteller(hosteller); // Update in the database
            } catch (SQLException e) {
                showError("Database Error", "Failed to update admission number: " + e.getMessage());
            }
        });

        dateOfAdmissionColumn.setOnEditCommit(event -> {
            Hosteller hosteller = event.getRowValue();
            String newDateOfAdmission = event.getNewValue();
            hosteller.setDateOfAdmission(newDateOfAdmission);
            System.out.println("Updating date of admission to " + newDateOfAdmission);
            try {
                hostellerDAO.updateHosteller(hosteller);
            } catch (SQLException e) {
                showError("Database Error", "Failed to update date of admission: " + e.getMessage());
            }
        });

        contactColumn.setOnEditCommit(event -> {
            Hosteller hosteller = event.getRowValue();
            int newContact = event.getNewValue();
            hosteller.setContact(newContact);
            System.out.println("Updating contact to " + newContact);
            try {
                hostellerDAO.updateHosteller(hosteller);
            } catch (SQLException e) {
                showError("Database Error", "Failed to update contact number: " + e.getMessage());
            }
        });
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
                        String.valueOf(hosteller.getRoomNo()).contains(lowerCaseFilter) ||
                        hosteller.getAdmissionNumber().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Select all checkbox
        selectAllCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.forEach(hosteller -> hosteller.setSelected(newValue));
            updateButtonStates();
        });

        // Update button states when selection changes
        hostellerList.addListener((javafx.collections.ListChangeListener<Hosteller>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(hosteller ->
                            hosteller.selectedProperty().addListener((obs, old, newVal) -> updateButtonStates())
                    );
                }
            }
            updateButtonStates();
        });

        // Button actions
        addStudentButton.setOnAction(event -> addStudent());
        editSelectedButton.setOnAction(event -> editSelectedStudents());
        deleteSelectedButton.setOnAction(event -> {
            try {
                deleteSelectedStudents();
            } catch (SQLException e) {
                showError("Database Error", "Failed to delete selected students: " + e.getMessage());
            }
        });
    }

    private void updateButtonStates() {
        boolean anySelected = hostellerList.stream().anyMatch(Hosteller::isSelected);
        editSelectedButton.setDisable(!anySelected);
        deleteSelectedButton.setDisable(!anySelected);

        // Update select all checkbox state
        boolean allSelected = hostellerList.stream().allMatch(Hosteller::isSelected);
        selectAllCheckBox.setSelected(allSelected);
    }


    private void addStudent() {
        // Create a new hosteller with placeholder values
        Hosteller newHosteller = new Hosteller("", 0, "", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE), 0);

        // Show an input dialog to enter admission number
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Hosteller");
        dialog.setHeaderText("Enter Admission Number");
        dialog.setContentText("Admission Number:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().isEmpty()) {
            String admissionNumber = result.get();

            try {

                if (hostellerDAO.admissionNumberExists(admissionNumber)) {
                    showError("Duplicate Admission Number", "This admission number already exists.");
                    return;
                }
                newHosteller.setAdmissionNumber(admissionNumber);


                hostellerDAO.addHosteller(newHosteller);
                hostellerList.add(newHosteller);
                System.out.println("Added new hosteller: " + newHosteller);

                hostelerTable.getSelectionModel().select(newHosteller);
                hostelerTable.scrollTo(newHosteller);

                hostelerTable.edit(hostellerList.indexOf(newHosteller), nameColumn);
            } catch (SQLException e) {
                showError("Database Error", "Failed to add new hosteller: " + e.getMessage());
            }
        }
    }

    private void editSelectedStudents() {
        filteredData.stream()
                .filter(Hosteller::isSelected)
                .forEach(hosteller -> {
                    hostelerTable.getSelectionModel().select(hosteller);
                    hostelerTable.scrollTo(hosteller);

                    try {
                        hostellerDAO.updateHosteller(hosteller);
                        hostelerTable.refresh();
                    } catch (SQLException e) {
                        showError("Database Error", "Failed to update hosteller: " + e.getMessage());
                    }
                });
    }


    private void deleteSelectedStudents() throws SQLException {
        long selectedCount = filteredData.stream().filter(Hosteller::isSelected).count();
        if (selectedCount == 0) {
            showError("No Selection", "No hosteller selected for deletion.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText(null);
        alert.setContentText(String.format("Are you sure you want to delete %d selected student(s)?", selectedCount));

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Get the list of selected hostellers
            List<Hosteller> selectedHostellers = filteredData.stream()
                    .filter(Hosteller::isSelected)
                    .collect(Collectors.toList());

            // Delete from database
            hostellerDAO.deleteHostellers(selectedHostellers);

            // Remove from observable list
            hostellerList.removeIf(Hosteller::isSelected);
            updateButtonStates();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadData() {
        try {
            hostellerList.clear(); // Clear the list to avoid duplicates
            List<Hosteller> hostellersFromDB = hostellerDAO.getAllHostellers();
            System.out.println("Loading data: " + hostellersFromDB.size() + " records found.");
            hostellerList.addAll(hostellersFromDB);
        } catch (SQLException e) {
            showError("Data Loading Error", "Failed to load hosteller data: " + e.getMessage());
        }
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
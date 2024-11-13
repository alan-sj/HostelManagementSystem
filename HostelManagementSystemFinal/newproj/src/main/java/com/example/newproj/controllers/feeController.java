package com.example.newproj.controllers;

import com.example.newproj.Main;
import com.example.newproj.Models.fee;
import com.example.newproj.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class feeController {

    @FXML
    private Label UpdatesLabel;

    @FXML
    private Button markPaidButton;

    @FXML
    private TextField markPaidTF;

    @FXML
    private TextField miscPriceTF;

    @FXML
    private Button particularStudentButton;

    @FXML
    private TextField particularStudentFeeTF;

    @FXML
    private TextField particularStudentIDTF;

    @FXML
    private TextField pricePerDayTF;

    @FXML
    private Button setNewFeeButton;

    @FXML
    private TableView<fee> unpaidTable;
    @FXML
    private TableColumn<fee, Integer> colID;
    @FXML
    private TableColumn<fee, String> colName;
    @FXML
    private TableColumn<fee, Integer> colFee;
    @FXML
    private TableColumn<fee, String> colDueDate;

    @FXML
    public void initialize() throws SQLException {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colID.setCellValueFactory(new PropertyValueFactory<>("admissionNo"));
        colFee.setCellValueFactory(new PropertyValueFactory<>("fees"));
        colDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        loadUnpaidStudents();
    }

    private void loadUnpaidStudents() throws SQLException {
        ObservableList<fee> students = FXCollections.observableArrayList();
        DatabaseUtil connectNow = new DatabaseUtil();
        Connection connectDB = connectNow.getConnection();

        String query = "SELECT name, admission_no, fees, due_date FROM hosteller WHERE fee_status = 'Not Paid'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String admissionNo = resultSet.getString("admission_no");
                int fees = resultSet.getInt("fees");
                String dueDate = resultSet.getString("due_date");

                students.add(new fee(name, admissionNo, fees, dueDate));
            }

            unpaidTable.setItems(students);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void markFeePaid(ActionEvent event) throws SQLException {
        DatabaseUtil connectNow = new DatabaseUtil();
        Connection connectDB = connectNow.getConnection();

        String connectQuery = "UPDATE hosteller SET fee_status = 'Paid' WHERE admission_no = '" + markPaidTF.getText() + "'";

        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(connectQuery);
            UpdatesLabel.setText("Fees marked as paid for student with ID: " + markPaidTF.getText());
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void setNewFees(ActionEvent event) throws SQLException {
        DatabaseUtil connectNow = new DatabaseUtil();
        Connection connectDB = connectNow.getConnection();
        int fees = Integer.parseInt(pricePerDayTF.getText()) * 30 + Integer.parseInt(miscPriceTF.getText());

        LocalDate currentDate = LocalDate.now();
        LocalDate newDueDate = currentDate.plusMonths(1);
        String formattedNewDueDate = newDueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));


        String connectQuery = "UPDATE hosteller SET fees = " + fees;
        String connectQuery2 = "UPDATE hosteller SET fee_status = 'Not Paid'";
        String connectQuery3 = "UPDATE hosteller SET due_date = '" + formattedNewDueDate + "'";

        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(connectQuery);
            statement.executeUpdate(connectQuery2);
            statement.executeUpdate(connectQuery3);
            UpdatesLabel.setText("Fees updated successfully for all students.");
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void setParticularFee(ActionEvent event) throws SQLException {
        DatabaseUtil connectNow = new DatabaseUtil();
        Connection connectDB = connectNow.getConnection();
        int fees = Integer.parseInt(particularStudentFeeTF.getText());

        LocalDate currentDate = LocalDate.now();
        LocalDate newDueDate = currentDate.plusMonths(1);
        String formattedNewDueDate = newDueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String connectQuery = "UPDATE hosteller SET fees = " + fees + " WHERE admission_no = '" + particularStudentIDTF.getText() + "'";
        String connectQuery2 = "UPDATE hosteller SET fee_status = 'Not Paid' WHERE admission_no = '" + particularStudentIDTF.getText() + "'";
        String connectQuery3 = "UPDATE hosteller SET due_date = '" + formattedNewDueDate + "' WHERE admission_no = '" + particularStudentIDTF.getText() + "'";

        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(connectQuery);
            statement.executeUpdate(connectQuery2);
            statement.executeUpdate(connectQuery3);
            UpdatesLabel.setText("Fees updated successfully for student with ID: " + particularStudentIDTF.getText());
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
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

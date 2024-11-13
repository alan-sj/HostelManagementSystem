package com.example.newproj.controllers;

import com.example.newproj.Main;
import com.example.newproj.util.DatabaseUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DashController {

    @FXML
    private PieChart attendancePieChart;

    @FXML
    private Text staffCountText;

    @FXML
    private Text studentCountText;


    @FXML
    public void initialize() throws SQLException {
        updateStudentCount();
        updateStaffCount();
        setupPieChart();
    }

    void updateStudentCount() throws SQLException {
        DatabaseUtil connectNow = new DatabaseUtil();
        Connection connectDB = connectNow.getConnection();

        String countStudents = "SELECT count(1) FROM hosteller";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(countStudents);

            while(queryResult.next()) {
                studentCountText.setText(String.valueOf(queryResult.getInt(1)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
    void updateStaffCount() throws SQLException {
        DatabaseUtil connectNow = new DatabaseUtil();
        Connection connectDB = connectNow.getConnection();

        String countStaff = "SELECT count(1) FROM hosteller";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(countStaff);

            while(queryResult.next()) {
                staffCountText.setText(String.valueOf(queryResult.getInt(1)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
    void setupPieChart() throws SQLException {
        DatabaseUtil connectNow = new DatabaseUtil();
        Connection connectDB = connectNow.getConnection();

        String totalStudentsQuery = "SELECT COUNT(*) as total FROM hosteller";
        String presentStudentsQuery = "SELECT COUNT(*) as present FROM hosteller WHERE present_status = 'Present'";

        try {
            Statement statement = connectDB.createStatement();

            ResultSet totalResultSet = statement.executeQuery(totalStudentsQuery);
            totalResultSet.next();
            int totalStudents = totalResultSet.getInt("total");

            ResultSet presentResultSet = statement.executeQuery(presentStudentsQuery);
            presentResultSet.next();
            int presentStudents = presentResultSet.getInt("present");

            int otherStudents = totalStudents - presentStudents;

            double presentPercentage = (double) presentStudents / totalStudents * 100;
            double otherPercentage = (double) otherStudents / totalStudents * 100;

            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                    new PieChart.Data("Present (" + String.format("%.2f", presentPercentage) + "%)", presentStudents),
                    new PieChart.Data("Absent (" + String.format("%.2f", otherPercentage) + "%)", otherStudents)
            );

            attendancePieChart.setData(pieChartData);
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
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


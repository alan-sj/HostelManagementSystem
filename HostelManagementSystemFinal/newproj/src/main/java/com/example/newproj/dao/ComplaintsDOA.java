package com.example.newproj.dao;

import com.example.newproj.util.DatabaseUtil;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.w3c.dom.Text;

import java.sql.*;

public class ComplaintsDOA{
    private Connection connection;

    public ComplaintsDOA() throws SQLException {
        this.connection = DatabaseUtil.getConnection();
    }

//    private void addComplaint(String complainer, String date, String description) {
//        // Create VBox for a single complaint
//        VBox complaintBox = new VBox(8); // 8 is spacing
//        complaintBox.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-background-radius: 5px; -fx-border-radius: 5px;");
//        complaintBox.setPadding(new Insets(8, 10, 8, 10));
//
//        // Create and add Text elements
//        Text complainerText = new Text("Complainer: " + complainer);
//        Text dateText = new Text("Date of Complain: " + date);
//        Text descriptionText = new Text(description);
//
//        // Create HBox for buttons
//        HBox buttonBox = new HBox(10); // 10 is spacing
//        Button deleteButton = new Button("DELETE");
//        deleteButton.setStyle("-fx-background-color: #FF4444; -fx-text-fill: white;");
//
//        // Action for delete button (if needed)
//        deleteButton.setOnAction(event -> complaintContainer.getChildren().remove(complaintBox));
//
//        Button resolvedButton = new Button("RESOLVED");
//        resolvedButton.setStyle("-fx-background-color: #f5f5f5; -fx-text-fill: #000000;");
//
//        buttonBox.getChildren().addAll(deleteButton, resolvedButton);
//
//        // Add all elements to the VBox
//        complaintBox.getChildren().addAll(complainerText, dateText, descriptionText, buttonBox);
//
//        // Add the complaint VBox to the main container
//        complaintContainer.getChildren().add(complaintBox);
//    }

    private boolean executeUpdateWithTransaction(PreparedStatementSetter setter, String query) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            setter.setValues(stmt);
            int rowsAffected = stmt.executeUpdate();
            connection.commit();
            return rowsAffected > 0;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private boolean executeBatchUpdateWithTransaction(PreparedStatementSetter setter, String query) throws SQLException {
        connection.setAutoCommit(false);
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            setter.setValues(stmt);
            int[] results = stmt.executeBatch();
            connection.commit();
            for (int result : results) {
                if (result != Statement.SUCCESS_NO_INFO && result <= 0) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    public void updateRoom(int admNo) throws SQLException{
        String query = "UPDATE students set roomNo = ? where admNo = ?";
        executeUpdateWithTransaction(stmt -> {
            stmt.setString(1, "selectedRoom");
            stmt.setInt(2, admNo);
        }, query);
    }

    @FunctionalInterface
    private interface PreparedStatementSetter {
        void setValues(PreparedStatement stmt) throws SQLException;
    }
}

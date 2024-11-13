package com.example.newproj.dao;

import com.example.newproj.controllers.AllotRoomsController;
import com.example.newproj.util.DatabaseUtil;
import com.example.newproj.Models.AllotRoomsModel;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AllotRoomsDOA {
    private Connection connection;
    public static String selectedRoom = "";
    private static List<AllotRoomsModel> roomList = new ArrayList<>();

    public AllotRoomsDOA() throws SQLException {
        this.connection = DatabaseUtil.getConnection();
    }

    public EventHandler<? super MouseEvent> onRoomClicked(String room_no) throws SQLException {
        if (selectedRoom != room_no){
            selectedRoom = room_no;
            showAlert("Room Selected", "Selected Room number: " + selectedRoom);
            System.out.println("Selected Room: " + selectedRoom);
        }
        return null;
    }

    private AnchorPane createRoomPane(String roomNumber) throws SQLException {
        AnchorPane roomPane = new AnchorPane();
        AllotRoomsController allotRoomsController = new AllotRoomsController();
        //roomPane.setPrefSize(200, 200);
        roomPane.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 20px;");
        roomPane.snappedBottomInset();
        roomPane.snappedLeftInset();
        roomPane.snappedRightInset();
        roomPane.snappedTopInset();
        roomPane.setOnMouseClicked(_ -> {
            try {
                onRoomClicked(roomNumber);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        VBox roomBox = new VBox();
        roomBox.setPrefSize(180, 100);
        roomBox.setAlignment(Pos.CENTER);

        Text roomText = new Text(roomNumber);
        roomText.setFont(new Font("Inter_FXH Regular", 18));
        roomBox.getChildren().add(roomText);

        roomPane.getChildren().add(roomBox);
        return roomPane;
    }

    public List<AllotRoomsModel> getRoomModels() throws SQLException {
        String query = "SELECT DISTINCT room_no FROM hosteller";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                AllotRoomsModel model = new AllotRoomsModel(
                        false,
                        rs.getString("room_no"),
                        createRoomPane(rs.getString("room_no"))
                );
                roomList.add(model);
            }
        }
        return roomList;
    }

    public String getStudentName(int admission_no) throws SQLException {
        String query = "SELECT name FROM hosteller WHERE admission_no = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, admission_no);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name"); // Return the student's name
                }
            }
            return "Student Not Found";
        }
    }

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

    public void updateRoom(int admission_no) throws SQLException{
        String query = "UPDATE hosteller set room_no = ? where admission_no = ?";
        executeUpdateWithTransaction(stmt -> {
            stmt.setString(1, selectedRoom);
            stmt.setInt(2, admission_no);
        }, query);
        showAlert("Room updated", "Room allocation for "+admission_no+" successful.\nRoom number: "+selectedRoom);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FunctionalInterface
    private interface PreparedStatementSetter {
        void setValues(PreparedStatement stmt) throws SQLException;
    }
}
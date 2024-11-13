package com.example.newproj.dao;

import com.example.newproj.util.DatabaseUtil;
import com.example.newproj.Models.AttendanceModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceDOA {
    private Connection connection;

    public AttendanceDOA() throws SQLException {
        this.connection = DatabaseUtil.getConnection();
    }

    // Retrieve all AttendanceModels from the database
    public List<AttendanceModel> getAllAttendanceModels() throws SQLException {
        List<AttendanceModel> AttendanceModels = new ArrayList<>();
        String query = "SELECT * FROM hosteller ORDER BY admission_no";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                AttendanceModel AttendanceModel = new AttendanceModel(
                        rs.getString("name"),
                        rs.getString("room_no"),
                        rs.getString("admission_no"),
                        rs.getInt("ndp")
                );
                AttendanceModels.add(AttendanceModel);
            }
        }
        return AttendanceModels;
    }

    public boolean updateDayCount(List<AttendanceModel> selected) throws SQLException {
        String query = "UPDATE hosteller set ndp = ? where admission_no = ?";
        return executeBatchUpdateWithTransaction(stmt -> {
            for (AttendanceModel hosteller : selected) {
                hosteller.setNoDaysPresent(hosteller.getNoDaysPresent() + 1);
                stmt.setInt(1, hosteller.getNoDaysPresent());
                stmt.setString(2, hosteller.getAdmissionNumber());
                stmt.addBatch();
            }
        }, query);
    }

    public void deselectAll(List<AttendanceModel> selected) throws SQLException {
        for (AttendanceModel attendance : selected) {
            attendance.setSelected(false);
        }
    }

    public boolean subCountAttendanceModel(AttendanceModel attendanceModel) throws SQLException {
        String query = "UPDATE hosteller SET ndp = ? WHERE admission_no = ?";
        int newDayCount = attendanceModel.getNoDaysPresent()-1;
        attendanceModel.setNoDaysPresent(newDayCount);
        return executeUpdateWithTransaction(stmt -> {
            stmt.setInt(1, attendanceModel.getNoDaysPresent());
            stmt.setString(2, attendanceModel.getAdmissionNumber());
        }, query);
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

    @FunctionalInterface
    private interface PreparedStatementSetter {
        void setValues(PreparedStatement stmt) throws SQLException;
    }
}

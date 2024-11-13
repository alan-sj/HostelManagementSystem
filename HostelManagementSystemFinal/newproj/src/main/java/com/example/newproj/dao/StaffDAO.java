package com.example.newproj.dao;

import com.example.newproj.Models.Staff;
import com.example.newproj.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {
    private final Connection connection;

    public StaffDAO() throws SQLException {
        this.connection = DatabaseUtil.getConnection();
    }

    // Retrieve all staff records from the database
    public List<Staff> getAllStaff() throws SQLException {
        List<Staff> staffList = new ArrayList<>();
        String query = "SELECT name, staff_id, role, salary, date_of_join, contact FROM staff ORDER BY staff_id";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Staff staff = new Staff(
                        rs.getString("name"),
                        rs.getString("staff_id"),
                        rs.getString("role"),
                        rs.getDouble("salary"),
                        rs.getString("date_of_join"),
                        rs.getInt("contact")
                );
                staffList.add(staff);
            }
        }
        return staffList;
    }

    // Add a new staff member to the database
    public boolean addStaff(Staff staff) throws SQLException {
        if (checkIfStaffExists(staff.getStaffId())) {
            throw new SQLException("Staff member with ID " + staff.getStaffId() + " already exists.");
        }

        String query = "INSERT INTO staff (name, staff_id, role, salary, date_of_join, contact) VALUES (?, ?, ?, ?, ?, ?)";
        return executeUpdateWithTransaction(stmt -> {
            stmt.setString(1, staff.getName());
            stmt.setString(2, staff.getStaffId());
            stmt.setString(3, staff.getRole());
            stmt.setDouble(4, staff.getSalary());
            stmt.setString(5, staff.getDateOfJoin());
            stmt.setInt(6, staff.getContact());
        }, query);
    }

    // Check if a staff member exists by staff ID
    public boolean checkIfStaffExists(String staffId) throws SQLException {
        String query = "SELECT COUNT(*) FROM staff WHERE staff_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, staffId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // Update an existing staff member in the database
    public boolean updateStaff(Staff staff) throws SQLException {
        if (!checkIfStaffExists(staff.getStaffId())) {
            throw new SQLException("No staff member found with ID " + staff.getStaffId());
        }

        String query = "UPDATE staff SET name = ?, role = ?, salary = ?, date_of_join = ?, contact = ? WHERE staff_id = ?";
        return executeUpdateWithTransaction(stmt -> {
            stmt.setString(1, staff.getName());
            stmt.setString(2, staff.getRole());
            stmt.setDouble(3, staff.getSalary());
            stmt.setString(4, staff.getDateOfJoin());
            stmt.setInt(5, staff.getContact());
            stmt.setString(6, staff.getStaffId());
        }, query);
    }

    // Delete a staff member from the database
    public void deleteStaff(String staffId) throws SQLException {
        String query = "DELETE FROM staff WHERE staff_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, staffId);
            pstmt.executeUpdate();
        }
    }

    // Utility method for executing an update within a transaction
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

    // Close database connection
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

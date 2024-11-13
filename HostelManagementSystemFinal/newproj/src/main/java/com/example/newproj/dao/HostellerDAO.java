package com.example.newproj.dao;

import com.example.newproj.Models.Hosteller;
import com.example.newproj.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HostellerDAO {
    private Connection connection;

    public HostellerDAO() throws SQLException {
        this.connection = DatabaseUtil.getConnection();
    }

    // Retrieve all hostellers from the database
    public List<Hosteller> getAllHostellers() throws SQLException {
        List<Hosteller> hostellers = new ArrayList<>();
        String query = "SELECT * FROM Hosteller ORDER BY admission_no";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Hosteller hosteller = new Hosteller(
                        rs.getString("name"),
                        rs.getInt("room_no"),
                        rs.getString("admission_no"),
                        rs.getString("date_of_admission"),
                        rs.getInt("contact")
                );
                hostellers.add(hosteller);
            }
        }
        return hostellers;

    }
    public boolean admissionNumberExists(String admissionNumber) throws SQLException {
        String query = "SELECT COUNT(*) FROM Hosteller WHERE admission_no = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, admissionNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0; // Returns true if exists
            }
        }
        return false;
    }

    public boolean hostellerExists(String admissionNumber) throws SQLException {
        String query = "SELECT COUNT(*) FROM Hosteller WHERE admission_no = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, admissionNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    public boolean checkIfHostellerExists(String admissionNumber) throws SQLException {
        String query = "SELECT COUNT(*) FROM Hosteller WHERE admission_no = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, admissionNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }


    // Add a new hosteller to the database
    public boolean addHosteller(Hosteller hosteller) throws SQLException {
        if (isAdmissionNumberExists(hosteller.getAdmissionNumber())) {
            throw new SQLException("A student with admission number " + hosteller.getAdmissionNumber() + " already exists.");
        }

        String query = "INSERT INTO Hosteller (name, room_no, admission_no, date_of_admission, contact) VALUES (?, ?, ?, ?, ?)";
        return executeUpdateWithTransaction(stmt -> {
            stmt.setString(1, hosteller.getName());
            stmt.setInt(2, hosteller.getRoomNo());
            stmt.setString(3, hosteller.getAdmissionNumber());
            stmt.setString(4, hosteller.getDateOfAdmission());
            stmt.setInt(5, hosteller.getContact());
        }, query);
    }

    // Check if admission number already exists
    private boolean isAdmissionNumberExists(String admissionNumber) throws SQLException {
        String query = "SELECT COUNT(*) FROM Hosteller WHERE admission_no = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, admissionNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private boolean isAdmissionNumberExistsInOtherRecords(String admissionNumber) throws SQLException {
        String query = "SELECT COUNT(*) FROM Hosteller WHERE admission_no = ? AND admission_no != ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, admissionNumber);
            stmt.setString(2, admissionNumber); // Exclude the current record in the query
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    // Update an existing hosteller in the database
    public boolean updateHosteller(Hosteller hosteller) throws SQLException {
        if (isAdmissionNumberExistsInOtherRecords(hosteller.getAdmissionNumber())) {
            throw new SQLException("A student with admission number " + hosteller.getAdmissionNumber() + " already exists.");
        }

        String query = "UPDATE Hosteller SET name = ?, room_no = ?, date_of_admission = ?, contact = ? WHERE admission_no = ?";
        return executeUpdateWithTransaction(stmt -> {
            stmt.setString(1, hosteller.getName());
            stmt.setInt(2, hosteller.getRoomNo());
            stmt.setString(3, hosteller.getDateOfAdmission());
            stmt.setInt(4, hosteller.getContact());
            stmt.setString(5, hosteller.getAdmissionNumber());
        }, query);
    }

    // Delete selected hostellers from the database
    public boolean deleteHostellers(List<Hosteller> hostellers) throws SQLException {
        String query = "DELETE FROM Hosteller WHERE admission_no = ?";
        return executeBatchUpdateWithTransaction(stmt -> {
            for (Hosteller hosteller : hostellers) {
                stmt.setString(1, hosteller.getAdmissionNumber());
                stmt.addBatch();
            }
        }, query);
    }

    // Delete a single hosteller from the database
    public boolean deleteHosteller(String admissionNumber) throws SQLException {
        String query = "DELETE FROM Hosteller WHERE admission_no = ?";
        return executeUpdateWithTransaction(stmt -> {
            stmt.setString(1, admissionNumber);
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
package com.company.employee.dao;

import com.company.employee.model.Performance;
import com.company.employee.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class JdbcPerformanceDao implements PerformanceDao {

    @Override
    public Optional<Performance> findByEmployeeId(int empId) {
        String sql = "SELECT emp_id, score FROM Performance WHERE emp_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Performance(rs.getInt("emp_id"), rs.getInt("score")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching performance score: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean upsertScore(int empId, int score) {
        String sql = "INSERT INTO Performance(emp_id, score) VALUES(?, ?) " +
                "ON DUPLICATE KEY UPDATE score = VALUES(score)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            ps.setInt(2, score);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating performance score: " + e.getMessage());
            return false;
        }
    }
}

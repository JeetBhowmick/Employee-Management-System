package com.company.employee.dao;

import com.company.employee.model.LeaveRequest;
import com.company.employee.model.LeaveStatus;
import com.company.employee.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcLeaveDao implements LeaveDao {

    @Override
    public boolean applyLeave(int empId, int days) {
        String sql = "INSERT INTO Leaves(emp_id, days, status) VALUES(?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            ps.setInt(2, days);
            ps.setString(3, LeaveStatus.PENDING.name());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error applying leave: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<LeaveRequest> findByEmployeeId(int empId) {
        List<LeaveRequest> leaves = new ArrayList<>();
        String sql = "SELECT id, emp_id, days, status FROM Leaves WHERE emp_id = ? ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    leaves.add(mapLeave(rs));
                }
            }
        } catch (SQLException | IllegalArgumentException e) {
            System.err.println("Error fetching leave requests: " + e.getMessage());
        }
        return leaves;
    }

    @Override
    public List<LeaveRequest> findAll() {
        List<LeaveRequest> leaves = new ArrayList<>();
        String sql = "SELECT id, emp_id, days, status FROM Leaves ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                leaves.add(mapLeave(rs));
            }
        } catch (SQLException | IllegalArgumentException e) {
            System.err.println("Error fetching all leaves: " + e.getMessage());
        }
        return leaves;
    }

    @Override
    public boolean updateLeaveStatus(int leaveId, LeaveStatus status) {
        String sql = "UPDATE Leaves SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, leaveId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating leave status: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int countPendingLeaves() {
        String sql = "SELECT COUNT(*) FROM Leaves WHERE status = 'PENDING'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting pending leaves: " + e.getMessage());
        }
        return 0;
    }

    private LeaveRequest mapLeave(ResultSet rs) throws SQLException {
        return new LeaveRequest(
                rs.getInt("id"),
                rs.getInt("emp_id"),
                rs.getInt("days"),
                LeaveStatus.valueOf(rs.getString("status").toUpperCase())
        );
    }
}

package com.company.employee.dao;

import com.company.employee.model.Task;
import com.company.employee.model.TaskPriority;
import com.company.employee.model.TaskStatus;
import com.company.employee.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTaskDao implements TaskDao {

    @Override
    public List<Task> findByEmployeeId(int empId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, emp_id, task_name, priority, status FROM Tasks WHERE emp_id = ? ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapTask(rs));
                }
            }
        } catch (SQLException | IllegalArgumentException e) {
            System.err.println("Error fetching tasks: " + e.getMessage());
        }
        return tasks;
    }

    @Override
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, emp_id, task_name, priority, status FROM Tasks ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tasks.add(mapTask(rs));
            }
        } catch (SQLException | IllegalArgumentException e) {
            System.err.println("Error fetching all tasks: " + e.getMessage());
        }
        return tasks;
    }

    @Override
    public boolean assignTask(int empId, String taskName, TaskPriority priority) {
        String sql = "INSERT INTO Tasks(emp_id, task_name, priority, status) VALUES(?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            ps.setString(2, taskName);
            ps.setString(3, priority.name());
            ps.setString(4, TaskStatus.PENDING.name());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error assigning task: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateStatus(int taskId, TaskStatus status) {
        String sql = "UPDATE Tasks SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, taskId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating task status: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int countCompletedTasks() {
        String sql = "SELECT COUNT(*) FROM Tasks WHERE status = 'COMPLETED'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting completed tasks: " + e.getMessage());
        }
        return 0;
    }

    @Override
    public int countCompletedTasksByEmployee(int empId) {
        String sql = "SELECT COUNT(*) FROM Tasks WHERE emp_id = ? AND status = 'COMPLETED'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, empId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting employee completed tasks: " + e.getMessage());
        }
        return 0;
    }

    private Task mapTask(ResultSet rs) throws SQLException {
        return new Task(
                rs.getInt("id"),
                rs.getInt("emp_id"),
                rs.getString("task_name"),
                TaskPriority.valueOf(rs.getString("priority").toUpperCase()),
                TaskStatus.valueOf(rs.getString("status").toUpperCase())
        );
    }
}

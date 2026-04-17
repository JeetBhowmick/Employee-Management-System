package com.company.employee.dao;

import com.company.employee.model.Employee;
import com.company.employee.model.Role;
import com.company.employee.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcEmployeeDao implements EmployeeDao {

    @Override
    public Optional<Employee> findById(int id) {
        String sql = "SELECT id, name, role FROM Employees WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Employee employee = new Employee(
                            rs.getInt("id"),
                            rs.getString("name"),
                            Role.valueOf(rs.getString("role").toUpperCase())
                    );
                    return Optional.of(employee);
                }
            }
        } catch (SQLException | IllegalArgumentException e) {
            System.err.println("Error fetching employee: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT id, name, role FROM Employees ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        Role.valueOf(rs.getString("role").toUpperCase())
                ));
            }
        } catch (SQLException | IllegalArgumentException e) {
            System.err.println("Error fetching employee list: " + e.getMessage());
        }
        return employees;
    }

    @Override
    public boolean addEmployee(String name, Role role) {
        String sql = "INSERT INTO Employees(name, role) VALUES(?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, role.name());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding employee: " + e.getMessage());
            return false;
        }
    }

    @Override
    public int countEmployees() {
        String sql = "SELECT COUNT(*) FROM Employees";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting employees: " + e.getMessage());
        }
        return 0;
    }
}

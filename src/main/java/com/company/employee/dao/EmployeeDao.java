package com.company.employee.dao;

import com.company.employee.model.Employee;
import com.company.employee.model.Role;

import java.util.List;
import java.util.Optional;

public interface EmployeeDao {
    Optional<Employee> findById(int id);
    List<Employee> findAll();
    boolean addEmployee(String name, Role role);
    int countEmployees();
}

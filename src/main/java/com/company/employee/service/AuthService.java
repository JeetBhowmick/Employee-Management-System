package com.company.employee.service;

import com.company.employee.dao.EmployeeDao;
import com.company.employee.model.Employee;

import java.util.Optional;

public class AuthService {
    private final EmployeeDao employeeDao;

    public AuthService(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    public Optional<Employee> login(int employeeId) {
        return employeeDao.findById(employeeId);
    }
}

package com.company.employee.dao;

import com.company.employee.model.Performance;

import java.util.Optional;

public interface PerformanceDao {
    Optional<Performance> findByEmployeeId(int empId);
    boolean upsertScore(int empId, int score);
}

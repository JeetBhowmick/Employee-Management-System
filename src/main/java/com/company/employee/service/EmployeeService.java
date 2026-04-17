package com.company.employee.service;

import com.company.employee.dao.LeaveDao;
import com.company.employee.dao.PerformanceDao;
import com.company.employee.dao.TaskDao;
import com.company.employee.model.LeaveRequest;
import com.company.employee.model.Performance;
import com.company.employee.model.Task;

import java.util.List;
import java.util.Optional;

public class EmployeeService {
    private final TaskDao taskDao;
    private final LeaveDao leaveDao;
    private final PerformanceDao performanceDao;
    private final PerformanceService performanceService;

    public EmployeeService(TaskDao taskDao, LeaveDao leaveDao, PerformanceDao performanceDao, PerformanceService performanceService) {
        this.taskDao = taskDao;
        this.leaveDao = leaveDao;
        this.performanceDao = performanceDao;
        this.performanceService = performanceService;
    }

    public List<Task> viewAssignedTasks(int empId) {
        return taskDao.findByEmployeeId(empId);
    }

    public boolean applyLeave(int empId, int days) {
        if (days <= 0) {
            return false;
        }
        return leaveDao.applyLeave(empId, days);
    }

    public List<LeaveRequest> viewLeaveStatus(int empId) {
        return leaveDao.findByEmployeeId(empId);
    }

    public int viewPerformanceScore(int empId) {
        performanceService.refreshEmployeeScore(empId);
        Optional<Performance> performance = performanceDao.findByEmployeeId(empId);
        return performance.map(Performance::getScore).orElse(0);
    }
}

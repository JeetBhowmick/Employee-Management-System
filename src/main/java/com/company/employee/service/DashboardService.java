package com.company.employee.service;

import com.company.employee.dao.EmployeeDao;
import com.company.employee.dao.LeaveDao;
import com.company.employee.dao.TaskDao;

import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardService {
    private final EmployeeDao employeeDao;
    private final LeaveDao leaveDao;
    private final TaskDao taskDao;

    public DashboardService(EmployeeDao employeeDao, LeaveDao leaveDao, TaskDao taskDao) {
        this.employeeDao = employeeDao;
        this.leaveDao = leaveDao;
        this.taskDao = taskDao;
    }

    public Map<String, Integer> getAdminDashboardStats() {
        Map<String, Integer> stats = new LinkedHashMap<>();
        stats.put("Total Employees", employeeDao.countEmployees());
        stats.put("Pending Leaves", leaveDao.countPendingLeaves());
        stats.put("Completed Tasks", taskDao.countCompletedTasks());
        return stats;
    }
}

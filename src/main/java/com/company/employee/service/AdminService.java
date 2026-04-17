package com.company.employee.service;

import com.company.employee.dao.EmployeeDao;
import com.company.employee.dao.LeaveDao;
import com.company.employee.dao.TaskDao;
import com.company.employee.model.Employee;
import com.company.employee.model.LeaveRequest;
import com.company.employee.model.LeaveStatus;
import com.company.employee.model.Role;
import com.company.employee.model.Task;
import com.company.employee.model.TaskPriority;
import com.company.employee.model.TaskStatus;

import java.util.List;

public class AdminService {
    private final EmployeeDao employeeDao;
    private final TaskDao taskDao;
    private final LeaveDao leaveDao;
    private final PerformanceService performanceService;

    public AdminService(EmployeeDao employeeDao, TaskDao taskDao, LeaveDao leaveDao, PerformanceService performanceService) {
        this.employeeDao = employeeDao;
        this.taskDao = taskDao;
        this.leaveDao = leaveDao;
        this.performanceService = performanceService;
    }

    public boolean addEmployee(String name, Role role) {
        if (name == null || name.isBlank()) {
            return false;
        }
        return employeeDao.addEmployee(name.trim(), role);
    }

    public boolean assignTask(int empId, String taskName, TaskPriority priority) {
        if (taskName == null || taskName.isBlank()) {
            return false;
        }
        return taskDao.assignTask(empId, taskName.trim(), priority);
    }

    public boolean updateTaskStatus(int taskId, TaskStatus status) {
        boolean updated = taskDao.updateStatus(taskId, status);
        if (updated) {
            Task task = taskDao.findAll().stream().filter(t -> t.getId() == taskId).findFirst().orElse(null);
            if (task != null) {
                performanceService.refreshEmployeeScore(task.getEmpId());
            }
        }
        return updated;
    }

    public boolean approveOrRejectLeave(int leaveId, LeaveStatus status) {
        return leaveDao.updateLeaveStatus(leaveId, status);
    }

    public List<Employee> viewAllEmployees() {
        return employeeDao.findAll();
    }

    public List<Task> viewAllTasks() {
        return taskDao.findAll();
    }

    public List<LeaveRequest> viewAllLeaves() {
        return leaveDao.findAll();
    }
}

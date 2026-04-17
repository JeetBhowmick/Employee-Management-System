package com.company.employee.dao;

import com.company.employee.model.Task;
import com.company.employee.model.TaskPriority;
import com.company.employee.model.TaskStatus;

import java.util.List;

public interface TaskDao {
    List<Task> findByEmployeeId(int empId);
    List<Task> findAll();
    boolean assignTask(int empId, String taskName, TaskPriority priority);
    boolean updateStatus(int taskId, TaskStatus status);
    int countCompletedTasks();
    int countCompletedTasksByEmployee(int empId);
}

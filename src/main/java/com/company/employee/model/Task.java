package com.company.employee.model;

public class Task {
    private int id;
    private int empId;
    private String taskName;
    private TaskPriority priority;
    private TaskStatus status;

    public Task() {
    }

    public Task(int id, int empId, String taskName, TaskPriority priority, TaskStatus status) {
        this.id = id;
        this.empId = empId;
        this.taskName = taskName;
        this.priority = priority;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getEmpId() {
        return empId;
    }

    public String getTaskName() {
        return taskName;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public TaskStatus getStatus() {
        return status;
    }
}

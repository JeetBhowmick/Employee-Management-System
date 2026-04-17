package com.company.employee.model;

public class LeaveRequest {
    private int id;
    private int empId;
    private int days;
    private LeaveStatus status;

    public LeaveRequest(int id, int empId, int days, LeaveStatus status) {
        this.id = id;
        this.empId = empId;
        this.days = days;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getEmpId() {
        return empId;
    }

    public int getDays() {
        return days;
    }

    public LeaveStatus getStatus() {
        return status;
    }
}

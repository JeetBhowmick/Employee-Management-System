package com.company.employee.dao;

import com.company.employee.model.LeaveRequest;
import com.company.employee.model.LeaveStatus;

import java.util.List;

public interface LeaveDao {
    boolean applyLeave(int empId, int days);
    List<LeaveRequest> findByEmployeeId(int empId);
    List<LeaveRequest> findAll();
    boolean updateLeaveStatus(int leaveId, LeaveStatus status);
    int countPendingLeaves();
}

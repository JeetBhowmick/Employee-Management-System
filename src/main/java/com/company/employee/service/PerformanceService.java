package com.company.employee.service;

import com.company.employee.dao.PerformanceDao;
import com.company.employee.dao.TaskDao;

public class PerformanceService {
    private static final int POINTS_PER_COMPLETED_TASK = 10;

    private final TaskDao taskDao;
    private final PerformanceDao performanceDao;

    public PerformanceService(TaskDao taskDao, PerformanceDao performanceDao) {
        this.taskDao = taskDao;
        this.performanceDao = performanceDao;
    }

    public int refreshEmployeeScore(int empId) {
        int completedTasks = taskDao.countCompletedTasksByEmployee(empId);
        int score = completedTasks * POINTS_PER_COMPLETED_TASK;
        performanceDao.upsertScore(empId, score);
        return score;
    }
}

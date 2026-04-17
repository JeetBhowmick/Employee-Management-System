package com.company.employee.model;

public class Performance {
    private int empId;
    private int score;

    public Performance(int empId, int score) {
        this.empId = empId;
        this.score = score;
    }

    public int getEmpId() {
        return empId;
    }

    public int getScore() {
        return score;
    }
}

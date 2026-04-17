CREATE DATABASE IF NOT EXISTS employee_db;
USE employee_db;

CREATE TABLE IF NOT EXISTS Employees (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS Tasks (
    id INT PRIMARY KEY AUTO_INCREMENT,
    emp_id INT NOT NULL,
    task_name VARCHAR(255) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_tasks_employee FOREIGN KEY (emp_id) REFERENCES Employees(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Leaves (
    id INT PRIMARY KEY AUTO_INCREMENT,
    emp_id INT NOT NULL,
    days INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_leaves_employee FOREIGN KEY (emp_id) REFERENCES Employees(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Performance (
    emp_id INT PRIMARY KEY,
    score INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_performance_employee FOREIGN KEY (emp_id) REFERENCES Employees(id) ON DELETE CASCADE
);

INSERT INTO Employees(name, role) VALUES
('System Admin', 'ADMIN'),
('Alice Johnson', 'EMPLOYEE'),
('Bob Smith', 'EMPLOYEE');

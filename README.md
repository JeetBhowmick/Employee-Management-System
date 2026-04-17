# Employee Task, Leave & Performance Management System

## Overview

A Java-based backend system designed to manage employee workflows including task assignment, leave management, and performance tracking. The system follows a layered architecture and simulates real-world enterprise backend operations.

## Features

* Role-based access (Admin / Employee)
* Task assignment with priority levels
* Leave request and approval workflow
* Task status tracking (Pending, In Progress, Completed)
* Performance scoring based on task completion
* Dashboard insights (employees, tasks, leaves)

## Architecture

UI Layer → Service Layer → DAO Layer → MySQL Database

## Tech Stack

* Java (Core)
* JDBC
* MySQL
* Maven

## Database Setup

Run the following SQL:

CREATE DATABASE employee_db;

CREATE TABLE employees(
id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(50),
role VARCHAR(20)
);

CREATE TABLE tasks(
id INT AUTO_INCREMENT PRIMARY KEY,
emp_id INT,
task_name VARCHAR(100),
priority VARCHAR(10),
status VARCHAR(20)
);

CREATE TABLE leaves(
id INT AUTO_INCREMENT PRIMARY KEY,
emp_id INT,
days INT,
status VARCHAR(20)
);
##workflow
1. Login as Admin
2. Add Employee
3. Assign Task
4. Approve Leave
   
## How to Run

1. Clone repository
2. Configure MySQL credentials in code
3. Run Main.java
4. Use console menu

## Sample Output

(Admin assigns task → Employee completes → Performance updates)

## Future Improvements

* REST API version
* Web-based frontend
* Authentication system

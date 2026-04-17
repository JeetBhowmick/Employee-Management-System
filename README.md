# Employee Task, Leave, and Performance Management System

A professional Java console-based backend mini-system demonstrating layered architecture, JDBC + MySQL integration, and business workflows for role-based employee and admin operations.

## Tech Stack
- Core Java (JDK 17)
- JDBC (`PreparedStatement` + `ResultSet`)
- MySQL (`employee_db`)
- Maven

## Project Architecture
```
src/main/java/com/company/employee
├── dao        # Database operations (JDBC implementations)
├── model      # Entities and enums
├── service    # Business logic and workflows
├── ui         # Menu-driven console interface
└── util       # Shared utilities (DB connection, table printer)
```

## Core Features
### Employee Module
- View assigned tasks
- Apply for leave
- View leave status
- View performance score

### Admin Module
- Add employee
- Assign tasks with priority (`HIGH`, `MEDIUM`, `LOW`)
- Update task status (`PENDING`, `IN_PROGRESS`, `COMPLETED`)
- Approve/reject leave requests (`PENDING -> APPROVED/REJECTED`)
- View all employees, tasks, and leave requests
- Dashboard statistics:
  - Total employees
  - Pending leaves
  - Completed tasks

## Role-Based Login
- Login by Employee ID.
- Employees with role `ADMIN` enter admin menu.
- Employees with role `EMPLOYEE` enter employee menu.

## Performance Scoring Rule
- Score = `completed_tasks * 10`
- Score is recomputed and upserted in `Performance` table.

## Database Setup
1. Ensure MySQL is running.
2. Execute:
   ```sql
   source src/main/resources/schema.sql;
   ```

## Environment Variables (Optional)
Defaults are used if not provided.
- `DB_URL` (default: `jdbc:mysql://localhost:3306/employee_db`)
- `DB_USER` (default: `root`)
- `DB_PASSWORD` (default: `root`)

## Build & Run
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass=com.company.employee.ui.EmployeeManagementApp
```

## Notes
- Console output uses formatted table rendering for better readability.
- Exception handling is included across DAO operations with meaningful error logs.
- Designed to stay modular and enterprise-like without unnecessary complexity.



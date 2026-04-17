package com.company.employee.ui;

import com.company.employee.dao.EmployeeDao;
import com.company.employee.dao.JdbcEmployeeDao;
import com.company.employee.dao.JdbcLeaveDao;
import com.company.employee.dao.JdbcPerformanceDao;
import com.company.employee.dao.JdbcTaskDao;
import com.company.employee.dao.LeaveDao;
import com.company.employee.dao.PerformanceDao;
import com.company.employee.dao.TaskDao;
import com.company.employee.model.Employee;
import com.company.employee.model.LeaveRequest;
import com.company.employee.model.LeaveStatus;
import com.company.employee.model.Role;
import com.company.employee.model.Task;
import com.company.employee.model.TaskPriority;
import com.company.employee.model.TaskStatus;
import com.company.employee.service.AdminService;
import com.company.employee.service.AuthService;
import com.company.employee.service.DashboardService;
import com.company.employee.service.EmployeeService;
import com.company.employee.service.PerformanceService;
import com.company.employee.util.ConsoleTablePrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class EmployeeManagementApp {
    private final Scanner scanner = new Scanner(System.in);

    private final EmployeeDao employeeDao = new JdbcEmployeeDao();
    private final TaskDao taskDao = new JdbcTaskDao();
    private final LeaveDao leaveDao = new JdbcLeaveDao();
    private final PerformanceDao performanceDao = new JdbcPerformanceDao();

    private final PerformanceService performanceService = new PerformanceService(taskDao, performanceDao);
    private final AuthService authService = new AuthService(employeeDao);
    private final EmployeeService employeeService = new EmployeeService(taskDao, leaveDao, performanceDao, performanceService);
    private final AdminService adminService = new AdminService(employeeDao, taskDao, leaveDao, performanceService);
    private final DashboardService dashboardService = new DashboardService(employeeDao, leaveDao, taskDao);

    public static void main(String[] args) {
        new EmployeeManagementApp().start();
    }

    private void start() {
        System.out.println("==============================================");
        System.out.println(" Employee Task, Leave & Performance System");
        System.out.println("==============================================");

        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Exit");
            int choice = readInt("Select option: ");

            switch (choice) {
                case 1 -> loginFlow();
                case 2 -> {
                    System.out.println("Thank you. Exiting system.");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void loginFlow() {
        int id = readInt("Enter Employee ID: ");
        Optional<Employee> employeeOpt = authService.login(id);

        if (employeeOpt.isEmpty()) {
            System.out.println("Login failed: Employee not found.");
            return;
        }

        Employee employee = employeeOpt.get();
        System.out.println("Welcome, " + employee.getName() + " (" + employee.getRole() + ")");

        if (employee.getRole() == Role.ADMIN) {
            adminMenu();
        } else {
            employeeMenu(employee.getId());
        }
    }

    private void adminMenu() {
        while (true) {
            System.out.println("\n=== Admin Menu ===");
            System.out.println("1. Add Employee");
            System.out.println("2. Assign Task");
            System.out.println("3. Update Task Status");
            System.out.println("4. Approve/Reject Leave");
            System.out.println("5. View All Employees");
            System.out.println("6. View All Tasks");
            System.out.println("7. View All Leaves");
            System.out.println("8. Dashboard Statistics");
            System.out.println("9. Logout");

            int choice = readInt("Choose option: ");
            switch (choice) {
                case 1 -> addEmployeeFlow();
                case 2 -> assignTaskFlow();
                case 3 -> updateTaskStatusFlow();
                case 4 -> manageLeaveFlow();
                case 5 -> printEmployees(adminService.viewAllEmployees());
                case 6 -> printTasks(adminService.viewAllTasks());
                case 7 -> printLeaves(adminService.viewAllLeaves());
                case 8 -> printDashboard();
                case 9 -> {
                    System.out.println("Logging out from admin session.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void employeeMenu(int empId) {
        while (true) {
            System.out.println("\n=== Employee Menu ===");
            System.out.println("1. View Assigned Tasks");
            System.out.println("2. Apply Leave");
            System.out.println("3. View Leave Status");
            System.out.println("4. View Performance Score");
            System.out.println("5. Logout");

            int choice = readInt("Choose option: ");
            switch (choice) {
                case 1 -> printTasks(employeeService.viewAssignedTasks(empId));
                case 2 -> applyLeaveFlow(empId);
                case 3 -> printLeaves(employeeService.viewLeaveStatus(empId));
                case 4 -> System.out.println("Current Performance Score: " + employeeService.viewPerformanceScore(empId));
                case 5 -> {
                    System.out.println("Logging out from employee session.");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addEmployeeFlow() {
        System.out.print("Enter employee name: ");
        String name = scanner.nextLine();
        Role role = readRole();

        boolean success = adminService.addEmployee(name, role);
        System.out.println(success ? "Employee added successfully." : "Failed to add employee.");
    }

    private void assignTaskFlow() {
        int empId = readInt("Enter employee ID for task assignment: ");
        System.out.print("Enter task name: ");
        String taskName = scanner.nextLine();
        TaskPriority priority = readPriority();

        boolean success = adminService.assignTask(empId, taskName, priority);
        System.out.println(success ? "Task assigned successfully." : "Failed to assign task.");
    }

    private void updateTaskStatusFlow() {
        int taskId = readInt("Enter task ID: ");
        TaskStatus status = readTaskStatus();

        boolean success = adminService.updateTaskStatus(taskId, status);
        System.out.println(success ? "Task status updated successfully." : "Task update failed.");
    }

    private void manageLeaveFlow() {
        int leaveId = readInt("Enter leave request ID: ");
        LeaveStatus status = readLeaveDecision();

        boolean success = adminService.approveOrRejectLeave(leaveId, status);
        System.out.println(success ? "Leave request updated." : "Leave update failed.");
    }

    private void applyLeaveFlow(int empId) {
        int days = readInt("Enter number of leave days: ");
        boolean success = employeeService.applyLeave(empId, days);
        System.out.println(success ? "Leave request submitted with status PENDING." : "Failed: Enter valid leave days.");
    }

    private void printEmployees(List<Employee> employees) {
        List<String[]> rows = new ArrayList<>();
        for (Employee employee : employees) {
            rows.add(new String[]{
                    String.valueOf(employee.getId()),
                    employee.getName(),
                    employee.getRole().name()
            });
        }
        ConsoleTablePrinter.printTable("Employees", new String[]{"ID", "Name", "Role"}, rows);
    }

    private void printTasks(List<Task> tasks) {
        List<String[]> rows = new ArrayList<>();
        for (Task task : tasks) {
            rows.add(new String[]{
                    String.valueOf(task.getId()),
                    String.valueOf(task.getEmpId()),
                    task.getTaskName(),
                    task.getPriority().name(),
                    task.getStatus().name()
            });
        }
        ConsoleTablePrinter.printTable("Tasks", new String[]{"Task ID", "Emp ID", "Task Name", "Priority", "Status"}, rows);
    }

    private void printLeaves(List<LeaveRequest> leaves) {
        List<String[]> rows = new ArrayList<>();
        for (LeaveRequest leaveRequest : leaves) {
            rows.add(new String[]{
                    String.valueOf(leaveRequest.getId()),
                    String.valueOf(leaveRequest.getEmpId()),
                    String.valueOf(leaveRequest.getDays()),
                    leaveRequest.getStatus().name()
            });
        }
        ConsoleTablePrinter.printTable("Leave Requests", new String[]{"Leave ID", "Emp ID", "Days", "Status"}, rows);
    }

    private void printDashboard() {
        Map<String, Integer> stats = dashboardService.getAdminDashboardStats();
        List<String[]> rows = new ArrayList<>();
        for (Map.Entry<String, Integer> stat : stats.entrySet()) {
            rows.add(new String[]{stat.getKey(), String.valueOf(stat.getValue())});
        }
        ConsoleTablePrinter.printTable("Admin Dashboard", new String[]{"Metric", "Value"}, rows);
    }

    private Role readRole() {
        while (true) {
            System.out.print("Enter role (ADMIN/EMPLOYEE): ");
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                return Role.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid role. Allowed: ADMIN, EMPLOYEE.");
            }
        }
    }

    private TaskPriority readPriority() {
        while (true) {
            System.out.print("Enter priority (HIGH/MEDIUM/LOW): ");
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                return TaskPriority.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid priority. Allowed: HIGH, MEDIUM, LOW.");
            }
        }
    }

    private TaskStatus readTaskStatus() {
        while (true) {
            System.out.print("Enter status (PENDING/IN_PROGRESS/COMPLETED): ");
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                return TaskStatus.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid status. Allowed: PENDING, IN_PROGRESS, COMPLETED.");
            }
        }
    }

    private LeaveStatus readLeaveDecision() {
        while (true) {
            System.out.print("Enter leave decision (APPROVED/REJECTED): ");
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                LeaveStatus status = LeaveStatus.valueOf(input);
                if (status == LeaveStatus.PENDING) {
                    System.out.println("Decision cannot be PENDING.");
                    continue;
                }
                return status;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid decision. Allowed: APPROVED, REJECTED.");
            }
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}

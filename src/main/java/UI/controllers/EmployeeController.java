package UI.controllers;

import data.models.*;
import services.contracts.IEmployeeService;
import services.contracts.IPrintHouseService;
import services.contracts.IPrintingPressService;
import utilities.EgnValidator;
import utilities.exceptions.InvalidEmployeeException;
import utilities.globalconstants.ExceptionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Handles console interactions for managing {@link Employee} entities within a {@link PrintHouse}.
 */
public class EmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
    private final IEmployeeService employeeService;
    private final IPrintHouseService printHouseService;
    private final IPrintingPressService printingPressService;
    private final Scanner scanner;

    public EmployeeController(IEmployeeService employeeService,
                              IPrintHouseService printHouseService, IPrintingPressService printingPressService, Scanner scanner) {
        if (employeeService == null || printHouseService == null || printingPressService == null || scanner == null) {
            logger.error("Dependencies cannot be null");
            throw new IllegalArgumentException(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL);
        }
        this.scanner = scanner;
        this.employeeService = employeeService;
        this.printHouseService = printHouseService;
        this.printingPressService = printingPressService;
        logger.info("EmployeeController initialized");
    }

    public void handleMenu() {
        logger.info("Starting employee management menu");
        while (true) {
            displayMenu();
            int choice = getUserChoice();
            if (choice == 0) break;
            processChoice(choice);
        }
        logger.info("Exiting employee management menu");
    }

    private void displayMenu() {
        System.out.println("\n--- Employee Management ---");
        System.out.println("1. Add an employee");
        System.out.println("2. Remove an employee");
        System.out.println("3. Update an employee");
        System.out.println("4. Calculate total employee cost");
        System.out.println("5. List all employees");
        System.out.println("0. Back to main menu");
        System.out.print("Enter your choice: ");
        logger.debug("Displayed employee menu");
    }

    private int getUserChoice() {
        try {
            if (!scanner.hasNextLine()) {
                logger.warn("No input available");
                System.out.println("Input error. Please try again.");
                return -1;
            }
            int choice = Integer.parseInt(scanner.nextLine().trim());
            logger.debug("User choice: {}", choice);
            return choice;
        } catch (NumberFormatException e) {
            logger.warn("Invalid input: {}", e.getMessage());
            System.out.println("Invalid input. Please enter a number.");
            return -1;
        } catch (NoSuchElementException e) {
            logger.error("Failed to read input: {}", e.getMessage(), e);
            System.out.println("Input error. Please try again.");
            return -1;
        }
    }

    private void processChoice(int choice) {
        try {
            switch (choice) {
                case 1 -> addEmployee();
                case 2 -> removeEmployee();
                case 3 -> updateEmployee();
                case 4 -> calculateTotalEmployeeCost();
                case 5 -> listAllEmployees();
                default -> {
                    logger.warn("Invalid choice: {}", choice);
                    System.out.println("Invalid choice.");
                }
            }
        } catch (Exception e) {
            logger.error("Error in choice {}: {}", choice, e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addEmployee() {
        PrintHouse printHouse = selectPrintHouse();
        if (printHouse == null) return;
        System.out.print("Enter employee EGN (10 digits): ");
        String egn = scanner.nextLine().trim();

        if (!EgnValidator.isValidEGN(egn)){
            logger.error("Invalid EGN: {}", egn);
            System.out.println("Invalid EGN");
            return;
        }

        String typeInput = getEnumInput("Employee type (OPERATOR/MANAGER): ", EmployeeType.class);
        if (typeInput == null) return;
        Employee employee = new Employee(egn, EmployeeType.valueOf(typeInput));
        try {
            employeeService.addEmployee(printHouse, employee);
            System.out.println("Employee added.");
            logger.info("Added employee with EGN {} of type {}", egn, typeInput);
        } catch (InvalidEmployeeException e) {
            System.out.println("Error: " + e.getMessage());
            logger.warn("Failed to add employee with EGN {}: {}", egn, e.getMessage());
        }
    }

    private void removeEmployee() {
        PrintHouse printHouse = selectPrintHouse();
        if (printHouse == null) return;
        List<Employee> employees = employeeService.getEmployees(printHouse);
        if (employees.isEmpty()) {
            System.out.println("No employees.");
            logger.info("No employees to remove");
            return;
        }
        listEmployees(employees);
        int index = getIntInput("Employee number to remove: ", 1, employees.size()) - 1;
        if (index < 0) return;
        try {
            employeeService.removeEmployee(printHouse, index);
            System.out.println("Employee removed.");
            logger.info("Removed employee at index: {}", index);
        } catch (InvalidEmployeeException e) {
            System.out.println("Error: " + e.getMessage());
            logger.warn("Failed to remove employee at index {}: {}", index, e.getMessage());
        }
    }

    private void updateEmployee() {
        PrintHouse printHouse = selectPrintHouse();
        if (printHouse == null) return;
        List<Employee> employees = employeeService.getEmployees(printHouse);
        if (employees.isEmpty()) {
            System.out.println("No employees.");
            logger.info("No employees to update");
            return;
        }
        listEmployees(employees);
        int index = getIntInput("Employee number to update: ", 1, employees.size()) - 1;
        if (index < 0) return;
        Employee existingEmployee = employees.get(index);
        String typeInput = getEnumInput("New employee type (OPERATOR/MANAGER): ", EmployeeType.class);
        if (typeInput == null) return;
        Employee updatedEmployee = new Employee(existingEmployee.getEgn(), EmployeeType.valueOf(typeInput));
        try {
            employeeService.updateEmployee(printHouse, index, updatedEmployee);
            System.out.println("Employee updated.");
            logger.info("Updated employee at index {}: {}", index, updatedEmployee);
        } catch (InvalidEmployeeException e) {
            System.out.println("Error: " + e.getMessage());
            logger.warn("Failed to update employee at index {}: {}", index, e.getMessage());
        }
    }

    private void calculateTotalEmployeeCost() {
        PrintHouse printHouse = selectPrintHouse();
        if (printHouse == null) return;
        BigDecimal totalCost = employeeService.getTotalCostForEmployees(printHouse);
        System.out.printf("Total Employee Cost: %.2f%n", totalCost);
        logger.info("Calculated total cost: {}", totalCost);
    }

    private void listAllEmployees() {
        PrintHouse printHouse = selectPrintHouse();
        if (printHouse == null) return;
        List<Employee> employees = employeeService.getEmployees(printHouse);
        listEmployees(employees);
    }

    private void listEmployees(List<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("No employees.");
            logger.info("No employees found");
        } else {
            for (int i = 0; i < employees.size(); i++) {
                System.out.println((i + 1) + ". " + employees.get(i));
            }
            logger.info("Listed {} employees", employees.size());
        }
    }

    private PrintHouse selectPrintHouse() {
        List<PrintHouse> houses = printHouseService.getAllPrintHouses();
        if (houses.isEmpty()) {
            System.out.println("No print houses available.");
            logger.warn("No print houses found");
            return null;
        }
        for (int i = 0; i < houses.size(); i++) {
            System.out.println((i + 1) + ". PrintHouse{baseSalary=" + houses.get(i).getBaseSalary() + ", employees=" + houses.get(i).getEmployees().size() + "}");
        }
        int index = getIntInput("Select print house: ", 1, houses.size()) - 1;
        if (index < 0) {
            logger.warn("Invalid print house selection");
            return null;
        }
        logger.debug("Selected print house at index: {}", index);
        return houses.get(index);
    }

    private int getIntInput(String prompt, int min, int max) {
        System.out.print(prompt);
        try {
            int value = Integer.parseInt(scanner.nextLine().trim());
            if (value < min || value > max) {
                System.out.println("Value must be between " + min + " and " + max + ".");
                logger.warn("Input out of range: {}", value);
                return -1;
            }
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            logger.warn("Invalid int input: {}", e.getMessage());
            return -1;
        }
    }

    private String getEnumInput(String prompt, Class<? extends Enum<?>> enumClass) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim().toUpperCase();
        if (input.isEmpty()) {
            System.out.println("Input cannot be empty.");
            logger.warn("Empty enum input");
            return null;
        }
        try {
            Enum.valueOf((Class<? extends Enum>) enumClass, input);
            logger.debug("Valid enum input: {}", input);
            return input;
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid value. Options: " + Arrays.toString(enumClass.getEnumConstants()));
            logger.warn("Invalid enum input: {}", input);
            return null;
        }
    }
}
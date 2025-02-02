package org.PrintHouse.core;

import org.PrintHouse.core.contracts.IEngine;
import org.PrintHouse.models.*;
import org.PrintHouse.models.Contracts.IEmployable;
import org.PrintHouse.models.Contracts.IPrintHouse;
import org.PrintHouse.models.Contracts.IPrintingPress;
import org.PrintHouse.utilities.SerializationService;
import org.PrintHouse.utilities.exceptions.InvalidEmployeeException;
import org.PrintHouse.utilities.exceptions.InvalidNumberOfPagesException;
import org.PrintHouse.utilities.exceptions.InvalidPaperLoadException;
import org.PrintHouse.utilities.exceptions.InvalidPrintingPressException;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConsoleEngine implements IEngine {
    private IPrintHouse<EmployeeType, PaperType, Size> printHouse;
    private final Scanner scanner;
    private SerializationService<IPrintHouse<EmployeeType, PaperType, Size>> serializationService;

    public ConsoleEngine() {
        this.scanner = new Scanner(System.in);
        this.serializationService = new SerializationService<>("print_house_data.ser"); // Default file name
        initializePrintHouse();
    }

    @Override
    public void run() {
        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:
                        addEmployee();
                        break;
                    case 2:
                        removeEmployee();
                        break;
                    case 3:
                        addPrintingPress();
                        break;
                    case 4:
                        removePrintingPress();
                        break;
                    case 5:
                        calculateTotalCostForPrint();
                        break;
                    case 6:
                        calculateTotalRevenue();
                        break;
                    case 7:
                        calculateTotalCostForEmployees();
                        break;
                    case 8:
                        saveData();
                        break;
                    case 9:
                        loadData();
                        break;
                    case 10:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    private void initializePrintHouse() {
        System.out.println("Initializing PrintHouse...");
        BigDecimal employeeSalaryIncrementPercentage = getBigDecimalInput("Enter employee salary increment percentage: ");
        BigDecimal paperIncrementPercentage = getBigDecimalInput("Enter paper increment percentage: ");
        BigDecimal baseSalary = getBigDecimalInput("Enter base salary: ");
        List<EmployeeType> incrementEligibleRoles = List.of(EmployeeType.MANAGER); // Default value
        BigDecimal revenueTarget = getBigDecimalInput("Enter revenue target: ");
        int salesDiscountCount = getIntInput("Enter sales discount count: ");
        BigDecimal salesDiscountPercentage = getBigDecimalInput("Enter sales discount percentage: ");

        printHouse = new PrintHouse<>(
                employeeSalaryIncrementPercentage,
                paperIncrementPercentage,
                baseSalary,
                incrementEligibleRoles,
                revenueTarget,
                salesDiscountCount,
                salesDiscountPercentage
        );
        System.out.println("PrintHouse initialized successfully.");
    }

    private void displayMenu() {
        System.out.println("\n=== PrintHouse Management System ===");
        System.out.println("1. Add Employee");
        System.out.println("2. Remove Employee");
        System.out.println("3. Add Printing Press");
        System.out.println("4. Remove Printing Press");
        System.out.println("5. Calculate Total Cost for Print");
        System.out.println("6. Calculate Total Revenue");
        System.out.println("7. Calculate Total Cost for Employees");
        System.out.println("8. Save Data");
        System.out.println("9. Load Data");
        System.out.println("10. Exit");
        System.out.print("Enter your choice: ");
    }

    private void addEmployee() {
        String validEmployeeTypes = Arrays.stream(EmployeeType.values())
                .map(x -> x.name())
                .collect(Collectors.joining(", "));
        System.out.print(MessageFormat.format("Enter employee type ({0}): ",validEmployeeTypes));
        String type = scanner.nextLine().toUpperCase();
        try {
            EmployeeType employeeType = EmployeeType.valueOf(type);
            IEmployable<EmployeeType> employee = new Employee<>(employeeType);
            printHouse.addEmployee(employee);
            System.out.println("Employee added successfully.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid employee type.");
        } catch (InvalidEmployeeException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void removeEmployee() {
        System.out.print("Enter employee type (OPERATOR, MANAGER): ");
        String type = scanner.nextLine().toUpperCase();
        try {
            EmployeeType employeeType = EmployeeType.valueOf(type);
            List<IEmployable<EmployeeType>> employees = printHouse.getEmployees();
            employees.stream()
                    .filter(e -> e.getEmployeeType() == employeeType)
                    .findFirst()
                    .ifPresentOrElse(
                            employee -> {
                                printHouse.removeEmployee(employee);
                                System.out.println("Employee removed successfully.");
                            },
                            () -> System.out.println("Employee not found.")
                    );
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid employee type.");
        }
    }

    private void addPrintingPress() {
        try {
            int maxPaperLoad = getIntInput("Enter max paper load: ");
            int currentPaperLoad = getIntInput("Enter current paper load: ");
            boolean isColor = getBooleanInput("Can print in color (true/false): ");
            int maxPagesPerMinute = getIntInput("Enter maximum pages per minute: ");

            PrintingPress<PaperType, Size> printingPress = new PrintingPress<>(
                    maxPaperLoad,
                    currentPaperLoad,
                    isColor,
                    maxPagesPerMinute
            );
            printHouse.addPrintingPress(printingPress);
            System.out.println("Printing press added successfully.");
        } catch (InvalidPaperLoadException ex) {
            System.out.println(ex.getMessage());
        } catch (InvalidNumberOfPagesException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void removePrintingPress() {
        List<IPrintingPress<PaperType, Size>> printingPresses = printHouse.getPrintingPressList();
        if (printingPresses.isEmpty()) {
            System.out.println("No printing presses available.");
            return;
        }

        System.out.println("Available Printing Presses:");
        for (int i = 0; i < printingPresses.size(); i++) {
            System.out.println((i + 1) + ". " + printingPresses.get(i));
        }

        System.out.print("Enter the number of the printing press to remove: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice > 0 && choice <= printingPresses.size()) {
            printHouse.removePrintingPress(printingPresses.get(choice - 1));
            System.out.println("Printing press removed successfully.");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void calculateTotalCostForPrint() {
        try {
            BigDecimal totalCost = printHouse.getTotalCostForPrint();
            System.out.println("Total Cost for Print: " + totalCost);
        } catch (InvalidPrintingPressException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void calculateTotalRevenue() {
        BigDecimal totalRevenue = printHouse.getTotalRevenue();
        System.out.println("Total Revenue: " + totalRevenue);
    }

    private void calculateTotalCostForEmployees() {
        BigDecimal totalCost = printHouse.getTotalCostForEmployees();
        System.out.println("Total Cost for Employees: " + totalCost);
    }

    private void saveData() {
        getFileDirectoryDetails();

        try {
            serializationService.serialize((PrintHouse<EmployeeType, PaperType, Size>) printHouse);
            System.out.println("Data saved successfully.");
        } catch (Exception e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private void getFileDirectoryDetails() {
        System.out.print("Enter file name (default: print_house_data.ser): ");
        String fileName = scanner.nextLine().trim();
        if (fileName.isEmpty()) {
            fileName = "print_house_data.ser";
        }

        System.out.print("Enter file path (leave blank for default project directory): ");
        String path = scanner.nextLine().trim();

        if (path.isEmpty()) {
            serializationService = new SerializationService<>(fileName);
        } else {
            serializationService = new SerializationService<>(path, fileName);
        }
    }

    private void loadData() {
        getFileDirectoryDetails();

        try {
            IPrintHouse<EmployeeType, PaperType, Size> loadedPrintHouse = serializationService.deserializeSingleObject();
            if (loadedPrintHouse != null) {
                printHouse = loadedPrintHouse;
                System.out.println("Data loaded successfully.");
            } else {
                System.out.println("Failed to load data.");
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }


    // Helper methods for user input
    private BigDecimal getBigDecimalInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private boolean getBooleanInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().toLowerCase();
            if (input.equals("true") || input.equals("false")) {
                return Boolean.parseBoolean(input);
            }
            System.out.println("Invalid input. Please enter 'true' or 'false'.");
        }
    }
}
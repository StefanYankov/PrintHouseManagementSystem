package UI.controllers;

import data.models.EmployeeType;
import data.models.PrintHouse;
import services.contracts.IPrintHouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Handles console interactions for managing {@link PrintHouse} entities.
 */
public class PrintHouseController {
    private static final Logger logger = LoggerFactory.getLogger(PrintHouseController.class);
    private final IPrintHouseService service;
    private final Scanner scanner;

    public PrintHouseController(IPrintHouseService service, Scanner scanner) {
        if (service == null || scanner == null) {
            throw new IllegalArgumentException("Dependencies cannot be null.");
        }
        this.service = service;
        this.scanner = scanner;
        logger.info("PrintHouseController initialized with dependencies");
    }

    public void handleMenu() {
        logger.info("Entering PrintHouseController menu");
        while (true) {
            displayMenu();
            int choice = getUserChoice();
            logger.debug("User selected menu option: {}", choice);
            if (choice == 0) {
                logger.info("Exiting PrintHouseController menu");
                break;
            }
            processChoice(choice);
        }
    }

    private void displayMenu() {
        System.out.println(System.lineSeparator() + "--- Print House Management ---" + System.lineSeparator());
        System.out.println("1. List all print houses");
        System.out.println("2. Add a new print house");
        System.out.println("3. Update a print house");
        System.out.println("4. Remove a print house");
        System.out.println("5. Save all print houses");
        System.out.println("6. Load all print houses");
        System.out.println("0. Back to main menu");
        System.out.print("Enter your choice: ");
        logger.debug("Displayed Print House Management menu");
    }

    private void processChoice(int choice) {
        try {
            switch (choice) {
                case 1 -> listAllPrintHouses();
                case 2 -> addNewPrintHouse();
                case 3 -> updatePrintHouse();
                case 4 -> removePrintHouse();
                case 5 -> saveAllPrintHouses();
                case 6 -> loadAllPrintHouses();
                default -> {
                    logger.warn("Invalid choice received: {}", choice);
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            logger.error("Error processing choice {}: {}", choice, e.getMessage(), e);
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private void listAllPrintHouses() {
        logger.debug("Fetching all print houses");
        List<PrintHouse> houses = service.getAllPrintHouses();
        if (houses.isEmpty()) {
            logger.info("No print houses found");
            System.out.println("No print houses found.");
        } else {
            logger.info("Found {} print houses", houses.size());
            for (int i = 0; i < houses.size(); i++) {
                System.out.println((i + 1) + ". " + houses.get(i));
            }
        }
    }

    private void addNewPrintHouse() {
        BigDecimal salaryIncrement = getBigDecimalInput("Salary increment %: ", false);
        if (salaryIncrement == null) {
            return;
        }

        BigDecimal paperIncrement = getBigDecimalInput("Paper increment %: ", false);
        if (paperIncrement == null) return;
        BigDecimal baseSalary = getBigDecimalInput("Base salary: ", false);
        if (baseSalary == null) return;
        List<EmployeeType> eligibleRoles = List.of(EmployeeType.MANAGER);
        BigDecimal revenueTarget = getBigDecimalInput("Revenue target: ", false);
        if (revenueTarget == null) return;
        int discountCount = getIntInput("Discount count: ", false);
        if (discountCount == -1) return;
        BigDecimal discountPercent = getBigDecimalInput("Discount %: ", false);
        if (discountPercent == null) return;

        logger.info("Creating new print house with parameters: salaryIncrement={}, paperIncrement={}, baseSalary={}, revenueTarget={}, discountCount={}, discountPercent={}",
                salaryIncrement, paperIncrement, baseSalary, revenueTarget, discountCount, discountPercent);
        PrintHouse newPrintHouse = service.createPrintHouse(salaryIncrement, paperIncrement, baseSalary,
                eligibleRoles, revenueTarget, discountCount, discountPercent);
        System.out.println("Print house created successfully at index " + (service.getAllPrintHouses().size() - 1));
        logger.info("Print house created successfully at index {}", service.getAllPrintHouses().size() - 1);
    }

    private void updatePrintHouse() {
        listAllPrintHouses();
        int index = getIntInput("Enter print house number to update (1-based): ", false) - 1;
        if (index < 0 || index >= service.getAllPrintHouses().size()) {
            logger.warn("Invalid print house index: {}", index);
            System.out.println("Invalid print house selection.");
            return;
        }
        PrintHouse current = service.getPrintHouse(index);
        System.out.println("Current: " + current);
        BigDecimal salaryIncrement = getOptionalBigDecimalInput("New salary increment % (leave blank to keep " + current.getEmployeeSalaryIncrementPercentage() + "): ", false);
        BigDecimal paperIncrement = getOptionalBigDecimalInput("New paper increment % (leave blank to keep " + current.getPaperIncrementPercentage() + "): ", false);
        BigDecimal baseSalary = getOptionalBigDecimalInput("New base salary (leave blank to keep " + current.getBaseSalary() + "): ", false);
        List<EmployeeType> eligibleRoles = getOptionalEligibleRoles("New eligible roles (comma-separated OPERATOR/MANAGER, leave blank to keep " + current.getIncrementEligibleRoles() + "): ");
        BigDecimal revenueTarget = getOptionalBigDecimalInput("New revenue target (leave blank to keep " + current.getRevenueTarget() + "): ", false);
        Integer discountCount = getOptionalIntInput("New discount count (leave blank to keep " + current.getSalesDiscountCount() + "): ", false);
        BigDecimal discountPercent = getOptionalBigDecimalInput("New discount % (leave blank to keep " + current.getSalesDiscountPercentage() + "): ", false);

        service.updatePrintHouse(index, salaryIncrement, paperIncrement, baseSalary, eligibleRoles, revenueTarget, discountCount, discountPercent);
        System.out.println("Print house updated successfully.");
        logger.info("Print house updated at index {}", index);
    }

    private void removePrintHouse() {
        listAllPrintHouses();
        int index = getIntInput("Enter print house number to remove (1-based): ", false) - 1;
        if (index < 0 || index >= service.getAllPrintHouses().size()) {
            logger.warn("Invalid print house index: {}", index);
            System.out.println("Invalid print house selection.");
            return;
        }
        service.removePrintHouse(index);
        System.out.println("Print house removed successfully.");
        logger.info("Print house removed at index {}", index);
    }

    private void saveAllPrintHouses() {
        System.out.print("Enter file name (default: print_houses_data.ser): ");
        String filePath = scanner.nextLine().trim();
        filePath = filePath.isEmpty() ? "print_houses_data.ser" : filePath;
        logger.debug("Saving print houses to file: {}", filePath);
        service.saveAllPrintHouses(filePath);
        System.out.println("Data saved successfully.");
        logger.info("Saved all print houses to {}", filePath);
    }

    private void loadAllPrintHouses() {
        System.out.print("Enter file name (default: print_houses_data.ser): ");
        String filePath = scanner.nextLine().trim();
        filePath = filePath.isEmpty() ? "print_houses_data.ser" : filePath;
        logger.debug("Loading print houses from file: {}", filePath);
        service.loadAllPrintHouses(filePath);
        System.out.println("Data loaded successfully.");
        logger.info("Loaded print houses from {}", filePath);
    }

    private int getUserChoice() {
        try {
            String input = scanner.nextLine().trim();
            int choice = Integer.parseInt(input);
            logger.debug("Received valid menu choice: {}", choice);
            return choice;
        } catch (NumberFormatException e) {
            logger.warn("Invalid menu input received: {}", e.getMessage());
            return -1;
        }  catch (Exception e) {
            System.out.println("An error occurred, please try again.");
            return -1;
        }
    }

    private int getIntInput(String prompt, boolean allowNegative) {
        System.out.print(prompt);
        try {
            int value = Integer.parseInt(scanner.nextLine().trim());
            if (!allowNegative && value < 0) {
                System.out.println("Value cannot be negative.");
                return -1;
            }
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            return -1;
        } catch (Exception e) {
            System.out.println("An error occurred, please try again.");
            return -1;
        }
    }

    private BigDecimal getBigDecimalInput(String prompt, boolean allowNegative) {
        System.out.print(prompt);
        try {
            BigDecimal value = new BigDecimal(scanner.nextLine().trim());
            if (!allowNegative && value.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("Value cannot be negative.");
                return null;
            }
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            return null;
        }
    }

    private BigDecimal getOptionalBigDecimalInput(String prompt, boolean allowNegative) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return null;
        try {
            BigDecimal value = new BigDecimal(input);
            if (!allowNegative && value.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("Value cannot be negative.");
                return null;
            }
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            return null;
        }
    }

    private Integer getOptionalIntInput(String prompt, boolean allowNegative) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return null;
        try {
            int value = Integer.parseInt(input);
            if (!allowNegative && value < 0) {
                System.out.println("Value cannot be negative.");
                return null;
            }
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            return null;
        }
    }

    private List<EmployeeType> getOptionalEligibleRoles(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return null;
        try {
            return Arrays.stream(input.split(","))
                    .map(String::trim)
                    .map(String::toUpperCase)
                    .map(EmployeeType::valueOf)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid employee type in input: {}", input);
            System.out.println("Invalid employee type(s). Use OPERATOR or MANAGER, comma-separated.");
            return null;
        }
    }
}
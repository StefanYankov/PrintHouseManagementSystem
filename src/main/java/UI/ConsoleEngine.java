package UI;

import UI.contracts.IEngine;
import UI.controllers.*;
import data.models.Edition;
import data.models.PrintHouse;
import services.contracts.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Manages the console-based user interface for the print house application.
 */
public class ConsoleEngine implements IEngine {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleEngine.class);
    private static final Scanner scanner = new Scanner(System.in);

    private final ISerializationService<PrintHouse> serializationService;
    private final ISerializationService<Edition> editionSerializationService;
    private final IPrintHouseService printHouseService;
    private final IEmployeeService employeeService;
    private final IPrintingPressService printingPressService;
    private final IEditionService editionService;
    private final PrintHouseController printHouseController;
    private final EmployeeController employeeController;
    private final PrintingPressController printingPressController;
    private final EditionController editionController;

    public ConsoleEngine(ISerializationService<PrintHouse> serializationService,
                         ISerializationService<Edition> editionSerializationService,
                         IPrintHouseService printHouseService,
                         IEmployeeService employeeService,
                         IPrintingPressService printingPressService,
                         IEditionService editionService) {
        this.serializationService = serializationService;
        this.editionSerializationService = editionSerializationService;
        this.printHouseService = printHouseService;
        this.employeeService = employeeService;
        this.printingPressService = printingPressService;
        this.editionService = editionService;
        this.printHouseController = new PrintHouseController(printHouseService, scanner);
        this.employeeController = new EmployeeController(employeeService, printHouseService, printingPressService, scanner);
        this.printingPressController = new PrintingPressController(printingPressService, printHouseService, editionService, scanner);
        this.editionController = new EditionController(editionService, printHouseService, scanner);
        logger.info("ConsoleEngine initialized with all services and controllers");
    }

    @Override
    public void run() {
        logger.info("Starting PrintHouse Application");
        while (true) {
            displayMenu();
            int choice = getUserChoice();
            if (choice == 0) break;
            processChoice(choice);
        }
        logger.info("Exiting ConsoleEngine");
        System.out.println("Exiting application...");
        scanner.close();
    }

    private void displayMenu() {
        System.out.println("\n=== Print House Management System ===");
        System.out.println("1. Manage Print Houses");
        System.out.println("2. Manage Employees");
        System.out.println("3. Manage Printing Presses");
        System.out.println("4. Manage Editions");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
        logger.debug("Displayed main menu");
    }

    private int getUserChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            logger.debug("User selected option: {}", choice);
            return choice;
        } catch (NumberFormatException e) {
            logger.warn("Invalid input: {}", e.getMessage());
            System.out.println("Invalid input. Please enter a number.");
            return -1;
        }
    }

    private void processChoice(int choice) {
        try {
            switch (choice) {
                case 1 -> printHouseController.handleMenu();
                case 2 -> employeeController.handleMenu();
                case 3 -> printingPressController.handleMenu();
                case 4 -> editionController.handleMenu();
                default -> {
                    logger.warn("Invalid choice: {}", choice);
                    System.out.println("Invalid choice.");
                }
            }
        } catch (Exception e) {
            logger.error("Error processing choice {}: {}", choice, e.getMessage());
            System.out.println("Error: " + e.getMessage());
        }
    }
}
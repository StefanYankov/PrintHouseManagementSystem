package UI.controllers;

import data.models.*;
import services.contracts.IEditionService;
import services.contracts.IPrintHouseService;
import utilities.globalconstants.ExceptionMessages;
import utilities.globalconstants.ModelsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Handles console interactions for managing {@link Edition} entities.
 */
public class EditionController {
    private static final Logger logger = LoggerFactory.getLogger(EditionController.class);
    private final IEditionService editionService;
    private final IPrintHouseService printHouseService;
    private final Scanner scanner;

    public EditionController(IEditionService editionService, IPrintHouseService printHouseService, Scanner scanner) {
        if (editionService == null || printHouseService == null || scanner == null) {
            logger.error("Dependencies cannot be null");
            throw new IllegalArgumentException(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL);
        }
        this.editionService = editionService;
        this.printHouseService = printHouseService;
        this.scanner = scanner;
        logger.info("EditionController initialized");
    }

    public void handleMenu() {
        logger.info("Starting edition management menu");
        while (true) {
            displayMenu();
            int choice = getUserChoice();
            if (choice == 0) break;
            processChoice(choice);
        }
        logger.info("Exiting edition management menu");
    }

    private void displayMenu() {
        System.out.println("\n--- Edition Management ---");
        System.out.println("1. List editions");
        System.out.println("2. Add edition");
        System.out.println("3. Update edition");
        System.out.println("4. Remove edition");
        System.out.println("5. Save editions");
        System.out.println("6. Load editions");
        System.out.println("0. Back to main menu");
        System.out.print("Enter your choice: ");
        logger.debug("Displayed edition menu");
    }

    private int getUserChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            logger.debug("User choice: {}", choice);
            return choice;
        } catch (NumberFormatException e) {
            logger.warn("Invalid choice input: {}", e.getMessage());
            System.out.println("Invalid input. Please enter a number.");
            return -1;
        }
    }

    private void processChoice(int choice) {
        try {
            switch (choice) {
                case 1 -> listEditions();
                case 2 -> addEdition();
                case 3 -> updateEdition();
                case 4 -> removeEdition();
                case 5 -> saveEditions();
                case 6 -> loadEditions();
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

    private void listEditions() {
        PrintHouse printHouse = selectPrintHouse();
        if (printHouse == null) return;
        List<Edition> editions = editionService.getEditions(printHouse);
        if (editions.isEmpty()) {
            System.out.println("No editions available.");
            logger.info("No editions found");
        } else {
            for (int i = 0; i < editions.size(); i++) {
                System.out.println((i + 1) + ". " + editions.get(i));
            }
            logger.info("Listed {} editions", editions.size());
        }
    }

    private void addEdition() {
        PrintHouse printHouse = selectPrintHouse();
        if (printHouse == null) return;
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();
        if (title.length() < ModelsConstants.MIN_TITLE_LENGTH || title.length() > ModelsConstants.MAX_TITLE_LENGTH) {
            logger.warn("Invalid title length: {}", title.length());
            System.out.println("Title length must be between " + ModelsConstants.MIN_TITLE_LENGTH + " and " + ModelsConstants.MAX_TITLE_LENGTH);
            return;
        }
        int pages = getIntInput("Number of pages: ", ModelsConstants.MIN_PAGE_COUNT, ModelsConstants.MAX_PAGE_COUNT);
        if (pages == -1) return;
        String sizeInput = getEnumInput("Size (A5/A4/A3/A2/A1): ", Size.class);
        if (sizeInput == null) return;
        Edition edition = new Edition(title, pages, Size.valueOf(sizeInput));
        editionService.addEdition(printHouse, edition);
        System.out.println("Edition added.");
        logger.info("Added edition: {}", edition);
    }

    private void updateEdition() {
        PrintHouse printHouse = selectPrintHouse();
        if (printHouse == null) return;
        listEditions();
        List<Edition> editions = editionService.getEditions(printHouse);
        if (editions.isEmpty()) return;
        int index = getIntInput("Select edition number: ", 1, editions.size()) - 1;
        if (index < 0) return;
        Edition edition = editionService.getEdition(printHouse, index);
        System.out.print("New title (blank to keep): ");
        String title = scanner.nextLine().trim();
        Integer pages = getOptionalIntInput("New pages (blank to keep): ", ModelsConstants.MIN_PAGE_COUNT, ModelsConstants.MAX_PAGE_COUNT);
        String sizeInput = getOptionalEnumInput("New size (A5/A4/A3/A2/A1, blank to keep): ", Size.class);
        editionService.updateEdition(printHouse, edition, title.isEmpty() ? null : title, pages, sizeInput == null ? null : Size.valueOf(sizeInput));
        System.out.println("Edition updated.");
        logger.info("Updated edition: {}", edition);
    }

    private void removeEdition() {
        PrintHouse printHouse = selectPrintHouse();
        if (printHouse == null) return;
        listEditions();
        List<Edition> editions = editionService.getEditions(printHouse);
        if (editions.isEmpty()) return;
        int index = getIntInput("Select edition number: ", 1, editions.size()) - 1;
        if (index < 0) return;
        Edition edition = editionService.getEdition(printHouse, index);
        editionService.removeEdition(printHouse, edition);
        System.out.println("Edition removed.");
        logger.info("Removed edition: {}", edition);
    }

    private void saveEditions() {
        PrintHouse printHouse = selectPrintHouse();
        if (printHouse == null) {
            return;
        }
        System.out.print("Enter file path to save editions: ");
        String filePath = scanner.nextLine().trim();
        editionService.saveEditions(printHouse, filePath);
        System.out.println("Editions saved.");
        logger.info("Saved editions for PrintHouse {}", printHouse);
    }

    private void loadEditions() {
        PrintHouse printHouse = selectPrintHouse();
        if (printHouse == null) {
            return;
        }
        System.out.print("Enter file path to load editions: ");
        String filePath = scanner.nextLine().trim();
        editionService.loadEditions(printHouse, filePath);
        System.out.println("Editions loaded.");
        logger.info("Loaded editions for PrintHouse {}", printHouse);
    }

    private PrintHouse selectPrintHouse() {
        List<PrintHouse> houses = printHouseService.getAllPrintHouses();
        if (houses.isEmpty()) {
            System.out.println("No print houses available.");
            logger.warn("No print houses found");
            return null;
        }
        for (int i = 0; i < houses.size(); i++) {
            System.out.println((i + 1) + ". " + houses.get(i));
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

    private Integer getOptionalIntInput(String prompt, int min, int max) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return null;
        try {
            int value = Integer.parseInt(input);
            if (value < min || value > max) {
                System.out.println("Value must be between " + min + " and " + max + ".");
                logger.warn("Input out of range: {}", value);
                return null;
            }
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            logger.warn("Invalid int input: {}", e.getMessage());
            return null;
        }
    }

    private String getEnumInput(String prompt, Class<? extends Enum<?>> enumClass) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim().toUpperCase();
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

    private String getOptionalEnumInput(String prompt, Class<? extends Enum<?>> enumClass) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim().toUpperCase();
        if (input.isEmpty()) return null;
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
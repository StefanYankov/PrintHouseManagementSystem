package UI.controllers;

import data.models.*;
import services.contracts.IEditionService;
import services.contracts.IPrintHouseService;
import services.contracts.IPrintingPressService;
import utilities.exceptions.InvalidPaperLoadException;
import utilities.globalconstants.ExceptionMessages;
import utilities.globalconstants.ModelsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Handles console interactions for managing {@link PrintingPress} entities and printing operations within a {@link PrintHouse}.
 */
public class PrintingPressController {
    private static final Logger logger = LoggerFactory.getLogger(PrintingPressController.class);
    private final IPrintingPressService printingPressService;
    private final IPrintHouseService printHouseService;
    private final IEditionService editionService;
    private final Scanner scanner;

    public PrintingPressController(IPrintingPressService printingPressService, IPrintHouseService printHouseService,
                                   IEditionService editionService,
                                   Scanner scanner) {
        if (printingPressService == null || printHouseService == null || scanner == null) {
            logger.error("Dependencies cannot be null");
            throw new IllegalArgumentException(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL);
        }
        this.printingPressService = printingPressService;
        this.printHouseService = printHouseService;
        this.editionService = editionService;
        this.scanner = scanner;
        logger.info("PrintingPressController initialized");
    }

    public void handleMenu() {
        logger.info("Starting printing press management menu");
        while (true) {
            displayMenu();
            int choice = getUserChoice();
            if (choice == 0) break;
            processChoice(choice);
        }
        logger.info("Exiting printing press management menu");
    }

    private void displayMenu() {
        System.out.println(System.lineSeparator() + "--- Printing Press Management ---" + System.lineSeparator());
        System.out.println("1. Add a printing press");
        System.out.println("2. Remove a printing press");
        System.out.println("3. Update a printing press");
        System.out.println("4. Show all printing presses");
        System.out.println("5. Load paper into press");
        System.out.println("6. Print an item");
        System.out.println("7. Calculate total print cost");
        System.out.println("8. Calculate total revenue");
        System.out.println("9. Calculate total printed pages");
        System.out.println("0. Back to main menu");
        System.out.print("Enter your choice: ");
        logger.debug("Displayed printing press menu");
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
                case 1 -> addPrintingPress();
                case 2 -> removePrintingPress();
                case 3 -> updatePrintingPress();
                case 4 -> showAllPrintingPresses();
                case 5 -> loadPaper();
                case 6 -> printItem();
                case 7 -> calculateTotalPrintCost();
                case 8 -> calculateTotalRevenue();
                case 9 -> calculateTotalPrintedPages();
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

    private void addPrintingPress() {
        PrintHouse printHouse = selectPrintHouse();
        if (printHouse == null) return;
        int maxPaperLoad = getIntInput("Max paper load: ", 1, ModelsConstants.MAXIMUM_PAPER_LOAD);
        if (maxPaperLoad == -1) return;
        int currentPaperLoad = getIntInput("Current paper load: ", 0, maxPaperLoad);
        if (currentPaperLoad == -1) return;
        Boolean isColour = getBooleanInput("Color capable (true/false): ");
        if (isColour == null) return;
        int maxPagesPerMinute = getIntInput("Max pages per minute: ", 1, ModelsConstants.MAXIMUM_PAGES_PER_MINUTE);
        if (maxPagesPerMinute == -1) return;
        PrintingPress press = new PrintingPress(maxPaperLoad, currentPaperLoad, isColour, maxPagesPerMinute);
        printingPressService.addPrintingPress(printHouse, press);
        System.out.println("Printing press added.");
        logger.info("Added printing press: {}", press);
    }

    private void removePrintingPress() {
        PrintHouse printHouse = selectPrintHouse();
        if (printHouse == null) return;
        PrintingPress press = selectPrintingPress(printHouse);
        if (press == null) return;
        printingPressService.removePrintingPress(printHouse, press);
        System.out.println("Printing press removed.");
        logger.info("Removed printing press: {}", press);
    }

    private void updatePrintingPress() {
        PrintHouse printHouse = selectPrintHouse();
        if (printHouse == null) return;
        PrintingPress press = selectPrintingPress(printHouse);
        if (press == null) return;
        Integer maxLoad = getOptionalIntInput("New max paper load (blank to keep): ", 1, ModelsConstants.MAXIMUM_PAPER_LOAD);
        Integer currentLoad = getOptionalIntInput("New current paper load (blank to keep): ", 0, maxLoad != null ? maxLoad : press.getMaxPaperLoad());
        Boolean isColour = getOptionalBooleanInput("New color capability (true/false, blank to keep): ");
        Integer maxPages = getOptionalIntInput("New max pages per minute (blank to keep): ", 1, ModelsConstants.MAXIMUM_PAGES_PER_MINUTE);
        printingPressService.updatePrintingPress(printHouse, press, maxLoad, currentLoad, isColour, maxPages);
        System.out.println("Printing press updated.");
        logger.info("Updated printing press: {}", press);
    }

    private void showAllPrintingPresses(){
        PrintHouse printHouse = selectPrintHouse();
        if (printHouse == null) return;
        List<PrintingPress> presses = printHouse.getPrintingPresses();
        if (presses.isEmpty()) {
            System.out.println("No printing presses found.");
            logger.info("No printing presses in PrintHouse {}", printHouse);
        } else {
            presses.forEach(System.out::println);
            logger.info("Displayed all printing presses for PrintHouse {}", printHouse);
        }
    }

    private void loadPaper() {
        PrintHouse printHouse = selectPrintHouse();
        if (printHouse == null) return;
        PrintingPress press = selectPrintingPress(printHouse);
        if (press == null) return;
        int maxLoad = press.getMaxPaperLoad() - press.getCurrentPaperLoad();
        int amount = getIntInput("Amount of paper to load: ", 1, maxLoad);
        if (amount == -1) return;
        printingPressService.loadPaper(printHouse, press, amount);
        System.out.println("Paper loaded.");
        logger.info("Loaded {} paper into press: {}", amount, press);
    }

    private void printItem() {
        PrintHouse printHouse = selectPrintHouse();
        if (printHouse == null) {
            logger.warn("Print house selection failed");
            return;
        }
        PrintingPress press = selectPrintingPress(printHouse);
        if (press == null) {
            logger.warn("Printing press selection failed");
            return;
        }
        List<Edition> editions = editionService.getEditions(printHouse);
        if (editions.isEmpty()) {
            System.out.println("No editions available.");
            logger.warn("No editions found for printing in PrintHouse {}", printHouse);
            return;
        }
        for (int i = 0; i < editions.size(); i++) {
            System.out.println((i + 1) + ". " + editions.get(i));
        }
        int index = getIntInput("Select edition number: ", 1, editions.size()) - 1;
        if (index < 0) {
            logger.warn("Invalid edition selection");
            return;
        }
        Edition edition = editions.get(index);

        String paperTypeInput = getEnumInput("Paper type (STANDARD/GLOSSY/NEWSPAPER): ", PaperType.class);
        if (paperTypeInput == null) {
            logger.warn("Invalid paper type input");
            return;
        }
        PaperType paperType = PaperType.valueOf(paperTypeInput);

        BigDecimal pricePerCopy = getBigDecimalInput("Price per copy: ", BigDecimal.ZERO, ModelsConstants.MAXIMUM_PRICE);
        if (pricePerCopy == null) {
            logger.warn("Invalid price per copy input");
            return;
        }

        int copies = getIntInput("Number of copies: ", 1, Integer.MAX_VALUE);
        if (copies == -1) {
            logger.warn("Invalid number of copies input");
            return;
        }

        Boolean isColour = getBooleanInput("Color print (true/false): ");
        if (isColour == null) {
            logger.warn("Invalid color input");
            return;
        }

        try {
            printingPressService.printItem(printHouse, press, edition, paperType, pricePerCopy, copies, isColour);
            System.out.println("Item printed successfully.");
            logger.info("Printed {} copies of edition {} on press {}", copies, edition, press);
        } catch (InvalidPaperLoadException e) {
            System.out.println("Insufficient paper load. Please reload the printing press and try again ");
            logger.error("Failed to print item: {}", e.getMessage(), e);
        } catch (Exception e) {
            System.out.println("Something went wrong! Please try again");
            logger.error("Failed to print item: {}", e.getMessage(), e);
        }
    }

    private void calculateTotalPrintCost() {
        PrintHouse printHouse = selectPrintHouse();
        if (printHouse == null) return;
        BigDecimal cost = printingPressService.getTotalCostForPrint(printHouse);
        System.out.println("Total Print Cost: " + cost);
        logger.info("Calculated total print cost: {}", cost);
    }

    private void calculateTotalRevenue() {
        PrintHouse printHouse = selectPrintHouse();
        if (printHouse == null) return;
        BigDecimal revenue = printingPressService.getTotalRevenue(printHouse);
        System.out.println("Total Revenue: " + revenue);
        logger.info("Calculated total revenue: {}", revenue);
    }

    private void calculateTotalPrintedPages() {
        PrintHouse printHouse = selectPrintHouse();
        if (printHouse == null) return;
        PrintingPress press = selectPrintingPress(printHouse);
        if (press == null) return;
        long totalPages = printingPressService.totalPrintedPages(printHouse, press);
        System.out.println("Total Printed Pages: " + totalPages);
        logger.info("Calculated total printed pages: {}", totalPages);
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

    private PrintingPress selectPrintingPress(PrintHouse printHouse) {
        List<PrintingPress> presses = printHouse.getPrintingPresses();
        if (presses.isEmpty()) {
            System.out.println("No printing presses available.");
            logger.warn("No printing presses found in print house");
            return null;
        }
        for (int i = 0; i < presses.size(); i++) {
            System.out.println((i + 1) + ". " + presses.get(i));
        }
        int index = getIntInput("Select printing press: ", 1, presses.size()) - 1;
        if (index < 0) {
            logger.warn("Invalid printing press selection");
            return null;
        }
        logger.debug("Selected printing press at index: {}", index);
        return presses.get(index);
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

    private BigDecimal getBigDecimalInput(String prompt, BigDecimal min, BigDecimal max) {
        System.out.print(prompt);
        try {
            BigDecimal value = new BigDecimal(scanner.nextLine().trim());
            if (value.compareTo(min) <= 0 || value.compareTo(max) > 0) {
                System.out.println("Value must be between " + min + " and " + max + ".");
                logger.warn("Input out of range: {}", value);
                return null;
            }
            logger.debug("Valid BigDecimal input: {}", value);
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            logger.warn("Invalid BigDecimal input: {}", e.getMessage());
            return null;
        }
    }

    private Boolean getBooleanInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim().toLowerCase();
        if ("true".equals(input)) return true;
        if ("false".equals(input)) return false;
        System.out.println("Invalid input. Please enter 'true' or 'false'.");
        logger.warn("Invalid boolean input: {}", input);
        return null;
    }

    private Boolean getOptionalBooleanInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim().toLowerCase();
        if (input.isEmpty()) {
            return null;
        }
        if ("true".equals(input)) {
            return true;
        }
        if ("false".equals(input)) {
            return false;
        }
        System.out.println("Invalid input. Please enter 'true' or 'false'.");
        logger.warn("Invalid boolean input: {}", input);
        return null;
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
}
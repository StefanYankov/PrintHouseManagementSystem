package org.PrintHouse.core;

import org.PrintHouse.core.contracts.IEngine;
import org.PrintHouse.models.*;
import org.PrintHouse.models.Contracts.*;
import org.PrintHouse.models.EmployeeType;
import org.PrintHouse.utilities.SerializationService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DemoEngine implements IEngine {

    private IPrintHouse<EmployeeType, PaperType, Size> printHouse;
    private SerializationService<IPrintHouse<EmployeeType, PaperType, Size>> serializationService;
    private BigDecimal salaryIncrementBonusPercentage;
    private BigDecimal paperIncrementPercentage;
    private BigDecimal baseSalary;
    private List<EmployeeType> incrementEligibleRoles;
    private BigDecimal revenueTarget;
    private int discountCount;
    private BigDecimal discountPercentage;
    SerializationService<IEdition<Size>> serializationServiceEdition;

    @Override
    public void run() {

        // setup dummy data
        // 1 for the print house
        salaryIncrementBonusPercentage = BigDecimal.valueOf(10);
        paperIncrementPercentage = BigDecimal.valueOf(20);
        baseSalary = BigDecimal.valueOf(1000);
        incrementEligibleRoles = new ArrayList<>();
        incrementEligibleRoles.add(EmployeeType.MANAGER);
        revenueTarget = BigDecimal.valueOf(10000);
        discountCount = 10;
        discountPercentage = BigDecimal.valueOf(5);

        //2 for the printing press

        // create a print house
        printHouse = new PrintHouse<>(
                salaryIncrementBonusPercentage,
                paperIncrementPercentage,
                baseSalary,
                incrementEligibleRoles,
                revenueTarget,
                discountCount,
                discountPercentage
        );

        IEdition<Size> theLordOfTheRings = new Edition<>(
                "The Lord of the Rings: The Two Towers",
                352,
                Size.A4);

        IEdition<Size> divineComedy = new Edition<>(
                "Divine comedy",
                784,
                Size.A4);

        IEdition<Size> crimeAndPunishment = new Edition<>(
                "Crime and punishment",
                527,
                Size.A4);

        // Add entities to a list
        List<IEdition<Size>> editions = new ArrayList<>();
        editions.add(theLordOfTheRings);
        editions.add(divineComedy);
        editions.add(crimeAndPunishment);

        // Serialize the list of entities
        serializationServiceEdition = new SerializationService<>("editions.ser");
        serializationServiceEdition.serialize(editions);

        // Deserialize the list of entities
        List<IEdition<Size>> deserializedEditions = serializationServiceEdition.deserialize();

        // create a printing pres
        int maxPaperLoad = 1000;
        int currentPaperLoad = 1000;
        boolean canColorPrint = true;
        int maximumPagesPerMinute = 500;

        IPrintingPress<PaperType, Size> printingPress1 = new PrintingPress<>(
                maxPaperLoad,
                currentPaperLoad,
                canColorPrint,
                maximumPagesPerMinute);

        // add the printing press to the print house
        printHouse.addPrintingPress(printingPress1);


        // create printed items based on the deserialized object list
        for (var edition : deserializedEditions) {
            printingPress1.printItems(canColorPrint, edition, PaperType.STANDARD,BigDecimal.valueOf(25),1);
        }


        // System.out.println(printingPress1.getPrintedItems());

        System.out.println(printHouse.getCostForPrintedItemsByPrintingPress(printingPress1));

        IEmployable<EmployeeType> operator1 = new Employee<>(EmployeeType.OPERATOR);
        IEmployable<EmployeeType> operator2 = new Employee<>(EmployeeType.OPERATOR);
        IEmployable<EmployeeType> manager1 = new Employee<>(EmployeeType.MANAGER);
        IEmployable<EmployeeType> manager2 = new Employee<>(EmployeeType.MANAGER);

        printHouse.addEmployee(operator1);
        printHouse.addEmployee(operator2);
        printHouse.addEmployee(manager1);
        printHouse.addEmployee(manager2);
        try {
            printHouse.addEmployee(manager2);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }


        System.out.println(printHouse.getEmployees());

        System.out.println("Total revenue: " + printHouse.getTotalRevenue());
        System.out.println("Revenue target: " + printHouse.getRevenueTarget());
        System.out.println("Total cost for employees: " + printHouse.getTotalCostForEmployees());

        printHouse.setRevenueTarget(BigDecimal.valueOf(100));
        System.out.println("Total cost for employees after hitting revenue target: " + printHouse.getTotalCostForEmployees());

        var printingPress = printHouse.getPrintingPressList().getFirst();
        System.out.println("Current paper load for " + printingPress.getCurrentPaperLoad());


        try {
            printingPress.printAnItem(true, crimeAndPunishment,PaperType.GLOSSY, BigDecimal.valueOf(50));

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        printingPress.loadPaper(500);

        try {
            printingPress.printItems(true, crimeAndPunishment,PaperType.GLOSSY, BigDecimal.valueOf(50), 2);

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        try {
            printingPress.printAnItem(true, crimeAndPunishment, PaperType.GLOSSY, BigDecimal.valueOf(50));

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

        for (var edition : printingPress.getPrintedItems().keySet()) {
            System.out.println(edition + ": " + printingPress.getPrintedItems().get(edition));
        }

    }

}

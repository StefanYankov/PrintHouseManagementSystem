package org.PrintHouse.models;

import org.PrintHouse.models.Contracts.IPaperTypes;
import org.PrintHouse.utilities.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrintHouseTest {

    private PrintHouse<EmployeeType, PaperType, Size> printHouse;
    private List<EmployeeType> incrementEligibleRoles;

    private BigDecimal validEmployeeSalaryIncrementPercentage;
    private BigDecimal validPaperIncrementPercentage;
    private BigDecimal validBaseSalary;
    private BigDecimal validRevenueTarget;
    private int validSalesDiscountCount;
    private BigDecimal validSalesDiscountPercentage;
    private Employee<EmployeeType> employee;
    private PrintingPress<PaperType, Size> printingPress;
    private PrintedItem<PaperType, Size> printedItem;
    private Edition<Size> edition;

    @BeforeEach
    void setUp() {
        validEmployeeSalaryIncrementPercentage = BigDecimal.valueOf(10);
        validPaperIncrementPercentage = BigDecimal.valueOf(5);
        validBaseSalary = BigDecimal.valueOf(1000);
        incrementEligibleRoles = List.of(EmployeeType.MANAGER);
        validRevenueTarget = BigDecimal.valueOf(10000);
        validSalesDiscountCount = 10;
        validSalesDiscountPercentage = BigDecimal.valueOf(90);

        printHouse = new PrintHouse<>(
                validEmployeeSalaryIncrementPercentage,
                validPaperIncrementPercentage,
                validBaseSalary,
                incrementEligibleRoles,
                validRevenueTarget,
                validSalesDiscountCount,
                validSalesDiscountPercentage
        );

        employee = new Employee<>(EmployeeType.MANAGER);
        printingPress = new PrintingPress<>(1000, 500, true, 50);
        edition = new Edition<>("Test Edition", 100, Size.A4);
        printedItem = new PrintedItem<>(edition, PaperType.STANDARD, BigDecimal.valueOf(50));
    }

    // 1) Happy path
    @Test
    public void constructorWithValidDataShouldSetupCorrectly() {
        assertNotNull(printHouse);
        assertEquals(validEmployeeSalaryIncrementPercentage, printHouse.getEmployeeSalaryIncrementPercentage());
        assertEquals(validPaperIncrementPercentage, printHouse.getPaperIncrementPercentage());
        assertEquals(validBaseSalary, printHouse.getBaseSalary());
        assertEquals(incrementEligibleRoles, printHouse.getIncrementEligibleRoles());
        assertEquals(validRevenueTarget, printHouse.getRevenueTarget());
        assertEquals(validSalesDiscountCount, printHouse.getSalesDiscountCount());
        assertEquals(validSalesDiscountPercentage, printHouse.getSalesDiscountPercentage());
    }

    @Test
    void addEmployeeWithValidDataShouldSetCorrectly() {
        printHouse.addEmployee(employee);
        assertEquals(1, printHouse.getEmployees().size());
        assertEquals(employee, printHouse.getEmployees().getFirst());
    }

    @Test
    void addPrintingPressWithValidDataShouldSetCorrectly() {
        printHouse.addPrintingPress(printingPress);
        assertEquals(1, printHouse.getPrintingPressList().size());
        assertEquals(printingPress, printHouse.getPrintingPressList().getFirst());
    }

    @Test
    void getTotalCostForPrintWithValidDataShouldSetCorrectly() {
        printHouse.addPrintingPress(printingPress);
        printingPress.printItems(true, printedItem, 10);
        BigDecimal cost = printHouse.getCostForPrintedItemsByPrintingPress(printingPress);
        assertTrue(cost.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void getTotalRevenueWithValidDataShouldSetCorrectly() {
        printHouse.addPrintingPress(printingPress);
        printingPress.printItems(true, printedItem, 15); // Above discount count
        BigDecimal totalRevenue = printHouse.getTotalRevenue();
        assertTrue(totalRevenue.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    public void getTotalCostForEmployeesWithRevenueTargetNotMetWithValidDataShouldSetCorrectly() {
        printHouse.addEmployee(employee);
        BigDecimal totalCost = printHouse.getTotalCostForEmployees();
        assertEquals(BigDecimal.valueOf(1000), totalCost);
    }

    @Test
    void getTotalCostForEmployeesWithRevenueTargetMetWithValidDataShouldSetCorrectly() {
        printHouse.addEmployee(employee);
        printHouse.addPrintingPress(printingPress);
        printingPress.printItems(true, printedItem, 200);
        BigDecimal totalCost = printHouse.getTotalCostForEmployees();
        assertEquals(BigDecimal.valueOf(1000), totalCost);
    }

    // 2) Error Cases
    @Test
    void testConstructor_NullEmployeeSalaryIncrementPercentage() {
        assertThrows(InvalidIncrementPercentage.class, () -> new PrintHouse<>(
                null, // employeeSalaryIncrementPercentage (invalid)
                validPaperIncrementPercentage,
                validBaseSalary,
                incrementEligibleRoles,
                validRevenueTarget,
                validSalesDiscountCount,
                validSalesDiscountPercentage
        ));
    }

    @Test
    void testConstructor_NullPaperIncrementPercentage() {
        assertThrows(InvalidIncrementPercentage.class, () -> new PrintHouse<>(
                validEmployeeSalaryIncrementPercentage,
                null, // paperIncrementPercentage (invalid)
                validBaseSalary,
                incrementEligibleRoles,
                validRevenueTarget,
                validSalesDiscountCount,
                validSalesDiscountPercentage
        ));
    }

    @Test
    void testConstructor_NullBaseSalary() {
        assertThrows(InvalidSalaryException.class, () -> new PrintHouse<>(
                validEmployeeSalaryIncrementPercentage,
                validPaperIncrementPercentage,
                null, // baseSalary (invalid)
                incrementEligibleRoles,
                validRevenueTarget,
                validSalesDiscountCount,
                validSalesDiscountPercentage
        ));
    }

    @Test
    public void constructorWithNullIncrementEligibleRolesShouldThrowException() {
        assertThrows(InvalidIncrementEligibleRoles.class, () -> new PrintHouse<>(
                validEmployeeSalaryIncrementPercentage,
                validPaperIncrementPercentage,
                validBaseSalary,
                null,
                validRevenueTarget,
                validSalesDiscountCount,
                validSalesDiscountPercentage
        ));
    }

    @Test
    public void constructorWithNegativeEmployeeSalaryIncrementPercentageShouldThrowException() {
        assertThrows(InvalidIncrementPercentage.class, () -> new PrintHouse<>(
                BigDecimal.valueOf(-1),
                validPaperIncrementPercentage,
                validBaseSalary,
                incrementEligibleRoles,
                validRevenueTarget,
                validSalesDiscountCount,
                validSalesDiscountPercentage
        ));
    }

    @Test
    void testConstructor_NegativePaperIncrementPercentage() {
        assertThrows(InvalidIncrementPercentage.class, () -> new PrintHouse<>(
                validEmployeeSalaryIncrementPercentage,
                BigDecimal.valueOf(-1), // paperIncrementPercentage (invalid)
                validBaseSalary,
                incrementEligibleRoles,
                validRevenueTarget,
                validSalesDiscountCount,
                validSalesDiscountPercentage
        ));
    }

    @Test
    void testConstructor_NegativeBaseSalary() {
        assertThrows(InvalidSalaryException.class, () -> new PrintHouse<>(
                validEmployeeSalaryIncrementPercentage,
                validPaperIncrementPercentage,
                BigDecimal.valueOf(-1), // baseSalary (invalid)
                incrementEligibleRoles,
                validRevenueTarget,
                validSalesDiscountCount,
                validSalesDiscountPercentage
        ));
    }

    @Test
    void testGetCostForPrintedItemsByPrintingPress_NullPrintingPress() {
        assertThrows(InvalidPrintingPressException.class, () -> printHouse.getCostForPrintedItemsByPrintingPress(null));
    }

    @Test
    void testGetCostForPrintedItemsByPrintingPress_PrintingPressNotInList() {
        assertThrows(InvalidPrintingPressException.class, () -> printHouse.getCostForPrintedItemsByPrintingPress(printingPress));
    }

    // 3) Common Edge Cases
    @Test
    void testConstructor_ZeroEmployeeSalaryIncrementPercentage() {
        printHouse = new PrintHouse<>(
                BigDecimal.ZERO, // employeeSalaryIncrementPercentage (edge case)
                validPaperIncrementPercentage,
                validBaseSalary,
                incrementEligibleRoles,
                validRevenueTarget,
                validSalesDiscountCount,
                validSalesDiscountPercentage
        );

        assertNotNull(printHouse);
        assertEquals(BigDecimal.ZERO, printHouse.getEmployeeSalaryIncrementPercentage());
    }

    @Test
    void testConstructor_ZeroPaperIncrementPercentage() {
        printHouse = new PrintHouse<>(
                validEmployeeSalaryIncrementPercentage,
                BigDecimal.ZERO, // paperIncrementPercentage (edge case)
                validBaseSalary,
                incrementEligibleRoles,
                validRevenueTarget,
                validSalesDiscountCount,
                validSalesDiscountPercentage
        );

        assertNotNull(printHouse);
        assertEquals(BigDecimal.ZERO, printHouse.getPaperIncrementPercentage());
    }

    @Test
    void testConstructor_ZeroBaseSalary() {
        printHouse = new PrintHouse<>(
                validEmployeeSalaryIncrementPercentage,
                validPaperIncrementPercentage,
                BigDecimal.ZERO, // baseSalary (edge case)
                incrementEligibleRoles,
                validRevenueTarget,
                validSalesDiscountCount,
                validSalesDiscountPercentage
        );

        assertNotNull(printHouse);
        assertEquals(BigDecimal.ZERO, printHouse.getBaseSalary());
    }

    @Test
    void testConstructor_EmptyIncrementEligibleRoles() {
        printHouse = new PrintHouse<EmployeeType, PaperType, Size>(
                validEmployeeSalaryIncrementPercentage,
                validPaperIncrementPercentage,
                validBaseSalary,
                List.<EmployeeType>of(), // incrementEligibleRoles (edge case: empty list)
                validRevenueTarget,
                validSalesDiscountCount,
                validSalesDiscountPercentage
        );

        assertNotNull(printHouse);
        assertTrue(printHouse.getIncrementEligibleRoles().isEmpty());
    }

    // 4) Less Common Edge Cases
    @Test
    public void testGetCostForPrintedItemsByPrintingPress_InvalidPaperCost() {

        enum InvalidPaperType implements IPaperTypes {
            INVALID(null);

            private final BigDecimal cost;

            InvalidPaperType(BigDecimal cost) {
                this.cost = cost;
            }

            @Override
            public BigDecimal getCost() {
                return cost;
            }
        }

        // Create a PrintHouse instance
        PrintHouse<EmployeeType, InvalidPaperType, Size> printHouse = new PrintHouse<>(
                validEmployeeSalaryIncrementPercentage,
                validPaperIncrementPercentage,
                validBaseSalary,
                incrementEligibleRoles,
                validRevenueTarget,
                validSalesDiscountCount,
                validSalesDiscountPercentage
        );

        // Local values for PrintingPress constructor
        int maxPaperLoad = 1000;
        int currentPaperLoad = 500;
        boolean isColor = true;
        int maximumPagesPerMinute = 50;

        // Create a PrintingPress instance
        PrintingPress<InvalidPaperType, Size> printingPress = new PrintingPress<>(
                maxPaperLoad,
                currentPaperLoad,
                isColor,
                maximumPagesPerMinute
        );

        printHouse.addPrintingPress(printingPress);

        // Local values for Edition constructor
        String title = "Test Edition";
        int numberOfPages = 100;
        Size size = Size.A4;

        Edition<Size> edition = new Edition<>(title, numberOfPages, size);
        BigDecimal price = BigDecimal.valueOf(50);
        PrintedItem<InvalidPaperType, Size> printedItem = new PrintedItem<>(edition, InvalidPaperType.INVALID, price);
        printingPress.printItems(true, printedItem, 10);
        assertThrows(InvalidPaperCostException.class, () -> printHouse.getCostForPrintedItemsByPrintingPress(printingPress));
    }
}
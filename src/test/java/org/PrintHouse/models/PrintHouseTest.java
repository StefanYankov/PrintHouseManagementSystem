package org.PrintHouse.models;

import org.PrintHouse.models.Contracts.IEmployable;
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
    }

    // 1) Happy path
    @Test
    public void Constructor_ValidData_ShouldSetupCorrectly() {
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
    public void AddEmployee_ValidEmployee_ShouldBeAdded() {
        printHouse.addEmployee(employee);
        assertEquals(1, printHouse.getEmployees().size());
        assertEquals(employee, printHouse.getEmployees().getFirst());
    }

    @Test
    public void AddPrintingPress_ValidPrintingPress_ShouldBeAdded() {
        printHouse.addPrintingPress(printingPress);
        assertEquals(1, printHouse.getPrintingPressList().size());
        assertEquals(printingPress, printHouse.getPrintingPressList().getFirst());
    }

    @Test
    public void getTotalCostForPrint_ValidData_ShouldReturnPositiveCost() {
        printHouse.addPrintingPress(printingPress);
        printingPress.printItems(true, edition, PaperType.STANDARD,BigDecimal.valueOf(50),10);
        BigDecimal cost = printHouse.getCostForPrintedItemsByPrintingPress(printingPress);
        assertTrue(cost.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    public void GetTotalRevenue_ValidData_ShouldReturnPositiveRevenue() {
        printHouse.addPrintingPress(printingPress);
        printingPress.printItems(true, edition, PaperType.STANDARD,BigDecimal.valueOf(50),15); // Above discount count
        BigDecimal totalRevenue = printHouse.getTotalRevenue();
        assertTrue(totalRevenue.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    public void GetTotalCostForEmployees_RevenueTargetNotMet_ShouldReturnBaseSalary() {
        printHouse.addEmployee(employee);
        BigDecimal totalCost = printHouse.getTotalCostForEmployees();
        assertEquals(BigDecimal.valueOf(1000), totalCost);
    }

    @Test
    public void GetTotalCostForEmployees_RevenueTargetMet_ShouldReturnIncrementedSalary() {

        printHouse.addEmployee(employee);
        printHouse.addPrintingPress(printingPress);

        printingPress.printItems(true, edition, PaperType.STANDARD,BigDecimal.valueOf(50), 300);

        BigDecimal totalCost = printHouse.getTotalCostForEmployees();

        assertEquals(new BigDecimal("1100.00"), totalCost);
    }

    // 2) Error Cases
    @Test
    public void Constructor_NullEmployeeSalaryIncrementPercentage_ShouldThrowInvalidIncrementPercentage() {
        assertThrows(InvalidIncrementPercentage.class, () -> new PrintHouse<>(
                null,
                validPaperIncrementPercentage,
                validBaseSalary,
                incrementEligibleRoles,
                validRevenueTarget,
                validSalesDiscountCount,
                validSalesDiscountPercentage
        ));
    }

    @Test
    public void Constructor_NullPaperIncrementPercentage_ShouldThrowInvalidIncrementPercentage() {
        assertThrows(InvalidIncrementPercentage.class, () -> new PrintHouse<>(
                validEmployeeSalaryIncrementPercentage,
                null,
                validBaseSalary,
                incrementEligibleRoles,
                validRevenueTarget,
                validSalesDiscountCount,
                validSalesDiscountPercentage
        ));
    }

    @Test
    public void Constructor_NullBaseSalary_ShouldThrowInvalidSalaryException() {
        assertThrows(InvalidSalaryException.class, () -> new PrintHouse<>(
                validEmployeeSalaryIncrementPercentage,
                validPaperIncrementPercentage,
                null,
                incrementEligibleRoles,
                validRevenueTarget,
                validSalesDiscountCount,
                validSalesDiscountPercentage
        ));
    }

    @Test
    public void Constructor_NullIncrementEligibleRoles_ShouldThrowInvalidIncrementEligibleRoles() {
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
    public void Constructor_NegativeEmployeeSalaryIncrementPercentage_ShouldThrowInvalidIncrementPercentage() {
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
    public void Constructor_NegativePaperIncrementPercentage_ShouldThrowInvalidIncrementPercentage() {
        assertThrows(InvalidIncrementPercentage.class, () -> new PrintHouse<>(
                validEmployeeSalaryIncrementPercentage,
                BigDecimal.valueOf(-1),
                validBaseSalary,
                incrementEligibleRoles,
                validRevenueTarget,
                validSalesDiscountCount,
                validSalesDiscountPercentage
        ));
    }

    @Test
    public void Constructor_NegativeBaseSalary_ShouldThrowInvalidSalaryException() {
        assertThrows(InvalidSalaryException.class, () -> new PrintHouse<>(
                validEmployeeSalaryIncrementPercentage,
                validPaperIncrementPercentage,
                BigDecimal.valueOf(-1),
                incrementEligibleRoles,
                validRevenueTarget,
                validSalesDiscountCount,
                validSalesDiscountPercentage
        ));
    }

    @Test
    public void GetCostForPrintedItemsByPrintingPress_NullPrintingPress_ShouldThrowInvalidPrintingPressException() {
        assertThrows(InvalidPrintingPressException.class, () -> printHouse.getCostForPrintedItemsByPrintingPress(null));
    }

    @Test
    public void GetCostForPrintedItemsByPrintingPress_PrintingPressNotInList_ShouldThrowInvalidPrintingPressException() {
        assertThrows(InvalidPrintingPressException.class, () -> printHouse.getCostForPrintedItemsByPrintingPress(printingPress));
    }

    @Test
    public void addEmployee_DuplicateEmployee_ShouldThrowDuplicateEmployeeException() {
        IEmployable<EmployeeType> operator = new Employee<>(EmployeeType.OPERATOR);
        printHouse.addEmployee(operator);
        assertThrows(InvalidEmployeeException.class, () -> printHouse.addEmployee(operator));


        IEmployable<EmployeeType> manager = new Employee<>(EmployeeType.MANAGER);
        printHouse.addEmployee(manager);
        assertThrows(InvalidEmployeeException.class, () -> printHouse.addEmployee(manager));
    }

    // 3) Common Edge Cases
    @Test
    public void Constructor_ZeroEmployeeSalaryIncrementPercentage_ShouldSetCorrectly() {
        printHouse = new PrintHouse<>(
                BigDecimal.ZERO,
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
    public void Constructor_ZeroPaperIncrementPercentage_ShouldSetCorrectly() {
        printHouse = new PrintHouse<>(
                validEmployeeSalaryIncrementPercentage,
                BigDecimal.ZERO,
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
    public void Constructor_ZeroBaseSalary_ShouldSetCorrectly() {
        printHouse = new PrintHouse<>(
                validEmployeeSalaryIncrementPercentage,
                validPaperIncrementPercentage,
                BigDecimal.ZERO,
                incrementEligibleRoles,
                validRevenueTarget,
                validSalesDiscountCount,
                validSalesDiscountPercentage
        );

        assertNotNull(printHouse);
        assertEquals(BigDecimal.ZERO, printHouse.getBaseSalary());
    }

    @Test
    public void Constructor_EmptyIncrementEligibleRoles_ShouldCreatePrintHouse() {
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
    public void GetCostForPrintedItemsByPrintingPress_InvalidPaperCost_ShouldThrowInvalidPaperCostException() {

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
        printingPress.printItems(true, edition, InvalidPaperType.INVALID,price, 10);
        assertThrows(InvalidPaperCostException.class, () -> printHouse.getCostForPrintedItemsByPrintingPress(printingPress));
    }
}
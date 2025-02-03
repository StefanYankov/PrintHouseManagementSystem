package org.PrintHouse.models;

import org.PrintHouse.models.Contracts.*;
import org.PrintHouse.utilities.contracts.ISerializable;
import org.PrintHouse.utilities.exceptions.*;
import org.PrintHouse.utilities.globalconstants.ExceptionMessages;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * The PrintHouse class represents a printing business with employees, printing presses, and configurable pricing and salary structures.
 *
 * @param <T> Employee role type, extending Enum to ensure predefined roles.
 * @param <P> Paper type, extending Enum and implementing IPaperTypes to represent various paper types.
 * @param <S> Paper size type, extending Enum to define supported sizes for printing.
 */
public class PrintHouse<T extends Enum<T>, P extends Enum<P> & IPaperTypes, S extends Enum<S>> implements IPrintHouse<T, P, S>, ISerializable {
    private final List<IEmployable<T>> employees;
    private final List<IPrintingPress<P, S>> printingPressList;

    /**
     * Percentage increment for employee salaries based on management bonus or performance.
     */
    private BigDecimal employeeSalaryIncrementPercentage;
    // Percentage increment for paper costs based on size or type.
    private BigDecimal paperIncrementPercentage;
    // Roles eligible for salary increments.
    private List<T> incrementEligibleRoles;
    private BigDecimal baseSalary;

    /**
     * Target revenue for roles eligible for salary increments to have the salary incremented.
     */
    private BigDecimal revenueTarget;
    private int salesDiscountCount;
    private BigDecimal salesDiscountPercentage;

    /**
     * Constructs a PrintHouse instance with the specified configuration.
     *
     * @param salaryIncrementBonusPercentage The percentage increment for employee salaries.
     * @param paperIncrementPercentage       The percentage increment for paper costs.
     * @param baseSalary                     The base salary for employees.
     * @param incrementEligibleRoles         The list of roles eligible for salary increments.
     * @param revenueTarget                  The revenue target for the print house.
     * @param discountCount                  The number of discounts allowed for sales promotions.
     * @param discountPercentage             The percentage discount applied during sales promotions.
     */
    public PrintHouse(BigDecimal salaryIncrementBonusPercentage,
                      BigDecimal paperIncrementPercentage,
                      BigDecimal baseSalary,
                      List<T> incrementEligibleRoles,
                      BigDecimal revenueTarget,
                      int discountCount,
                      BigDecimal discountPercentage) {
        this.employees = new ArrayList<IEmployable<T>>();
        this.printingPressList = new ArrayList<IPrintingPress<P, S>>();
        this.setEmployeeSalaryIncrementPercentage(salaryIncrementBonusPercentage);
        this.setBaseSalary(baseSalary);
        this.setIncrementEligibleRoles(incrementEligibleRoles);
        this.setPaperIncrementPercentage(paperIncrementPercentage);
        this.setRevenueTarget(revenueTarget);
        this.setSalesDiscountCount(discountCount);
        this.setSalesDiscountPercentage(discountPercentage);
    }

    @Override
    public void addEmployee(IEmployable<T> employee) {

        if (employee == null){
            throw new InvalidEmployeeException(ExceptionMessages.EMPLOYEE_CANNOT_BE_NULL);
        }
        if (employees.contains(employee)){
            throw new InvalidEmployeeException(ExceptionMessages.EMPLOYEE_IS_ALREADY_ADDED_IN_PRINT_HOUSE);
        }
        employee.setBaseSalary(this.baseSalary);
        employees.add(employee);
    }

    @Override
    public void removeEmployee(IEmployable<T> employee) {
        employees.remove(employee);
    }

    @Override
    public void addPrintingPress(IPrintingPress<P, S> printingPress) {
        printingPressList.add(printingPress);
    }

    @Override
    public void removePrintingPress(IPrintingPress<P, S> printingPress) {
        printingPressList.remove(printingPress);
    }

    @Override
    public List<IEmployable<T>> getEmployees() {
        return employees;
    }

    @Override
    public List<IPrintingPress<P, S>> getPrintingPressList() {
        return printingPressList;
    }

    @Override
    public BigDecimal getPaperIncrementPercentage() {
        return paperIncrementPercentage;
    }

    @Override
    public void setPaperIncrementPercentage(BigDecimal paperIncrementPercentage) {
        if (paperIncrementPercentage == null) {
            throw new InvalidIncrementPercentage(ExceptionMessages.PAPER_INCREMENT_PERCENTAGE_CANNOT_BE_NULL);
        }
        if (paperIncrementPercentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidIncrementPercentage(ExceptionMessages.PAPER_INCREMENT_PERCENTAGE_CANNOT_BE_NEGATIVE_NUMBER);
        }
        this.paperIncrementPercentage = paperIncrementPercentage;
    }

    @Override
    public BigDecimal getEmployeeSalaryIncrementPercentage() {
        return employeeSalaryIncrementPercentage;
    }

    @Override
    public void setEmployeeSalaryIncrementPercentage(BigDecimal employeeSalaryIncrementPercentage) {
        if (employeeSalaryIncrementPercentage == null) {
            throw new InvalidIncrementPercentage(ExceptionMessages.EMPLOYEE_SALARY_INCREMENT_PERCENTAGE_CANNOT_BE_NULL);
        }

        if (employeeSalaryIncrementPercentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidIncrementPercentage(ExceptionMessages.EMPLOYEE_SALARY_INCREMENT_PERCENTAGE_CANNOT_BE_A_NEGATIVE_NUMBER);
        }

        this.employeeSalaryIncrementPercentage = employeeSalaryIncrementPercentage;
    }

    @Override
    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    @Override
    public void setBaseSalary(BigDecimal baseSalary) {
        if (baseSalary == null) {
            throw new InvalidSalaryException(ExceptionMessages.BASE_SALARY_CANNOT_BE_NULL);
        }

        if (baseSalary.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidSalaryException(ExceptionMessages.BASE_SALARY_CANNOT_BE_A_NEGATIVE_NUMBER);
        }
        this.baseSalary = baseSalary;
    }

    @Override
    public List<T> getIncrementEligibleRoles() {
        return incrementEligibleRoles;
    }

    @Override
    public void setIncrementEligibleRoles(List<T> incrementEligibleRoles) {
        if (incrementEligibleRoles == null) {
            throw new InvalidIncrementEligibleRoles(ExceptionMessages.INCREMENT_ELIGIBLE_ROLES_CANNOT_BE_NULL);
        }
        this.incrementEligibleRoles = incrementEligibleRoles;
    }

    @Override
    public BigDecimal getRevenueTarget() {
        return revenueTarget;
    }

    @Override
    public void setRevenueTarget(BigDecimal revenueTarget) {
        if (revenueTarget == null) {
            throw new InvalidRevenueException(ExceptionMessages.REVENUE_CANNOT_BE_NULL);
        }

        if (revenueTarget.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidRevenueException(ExceptionMessages.REVENUE_CANNOT_BE_A_NEGATIVE_NUMBER);
        }

        this.revenueTarget = revenueTarget;
    }

    @Override
    public int getSalesDiscountCount() {
        return salesDiscountCount;
    }

    @Override
    public void setSalesDiscountCount(int salesDiscountCount) {

        if (salesDiscountCount < 0) {
            throw new InvalidSalesCountException(ExceptionMessages.SALES_COUNT_CANNOT_BE_A_NEGATIVE_NUMBER);
        }

        this.salesDiscountCount = salesDiscountCount;
    }

    @Override
    public BigDecimal getSalesDiscountPercentage() {
        return salesDiscountPercentage;
    }

    @Override
    public void setSalesDiscountPercentage(BigDecimal salesDiscountPercentage) {
        if (salesDiscountPercentage == null) {
            throw new InvalidRevenueException(ExceptionMessages.SALES_DISCOUNT_PERCENTAGE_CANNOT_BE_NULL);
        }

        if (salesDiscountPercentage.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidRevenueException(ExceptionMessages.REVENUE_CANNOT_BE_A_NEGATIVE_NUMBER);
        }
        this.salesDiscountPercentage = salesDiscountPercentage;
    }

    /**
     * {@inheritDoc}
     */
    public BigDecimal getTotalCostForPrint() {
        BigDecimal totalCost = BigDecimal.ZERO;
        var printingPressList = this.getPrintingPressList();

        for (IPrintingPress<P, S> printingPress : printingPressList) {
            totalCost = totalCost
                    .add(this.getCostForPrintedItemsByPrintingPress(printingPress));
        }
        return totalCost;
    }

    @Override
    public BigDecimal getCostForPrintedItemsByPrintingPress(IPrintingPress<P, S> printingPress) {

        if (printingPress == null) {
            throw new InvalidPrintingPressException(ExceptionMessages.PRINTING_PRESS_CANNOT_BE_NULL);
        }
        if (!this.getPrintingPressList().contains(printingPress)) {
            throw new InvalidPrintingPressException(MessageFormat
                    .format(ExceptionMessages.PRINTING_PRESS_IS_NOT_PART_OF_THIS_PRINTING_HOUSE, this.getClass().getName()));
        }

        var items = printingPress.getPrintedItems();
        BigDecimal totalCost = BigDecimal.ZERO;
        for (var item : items.keySet()) {
            var size = item.getEdition().getSize();
            var type = item.getPaperType();
            var pageCount = item.getEdition().getNumberOfPages();
            var currentItemCost = this.getCostForSpecificPageSizeAndType(type,  size, pageCount);
            var copies = BigDecimal.valueOf(items.get(item));
            totalCost = totalCost.add(currentItemCost).multiply(copies);
        }

        return totalCost;
    }

    /**
     * Calculates the total cost of employees' salaries without applying any modifiers.
     * <p>
     * This method is useful when no conditions need to be applied to employee salaries,
     * such as when the company's revenue doesn't meet the threshold for salary increments.
     *
     * @return The total cost of all employees' base salaries.
     */
    @Override
    public BigDecimal getTotalCostForEmployees() {

        var totalRevenue = this.getTotalRevenue();
        // check if the revenue target is hi and if that is the case increments only the salary of the eligible roles with the percentage
        if (this.revenueTarget.compareTo(totalRevenue) < 0) {
            BigDecimal output = this.getEmployees().stream()
                    .map(x -> {
                        T role = x.getEmployeeType();
                        BigDecimal salary = x.getBaseSalary();
                        if (this.incrementEligibleRoles.contains(role)) {
                            salary = this.applySalaryIncrement(salary, this.employeeSalaryIncrementPercentage);
                            return salary;
                        }
                        return salary;

                    }).reduce(BigDecimal.ZERO, (acc, salary) -> acc.add(salary));
            return output;
        }

        return this.getEmployees()
                .stream()
                .map(employee -> employee.getBaseSalary())
                .reduce(BigDecimal.ZERO, (acc, salary) -> acc.add(salary));
    }

    @Override
    public BigDecimal getTotalRevenue() {

        BigDecimal output = this.printingPressList.stream()
                .flatMap(pp -> pp.getPrintedItems().entrySet().stream())
                .map(printedItemWithCount -> {
                    IPrintedItem<P, S> printedItem = printedItemWithCount.getKey();
                    int count = printedItemWithCount.getValue();
                    BigDecimal itemPrice = printedItem.getPrice();
                    if (count > this.salesDiscountCount) {
                        itemPrice = itemPrice.multiply(this.salesDiscountPercentage.divide(BigDecimal.valueOf(100)));
                    }
                    return itemPrice.multiply(BigDecimal.valueOf(count));
                })
                .reduce(BigDecimal.ZERO, (acc, price) -> acc.add(price));

        return output;
    }

    /**
     * Calculates the cost for printing a specific page size and type, given the base cost
     * and incremental cost adjustments for different sizes. The cost is determined based
     * on the paper type, size, and the number of pages to be printed.
     *
     * @param paperType     The type of paper to be used (e.g., STANDARD, GLOSSY, NEWSPAPER).
     *                      Must implement the {@link IPaperTypes} interface.
     * @param requestedSize The size of the page to be printed (e.g., A5, A4, A3, etc.).
     *                      This should be an enum value that corresponds to the declared sizes.
     *                      Smallest size is always first in the enum.
     * @param pageCount     The number of pages to be printed. Must be greater than zero.
     * @return The total cost for the requested size, paper type, and page count.
     */
    private BigDecimal getCostForSpecificPageSizeAndType(P paperType, S requestedSize, int pageCount) {

        if (pageCount <= 0) {
            throw new InvalidNumberOfPagesException(ExceptionMessages.NUMBER_OF_PAGES_MUST_BE_GREATER_THAN_ZERO);
        }

        if (paperType == null) {
            throw new InvalidPaperTypeException(ExceptionMessages.PAPER_TYPE_CANNOT_BE_NULL);
        }

        BigDecimal basePaperCost = paperType.getCost();

        if (basePaperCost == null) {
            throw new InvalidPaperCostException(ExceptionMessages.PAPER_COST_CANNOT_BE_NULL);
        }

        if (basePaperCost.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPaperCostException(ExceptionMessages.PAPER_COST_MUST_BE_A_POSITIVE_NUMBER);
        }

        // Get the enum constants (sizes) to determine the base size and the requested size index
        S[] enumConstants = requestedSize.getDeclaringClass().getEnumConstants();

        if (enumConstants == null) {
            throw new InvalidPageSizeException(ExceptionMessages.INVALID_PAGE_SIZE);
        }

        // The first enum constant is the base size, and its cost will be the base cost
        BigDecimal currentCost = basePaperCost;

        // Iterate through the enum constants and apply the increment percentage for each size
        for (S size : enumConstants) {
            // When the requested size is found, return the cost for that size
            if (size == requestedSize) {
                currentCost = currentCost.multiply(BigDecimal.valueOf(pageCount));
                break;
            }

            // Otherwise, increment the cost by the percentage
            currentCost = currentCost.multiply(BigDecimal.ONE.add(this.incrementPercentageInPercent(this.getPaperIncrementPercentage())));
        }

        return currentCost;
    }

    // converts raw percentage - for example 10 - to an actual percentage that can be used in calculations - 10/100
    private BigDecimal incrementPercentageInPercent(BigDecimal paperIncrementPercentage) {
        return paperIncrementPercentage.divide(BigDecimal.valueOf(100));
    }

    /**
     * Applies a salary increment to the base salary based on the specified percentage.
     *
     * @param baseSalary          the original salary of the employee
     * @param incrementPercentage the percentage increment to be applied
     * @return the adjusted salary after applying the increment
     */
    private BigDecimal applySalaryIncrement(BigDecimal baseSalary, BigDecimal incrementPercentage) {
        return baseSalary.multiply(BigDecimal.ONE.add(incrementPercentage.divide(BigDecimal.valueOf(100))));
    }
}
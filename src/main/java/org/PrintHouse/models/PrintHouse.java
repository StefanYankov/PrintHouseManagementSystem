package org.PrintHouse.models;

import org.PrintHouse.models.Contracts.IEmployable;
import org.PrintHouse.models.Contracts.IPaperTypes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a printing house with employees, printing presses, and cost/revenue management.
 *
 * @param <T> The enum type representing employee roles.
 * @param <P> The enum type representing paper types, must implement {@link IPaperTypes}.
 */
public class PrintHouse<T extends Enum<T>, P extends Enum<P> & IPaperTypes> {
    private final List<IEmployable<T>> employees;
    private final List<PrintingPress> printingPressList;

    /**
     * Percentage increment for employee salaries based on management bonus or performance.
     */
    private BigDecimal employeeSalaryIncrementPercentage;

    /**
     * Percentage increment for paper costs based on size or type.
     */
    private BigDecimal paperIncrementPercentage;
    /**
     * Roles eligible for salary increments.
     */
    private final List<T> incrementEligibleRoles;
    private BigDecimal baseSalary;

    /**
     * Target revenue for roles eligible for salary increments to have the salary incremented.
     */
    private BigDecimal revenueTarget;
    private int salesDiscountCount;
    private BigDecimal salesDiscountPercentage;

    /**
     * Constructs a new PrintHouse instance with specified parameters.
     *
     * @param salaryIncrementBonusPercentage  Percentage increment for bonuses eligible roles.
     * @param paperIncrementPercentage   Percentage increment for paper costs.
     * @param baseSalary                 Base salary for employees.
     * @param incrementEligibleRoles     Roles eligible for salary increments.
     * @param revenueTarget              Revenue target for the printing house.
     * @param discountCount              Threshold count for sales discounts.
     * @param discountPercentage         Percentage discount applied to sales above the threshold.
     */
    public PrintHouse(BigDecimal salaryIncrementBonusPercentage,
                      BigDecimal paperIncrementPercentage,
                      BigDecimal baseSalary,
                      List<T> incrementEligibleRoles,
                      BigDecimal revenueTarget,
                      int discountCount,
                      BigDecimal discountPercentage) {
        this.employees = new ArrayList<IEmployable<T>>();
        this.printingPressList = new ArrayList<PrintingPress>();
        this.employeeSalaryIncrementPercentage = salaryIncrementBonusPercentage;
        this.baseSalary = baseSalary;
        this.incrementEligibleRoles = incrementEligibleRoles;
        this.paperIncrementPercentage = paperIncrementPercentage;
        this.revenueTarget = revenueTarget;
        this.salesDiscountCount = discountCount;
        this.salesDiscountPercentage = discountPercentage;
    }

    public void addEmployee(IEmployable<T> employee) {
        employee.setBaseSalary(this.baseSalary);
        employees.add(employee);
    }

    public void removeEmployee(IEmployable<T> employee) {
        employees.remove(employee);
    }

    public void addPrintingPress(PrintingPress printingPress) {
        printingPressList.add(printingPress);
    }

    public void removePrintingPress(PrintingPress printingPress) {
        printingPressList.remove(printingPress);
    }

    public List<IEmployable<T>> getEmployees() {
        return employees;
    }

    public List<PrintingPress> getPrintingPressList() {
        return printingPressList;
    }

    public BigDecimal getPaperIncrementPercentage() {
        return paperIncrementPercentage;
    }

    public void setPaperIncrementPercentage(BigDecimal paperIncrementPercentage) {
        this.paperIncrementPercentage = paperIncrementPercentage;
    }

    public BigDecimal getEmployeeSalaryIncrementPercentage() {
        return employeeSalaryIncrementPercentage;
    }

    public void setEmployeeSalaryIncrementPercentage(BigDecimal employeeSalaryIncrementPercentage) {
        this.employeeSalaryIncrementPercentage = employeeSalaryIncrementPercentage;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    /**
     * Calculates the total cost of printed items across all printing presses.
     *
     * @return Total cost of printed items.
     */
    public BigDecimal getTotalCostForPrint() {
        BigDecimal totalCost = BigDecimal.ZERO;
        var printingPressList = this.getPrintingPressList();

        for (PrintingPress printingPress : printingPressList) {
            totalCost = totalCost
                    .add(this.getCostForPrintedItemsByPrintingPress(printingPress));
        }
        return totalCost;
    }

    public BigDecimal getCostForPrintedItemsByPrintingPress(PrintingPress printingPress) {

        if (printingPress == null) {
            return BigDecimal.ZERO;
        }
        if (!this.getPrintingPressList().contains(printingPress)) {
            throw new IllegalArgumentException("printingPress is not in printingPressList");
        }

        var items = printingPress.getPrintedItems();
        BigDecimal totalCost = BigDecimal.ZERO;
        for (var item : items.keySet()) {
            var size = item.getEdition().getSize();
            var type = item.getPaperType();
            var pageCount = item.getEdition().getNumberOfPages();
            var currentItemCost = this.getCostForSpecificPageSizeAndType((P) type, (T) size, pageCount);
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
    public BigDecimal getTotalCostForEmployees() {

        if (this.revenueTarget.compareTo(this.getTotalRevenue()) > 0) {
            BigDecimal output = this.getEmployees().stream()
                    .map(x -> {
                        T role = x.getEmployeeType();
                        BigDecimal salary = x.getBaseSalary();
                        if (this.incrementEligibleRoles.contains(role)) {
                            return this.applySalaryIncrement(salary, this.employeeSalaryIncrementPercentage);
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

    public BigDecimal getTotalRevenue() {

        BigDecimal output = this.printingPressList.stream()
                .flatMap(pp -> pp.getPrintedItems().entrySet().stream())
                .map(printedItemWithCount -> {
                    PrintedItem printedItem = printedItemWithCount.getKey();
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
     * @throws IllegalArgumentException If:
     *                                  <ul>
     *                                      <li>`pageCount` is less than or equal to zero.</li>
     *                                      <li>The base cost for the specified `paperType` is null or invalid (less than or equal to zero).</li>
     *                                      <li>The requested size is not part of the defined enum constants for the size.</li>
     *                                  </ul>
     */
    private BigDecimal getCostForSpecificPageSizeAndType(P paperType, T requestedSize, int pageCount) {

        if (pageCount <= 0) {
            throw new IllegalArgumentException("Page count must be greater than zero.");
        }

        BigDecimal basePaperCost = paperType.getCost(paperType);

        if (basePaperCost == null || basePaperCost.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Base cost for paper type " + paperType + " is invalid.");
        }

        // Get the enum constants (sizes) to determine the base size and the requested size index
        T[] enumConstants = requestedSize.getDeclaringClass().getEnumConstants();

        // The first enum constant is the base size, and its cost will be the base cost
        BigDecimal currentCost = basePaperCost;

        // Iterate through the enum constants and apply the increment percentage for each size
        for (T size : enumConstants) {
            // When the requested size is found, return the cost for that size
            if (size == requestedSize) {
                return currentCost.multiply(BigDecimal.valueOf(pageCount));
            }

            // Otherwise, increment the cost by the percentage
            currentCost = currentCost.multiply(BigDecimal.ONE.add(this.incrementPercentageInPercent(this.getPaperIncrementPercentage())));
        }

        throw new IllegalArgumentException("Requested size " + requestedSize + " is not part of the enum.");
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
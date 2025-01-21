package org.PrintHouse.models;

import org.PrintHouse.models.Contracts.IEmployable;
import org.PrintHouse.models.Contracts.IPaperTypes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PrintHouse<T extends Enum<T>, P extends Enum<P> & IPaperTypes> {
    private final List<IEmployable<T>> employees;
    private final List<PrintingPress> printingPressList;
    private BigDecimal employeeSalaryIncrementPercentage;
    private BigDecimal paperIncrementPercentage;
    private BigDecimal baseSalary;
    private BigDecimal revenueTarget;
    private int salesDiscountCount;
    private BigDecimal salesDiscountPercentage;

    public PrintHouse(BigDecimal managementBonusPercentage,
                      BigDecimal paperIncrementPercentage,
                      BigDecimal baseSalary,
                      BigDecimal revenueTarget,
                      int discountCount,
                      BigDecimal discountPercentage) {
        this.employees = new ArrayList<IEmployable<T>>();
        this.printingPressList = new ArrayList<PrintingPress>();
        this.employeeSalaryIncrementPercentage = managementBonusPercentage;
        this.baseSalary = baseSalary;
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
            var pageCount = item.getNumberOfPages();
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
            return this.getTotalCostForEmployees((T) EmployeeType.MANAGER);

        }

        return this.getEmployees()
                .stream()
                .map(employee -> employee.getBaseSalary())
                .reduce(BigDecimal.ZERO, (acc, salary) -> acc.add(salary));
    }

    public BigDecimal getTotalRevenue() {

        var collection = this.printingPressList.stream()
                .flatMap(pp -> pp.getPrintedItems().entrySet().stream())
                .toList();

        var output = this.printingPressList.stream()
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

    /**
     * Calculates the total cost of all employees, adjusting their salaries based on the provided employee type.
     *
     * @param employeeType the employee type used to determine salary adjustments
     * @return the total cost of all employees
     */
    private BigDecimal getTotalCostForEmployees(T employeeType) {
        return this.getEmployees()
                .stream()
                .map(employee -> adjustSalaryBasedOnFilter(employee, employeeType))
                .reduce(BigDecimal.ZERO, (acc, salary) -> acc.add(salary));
    }

    // converts raw percentage - for example 10 - to an actual percentage that can be used in calculations - 10/100
    private BigDecimal incrementPercentageInPercent(BigDecimal paperIncrementPercentage) {
        return paperIncrementPercentage.divide(BigDecimal.valueOf(100));
    }

    /**
     * Adjusts the salary of an employee based on the given employee type.
     *
     * @param employee     the employee whose salary is being adjusted
     * @param employeeType the type of employee used for determining salary adjustments
     * @return the adjusted salary of the employee
     */
    private BigDecimal adjustSalaryBasedOnFilter(IEmployable<T> employee, T employeeType) {
        // Apply the increment if the employee matches the specified type
        if (employee.getEmployeeType().equals(employeeType)) {
            return applySalaryIncrement(employee.getBaseSalary(), this.getEmployeeSalaryIncrementPercentage());
        }
        // Return the base salary if no adjustment is needed
        return employee.getBaseSalary();
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
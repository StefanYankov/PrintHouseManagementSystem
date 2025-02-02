package org.PrintHouse.models.Contracts;

import org.PrintHouse.utilities.contracts.ISerializable;

import java.math.BigDecimal;
import java.util.List;

/**
 * The PrintHouse class represents a printing business with employees, printing presses, and configurable pricing and salary structures.
 *
 * @param <T> Employee role type, extending Enum to ensure predefined roles.
 * @param <P> Paper type, extending Enum and implementing {@link IPaperTypes} to represent various paper types.
 * @param <S> Paper size type, extending Enum to define supported sizes for printing.
 */
public interface IPrintHouse<T extends Enum<T>, P extends Enum<P> & IPaperTypes, S extends Enum<S>> extends ISerializable {

    /**
     * Adds an employee to the print house.
     *
     * @param employee The employee to add.
     */
    public void addEmployee(IEmployable<T> employee);

    /**
     * Removes an employee from the print house.
     *
     * @param employee The employee to remove.
     */
    public void removeEmployee(IEmployable<T> employee);

    /**
     * Adds a printing press to the print house.
     *
     * @param printingPress The printing press to add.
     */
    public void addPrintingPress(IPrintingPress<P, S> printingPress);

    /**
     * Removes a printing press from the print house.
     *
     * @param printingPress The printing press to remove.
     */
    public void removePrintingPress(IPrintingPress<P, S> printingPress);

    /**
     * Gets the employee salary increment percentage.
     *
     * @return The employee salary increment percentage as a {@link BigDecimal}.
     */
    public List<IEmployable<T>> getEmployees();

    /**
     * Gets the list of printing presses in the print house.
     *
     * @return A list of printing presses.
     */
    public List<IPrintingPress<P, S>> getPrintingPressList();

    /**
     * Gets the paper increment percentage the whole print house.
     *
     * @return The paper increment percentage as a {@link BigDecimal}.
     */
    public BigDecimal getPaperIncrementPercentage();

    /**
     * Sets the paper increment percentage for the whole print house.
     *
     * @param paperIncrementPercentage The new paper increment percentage.
     */
    public void setPaperIncrementPercentage(BigDecimal paperIncrementPercentage);

    /**
     * Gets the employee salary increment percentage.
     *
     * @return The employee salary increment percentage as a {@link BigDecimal}.
     */
    public BigDecimal getEmployeeSalaryIncrementPercentage();

    /**
     * Sets the employee salary increment percentage.
     *
     * @param employeeSalaryIncrementPercentage The new employee salary increment percentage.
     */
    public void setEmployeeSalaryIncrementPercentage(BigDecimal employeeSalaryIncrementPercentage);

    /**
     * Gets the base salary for employees.
     *
     * @return The base salary as a {@link BigDecimal}.
     */
    public BigDecimal getBaseSalary();

    /**
     * Sets the base salary for employees.
     *
     * @param baseSalary The new base salary.
     */
    public void setBaseSalary(BigDecimal baseSalary);

    /**
     * Gets the list of roles eligible for salary increments.
     *
     * @return A list of roles eligible for salary increments.
     */
    public List<T> getIncrementEligibleRoles();

    /**
     * Sets the list of roles eligible for salary increments.
     *
     * @param incrementEligibleRoles The new list of roles eligible for salary increments.
     */
    public void setIncrementEligibleRoles(List<T> incrementEligibleRoles);

    /**
     * Gets the revenue target for the print house.
     *
     * @return The revenue target as a {@link BigDecimal}.
     */
    public BigDecimal getRevenueTarget();

    /**
     * Sets the revenue target for the print house.
     *
     * @param revenueTarget The new revenue target.
     */
    public void setRevenueTarget(BigDecimal revenueTarget);

    /**
     * Gets the number of sales required to qualify for a discount.
     *
     * @return The number of sales required for a discount.
     */
    public int getSalesDiscountCount();

    /**
     * Sets the number of sales required to qualify for a discount.
     *
     * @param salesDiscountCount The new number of sales required for a discount.
     */
    public void setSalesDiscountCount(int salesDiscountCount);

    /**
     * Gets the discount percentage applied to sales above the discount count.
     *
     * @return The discount percentage as a {@link BigDecimal}.
     */
    public BigDecimal getSalesDiscountPercentage();

    /**
     * Sets the discount percentage applied to sales above the discount count.
     *
     * @param salesDiscountPercentage The new discount percentage.
     */
    public void setSalesDiscountPercentage(BigDecimal salesDiscountPercentage);

    /**
     * Calculates the cost for printed items by a specific printing press.
     *
     * @param printingPress The printing press to calculate the cost for.
     * @return The cost as a {@link BigDecimal}.
     */
    public BigDecimal getCostForPrintedItemsByPrintingPress(IPrintingPress<P, S> printingPress);

    /**
     * Calculates the total cost for all employees, including salary increments if applicable.
     *
     * @return The total cost as a {@link BigDecimal}.
     */
    public BigDecimal getTotalCostForEmployees();

    /**
     * Calculates the total revenue generated by the print house.
     *
     * @return The total revenue as a {@link BigDecimal}.
     */
    public BigDecimal getTotalRevenue();

    /**
     * Calculates the total cost of printed items across all printing presses.
     *
     * @return Total cost of printed items.
     */
    public BigDecimal getTotalCostForPrint();
}

package org.PrintHouse.models;

import java.math.BigDecimal;

/**
 * Represents the base type of employee within the organization.
 *
 * <p>This class defines the title of the employee and an optional
 * salary modifier (as a percentage) to adjust their salary based
 * on their role. For example, managers may receive a higher salary
 * due to their responsibilities.</p>
 */
public class EmployeeType {
    /**
     * The title of the employee (e.g., "Manager", "Operator").
     */
    private String title;
    /**
     * A percentage modifier for the employee's salary.
     *
     * <p>This value adjusts the employee's base salary depending on their role.
     * For example, managers may have a positive modifier to indicate a higher salary.</p>
     */
    private BigDecimal salaryModifierInPercentage;

    public EmployeeType(String title, BigDecimal salaryModifierInPercentage) {
        this.title = title;
        this.salaryModifierInPercentage = salaryModifierInPercentage;
    }

    /**
     * Constructs an EmployeeType with a specified title and a default salary modifier of 0%.
     *
     * @param title The title of the employee (e.g., "Operator").
     */
    public EmployeeType(String title) {
        this(title, BigDecimal.ZERO);
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getSalaryModifierInPercentage() {
        return salaryModifierInPercentage;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the salary modifier in percentage.
     *
     * <p>This value adjusts the employee's salary based on their role.
     * It should be non-null and a realistic percentage value.</p>
     *
     * @param salaryModifierInPercentage The new salary modifier percentage.
     * @throws IllegalArgumentException If the value is null or not within a valid range.
     */
    public void setSalaryModifierInPercentage(BigDecimal salaryModifierInPercentage) {

        if (salaryModifierInPercentage == null) {
            // TODO: Add custom error messaging
            throw new IllegalArgumentException("salaryModifierInPercentage cannot be null");
        }
        this.salaryModifierInPercentage = salaryModifierInPercentage;
    }
}

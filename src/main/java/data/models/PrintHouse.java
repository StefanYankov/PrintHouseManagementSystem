package data.models;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the print house entity with all its configurations and collections.
 */
public class PrintHouse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private List<Employee> employees;
    private List<PrintingPress> printingPresses;
    private BigDecimal employeeSalaryIncrementPercentage;
    private BigDecimal paperIncrementPercentage;
    private BigDecimal baseSalary;
    private List<EmployeeType> incrementEligibleRoles;
    private BigDecimal revenueTarget;
    private int salesDiscountCount;
    private BigDecimal salesDiscountPercentage;

    public PrintHouse() {
        this.employees = new ArrayList<>();
        this.printingPresses = new ArrayList<>();
    }

    public PrintHouse(BigDecimal employeeSalaryIncrementPercentage,
                      BigDecimal paperIncrementPercentage,
                      BigDecimal baseSalary,
                      List<EmployeeType> incrementEligibleRoles,
                      BigDecimal revenueTarget,
                      int salesDiscountCount,
                      BigDecimal salesDiscountPercentage) {
        this.employees = new ArrayList<>();
        this.printingPresses = new ArrayList<>();
        this.employeeSalaryIncrementPercentage = employeeSalaryIncrementPercentage;
        this.paperIncrementPercentage = paperIncrementPercentage;
        this.baseSalary = baseSalary;
        this.incrementEligibleRoles = incrementEligibleRoles;
        this.revenueTarget = revenueTarget;
        this.salesDiscountCount = salesDiscountCount;
        this.salesDiscountPercentage = salesDiscountPercentage;
    }

    public List<Employee> getEmployees() { return employees; }
    public void setEmployees(List<Employee> employees) {
        if (employees == null) {
            throw new IllegalArgumentException("Employees list cannot be null.");
        }
        this.employees = employees;
    }

    public List<PrintingPress> getPrintingPresses() { return printingPresses; }
    public void setPrintingPresses(List<PrintingPress> printingPresses) {
        if (printingPresses == null) {
            throw new IllegalArgumentException("Printing presses list cannot be null.");
        }
        this.printingPresses = printingPresses;
    }

    public BigDecimal getEmployeeSalaryIncrementPercentage() { return employeeSalaryIncrementPercentage; }
    public void setEmployeeSalaryIncrementPercentage(BigDecimal employeeSalaryIncrementPercentage) {
        this.employeeSalaryIncrementPercentage = employeeSalaryIncrementPercentage;
    }

    public BigDecimal getPaperIncrementPercentage() { return paperIncrementPercentage; }
    public void setPaperIncrementPercentage(BigDecimal paperIncrementPercentage) {
        this.paperIncrementPercentage = paperIncrementPercentage;
    }

    public BigDecimal getBaseSalary() { return baseSalary; }
    public void setBaseSalary(BigDecimal baseSalary) { this.baseSalary = baseSalary; }

    public List<EmployeeType> getIncrementEligibleRoles() { return incrementEligibleRoles; }
    public void setIncrementEligibleRoles(List<EmployeeType> incrementEligibleRoles) {
        this.incrementEligibleRoles = incrementEligibleRoles;
    }

    public BigDecimal getRevenueTarget() { return revenueTarget; }
    public void setRevenueTarget(BigDecimal revenueTarget) { this.revenueTarget = revenueTarget; }

    public int getSalesDiscountCount() { return salesDiscountCount; }
    public void setSalesDiscountCount(int salesDiscountCount) { this.salesDiscountCount = salesDiscountCount; }

    public BigDecimal getSalesDiscountPercentage() { return salesDiscountPercentage; }
    public void setSalesDiscountPercentage(BigDecimal salesDiscountPercentage) {
        this.salesDiscountPercentage = salesDiscountPercentage;
    }

    @Override
    public String toString() {
        return "PrintHouse{" +
                "employees=" + employees +
                ", printingPresses=" + printingPresses +
                ", employeeSalaryIncrementPercentage=" + employeeSalaryIncrementPercentage +
                ", paperIncrementPercentage=" + paperIncrementPercentage +
                ", baseSalary=" + baseSalary +
                ", incrementEligibleRoles=" + incrementEligibleRoles +
                ", revenueTarget=" + revenueTarget +
                ", salesDiscountCount=" + salesDiscountCount +
                ", salesDiscountPercentage=" + salesDiscountPercentage +
                '}';
    }
}
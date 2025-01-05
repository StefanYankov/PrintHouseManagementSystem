package org.PrintHouse.models;

import org.PrintHouse.models.Contracts.IEmployable;

import java.math.BigDecimal;

public class Employee implements IEmployable {

    private static BigDecimal basePay;
    private EmployeeType employeeType;

    public Employee(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public static BigDecimal getBasePay() {
        return basePay;
    }

    public static void setBasePay(BigDecimal basePay) {
        // TODO: change c
        if (basePay.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("BasePay must be greater than zero");
        }

        if (basePay.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("BasePay must be greater than zero");
        }

        Employee.basePay = basePay;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    @Override
    public BigDecimal getSalary() {
        BigDecimal meritSalary = Employee.getBasePay().multiply(employeeType.getSalaryModifierInPercentage());
        return Employee.getBasePay().add(meritSalary);
    }
}

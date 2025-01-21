package org.PrintHouse.models;

import org.PrintHouse.models.Contracts.IEmployable;

import java.math.BigDecimal;

//  Single Table Inheritance will be used so we don't have a new table for every derived class

public class Employee<T> implements IEmployable<T>  {
    private BigDecimal baseSalary; // per specification this could be dropped as the base salary will be the same for every employee
    private T employeeType;

    public Employee() {}
    public Employee(T employeeType) {
        this.employeeType = employeeType;
    }

    @Override
    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    @Override
    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    @Override
    public T getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(T employeeType) {
        this.employeeType = employeeType;
    }
}

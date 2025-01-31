package org.PrintHouse.models;

import org.PrintHouse.models.Contracts.IEmployable;
import org.PrintHouse.utilities.exceptions.InvalidEmployeeException;
import org.PrintHouse.utilities.exceptions.InvalidSalaryException;
import org.PrintHouse.utilities.globalconstants.ExceptionMessages;

import java.math.BigDecimal;

//  Single Table Inheritance will be used so we don't have a new table for every derived class

public class Employee<T> implements IEmployable<T>  {
    private BigDecimal baseSalary; // per specification this could be dropped as the base salary will be the same for every employee
    private T employeeType;

    public Employee(T employeeType) {
        this.setEmployeeType(employeeType);
        this.setBaseSalary(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    @Override
    public void setBaseSalary(BigDecimal baseSalary) {
        if (baseSalary == null){
            throw new InvalidSalaryException(ExceptionMessages.BASE_SALARY_CANNOT_BE_NULL);
        }

        if (baseSalary.compareTo(BigDecimal.ZERO) < 0){
            throw new InvalidSalaryException(ExceptionMessages.BASE_SALARY_CANNOT_BE_A_NEGATIVE_NUMBER);
        }

        this.baseSalary = baseSalary;
    }

    @Override
    public T getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(T employeeType) {
        if (employeeType == null){
            throw new InvalidEmployeeException(ExceptionMessages.EMPLOYEE_CANNOT_BE_NULL);
        }

        this.employeeType = employeeType;
    }

}

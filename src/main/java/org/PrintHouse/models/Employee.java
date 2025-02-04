package org.PrintHouse.models;

import org.PrintHouse.models.Contracts.IEmployable;
import org.PrintHouse.utilities.exceptions.InvalidEmployeeException;
import org.PrintHouse.utilities.exceptions.InvalidSalaryException;
import org.PrintHouse.utilities.globalconstants.ExceptionMessages;

import java.io.Serial;
import java.math.BigDecimal;

//  Single Table Inheritance will be used so we don't have a new table for every derived class
/**
 * The {@code Employee<T>} class represents an employee in the system. It implements the {@code IEmployable<T>}
 * interface and provides functionality to manage an employee's base salary and type. The generic type {@code T}
 * is constrained to be an {@code Enum}, ensuring that only enum types can be used as employee types.
 *
 * @param <T> the type of the employee, which must be an enum
 */
public class Employee<T extends Enum<T>> implements IEmployable<T>  {

    @Serial
    private static final long serialVersionUID = 1L;

    private BigDecimal baseSalary; // per specification this could be dropped as the base salary will be the same for every employee
    private T employeeType;

    /**
     * Constructs a new {@code Employee} instance with the specified employee type and a base salary of zero.
     *
     * @param employeeType the type of the employee, as an instance of {@code T}
     * @throws InvalidEmployeeException if the employee type is null
     */
    public Employee(T employeeType) {
        this.setEmployeeType(employeeType);
        this.setBaseSalary(BigDecimal.ZERO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public T getEmployeeType() {
        return employeeType;
    }

    /**
     * {@inheritDoc}
     */
    public void setEmployeeType(T employeeType) {
        if (employeeType == null){
            throw new InvalidEmployeeException(ExceptionMessages.EMPLOYEE_CANNOT_BE_NULL);
        }

        this.employeeType = employeeType;
    }


    @Override
    public String toString() {
        return "Employee{" +
                "baseSalary=" + baseSalary +
                ", employeeType=" + employeeType +
                '}';
    }
}

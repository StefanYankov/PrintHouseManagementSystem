package org.PrintHouse.models.Contracts;

import org.PrintHouse.utilities.contracts.ISerializable;

import java.math.BigDecimal;

/**
 * The {@code IEmployable<T>} interface defines the contract for classes that represent employable entities.
 * The generic type {@code T} is constrained to be an {@code Enum}, ensuring that only enum types can be used
 * as employee types.
 *
 * @param <T> the type of the employee, which must be an enum
 */
public interface IEmployable<T extends Enum<T>> extends ISerializable {
    /**
     * Returns the base salary of the employable entity.
     *
     * @return the base salary as a {@code BigDecimal}
     */
    public BigDecimal getBaseSalary();

    /**
     * Sets the base salary of the employable entity.
     *
     * @param baseSalary the base salary to set, as a {@code BigDecimal}
     */
    public void setBaseSalary(BigDecimal baseSalary);

    /**
     * Returns the type of the employable entity.
     *
     * @return the employee type as an instance of {@code T}
     */
    public T getEmployeeType();
}

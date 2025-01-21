package org.PrintHouse.models.Contracts;

import java.math.BigDecimal;

public interface IEmployable<T> {
    public BigDecimal getBaseSalary();
    public void setBaseSalary(BigDecimal baseSalary);
    public T getEmployeeType();
}

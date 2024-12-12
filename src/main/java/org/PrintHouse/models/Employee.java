package org.PrintHouse.models;

import java.math.BigDecimal;

public abstract class Employee implements IEmployable {

    private BigDecimal basePay;

    public Employee(BigDecimal basePay) {
        this.basePay = basePay;
    }

    protected BigDecimal getBasePay() {
        return basePay;
    }

}

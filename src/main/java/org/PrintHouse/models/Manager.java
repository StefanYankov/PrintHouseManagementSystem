package org.PrintHouse.models;

import java.math.BigDecimal;

public class Manager extends Employee{

    //TODO: extra pay for printing house performance
    public Manager(BigDecimal basePay) {
        super(basePay);
    }


    @Override
    public BigDecimal getSalary() {
        return this.getBasePay();
    }
}

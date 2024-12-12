package org.PrintHouse.models;

import java.math.BigDecimal;

public class PrintingPressOperator extends Employee {
    public PrintingPressOperator(BigDecimal basePay) {
        super(basePay);
    }


    @Override
    public BigDecimal getSalary() {
        return this.getBasePay();
    }
}

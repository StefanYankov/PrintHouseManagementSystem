package org.PrintHouse;

import org.PrintHouse.models.*;
import org.PrintHouse.models.Contracts.IPaperTypable;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

        // Sizes
        // TODO: move to a config file
        BigDecimal baseCost = new BigDecimal(1000);
        PageSize.setIncrementalPercentage(new BigDecimal(10));
        PageSize<Sizes> pageSize = new PageSize<>(Sizes.class, baseCost);
        System.out.println(pageSize.toString());
//        // Paper
//        IPaperTypable paperType = new PaperType();
//
//        // Employee
//        Employee.setBasePay(BigDecimal.valueOf(1000));
//        EmployeeType operator = new EmployeeType("Operator");
//        EmployeeType manager = new EmployeeType("Manager");
//        Employee operator1 = new Employee(operator);
//        Employee operator2 = new Employee(operator);
//        Employee manager1 = new Employee(manager);



    }
}
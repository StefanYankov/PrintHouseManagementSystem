package org.PrintHouse;

import org.PrintHouse.models.*;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {


        // Paper


        // Employee
        Employee.setBasePay(BigDecimal.valueOf(1000));
        EmployeeType operator = new EmployeeType("Operator");
        EmployeeType manager = new EmployeeType("Manager");
        Employee operator1 = new Employee(operator);
        Employee operator2 = new Employee(operator);
        Employee manager1 = new Employee(manager);



    }
}
package org.PrintHouse.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PrintHouse {
    private final List<IEmployable> employees;
    private final List<PrintedItem> items;

    public PrintHouse() {
        employees = new ArrayList<IEmployable>();
        items = new ArrayList<PrintedItem>();
    }

    public void addEmployee(IEmployable employee) {
        employees.add(employee);
    }

    public void removeEmployee(IEmployable employee) {
        employees.remove(employee);
    }

    public List<IEmployable> getEmployees() {
        return employees;
    }
// expense
    public BigDecimal getTotalSalaries(){
        return employees.stream()
                .map(x -> x.getSalary())
                .reduce(BigDecimal.ZERO, (acc, salary) -> acc.add(salary));
    }

//    public BigDecimal getTotal
//
//            //re venue
}

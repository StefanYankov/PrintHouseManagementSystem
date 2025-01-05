package org.PrintHouse.models;

import org.PrintHouse.models.Contracts.IEmployable;
import org.PrintHouse.models.Contracts.IPrintable;
import org.PrintHouse.services.IPrintingService;
import org.PrintHouse.services.ISalaryService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PrintHouse {
    private final List<IEmployable> employees;
    private final IPrintingService printingService;
    private final ISalaryService salaryService;

    public PrintHouse(IPrintingService printingService, ISalaryService salaryService) {
        this.printingService = printingService;
        this.salaryService = salaryService;
        this.employees = new ArrayList<>();
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

    public void printItem(IPrintable item, int copies, boolean isColor) {
        printingService.print(item, copies, isColor);
    }

    public BigDecimal calculateSalaries() {
        return salaryService.getTotalSalaries();
    }

    public BigDecimal calculateTotalExpenses() {
        return salaryService.getTotalSalaries().add(printingService.calculatePaperCost(null, 0));
    }          //re venue
}

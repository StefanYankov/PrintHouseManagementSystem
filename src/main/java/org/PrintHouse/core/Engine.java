package org.PrintHouse.core;

import org.PrintHouse.core.contracts.IEngine;
import org.PrintHouse.models.*;
import org.PrintHouse.models.Contracts.IEmployable;
import org.PrintHouse.models.Contracts.IPrintingPress;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Engine implements IEngine {
    @Override
    public void run() {
        BigDecimal incrementalPercentage = BigDecimal.valueOf(10);

        Edition edition = new Edition("The Lord of the Rings: The Two Towers", 1, Size.A3);
        BigDecimal printedItem1Price = BigDecimal.valueOf(20);
        PrintedItem printedItem1 = new PrintedItem(edition, PaperType.STANDARD, printedItem1Price);

        BigDecimal printedItem2Price = BigDecimal.valueOf(10);
        Edition harryPotter = new Edition("Harry Potter", 10, Size.A3);
        PrintedItem printedItem2 = new PrintedItem(harryPotter, PaperType.GLOSSY, printedItem2Price);

        IPrintingPress<PaperType, Size> printingPress1;

        printingPress1 = new PrintingPress<PaperType,Size>(1000, 1000, false, 250);

        printingPress1.printAnItem(false, printedItem1);
        // IPrintingPress<PaperType, Size> printingPress2 = new PrintingPress(2000, 2000, true, 250);

        // printingPress2.printAnItem(false, printedItem2, 5);

        List<EmployeeType> incrementEligibleRoles = new ArrayList<>();
        incrementEligibleRoles.add(EmployeeType.MANAGER);

        BigDecimal printHouseBaseSalary = BigDecimal.valueOf(1000);
        BigDecimal paperIncrementPercentage = BigDecimal.valueOf(10);
        PrintHouse printHouse =
                new PrintHouse(BigDecimal.valueOf(15),
                        paperIncrementPercentage,
                        printHouseBaseSalary,
                        incrementEligibleRoles,
                        BigDecimal.valueOf(100),
                        15,
                        BigDecimal.valueOf(10));

        printHouse.addPrintingPress(printingPress1);
        //printHouse.addPrintingPress(printingPress2);
        var totalCostForPrint = printHouse.getTotalCostForPrint();
        System.out.println("total cost for print is " + totalCostForPrint);

        IEmployable<EmployeeType> operator1 = new Employee<EmployeeType>(EmployeeType.OPERATOR);
        IEmployable<EmployeeType> manager1 = new Employee<EmployeeType>(EmployeeType.MANAGER);

        printHouse.addEmployee(operator1);
        printHouse.addEmployee(manager1);

        System.out.println(printHouse.getTotalRevenue());
    }
}

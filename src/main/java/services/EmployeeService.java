package services;

import data.models.*;
import services.contracts.IEmployeeService;
import services.contracts.IPrintingPressService;
import utilities.exceptions.*;
import utilities.globalconstants.ExceptionMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages {@link Employee} entities within a {@link PrintHouse}.
 */
public class EmployeeService implements IEmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    private final IPrintingPressService printingPressService;

    public EmployeeService(IPrintingPressService printingPressService) {
        this.printingPressService = printingPressService;
    }

    /** {@inheritDoc} */
    @Override
    public void addEmployee(PrintHouse printHouse, Employee employee) {
        validatePrintHouse(printHouse);
        validateEmployee(employee, printHouse);
        printHouse.getEmployees().add(employee);
        logger.info("Added employee {} to print house: {}", employee, printHouse);
    }

    /** {@inheritDoc} */
    @Override
    public void updateEmployee(PrintHouse printHouse, int employeeIndex, Employee updatedEmployee) {
        validatePrintHouse(printHouse);
        List<Employee> employees = printHouse.getEmployees();
        if (employeeIndex < 0 || employeeIndex >= employees.size()) {
            logger.error("Invalid employee index: {}", employeeIndex);
            throw new InvalidEmployeeException("Invalid employee index");
        }
        Employee existingEmployee = employees.get(employeeIndex);

        if (updatedEmployee == null || updatedEmployee.getEmployeeType() == null) {
            logger.error("Updated employee or type cannot be null");
            throw new InvalidEmployeeException("Updated employee or type cannot be null");
        }

        if (!updatedEmployee.getEgn().equals(existingEmployee.getEgn())) {
            logger.error("Cannot change ЕГН from {} to {}", existingEmployee.getEgn(), updatedEmployee.getEgn());
            throw new InvalidEmployeeException("ЕГН cannot be modified");
        }

        existingEmployee.setEmployeeType(updatedEmployee.getEmployeeType());
        logger.info("Updated employee at index {} in PrintHouse {}: {}", employeeIndex, printHouse, existingEmployee);
    }

    /** {@inheritDoc} */
    @Override
    public void removeEmployee(PrintHouse printHouse, int employeeIndex) {
        validatePrintHouse(printHouse);
        List<Employee> employees = printHouse.getEmployees();
        if (employeeIndex < 0 || employeeIndex >= employees.size()) {
            logger.error("Invalid employee index: {}", employeeIndex);
            throw new InvalidEmployeeException("Invalid employee index");
        }
        Employee removedEmployee = employees.remove(employeeIndex);
        logger.info("Removed employee {} at index {} from PrintHouse {}", removedEmployee, employeeIndex, printHouse);
    }

    /** {@inheritDoc} */
    @Override
    public BigDecimal getTotalCostForEmployees(PrintHouse printHouse) {
        validatePrintHouse(printHouse);
        BigDecimal totalCost = BigDecimal.ZERO;
        // Note: Dependency on PrintingPressService for revenue is kept for demo simplicity.
        // In a fuller design, revenue could be passed as a parameter to avoid entanglement.
        BigDecimal revenue = printingPressService.getTotalRevenue(printHouse);
        BigDecimal baseSalary = printHouse.getBaseSalary();
        for (Employee employee : printHouse.getEmployees()) {
            BigDecimal employeeCost = baseSalary;
            if (revenue.compareTo(printHouse.getRevenueTarget()) >= 0 &&
                    printHouse.getIncrementEligibleRoles().contains(employee.getEmployeeType())) {
                employeeCost = employeeCost.multiply(
                        BigDecimal.ONE.add(printHouse.getEmployeeSalaryIncrementPercentage().divide(BigDecimal.valueOf(100))));
            }
            totalCost = totalCost.add(employeeCost);
        }
        logger.info("Calculated total cost: {}", totalCost);
        return totalCost;
    }

    /** {@inheritDoc} */
    @Override
    public List<Employee> getEmployees(PrintHouse printHouse) {
        validatePrintHouse(printHouse);
        List<Employee> employees = printHouse.getEmployees();
        logger.debug("Retrieved {} employees from PrintHouse", employees.size());
        return new ArrayList<>(employees);
    }

    private void validatePrintHouse(PrintHouse printHouse) {
        if (printHouse == null) {
            logger.error(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL);
            throw new InvalidPrintHouseException(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL);
        }
    }

    private void validateEmployee(Employee employee, PrintHouse printHouse) {
        if (employee == null || employee.getEmployeeType() == null || employee.getEgn() == null) {
            logger.error("Employee, type, or ЕГН cannot be null");
            throw new InvalidEmployeeException("Employee, type, or ЕГН cannot be null");
        }

        String egn = employee.getEgn();
        String egnPattern = "[0-9]{2}[0,1,2,4][0-9][0-9]{2}[0-9]{4}";
        if (!egn.matches(egnPattern)) {
            logger.error("ЕГН must follow YY[0,1,2,4]MDDSSSC format: {}", egn);
            throw new InvalidEmployeeException("Invalid ЕГН format");
        }

        int year = Integer.parseInt(egn.substring(0, 2));
        int month = Integer.parseInt(egn.substring(2, 4));
        int day = Integer.parseInt(egn.substring(4, 6));

        if (month > 40) {
            month -= 40;
            year += 2000;
        } else if (month > 20) {
            month -= 20;
            year += 1800;
        } else {
            year += 1900;
        }

        try {
            LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            logger.error("Invalid date in ЕГН: {}", egn);
            throw new InvalidEmployeeException("Invalid date in ЕГН");
        }

        int[] weights = {2, 4, 8, 5, 10, 9, 7, 3, 6};
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (egn.charAt(i) - '0') * weights[i];
        }
        int checksum = sum % 11;
        if (checksum == 10) checksum = 0;
        if (checksum != (egn.charAt(9) - '0')) {
            logger.error("Invalid ЕГН checksum: {}", egn);
            throw new InvalidEmployeeException("Invalid ЕГН checksum");
        }

        if (printHouse.getEmployees().stream().anyMatch(e -> e.getEgn().equals(egn))) {
            logger.warn("Employee with ЕГН {} already exists in PrintHouse {}", egn, printHouse);
            throw new InvalidEmployeeException("Employee with this ЕГН already exists");
        }
    }
}
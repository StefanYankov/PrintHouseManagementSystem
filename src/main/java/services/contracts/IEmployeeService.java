package services.contracts;

import data.models.Employee;
import data.models.PrintHouse;

import java.math.BigDecimal;
import java.util.List;

/**
 * Defines the contract for managing employee-related operations within a {@link PrintHouse}.
 */
public interface IEmployeeService {
    /**
     * Adds an employee to the specified print house.
     * @param printHouse The print house to add the employee to.
     * @param employee The employee to add.
     */
    void addEmployee(PrintHouse printHouse, Employee employee);

    /**
     * Updates an existing employee in the specified print house.
     * @param printHouse The print house containing the employee.
     * @param employeeIndex The index of the employee to update.
     * @param updatedEmployee The updated employee data.
     */
    void updateEmployee(PrintHouse printHouse, int employeeIndex, Employee updatedEmployee);

    /**
     * Removes an employee from the specified print house.
     * @param printHouse The print house to remove the employee from.
     * @param employeeIndex The index of the employee to remove.
     */
    void removeEmployee(PrintHouse printHouse, int employeeIndex);

    /**
     * Calculates the total cost for all employees in the specified print house.
     * @param printHouse The print house to calculate costs for.
     * @return The total employee cost.
     */
    BigDecimal getTotalCostForEmployees(PrintHouse printHouse);

    /**
     * Retrieves the list of employees in the specified print house.
     * @param printHouse The print house to get employees from.
     * @return A list of employees.
     */
    List<Employee> getEmployees(PrintHouse printHouse);
}
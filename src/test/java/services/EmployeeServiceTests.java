package services;

import data.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import services.contracts.IPrintingPressService;
import utilities.exceptions.InvalidEmployeeException;
import utilities.exceptions.InvalidPrintHouseException;
import utilities.globalconstants.ExceptionMessages;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {
    @Mock
    private IPrintingPressService printingPressService;

    @InjectMocks
    private EmployeeService employeeService;

    private PrintHouse printHouse;

    @BeforeEach
    void setUp() {
        printHouse = new PrintHouse();
        printHouse.setBaseSalary(new BigDecimal("2500"));
        printHouse.setEmployeeSalaryIncrementPercentage(new BigDecimal("15"));
        printHouse.setRevenueTarget(new BigDecimal("10000"));
        printHouse.setIncrementEligibleRoles(Collections.singletonList(EmployeeType.MANAGER));
        printHouse.setEmployees(new ArrayList<>());
    }

    // Happy Path Tests
    @Test
    void AddEmployee_ValidEmployee_AddsSuccessfully() {
        Employee employee = new Employee("7501020018", EmployeeType.OPERATOR);
        employeeService.addEmployee(printHouse, employee);

        assertEquals(1, printHouse.getEmployees().size());
        assertEquals(employee, printHouse.getEmployees().getFirst());
    }

    @Test
    void UpdateEmployee_ValidUpdate_UpdatesSuccessfully() {
        Employee employee = new Employee("7501020018", EmployeeType.OPERATOR);
        printHouse.getEmployees().add(employee);
        Employee updatedEmployee = new Employee("7501020018", EmployeeType.MANAGER);

        employeeService.updateEmployee(printHouse, 0, updatedEmployee);

        assertEquals(EmployeeType.MANAGER, printHouse.getEmployees().getFirst().getEmployeeType());
    }

    @Test
    void RemoveEmployee_ValidIndex_RemovesSuccessfully() {
        Employee employee = new Employee("7501020018", EmployeeType.OPERATOR);
        printHouse.getEmployees().add(employee);

        employeeService.removeEmployee(printHouse, 0);

        assertTrue(printHouse.getEmployees().isEmpty());
    }

    @Test
    void GetTotalCostForEmployees_BelowTarget_ReturnsBaseCost() {
        Employee operator = new Employee("7501020018", EmployeeType.OPERATOR);
        Employee manager = new Employee("8003050024", EmployeeType.MANAGER);
        printHouse.getEmployees().add(operator);
        printHouse.getEmployees().add(manager);
        when(printingPressService.getTotalRevenue(printHouse)).thenReturn(new BigDecimal("5000")); // Below 10000

        BigDecimal totalCost = employeeService.getTotalCostForEmployees(printHouse);

        assertEquals(new BigDecimal("5000"), totalCost); // 2 * 2500
    }

    @Test
    void GetTotalCostForEmployees_HitsTarget_ReturnsIncrementedCost() {
        Employee operator = new Employee("7501020018", EmployeeType.OPERATOR);
        Employee manager = new Employee("8003050024", EmployeeType.MANAGER);
        printHouse.getEmployees().add(operator);
        printHouse.getEmployees().add(manager);
        when(printingPressService.getTotalRevenue(printHouse)).thenReturn(new BigDecimal("12000")); // Above 10000

        BigDecimal totalCost = employeeService.getTotalCostForEmployees(printHouse);

        BigDecimal expectedCost = new BigDecimal("2500").add(new BigDecimal("2500").multiply(new BigDecimal("1.15")));
        assertEquals(expectedCost, totalCost); // 2500 + (2500 * 1.15) = 5375
    }

    @Test
    void GetEmployees_ValidPrintHouse_ReturnsEmployeeList() {
        Employee employee1 = new Employee("7501020018", EmployeeType.OPERATOR);
        Employee employee2 = new Employee("8003050024", EmployeeType.MANAGER);
        printHouse.getEmployees().add(employee1);
        printHouse.getEmployees().add(employee2);

        List<Employee> employees = employeeService.getEmployees(printHouse);

        assertEquals(2, employees.size());
        assertTrue(employees.contains(employee1));
        assertTrue(employees.contains(employee2));
        assertNotSame(printHouse.getEmployees(), employees); // Ensure defensive copy
    }

    // Error Cases
    @Test
    void AddEmployee_InvalidEgnFormat_ThrowsException() {
        Employee employee = new Employee("7533021234", EmployeeType.OPERATOR); // Invalid month prefix
        InvalidEmployeeException exception = assertThrows(InvalidEmployeeException.class, () ->
                employeeService.addEmployee(printHouse, employee));
        assertEquals("Invalid ЕГН format", exception.getMessage());
        assertTrue(printHouse.getEmployees().isEmpty());
    }

    @Test
    void AddEmployee_InvalidDate_ThrowsException() {
        Employee employee = new Employee("7502301239", EmployeeType.OPERATOR); // Feb 30
        InvalidEmployeeException exception = assertThrows(InvalidEmployeeException.class, () ->
                employeeService.addEmployee(printHouse, employee));
        assertEquals("Invalid date in ЕГН", exception.getMessage());
        assertTrue(printHouse.getEmployees().isEmpty());
    }

    @Test
    void AddEmployee_InvalidChecksum_ThrowsException() {
        Employee employee = new Employee("7501020019", EmployeeType.OPERATOR); // Checksum should be 8, not 9
        InvalidEmployeeException exception = assertThrows(InvalidEmployeeException.class, () ->
                employeeService.addEmployee(printHouse, employee));
        assertEquals("Invalid ЕГН checksum", exception.getMessage());
        assertTrue(printHouse.getEmployees().isEmpty());
    }

    @Test
    void AddEmployee_DuplicateEgn_ThrowsException() {
        Employee employee1 = new Employee("7501020018", EmployeeType.OPERATOR);
        employeeService.addEmployee(printHouse, employee1);
        Employee employee2 = new Employee("7501020018", EmployeeType.MANAGER);

        InvalidEmployeeException exception = assertThrows(InvalidEmployeeException.class, () ->
                employeeService.addEmployee(printHouse, employee2));
        assertEquals("Employee with this ЕГН already exists", exception.getMessage());
        assertEquals(1, printHouse.getEmployees().size());
    }

    @Test
    void AddEmployee_NullPrintHouse_ThrowsException() {
        Employee employee = new Employee("7501020018", EmployeeType.OPERATOR);
        InvalidPrintHouseException exception = assertThrows(InvalidPrintHouseException.class, () ->
                employeeService.addEmployee(null, employee));
        assertEquals(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void UpdateEmployee_InvalidIndex_ThrowsException() {
        Employee employee = new Employee("7501020018", EmployeeType.OPERATOR);
        printHouse.getEmployees().add(employee);
        Employee updatedEmployee = new Employee("7501020018", EmployeeType.MANAGER);

        InvalidEmployeeException exception = assertThrows(InvalidEmployeeException.class, () ->
                employeeService.updateEmployee(printHouse, 1, updatedEmployee));
        assertEquals("Invalid employee index", exception.getMessage());
    }

    @Test
    void UpdateEmployee_EgnMismatch_ThrowsException() {
        Employee employee = new Employee("7501020018", EmployeeType.OPERATOR);
        printHouse.getEmployees().add(employee);
        Employee updatedEmployee = new Employee("8003050024", EmployeeType.MANAGER);

        InvalidEmployeeException exception = assertThrows(InvalidEmployeeException.class, () ->
                employeeService.updateEmployee(printHouse, 0, updatedEmployee));
        assertEquals("ЕГН cannot be modified", exception.getMessage());
    }

    @Test
    void UpdateEmployee_NullUpdatedEmployee_ThrowsException() {
        Employee employee = new Employee("7501020018", EmployeeType.OPERATOR);
        printHouse.getEmployees().add(employee);
        InvalidEmployeeException exception = assertThrows(InvalidEmployeeException.class, () ->
                employeeService.updateEmployee(printHouse, 0, null));
        assertEquals("Updated employee or type cannot be null", exception.getMessage());
    }

    @Test
    void UpdateEmployee_NullEmployeeType_ThrowsException() {
        Employee employee = new Employee("7501020018", EmployeeType.OPERATOR);
        printHouse.getEmployees().add(employee);
        Employee updatedEmployee = new Employee("7501020018", null); // Null type
        InvalidEmployeeException exception = assertThrows(InvalidEmployeeException.class, () ->
                employeeService.updateEmployee(printHouse, 0, updatedEmployee));
        assertEquals("Updated employee or type cannot be null", exception.getMessage());
    }

    @Test
    void RemoveEmployee_InvalidIndex_ThrowsException() {
        Employee employee = new Employee("7501020018", EmployeeType.OPERATOR);
        printHouse.getEmployees().add(employee);

        InvalidEmployeeException exception = assertThrows(InvalidEmployeeException.class, () ->
                employeeService.removeEmployee(printHouse, 1));
        assertEquals("Invalid employee index", exception.getMessage());
        assertEquals(1, printHouse.getEmployees().size());
    }

    @Test
    void GetTotalCostForEmployees_NullPrintHouse_ThrowsException() {
        InvalidPrintHouseException exception = assertThrows(InvalidPrintHouseException.class, () ->
                employeeService.getTotalCostForEmployees(null));
        assertEquals(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void GetEmployees_NullPrintHouse_ThrowsException() {
        InvalidPrintHouseException exception = assertThrows(InvalidPrintHouseException.class, () ->
                employeeService.getEmployees(null));
        assertEquals(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL, exception.getMessage());
    }

    // Edge Cases
    @Test
    void AddEmployee_MaximumLengthEgn_AddsSuccessfully() {
        Employee employee = new Employee("7501020018", EmployeeType.OPERATOR); // Valid 10-digit ЕГН
        employeeService.addEmployee(printHouse, employee);

        assertEquals(1, printHouse.getEmployees().size());
        assertEquals("7501020018", printHouse.getEmployees().getFirst().getEgn());
    }

    @Test
    void GetTotalCostForEmployees_EmptyEmployeeList_ReturnsZero() {
        BigDecimal totalCost = employeeService.getTotalCostForEmployees(printHouse);
        assertEquals(BigDecimal.ZERO, totalCost);
    }

    @Test
    void GetEmployees_EmptyList_ReturnsEmptyList() {
        List<Employee> employees = employeeService.getEmployees(printHouse);
        assertTrue(employees.isEmpty());
        assertNotSame(printHouse.getEmployees(), employees); // Ensure defensive copy
    }

    @Test
    void RemoveEmployee_LastEmployee_RemovesSuccessfully() {
        Employee employee = new Employee("7501020018", EmployeeType.OPERATOR);
        printHouse.getEmployees().add(employee);
        assertEquals(1, printHouse.getEmployees().size());

        employeeService.removeEmployee(printHouse, 0);
        assertTrue(printHouse.getEmployees().isEmpty());
    }
}
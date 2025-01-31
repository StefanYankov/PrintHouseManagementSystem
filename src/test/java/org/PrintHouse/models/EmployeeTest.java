package org.PrintHouse.models;

import org.PrintHouse.utilities.exceptions.InvalidEmployeeException;
import org.PrintHouse.utilities.exceptions.InvalidSalaryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {

    private BigDecimal baseSalary;
    private EmployeeType validEmployeeType;
    private Employee<EmployeeType> employee;;

    @BeforeEach
    public void setUp() {
        validEmployeeType = EmployeeType.OPERATOR;
        employee = new Employee<>(validEmployeeType);
    }

    // 1) Happy path
    @Test
    public void constructorWithValidDataShouldSetupCorrectly(){
        assertNotNull(employee);
        assertEquals(validEmployeeType, employee.getEmployeeType());
        assertEquals(BigDecimal.ZERO, employee.getBaseSalary());
    }

    @Test
    public void updatingEmployeeTypeShouldSetCorrectly(){
        validEmployeeType = EmployeeType.MANAGER;
        employee.setEmployeeType(validEmployeeType);
        assertEquals(validEmployeeType, employee.getEmployeeType());
    }

    @Test
    public void updatingBaseSalaryShouldSetCorrectly(){
        baseSalary = BigDecimal.ONE;
        employee.setBaseSalary(baseSalary);
        assertEquals(baseSalary, employee.getBaseSalary());
    }

    // 2) Error Cases
    @Test
    public void settingBaseSalaryToNullShouldThrowException(){
        assertThrows(InvalidSalaryException.class, () -> employee.setBaseSalary(null));
    }

    @Test
    public void settingBaseSalaryToNegativeShouldThrowException(){
        assertThrows(InvalidSalaryException.class, () -> employee.setBaseSalary(BigDecimal.valueOf(-1)));
    }

    @Test
    public void settingEmployeeTypeToNullShouldThrowException(){
        assertThrows(InvalidEmployeeException.class, () -> employee.setEmployeeType(null));
    }

}

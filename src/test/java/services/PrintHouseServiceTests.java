package services;

import data.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import services.contracts.IPrintHouseService;
import services.contracts.ISerializationService;
import utilities.exceptions.*;
import utilities.globalconstants.ExceptionMessages;
import utilities.globalconstants.ModelsConstants;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PrintHouseServiceTests {
    private IPrintHouseService service;
    private ISerializationService<PrintHouse> serializationService;

    @BeforeEach
    void setUp() {
        serializationService = mock(ISerializationService.class);
        service = new PrintHouseService(serializationService);
    }

    // Happy Path Tests
    @Test
    void CreatePrintHouse_ValidParameters_CreatesSuccessfully() {
        PrintHouse ph = service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5));
        assertEquals(1, service.getAllPrintHouses().size());
        assertEquals(BigDecimal.TEN, ph.getEmployeeSalaryIncrementPercentage());
    }

    @Test
    void UpdatePrintHouse_ValidParameters_UpdatesSuccessfully() {
        service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5));
        service.updatePrintHouse(0, BigDecimal.valueOf(15), null, null, null, null, null, null);
        assertEquals(BigDecimal.valueOf(15), service.getPrintHouse(0).getEmployeeSalaryIncrementPercentage());
    }

    @Test
    void RemovePrintHouse_ValidIndex_RemovesSuccessfully() {
        service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5));
        assertEquals(1, service.getAllPrintHouses().size());
        service.removePrintHouse(0);
        assertTrue(service.getAllPrintHouses().isEmpty());
    }

    @Test
    void SaveAllPrintHouses_ValidPath_SerializesSuccessfully(@TempDir Path tempDir) {
        service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5));
        String filePath = tempDir.resolve("printhouses.ser").toString();
        service.saveAllPrintHouses(filePath);
        verify(serializationService).serialize(anyList(), eq(filePath));
    }

    // Error Cases
    @Test
    void CreatePrintHouse_NullSalaryIncrementPercentage_ThrowsException() {
        InvalidIncrementPercentageException exception = assertThrows(InvalidIncrementPercentageException.class, () ->
                service.createPrintHouse(null, BigDecimal.TEN, BigDecimal.valueOf(1000),
                        List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5)));
        assertEquals(ExceptionMessages.EMPLOYEE_SALARY_INCREMENT_PERCENTAGE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void CreatePrintHouse_NegativeSalaryIncrementPercentage_ThrowsException() {
        InvalidIncrementPercentageException exception = assertThrows(InvalidIncrementPercentageException.class, () ->
                service.createPrintHouse(BigDecimal.valueOf(-5), BigDecimal.TEN, BigDecimal.valueOf(1000),
                        List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5)));
        assertEquals(ExceptionMessages.EMPLOYEE_SALARY_INCREMENT_PERCENTAGE_CANNOT_BE_A_NEGATIVE_NUMBER, exception.getMessage());
    }

    @Test
    void CreatePrintHouse_NullPaperIncrementPercentage_ThrowsException() {
        InvalidIncrementPercentageException exception = assertThrows(InvalidIncrementPercentageException.class, () ->
                service.createPrintHouse(BigDecimal.TEN, null, BigDecimal.valueOf(1000),
                        List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5)));
        assertEquals(ExceptionMessages.PAPER_INCREMENT_PERCENTAGE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void CreatePrintHouse_NegativePaperIncrementPercentage_ThrowsException() {
        InvalidIncrementPercentageException exception = assertThrows(InvalidIncrementPercentageException.class, () ->
                service.createPrintHouse(BigDecimal.TEN, BigDecimal.valueOf(-5), BigDecimal.valueOf(1000),
                        List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5)));
        assertEquals(ExceptionMessages.PAPER_INCREMENT_PERCENTAGE_CANNOT_BE_NEGATIVE_NUMBER, exception.getMessage());
    }

    @Test
    void CreatePrintHouse_NullIncrementEligibleRoles_ThrowsException() {
        InvalidEmployeeException exception = assertThrows(InvalidEmployeeException.class, () ->
                service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                        null, BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5)));
        assertEquals(ExceptionMessages.INCREMENT_ELIGIBLE_ROLES_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void CreatePrintHouse_NullRevenueTarget_ThrowsException() {
        InvalidRevenueTargetException exception = assertThrows(InvalidRevenueTargetException.class, () ->
                service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                        List.of(EmployeeType.MANAGER), null, 10, BigDecimal.valueOf(5)));
        assertEquals(ExceptionMessages.REVENUE_TARGET_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void CreatePrintHouse_NegativeRevenueTarget_ThrowsException() {
        InvalidRevenueTargetException exception = assertThrows(InvalidRevenueTargetException.class, () ->
                service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                        List.of(EmployeeType.MANAGER), BigDecimal.valueOf(-5000), 10, BigDecimal.valueOf(5)));
        assertEquals(ExceptionMessages.REVENUE_CANNOT_BE_A_NEGATIVE_NUMBER, exception.getMessage());
    }

    @Test
    void CreatePrintHouse_ZeroRevenueTarget_ThrowsException() {
        InvalidRevenueTargetException exception = assertThrows(InvalidRevenueTargetException.class, () ->
                service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                        List.of(EmployeeType.MANAGER), BigDecimal.ZERO, 10, BigDecimal.valueOf(5)));
        assertEquals(ExceptionMessages.REVENUE_CANNOT_BE_A_NEGATIVE_NUMBER, exception.getMessage());
    }

    @Test
    void CreatePrintHouse_NegativeSalesDiscountCount_ThrowsException() {
        InvalidDiscountCountException exception = assertThrows(InvalidDiscountCountException.class, () ->
                service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                        List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), -1, BigDecimal.valueOf(5)));
        assertEquals(ExceptionMessages.SALES_DISCOUNT_COUNT_CANNOT_BE_NEGATIVE, exception.getMessage());
    }

    @Test
    void CreatePrintHouse_NullSalesDiscountPercentage_ThrowsException() {
        InvalidDiscountPercentageException exception = assertThrows(InvalidDiscountPercentageException.class, () ->
                service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                        List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, null));
        assertEquals(ExceptionMessages.SALES_DISCOUNT_PERCENTAGE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void CreatePrintHouse_NegativeSalesDiscountPercentage_ThrowsException() {
        InvalidDiscountPercentageException exception = assertThrows(InvalidDiscountPercentageException.class, () ->
                service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                        List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(-5)));
        assertEquals(ExceptionMessages.SALES_DISCOUNT_PERCENTAGE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void CreatePrintHouse_ZeroSalesDiscountPercentage_ThrowsException() {
        InvalidDiscountPercentageException exception = assertThrows(InvalidDiscountPercentageException.class, () ->
                service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                        List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.ZERO));
        assertEquals(ExceptionMessages.SALES_DISCOUNT_PERCENTAGE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void CreatePrintHouse_NegativeBaseSalary_ThrowsException() {
        InvalidSalaryException exception = assertThrows(InvalidSalaryException.class, () ->
                service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(-1000),
                        List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5)));
        assertEquals(ExceptionMessages.BASE_SALARY_CANNOT_BE_A_NEGATIVE_NUMBER, exception.getMessage());
    }

    @Test
    void UpdatePrintHouse_InvalidIndex_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                service.updatePrintHouse(0, BigDecimal.TEN, null, null, null, null, null, null));
        assertTrue(exception.getMessage().contains("Invalid print house index"));
    }

    @Test
    void UpdatePrintHouse_NegativeSalaryIncrement_ThrowsException() {
        service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5));
        InvalidIncrementPercentageException exception = assertThrows(InvalidIncrementPercentageException.class, () ->
                service.updatePrintHouse(0, BigDecimal.valueOf(-5), null, null, null, null, null, null));
        assertEquals(ExceptionMessages.EMPLOYEE_SALARY_INCREMENT_PERCENTAGE_CANNOT_BE_NEGATIVE, exception.getMessage());
    }

    @Test
    void UpdatePrintHouse_ExcessivePaperIncrement_ThrowsException() {
        service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5));
        InvalidIncrementPercentageException exception = assertThrows(InvalidIncrementPercentageException.class, () ->
                service.updatePrintHouse(0, null, ModelsConstants.MAXIMUM_PERCENTAGE.add(BigDecimal.ONE), null, null, null, null, null));
        assertEquals(ExceptionMessages.PAPER_INCREMENT_PERCENTAGE_CANNOT_BE_NEGATIVE, exception.getMessage());
    }

    @Test
    void UpdatePrintHouse_NegativeBaseSalary_ThrowsException() {
        service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5));
        InvalidSalaryException exception = assertThrows(InvalidSalaryException.class, () ->
                service.updatePrintHouse(0, null, null, BigDecimal.valueOf(-100), null, null, null, null));
        assertEquals(ExceptionMessages.BASE_SALARY_CANNOT_BE_A_NEGATIVE_NUMBER, exception.getMessage());
    }

    @Test
    void UpdatePrintHouse_NegativeRevenueTarget_ThrowsException() {
        service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5));
        InvalidRevenueTargetException exception = assertThrows(InvalidRevenueTargetException.class, () ->
                service.updatePrintHouse(0, null, null, null, null, BigDecimal.valueOf(-5000), null, null));
        assertEquals(ExceptionMessages.REVENUE_CANNOT_BE_A_NEGATIVE_NUMBER, exception.getMessage());
    }

    @Test
    void UpdatePrintHouse_NegativeSalesDiscountCount_ThrowsException() {
        service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5));
        InvalidDiscountCountException exception = assertThrows(InvalidDiscountCountException.class, () ->
                service.updatePrintHouse(0, null, null, null, null, null, -1, null));
        assertEquals(ExceptionMessages.SALES_DISCOUNT_COUNT_CANNOT_BE_NEGATIVE, exception.getMessage());
    }

    @Test
    void UpdatePrintHouse_ExcessiveDiscountPercentage_ThrowsException() {
        service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5));
        InvalidDiscountPercentageException exception = assertThrows(InvalidDiscountPercentageException.class, () ->
                service.updatePrintHouse(0, null, null, null, null, null, null, BigDecimal.valueOf(101)));
        assertEquals(ExceptionMessages.SALES_DISCOUNT_PERCENTAGE_CANNOT_BE_NEGATIVE, exception.getMessage());
    }

    @Test
    void RemovePrintHouse_InvalidIndex_ThrowsException() {
        IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class, () ->
                service.removePrintHouse(0));
        assertTrue(exception.getMessage().contains("out of bound"));
    }

    @Test
    void SaveAllPrintHouses_NullPath_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                service.saveAllPrintHouses(null));
        assertEquals(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void LoadAllPrintHouses_NullPath_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                service.loadAllPrintHouses(null));
        assertEquals(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL, exception.getMessage());
    }

    // Edge Cases
    @Test
    void CreatePrintHouse_MaximumPercentage_CreatesSuccessfully() {
        PrintHouse ph = service.createPrintHouse(ModelsConstants.MAXIMUM_PERCENTAGE, ModelsConstants.MAXIMUM_PERCENTAGE,
                BigDecimal.valueOf(1000), List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5));
        assertEquals(1, service.getAllPrintHouses().size());
        assertEquals(ModelsConstants.MAXIMUM_PERCENTAGE, ph.getEmployeeSalaryIncrementPercentage());
        assertEquals(ModelsConstants.MAXIMUM_PERCENTAGE, ph.getPaperIncrementPercentage());
    }

    @Test
    void UpdatePrintHouse_MaximumPercentage_UpdatesSuccessfully() {
        service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5));
        service.updatePrintHouse(0, ModelsConstants.MAXIMUM_PERCENTAGE, null, null, null, null, null, null);
        assertEquals(ModelsConstants.MAXIMUM_PERCENTAGE, service.getPrintHouse(0).getEmployeeSalaryIncrementPercentage());
    }

    @Test
    void LoadAllPrintHouses_EmptyList_LoadsEmpty() {
        when(serializationService.deserialize("test.ser")).thenReturn(Collections.emptyList());
        service.loadAllPrintHouses("test.ser");
        assertTrue(service.getAllPrintHouses().isEmpty());
    }

    @Test
    void UpdatePrintHouse_AllParametersNull_DoesNotThrow() {
        service.createPrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5));
        PrintHouse original = service.getPrintHouse(0);
        service.updatePrintHouse(0, null, null, null, null, null, null, null);
        assertEquals(original, service.getPrintHouse(0)); // No changes applied
    }
}
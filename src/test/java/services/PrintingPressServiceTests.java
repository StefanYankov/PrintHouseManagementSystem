package services;

import data.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.contracts.IPrintingPressService;
import utilities.exceptions.*;
import utilities.globalconstants.ExceptionMessages;
import utilities.globalconstants.ModelsConstants;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PrintingPressServiceTests {
    private IPrintingPressService service;
    private PrintHouse printHouse;
    private PrintingPress press;

    @BeforeEach
    void setUp() {
        service = new PrintingPressService();
        printHouse = new PrintHouse(BigDecimal.TEN,
                BigDecimal.TEN, BigDecimal.valueOf(1000),
                List.of(EmployeeType.MANAGER),
                BigDecimal.valueOf(5000),
                10,
                BigDecimal.valueOf(5));
        press = new PrintingPress(1000, 500, true, 100);
    }

    // Happy Path Tests
    @Test
    void PrintItem_ValidParameters_PrintsSuccessfully() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addPrintingPress(printHouse, press);
        service.printItem(printHouse, press, edition, PaperType.STANDARD, BigDecimal.TEN, 5, true);
        Map<PrintedItem, Integer> printedItems = press.getPrintedItems();
        assertEquals(1, printedItems.size());
        assertEquals(5, printedItems.values().iterator().next());
    }

    @Test
    void CalculatePaperCost_ValidParameters_ReturnsCost() {
        BigDecimal cost = service.calculatePaperCost(printHouse, PaperType.STANDARD, Size.A4, 100);
        assertTrue(cost.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void GetTotalCostForPrint_ValidPrintHouse_ReturnsCost() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addPrintingPress(printHouse, press);
        service.printItem(printHouse, press, edition, PaperType.STANDARD, BigDecimal.TEN, 5, true);
        BigDecimal cost = service.getTotalCostForPrint(printHouse);
        assertTrue(cost.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void TotalPrintedPages_ValidPress_ReturnsPageCount() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addPrintingPress(printHouse, press);
        service.printItem(printHouse, press, edition, PaperType.STANDARD, BigDecimal.TEN, 5, true);
        long totalPages = service.totalPrintedPages(printHouse, press);
        assertEquals(500, totalPages); // 100 pages * 5 copies
    }

    @Test
    void LoadPaper_ValidAmount_LoadsSuccessfully() {
        service.addPrintingPress(printHouse, press);
        service.loadPaper(printHouse, press, 300);
        assertEquals(800, press.getCurrentPaperLoad());
    }

    @Test
    void UpdatePrintingPress_ValidParameters_UpdatesSuccessfully() {
        service.addPrintingPress(printHouse, press);
        service.updatePrintingPress(printHouse, press, 2000, 1000, false, 200);
        assertEquals(2000, press.getMaxPaperLoad());
        assertEquals(1000, press.getCurrentPaperLoad());
        assertFalse(press.isColour());
        assertEquals(200, press.getMaximumPagesPerMinute());
    }

    @Test
    void RemovePrintingPress_ValidPress_RemovesSuccessfully() {
        service.addPrintingPress(printHouse, press);
        assertEquals(1, printHouse.getPrintingPresses().size());
        service.removePrintingPress(printHouse, press);
        assertTrue(printHouse.getPrintingPresses().isEmpty());
    }

    @Test
    void GetTotalRevenue_ValidPrintHouse_ReturnsRevenue() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addPrintingPress(printHouse, press);
        service.printItem(printHouse, press, edition, PaperType.STANDARD, BigDecimal.TEN, 5, true);
        BigDecimal revenue = service.getTotalRevenue(printHouse);
        assertEquals(BigDecimal.valueOf(50), revenue); // 5 * 10 = 50 (no discount)
    }

    // Error Cases
    @Test
    void PrintItem_NullPrintHouse_ThrowsException() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        InvalidPrintHouseException exception = assertThrows(InvalidPrintHouseException.class, () ->
                service.printItem(null, press, edition, PaperType.STANDARD, BigDecimal.TEN, 5, true));
        assertEquals(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void PrintItem_NullPress_ThrowsException() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        InvalidPrintingPressException exception = assertThrows(InvalidPrintingPressException.class, () ->
                service.printItem(printHouse, null, edition, PaperType.STANDARD, BigDecimal.TEN, 5, true));
        assertEquals(ExceptionMessages.PRINTING_PRESS_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void PrintItem_NullEdition_ThrowsException() {
        service.addPrintingPress(printHouse, press);
        InvalidEditionException exception = assertThrows(InvalidEditionException.class, () ->
                service.printItem(printHouse, press, null, PaperType.STANDARD, BigDecimal.TEN, 5, true));
        assertEquals(ExceptionMessages.EDITION_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void PrintItem_NullPaperType_ThrowsException() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addPrintingPress(printHouse, press);
        InvalidPaperTypeException exception = assertThrows(InvalidPaperTypeException.class, () ->
                service.printItem(printHouse, press, edition, null, BigDecimal.TEN, 5, true));
        assertEquals(ExceptionMessages.PAPER_TYPE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void PrintItem_NullPricePerCopy_ThrowsException() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addPrintingPress(printHouse, press);
        InvalidPriceException exception = assertThrows(InvalidPriceException.class, () ->
                service.printItem(printHouse, press, edition, PaperType.STANDARD, null, 5, true));
        assertEquals(ExceptionMessages.PRICE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void PrintItem_NegativePricePerCopy_ThrowsException() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addPrintingPress(printHouse, press);
        InvalidPriceException exception = assertThrows(InvalidPriceException.class, () ->
                service.printItem(printHouse, press, edition, PaperType.STANDARD, BigDecimal.valueOf(-1), 5, true));
        assertEquals(ExceptionMessages.PRICE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void PrintItem_ExcessivePricePerCopy_ThrowsException() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addPrintingPress(printHouse, press);
        InvalidPriceException exception = assertThrows(InvalidPriceException.class, () ->
                service.printItem(printHouse, press, edition, PaperType.STANDARD, ModelsConstants.MAXIMUM_PRICE.add(BigDecimal.ONE), 5, true));
        assertEquals(ExceptionMessages.PRICE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void PrintItem_InsufficientPaper_ThrowsException() {
        Edition edition = new Edition("Test Book", 2000, Size.A4); // Needs 1000 sheets (2000/2)
        press.setCurrentPaperLoad(500); // Only 500 available
        service.addPrintingPress(printHouse, press);
        InvalidPaperLoadException exception = assertThrows(InvalidPaperLoadException.class, () ->
                service.printItem(printHouse, press, edition, PaperType.STANDARD, BigDecimal.TEN, 1, true));
        assertEquals(MessageFormat.format(ExceptionMessages.INSUFFICIENT_PAPER_LOAD, 1000, 500), exception.getMessage());
    }

    @Test
    void PrintItem_ColorMismatch_ThrowsException() {
        press = new PrintingPress(1000, 500, false, 100); // Non-color press
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addPrintingPress(printHouse, press);
        UnsupportedPrintColorException exception = assertThrows(UnsupportedPrintColorException.class, () ->
                service.printItem(printHouse, press, edition, PaperType.STANDARD, BigDecimal.TEN, 5, true));
        assertEquals(MessageFormat.format(ExceptionMessages.INCOMPATIBLE_COLOR_TYPE, false, true), exception.getMessage());
    }

    @Test
    void PrintItem_InvalidMaxPaperLoad_ThrowsException() {
        press = new PrintingPress(0, 500, true, 100);
        Edition edition = new Edition("Test Book", 100, Size.A4);
        InvalidPaperLoadException exception = assertThrows(InvalidPaperLoadException.class, () ->
                service.addPrintingPress(printHouse, press));
        assertEquals(ExceptionMessages.PAPER_LOAD_CANNOT_BE_ZERO_OR_NEGATIVE_NUMBER, exception.getMessage());
    }

    @Test
    void PrintItem_ExcessiveMaxPaperLoad_ThrowsException() {
        press = new PrintingPress(ModelsConstants.MAXIMUM_PAPER_LOAD + 1, 500, true, 100);
        Edition edition = new Edition("Test Book", 100, Size.A4);
        InvalidPaperLoadException exception = assertThrows(InvalidPaperLoadException.class, () ->
                service.addPrintingPress(printHouse, press));
        assertEquals(ExceptionMessages.PAPER_LOAD_CANNOT_BE_ZERO_OR_NEGATIVE_NUMBER, exception.getMessage());
    }

    @Test
    void PrintItem_NegativeCurrentPaperLoad_ThrowsException() {
        press = new PrintingPress(1000, -1, true, 100);
        Edition edition = new Edition("Test Book", 100, Size.A4);
        InvalidPaperLoadException exception = assertThrows(InvalidPaperLoadException.class, () ->
                service.addPrintingPress(printHouse, press));
        assertEquals(ExceptionMessages.PAPER_LOAD_CANNOT_BE_ABOVE_THE_MAXIMUM_CAPACITY, exception.getMessage());
    }

    @Test
    void PrintItem_ExcessiveCurrentPaperLoad_ThrowsException() {
        press = new PrintingPress(1000, 1001, true, 100);
        Edition edition = new Edition("Test Book", 100, Size.A4);
        InvalidPaperLoadException exception = assertThrows(InvalidPaperLoadException.class, () ->
                service.addPrintingPress(printHouse, press));
        assertEquals(ExceptionMessages.PAPER_LOAD_CANNOT_BE_ABOVE_THE_MAXIMUM_CAPACITY, exception.getMessage());
    }

    @Test
    void PrintItem_InvalidMaxPagesPerMinute_ThrowsException() {
        press = new PrintingPress(1000, 500, true, 0);
        Edition edition = new Edition("Test Book", 100, Size.A4);
        InvalidPrintingPressException exception = assertThrows(InvalidPrintingPressException.class, () ->
                service.addPrintingPress(printHouse, press));
        assertEquals(ExceptionMessages.MAX_PAGES_PER_MINUTE_PRINTED_CANNOT_BE_A_NEGATIVE_VALUE, exception.getMessage());
    }

    @Test
    void PrintItem_NegativeCopies_ThrowsException() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addPrintingPress(printHouse, press);
        InvalidCopiesCountException exception = assertThrows(InvalidCopiesCountException.class, () ->
                service.printItem(printHouse, press, edition, PaperType.STANDARD, BigDecimal.TEN, -1, true));
        assertEquals(ExceptionMessages.COPIES_COUNT_CANNOT_BE_A_NEGATIVE_NUMBER, exception.getMessage());
    }

    @Test
    void PrintItem_ZeroCopies_ThrowsException() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addPrintingPress(printHouse, press);
        InvalidCopiesCountException exception = assertThrows(InvalidCopiesCountException.class, () ->
                service.printItem(printHouse, press, edition, PaperType.STANDARD, BigDecimal.TEN, 0, true));
        assertEquals(ExceptionMessages.COPIES_COUNT_CANNOT_BE_A_NEGATIVE_NUMBER, exception.getMessage());
    }

    @Test
    void CalculatePaperCost_NullPaperType_ThrowsException() {
        InvalidPaperTypeException exception = assertThrows(InvalidPaperTypeException.class, () ->
                service.calculatePaperCost(printHouse, null, Size.A4, 100));
        assertEquals(ExceptionMessages.PAPER_TYPE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void CalculatePaperCost_NegativePageCount_ThrowsException() {
        InvalidNumberOfPagesException exception = assertThrows(InvalidNumberOfPagesException.class, () ->
                service.calculatePaperCost(printHouse, PaperType.STANDARD, Size.A4, -1));
        assertEquals(ExceptionMessages.NUMBER_OF_PAGES_MUST_BE_GREATER_THAN_ZERO, exception.getMessage());
    }

    @Test
    void CalculatePaperCost_ZeroPageCount_ThrowsException() {
        InvalidNumberOfPagesException exception = assertThrows(InvalidNumberOfPagesException.class, () ->
                service.calculatePaperCost(printHouse, PaperType.STANDARD, Size.A4, 0));
        assertEquals(ExceptionMessages.NUMBER_OF_PAGES_MUST_BE_GREATER_THAN_ZERO, exception.getMessage());
    }

    @Test
    void GetTotalCostForPrint_NullPrintHouse_ThrowsException() {
        InvalidPrintHouseException exception = assertThrows(InvalidPrintHouseException.class, () ->
                service.getTotalCostForPrint(null));
        assertEquals(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void TotalPrintedPages_NullPrintHouse_ThrowsException() {
        InvalidPrintHouseException exception = assertThrows(InvalidPrintHouseException.class, () ->
                service.totalPrintedPages(null, press));
        assertEquals(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void TotalPrintedPages_NullPress_ThrowsException() {
        InvalidPrintingPressException exception = assertThrows(InvalidPrintingPressException.class, () ->
                service.totalPrintedPages(printHouse, null));
        assertEquals(ExceptionMessages.PRINTING_PRESS_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void LoadPaper_NullPrintHouse_ThrowsException() {
        InvalidPrintHouseException exception = assertThrows(InvalidPrintHouseException.class, () ->
                service.loadPaper(null, press, 300));
        assertEquals(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void LoadPaper_NullPress_ThrowsException() {
        InvalidPrintingPressException exception = assertThrows(InvalidPrintingPressException.class, () ->
                service.loadPaper(printHouse, null, 300));
        assertEquals(ExceptionMessages.PRINTING_PRESS_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void LoadPaper_NegativeAmount_ThrowsException() {
        service.addPrintingPress(printHouse, press);
        InvalidPaperLoadException exception = assertThrows(InvalidPaperLoadException.class, () ->
                service.loadPaper(printHouse, press, -1));
        assertEquals(ExceptionMessages.PAPER_LOAD_CANNOT_BE_ZERO_OR_NEGATIVE_NUMBER, exception.getMessage());
    }

    @Test
    void LoadPaper_ZeroAmount_ThrowsException() {
        service.addPrintingPress(printHouse, press);
        InvalidPaperLoadException exception = assertThrows(InvalidPaperLoadException.class, () ->
                service.loadPaper(printHouse, press, 0));
        assertEquals(ExceptionMessages.PAPER_LOAD_CANNOT_BE_ZERO_OR_NEGATIVE_NUMBER, exception.getMessage());
    }

    @Test
    void LoadPaper_ExcessiveAmount_ThrowsException() {
        service.addPrintingPress(printHouse, press);
        InvalidPaperLoadException exception = assertThrows(InvalidPaperLoadException.class, () ->
                service.loadPaper(printHouse, press, 501)); // 500 + 501 > 1000
        assertEquals(MessageFormat.format(ExceptionMessages.PAPER_LOAD_CANNOT_BE_ABOVE_THE_MAXIMUM_CAPACITY, 1000), exception.getMessage());
    }

    @Test
    void UpdatePrintingPress_NullPrintHouse_ThrowsException() {
        InvalidPrintHouseException exception = assertThrows(InvalidPrintHouseException.class, () ->
                service.updatePrintingPress(null, press, 2000, 1000, false, 200));
        assertEquals(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void UpdatePrintingPress_NullPress_ThrowsException() {
        InvalidPrintingPressException exception = assertThrows(InvalidPrintingPressException.class, () ->
                service.updatePrintingPress(printHouse, null, 2000, 1000, false, 200));
        assertEquals(ExceptionMessages.PRINTING_PRESS_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void UpdatePrintingPress_InvalidMaxPaperLoad_ThrowsException() {
        service.addPrintingPress(printHouse, press);
        InvalidPaperLoadException exception = assertThrows(InvalidPaperLoadException.class, () ->
                service.updatePrintingPress(printHouse, press, 0, null, null, null));
        assertEquals(ExceptionMessages.PAPER_LOAD_CANNOT_BE_ZERO_OR_NEGATIVE_NUMBER, exception.getMessage());
    }

    @Test
    void RemovePrintingPress_NullPrintHouse_ThrowsException() {
        InvalidPrintHouseException exception = assertThrows(InvalidPrintHouseException.class, () ->
                service.removePrintingPress(null, press));
        assertEquals(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void RemovePrintingPress_NullPress_ThrowsException() {
        InvalidPrintingPressException exception = assertThrows(InvalidPrintingPressException.class, () ->
                service.removePrintingPress(printHouse, null));
        assertEquals(ExceptionMessages.PRINTING_PRESS_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void GetTotalRevenue_NullPrintHouse_ThrowsException() {
        InvalidPrintHouseException exception = assertThrows(InvalidPrintHouseException.class, () ->
                service.getTotalRevenue(null));
        assertEquals(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL, exception.getMessage());
    }

    // Edge Cases
    @Test
    void PrintItem_MaximumCopies_PrintsSuccessfully() {
        Edition edition = new Edition("Test Book", 2, Size.A4); // 1 sheet per copy
        press.setCurrentPaperLoad(1000);
        service.addPrintingPress(printHouse, press);
        service.printItem(printHouse, press, edition, PaperType.STANDARD, BigDecimal.TEN, 1000, true);
        Map<PrintedItem, Integer> printedItems = press.getPrintedItems();
        assertEquals(0, press.getCurrentPaperLoad());
        assertEquals(1000, printedItems.values().iterator().next());
    }

    @Test
    void CalculatePaperCost_MaximumPageCount_ReturnsCost() {
        BigDecimal cost = service.calculatePaperCost(printHouse, PaperType.STANDARD, Size.A4, ModelsConstants.MAX_PAGE_COUNT);
        assertTrue(cost.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void GetTotalCostForPrint_EmptyPresses_ReturnsZero() {
        BigDecimal cost = service.getTotalCostForPrint(printHouse);
        assertEquals(BigDecimal.ZERO, cost);
    }

    @Test
    void TotalPrintedPages_NoItems_ReturnsZero() {
        service.addPrintingPress(printHouse, press);
        long totalPages = service.totalPrintedPages(printHouse, press);
        assertEquals(0, totalPages);
    }

    @Test
    void LoadPaper_MaximumCapacity_LoadsSuccessfully() {
        service.addPrintingPress(printHouse, press);
        service.loadPaper(printHouse, press, 500); // 500 + 500 = 1000
        assertEquals(1000, press.getCurrentPaperLoad());
    }

    @Test
    void UpdatePrintingPress_AllNullParameters_DoesNotThrow() {
        service.addPrintingPress(printHouse, press);
        int originalMaxPaperLoad = press.getMaxPaperLoad();
        int originalCurrentPaperLoad = press.getCurrentPaperLoad();
        boolean originalColour = press.isColour();
        int originalMaxPages = press.getMaximumPagesPerMinute();
        service.updatePrintingPress(printHouse, press, null, null, null, null);
        assertEquals(originalMaxPaperLoad, press.getMaxPaperLoad());
        assertEquals(originalCurrentPaperLoad, press.getCurrentPaperLoad());
        assertEquals(originalColour, press.isColour());
        assertEquals(originalMaxPages, press.getMaximumPagesPerMinute());
    }

    @Test
    void GetTotalRevenue_DiscountApplied_ReturnsDiscountedRevenue() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        press.setCurrentPaperLoad(1000); // Increase to handle 750 sheets (100 / 2 * 15)
        service.addPrintingPress(printHouse, press);
        service.printItem(printHouse, press, edition, PaperType.STANDARD, BigDecimal.TEN, 15, true); // Above discount count (10)
        BigDecimal revenue = service.getTotalRevenue(printHouse);
        BigDecimal expected = BigDecimal.valueOf(15).multiply(BigDecimal.TEN).multiply(BigDecimal.valueOf(0.95)); // 15 * 10 * 0.95 = 142.5 (5% discount)
        assertEquals(expected, revenue);
    }
}
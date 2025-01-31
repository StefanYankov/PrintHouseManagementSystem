package org.PrintHouse.models;

import org.PrintHouse.models.Contracts.IPrintedItem;
import org.PrintHouse.utilities.exceptions.InvalidPaperLoadException;
import org.PrintHouse.utilities.globalconstants.ModelsConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PrintingPressTest {

    private PrintingPress<PaperType, Size> printingPress;

    private Edition<Size> validEditionA4;
    private Edition<Size> validEditionA5;
    private String validTitle;
    private int validMaximumPaperLoad;
    BigDecimal priceStandard;
    BigDecimal priceGlossy;

    private PrintedItem<PaperType, Size> itemStandard;
    private PrintedItem<PaperType, Size> itemGlossy;

    @BeforeEach
    void setUp() {
        printingPress = new PrintingPress<>(100, 50, true, 30);
        validTitle = "The Lord of the Rings: The Two Towers";
        validEditionA4 = new Edition<>(validTitle, 10, Size.A4);
        validEditionA5 = new Edition<>(validTitle, 20, Size.A5);

        priceStandard = BigDecimal.valueOf(200);
        priceGlossy = BigDecimal.valueOf(300);
        validMaximumPaperLoad = 100;
        itemStandard = new PrintedItem<>(validEditionA4, PaperType.STANDARD, priceStandard);
        itemGlossy = new PrintedItem<>(validEditionA5, PaperType.GLOSSY, priceGlossy);
    }

    // 1) Happy path
    @Test
    public void constructorWithValidDataShouldSetupCorrectly() {
        assertNotNull(printingPress);
        assertEquals(validMaximumPaperLoad, printingPress.getMaxPaperLoad());
        assertEquals(50, printingPress.getCurrentPaperLoad());
        assertTrue(printingPress.getIsColor());
        assertEquals(30, printingPress.getMaximumPagesPerMinute());
        assertNotNull(printingPress.getPrintedItems());
    }

    @Test
    public void setMaxPaperLoadShouldSetMaxPaperLoadCorrectly() {
        assertEquals(validMaximumPaperLoad, printingPress.getMaxPaperLoad());
        printingPress.setMaxPaperLoad(0);
        assertEquals(0, printingPress.getMaxPaperLoad());
        printingPress.setMaxPaperLoad(ModelsConstants.MAXIMUM_PAPER_LOAD);
        assertEquals(ModelsConstants.MAXIMUM_PAPER_LOAD, printingPress.getMaxPaperLoad());
    }

    @Test
    public void setCurrentPaperLoadShouldSetCurrentPaperLoadCorrectly() {
        this.printingPress.setCurrentPaperLoad(0);
        assertEquals(0, printingPress.getCurrentPaperLoad());

        this.printingPress.setCurrentPaperLoad(100);
        assertEquals(100, printingPress.getCurrentPaperLoad());

        this.printingPress.setCurrentPaperLoad(validMaximumPaperLoad);
        assertEquals(validMaximumPaperLoad, printingPress.getCurrentPaperLoad());
    }

    // Error cases

    @Test
    public void setMaxPaperLoadWithNegativeValueShouldThrowException() {
        assertThrows(InvalidPaperLoadException.class, () -> this.printingPress.setMaxPaperLoad(-1));
    }

    @Test
    public void setMaxPaperLoadAboveMaximumAllowedShouldThrowException() {
        assertThrows(InvalidPaperLoadException.class, () -> {
            this.printingPress.setMaxPaperLoad(ModelsConstants.MAXIMUM_PAPER_LOAD + 1);
        });
    }

    @Test
    public void setCurrentPaperLoadWithNegativeValueShouldThrowException() {
        assertThrows(InvalidPaperLoadException.class, () -> {
            this.printingPress.setCurrentPaperLoad(-1);
        });

        assertThrows(InvalidPaperLoadException.class, () -> {
            this.printingPress.setCurrentPaperLoad(Integer.MIN_VALUE);
        });
    }

    @Test
    public void setCurrentPaperLoadAboveMaximumAllowedShouldThrowException() {
        assertThrows(InvalidPaperLoadException.class, () -> {
            this.printingPress.setCurrentPaperLoad(validMaximumPaperLoad + 1);
        });
    }
    // Common edge cases

    // 4) Less Common Edge Cases
//    @Test
//    void testPrintItems_ZeroCopies() {
//
//        IPrintedItem<PaperType, Size> printedItem = 00
//11
//        printingPress.printItems(true, printedItem, 0);
//        Map<IPrintedItem<PaperType, Size>, Integer> printedItems = printingPress.getPrintedItems();
//        assertEquals(0, printedItems.size());
//        assertEquals(500, printingPress.getCurrentPaperLoad()); // No pages used
//    }

    @Test
    void testPrintItems_NullPrintedItem() {
        assertThrows(NullPointerException.class, () -> printingPress.printItems(true, null, 10));
    }

//    @Test
//    void testPrintItems_NegativeCopies() {
//        assertThrows(IllegalArgumentException.class, () -> printingPress.printItems(true, printedItem, -1));
//    }
}

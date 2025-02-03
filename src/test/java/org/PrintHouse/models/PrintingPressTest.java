package org.PrintHouse.models;

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
    private BigDecimal priceStandard;
    private BigDecimal priceGlossy;
    private boolean isColour;


    @BeforeEach
    void setUp() {
        isColour = true;

        printingPress = new PrintingPress<>(100, 50, isColour, 30);
        validTitle = "The Lord of the Rings: The Two Towers";
        validEditionA4 = new Edition<>(validTitle, 10, Size.A4);
        validEditionA5 = new Edition<>(validTitle, 20, Size.A5);

        priceStandard = BigDecimal.valueOf(200);
        priceGlossy = BigDecimal.valueOf(300);
        validMaximumPaperLoad = 100;
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

    @Test
    public void printItems_WithValidCount_ShouldWorkCorrectly() {
        int copies = 3;
        printingPress.printItems(true, validEditionA4, PaperType.STANDARD, priceStandard, copies);
        var items = printingPress.getPrintedItems().values()
                .stream()
                .mapToInt(x -> x)
                .sum();
        assertEquals(copies, items);
    }

    @Test
    public void printItems_DuplicatePrintItem_ShouldIncrementTheCountValue() {
        printingPress.printItems(isColour, validEditionA4, PaperType.STANDARD, priceStandard, 1);
        printingPress.printItems(isColour, validEditionA4, PaperType.STANDARD, priceStandard, 2);

        var copiesCount = printingPress.getPrintedItems()
                .entrySet()
                .stream()
                .filter(x -> x.getKey().getEdition().equals(validEditionA4))
                .mapToInt(x -> x.getValue())
                .sum();

        assertEquals(3, copiesCount);

        printingPress.printItems(isColour, validEditionA4, PaperType.GLOSSY, priceGlossy, 1);

        int totalPrintedItems = printingPress.getPrintedItems()
                .values()
                .stream()
                .mapToInt(x -> x)
                .sum();

        assertEquals(4, totalPrintedItems);
    }

    @Test
    public void printAnItem_DuplicatePrintItem_ShouldAmendTheCountValue() {
        printingPress.printAnItem(true, validEditionA4, PaperType.STANDARD, priceStandard);
        printingPress.printAnItem(true, validEditionA4, PaperType.STANDARD, priceStandard);
        printingPress.printAnItem(true, validEditionA4, PaperType.STANDARD, priceStandard);

        var copiesCount = printingPress.getPrintedItems()
                .entrySet()
                .stream()
                .filter(x -> x.getKey().getEdition().equals(validEditionA4))
                .mapToInt(x -> x.getValue())
                .sum();
        assertEquals(3, copiesCount);
    }

    @Test
    public void printItems_DifferentPaperTypes_ShouldAddThemCorrectly() {
        printingPress.printItems(true, validEditionA4, PaperType.STANDARD, BigDecimal.valueOf(50), 1);
        printingPress.printItems(true, validEditionA4, PaperType.STANDARD, BigDecimal.valueOf(50), 1);
        assertEquals(2, this.printingPress.getPrintedItems().size());
    }

    // Error cases

    @Test
    public void setMaxPaperLoadWithNegativeValueShouldThrowException() {
        assertThrows(InvalidPaperLoadException.class, () -> this.printingPress.setMaxPaperLoad(-1));
        assertThrows(InvalidPaperLoadException.class, () -> this.printingPress.setMaxPaperLoad(Integer.MIN_VALUE));

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

}

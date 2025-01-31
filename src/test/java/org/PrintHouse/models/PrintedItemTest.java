package org.PrintHouse.models;

import org.PrintHouse.models.Contracts.IEdition;
import org.PrintHouse.utilities.exceptions.InvalidEditionException;
import org.PrintHouse.utilities.exceptions.InvalidPaperTypeException;
import org.PrintHouse.utilities.exceptions.InvalidPriceException;
import org.PrintHouse.utilities.globalconstants.ExceptionMessages;
import org.PrintHouse.utilities.globalconstants.ModelsConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrintedItemTest {
    private PrintedItem<PaperType, Size> printedItem;
    private String title;
    private int numberOfPages;
    private Size size;
    PaperType paperType;
    private IEdition edition;
    private BigDecimal price;

    @BeforeEach
    public void setUp() {
        title = "The Lord of the Rings: The Two Towers";
        numberOfPages = 352;
        size = Size.A4;
        edition = new Edition<>(title, numberOfPages, size);
        paperType = PaperType.STANDARD;
        price = BigDecimal.valueOf(100);
        printedItem = new PrintedItem<>(edition, paperType, price);
    }

    // Happy path
    @Test
    public void constructorWithValidDataShouldSetupCorrectly() {
        // Assert
        assertNotNull(printedItem);
        assertEquals(this.edition, printedItem.getEdition());
        assertEquals(this.paperType, printedItem.getPaperType());
        assertEquals(this.price, printedItem.getPrice());
    }

    @Test
    public void setPaperTypeWithValidDataShouldSetCorrectly() {
        // Arrange
        PaperType paperType = PaperType.STANDARD;

        // Act
        printedItem.setPaperType(paperType);

        // Assert
        assertEquals(paperType, printedItem.getPaperType());
    }

    @Test
    public void setPriceWithValidDataShouldSetCorrectly() {

        // Arrange
        BigDecimal testPrice = BigDecimal.valueOf(5);

        // Act
        printedItem.setPrice(testPrice);

        // Assert
        assertEquals(testPrice, printedItem.getPrice());
    }

    // Error cases
    @Test
    public void setEditionToNullShouldThrowAnException(){
        // Act
        InvalidEditionException exception = assertThrows(InvalidEditionException.class, () ->
                printedItem.setEdition(null));

        // Assert
        assertEquals(ExceptionMessages.EDITION_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    public void setPaperTypeToNullShouldThrowAnException() {
        // Act
        InvalidPaperTypeException exception = assertThrows(InvalidPaperTypeException.class, () ->
                printedItem.setPaperType(null));

        // Assert
        assertEquals(ExceptionMessages.PAPER_TYPE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    public void setPriceToNullShouldThrowAnException() {

        // Act
        InvalidPriceException exception = assertThrows(InvalidPriceException.class, () ->
                printedItem.setPrice(null));

        // Assert
        assertEquals(ExceptionMessages.PRICE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    public void setPriceToNegativeShouldThrowAnException() {

        // Arrange
        BigDecimal negativePrice = BigDecimal.valueOf(-1);

        // Act
        InvalidPriceException exception = assertThrows(InvalidPriceException.class, () ->
                printedItem.setPrice(negativePrice));

        // Assert
        assertEquals(ExceptionMessages.PRICE_CANNOT_BE_A_NEGATIVE_NUMBER, exception.getMessage());
    }

    @Test
    public void setPriceAboveTheDefinedMaximumShouldThrownAnException(){

        // Arrange
        BigDecimal priceAboveTheDefinedMaximum = BigDecimal.ONE.add(ModelsConstants.MAXIMUM_PRICE);

        // Act
        InvalidPriceException exception = assertThrows(InvalidPriceException.class, () ->
                printedItem.setPrice(priceAboveTheDefinedMaximum));

        // Assert
        assertEquals(MessageFormat
                .format(ExceptionMessages.PRICE_CANNOT_BE_GREATER_THAN_THE_MAXIMUM_PRICE,ModelsConstants.MAXIMUM_PRICE), exception.getMessage());
    }

}
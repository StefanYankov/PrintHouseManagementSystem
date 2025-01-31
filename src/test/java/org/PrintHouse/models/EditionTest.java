package org.PrintHouse.models;

import org.PrintHouse.utilities.exceptions.InvalidNumberOfPagesException;
import org.PrintHouse.utilities.exceptions.InvalidPageSizeException;
import org.PrintHouse.utilities.exceptions.InvalidTitleException;
import org.PrintHouse.utilities.globalconstants.ExceptionMessages;
import org.PrintHouse.utilities.globalconstants.ModelsConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Edition class.
 */
public class EditionTest {

    private String title;
    private int numberOfPages;
    private Size size;
    private Edition<Size> edition;

    @BeforeEach
    public void setUp() {
        // Arrange common values
        title = "The Lord of the Rings: The Two Towers";
        numberOfPages = 352;
        size = Size.A4;
        edition = new Edition<>(title, numberOfPages, size);
    }

    @Test
    public void constructorWithValidDataShouldSetupCorrectly() {
        // Act: Create a new Edition
        Edition<Size> edition = new Edition<>(title, numberOfPages, size);

        // Assert: Check if the values are set correctly
        assertNotNull(edition);
        assertEquals(title, edition.getTitle());
        assertEquals(numberOfPages, edition.getNumberOfPages());
        assertEquals(size, edition.getSize());
    }

    @Test
    public void setTitleShouldWorkCorrectly() {
        // Act: Set a new valid title
        edition.setTitle(this.title);

        // Assert: Check if the title is updated correctly
        assertEquals(this.title, edition.getTitle());
    }

    @Test
    public void setTitleToNullShouldThrowAnException() {
        // Act & Assert
        InvalidTitleException exception = assertThrows(InvalidTitleException.class, () -> edition.setTitle(null));
        assertEquals(String.format(ExceptionMessages.TITLE_CANNOT_BE_NULL), exception.getMessage());
    }

    @Test
    public void setTitleToEmptyOrWhitespaceShouldThrowAnException() {
        // Act & Assert
        InvalidTitleException exception = assertThrows(InvalidTitleException.class, () -> edition.setTitle(" "));

        assertEquals(ExceptionMessages.TITLE_CANNOT_BE_EMPTY, exception.getMessage());
    }

    @Test
    public void setTitleToBelowMinimumLengthShouldThrowAnException() {
        // Act
        InvalidTitleException exception = assertThrows(InvalidTitleException.class, () -> edition.setTitle("A"));

        // Assert
        assertEquals(MessageFormat.format(ExceptionMessages.TITLE_LENGTH_INVALID, ModelsConstants.MIN_TITLE_LENGTH, ModelsConstants.MAX_TITLE_LENGTH), exception.getMessage());
    }

    @Test
    public void setTitleToAboveMaximumLengthShouldThrowAnException() {
        // Act
        InvalidTitleException exception = assertThrows(InvalidTitleException.class, () -> edition.setTitle("A".repeat(151)));

        // Assert
        assertEquals(MessageFormat.format(ExceptionMessages.TITLE_LENGTH_INVALID, ModelsConstants.MIN_TITLE_LENGTH, ModelsConstants.MAX_TITLE_LENGTH), exception.getMessage());
    }

    @Test
    public void setNumberOfPagesToValidCountShouldWorkCorrectly() {
        // Act
        edition.setNumberOfPages(this.numberOfPages);

        // Assert
        assertEquals(this.numberOfPages, edition.getNumberOfPages());
    }

    @Test
    public void setNumberOfPagesAtMinLimitShouldThrowException() {
        // Act
        InvalidNumberOfPagesException exception = assertThrows(InvalidNumberOfPagesException.class,
                () -> edition.setNumberOfPages(ModelsConstants.MIN_PAGE_COUNT - 1));

        // Assert
        assertEquals(MessageFormat.format(ExceptionMessages.NUMBER_OF_PAGES_INVALID, ModelsConstants.MIN_PAGE_COUNT, ModelsConstants.MAX_PAGE_COUNT), exception.getMessage());

    }

    @Test
    public void setNumberOfPagesAtMaxLimitShouldThrowException() {
        // Act
        InvalidNumberOfPagesException exception = assertThrows(InvalidNumberOfPagesException.class,
                () -> edition.setNumberOfPages(ModelsConstants.MAX_PAGE_COUNT + 1));

        // Assert
        assertEquals(MessageFormat.format(ExceptionMessages.NUMBER_OF_PAGES_INVALID, ModelsConstants.MIN_PAGE_COUNT, ModelsConstants.MAX_PAGE_COUNT), exception.getMessage());
    }

    @Test
    public void setPageSizeShouldWorkCorrectly() {
        // Act
        edition.setSize(this.size);

        // Assert
        assertEquals(this.size, edition.getSize());
    }

    @Test
    public void pageSizeIsNullShouldThrowAnException() {
        // Act
        InvalidPageSizeException exception = assertThrows(InvalidPageSizeException.class, () -> {
            edition.setSize(null);
        });

        //Assert
        assertEquals(ExceptionMessages.INVALID_PAGE_SIZE, exception.getMessage());
    }
}

package services;

import data.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import services.contracts.IEditionService;
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

public class EditionServiceTests {
    private IEditionService service;
    private PrintHouse printHouse;
    private ISerializationService<Edition> serializationService;

    @BeforeEach
    void setUp() {
        serializationService = mock(ISerializationService.class);
        service = new EditionService(serializationService);
        printHouse = new PrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5));
    }

    // Happy Path
    @Test
    void addEdition_WhenValid_AddsSuccessfully() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addEdition(printHouse, edition);
        assertEquals(1, service.getEditions(printHouse).size());
        assertTrue(service.getEditions(printHouse).contains(edition));
    }

    @Test
    void getEdition_ValidIndex_ReturnsEdition() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addEdition(printHouse, edition);
        Edition result = service.getEdition(printHouse, 0);
        assertEquals(edition, result);
    }

    @Test
    void updateEdition_ValidUpdates_UpdatesFields() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addEdition(printHouse, edition);
        service.updateEdition(printHouse, edition, "New Title", 1500, Size.A3);
        assertEquals("New Title", edition.getTitle());
        assertEquals(1500, edition.getNumberOfPages());
        assertEquals(Size.A3, edition.getSize());
    }

    @Test
    void removeEdition_ValidEdition_RemovesSuccessfully() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addEdition(printHouse, edition);
        service.removeEdition(printHouse, edition);
        assertTrue(service.getEditions(printHouse).isEmpty());
    }

    @Test
    void saveEditions_WhenValid_SerializesSuccessfully(@TempDir Path tempDir) {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addEdition(printHouse, edition);
        String filePath = tempDir.resolve("editions").toString();
        service.saveEditions(printHouse, filePath);
        verify(serializationService).serialize(anyList(), eq(filePath + "_ph" + printHouse.hashCode() + ".ser"));
    }

    @Test
    void loadEditions_WhenValid_LoadsSuccessfully(@TempDir Path tempDir) {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        String filePath = tempDir.resolve("editions").toString();
        when(serializationService.deserialize(filePath + "_ph" + printHouse.hashCode() + ".ser"))
                .thenReturn(List.of(edition));
        service.loadEditions(printHouse, filePath);
        assertEquals(1, service.getEditions(printHouse).size());
        assertEquals(edition, service.getEditions(printHouse).getFirst());
    }

    // Error Cases
    @Test
    void addEdition_NullPrintHouse_ThrowsInvalidPrintHouseException() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        InvalidPrintHouseException exception = assertThrows(InvalidPrintHouseException.class, () ->
                service.addEdition(null, edition));
        assertEquals(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void addEdition_NullEdition_ThrowsInvalidEditionException() {
        InvalidEditionException exception = assertThrows(InvalidEditionException.class, () ->
                service.addEdition(printHouse, null));
        assertEquals(ExceptionMessages.EDITION_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void addEdition_WhenEditionAlreadyExists_ThrowsInvalidEditionException() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addEdition(printHouse, edition);
        InvalidEditionException exception = assertThrows(InvalidEditionException.class, () ->
                service.addEdition(printHouse, edition));
        assertEquals(ExceptionMessages.EDITION_NOT_IN_PRINT_HOUSE, exception.getMessage());
        assertEquals(1, service.getEditions(printHouse).size());
    }

    @Test
    void getEdition_NullPrintHouse_ThrowsInvalidPrintHouseException() {
        InvalidPrintHouseException exception = assertThrows(InvalidPrintHouseException.class, () ->
                service.getEdition(null, 0));
        assertEquals(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void getEdition_InvalidIndex_ThrowsInvalidEditionException() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addEdition(printHouse, edition);
        InvalidEditionException exception = assertThrows(InvalidEditionException.class, () ->
                service.getEdition(printHouse, 1));
        assertEquals(ExceptionMessages.EDITION_NOT_IN_PRINT_HOUSE, exception.getMessage());
    }

    @Test
    void updateEdition_NullPrintHouse_ThrowsInvalidPrintHouseException() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        InvalidPrintHouseException exception = assertThrows(InvalidPrintHouseException.class, () ->
                service.updateEdition(null, edition, "New Title", null, null));
        assertEquals(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void updateEdition_NullEdition_ThrowsInvalidEditionException() {
        InvalidEditionException exception = assertThrows(InvalidEditionException.class, () ->
                service.updateEdition(printHouse, null, "New Title", null, null));
        assertEquals(ExceptionMessages.EDITION_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void updateEdition_InvalidTitleLength_ThrowsInvalidTitleException() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addEdition(printHouse, edition);
        String longTitle = "A".repeat(ModelsConstants.MAX_TITLE_LENGTH + 1);
        InvalidTitleException exception = assertThrows(InvalidTitleException.class, () ->
                service.updateEdition(printHouse, edition, longTitle, null, null));
        assertEquals(ExceptionMessages.TITLE_LENGTH_INVALID, exception.getMessage());
    }

    @Test
    void updateEdition_InvalidPageCount_ThrowsInvalidNumberOfPagesException() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addEdition(printHouse, edition);
        InvalidNumberOfPagesException exception = assertThrows(InvalidNumberOfPagesException.class, () ->
                service.updateEdition(printHouse, edition, null, ModelsConstants.MAX_PAGE_COUNT + 1, null));
        assertEquals(ExceptionMessages.NUMBER_OF_PAGES_INVALID, exception.getMessage());
    }

    @Test
    void removeEdition_NullPrintHouse_ThrowsInvalidPrintHouseException() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        InvalidPrintHouseException exception = assertThrows(InvalidPrintHouseException.class, () ->
                service.removeEdition(null, edition));
        assertEquals(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void removeEdition_NullEdition_ThrowsInvalidEditionException() {
        InvalidEditionException exception = assertThrows(InvalidEditionException.class, () ->
                service.removeEdition(printHouse, null));
        assertEquals(ExceptionMessages.EDITION_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void saveEditions_NullPath_ThrowsIllegalArgumentException() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addEdition(printHouse, edition);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                service.saveEditions(printHouse, null));
        assertEquals(ExceptionMessages.FILE_PATH_CANNOT_BE_NULL_OR_EMPTY, exception.getMessage());
    }

    @Test
    void loadEditions_NullPrintHouse_ThrowsInvalidPrintHouseException() {
        InvalidPrintHouseException exception = assertThrows(InvalidPrintHouseException.class, () ->
                service.loadEditions(null, "test"));
        assertEquals(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL, exception.getMessage());
    }

    @Test
    void loadEditions_NullPath_ThrowsIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                service.loadEditions(printHouse, null));
        assertEquals(ExceptionMessages.FILE_PATH_CANNOT_BE_NULL_OR_EMPTY, exception.getMessage());
    }

    // Edge Cases

    @Test
    void getEdition_EmptyList_ThrowsInvalidEditionException() {
        InvalidEditionException exception = assertThrows(InvalidEditionException.class, () ->
                service.getEdition(printHouse, 0));
        assertEquals(ExceptionMessages.EDITION_NOT_IN_PRINT_HOUSE, exception.getMessage());
    }

    @Test
    void updateEdition_EditionNotInPrintHouse_ThrowsInvalidEditionException() {
        Edition unknownEdition = new Edition("The Hobbit", 300, Size.A5);
        InvalidEditionException exception = assertThrows(InvalidEditionException.class, () ->
                service.updateEdition(printHouse, unknownEdition, "New Title", null, null));
        assertEquals(ExceptionMessages.EDITION_NOT_IN_PRINT_HOUSE, exception.getMessage());
    }

    @Test
    void updateEdition_NullFields_DoesNotUpdate() {
        Edition edition = new Edition("Test Book", 100, Size.A4);
        service.addEdition(printHouse, edition);
        service.updateEdition(printHouse, edition, null, null, null);
        assertEquals("Test Book", edition.getTitle());
        assertEquals(100, edition.getNumberOfPages());
        assertEquals(Size.A4, edition.getSize());
    }

    @Test
    void removeEdition_EditionNotInPrintHouse_DoesNotThrow() {
        Edition unknownEdition = new Edition("The Hobbit", 300, Size.A5);
        service.removeEdition(printHouse, unknownEdition); // Logs warning, no throw
        assertTrue(service.getEditions(printHouse).isEmpty());
    }

    @Test
    void saveEditions_EmptyList_SerializesEmpty(@TempDir Path tempDir) {
        String filePath = tempDir.resolve("editions").toString();
        service.saveEditions(printHouse, filePath);
        verify(serializationService).serialize(eq(Collections.emptyList()),
                eq(filePath + "_ph" + printHouse.hashCode() + ".ser"));
    }

    @Test
    void loadEditions_FileNotFound_LoadsEmpty(@TempDir Path tempDir) {
        String filePath = tempDir.resolve("nonexistent").toString();
        when(serializationService.deserialize(anyString())).thenReturn(Collections.emptyList());
        service.loadEditions(printHouse, filePath);
        assertTrue(service.getEditions(printHouse).isEmpty());
    }

    @Test
    void addEdition_MultiplePrintHouses_SeparatesCorrectly() {
        PrintHouse ph2 = new PrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                List.of(EmployeeType.OPERATOR), BigDecimal.valueOf(6000), 5, BigDecimal.valueOf(5));
        Edition ed1 = new Edition("Book1", 100, Size.A4);
        Edition ed2 = new Edition("Book2", 200, Size.A3);
        service.addEdition(printHouse, ed1);
        service.addEdition(ph2, ed2);
        assertEquals(List.of(ed1), service.getEditions(printHouse));
        assertEquals(List.of(ed2), service.getEditions(ph2));
    }
}
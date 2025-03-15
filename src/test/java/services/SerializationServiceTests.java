package services;

import data.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SerializationServiceTests {
    private SerializationService<PrintHouse> service;

    @BeforeEach
    void setUp() {
        service = new SerializationService<>();
    }

    // Happy Path
    @Test
    void serializeList_WhenValidData_SerializesSuccessfully(@TempDir Path tempDir) {
        PrintHouse ph = new PrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5));
        String filePath = tempDir.resolve("test.ser").toString();
        service.serialize(List.of(ph), filePath);
        // Verification as in original
    }

    @Test
    void deserialize_WhenValidFile_ReturnsList(@TempDir Path tempDir) throws IOException {
        PrintHouse ph = new PrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5));
        String filePath = tempDir.resolve("test.ser").toString();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(List.of(ph));
        }
        assertEquals(1, service.deserialize(filePath).size());
    }

    // Error Cases

    @Test
    void serializeSingle_WhenPathNull_ThrowsException() {
        PrintHouse ph = new PrintHouse();
        assertThrows(IllegalArgumentException.class, () -> service.serialize(ph, null));
    }

    // Edge Cases
    @Test
    void serializeList_WhenLargeData_SerializesSuccessfully(@TempDir Path tempDir) {
        PrintHouse ph = new PrintHouse(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.valueOf(1000),
                List.of(EmployeeType.MANAGER), BigDecimal.valueOf(5000), 10, BigDecimal.valueOf(5));
        String filePath = tempDir.resolve("large.ser").toString();
        service.serialize(Collections.nCopies(1000, ph), filePath);
        assertEquals(1000, service.deserialize(filePath).size());
    }


    @Test
    void deserializeSingle_WhenFileNotFound_ReturnsNull() {
        assertNull(service.deserializeSingleObject("nonexistent.ser"));
    }
}
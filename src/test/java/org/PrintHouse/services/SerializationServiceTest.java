package org.PrintHouse.services;

import org.PrintHouse.utilities.SerializationService;
import org.PrintHouse.utilities.contracts.ISerializable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SerializationServiceTest {

    private static final String TEST_FILE_PATH = "test_serialization.dat";
    private SerializationService<TestSerializableEntity> serializationService;

    @BeforeEach
    public void setUp() {
        serializationService = new SerializationService<>(TEST_FILE_PATH);
    }

    @AfterEach
    public void tearDown() {
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void Serialize_WhenSingleObjectIsSerialized_ItShouldBeDeserializedCorrectly() {
        TestSerializableEntity entity = new TestSerializableEntity(1, "Test");
        serializationService.serialize(entity);

        TestSerializableEntity deserializedEntity = serializationService.deserializeSingleObject();
        assertNotNull(deserializedEntity);
        assertEquals(entity.getId(), deserializedEntity.getId());
        assertEquals(entity.getName(), deserializedEntity.getName());
    }

    @Test
    public void Serialize_WhenListOfObjectsIsSerialized_ItShouldBeDeserializedCorrectly() {
        List<TestSerializableEntity> entities = new ArrayList<>();
        entities.add(new TestSerializableEntity(1, "Entity1"));
        entities.add(new TestSerializableEntity(2, "Entity2"));

        serializationService.serialize(entities);
        List<TestSerializableEntity> deserializedList = serializationService.deserialize();

        assertNotNull(deserializedList);
        assertEquals(entities.size(), deserializedList.size());
        assertEquals(entities.getFirst().getName(), deserializedList.getFirst().getName());
    }

    @Test
    public void deserializeSingleObject_FileDoesNotExist_ShouldReturnNull() {
        SerializationService<TestSerializableEntity> newService = new SerializationService<>("non_existent.dat");
        assertNull(newService.deserializeSingleObject());
    }

    @Test
    public void deserializeList_FileDoesNotExist_ShouldReturnEmptyList() {
        SerializationService<TestSerializableEntity> newService = new SerializationService<>("non_existent.dat");
        assertTrue(newService.deserialize().isEmpty());
    }

    @Test
    public void constructor_WithPathEndingWithSlash_ShouldConcatenateCorrectly() {
        // Arrange
        String path = "test/path/";
        String fileName = "testFile.dat";
        String expectedFilePath = "test/path/testFile.dat";

        // Act
        SerializationService<TestSerializableEntity> serializationService = new SerializationService<>(path, fileName);

        // Assert
        assertEquals(expectedFilePath, getFilePath(serializationService));
    }

    @Test
    public void constructor_WithPathNotEndingWithSlash_ShouldConcatenateCorrectly() {
        // Arrange
        String path = "test/path";
        String fileName = "testFile.dat";
        String expectedFilePath = "test/path/testFile.dat";

        // Act
        SerializationService<TestSerializableEntity> serializationService = new SerializationService<>(path, fileName);

        // Assert
        assertEquals(expectedFilePath, getFilePath(serializationService));
    }



    // Helper method to access the private filePath field for testing purposes
    private String getFilePath(SerializationService<?> service) {
        try {
            java.lang.reflect.Field field = SerializationService.class.getDeclaredField("filePath");
            field.setAccessible(true);
            return (String) field.get(service);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class TestSerializableEntity implements ISerializable, java.io.Serializable {
        private final int id;
        private final String name;

        public TestSerializableEntity(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() { return id; }
        public String getName() { return name; }
    }
}
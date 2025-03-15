package services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.contracts.ISerializationService;
import utilities.globalconstants.ExceptionMessages;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides serialization and deserialization functionality for objects within the services layer.
 * @param <T> The type of object to serialize/deserialize, must implement Serializable.
 */
public class SerializationService<T extends Serializable> implements ISerializationService<T> {
    private static final Logger logger = LoggerFactory.getLogger(SerializationService.class);

    /** {@inheritDoc} */
    @Override
    public void serialize(T entity, String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            logger.error("Invalid file path: {}", filePath);
            throw new IllegalArgumentException(ExceptionMessages.FILE_PATH_CANNOT_BE_NULL_OR_EMPTY);
        }
        if (entity == null) {
            logger.error("Entity to serialize cannot be null for filePath: {}", filePath);
            throw new IllegalArgumentException("Entity cannot be null");
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(entity);
            logger.info("Serialized entity to {}", filePath);
        } catch (IOException e) {
            logger.error("Serialization failed for {}: {}", filePath, e.getMessage(), e);
            throw new RuntimeException("Serialization failed: " + e.getMessage(), e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void serialize(List<T> entities, String filePath) {
        if (entities == null) {
            logger.error("Entities list cannot be null");
            throw new IllegalArgumentException("Entities list cannot be null");
        }
        if (filePath == null || filePath.trim().isEmpty()) {
            logger.error("Invalid file path: {}", filePath);
            throw new IllegalArgumentException(ExceptionMessages.FILE_PATH_CANNOT_BE_NULL_OR_EMPTY);
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(entities);
            logger.info("Serialized {} entities to {}", entities.size(), filePath);
        } catch (IOException e) {
            logger.error("Serialization failed for {}: {}", filePath, e.getMessage(), e);
            throw new RuntimeException("Serialization failed: " + e.getMessage(), e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public T deserializeSingleObject(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            logger.error("Invalid file path: {}", filePath);
            throw new IllegalArgumentException(ExceptionMessages.FILE_PATH_CANNOT_BE_NULL_OR_EMPTY);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            T entity = (T) ois.readObject();
            logger.info("Deserialized entity from {}", filePath);
            return entity;
        } catch (IOException | ClassNotFoundException e) {
            logger.warn("Deserialization failed for {}: {}", filePath, e.getMessage());
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<T> deserialize(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            logger.error("Invalid file path: {}", filePath);
            throw new IllegalArgumentException(ExceptionMessages.FILE_PATH_CANNOT_BE_NULL_OR_EMPTY);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            List<T> entities = (List<T>) ois.readObject();
            logger.info("Deserialized {} entities from {}", entities.size(), filePath);
            return entities;
        } catch (IOException | ClassNotFoundException e) {
            logger.warn("Deserialization failed for {}: {}, returning empty list", filePath, e.getMessage());
            return new ArrayList<>();
        }
    }
}
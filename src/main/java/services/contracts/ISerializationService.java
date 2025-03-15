package services.contracts;

import java.io.Serializable;
import java.util.List;

/**
 * Defines methods for serializing and deserializing objects to and from persistent storage.
 *
 * <p>This interface provides a generic contract for saving and loading entities, ensuring
 * that implementations handle serialization in a consistent manner. It is designed to work
 * with any class that implements {@link Serializable}.</p>
 *
 * @param <T> the type of object to serialize/deserialize, must implement {@link Serializable}
 */
public interface ISerializationService<T extends Serializable> {
    /**
     * Serializes a single entity to the specified file path.
     *
     * @param entity   the entity to serialize
     * @param filePath the file path where the entity will be saved
     * @throws IllegalArgumentException if the file path is null or empty
     * @throws RuntimeException         if serialization fails due to I/O errors
     */
    void serialize(T entity, String filePath);

    /**
     * Serializes a list of entities to the specified file path.
     *
     * @param entities the list of entities to serialize
     * @param filePath the file path where the entities will be saved
     * @throws IllegalArgumentException if entities or file path is null or empty
     * @throws RuntimeException         if serialization fails due to I/O errors
     */
    void serialize(List<T> entities, String filePath);

    /**
     * Deserializes a single object from the specified file path.
     *
     * @param filePath the file path to read from
     * @return the deserialized object, or null if deserialization fails
     * @throws IllegalArgumentException if the file path is null or empty
     */
    T deserializeSingleObject(String filePath);

    /**
     * Deserializes a list of objects from the specified file path.
     *
     * @param filePath the file path to read from
     * @return the list of deserialized objects, or an empty list if deserialization fails
     * @throws IllegalArgumentException if the file path is null or empty
     */
    List<T> deserialize(String filePath);
}
package org.PrintHouse.utilities;

import org.PrintHouse.utilities.contracts.ISerializable;
import org.PrintHouse.utilities.globalconstants.ServicesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Provides functionality to serialize and deserialize objects that implement the {@link ISerializable} interface.
 * This class is flexible to handle different types that implement ISerializable without being coupled to specific entities.
 *
 * @param <T> The type of object to serialize or deserialize, which must implement {@link ISerializable}.
 */
public class SerializationService<T extends ISerializable> {

    private static final Logger logger = LoggerFactory.getLogger(SerializationService.class);
    private final String filePath;

    /**
     * Constructs a new {@code SerializationService} instance with the specified file path.
     *
     * @param filePath The path to the file where serialized data will be stored or read from.
     */
    public SerializationService(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Constructs a new {@code SerializationService} instance with the specified path and file name.
     *
     * @param path     The directory path where the file will be stored.
     * @param fileName The name of the file where serialized data will be stored or read from.
     */
    public SerializationService(String path, String fileName) {
        this.filePath = path.endsWith("/") ? path + fileName : path + "/" + fileName;
    }

    /**
     * Serializes a single object that implements ISerializable to a file.
     */
    public void serialize(T entity) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(entity);
            logger.info(MessageFormat.format(ServicesConstants.SUCCESSFUL_SERIALIZATION_OF_A_SINGLE_OBJECT, filePath));
        } catch (FileNotFoundException ex) {
            logger.error(MessageFormat.format(ServicesConstants.FILE_NOT_FOUND, filePath), ex);
        } catch (IOException ex) {
            logger.error(MessageFormat.format(ServicesConstants.ERROR_DURING_SERIALIZATION, ex.getMessage()), ex);
        }
    }

    /**
     * Serializes a list of objects implementing ISerializable to a file.
     */
    public void serialize(List<T> entities) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            objectOutputStream.writeObject(entities);
            logger.info(MessageFormat.format(ServicesConstants.SUCCESSFUL_SERIALIZATION_OF_A_LIST_OF_OBJECTS, filePath));
        } catch (FileNotFoundException ex) {
            logger.error(MessageFormat.format(ServicesConstants.FILE_NOT_FOUND, filePath), ex);
        } catch (IOException ex) {
            logger.error(MessageFormat.format(ServicesConstants.ERROR_DURING_SERIALIZATION, ex.getMessage()), ex);
        }
    }

    /**
     * Deserializes a single object from the file that implements ISerializable.
     *
     * @return The deserialized object, or {@code null} if an error occurs.
     */
    public T deserializeSingleObject() {
        T entity = null;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            entity = (T) objectInputStream.readObject();
            logger.info(MessageFormat.format(ServicesConstants.SUCCESSFUL_DESERIALIZATION_OF_A_SINGLE_OBJECT, filePath));
        } catch (FileNotFoundException ex) {
            logger.error(MessageFormat.format(ServicesConstants.FILE_NOT_FOUND, filePath), ex);
        } catch (IOException | ClassNotFoundException ex) {
            logger.error(MessageFormat.format(ServicesConstants.ERROR_DURING_DESERIALIZATION, ex.getMessage()), ex);
        }
        return entity;
    }

    /**
     * Deserializes a list of objects that implement ISerializable from the file.
     *
     * @return The deserialized list of objects, or an empty list if an error occurs.
     */
    public List<T> deserialize() {
        List<T> entities = new ArrayList<>();
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            entities = (List<T>) objectInputStream.readObject();
            logger.info(MessageFormat.format(ServicesConstants.SUCCESSFUL_DESERIALIZATION_OF_A_LIST_OF_OBJECTS, filePath));
        } catch (FileNotFoundException ex) {
            logger.error(MessageFormat.format(ServicesConstants.FILE_NOT_FOUND, filePath), ex);
        } catch (IOException | ClassNotFoundException ex) {
            logger.error(MessageFormat.format(ServicesConstants.ERROR_DURING_DESERIALIZATION, ex.getMessage()), ex);
        }
        return entities;
    }
}
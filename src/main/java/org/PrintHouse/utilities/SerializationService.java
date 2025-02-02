package org.PrintHouse.utilities;

import org.PrintHouse.utilities.contracts.ISerializable;
import org.PrintHouse.utilities.globalconstants.ServicesConstants;

import java.io.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides functionality to serialize and deserialize objects that implement the {@link ISerializable} interface.
 *
 * @param <T> The type of object to serialize or deserialize, which must implement {@link ISerializable}.
 */
public class SerializationService<T extends ISerializable>  {

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
     * Serializes a single SerializableEntity object to a file.
     */
    public void serialize(T entity) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(entity);
            System.out.println(MessageFormat.format(ServicesConstants.SUCCESSFUL_SERIALIZATION_OF_A_SINGLE_OBJECT, filePath)); // TODO: change to a logger
        } catch (FileNotFoundException ex) {
            System.out.println(MessageFormat.format(ServicesConstants.FILE_NOT_FOUND, filePath)); // TODO: change to a logger
            ex.printStackTrace();
        } catch (IOException ex) {
            System.err.println(MessageFormat.format(ServicesConstants.ERROR_DURING_SERIALIZATION, ex.getMessage())); // TODO: change to a logger
            ex.printStackTrace();
        }
    }


    /**
     * Serializes a list of SerializableEntity objects to a file.
     */
    public void serialize(List<T> entities) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            objectOutputStream.writeObject(entities);
            System.out.println(MessageFormat.format(ServicesConstants.SUCCESSFUL_SERIALIZATION_OF_A_LIST_OF_OBJECTS, filePath)); // TODO: change to a logger
        } catch (FileNotFoundException ex) {
            System.out.println(MessageFormat.format(ServicesConstants.FILE_NOT_FOUND, filePath)); // TODO: change to a logger
            ex.printStackTrace();
        } catch (IOException ex) {
            System.err.println(MessageFormat.format(ServicesConstants.ERROR_DURING_SERIALIZATION, ex.getMessage())); // TODO: change to a logger
            ex.printStackTrace();
        }
    }

    /**
     * Deserializes a single object from the file.
     *
     * @return The deserialized object, or {@code null} if an error occurs.
     */
    public T deserializeSingleObject() {
        T entity = null;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            entity = (T) objectInputStream.readObject();
            System.out.println(MessageFormat.format(ServicesConstants.SUCCESSFUL_DESERIALIZATION_OF_A_SINGLE_OBJECT, filePath)); // TODO: change to a logger
        } catch (FileNotFoundException ex) {
            System.out.println(MessageFormat.format(ServicesConstants.FILE_NOT_FOUND, filePath)); // TODO: change to a logger
            ex.printStackTrace();
        } catch (IOException ex) {
            System.err.println(MessageFormat.format(ServicesConstants.ERROR_DURING_DESERIALIZATION, ex.getMessage())); // TODO: change to a logger
        } catch (ClassNotFoundException ex) {
            System.err.println(MessageFormat.format(ServicesConstants.ERROR_DURING_DESERIALIZATION, ex.getMessage())); // TODO: change to a logger
        }

        return entity;
    }

    /**
     * Deserializes a list of objects from the file.
     *
     * @return The deserialized list of objects, or an empty list if an error occurs.
     */
    public List<T> deserialize() {
        List<T> entities = new ArrayList<>();

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath))) {
            entities = (List<T>) objectInputStream.readObject();
            System.out.println(MessageFormat.format(ServicesConstants.SUCCESSFUL_DESERIALIZATION_OF_A_LIST_OF_OBJECTS, filePath));
        } catch (FileNotFoundException ex) {
            System.out.println(MessageFormat.format(ServicesConstants.ERROR_DURING_DESERIALIZATION, ex.getMessage())); // TODO: change to a logger
            ex.printStackTrace();
        }
        catch (IOException ex) {
            System.err.println(MessageFormat.format(ServicesConstants.ERROR_DURING_DESERIALIZATION, ex.getMessage())); // TODO: change to a logger
        } catch (ClassNotFoundException ex) {
            System.err.println(MessageFormat.format(ServicesConstants.ERROR_DURING_DESERIALIZATION, ex.getMessage())); // TODO: change to a logger
        }

        return entities;
    }
}

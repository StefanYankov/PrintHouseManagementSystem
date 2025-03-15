package services.contracts;

import data.models.Edition;
import data.models.PrintHouse;
import data.models.Size;

import java.util.List;

/**
 * Defines operations for managing {@link Edition} entities within a {@link PrintHouse}.
 */
public interface IEditionService {
    /**
     * Adds an edition to the specified print house.
     *
     * @param printHouse the print house to which the edition is added
     * @param edition    the edition to add
     */
    void addEdition(PrintHouse printHouse, Edition edition);

    /**
     * Retrieves all editions associated with the specified print house.
     *
     * @param printHouse the print house whose editions are to be retrieved
     * @return a list of editions
     */
    List<Edition> getEditions(PrintHouse printHouse);

    /**
     * Retrieves a specific edition by index from the specified print house.
     *
     * @param printHouse the print house containing the edition
     * @param index      the index of the edition in the print house's edition list
     * @return the edition at the specified index
     */
    Edition getEdition(PrintHouse printHouse, int index);

    /**
     * Updates an existing edition in the specified print house with provided attributes.
     * Only non-null parameters are applied.
     *
     * @param printHouse    the print house containing the edition
     * @param edition       the edition to update
     * @param title         the new title, or null to keep unchanged
     * @param numberOfPages the new page count, or null to keep unchanged
     * @param size          the new size, or null to keep unchanged
     */
    void updateEdition(PrintHouse printHouse, Edition edition, String title, Integer numberOfPages, Size size);

    /**
     * Removes an edition from the specified print house.
     *
     * @param printHouse the print house from which to remove the edition
     * @param edition    the edition to remove
     */
    void removeEdition(PrintHouse printHouse, Edition edition);

    /**
     * Saves all editions associated with a specific PrintHouse to a file.
     *
     * @param printHouse The PrintHouse whose editions should be saved.
     * @param filePath   The base file path (will be appended with PrintHouse index).
     */
    void saveEditions(PrintHouse printHouse, String filePath);

    /**
     * Loads editions for a specific PrintHouse from a file.
     *
     * @param printHouse The PrintHouse whose editions should be loaded.
     * @param filePath   The base file path (will be appended with PrintHouse index).
     */
    void loadEditions(PrintHouse printHouse, String filePath);
}
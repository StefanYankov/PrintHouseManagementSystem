package org.PrintHouse.models.Contracts;

import java.util.Map;

/**
 * Interface representing a printing press with operations for retrieving printed items.
 */
public interface IPrintingPress<P extends Enum<P> & IPaperTypes, T extends Enum<T>> {

    /**
     * Retrieves the printed items along with their counts.
     *
     * @return A map where keys are printed items (implementing IPrintedItem) and values are their counts.
     */
    Map<IPrintedItem<P, T>, Integer> getPrintedItems();

    /**
     * Gets the identifier or name of the printing press.
     *
     * @return A string representing the printing press's identifier.
     */
    String getIdentifier();
}

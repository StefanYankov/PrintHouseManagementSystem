package org.PrintHouse.models.Contracts;

import org.PrintHouse.models.PaperType;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Interface representing a printing press with operations for managing printed items,
 * paper load, and printing configurations.
 *
 * @param <P> The type of paper, represented by an enum.
 * @param <S> The type of edition page size, represented by an enum.
 */
public interface IPrintingPress<P extends Enum<P> , S extends Enum<S>> {

    /**
     * Retrieves the maximum paper load of the printing press.
     *
     * @return The maximum paper capacity.
     */
    public int getMaxPaperLoad();

    /**
     * Retrieves the current paper load of the printing press.
     *
     * @return The current paper load.
     */
    public int getCurrentPaperLoad();

    /**
     * Loads paper into the printing press.
     *
     * @param paperCount The number of paper sheets to load.
     */
    public void loadPaper(int paperCount);

    /**
     * Retrieves whether the printing press supports color printing.
     *
     * @return True if color printing is supported, otherwise false.
     */
    public boolean getIsColor();

    /**
     * Retrieves the maximum pages that can be printed per minute.
     *
     * @return The maximum pages printed per minute.
     */
    public int getMaximumPagesPerMinute();

    /**
     * Prints a specified item a given number of times.
     *
     * @param isColor        A boolean indicating whether the print should be in color (true) or black and white (false).
     * @param editionToPrint The edition to print, implementing {@link IEdition}.
     * @param paperType      The type of paper to use for printing.
     * @param pricePerCopy   The price per copy of the printed item.
     * @param copies         The number of copies to print. Must be greater than zero.
     */
    public void printItems(boolean isColor, IEdition<S> editionToPrint, P paperType, BigDecimal pricePerCopy, int copies);

    /**
     * Prints a specified item a single time.
     *
     * @param isColor      A boolean indicating whether the print should be in color (true) or black and white (false).
     * @param editionToPrint      The edition to print, implementing {@link IEdition}.
     * @param paperType    The type of paper to use for printing.
     * @param pricePerCopy The price per copy of the printed item.
     */
    public void printAnItem(boolean isColor, IEdition<S> editionToPrint, P paperType, BigDecimal pricePerCopy);

    /**
     * Retrieves the map of printed items and their respective counts.
     *
     * @return A map of printed items and their quantities.
     */
    public Map<IPrintedItem<P, S>, Integer> getPrintedItems();

    /**
     * Calculates the total number of pages printed by the press.
     *
     * @return The total number of printed pages.
     */
    public long totalPrintedPages();
}
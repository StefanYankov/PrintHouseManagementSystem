package services.contracts;

import data.models.*;

import java.math.BigDecimal;

/**
 * Defines operations for managing {@link PrintingPress} entities and printing operations within a {@link PrintHouse}.
 */
public interface IPrintingPressService {
    /**
     * Adds a printing press to a print house.
     *
     * @param printHouse    the {@link PrintHouse} to add the press to
     * @param printingPress the {@link PrintingPress} to add
     */
    void addPrintingPress(PrintHouse printHouse, PrintingPress printingPress);

    /**
     * Updates the attributes of an existing {@link PrintingPress} within a {@link PrintHouse}.
     * Only provided (non-null) parameters are updated, allowing for partial modifications.
     *
     * @param printHouse        the {@link PrintHouse} containing the printing press
     * @param printingPress     the {@link PrintingPress} to update
     * @param maxPaperLoad      the new maximum paper load, or null to keep unchanged
     * @param currentPaperLoad  the new current paper load, or null to keep unchanged
     * @param isColour          the new color capability, or null to keep unchanged
     * @param maxPagesPerMinute the new maximum pages per minute, or null to keep unchanged
     */
    void updatePrintingPress(PrintHouse printHouse, PrintingPress printingPress, Integer maxPaperLoad,
                             Integer currentPaperLoad, Boolean isColour, Integer maxPagesPerMinute);

    /**
     * Removes a printing press from a print house.
     *
     * @param printHouse    the {@link PrintHouse} to remove the press from
     * @param printingPress the {@link PrintingPress} to remove
     */
    void removePrintingPress(PrintHouse printHouse, PrintingPress printingPress);

    /**
     * Prints an item using a specified printing press.
     *
     * @param printHouse   the {@link PrintHouse} context
     * @param press        the {@link PrintingPress} to use
     * @param edition      the {@link Edition} to print
     * @param paperType    the {@link PaperType} to use
     * @param pricePerCopy the price per copy
     * @param copies       the number of copies to print
     * @param isColour     whether to print in color
     */
    void printItem(PrintHouse printHouse, PrintingPress press, Edition edition, PaperType paperType,
                   BigDecimal pricePerCopy, int copies, boolean isColour);

    /**
     * Calculates the total cost of all printed items in a print house.
     *
     * @param printHouse the {@link PrintHouse} to calculate costs for
     * @return the total print cost
     */
    BigDecimal getTotalCostForPrint(PrintHouse printHouse);

    /**
     * Calculates the total revenue from all printed items in a print house.
     *
     * @param printHouse the {@link PrintHouse} to calculate revenue for
     * @return the total revenue
     */
    BigDecimal getTotalRevenue(PrintHouse printHouse);

    /**
     * Calculates the paper cost for a specific print job.
     *
     * @param printHouse the {@link PrintHouse} context
     * @param paperType  the {@link PaperType} to use
     * @param size       the {@link Size} of the paper
     * @param pageCount  the number of pages
     * @return the calculated paper cost
     */
    BigDecimal calculatePaperCost(PrintHouse printHouse, PaperType paperType, Size size, int pageCount);

    /**
     * Calculates the total number of pages printed by a specific press.
     *
     * @param printHouse the {@link PrintHouse} context
     * @param press      the {@link PrintingPress} to calculate for
     * @return the total number of pages printed
     */
    long totalPrintedPages(PrintHouse printHouse, PrintingPress press);

    /**
     * Loads paper into the specified printing press.
     *
     * @param printHouse the print house containing the press
     * @param press      the printing press to load paper into
     * @param amount     the amount of paper to load
     */
    public void loadPaper(PrintHouse printHouse, PrintingPress press, int amount);
}
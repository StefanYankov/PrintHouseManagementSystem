package org.PrintHouse.models;

import org.PrintHouse.models.Contracts.IEdition;
import org.PrintHouse.models.Contracts.IPaperTypes;
import org.PrintHouse.models.Contracts.IPrintedItem;
import org.PrintHouse.models.Contracts.IPrintingPress;
import org.PrintHouse.utilities.exceptions.InvalidCopiesCountException;
import org.PrintHouse.utilities.exceptions.InvalidNumberOfPagesException;
import org.PrintHouse.utilities.exceptions.InvalidPaperLoadException;
import org.PrintHouse.utilities.exceptions.UnsupportedPrintColorException;
import org.PrintHouse.utilities.globalconstants.ExceptionMessages;
import org.PrintHouse.utilities.globalconstants.ModelsConstants;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a printing press capable of managing printed items, paper load, and printing operations.
 * <p>
 * This class provides functionality to load paper, manage printed items, and handle printing tasks
 * with specific configurations for paper capacity and color printing capability.
 * </p>
 *
 * @param <P> The type of paper, represented by an enum implementing {@link IPaperTypes}.
 * @param <S> The type of edition size, represented by an enum.
 */
public class PrintingPress<P extends Enum<P> & IPaperTypes, S extends Enum<S>> implements IPrintingPress<P, S>, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private int maxPaperLoad;
    private int currentPaperLoad;
    /**
     * Indicates whether the printing press can print in color. If false
     * it can only print black and white.
     */
    private final boolean isColor;
    private int maximumPagesPerMinute;

    private final Map<IPrintedItem<P, S>, Integer> printedItems;

    /**
     * Constructs a new {@code PrintingPress} instance with the specified configurations.
     *
     * @param maxPaperLoad          The maximum paper capacity of the printing press.
     * @param currentPaperLoad      The initial paper load.
     * @param canColorPrint         Indicates if the printing press supports color printing.
     * @param maximumPagesPerMinute The maximum number of pages the printing press can produce per minute.
     */
    public PrintingPress(int maxPaperLoad, int currentPaperLoad, boolean canColorPrint, int maximumPagesPerMinute) {
        this.setMaxPaperLoad(maxPaperLoad);
        this.setCurrentPaperLoad(currentPaperLoad);
        this.isColor = canColorPrint;
        this.setMaximumPagesPerMinute(maximumPagesPerMinute);
        this.printedItems = new HashMap<IPrintedItem<P, S>, Integer>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaxPaperLoad() {
        return maxPaperLoad;
    }

    protected void setMaxPaperLoad(int maxPaperLoad) {
        if (maxPaperLoad < 0) {
            throw new InvalidPaperLoadException(ExceptionMessages.PAPER_LOAD_CANNOT_BE_ZERO_OR_NEGATIVE_NUMBER);
        }

        if (maxPaperLoad > ModelsConstants.MAXIMUM_PAPER_LOAD) {
            throw new InvalidPaperLoadException(MessageFormat
                    .format(ExceptionMessages.PAPER_LOAD_CANNOT_EXCEED_MAXIMUM_AVAILABLE_LOADING_CAPACITY,
                            ModelsConstants.MAXIMUM_PAPER_LOAD));
        }
        this.maxPaperLoad = maxPaperLoad;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCurrentPaperLoad() {
        return currentPaperLoad;
    }

    /**
     * Sets the current paper load for the printing press.
     * <p>
     * Validates the provided value to ensure it is non-negative and does not exceed
     * the allowable maximum capacity, which is defined by {@link #maxPaperLoad}.
     * </p>
     *
     * @param currentPaperLoad The current paper load to set.
     * @throws InvalidPaperLoadException If the value is negative or exceeds the maximum paper load.
     */
    protected void setCurrentPaperLoad(int currentPaperLoad) {

        if (currentPaperLoad < 0) {
            throw new InvalidPaperLoadException(ExceptionMessages.PAPER_LOAD_CANNOT_BE_ZERO_OR_NEGATIVE_NUMBER);
        }

        if (currentPaperLoad > maxPaperLoad) {
            throw new InvalidPaperLoadException(MessageFormat
                    .format(ExceptionMessages.PAPER_LOAD_CANNOT_BE_ABOVE_THE_MAXIMUM_CAPACITY, this.maxPaperLoad));
        }

        this.currentPaperLoad = currentPaperLoad;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getIsColor() {
        return isColor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMaximumPagesPerMinute() {
        return maximumPagesPerMinute;
    }

    /**
     * Sets the maximum number of pages the printing press can produce per minute.
     * <p>
     * Validates the provided value to ensure it is non-negative and does not exceed
     * the allowable limit defined in {@link ModelsConstants#MAXIMUM_PAGES_PER_MINUTE}.
     * </p>
     *
     * @param maximumPagesPerMinute The maximum pages per minute to set.
     * @throws InvalidNumberOfPagesException If the value is negative or exceeds the maximum allowed.
     */
    private void setMaximumPagesPerMinute(int maximumPagesPerMinute) {
        if (maximumPagesPerMinute < 0) {
            throw new InvalidNumberOfPagesException(ExceptionMessages.MAX_PAGES_PER_MINUTE_PRINTED_CANNOT_BE_A_NEGATIVE_VALUE);
        }

        if (maximumPagesPerMinute > ModelsConstants.MAXIMUM_PAGES_PER_MINUTE) {
            throw new InvalidNumberOfPagesException(MessageFormat
                    .format(ExceptionMessages.MAX_PAGES_PER_MINUTE_PRINTED_CANNOT_EXCEED_MAXIMUM_ALLOWED, ModelsConstants.MAXIMUM_PAGES_PER_MINUTE));
        }

        this.maximumPagesPerMinute = maximumPagesPerMinute;
    }

    /**
     * Gets the map of printed items and their respective counts.
     *
     * @return A map of printed items and the number of copies printed.
     */
    public Map<IPrintedItem<P, S>, Integer> getPrintedItems() {
        return printedItems;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void loadPaper(int paperCount) {
        if (this.getCurrentPaperLoad() == this.getMaxPaperLoad()) {
            throw new InvalidPaperLoadException(MessageFormat
                    .format(ExceptionMessages.PAPER_LOAD_CANNOT_BE_ABOVE_THE_MAXIMUM_CAPACITY, this.maxPaperLoad));
        }

        if ((this.getCurrentPaperLoad() + paperCount) > this.getMaxPaperLoad()) {
            this.setCurrentPaperLoad(this.getMaxPaperLoad());
        } else {
            this.setCurrentPaperLoad(this.getCurrentPaperLoad() + paperCount);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printItems(boolean isColor, IEdition<S> editionToPrint, P paperType, BigDecimal pricePerCopy, int copies) {
        if (isColor != this.getIsColor()) {
            throw new UnsupportedPrintColorException(MessageFormat
                    .format(ExceptionMessages.INCOMPATIBLE_COLOR_TYPE, this.getIsColor(), isColor));
        }

        if (copies <= 0) {
            throw new InvalidCopiesCountException(ExceptionMessages.COPIES_COUNT_NEED_TO_BE_GREATER_THAN_ZERO);
        }

        // two pages per piece of paper
        int pagesToPrint = (int) Math.ceil(editionToPrint.getNumberOfPages() / 2.0);

        if (pagesToPrint > this.getCurrentPaperLoad()) {
            throw new InvalidPaperLoadException(MessageFormat
                    .format(ExceptionMessages.INSUFFICIENT_PAPER_LOAD, pagesToPrint, this.currentPaperLoad));
        }

        // TODO: create a factory class or a service to not make the print items that tightly coupled to PrintingPress
        IPrintedItem<P, S> printedItem = new PrintedItem<>(editionToPrint, paperType, pricePerCopy, isColor);

        this.addPrintedItem(printedItem, copies);
        this.setCurrentPaperLoad(this.getCurrentPaperLoad() - pagesToPrint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printAnItem(boolean isColor, IEdition<S> edition, P paperType, BigDecimal pricePerCopy) {
        this.printItems(isColor, edition, paperType, pricePerCopy, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long totalPrintedPages() {
        return this.getPrintedItems()
                .entrySet()
                .stream()
                .mapToLong(x -> (long) x.getKey().getEdition().getNumberOfPages() * x.getValue())
                .sum();
    }

    private void addPrintedItem(IPrintedItem<P, S> printedItem, int countOfCopies) {

        if (!this.printedItems.containsKey(printedItem)) {
            this.printedItems.put(printedItem, countOfCopies);
        } else {
            this.printedItems.put(printedItem, printedItems.get(printedItem) + countOfCopies);
        }

    }
}
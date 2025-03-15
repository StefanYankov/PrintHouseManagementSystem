package services;

import data.models.*;
import services.contracts.IPrintingPressService;
import utilities.exceptions.*;
import utilities.globalconstants.ExceptionMessages;
import utilities.globalconstants.ModelsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;

/**
 * Manages {@link PrintingPress} entities and printing operations within a {@link PrintHouse}.
 */
public class PrintingPressService implements IPrintingPressService {
    private static final Logger logger = LoggerFactory.getLogger(PrintingPressService.class);

    /** {@inheritDoc} */
    @Override
    public void addPrintingPress(PrintHouse printHouse, PrintingPress printingPress) {
        validatePrintHouse(printHouse);
        validatePrintingPress(printingPress);
        validatePressParameters(printingPress);
        printHouse.getPrintingPresses().add(printingPress);
        logger.info("Added printing press to PrintHouse {}: {}", printHouse, printingPress);
    }

    /** {@inheritDoc} */
    @Override
    public void updatePrintingPress(PrintHouse printHouse, PrintingPress printingPress, Integer maxPaperLoad,
                                    Integer currentPaperLoad, Boolean isColour, Integer maxPagesPerMinute) {
        validatePrintHouse(printHouse);
        validatePrintingPress(printingPress);
        if (!printHouse.getPrintingPresses().contains(printingPress)) {
            logger.error(ExceptionMessages.PRINTING_PRESS_IS_NOT_PART_OF_THIS_PRINTING_HOUSE);
            throw new InvalidPrintingPressException(ExceptionMessages.PRINTING_PRESS_IS_NOT_PART_OF_THIS_PRINTING_HOUSE);
        }
        if (maxPaperLoad != null) {
            if (maxPaperLoad <= 0 || maxPaperLoad > ModelsConstants.MAXIMUM_PAPER_LOAD) {
                logger.error("Invalid max paper load: {}", maxPaperLoad);
                throw new InvalidPaperLoadException(ExceptionMessages.PAPER_LOAD_CANNOT_BE_ZERO_OR_NEGATIVE_NUMBER);
            }
            printingPress.setMaxPaperLoad(maxPaperLoad);
        }
        if (currentPaperLoad != null) {
            if (currentPaperLoad < 0 || currentPaperLoad > printingPress.getMaxPaperLoad()) {
                logger.error("Invalid current paper load: {}", currentPaperLoad);
                throw new InvalidPaperLoadException(MessageFormat.format(ExceptionMessages.PAPER_LOAD_CANNOT_BE_ABOVE_THE_MAXIMUM_CAPACITY, printingPress.getMaxPaperLoad()));
            }
            printingPress.setCurrentPaperLoad(currentPaperLoad);
        }
        if (isColour != null) printingPress.setColour(isColour);
        if (maxPagesPerMinute != null) {
            if (maxPagesPerMinute <= 0 || maxPagesPerMinute > ModelsConstants.MAXIMUM_PAGES_PER_MINUTE) {
                logger.error("Invalid max pages per minute: {}", maxPagesPerMinute);
                throw new InvalidPrintingPressException(ExceptionMessages.MAX_PAGES_PER_MINUTE_PRINTED_CANNOT_BE_A_NEGATIVE_VALUE);
            }
            printingPress.setMaximumPagesPerMinute(maxPagesPerMinute);
        }
        logger.info("Printing press updated: {}", printingPress);
    }

    /** {@inheritDoc} */
    @Override
    public void removePrintingPress(PrintHouse printHouse, PrintingPress printingPress) {
        validatePrintHouse(printHouse);
        validatePrintingPress(printingPress);
        if (!printHouse.getPrintingPresses().remove(printingPress)) {
            logger.warn("Printing press not found: {}", printingPress);
        } else {
            logger.info("Printing press removed: {}", printingPress);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void printItem(PrintHouse printHouse, PrintingPress press, Edition edition, PaperType paperType,
                          BigDecimal pricePerCopy, int copies, boolean isColour) {
        validatePrintHouse(printHouse);
        validatePrintingPress(press);
        validatePressParameters(press);
        validatePrintParameters(edition, paperType, pricePerCopy, copies, isColour, press);
        int pagesNeeded = (int) Math.ceil(edition.getNumberOfPages() / 2.0) * copies;
        if (pagesNeeded > press.getCurrentPaperLoad()) {
            logger.error("Insufficient paper: needed={}, available={}", pagesNeeded, press.getCurrentPaperLoad());
            throw new InvalidPaperLoadException(MessageFormat
                    .format(ExceptionMessages.INSUFFICIENT_PAPER_LOAD, pagesNeeded, press.getCurrentPaperLoad()));
        }

        PrintedItem item = new PrintedItem(edition, paperType, pricePerCopy, isColour);
        press.getPrintedItems().merge(item, copies, Integer::sum);
        press.setCurrentPaperLoad(press.getCurrentPaperLoad() - pagesNeeded);
        logger.info("Printed {} copies of {} using {}", copies, item, press);
    }

    /** {@inheritDoc} */
    @Override
    public BigDecimal getTotalCostForPrint(PrintHouse printHouse) {
        validatePrintHouse(printHouse);
        BigDecimal total = BigDecimal.ZERO;
        for (PrintingPress press : printHouse.getPrintingPresses()) {
            for (var entry : press.getPrintedItems().entrySet()) {
                PrintedItem item = entry.getKey();
                int copies = entry.getValue();
                BigDecimal cost = calculatePaperCost(printHouse, item.getPaperType(), item.getEdition().getSize(), item.getEdition().getNumberOfPages());
                total = total.add(cost.multiply(BigDecimal.valueOf(copies)));
            }
        }
        logger.info("Total print cost: {}", total);
        return total;
    }

    /** {@inheritDoc} */
    @Override
    public BigDecimal getTotalRevenue(PrintHouse printHouse) {
        validatePrintHouse(printHouse);
        BigDecimal total = BigDecimal.ZERO;
        for (PrintingPress press : printHouse.getPrintingPresses()) {
            for (var entry : press.getPrintedItems().entrySet()) {
                PrintedItem item = entry.getKey();
                int copies = entry.getValue();
                BigDecimal price = item.getPrice();
                if (copies > printHouse.getSalesDiscountCount()) {
                    BigDecimal discount = printHouse.getSalesDiscountPercentage().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    price = price.multiply(BigDecimal.ONE.subtract(discount));
                }
                total = total.add(price.multiply(BigDecimal.valueOf(copies)));
            }
        }
        logger.info("Total revenue: {}", total);
        return total;
    }

    /** {@inheritDoc} */
    @Override
    public BigDecimal calculatePaperCost(PrintHouse printHouse, PaperType paperType, Size size, int pageCount) {
        validatePrintHouse(printHouse);
        if (paperType == null) {
            logger.error(ExceptionMessages.PAPER_TYPE_CANNOT_BE_NULL);
            throw new InvalidPaperTypeException(ExceptionMessages.PAPER_TYPE_CANNOT_BE_NULL);
        }
        if (pageCount <= 0) {
            logger.error(ExceptionMessages.NUMBER_OF_PAGES_MUST_BE_GREATER_THAN_ZERO);
            throw new InvalidNumberOfPagesException(ExceptionMessages.NUMBER_OF_PAGES_MUST_BE_GREATER_THAN_ZERO);
        }
        BigDecimal cost = paperType.getCost();
        int sizeIndex = size.ordinal();
        for (int i = 0; i < sizeIndex; i++) {
            BigDecimal increment = printHouse.getPaperIncrementPercentage()
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            cost = cost.multiply(BigDecimal.ONE.add(increment));
        }
        cost = cost.multiply(BigDecimal.valueOf(pageCount));
        logger.debug("Paper cost for {} pages of {} {}: {}", pageCount, paperType, size, cost);
        return cost;
    }

    /** {@inheritDoc} */
    @Override
    public long totalPrintedPages(PrintHouse printHouse, PrintingPress press) {
        validatePrintHouse(printHouse);
        validatePrintingPress(press);
        long total = press.getPrintedItems().entrySet().stream()
                .mapToLong(entry -> (long) entry.getKey().getEdition().getNumberOfPages() * entry.getValue())
                .sum();
        logger.info("Total printed pages: {}", total);
        return total;
    }

    /** {@inheritDoc} */
    @Override
    public void loadPaper(PrintHouse printHouse, PrintingPress press, int amount) {
        validatePrintHouse(printHouse);
        validatePrintingPress(press);
        if (amount <= 0) {
            logger.error(ExceptionMessages.PAPER_LOAD_CANNOT_BE_ZERO_OR_NEGATIVE_NUMBER);
            throw new InvalidPaperLoadException(ExceptionMessages.PAPER_LOAD_CANNOT_BE_ZERO_OR_NEGATIVE_NUMBER);
        }

        int newLoad = press.getCurrentPaperLoad() + amount;
        if (newLoad > press.getMaxPaperLoad()) {
            logger.error("Paper load exceeds max capacity: {}", newLoad);
            throw new InvalidPaperLoadException(MessageFormat.format(ExceptionMessages.PAPER_LOAD_CANNOT_BE_ABOVE_THE_MAXIMUM_CAPACITY, press.getMaxPaperLoad()));
        }

        press.setCurrentPaperLoad(newLoad);
        logger.info("Loaded {} paper, new load: {}", amount, newLoad);
    }

    private void validatePrintHouse(PrintHouse printHouse) {
        if (printHouse == null) {
            logger.error(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL);
            throw new InvalidPrintHouseException(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL);
        }
    }

    private void validatePrintingPress(PrintingPress press) {
        if (press == null) {
            logger.error(ExceptionMessages.PRINTING_PRESS_CANNOT_BE_NULL);
            throw new InvalidPrintingPressException(ExceptionMessages.PRINTING_PRESS_CANNOT_BE_NULL);
        }
    }

    private void validatePressParameters(PrintingPress press) {
        if (press.getMaxPaperLoad() <= 0 || press.getMaxPaperLoad() > ModelsConstants.MAXIMUM_PAPER_LOAD) {
            logger.error("Invalid max paper load: {}", press.getMaxPaperLoad());
            throw new InvalidPaperLoadException(ExceptionMessages.PAPER_LOAD_CANNOT_BE_ZERO_OR_NEGATIVE_NUMBER);
        }
        if (press.getCurrentPaperLoad() < 0 || press.getCurrentPaperLoad() > press.getMaxPaperLoad()) {
            logger.error("Invalid current paper load: {}", press.getCurrentPaperLoad());
            throw new InvalidPaperLoadException(ExceptionMessages.PAPER_LOAD_CANNOT_BE_ABOVE_THE_MAXIMUM_CAPACITY);
        }
        if (press.getMaximumPagesPerMinute() <= 0 || press.getMaximumPagesPerMinute() > ModelsConstants.MAXIMUM_PAGES_PER_MINUTE) {
            logger.error("Invalid max pages per minute: {}", press.getMaximumPagesPerMinute());
            throw new InvalidPrintingPressException(ExceptionMessages.MAX_PAGES_PER_MINUTE_PRINTED_CANNOT_BE_A_NEGATIVE_VALUE);
        }
    }

    private void validatePrintParameters(Edition edition, PaperType paperType, BigDecimal pricePerCopy, int copies, boolean isColour, PrintingPress press) {
        if (edition == null) {
            logger.error(ExceptionMessages.EDITION_CANNOT_BE_NULL);
            throw new InvalidEditionException(ExceptionMessages.EDITION_CANNOT_BE_NULL);
        }
        if (paperType == null) {
            logger.error(ExceptionMessages.PAPER_TYPE_CANNOT_BE_NULL);
            throw new InvalidPaperTypeException(ExceptionMessages.PAPER_TYPE_CANNOT_BE_NULL);
        }
        if (pricePerCopy == null || pricePerCopy.compareTo(BigDecimal.ZERO) <= 0 || pricePerCopy.compareTo(ModelsConstants.MAXIMUM_PRICE) > 0) {
            logger.error("Invalid price: {}", pricePerCopy);
            throw new InvalidPriceException(ExceptionMessages.PRICE_CANNOT_BE_NULL);
        }
        if (copies <= 0) {
            logger.error("Invalid copies: {}", copies);
            throw new InvalidCopiesCountException(ExceptionMessages.COPIES_COUNT_CANNOT_BE_A_NEGATIVE_NUMBER);
        }

        if (isColour && !press.isColour()) {
            logger.error("Color mismatch: press={}, requested={}", press.isColour(), isColour);
            throw new UnsupportedPrintColorException(MessageFormat.format(ExceptionMessages.INCOMPATIBLE_COLOR_TYPE, press.isColour(), isColour));
        }
    }
}
package org.PrintHouse.models;

import org.PrintHouse.utilities.globalconstants.ExceptionMessages;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

/**
 * A utility class to calculate and manage costs for different page sizes defined in an Enum.
 *
 * @param <T> The type of Enum representing the page sizes. Sizes should be sorted, with the smallest size always first.
 */
public class PageSize<T extends Enum<T>> {

    // create a map variable to hold the KvP for sizes and their respective $$$
    private final Map<T, BigDecimal> pageSizesCost;
    // increment percentage same for all sizes above the first one
    private static BigDecimal incrementPercentage;
    private BigDecimal baseCost;

    /**
     * Constructs a new {@code PageSize} instance.
     *
     * @param enumType The class type of the Enum representing page sizes.
     * @param baseCost The base cost for the smallest page size.
     * @throws IllegalArgumentException If {@code baseCost} is null or less than or equal to zero.
     */
    public PageSize(Class<T> enumType, BigDecimal baseCost) {
        this.baseCost = baseCost;
        pageSizesCost = new EnumMap<>(enumType);

        // Initialize the map with null values for all enum constants
        for (T size : enumType.getEnumConstants()) {
            pageSizesCost.put(size, null);
        }

        setBaseCost(baseCost);
    }

    /**
     * Sets the percentage increment for each size above the base size.
     *
     * @param incrementalPercentage The percentage increment as a {@code BigDecimal}.
     * @throws IllegalArgumentException If the percentage is null, less than or equal to zero.
     */

    // option to set the increment % it.
    public static void setIncrementalPercentage(BigDecimal incrementalPercentage) {
        if (incrementalPercentage == null || incrementalPercentage.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_PAGE_SIZE_INCREMENTAL_PERCENTAGE);
        }
        // Convert percentage to decimal if it is greater than 1
        if (incrementalPercentage.compareTo(BigDecimal.ONE) > 0) {
            incrementalPercentage = incrementalPercentage.divide(BigDecimal.valueOf(100));
        }
        PageSize.incrementPercentage = incrementalPercentage;
    }

    /**
     * Sets the base cost for the smallest size and calculates costs for other sizes.
     *
     * @param baseCost The base cost for the smallest size as a {@code BigDecimal}.
     * @throws IllegalArgumentException If {@code baseCost} is null or less than or equal to zero.
     */
    public void setBaseCost(BigDecimal baseCost) {
        if (baseCost == null || baseCost.compareTo(BigDecimal.ZERO) <= 0) {
            // TODO: custom error handling
            throw new IllegalArgumentException("Base cost must be greater than zero");
        }
        calculateCosts(baseCost);
    }

    /**
     * Retrieves the cost of a specific page size.
     *
     * @param size The Enum constant representing the page size.
     * @return The cost of the specified page size.
     * @throws IllegalArgumentException If the size is not present in the map.
     */
    public BigDecimal getCost(T size) {
        if (!pageSizesCost.containsKey(size)) {
            // TODO: custom error handling
            throw new IllegalArgumentException("Size " + size + " does not have a defined cost");
        }
        return pageSizesCost.get(size);
    }

    /**
     * Retrieves all page sizes and their respective costs as an unmodifiable map.
     *
     * @return A copy of the size-to-cost map.
     */
    public Map<T, BigDecimal> getPageSizesCost() {
        return new EnumMap<>(pageSizesCost);
    }

    public BigDecimal getBaseCost() {
        return this.baseCost;
    }

    @Override
    public String toString() {
        return "PageSize{" +
                "pageSizesCost=" + pageSizesCost +
                '}';
    }

    /**
     * Calculates the cost for each size, starting from the smallest, based on the base cost and
     * the incremental percentage.
     *
     * @param baseCost The base cost for the smallest size.
     * @throws IllegalStateException If the map of page sizes is empty.
     */
    private void calculateCosts(BigDecimal baseCost) {
        if (pageSizesCost.isEmpty()) {
            // TODO: custom error handling
            throw new IllegalStateException("The pageSizesCost map is empty. Add sizes before setting costs.");
        }

        BigDecimal currentCost = baseCost;

        for (T size : pageSizesCost.keySet()) {
            pageSizesCost.put(size, currentCost);
            currentCost = currentCost.multiply(BigDecimal.ONE.add(incrementPercentage));
        }
    }
}

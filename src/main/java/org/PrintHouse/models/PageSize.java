package org.PrintHouse.models;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

public class PageSize {

    // create a map variable to hold the KvP for sizes and their respective $$$
    private final Map<Sizes, BigDecimal> pageSizesCost;
    // increment percentage same for all sizes above the first one
    private static BigDecimal incrementPercentage;

    // option to set the increment % it.
    public static void setIncrementalPercentage(BigDecimal incrementalPercentage) {
        PageSize.incrementPercentage = incrementalPercentage;
    }

    PageSize(BigDecimal baseCost) {
        pageSizesCost = new EnumMap<>(Sizes.class);
    }

    // Set the base cost and calculate costs for all sizes starting from the smallest size
    public void setBaseCost(BigDecimal baseCost) {
        if (baseCost == null || baseCost.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Base cost must be greater than zero");
        }

        // Determine the smallest size (first in the enum order)
        Sizes smallestSize = Sizes.values()[0];
        pageSizesCost.put(smallestSize, baseCost);

        // Calculate costs for larger sizes
        calculateCosts(smallestSize, baseCost);
    }

    // Calculate costs for all sizes relative to the base size
    private void calculateCosts(Sizes baseSize, BigDecimal baseCost) {
        // Forward calculation (larger sizes)
        BigDecimal previousCost = baseCost;
        for (Sizes size : Sizes.values()) {
            if (size.ordinal() > baseSize.ordinal()) {
                previousCost = previousCost.multiply(BigDecimal.ONE.add(incrementPercentage));
                pageSizesCost.put(size, previousCost);
            }
        }

        // Backward calculation (smaller sizes, if added later)
        previousCost = baseCost;
        for (int i = baseSize.ordinal() - 1; i >= 0; i--) {
            Sizes size = Sizes.values()[i];
            previousCost = previousCost.divide(BigDecimal.ONE.add(incrementPercentage), BigDecimal.ROUND_HALF_UP);
            pageSizesCost.put(size, previousCost);
        }
    }

    // Get the cost of a specific size
    public BigDecimal getCost(Sizes size) {
        if (!pageSizesCost.containsKey(size)) {
            throw new IllegalArgumentException("Size " + size + " does not have a defined cost");
        }
        return pageSizesCost.get(size);
    }

    // Get all size-to-cost mappings
    public Map<Sizes, BigDecimal> getPageSizesCost() {
        return new EnumMap<>(pageSizesCost);
    }
}

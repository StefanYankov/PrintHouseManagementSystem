package org.PrintHouse.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PageSizeTest {
    private PageSize<Sizes> pageSize;
    private PageSize<RelativeSizes> relativeSizesPageSize;
    private final BigDecimal baseCost = BigDecimal.valueOf(1000);
    private final BigDecimal increment = BigDecimal.valueOf(10);

    @BeforeEach
    public void setUp() {
        PageSize.setIncrementalPercentage(increment);
        pageSize = new PageSize<>(Sizes.class, baseCost);
        relativeSizesPageSize = new PageSize<>(RelativeSizes.class, baseCost);

    }

    @Test
    public void testConstructorWithStandardSizesEnum() {

        // Verify base cost
        assertTrue(pageSize.getCost(Sizes.A5).compareTo(BigDecimal.valueOf(1000)) == 0, "Base cost for A5 should be 1000");

        // Verify incremented costs
        assertTrue(pageSize.getCost(Sizes.A4).compareTo(BigDecimal.valueOf(1100.00)) == 0, "Cost for A4 should be 1100");
        assertTrue(pageSize.getCost(Sizes.A3).compareTo(BigDecimal.valueOf(1210.00)) == 0, "Cost for A3 should be 1210");
        assertTrue(pageSize.getCost(Sizes.A2).compareTo(BigDecimal.valueOf(1331.00)) == 0, "Cost for A2 should be 1331");
        assertTrue(pageSize.getCost(Sizes.A1).compareTo(BigDecimal.valueOf(1464.10)) == 0, "Cost for A1 should be 1464.10");

    }

    @Test
    public void testConstructorWithOtherSizesEnum() {
        // Verify base cost
        assertTrue(relativeSizesPageSize.getCost(RelativeSizes.SMALL).compareTo(BigDecimal.valueOf(1000)) == 0, "Base cost for Small should be 1000");

        // Verify incremented costs
        assertTrue(relativeSizesPageSize.getCost(RelativeSizes.MEDIUM).compareTo(BigDecimal.valueOf(1100.00)) == 0, "Cost for Medium should be 1100");
    }

    @Test
    public void testSetIncrementalPercentage() {
        // Set a new incremental percentage of 20%
        PageSize.setIncrementalPercentage(BigDecimal.valueOf(20));

        // Re-initialize PageSize with the same base cost to apply the new increment
        pageSize = new PageSize<>(Sizes.class, BigDecimal.valueOf(1000));

        // Verify base cost
        assertTrue(pageSize.getCost(Sizes.A5).compareTo(BigDecimal.valueOf(1000)) == 0, "Base cost for A5 should be 1000");

        // Verify incremented costs with 20%
        assertTrue(pageSize.getCost(Sizes.A4).compareTo(BigDecimal.valueOf(1200.00)) == 0, "Cost for A4 should be 1100");
        assertTrue(pageSize.getCost(Sizes.A3).compareTo(BigDecimal.valueOf(1440.00)) == 0, "Cost for A3 should be 1210");
        assertTrue(pageSize.getCost(Sizes.A2).compareTo(BigDecimal.valueOf(1728.00)) == 0, "Cost for A2 should be 1331");
        assertTrue(pageSize.getCost(Sizes.A1).compareTo(BigDecimal.valueOf(2073.60)) == 0, "Cost for A1 should be 1464.10");
    }

    @Test
    public void testInvalidIncrementalPercentageThrowsError(){
        // Test null percentage
        Exception exception1 = assertThrows(IllegalArgumentException.class, () ->
                PageSize.setIncrementalPercentage(null));
        assertEquals("Incremental percentage must be greater than zero", exception1.getMessage());

        // Test zero percentage
        Exception exception2 = assertThrows(IllegalArgumentException.class, () ->
                PageSize.setIncrementalPercentage(BigDecimal.ZERO));
        assertEquals("Incremental percentage must be greater than zero", exception2.getMessage());

        // Test negative percentage
        Exception exception3 = assertThrows(IllegalArgumentException.class, () ->
                PageSize.setIncrementalPercentage(BigDecimal.valueOf(-5)));
        assertEquals("Incremental percentage must be greater than zero", exception3.getMessage());

    }

    @Test
    public void testInvalidBaseCostThrowsError() {
        // Test null base cost
        Exception exception1 = assertThrows(IllegalArgumentException.class, () ->
                new PageSize<>(Sizes.class, null));
        assertEquals("Base cost must be greater than zero", exception1.getMessage());

        // Test zero base cost
        Exception exception2 = assertThrows(IllegalArgumentException.class, () ->
                new PageSize<>(Sizes.class, BigDecimal.ZERO));
        assertEquals("Base cost must be greater than zero", exception2.getMessage());

        // Test negative base cost
        Exception exception3 = assertThrows(IllegalArgumentException.class, () ->
                new PageSize<>(Sizes.class, BigDecimal.valueOf(-100)));
        assertEquals("Base cost must be greater than zero", exception3.getMessage());
    }
}

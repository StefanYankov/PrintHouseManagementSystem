package org.PrintHouse.models;


import org.PrintHouse.models.Contracts.IPaperTypes;

import java.math.BigDecimal;

/**
 * Represents different types of paper used in the printing process.
 * This enum implements the {@link IPaperTypes} interface and provides the cost for each paper type.
 */
public enum PaperType implements IPaperTypes {
    STANDARD(BigDecimal.valueOf(100)),
    GLOSSY(BigDecimal.valueOf(120)),
    NEWSPAPER(BigDecimal.valueOf(80));

    private final BigDecimal cost;

    PaperType(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * {@inheritDoc}
     */
    public BigDecimal getCost() {
        return this.cost;
    }
}

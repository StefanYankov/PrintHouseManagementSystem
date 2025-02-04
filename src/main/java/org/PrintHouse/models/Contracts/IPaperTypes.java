package org.PrintHouse.models.Contracts;

import java.math.BigDecimal;

/**
 * Represents the contract for paper types.
 * Any class or enum implementing this interface should define the cost of the paper type.
 */
public interface IPaperTypes {

    /**
     * Gets the cost of the paper type.
     *
     * @return The cost of the paper type as a {@link BigDecimal}.
     */
    public BigDecimal getCost();
}

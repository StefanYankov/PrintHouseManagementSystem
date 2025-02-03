package org.PrintHouse.models.Contracts;

import java.math.BigDecimal;

/**
 * Interface representing a printed item, with information about its edition
 * and the type of paper it uses.
 *
 * @param <P> The enum type representing the paper type.
 * @param <S> The enum type representing the size of the Edition.
 */
public interface IPrintedItem<P extends Enum<P>, S extends Enum<S>> {

    /**
     * Gets the edition associated with this printed item.
     *
     * @return The edition as an instance of IEdition.
     */
    public IEdition<S> getEdition();

    /**
     * Gets the paper type used for this printed item.
     *
     * @return The paper type as an enum of type P.
     */
    public P getPaperType();

    /**
     * Gets the price of the printed item.
     *
     * @return The price as a BigDecimal.
     */
    public BigDecimal getPrice();

    /**
     * Gets the color of the printed item.
     * @return True if the item is coloured and false if it is not.
     */
    public boolean isColour();
}
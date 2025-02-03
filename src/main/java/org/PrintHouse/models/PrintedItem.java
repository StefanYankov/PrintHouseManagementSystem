package org.PrintHouse.models;

import org.PrintHouse.models.Contracts.*;
import org.PrintHouse.utilities.exceptions.InvalidEditionException;
import org.PrintHouse.utilities.exceptions.InvalidPaperTypeException;
import org.PrintHouse.utilities.exceptions.InvalidPriceException;
import org.PrintHouse.utilities.globalconstants.ExceptionMessages;
import org.PrintHouse.utilities.globalconstants.ModelsConstants;

import java.math.BigDecimal;
import java.text.MessageFormat;

/**
 * Represents a printed item with details about its edition, paper type, and price.
 *
 * @param <P> The enum type representing the paper type of the printed item.
 * @param <S> The enum type representing the size of the Edition.
 */
public class PrintedItem<P extends Enum<P> & IPaperTypes, S extends Enum<S>> implements IPrintedItem<P, S> {

    /**
     * The edition associated with this printed item.
     */
    private IEdition<S> edition;

    /**
     * The paper type used for this printed item.
     */
    private P paperType;

    /**
     * The price of this printed item.
     */
    private BigDecimal price;

    /**
     * The color of the printed item
     */
    private final boolean isColour;

    /**
     * Constructs a new PrintedItem with the specified edition, paper type, and price.
     *
     * @param edition   The edition of the printed item, implementing {@link IEdition}.
     * @param paperType The paper type of the printed item.
     * @param price     The price of the printed item as a {@link BigDecimal}.
     * @param isColour  The colour of the printed item, where true is coloured
     * @throws InvalidPaperTypeException If the paper type is null.
     * @throws InvalidPriceException     If the price is null or less than zero.
     */
    public PrintedItem(IEdition<S> edition, P paperType, BigDecimal price, boolean isColour) {
        this.setEdition(edition);
        this.setPaperType(paperType);
        this.setPrice(price);
        this.isColour = isColour;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IEdition<S> getEdition() {
        return edition;
    }

    /**
     * Sets the edition for this printed Item
     * <p>
     * This method validates that the edition is not {@code null}.
     * If the edition is {@code null}, an {@link InvalidEditionException} is thrown with a message defined in {@link ExceptionMessages#EDITION_CANNOT_BE_NULL}.
     * </p>
     *
     * @param edition The edition to set for the printed item. Cannot be {@code null}.
     * @throws InvalidEditionException If the provided edition is {@code null}.
     */
    public void setEdition(IEdition<S> edition) {
        if (edition == null) {
            throw new InvalidEditionException(ExceptionMessages.EDITION_CANNOT_BE_NULL);
        }
        this.edition = edition;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public P getPaperType() {
        return paperType;
    }

    /**
     * Sets the paper type for this printed item.
     * <p>
     * This method validates that the paper type is not {@code null}.
     * If the paper typeis {@code null}, an {@link InvalidPaperTypeException} is thrown with a message defined in {@link ExceptionMessages#PAPER_TYPE_CANNOT_BE_NULL}.
     *
     * @param paperType The paper type to set for the printed item.
     * @throws InvalidPaperTypeException If the paper type is null.
     */
    public void setPaperType(P paperType) {
        if (paperType == null) {
            throw new InvalidPaperTypeException(ExceptionMessages.PAPER_TYPE_CANNOT_BE_NULL);
        }
        this.paperType = paperType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets the price for this printed item.
     * <p>
     * This method validates that the price is not {@code null}, not less than zero or does not exceed the maximum allowed price.
     * If the price is {@code null}, an {@link InvalidPriceException} is thrown with a message defined in {@link ExceptionMessages#PRICE_CANNOT_BE_NULL}.
     * If the price is less than zero, an {@link InvalidPriceException} is thrown with a message defined in {@link ExceptionMessages#PRICE_CANNOT_BE_A_NEGATIVE_NUMBER}.
     * If the price exceeds the maximum allowed value of {@link ModelsConstants#MAXIMUM_PRICE}, an {@link InvalidPriceException} is thrown with a message defined in {@link ExceptionMessages#PRICE_CANNOT_BE_GREATER_THAN_THE_MAXIMUM_PRICE}.
     * </p>
     *
     * @param price The price to set for the printed item. Must be greater than or equal to zero, cannot be {@code null}, and cannot exceed the maximum allowable value.
     * @throws InvalidPriceException If the price is {@code null}, less than zero, or greater than {@link ModelsConstants#MAXIMUM_PRICE}.
     */
    public void setPrice(BigDecimal price) {
        if (price == null) {
            throw new InvalidPriceException(ExceptionMessages.PRICE_CANNOT_BE_NULL);
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPriceException(ExceptionMessages.PRICE_CANNOT_BE_A_NEGATIVE_NUMBER);
        }

        if (price.compareTo(ModelsConstants.MAXIMUM_PRICE) > 0) {
            throw new InvalidPriceException(MessageFormat.format(
                    ExceptionMessages.PRICE_CANNOT_BE_GREATER_THAN_THE_MAXIMUM_PRICE, ModelsConstants.MAXIMUM_PRICE));
        }

        this.price = price;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isColour() {
        return isColour;
    }

    @Override
    public String toString() {
        return "PrintedItem{" +
                "edition=" + edition +
                ", paperType=" + paperType +
                ", price=" + price +
                ", isColor=" + isColour +
                '}';
    }
}

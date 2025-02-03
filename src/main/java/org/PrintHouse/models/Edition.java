package org.PrintHouse.models;

import org.PrintHouse.models.Contracts.IEdition;
import org.PrintHouse.utilities.contracts.ISerializable;
import org.PrintHouse.utilities.exceptions.InvalidNumberOfPagesException;
import org.PrintHouse.utilities.exceptions.InvalidPageSizeException;
import org.PrintHouse.utilities.exceptions.InvalidTitleException;
import org.PrintHouse.utilities.globalconstants.ExceptionMessages;
import org.PrintHouse.utilities.globalconstants.ModelsConstants;

import java.text.MessageFormat;

/**
 * Represents an Edition of a printed material, containing information about
 * its title, number of pages, and size.
 *
 * @param <S> The enum type representing the size of the Edition.
 */
public class Edition<S extends Enum<S>> implements IEdition<S>, ISerializable {

    private static final long serialVersionUID = 1L;
    /**
     * The title of the Edition.
     */
    private String title;

    /**
     * The number of pages in the Edition.
     */
    private int numberOfPages;

    /**
     * The size of the Edition, represented by an enum of type S.
     */
    private S size;

    /**
     * Constructs a new Edition with the specified title, number of pages, and size.
     * <p>
     * This constructor validates the input values and throws an exception if any of the following conditions are met:
     * - The title is null or empty.
     * - The title's length is outside the valid range (between {MIN_TITLE_LENGTH} and {MAX_TITLE_LENGTH}).
     * - The number of pages is less than the minimum required or exceeds the maximum allowed (between {MIN_PAGE_COUNT} and {MAX_PAGE_COUNT}).
     * - The size is {@code null}.
     *
     * @param title         The title of the Edition. Cannot be null, empty, or outside the valid length range.
     * @param numberOfPages The number of pages in the Edition. Must be between {MIN_PAGE_COUNT} and {MAX_PAGE_COUNT}.
     * @param size          The size of the Edition. Cannot be {@code null}.
     * @throws InvalidTitleException If the title is invalid.
     * @throws InvalidNumberOfPagesException If the number of pages is invalid.
     * @throws InvalidPageSizeException If the size is {@code null}.
     */
    public Edition(String title, int numberOfPages, S size) {
        this.setTitle(title);
        this.setNumberOfPages(numberOfPages);
        this.setSize(size);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the Edition.
     * This method validates the title and throws an exception if:
     * - The title is null.
     * - The title consists only of whitespace.
     * - The title's length is outside the valid range (between {MIN_TITLE_LENGTH} and {MAX_TITLE_LENGTH}).
     *
     * @param title The title to set for the Edition. Cannot be null or empty.
     * @throws InvalidTitleException If the title is null, only whitespace, or the length is outside the valid range.
     */
    @Override
    public void setTitle(String title) {

        if (title == null) {
            throw new InvalidTitleException(ExceptionMessages.TITLE_CANNOT_BE_NULL);
        }

        if (title.trim().isEmpty()) {
            throw new InvalidTitleException(ExceptionMessages.TITLE_CANNOT_BE_EMPTY);

        }

        if (title.length() < ModelsConstants.MIN_TITLE_LENGTH || title.length() > ModelsConstants.MAX_TITLE_LENGTH) {
            throw new InvalidTitleException(MessageFormat.format(
                    ExceptionMessages.TITLE_LENGTH_INVALID, ModelsConstants.MIN_TITLE_LENGTH, ModelsConstants.MAX_TITLE_LENGTH));
        }

        this.title = title;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNumberOfPages() {
        return numberOfPages;
    }

    /**
     * Sets the number of pages of the Edition.
     * <p>
     * This method validates the number of pages and throws an exception if:
     * - The number of pages is less than the minimum required (defined in {MIN_PAGE_COUNT}).
     *
     * @param numberOfPages The number of pages to set for the Edition. Must be greater than or equal to {MIN_PAGE_COUNT}.
     * @throws InvalidNumberOfPagesException If the number of pages is less than the minimum allowed.
     */
    @Override
    public void setNumberOfPages(int numberOfPages) {

        if (numberOfPages < ModelsConstants.MIN_PAGE_COUNT || numberOfPages > ModelsConstants.MAX_PAGE_COUNT) {
            throw new InvalidNumberOfPagesException(MessageFormat.format(
                    ExceptionMessages.NUMBER_OF_PAGES_INVALID, ModelsConstants.MIN_PAGE_COUNT, ModelsConstants.MAX_PAGE_COUNT));
        }

        this.numberOfPages = numberOfPages;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public S getSize() {
        return size;
    }

    /**
     * Sets the page size of the Edition.
     * This method validates the provided size is {@code null}, an exception is thrown.
     *
     * @param size The size to set for the Edition. Cannot be {@code null}.
     * @throws InvalidPageSizeException If the provided size is {@code null}.
     */
    @Override
    public void setSize(S size) {

        if (size == null) {
            throw new InvalidPageSizeException(ExceptionMessages.INVALID_PAGE_SIZE);
        }
        this.size = size;
    }

    @Override
    public String toString() {
        return "Edition{" +
                "title='" + title + '\'' +
                ", numberOfPages=" + numberOfPages +
                ", size=" + size +
                '}';
    }
}

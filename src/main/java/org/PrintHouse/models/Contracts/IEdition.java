package org.PrintHouse.models.Contracts;

/**
 * Interface representing the contract for an Edition.
 *
 * @param <E> The enum type representing the size of the Edition.
 */
public interface IEdition<E extends Enum<E>> {

    /**
     * Gets the title of the Edition.
     *
     * @return The title as a String.
     */
    public String getTitle();

    /**
     * Sets the title of the Edition.
     *
     * @param title The title as a String.
     */
    public void setTitle(String title);

    /**
     * Gets the number of pages in the Edition.
     *
     * @return The number of pages as an integer.
     */
    public int getNumberOfPages();

    /**
     * Sets the number of pages in the Edition.
     *
     * @param numberOfPages The number of pages as an integer.
     */
    public void setNumberOfPages(int numberOfPages);

    /**
     * Gets the size of the Edition.
     *
     * @return The size as an enum of type S.
     */
    public E getSize();

    /**
     * Sets the size of the Edition.
     *
     * @param size The size as an enum of type S.
     */
    public void setSize(E size);
}

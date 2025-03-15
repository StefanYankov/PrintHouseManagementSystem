package data.models;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents an edition of a printed material with basic properties.
 */
public class Edition implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String title;
    private int numberOfPages;
    private Size size;

    public Edition() {}

    public Edition(String title, int numberOfPages, Size size) {
        this.title = title;
        this.numberOfPages = numberOfPages;
        this.size = size;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getNumberOfPages() { return numberOfPages; }
    public void setNumberOfPages(int numberOfPages) { this.numberOfPages = numberOfPages; }

    public Size getSize() { return size; }
    public void setSize(Size size) { this.size = size; }

    @Override
    public String toString() {
        return "Edition{title='" + title + "', numberOfPages=" + numberOfPages + ", size=" + size + "}";
    }
}
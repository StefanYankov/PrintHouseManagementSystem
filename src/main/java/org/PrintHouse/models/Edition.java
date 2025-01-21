package org.PrintHouse.models;

public class Edition {

    private String title;
    private int numberOfPages;
    private Sizes size;

    public Edition(String title, int numberOfPages, Sizes size) {
        this.title = title;
        this.numberOfPages = numberOfPages;
        this.size = size;
    }

    public String getTitle() {
        return title;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public Sizes getSize() {
        return size;
    }

}

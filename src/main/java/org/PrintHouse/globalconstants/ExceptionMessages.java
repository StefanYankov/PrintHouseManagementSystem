package org.PrintHouse.globalconstants;

public class ExceptionMessages {

    //TODO: if messages become too many split them in static classes or in separate classes
    public static final String INVALID_PAGE_COUNT = "Page count must be greater than zero.";
    public static final String TITLE_NULL_OR_EMPTY = "Title cannot be null, empty, or just whitespace.";
    public static final String TITLE_TOO_SHORT = "Title must be at least {0} characters long.";
    public static final String TITLE_TOO_LONG = "Title cannot be longer than {0} characters.";
    public static final String INVALID_PAGE_SIZE = "Page size {0} is invalid.";
}

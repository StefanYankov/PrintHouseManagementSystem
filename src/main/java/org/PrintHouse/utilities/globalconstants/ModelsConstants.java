package org.PrintHouse.utilities.globalconstants;

import java.math.BigDecimal;

public class ModelsConstants {
    public static final int MIN_TITLE_LENGTH = 2;
    public static final int MAX_TITLE_LENGTH = 150;
    public static final int MIN_PAGE_COUNT = 3;
    public static final int MAX_PAGE_COUNT = 1000;
    public static final BigDecimal MAXIMUM_PRICE = BigDecimal.valueOf(1_000_000_000);
    public static final int MAXIMUM_PAPER_LOAD = 100_000;
    public static final int MAXIMUM_PAGES_PER_MINUTE = 1000;
}

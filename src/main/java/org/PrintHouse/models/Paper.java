package org.PrintHouse.models;

import java.math.BigDecimal;

public class Paper {
    private PaperType paperType;
    private PageSize pageSize;

    public Paper(PaperType paperType, PageSize pageSize) {
        this.paperType = paperType;
        this.pageSize = pageSize;
    }

    public BigDecimal getPrice() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

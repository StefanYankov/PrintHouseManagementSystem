package org.PrintHouse.models;

import java.math.BigDecimal;

public class Paper {
    private PaperType paperType;
    private PageSize pageSize;
    //TODO: Base price for A5
    //TODO: Price increase for other types
    public Paper(PaperType paperType, PageSize pageSize) {
        this.paperType = paperType;
        this.pageSize = pageSize;
    }

    public BigDecimal getPrice() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return "Paper{" +
                "paperType=" + paperType +
                ", pageSize=" + pageSize +
                '}';
    }
}

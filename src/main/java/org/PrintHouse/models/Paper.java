package org.PrintHouse.models;

import org.PrintHouse.models.Contracts.IPaperTypable;

import java.math.BigDecimal;

public class Paper {
    private IPaperTypable paperType;
    private PageSize pageSize;

    public Paper(IPaperTypable paperType, PageSize pageSize) {
        this.paperType = paperType;
        this.pageSize = pageSize;
    }


}

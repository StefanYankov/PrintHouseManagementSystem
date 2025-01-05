package org.PrintHouse.models.Contracts;

import org.PrintHouse.models.PageSize;

public interface IPrintable {
    public String getTitle();
    public int getPageCount();
    public PageSize getPageSize();
}

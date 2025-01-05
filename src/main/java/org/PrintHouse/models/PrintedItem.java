package org.PrintHouse.models;

import org.PrintHouse.exceptions.InvalidPageCountException;
import org.PrintHouse.exceptions.InvalidTitleException;
import org.PrintHouse.globalconstants.ExceptionMessages;
import org.PrintHouse.globalconstants.ModelsConstants;
import org.PrintHouse.models.Contracts.IPrintable;

public class PrintedItem implements IPrintable {
    private String title;
    private int pageCount;
    private PageSize pageSize;

    public PrintedItem(String title, int pageCount, PageSize pageSize) {
        this.setTitle(title);
        this.setPageCount(pageCount);
        this.pageSize = pageSize;
    }

    @Override
    public String getTitle() {

        return title;
    }

    private void setTitle(String title) {

        if (title == null || title.trim().isEmpty()) {
            throw new InvalidTitleException(ExceptionMessages.TITLE_NULL_OR_EMPTY);
        }
        if (title.length() < ModelsConstants.MIN_TITLE_LENGTH) {
            throw new InvalidTitleException(String.format(ExceptionMessages.TITLE_TOO_SHORT, ModelsConstants.MIN_TITLE_LENGTH));
        }
        if (title.length() > ModelsConstants.MAX_TITLE_LENGTH) {
            throw new InvalidTitleException(String.format(ExceptionMessages.TITLE_TOO_LONG, ModelsConstants.MAX_TITLE_LENGTH));
        }
        this.title = title;
    }

    @Override
    public int getPageCount() {
        return pageCount;
    }

    private void setPageCount(int pageCount) {
        if (pageCount <= 0) {
            throw new InvalidPageCountException(ExceptionMessages.INVALID_PAGE_COUNT);
        }
        this.pageCount = pageCount;
    }

    @Override
    public PageSize getPageSize() {
        return pageSize;
    }

    private void setPageSize(PageSize pageSize) {
        this.pageSize = pageSize;
    }
}

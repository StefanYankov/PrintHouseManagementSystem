package org.PrintHouse.models;

import java.math.BigDecimal;

public class PrintedItem {
    private Edition edition;
    private PaperType paperType;
    private BigDecimal price;


    public PrintedItem(Edition edition, PaperType paperType, BigDecimal price ) {
        this.edition = edition;
        this.paperType = paperType;
        this.price = price;
    }

    public Edition getEdition() {
        return edition;
    }

    public void setEdition(Edition edition) {
        this.edition = edition;
    }

    public PaperType getPaperType() {
        return paperType;
    }

    public void setPaperType(PaperType paperType) {
        this.paperType = paperType;
    }

    public BigDecimal getCost() {
        var pages = this.edition.getNumberOfPages();
        var pageSize = this.edition.getSize();
        Enum<?> enumValue = this.paperType;
        var costForSmallest = this.paperType.getCost(enumValue);

        return costForSmallest.multiply(BigDecimal.valueOf(pages));
    }

    public int getNumberOfPages(){
        return this.edition.getNumberOfPages();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        // validation can be added if price is below the cost
        this.price = price;
    }
}

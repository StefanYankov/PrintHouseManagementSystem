package data.models;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Represents a printed item with edition and printing-specific details.
 */
public class PrintedItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Edition edition;
    private PaperType paperType;
    private BigDecimal price;
    private boolean isColour;

    public PrintedItem() {}

    public PrintedItem(Edition edition, PaperType paperType, BigDecimal price, boolean isColour) {
        this.edition = edition;
        this.paperType = paperType;
        this.price = price;
        this.isColour = isColour;
    }

    public Edition getEdition() { return edition; }
    public void setEdition(Edition edition) { this.edition = edition; }

    public PaperType getPaperType() { return paperType; }
    public void setPaperType(PaperType paperType) { this.paperType = paperType; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public boolean isColour() { return isColour; }
    public void setColour(boolean isColour) { this.isColour = isColour; }

    @Override
    public String toString() {
        return "PrintedItem{edition=" + edition + ", paperType=" + paperType + ", price=" + price + ", isColour=" + isColour + "}";
    }
}
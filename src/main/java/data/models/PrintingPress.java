package data.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a printing press in the print house.
 */
public class PrintingPress implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private int maxPaperLoad;
    private int currentPaperLoad;
    private boolean isColour;
    private int maximumPagesPerMinute;
    private Map<PrintedItem, Integer> printedItems;

    public PrintingPress() {
        this.printedItems = new HashMap<>();
    }

    public PrintingPress(int maxPaperLoad, int currentPaperLoad, boolean isColour, int maximumPagesPerMinute) {
        this.maxPaperLoad = maxPaperLoad;
        this.currentPaperLoad = currentPaperLoad;
        this.isColour = isColour;
        this.maximumPagesPerMinute = maximumPagesPerMinute;
        this.printedItems = new HashMap<>();
    }

    public int getMaxPaperLoad() { return maxPaperLoad; }
    public void setMaxPaperLoad(int maxPaperLoad) { this.maxPaperLoad = maxPaperLoad; }

    public int getCurrentPaperLoad() { return currentPaperLoad; }
    public void setCurrentPaperLoad(int currentPaperLoad) { this.currentPaperLoad = currentPaperLoad; }

    public boolean isColour() { return isColour; }
    public void setColour(boolean isColour) { this.isColour = isColour; }

    public int getMaximumPagesPerMinute() { return maximumPagesPerMinute; }
    public void setMaximumPagesPerMinute(int maximumPagesPerMinute) { this.maximumPagesPerMinute = maximumPagesPerMinute; }

    public Map<PrintedItem, Integer> getPrintedItems() { return printedItems; }
    public void setPrintedItems(Map<PrintedItem, Integer> printedItems) {
        if (printedItems == null) throw new IllegalArgumentException("Printed items map cannot be null.");
        this.printedItems = printedItems;
    }

    @Override
    public String toString() {
        return "PrintingPress{maxPaperLoad=" + maxPaperLoad + ", currentPaperLoad=" + currentPaperLoad +
                ", isColour=" + isColour + ", maxPagesPerMinute=" + maximumPagesPerMinute + "}";
    }
}
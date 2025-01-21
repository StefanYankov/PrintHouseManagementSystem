package org.PrintHouse.models;

import java.util.HashMap;
import java.util.Map;

public class PrintingPress {
    private final int maxPaperLoad;
    private int currentPaperLoad;
    private final boolean canOnlyPrintColor;
    private final int maximumPagesPerMinute;
    private final Map<PrintedItem, Integer> printedItems;

    public PrintingPress(int maxPaperLoad, int currentPaperLoad, boolean canColorPrint, int maximumPagesPerMinute) {
        this.maxPaperLoad = maxPaperLoad;
        this.currentPaperLoad = currentPaperLoad;
        this.canOnlyPrintColor = canColorPrint;
        this.maximumPagesPerMinute = maximumPagesPerMinute;
        this.printedItems = new HashMap<PrintedItem, Integer>();
    }

    public int getMaxPaperLoad() {
        return maxPaperLoad;
    }

    public int getCurrentPaperLoad() {
        return currentPaperLoad;
    }

    public void setCurrentPaperLoad(int currentPaperLoad) {
        this.currentPaperLoad = currentPaperLoad;
    }

    public boolean isCanOnlyPrintColor() {
        return canOnlyPrintColor;
    }

    public int getMaximumPagesPerMinute() {
        return maximumPagesPerMinute;
    }

    public Map<PrintedItem, Integer> getPrintedItems() {
        return printedItems;
    }

    public void addPrintedItem(PrintedItem printedItem, int countOfCopies) {
        this.printedItems.put(printedItem, countOfCopies);
    }

    public void addPrintedItem(PrintedItem printedItem) {
        addPrintedItem(printedItem, 1);
    }

    public void loadPaper(int paperCount) {
        if (this.getCurrentPaperLoad() == this.getMaxPaperLoad()) {
            throw new IllegalArgumentException("Printer paper already at full capacity");
        }

        if (this.getCurrentPaperLoad() + paperCount > this.getMaxPaperLoad()) {
            this.setCurrentPaperLoad(this.getMaxPaperLoad());
        } else {
            this.setCurrentPaperLoad(this.getCurrentPaperLoad() + paperCount);
        }
    }

    public void printAnItem(boolean isColor, PrintedItem printedItem, PaperType paperType, int copies) {
        if (isColor != this.isCanOnlyPrintColor()) {
            throw new IllegalArgumentException("Incompatible color type");
        }

        if (printedItem.getNumberOfPages() > this.getCurrentPaperLoad()) {
            throw new IllegalArgumentException("insufficient paper load");
        }

        this.addPrintedItem(printedItem,copies);
    }

    public long totalPrintedPages() {
        return this.getPrintedItems()
                .entrySet()
                .stream()
                .mapToLong(x -> x.getKey().getNumberOfPages() * x.getValue())
                .sum();
    }
}

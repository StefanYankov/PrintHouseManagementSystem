package org.PrintHouse.models;

import org.PrintHouse.models.Contracts.IPaperTypable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PaperType implements IPaperTypable {

    private final Map<String, BigDecimal> paperCosts;

    public PaperType() {
        paperCosts = new HashMap<String, BigDecimal>();
    }

    public PaperType(Map<String, BigDecimal> paperCosts) {
        this.validateCosts(paperCosts);
        this.paperCosts = new HashMap<>(paperCosts);
    }

    @Override
    public void addPaperCost(String paperType, BigDecimal cost) {
        paperCosts.put(paperType, cost);
    }

    @Override
    public void removePaperCost(String paperType) {
        paperCosts.remove(paperType);
    }

    @Override
    public BigDecimal getPaperCost(String paperType) {

        if (!paperCosts.containsKey(paperType)) {
            //TODO: custom exception
            throw new IllegalArgumentException("Paper type " + paperType + " does not exist");
        }
        return paperCosts.get(paperType);
    }

    // Utility methods
    private void validateCost(BigDecimal cost) {
        if (cost == null || cost.compareTo(BigDecimal.ZERO) <= 0) {
            // TODO: add custom exception handling
            throw new IllegalArgumentException("cost should be greater than zero");
        }
    }

    private void validateCosts(Map<String, BigDecimal> costs) {
        for (Map.Entry<String, BigDecimal> entry : paperCosts.entrySet()) {
            validateCost(entry.getValue());
        }
    }
}

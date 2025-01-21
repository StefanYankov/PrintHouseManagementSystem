package org.PrintHouse.models;

import org.PrintHouse.models.Contracts.IPaperTypes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public enum PaperType implements IPaperTypes {
    STANDARD, GLOSSY, NEWSPAPER;

    private final Map<PaperType, BigDecimal> paperCosts = new HashMap<>();

    static {
        STANDARD.paperCosts.put(STANDARD, BigDecimal.valueOf(100));
        GLOSSY.paperCosts.put(GLOSSY, BigDecimal.valueOf(120));
        NEWSPAPER.paperCosts.put(NEWSPAPER, BigDecimal.valueOf(80));
    }

    @Override
    public BigDecimal getCost(Enum<?> type) {
        if (type instanceof PaperType) {
            return paperCosts.get(type);
        }
        return BigDecimal.ZERO;
    }
}

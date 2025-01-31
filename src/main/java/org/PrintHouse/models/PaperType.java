package org.PrintHouse.models;


import org.PrintHouse.models.Contracts.IPaperTypes;

import java.math.BigDecimal;


public enum PaperType implements IPaperTypes {
    STANDARD(BigDecimal.valueOf(100)),
    GLOSSY(BigDecimal.valueOf(120)),
    NEWSPAPER(BigDecimal.valueOf(80));

    private final BigDecimal cost;

    PaperType(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getCost() {
        return this.cost;
    }
}

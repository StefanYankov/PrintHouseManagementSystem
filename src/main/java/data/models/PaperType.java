package data.models;

import java.math.BigDecimal;

/**
 * Enum representing paper types with base costs for A5 size.
 */
public enum PaperType {
    STANDARD(BigDecimal.valueOf(100)),
    GLOSSY(BigDecimal.valueOf(120)),
    NEWSPAPER(BigDecimal.valueOf(80));

    private final BigDecimal cost;

    PaperType(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getCost() {
        return cost;
    }
}
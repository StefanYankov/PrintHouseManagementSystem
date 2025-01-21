package org.PrintHouse.models.Contracts;

import java.math.BigDecimal;

public interface IPaperTypes {
    public BigDecimal getCost(Enum<?> type);

}

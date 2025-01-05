package org.PrintHouse.models.Contracts;

import java.math.BigDecimal;

public interface IPaperTypable {

    public void addPaperCost(String paperType, BigDecimal cost);
    public void removePaperCost(String paperType);
    public BigDecimal getPaperCost(String paperType);

}

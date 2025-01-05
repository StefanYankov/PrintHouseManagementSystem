package org.PrintHouse.services;

import org.PrintHouse.models.Contracts.IPrintable;

import java.math.BigDecimal;

public interface IPrintingService {
    public void print(IPrintable item, int copies, boolean isColor);
    public BigDecimal calculatePaperCost(IPrintable item, int copies);
}

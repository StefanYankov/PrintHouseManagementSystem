package org.PrintHouse.services;

import org.PrintHouse.models.Contracts.IPrintable;

import java.math.BigDecimal;

public class PrintingService implements IPrintingService {
    @Override
    public void print(IPrintable item, int copies, boolean isColor) {

    }

    @Override
    public BigDecimal calculatePaperCost(IPrintable item, int copies) {
        return null;
    }
}

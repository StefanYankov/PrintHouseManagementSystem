package org.PrintHouse;

import org.PrintHouse.models.PageSize;
import org.PrintHouse.models.Paper;
import org.PrintHouse.models.PaperType;

public class Main {
    public static void main(String[] args) {


        Paper paper = new Paper(PaperType.NEWSPAPER_PAPER,PageSize.A5);
        System.out.println(paper.toString());
    }
}
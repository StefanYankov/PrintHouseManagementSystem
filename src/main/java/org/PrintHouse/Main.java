package org.PrintHouse;

import org.PrintHouse.core.Engine;
import org.PrintHouse.core.contracts.IEngine;

public class Main {
    public static void main(String[] args) {

        IEngine engine = new Engine();
        engine.run();
    }
}
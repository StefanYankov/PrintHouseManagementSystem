package org.PrintHouse;

import org.PrintHouse.core.ConsoleEngine;
import org.PrintHouse.core.DemoEngine;
import org.PrintHouse.core.contracts.IEngine;

public class Main {
    public static void main(String[] args) {

        // IEngine engine = new ConsoleEngine();
        IEngine engine = new DemoEngine(); // uncomment and comment above for change of engine
        engine.run();
    }
}
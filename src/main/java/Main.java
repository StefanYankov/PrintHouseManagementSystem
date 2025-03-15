import UI.ConsoleEngine;
import UI.contracts.IEngine;
import data.models.Edition;
import data.models.PrintHouse;
import services.EmployeeService;
import services.PrintHouseService;
import services.PrintingPressService;
import services.EditionService;
import services.SerializationService;
import services.contracts.IEmployeeService;
import services.contracts.IPrintHouseService;
import services.contracts.IPrintingPressService;
import services.contracts.IEditionService;
import services.contracts.ISerializationService;

public class Main {
    public static void main(String[] args) {

        // Services registration
        ISerializationService<PrintHouse> serializationService = new SerializationService<>();
        ISerializationService<Edition> editionISerializationService = new SerializationService<>();
        IPrintHouseService printHouseService = new PrintHouseService(serializationService);
        IPrintingPressService printingPressService = new PrintingPressService();
        IEmployeeService employeeService = new EmployeeService(printingPressService);

        IEditionService editionService = new EditionService(editionISerializationService);

        // Engine initialization
        IEngine engine = new ConsoleEngine(serializationService, editionISerializationService,
                printHouseService, employeeService,
                printingPressService, editionService);

        engine.run();
    }
}
# PrintHouse Management System

This project is the **final project** for the university course:  
**[CITB408 Java Programming - Spring Semester 2020/2021](https://ecatalog.nbu.bg/default.asp?V_Year=2021&YSem=4&Spec_ID=&Mod_ID=&PageShow=coursepresent&P_Menu=courses_part1&Fac_ID=3&M_PHD=&P_ID=832&TabIndex=1&K_ID=48852&K_TypeID=10&l=0)**.


## :point_right: Project Introduction :point_left:

The **PrintHouse Management System** is a Java-based application designed to simulate the operations of a printing house. It includes functionalities for managing employees, printing presses, editions, and calculating costs and revenues. The system is built using object-oriented principles and includes serialization for data persistence.

---

## Features

- **Employee Management**: Add and remove employees, manage their roles (e.g., Operator, Manager), and calculate salaries with potential increments.
- **Printing Press Management**: Manage printing presses, including paper load, color printing capabilities, and maximum pages per minute.
- **Edition Management**: Create and manage editions (e.g., books, magazines) with details like title, number of pages, and size.
- **Cost and Revenue Calculation**: Calculate the total cost of printed items, employee salaries, and overall revenue. Apply discounts based on sales targets.
- **Serialization**: Serialize and deserialize data to/from files for persistence.

---

## Code Structure

The project is organized into several packages:

- **`org.PrintHouse.core`**: Contains the core logic and engine of the application.
- **`org.PrintHouse.models`**: Contains the data models such as `Employee`, `Edition`, `PrintingPress`, and `PrintHouse`.
- **`org.PrintHouse.models.Contracts`**: Contains interfaces defining contracts for models.
- **`org.PrintHouse.utilities`**: Contains utility classes like `SerializationService` for handling serialization and deserialization.

---

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 or higher.
- Gradle (for building the project).

### Building the Project

1. Clone the repository:
   ```bash
   git clone https://github.com/StefanYankov/PrintHouseManagementSystem.git
   ```
2. Navigate to the project directory:
   ```bash
   cd PrintHouseManagementSystem
   ```
3. Build the project using Gradle:
   ```bash
   ./gradlew build
   ```

---

### Running the Application

   To run the application, execute the DemoEngine class, which serves as the entry point:

   ```bash
   ./gradlew run
   ```

---

### Example Usage

The DemoEngine class demonstrates the functionality of the system:

- Creates a `PrintHouse` instance with predefined configurations.
- Adds employees and printing presses.
- Creates and prints editions.
- Calculates costs and revenues.
- Serializes and deserializes data.

Alternativelly you can update the main method to use `ConsoleEngine`, where you have a simple console menu

---

### Dependencies

- **SLF4J**: For logging.
- **Logback**: As the logging implementation.

---

## Unit tests Code coverage

![Code coverage](https://github.com/StefanYankov/PrintHouseManagementSystem/blob/master/tests-code-coverage.PNG)

---

### :hammer: Used Technologies
- **Java 17 or higher** – Core programming language  
- **Gradle** – Build automation tool  
- **SLF4J & Logback** – Logging framework  

---

## :floppy_disk: UML Class Diagram
![]()

---

### License

The project is licensed under MIT License. See the **[LICENSE](https://github.com/StefanYankov/PrintHouseManagementSystem/blob/master/LICENSE)** file for details.

---

## Acknowledgments

- This project was developed as part of the **CITB408 Java Programming** course at [New Bulgarian University](https://nbu.bg/).
- Special thanks to the course instructor for creating the project requirements.

---

## Repository

GitHub Repository: [https://github.com/StefanYankov/PrintHouseManagementSystem/](https://github.com/StefanYankov/PrintHouseManagementSystem/)


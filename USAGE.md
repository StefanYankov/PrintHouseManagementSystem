# Use Cases for Print House Management System as per CITB408 Course Requirements

## Use Case 1: Manage Print Houses

**Requirement:** Въвеждане, редактиране и изтриване на печатници, които извършват печатни услуги и наемат служители  
**Purpose:** Manage print houses providing printing services and employing staff.

### Add Print House 1

- **Input:**
  - Menu selection: `1` (Manage Print Houses) → `2` (Add a print house)
  - Salary increment %: `15`
  - Paper increment %: `20`
  - Base salary: `2500`
  - Revenue target: `10000`
  - Discount count: `10`
  - Discount %: `10`
- **Expected Output:**

  ```text
  Print house created successfully at index: 0
  ```

- **Logger Output:**

  ```text
  [INFO] services.PrintHouseService - PrintHouse created: PrintHouse{employeeSalaryIncrementPercentage=15, paperIncrementPercentage=20, baseSalary=2500, incrementEligibleRoles=[MANAGER], revenueTarget=10000, salesDiscountCount=10, salesDiscountPercentage=10}
  ```

### Update Print House 1

- **Input:**
  - Menu selection: `1` (Manage Print Houses) → `3` (Update a print house)
  - Print house index: `1` (index 0)
  - New base salary: `3000` (Enter for others)
- **Expected Output:**

  ```text
  Print house updated.
  ```

- **Logger Output:**

  ```text
  [INFO] services.PrintHouseService - PrintHouse updated at index 0: PrintHouse{baseSalary=3000, ...}
  ```

### Remove Print House (Post-Demo)

- **Input:**
  - Menu selection: `1` (Manage Print Houses) → `4` (Remove a print house)
  - Print house index: `1` (index 0)
- **Expected Output:**

  ```text
  Print house removed.
  ```

- **Logger Output:**

  ```text
  [INFO] services.PrintHouseService - PrintHouse removed: PrintHouse{...}
  ```

---

## Use Case 2: Manage Employees

**Requirement:** Въвеждане, редактиране и изтриване на служителите в печатниците  
**Purpose:** Manage employee records with ЕГН uniqueness.

### Add Employee 1 (Operator) to Print House 1

- **Input:**
  - Menu selection: `2` (Manage Employees) → `1` (Add an employee)
  - Print house index: `1` (index 0)
  - ЕГН: `7501020018`
  - Employee type: `OPERATOR`
- **Expected Output:**

  ```text
  Employee added.
  ```

- **Logger Output:**

  ```text
  [INFO] services.EmployeeService - Added employee Employee{egn=7501020018, type=OPERATOR} to print house: PrintHouse{...}
  ```

### Add Employee 2 (Manager) to Print House 1

- **Input:**

  - Menu selection: `2` (Manage Employees) → `1` (Add an employee)
  - Print house index: `1` (index 0)
  - ЕГН: `6907167565`
  - Employee type: `MANAGER`
- **Expected Output:**

  ```text
  Employee added.
  ```

- **Logger Output:**

  ```text
  [INFO] services.EmployeeService - Added employee Employee{egn=8003050024, type=MANAGER} to print house: PrintHouse{...}
  ```

### List All Employees

- **Input:**

  - Menu selection: `2` (Manage Employees) → `5` (List all employees)
  - Print house index: `1` (index 0)
- **Expected Output:**

  ```text
  1. Employee{egn=7501020018, type=OPERATOR}
  2. Employee{egn=8003050024, type=MANAGER}
  ```

- **Logger Output:**

  ```text
  [INFO] UI.controllers.EmployeeController - Listed 2 employees
  ```

### Update Employee 1 (Operator to Manager)

- **Input:**
  - Menu selection: `2` (Manage Employees) → `3` (Update an employee)
  - Print house index: `1` (index 0)
  - Employee number: `1` (index 0)
  - New employee type: `MANAGER`
- **Expected Output:**

  ```text
  Employee updated.
  ```

- **Logger Output:**

  ```text
  [INFO] services.EmployeeService - Updated employee at index 0 in PrintHouse {...}: Employee{egn=7501020018, type=MANAGER}
  ```

### Remove Employee 2

- **Input:**
  - Menu selection: `2` (Manage Employees) → `2` (Remove an employee)
  - Print house index: `1` (index 0)
  - Employee number: `2` (index 1)
- **Expected Output:**

  ```text
  Employee removed.
  ```

- **Logger Output:**

  ```text
  [INFO] services.EmployeeService - Removed employee Employee{egn=8003050024, type=MANAGER} at index 1 from PrintHouse {...}
  ```

### Add Employee with Invalid ЕГН

- **Input:**
  - Menu selection: `2` (Manage Employees) → `1` (Add an employee)
  - Print house index: `1` (index 0)
  - ЕГН: `7533021234` (invalid month prefix)
  - Employee type: `OPERATOR`
- **Expected Output:**

  ```text
  Error: Invalid ЕГН format
  ```

- **Logger Output:**

  ```text
  [WARN] UI.controllers.EmployeeController - Failed to add employee with ЕГН 7533021234: Invalid ЕГН format
  ```

---

## Use Case 3: Manage Printing Presses

**Requirement:** Въвеждане, редактиране и изтриване на печатните машини, собственост на печатниците  
**Purpose:** Manage printing presses for print houses.

### Add Printing Press 1 to Print House 1

- **Input:**
  - Menu selection: `3` (Manage Printing Presses) → `1` (Add a printing press)
  - Print house index: `1` (index 0)
  - Max paper load: `5000`
  - Current paper load: `4000`
  - Color capable: `true`
  - Max pages per minute: `300`
- **Expected Output:**

   ```text
  Printing press added.
  ```

- **Logger Output:**

   ```text
  [INFO] services.PrintingPressService - Added printing press PrintingPress{maxPaperLoad=5000, currentPaperLoad=4000, isColour=true, maximumPagesPerMinute=300} to PrintHouse {...}
  ```

### Update Printing Press 1

- **Input:**
  - Menu selection: `3` (Manage Printing Presses) → `3` (Update a printing press)
  - Print house index: `1` (index 0)
  - Press index: `1` (index 0)
  - New max pages per minute: `350` (Enter for others)
- **Expected Output:**

  ```text
  Printing press updated.
  ```

- **Logger Output:**

  ```text
  [INFO] services.PrintingPressService - Updated printing press PrintingPress{maximumPagesPerMinute=350, ...} in PrintHouse {...}
  ```

### Remove Printing Press (Post-Demo)

- **Input:**
  - Menu selection: `3` (Manage Printing Presses) → `2` (Remove a printing press)
  - Print house index: `1` (index 0)
  - Press index: `1` (index 0)
- **Expected Output:**

  ```text
  Printing press removed.
  ```

- **Logger Output:**

  ```text
  [INFO] services.PrintingPressService - Removed printing press PrintingPress{...} from PrintHouse {...}
  ```

---

## Use Case 4: Manage Editions

**Requirement:** Въвеждане, редактиране и изтриване на изданията за печат  
**Purpose:** Manage editions for printing.

### Add Edition 1 to Print House 1

- **Input:**
  - Menu selection: `4` (Manage Editions) → `2` (Add edition)
  - Print house index: `1` (index 0)
  - Title: `Lord of the Rings`
  - Number of pages: `1300`
  - Size: `A4`
- **Expected Output:**

  ```text
  Edition added.
  ```

- **Logger Output:**

  ```text
  [INFO] services.EditionService - Added edition Edition{title='Lord of the Rings', numberOfPages=1300, size=A4} to PrintHouse {...}
  ```

### Update Edition 1

- **Input:**
  - Menu selection: `4` (Manage Editions) → `3` (Update edition)
  - Print house index: `1` (index 0)
  - Edition index: `1` (index 0)
  - New title: `Lord of the Rings: Fellowship` (Enter for others)
- **Expected Output:**

  ```text
  Edition updated.
  ```

- **Logger Output:**

  ```text
  [INFO] services.EditionService - Edition updated in PrintHouse {...}: Edition{title='Lord of the Rings: Fellowship', ...}
  ```

### Remove Edition (Post-Demo)

- **Input:**
  - Menu selection: `4` (Manage Editions) → `4` (Remove edition)
  - Print house index: `1` (index 0)
  - Edition index: `1` (index 0)
- **Expected Output:**

  ```text
  Edition removed.
  ```

- **Logger Output:**

  ```text
  [INFO] services.EditionService - Edition removed from PrintHouse {...}: Edition{...}
  ```

---

## Use Case 5: Print Items

**Requirement:** Печатане на издания с определени параметри (тип хартия, цена, брой копия)  
**Purpose:** Execute printing jobs.

### Print Edition 1 (Below Discount Threshold)

- **Input:**
  - Menu selection: `3` (Manage Printing Presses) → `5` (Print an item)
  - Print house index: `1` (index 0)
  - Press index: `1` (index 0)
  - Edition index: `1` (index 0)
  - Paper type: `STANDARD`
  - Price per copy: `60`
  - Number of copies: `5`
  - Color print: `true`
- **Expected Output:**

  ```text
  Item printed.
  ```

- **Logger Output:**

  ```text
  [INFO] services.PrintingPressService - Printed 5 copies of edition Edition{title='Lord of the Rings: Fellowship', ...} using press PrintingPress{...}
  ```

### Print Edition 1 (Hits Discount Threshold)

- **Input:**
  - Menu selection: `3` (Manage Printing Presses) → `5` (Print an item)
  - Print house index: `1` (index 0)
  - Press index: `1` (index 0)
  - Edition index: `1` (index 0)
  - Paper type: `STANDARD`
  - Price per copy: `600`
  - Number of copies: `20`
  - Color print: `true`
- **Expected Output:**

  ```text
  Item printed.
  ```

- **Logger Output:**

  ```text
  [INFO] services.PrintingPressService - Printed 20 copies of edition Edition{title='Lord of the Rings: Fellowship', ...} using press PrintingPress{...}
  ```

---

## Use Case 6: Calculate Costs and Revenues

**Requirement:** Изчисляване на разходите за печат и приходите от продажби  
**Purpose:** Track financial metrics.

### Calculate Total Print Cost

- **Input:**
  - Menu selection: `3` (Manage Printing Presses) → `6` (Calculate total print cost)
  - Print house index: `1` (index 0)
- **Expected Output:**

  ```text
  Total Print Cost: 1746000.00
  ```

- **Breakdown**: (5 *650* 120) + (20 *650* 120) = 390,000 + 1,356,000 (assuming 120 per A4 sheet).
- **Logger Output:**

  ```text
  [INFO] services.PrintingPressService - Calculated total print cost for PrintHouse {...}: 1746000.00
  ```

### Calculate Total Revenue (Below Target)

- **Input:**
  - Menu selection: `3` (Manage Printing Presses) → `7` (Calculate total revenue)
  - Print house index: `1` (index 0)
- **Expected Output:**

  ```text
  Total Revenue: 300.00
  ```

- **Breakdown**: 5 * 60 = 300 (no discount yet).
- **Logger Output:**

  ```text
  [INFO] services.PrintingPressService - Calculated total revenue for PrintHouse {...}: 300.00
  ```

### Calculate Employee Cost (Below Target)

- **Input:**
  - Menu selection: `2` (Manage Employees) → `4` (Calculate total employee cost)
  - Print house index: `1` (index 0)
- **Expected Output:**

  ```text
  Total Employee Cost: 3000.00
  ```

- **Breakdown**: 1 manager * 3000 (revenue 11,400 > 10,000 after second print, but only 1 employee remains).
- **Logger Output:**

  ```text
  [INFO] services.EmployeeService - Calculated total cost: 3000.00
  ```

### Calculate Total Revenue (Hits Target)

- **Input:**
  - Menu selection: `3` (Manage Printing Presses) → `7` (Calculate total revenue)
  - Print house index: `1` (index 0)
- **Expected Output:**

  ```text
  Total Revenue: 11100.00
  ```

- **Breakdown**: 300 + (20 *600* 0.9) = 300 + 10,800 (discount applies).
- **Logger Output:**

  ```text
  [INFO] services.PrintingPressService - Calculated total revenue for PrintHouse {...}: 11100.00
  ```

### Calculate Employee Cost (Hits Target)

- **Input:**
  - Menu selection: `2` (Manage Employees) → `4` (Calculate total employee cost)
  - Print house index: `1` (index 0)
- **Expected Output:**

  ```text
  Total Employee Cost: 3450.00
  ```

- **Breakdown**: 1 manager *3000* 1.15 = 3450 (increment applies).
- **Logger Output:**

  ```text
  [INFO] services.EmployeeService - Calculated total cost: 3450.00
  ```

---

## Use Case 7: Load Paper

**Requirement:** Зареждане на хартия в печатните машини  
**Purpose:** Manage paper stock.

### Load Paper into Press 1 (After First Print)

- **Input:**
  - Menu selection: `3` (Manage Printing Presses) → `4` (Load paper into press)
  - Print house index: `1` (index 0)
  - Press index: `1` (index 0)
  - Amount of paper: `2000`
- **Expected Output:**

  ```text
  Paper loaded.
  ```

- **Logger Output:**

  ```text
  [INFO] services.PrintingPressService - Loaded 2000 paper into press: PrintingPress{currentPaperLoad=4750, ...}
  ```

- **Note**: After 5 copies (3250 sheets), load was 750; now 4750.

---

## Use Case 8: Save and Retrieve Data with SerializationService

**Requirement:** Записване на данните за печатниците във файл и възможност за извличането и показването на тези данни  
**Purpose:** Persist and restore data.

### Save Print House Data

- **Input:**
  - Menu selection: `1` (Manage Print Houses) → `5` (Save all print houses)
  - File path: `print_houses.ser`
- **Expected Output:**

  ```text
  Saved 1 print houses to print_houses.ser
  ```

- **Logger Output:**

  ```text
  [INFO] services.PrintHouseService - Saved 1 print houses to print_houses.ser
  ```

### Load Print House Data

- **Input:**
  - Menu selection: `1` (Manage Print Houses) → `6` (Load all print houses)
  - File path: `print_houses.ser`
- **Expected Output:**

  ```text
  Loaded 1 print houses from print_houses.ser
  ```

- **Logger Output:**

  ```text
  [INFO] services.PrintHouseService - Loaded 1 print houses from print_houses.ser
  ```

---

## Use Case 9: Generate Reports

**Requirement:** Показване на справки за общ - `Total Printed Pages`, обща сума на приходите, разходите за печат и др.  
**Purpose:** Provide operational insights.

### View Total Printed Pages

- **Input:**
  - Menu selection: `3` (Manage Printing Presses) → `8` (Calculate total printed pages)
  - Print house index: `1` (index 0)
  - Press index: `1` (index 0)
- **Expected Output:**

  ```text
  Total Printed Pages: 16250
  ```

- **Breakdown**: (5 *650) + (20* 650) = 3250 + 13,000.
- **Logger Output:**

  ```text
  [INFO] services.PrintingPressService - Calculated total printed pages: 16250
  ```

---

## Use Case 10: Handle Invalid Print House Index

**Requirement:** Обработка на грешки при въвеждане на невалиден индекс на печатница  
**Purpose:** Ensure robust error handling.

### Attempt Invalid Print House Index

- **Input:**
  - Menu selection: `2` (Manage Employees) → `1` (Add an employee)
  - Print house index: `99`
- **Expected Output:**

  ```text
  No print houses available.
  ```

- **Logger Output:**

  ```text
  [WARN] UI.controllers.EmployeeController - No print houses found
  ```

---

## Use Case 11: Handle Insufficient Paper Load

**Requirement:** Обработка на грешки при недостатъчно количество хартия за печат  
**Purpose:** Prevent invalid print operations.

### Attempt Print with Insufficient Paper

- **Input:**
  - Menu selection: `3` (Manage Printing Presses) → `5` (Print an item)
  - Print house index: `1` (index 0)
  - Press index: `1` (index 0)
  - Edition index: `1` (index 0)
  - Paper type: `STANDARD`
  - Price per copy: `60`
  - Number of copies: `5` (needs 3250 sheets)
  - Color print: `true`
- **Expected Output (after prints, load < 3250):**

  ```text
  Error: Insufficient paper load
  ```

- **Logger Output:**

  ```text
  [ERROR] services.PrintingPressService - Insufficient paper: required 3250, available [current] for edition Edition{...}
  ```

---

## Use Case 12: Handle Invalid Paper Load Configuration

**Requirement:** Обработка на грешки при невалидна конфигурация на хартия в печатните машини  
**Purpose:** Prevent printing with invalid press states.

### Attempt Print with Negative Current Paper Load

- **Input:**
  - Menu selection: `3` (Manage Printing Presses) → `5` (Print an item)
  - Print house index: `1` (index 0)
  - Press index: `1` (index 0, assume modified to `currentPaperLoad=-1` via update or setup)
  - Edition index: `1` (index 0)
  - Paper type: `STANDARD`
  - Price per copy: `60`
  - Number of copies: `5`
  - Color print: `true`
- **Expected Output:**

  ```text
  Error: Paper load cannot be above the maximum capacity
  ```

- **Logger Output:**

  ```text
  [ERROR] services.PrintingPressService - Invalid current paper load: -1
  [ERROR] services.PrintingPressService - Paper load capacity must be greater than the maximum paper load capacity of 5000
  ```

- **Note**: Assumes press was updated to an invalid state (e.g., `currentPaperLoad=-1`) after initial setup.

---

## Use Case 13: Handle Invalid Press Speed Configuration

**Requirement:** Обработка на грешки при невалидна скорост на печат в печатните машини  
**Purpose:** Prevent printing with invalid press configurations.

### Attempt Print with Zero Maximum Pages Per Minute

- **Input:**
  - Menu selection: `3` (Manage Printing Presses) → `5` (Print an item)
  - Print house index: `1` (index 0)
  - Press index: `1` (index 0, assume modified to `maximumPagesPerMinute=0` via update)
  - Edition index: `1` (index 0)
  - Paper type: `STANDARD`
  - Price per copy: `60`
  - Number of copies: `5`
  - Color print: `true`
- **Expected Output:**

  ```text
  Error: The maximum pages per minute printed, cannot be a negative value
  ```

- **Logger Output:**

  ```text
  [ERROR] services.PrintingPressService - Invalid max pages per minute: 0
  [ERROR] services.PrintingPressService - The maximum pages per minute printed, cannot be a negative value
  ```

- **Note**: Assumes press was updated to an invalid state (e.g., `maximumPagesPerMinute=0`) after initial setup.

---

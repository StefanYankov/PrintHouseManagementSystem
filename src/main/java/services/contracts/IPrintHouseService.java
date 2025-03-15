package services.contracts;

import data.models.EmployeeType;
import data.models.PrintHouse;

import java.math.BigDecimal;
import java.util.List;

/**
 * Defines operations for managing {@link PrintHouse} entities.
 */
public interface IPrintHouseService {
    /**
     * Creates a new print house with specified parameters.
     *
     * @param salaryIncrementPercentage percentage increment for employee salaries
     * @param paperIncrementPercentage  percentage increment for paper costs per size step
     * @param baseSalary                base salary for employees
     * @param incrementEligibleRoles    roles eligible for salary increments
     * @param revenueTarget             revenue target for salary increments
     * @param salesDiscountCount        number of copies before discount applies
     * @param salesDiscountPercentage   discount percentage for bulk copies
     * @return the created {@link PrintHouse}
     */
    PrintHouse createPrintHouse(BigDecimal salaryIncrementPercentage,
                                BigDecimal paperIncrementPercentage,
                                BigDecimal baseSalary,
                                List<EmployeeType> incrementEligibleRoles,
                                BigDecimal revenueTarget,
                                int salesDiscountCount,
                                BigDecimal salesDiscountPercentage);

    /**
     * Retrieves all managed print houses.
     *
     * @return a list of {@link PrintHouse} instances
     */
    List<PrintHouse> getAllPrintHouses();

    /**
     * Retrieves a print house by index.
     *
     * @param index the index of the print house
     * @return the {@link PrintHouse} at the specified index
     */
    PrintHouse getPrintHouse(int index);

    /**
     * Updates the attributes of an existing {@link PrintHouse} at the specified index.
     * Only provided (non-null) parameters are updated, allowing for partial modifications.
     * If the base salary is updated, all associated employees' salaries are also updated.
     *
     * @param index                     the index of the {@link PrintHouse} to update (0-based)
     * @param salaryIncrementPercentage the new salary increment percentage, or null to keep unchanged
     * @param paperIncrementPercentage  the new paper increment percentage, or null to keep unchanged
     * @param baseSalary                the new base salary, or null to keep unchanged
     * @param incrementEligibleRoles    the new list of eligible roles for salary increments, or null to keep unchanged
     * @param revenueTarget             the new revenue target, or null to keep unchanged
     * @param salesDiscountCount        the new sales discount count, or null to keep unchanged
     * @param salesDiscountPercentage   the new sales discount percentage, or null to keep unchanged
     */
    void updatePrintHouse(int index, BigDecimal salaryIncrementPercentage, BigDecimal paperIncrementPercentage,
                          BigDecimal baseSalary, List<EmployeeType> incrementEligibleRoles,
                          BigDecimal revenueTarget, Integer salesDiscountCount, BigDecimal salesDiscountPercentage);

    /**
     * Removes the {@link PrintHouse} at the specified index from the system.
     *
     * @param index the index of the {@link PrintHouse} to remove (0-based)
     */
    void removePrintHouse(int index);

    /**
     * Saves all print houses to a file.
     *
     * @param filePath the file path to save to
     */
    void saveAllPrintHouses(String filePath);

    /**
     * Loads print houses from a file, replacing current data.
     *
     * @param filePath the file path to load from
     */
    void loadAllPrintHouses(String filePath);
}
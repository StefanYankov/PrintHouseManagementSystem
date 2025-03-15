package services;

import data.models.*;
import services.contracts.*;
import utilities.exceptions.*;
import utilities.globalconstants.ExceptionMessages;
import utilities.globalconstants.ModelsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages {@link PrintHouse} entities, focusing solely on their lifecycle and persistence.
 */
public class PrintHouseService implements IPrintHouseService {
    private static final Logger logger = LoggerFactory.getLogger(PrintHouseService.class);
    private final List<PrintHouse> printHouses = new ArrayList<>();
    private final ISerializationService<PrintHouse> serializationService;

    public PrintHouseService(ISerializationService<PrintHouse> serializationService) {
        if (serializationService == null) {
            logger.error("Serialization service cannot be null");
            throw new IllegalArgumentException("Serialization service cannot be null");
        }
        this.serializationService = serializationService;
        logger.info("PrintHouseService initialized");
    }

    /** {@inheritDoc} */
    @Override
    public PrintHouse createPrintHouse(BigDecimal salaryIncrementPercentage,
                                       BigDecimal paperIncrementPercentage,
                                       BigDecimal baseSalary,
                                       List<EmployeeType> incrementEligibleRoles,
                                       BigDecimal revenueTarget,
                                       int salesDiscountCount,
                                       BigDecimal salesDiscountPercentage) {
        validateParameters(salaryIncrementPercentage, paperIncrementPercentage, baseSalary,
                incrementEligibleRoles, revenueTarget, salesDiscountCount, salesDiscountPercentage);
        PrintHouse printHouse = new PrintHouse(salaryIncrementPercentage, paperIncrementPercentage, baseSalary,
                incrementEligibleRoles, revenueTarget, salesDiscountCount, salesDiscountPercentage);
        printHouses.add(printHouse);
        logger.info("PrintHouse created: {}", printHouse);
        return printHouse;
    }

    /** {@inheritDoc} */
    @Override
    public List<PrintHouse> getAllPrintHouses() {
        logger.debug("Returning {} print houses", printHouses.size());
        return new ArrayList<>(printHouses);
    }

    /** {@inheritDoc} */
    @Override
    public PrintHouse getPrintHouse(int index) {
        if (index < 0 || index >= printHouses.size()) {
            logger.error("Invalid index: {}", index);
            throw new IllegalArgumentException("Invalid print house index: " + index);
        }
        return printHouses.get(index);
    }

    /** {@inheritDoc} */
    @Override
    public void updatePrintHouse(int index, BigDecimal salaryIncrementPercentage, BigDecimal paperIncrementPercentage,
                                 BigDecimal baseSalary, List<EmployeeType> incrementEligibleRoles,
                                 BigDecimal revenueTarget, Integer salesDiscountCount, BigDecimal salesDiscountPercentage) {
        PrintHouse printHouse = getPrintHouse(index);
        if (salaryIncrementPercentage != null && (salaryIncrementPercentage.compareTo(BigDecimal.ZERO) < 0 || salaryIncrementPercentage.compareTo(ModelsConstants.MAXIMUM_PERCENTAGE) > 0)) {
            logger.error("Invalid salary increment: {}", salaryIncrementPercentage);
            throw new InvalidIncrementPercentageException(ExceptionMessages.EMPLOYEE_SALARY_INCREMENT_PERCENTAGE_CANNOT_BE_NEGATIVE);
        }
        if (paperIncrementPercentage != null && (paperIncrementPercentage.compareTo(BigDecimal.ZERO) < 0 || paperIncrementPercentage.compareTo(ModelsConstants.MAXIMUM_PERCENTAGE) > 0)) {
            logger.error("Invalid paper increment: {}", paperIncrementPercentage);
            throw new InvalidIncrementPercentageException(ExceptionMessages.PAPER_INCREMENT_PERCENTAGE_CANNOT_BE_NEGATIVE);
        }
        if (baseSalary != null && (baseSalary.compareTo(BigDecimal.ZERO) <= 0 || baseSalary.compareTo(ModelsConstants.MAXIMUM_SALARY) > 0)) {
            logger.error("Invalid base salary: {}", baseSalary);
            throw new InvalidSalaryException(ExceptionMessages.BASE_SALARY_CANNOT_BE_A_NEGATIVE_NUMBER);
        }
        if (revenueTarget != null && revenueTarget.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error("Invalid revenue target: {}", revenueTarget);
            throw new InvalidRevenueTargetException(ExceptionMessages.REVENUE_CANNOT_BE_A_NEGATIVE_NUMBER);
        }
        if (salesDiscountCount != null && salesDiscountCount < 0) {
            logger.error("Invalid discount count: {}", salesDiscountCount);
            throw new InvalidDiscountCountException(ExceptionMessages.SALES_DISCOUNT_COUNT_CANNOT_BE_NEGATIVE);
        }
        if (salesDiscountPercentage != null && (salesDiscountPercentage.compareTo(BigDecimal.ZERO) <= 0 || salesDiscountPercentage.compareTo(BigDecimal.valueOf(100)) > 0)) {
            logger.error("Invalid discount percentage: {}", salesDiscountPercentage);
            throw new InvalidDiscountPercentageException(ExceptionMessages.SALES_DISCOUNT_PERCENTAGE_CANNOT_BE_NEGATIVE);
        }

        if (salaryIncrementPercentage != null) {
            printHouse.setEmployeeSalaryIncrementPercentage(salaryIncrementPercentage);
        }

        if (paperIncrementPercentage != null) {
            printHouse.setPaperIncrementPercentage(paperIncrementPercentage);
        }

        if (baseSalary != null) {
            printHouse.setBaseSalary(baseSalary);
        }

        if (incrementEligibleRoles != null) {
            printHouse.setIncrementEligibleRoles(incrementEligibleRoles);
        }

        if (revenueTarget != null) {
            printHouse.setRevenueTarget(revenueTarget);
        }

        if (salesDiscountCount != null) {
            printHouse.setSalesDiscountCount(salesDiscountCount);
        }

        if (salesDiscountPercentage != null) {
            printHouse.setSalesDiscountPercentage(salesDiscountPercentage);
        }
        logger.info("PrintHouse updated at index {}: {}", index, printHouse);
    }

    /** {@inheritDoc} */
    @Override
    public void removePrintHouse(int index) {
        PrintHouse removed = printHouses.remove(index);
        logger.info("PrintHouse removed: {}", removed);
    }

    /** {@inheritDoc} */
    @Override
    public void saveAllPrintHouses(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            logger.error("Invalid file path: {}", filePath);
            throw new IllegalArgumentException(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL);
        }
        serializationService.serialize(printHouses, filePath);
        logger.info("Saved {} print houses to {}", printHouses.size(), filePath);
    }

    /** {@inheritDoc} */
    @Override
    public void loadAllPrintHouses(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            logger.error("Invalid file path: {}", filePath);
            throw new IllegalArgumentException(ExceptionMessages.PRINT_HOUSE_CANNOT_BE_NULL);
        }
        List<PrintHouse> loaded = serializationService.deserialize(filePath);
        printHouses.clear();
        printHouses.addAll(loaded);
        logger.info("Loaded {} print houses from {}", printHouses.size(), filePath);
    }

    private void validateParameters(BigDecimal salaryIncrementPercentage, BigDecimal paperIncrementPercentage,
                                    BigDecimal baseSalary, List<EmployeeType> incrementEligibleRoles,
                                    BigDecimal revenueTarget, int salesDiscountCount, BigDecimal salesDiscountPercentage) {

        if (salaryIncrementPercentage == null) {
            logger.error(ExceptionMessages.EMPLOYEE_SALARY_INCREMENT_PERCENTAGE_CANNOT_BE_NULL);
            throw new InvalidIncrementPercentageException(ExceptionMessages.EMPLOYEE_SALARY_INCREMENT_PERCENTAGE_CANNOT_BE_NULL);
        }

        if (salaryIncrementPercentage.compareTo(BigDecimal.ZERO) < 0) {
            logger.error(ExceptionMessages.EMPLOYEE_SALARY_INCREMENT_PERCENTAGE_CANNOT_BE_A_NEGATIVE_NUMBER);
            throw new InvalidIncrementPercentageException(ExceptionMessages.EMPLOYEE_SALARY_INCREMENT_PERCENTAGE_CANNOT_BE_A_NEGATIVE_NUMBER);
        }

        if (paperIncrementPercentage == null) {
            logger.error(ExceptionMessages.PAPER_INCREMENT_PERCENTAGE_CANNOT_BE_NULL);
            throw new InvalidIncrementPercentageException(ExceptionMessages.PAPER_INCREMENT_PERCENTAGE_CANNOT_BE_NULL);
        }

        if (paperIncrementPercentage.compareTo(BigDecimal.ZERO) < 0) {
            logger.error(ExceptionMessages.PAPER_INCREMENT_PERCENTAGE_CANNOT_BE_NEGATIVE_NUMBER);
            throw new InvalidIncrementPercentageException(ExceptionMessages.PAPER_INCREMENT_PERCENTAGE_CANNOT_BE_NEGATIVE_NUMBER);
        }

        if (baseSalary == null) {
            logger.error(ExceptionMessages.BASE_SALARY_CANNOT_BE_NULL);
            throw new InvalidSalaryException(ExceptionMessages.BASE_SALARY_CANNOT_BE_NULL);
        }

        if (baseSalary.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error(ExceptionMessages.BASE_SALARY_CANNOT_BE_A_NEGATIVE_NUMBER);
            throw new InvalidSalaryException(ExceptionMessages.BASE_SALARY_CANNOT_BE_A_NEGATIVE_NUMBER);
        }

        if (incrementEligibleRoles == null) {
            logger.error(ExceptionMessages.INCREMENT_ELIGIBLE_ROLES_CANNOT_BE_NULL);
            throw new InvalidEmployeeException(ExceptionMessages.INCREMENT_ELIGIBLE_ROLES_CANNOT_BE_NULL);
        }
        if (revenueTarget == null) {
            logger.error(ExceptionMessages.REVENUE_TARGET_CANNOT_BE_NULL);
            throw new InvalidRevenueTargetException(ExceptionMessages.REVENUE_TARGET_CANNOT_BE_NULL);
        }
        if (revenueTarget.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error(ExceptionMessages.REVENUE_CANNOT_BE_A_NEGATIVE_NUMBER);
            throw new InvalidRevenueTargetException(ExceptionMessages.REVENUE_CANNOT_BE_A_NEGATIVE_NUMBER);
        }
        if (salesDiscountCount < 0) {
            logger.error(ExceptionMessages.SALES_DISCOUNT_COUNT_CANNOT_BE_NEGATIVE);
            throw new InvalidDiscountCountException(ExceptionMessages.SALES_DISCOUNT_COUNT_CANNOT_BE_NEGATIVE);
        }
        if (salesDiscountPercentage == null || salesDiscountPercentage.compareTo(BigDecimal.ZERO) <= 0) {
            logger.error(ExceptionMessages.SALES_DISCOUNT_PERCENTAGE_CANNOT_BE_NULL);
            throw new InvalidDiscountPercentageException(ExceptionMessages.SALES_DISCOUNT_PERCENTAGE_CANNOT_BE_NULL);
        }
    }
}
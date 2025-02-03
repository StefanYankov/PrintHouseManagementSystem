package org.PrintHouse.utilities.globalconstants;

public class ExceptionMessages {

    public static final String TITLE_CANNOT_BE_NULL = "Title cannot be null.";
    public static final String TITLE_CANNOT_BE_EMPTY = "Title cannot be whitespace.";
    public static final String TITLE_LENGTH_INVALID = "Title length must be between {0} and {1} characters.";
    public static final String NUMBER_OF_PAGES_INVALID = "Number of pages must be between {0} and {1}.";
    public static final String NUMBER_OF_PAGES_MUST_BE_GREATER_THAN_ZERO = "Number of pages must be greater than zero.";
    public static final String INVALID_PAGE_SIZE_INCREMENTAL_PERCENTAGE = "Incremental percentage must be greater than zero.";
    public static final String INVALID_PAGE_SIZE = "Page size cannot be null.";

    public static final String PAPER_TYPE_CANNOT_BE_NULL = "Paper type cannot be null.";
    public static final String PRICE_CANNOT_BE_NULL = "Price cannot be null.";
    public static final String PRICE_CANNOT_BE_A_NEGATIVE_NUMBER = "Price should be greater than zero.";
    public static final String PRICE_CANNOT_BE_GREATER_THAN_THE_MAXIMUM_PRICE = "Price should not be greater than the maximum price of {0}.";
    public static final String EDITION_CANNOT_BE_NULL = "Edition cannot be null.";

    public static final String PAPER_LOAD_CANNOT_BE_ZERO_OR_NEGATIVE_NUMBER = "Paper load cannot be zero or negative number.";
    public static final String PAPER_LOAD_CANNOT_BE_ABOVE_THE_MAXIMUM_CAPACITY =
            "Paper load capacity must be greater than the maximum paper load capacity of {0}.";
    public static final String INCOMPATIBLE_COLOR_TYPE = "Incompatible color type: " +
            "- Printer prints color: {0};" +
            "- Requested print type color: {1} ";
    public static final String INSUFFICIENT_PAPER_LOAD =
            "Insufficient paper load - you requested {0} pages to print, but the printing press has only {1} available .";
    public static final String PAPER_LOAD_CANNOT_EXCEED_MAXIMUM_AVAILABLE_LOADING_CAPACITY = "Paper load maximum capacity cannot exceed {0}";
    public static final String MAX_PAGES_PER_MINUTE_PRINTED_CANNOT_BE_A_NEGATIVE_VALUE = "The maximum pages per minute printed, cannot be a negative value";
    public static final String MAX_PAGES_PER_MINUTE_PRINTED_CANNOT_EXCEED_MAXIMUM_ALLOWED = "The maximum pages per minute printed, cannot exceed maximum {0}";
    public static final String PRINTING_PRESS_IS_NOT_PART_OF_THIS_PRINTING_HOUSE = "The printing press {0} is not part of the printing house.";
    public static final String PRINTING_PRESS_CANNOT_BE_NULL = "The printing press cannot be null.";

    public static final String PAPER_INCREMENT_PERCENTAGE_CANNOT_BE_NULL = "Paper increment percentage cannot be null.";
    public static final String PAPER_INCREMENT_PERCENTAGE_CANNOT_BE_NEGATIVE_NUMBER = "Paper increment percentage cannot be negative number.";
    public static final String PAPER_COST_CANNOT_BE_NULL = "Paper cost cannot be null.";
    public static final String PAPER_COST_MUST_BE_A_POSITIVE_NUMBER = "Paper cost must be a positive number.";

    public static final String BASE_SALARY_CANNOT_BE_NULL = "Base salary cannot be null.";
    public static final String BASE_SALARY_CANNOT_BE_A_NEGATIVE_NUMBER = "Base salary cannot be a negative number.";

    public static final String EMPLOYEE_SALARY_INCREMENT_PERCENTAGE_CANNOT_BE_NULL = "Employee salary increment percentage cannot be null.";
    public static final String REVENUE_CANNOT_BE_NULL = "Revenue cannot be null.";
    public static final String REVENUE_CANNOT_BE_A_NEGATIVE_NUMBER = "Revenue cannot be a negative number.";
    public static final String SALES_COUNT_CANNOT_BE_A_NEGATIVE_NUMBER = "Sales count cannot be a negative number.";
    public static final String SALES_DISCOUNT_PERCENTAGE_CANNOT_BE_NULL = "Sales discount percentage cannot be null.";
    public static final String INCREMENT_ELIGIBLE_ROLES_CANNOT_BE_NULL = "Increment eligible roles cannot be null.";
    public static final String EMPLOYEE_SALARY_INCREMENT_PERCENTAGE_CANNOT_BE_A_NEGATIVE_NUMBER = "Employee salary increment percentage cannot be a negative number.";
    public static final String EMPLOYEE_CANNOT_BE_NULL = "Employee cannot be null.";
    public static final String EMPLOYEE_IS_ALREADY_ADDED_IN_PRINT_HOUSE = "Employee is already added in print house.";
    public static final String COPIES_COUNT_NEED_TO_BE_GREATER_THAN_ZERO = "Copies count must be greater than zero.";
}

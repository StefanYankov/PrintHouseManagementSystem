package utilities;


import java.time.DateTimeException;
import java.time.LocalDate;
/**
 * Utility class for validating Bulgarian EGN (Personal Identification Number).
 */
public class EgnValidator {
    private static final String EGN_PATTERN = "[0-9]{2}[0,1,2,4][0-9][0-9]{2}[0-9]{4}";
    private static final int[] WEIGHTS = {2, 4, 8, 5, 10, 9, 7, 3, 6};

    /**
     * Validates an EGN string for format, date validity, and checksum.
     *
     * @param egn The EGN string to validate (10 digits).
     * @return true if the EGN is valid, false otherwise.
     */
    public static boolean isValidEGN(String egn) {
        // Check for nullity
        if (egn == null) {
            return false;
        }

        if (egn.length() < 10){
            return false;
        }

        // Check format against regex (YY[0,1,2,4]MDDSSSC)
        if (!egn.matches(EGN_PATTERN)) {
            return false;
        }

        // Parse year, month, and day from EGN
        int year = Integer.parseInt(egn.substring(0, 2));
        int month = Integer.parseInt(egn.substring(2, 4));
        int day = Integer.parseInt(egn.substring(4, 6));

        // Adjust year based on month prefix (20 for 1800s, 40 for 2000s, else 1900s)
        if (month > 40) {
            month -= 40;
            year += 2000;
        } else if (month > 20) {
            month -= 20;
            year += 1800;
        } else {
            year += 1900;
        }

        // Validate date
        try {
            LocalDate.of(year, month, day);
        } catch (DateTimeException e) {
            return false;
        }

        // Calculate and verify checksum
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (egn.charAt(i) - '0') * WEIGHTS[i];
        }
        int checksum = sum % 11;
        if (checksum == 10) checksum = 0;
        if (checksum != (egn.charAt(9) - '0')) {
            return false;
        }

        // All checks passed
        return true;
    }
}
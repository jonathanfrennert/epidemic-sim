package org.epi.util;

/** Utility class with helper methods for common error checks.*/
public class Error {

    /** Error message tag.*/
    public static final String ERROR_TAG = "ERROR:";

    /** {@link NullPointerException} message format.*/
    private static final String NULL_MSG = ERROR_TAG + " Given %s must not be null.";

    /** Outside interval message format.*/
    private static final String INTERVAL_MSG = ERROR_TAG + " Given %s must be between %d and %d but is: %.2f";

    //---------------------------- Null checks ----------------------------

    /**
     * Return a {@link NullPointerException} message with the given object name.
     *
     * @param objectName the name of the object which is null
     * @return a {@link NullPointerException} message personalised with the object name
     */
    public static String getNullMsg(String objectName) {
        return String.format(NULL_MSG, objectName);
    }

    //---------------------------- Comparison & Interval checks ----------------------------

    /**
     * Return an outside-of-interval message with the given number category.
     *
     * @param numberCategory the number category for the value, i.e., probability or percentage etc.
     * @param lowest the low endpoint of the interval
     * @param highest the high endpoint of the interval
     * @param value the value which should be within the interval
     * @return a outside-of-interval message personalised with the number category and interval endpoints
     */
    public static String getIntervalMsg(String numberCategory, Number lowest, Number highest, Number value) {
        return String.format(INTERVAL_MSG, numberCategory, lowest.intValue(), highest.intValue(), value.doubleValue());
    }

    /**
     * Check if a given number is within an interval.
     *
     * @param numberCategory the number category for the value, i.e., probability or percentage etc.
     * @param lowest the low endpoint of the interval
     * @param highest the high endpoint of the interval
     * @param value the value which should be within the interval
     * @throws IllegalArgumentException if the given value is lower than lowest or higher than highest
     */
    public static void intervalCheck(String numberCategory, Number lowest, Number highest, Number value) {
        if (value.doubleValue() < lowest.doubleValue() || value.doubleValue() > highest.doubleValue()) {
            throw new IllegalArgumentException(getIntervalMsg(numberCategory, lowest, highest, value));
        }
    }

    /**
     * Check if the given number is non-negative.
     *
     * @param number a number
     * @throws IllegalArgumentException if the given number is non-negative
     */
    public static void nonNegativeCheck(Number number) {
        if (number.doubleValue() < 0) {
            throw new IllegalArgumentException(ERROR_TAG + " Given number is negative: " + number);
        }
    }

}

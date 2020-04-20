package org.epi.util;

import java.util.Objects;

/** */
public class ErrorUtil {

    /** {@link NullPointerException} message format.*/
    private static final String NULL_MSG = "ERROR: Given %s must not be null.";

    /**
     * Return a {@link NullPointerException} message with the given object name.
     *
     * @param objectName the name of the object which is null
     * @return a {@link NullPointerException} message personalised with the object name
     * @throws NullPointerException if the given objectName is null
     */
    public static String getNullMsg(String objectName) {
        Objects.requireNonNull(objectName, String.format(NULL_MSG, "object name"));
        return String.format(NULL_MSG, objectName);
    }

}

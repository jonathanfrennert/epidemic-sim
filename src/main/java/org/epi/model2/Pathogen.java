package org.epi.model2;

import javafx.beans.property.DoubleProperty;

public class Pathogen {


    /** This pathogen's immunity rate.*/
    private final DoubleProperty immunityRate;

    /** The duration of this pathogen's immunity in seconds.*/
    private final DoubleProperty immunityDuration;

    /**
     * Getter for {@link #immunityRate}
     *
     * @return {@link #immunityRate}
     */
    public double getImmunityRate() {
        return immunityRate.get();
    }

    /**
     * Getter for {@link #immunityDuration}
     *
     * @return {@link #immunityDuration}
     */
    public double getImmunityDuration() {
        return immunityDuration.get();
    }

}

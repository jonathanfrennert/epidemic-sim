package org.epi.model;

import org.epi.util.Error;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;

/** Wrapper class for the disease parameters.*/
public class Disease {

    /** The probability of a transmission occurring in effective contact.*/
    private final DoubleProperty transmissionRisk;

    /** The probability of an infected person dying from the disease.*/
    private final DoubleProperty fatalityRate;

    /** The total duration of the disease in number of seconds.*/
    private final DoubleProperty totalDuration;

    /**
     * The constructor for a disease.
     *
     * @param transmissionRisk The probability of a transmission occurring
     * @param fatalityRate The probability of an infected person dying from the disease
     * @param totalDuration The total duration of the disease in number of seconds
     * @throws IllegalArgumentException if the given transmissionRisk or fatalityRate is less than {@value Error#MIN_PROB} or more than
     *                                  {@value Error#MAX_PROB} and if the totalDuration is negative
     */
    public Disease(double transmissionRisk, double fatalityRate, double totalDuration) {
        Error.probabilityCheck(transmissionRisk);
        Error.probabilityCheck(fatalityRate);
        Error.nonNegativeCheck(totalDuration);

        this.transmissionRisk = new SimpleDoubleProperty(transmissionRisk);
        this.fatalityRate = new SimpleDoubleProperty(fatalityRate);
        this.totalDuration = new SimpleDoubleProperty(totalDuration);
    }

    //---------------------------- Getters & Setters ----------------------------

    /**
     * Getter for {@link Disease#transmissionRisk}
     *
     * @return {@link Disease#transmissionRisk}
     */
    public double getTransmissionRisk() {
        return transmissionRisk.get();
    }

    /**
     * Getter for {@link Disease#transmissionRisk} {@link DoubleProperty}
     *
     * @return {@link Disease#transmissionRisk}
     */
    public DoubleProperty transmissionRiskProperty() {
        return transmissionRisk;
    }

    /**
     * Setter for {@link Disease#transmissionRisk}
     *
     * @param transmissionRisk the probability of a transmission occurring in effective contact
     * @throws IllegalArgumentException if the given transmissionRisk is less than {@value Error#MIN_PROB} or more than
     *                                  {@value Error#MAX_PROB}
     */
    public void setTransmissionRisk(double transmissionRisk) {
        Error.probabilityCheck(transmissionRisk);

        this.transmissionRisk.set(transmissionRisk);
    }

    /**
     * Getter for {@link Disease#fatalityRate}
     *
     * @return {@link Disease#fatalityRate}
     */
    public double getFatalityRate() {
        return fatalityRate.get();
    }

    /**
     * Getter for {@link Disease#fatalityRate} {@link DoubleProperty}
     *
     * @return {@link Disease#fatalityRate}
     */
    public DoubleProperty fatalityRateProperty() {
        return fatalityRate;
    }

    /**
     * Setter for {@link Disease#transmissionRisk}
     *
     * @param fatalityRate the probability of an infected person dying from the disease
     * @throws IllegalArgumentException if the fatalityRate is less than {@value Error#MIN_PROB} or more than
     *                                  {@value Error#MAX_PROB}
     */
    public void setFatalityRate(double fatalityRate) {
        Error.probabilityCheck(fatalityRate);

        this.fatalityRate.set(fatalityRate);
    }

    /**
     * Getter for {@link Disease#totalDuration}
     *
     * @return {@link Disease#totalDuration}
     */
    public double getTotalDuration() {
        return totalDuration.get();
    }

    /**
     * Getter for {@link Disease#totalDuration} {@link IntegerProperty}
     *
     * @return {@link Disease#totalDuration}
     */
    public DoubleProperty totalDurationProperty() {
        return totalDuration;
    }

    /**
     * Setter for {@link Disease#totalDuration}
     *
     * @param totalDuration the total duration of the disease in number of seconds
     * @throws IllegalArgumentException if the given totalDuration is negative
     */
    public void setTotalDuration(double totalDuration) {
        Error.nonNegativeCheck(totalDuration);

        this.totalDuration.set(totalDuration);
    }

}

package org.epi.model;

import org.epi.util.ErrorUtil;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/** Wrapper class for the disease parameters.*/
public class Disease {

    /** The probability of a transmission occurring in effective contact.*/
    private final DoubleProperty transmissionRisk;

    /** The probability of an infected person dying from the disease.*/
    private final DoubleProperty fatalityRate;

    /** The total duration of the disease in number of frames.*/
    private final IntegerProperty totalDuration;

    /**
     * The constructor for a disease.
     *
     * @param transmissionRisk The probability of a transmission occurring
     * @param fatalityRate The probability of an infected person dying from the disease
     * @param totalDuration The total duration of the disease in number of frames
     * @throws IllegalArgumentException if the given transmissionRisk or fatalityRate is less than {@value ErrorUtil#MIN_PROB} or more than
     *                                  {@value ErrorUtil#MAX_PROB} and if the totalDuration is negative
     */
    public Disease(double transmissionRisk, double fatalityRate, int totalDuration) {
        ErrorUtil.probabilityCheck(transmissionRisk);
        ErrorUtil.probabilityCheck(fatalityRate);
        ErrorUtil.nonNegativeCheck(totalDuration);

        this.transmissionRisk = new SimpleDoubleProperty(transmissionRisk);
        this.fatalityRate = new SimpleDoubleProperty(fatalityRate);
        this.totalDuration = new SimpleIntegerProperty(totalDuration);
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
     * @throws IllegalArgumentException if the given transmissionRisk is less than {@value ErrorUtil#MIN_PROB} or more than
     *                                  {@value ErrorUtil#MAX_PROB}
     */
    public void setTransmissionRisk(double transmissionRisk) {
        ErrorUtil.probabilityCheck(transmissionRisk);

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
     * @throws IllegalArgumentException if the fatalityRate is less than {@value ErrorUtil#MIN_PROB} or more than
     *                                  {@value ErrorUtil#MAX_PROB}
     */
    public void setFatalityRate(double fatalityRate) {
        ErrorUtil.probabilityCheck(fatalityRate);

        this.fatalityRate.set(fatalityRate);
    }

    /**
     * Getter for {@link Disease#totalDuration}
     *
     * @return {@link Disease#totalDuration}
     */
    public int getTotalDuration() {
        return totalDuration.get();
    }

    /**
     * Getter for {@link Disease#totalDuration} {@link IntegerProperty}
     *
     * @return {@link Disease#totalDuration}
     */
    public IntegerProperty totalDurationProperty() {
        return totalDuration;
    }

    /**
     * Setter for {@link Disease#totalDuration}
     *
     * @param totalDuration the total duration of the disease in number of frames
     * @throws IllegalArgumentException if the given totalDuration is negative
     */
    public void setTotalDuration(int totalDuration) {
        ErrorUtil.nonNegativeCheck(totalDuration);

        this.totalDuration.set(totalDuration);
    }

}

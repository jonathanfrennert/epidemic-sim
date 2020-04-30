package org.epi.model2;

import org.epi.util.Probability;
import org.epi.util.Error;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/** A simple model of a world.
 * The class is used as a graphical representation of the world in the simulator.*/
public class World {

    /** The width of the city in pixels.*/
    private static final double CITY_WIDTH = 500;
    /** The height of the city in pixels.*/
    private static final double CITY_HEIGHT = 500;
    /** The width of the quarantine in pixels.*/
    private static final double QUARANTINE_WIDTH = 500;
    /** The height of the quarantine in pixels.*/
    private static final double QUARANTINE_HEIGHT = 500;

    /** The city in this world.*/
    private final Location city;

    /** The quarantine in this world.*/
    private final Location quarantine;

    /** The total number of seconds passed in this world.*/
    private final DoubleProperty totalElapsedSeconds;

    /** The probability of an infected person being detected in testing.*/
    private final DoubleProperty detectionRate;

    /** How often testing occurs in this world by number of seconds between testings.*/
    private final DoubleProperty testingFrequency;

    //---------------------------- Constructor ----------------------------

    /**
     * Create a world.
     *
     * @param detectionRate the probability of an infected person being detected in testing
     * @param testingFrequency how often testing occurs in this world by number of seconds between testings
     * @throws IllegalArgumentException if the given detection rate is less than {@value Probability#MIN_PROB} or more than
     *                                  {@value Probability#MAX_PROB} or if the testing frequency is negative
     */
    public World(double detectionRate, double testingFrequency) {
        Probability.probabilityCheck(detectionRate);
        Error.nonNegativeCheck(testingFrequency);

        this.city = new Location(CITY_WIDTH, CITY_HEIGHT);
        this.quarantine = new Location(QUARANTINE_WIDTH, QUARANTINE_HEIGHT);
        this.totalElapsedSeconds = new SimpleDoubleProperty(0);
        this.detectionRate = new SimpleDoubleProperty(detectionRate);
        this.testingFrequency = new SimpleDoubleProperty(testingFrequency);
    }

    //---------------------------- Simulator actions ----------------------------


    //---------------------------- Getters & Setters ----------------------------

    /**
     * Getter for {@link #city}.
     *
     * @return {@link #city}
     */
    public Location getCity() {
        return city;
    }

    /**
     * Getter for {@link #quarantine}.
     *
     * @return {@link #quarantine}
     */
    public Location getQuarantine() {
        return quarantine;
    }

    /**
     * Getter for {@link #totalElapsedSeconds}.
     *
     * @return {@link #totalElapsedSeconds}
     */
    public double getTotalElapsedSeconds() {
        return totalElapsedSeconds.get();
    }

    /**
     * Getter for {@link #totalElapsedSeconds} property.
     *
     * @return {@link #totalElapsedSeconds} property
     */
    public DoubleProperty totalElapsedSecondsProperty() {
        return totalElapsedSeconds;
    }

    /**
     * Getter for {@link #detectionRate}.
     *
     * @return {@link #detectionRate}
     */
    public double getDetectionRate() {
        return detectionRate.get();
    }

    /**
     * Getter for {@link #detectionRate} property.
     *
     * @return {@link #detectionRate} property
     */
    public DoubleProperty detectionRateProperty() {
        return detectionRate;
    }

    /**
     * Setter for {@link #detectionRate}.
     *
     * @throws IllegalArgumentException if the given parameter isless than {@value Probability#MIN_PROB} or more than
     *                                  {@value Probability#MAX_PROB}
     */
    public void setDetectionRate(double detectionRate) {
        Probability.probabilityCheck(detectionRate);
        this.detectionRate.set(detectionRate);
    }

    /**
     * Getter for {@link #testingFrequency}.
     *
     * @return {@link #testingFrequency}
     */
    public double getTestingFrequency() {
        return testingFrequency.get();
    }

    /**
     * Getter for {@link #testingFrequency} property.
     *
     * @return {@link #testingFrequency} property
     */
    public DoubleProperty testingFrequencyProperty() {
        return testingFrequency;
    }

    /**
     * Setter for {@link #testingFrequency}.
     *
     * @throws IllegalArgumentException if the given parameter is negative
     */
    public void setTestingFrequency(double testingFrequency) {
        Error.nonNegativeCheck(testingFrequency);
        this.testingFrequency.set(testingFrequency);
    }

}


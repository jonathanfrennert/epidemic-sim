package org.epi.model;

import org.epi.util.Probability;
import org.epi.util.Error;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.ArrayList;
import java.util.List;

/** A simple model of a world.
 * The class is used as a graphical representation of the world in the simulator.*/
public class World {

    /** The width of the city in pixels.*/
    private static final int CITY_WIDTH = 500;
    /** The height of the city in pixels.*/
    private static final int CITY_HEIGHT = 200;
    /** The width of the quarantine in pixels.*/
    private static final int QUARANTINE_WIDTH = 300;
    /** The height of the quarantine in pixels.*/
    private static final int QUARANTINE_HEIGHT = 100;

    /** The minimum population required for the simulation to run.*/
    public static final int MIN_POPULATION = 1;
    /** Maximum number of humans that can be handled without frame performance
     * issues and humans going over location edges.*/
    public static final int MAX_POPULATION = 300;

    /** The initial time offset for the total elapsed seconds, such that testing does not occur at 0 seconds.*/
    private static final double OFFSET = 1 / 1000_000_000.00;

    /** The city in this world.*/
    private final Location city;

    /** The quarantine in this world.*/
    private final Location quarantine;

    /** The total number of seconds passed in this world.*/
    private final DoubleProperty totalElapsedSeconds;

    /** The population total for this simulator.*/
    private final IntegerProperty populationTotal;

    /** The sick total for this simulator.*/
    private final IntegerProperty sickTotal;

    /** The probability of an sick human being detected in testing.*/
    private final DoubleProperty detectionRate;

    /** How often testing occurs in this world by number of seconds between testings.*/
    private final DoubleProperty testingFrequency;

    //---------------------------- Constructor ----------------------------

    /**
     * Create a world.
     *
     * @param populationTotal the population total
     * @param sickTotal the number of sick in the population
     * @param detectionRate the probability of a sick person being detected in testing
     * @param testingFrequency how often testing occurs in this world by number of seconds between testings
     * @throws IllegalArgumentException if the given detection rate is less than {@value Probability#MIN_PROB} or more than
     *                                  {@value Probability#MAX_PROB}, if the testing frequency is negative,
     *                                  if the population total is less than {@value MIN_POPULATION}
     *                                  or larger than the {@value MAX_POPULATION}, if the sick total is less than
     *                                  {@value MIN_POPULATION} or more than the population total
     */
    public World(int populationTotal, int sickTotal, double detectionRate, double testingFrequency) {
        Probability.probabilityCheck(detectionRate);
        Error.nonNegativeCheck(testingFrequency);
        Error.intervalCheck("population", MIN_POPULATION, MAX_POPULATION, populationTotal);
        Error.intervalCheck("sick population", MIN_POPULATION, populationTotal, sickTotal);

        this.city = new Location(CITY_WIDTH, CITY_HEIGHT);
        this.quarantine = new Location(QUARANTINE_WIDTH, QUARANTINE_HEIGHT);
        this.totalElapsedSeconds = new SimpleDoubleProperty(OFFSET);

        this.populationTotal = new SimpleIntegerProperty(populationTotal);
        this.sickTotal = new SimpleIntegerProperty(sickTotal);
        this.detectionRate = new SimpleDoubleProperty(detectionRate);
        this.testingFrequency = new SimpleDoubleProperty(testingFrequency);
    }

    //---------------------------- Simulator actions ----------------------------

    /**
     * Let time pass in the world and perform tests routinely.
     *
     * @param elapsedSeconds the number of seconds elapsed since this world was last updated
     * @throws IllegalArgumentException if the given parameter is negative
     */
    public void live(double elapsedSeconds) {
        Error.nonNegativeCheck(elapsedSeconds);

        double oldValue = totalElapsedSeconds.get();
        double newValue = oldValue + elapsedSeconds;

        boolean isTesting = Math.ceil(oldValue / testingFrequency.get()) <= Math.floor(newValue/ testingFrequency.get());

        if (isTesting) {
            test();
        }

        totalElapsedSeconds.set(newValue);
    }

    /**
     * Test the city population for the pathogen, and for those who test positive, send them to the quarantine.
     */
    private void test() {
        List<Human> quarantined = new ArrayList<>();

        for (Human testSubject : city.getPopulation()) {
            boolean isDetected = Probability.chance(detectionRate.get());

            if (testSubject.isSick() && isDetected) {
                quarantined.add(testSubject);
            }
        }

        quarantined.forEach(sick -> sick.setLocation(quarantine));
    }

    /**
     * Call collision methods for the city and quarantine.
     */
    public void collisions() {
        city.updateHash();
        city.wallCollisions();

        quarantine.updateHash();
        quarantine.wallCollisions();
    }

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
     * Getter for {@link #populationTotal}.
     *
     * @return {@link #populationTotal}
     */
    public int getPopulationTotal() {
        return populationTotal.get();
    }

    /**
     * Getter for {@link #populationTotal} property.
     *
     * @return {@link #populationTotal}
     */
    public IntegerProperty populationTotalProperty() {
        return populationTotal;
    }

    /**
     * Setter for {@link #populationTotal}. If the sick total is less than the population total, it is set
     * to the population total as well.
     *
     * @throws IllegalArgumentException if the population total is less than {@value MIN_POPULATION}
     *                                  or larger than the {@value MAX_POPULATION}
     */
    public void setPopulationTotal(int populationTotal) {
        Error.intervalCheck("population", MIN_POPULATION, MAX_POPULATION, populationTotal);

        if(populationTotal > sickTotal.get()) {
            sickTotal.set(populationTotal);
        }

        this.populationTotal.set(populationTotal);
    }

    /**
     * Getter for {@link #sickTotal}.
     *
     * @return {@link #sickTotal}
     */
    public int getSickTotal() {
        return sickTotal.get();
    }

    /**
     * Getter for {@link #sickTotal} property.
     *
     * @return {@link #sickTotal} property
     */
    public IntegerProperty sickTotalProperty() {
        return sickTotal;
    }

    /**
     * Setter for {@link #sickTotal}.
     *
     * @throws IllegalArgumentException  if the sick total is less than
     *                                  {@value MIN_POPULATION} or more than the population total
     */
    public void setSickTotal(int sickTotal) {
        Error.intervalCheck("sick population", MIN_POPULATION, populationTotal.get(), sickTotal);
        this.sickTotal.set(sickTotal);
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
     * @throws IllegalArgumentException if the given parameter is less than {@value Probability#MIN_PROB}
     *                                  or more than {@value Probability#MAX_PROB}
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


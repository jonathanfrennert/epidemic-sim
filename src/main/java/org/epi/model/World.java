package org.epi.model;

import org.epi.util.Probability;
import org.epi.util.Error;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/** Wrapper class for the world parameters.*/
public class World {

    /** The number of humans in the initial population.*/
    private final IntegerProperty population;

    /** The probability of human social distancing.*/
    private final DoubleProperty socialDistProb;

    /** The lowest possible population count.*/
    private static final int MIN_POPULATION = 1;

    /**
     * The constructor for a world.
     *
     * @param population the number of humans in the initial population
     * @param socialDistProb the probability of someone social distancing
     * @throws IllegalArgumentException if the given population is negative or above {@link Simulator#MAX_POPULATION} the socialDistProb is less than
     *                                  {@value Probability#MIN_PROB} or more than {@value Probability#MAX_PROB}
     */
    public World(int population, double socialDistProb) {
        Error.intervalCheck("population", MIN_POPULATION, Simulator.MAX_POPULATION, population);
        Probability.probabilityCheck(socialDistProb);

        this.population = new SimpleIntegerProperty(population);
        this.socialDistProb = new SimpleDoubleProperty(socialDistProb);
    }

    //---------------------------- Getters & Setters ----------------------------

    /**
     * Getter for {@link World#population}
     *
     * @return {@link World#population}
     */
    public int getPopulationCount() {
        return population.get();
    }

    /**
     * Getter for {@link World#population} {@link DoubleProperty}
     *
     * @return {@link World#population}
     */
    public IntegerProperty populationProperty() {
        return population;
    }

    /**
     * Getter for {@link World#socialDistProb}
     *
     * @return {@link World#socialDistProb}
     */
    public double getSocialDistProb() {
        return socialDistProb.get();
    }

    /**
     * Getter for {@link World#socialDistProb} {@link DoubleProperty}
     *
     * @return {@link World#socialDistProb}
     */
    public DoubleProperty socialDistProbProperty() {
        return socialDistProb;
    }

}

package org.epi.model;

import org.epi.util.ErrorUtil;

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

    /**
     * The constructor for a world.
     *
     * @param population the number of humans in the initial population
     * @param socialDistProb the probability of someone social distancing
     * @throws IllegalArgumentException if the given population is negative or the socialDistProb is less than
     *                                  {@value ErrorUtil#MIN_PROB} or more than {@value ErrorUtil#MAX_PROB}
     */
    public World(int population, double socialDistProb) {
        ErrorUtil.nonNegativeCheck(population);
        ErrorUtil.probabilityCheck(socialDistProb);

        this.population = new SimpleIntegerProperty(population);
        this.socialDistProb = new SimpleDoubleProperty(socialDistProb);
    }

    //---------------------------- Getters & Setters ----------------------------

    /**
     * Getter for {@link World#population}
     *
     * @return {@link World#population}
     */
    public int getPopulation() {
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

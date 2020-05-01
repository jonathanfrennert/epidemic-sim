package org.epi.util;

import org.epi.model2.Behaviour;

import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.distribution.EnumeratedDistribution;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.List;

/**
 * A utility class for distributing behaviour when creating a human population.
 */
public class BehaviourDistribution {

    /** The behaviour distribution.*/
    private final EnumeratedDistribution<Behaviour> behaveDist;

    /** The proportion of normal behaviour.*/
    private final DoubleProperty normalProp;

    /** The proportion of social distancing behaviour.*/
    private final DoubleProperty distancingProp;

    /** The proportion of avoidant behaviour.*/
    private final DoubleProperty avoidantProp;

    //---------------------------- Constructor ----------------------------

    /**
     * Create a behaviour distribution. Distribution is relative to the given proportions.
     *
     * @param normalProp the proportion of normal behaviour
     * @param distancingProp the proportion of social distancing behaviour
     * @param avoidantProp The proportion of avoidant behaviour
     */
    public BehaviourDistribution(double normalProp, double distancingProp, double avoidantProp) {
        this.normalProp = new SimpleDoubleProperty(normalProp);
        this.distancingProp = new SimpleDoubleProperty(distancingProp);
        this.avoidantProp = new SimpleDoubleProperty(avoidantProp);

        Pair<Behaviour, Double> normal = new Pair<>(new NormalBehaviour(),normalProp);
        Pair<Behaviour, Double> distancing = new Pair<>(new DistancingBehaviour(),distancingProp);
        Pair<Behaviour, Double> avoidant = new Pair<>(new AvoidantBehaviour(), avoidantProp);

        this.behaveDist = new EnumeratedDistribution<>(List.of(normal, distancing, avoidant));
    }

    /**
     * Check if a summation of values equals a value.
     *
     * @param sum a sum
     * @param values a list of numbers
     * @throws IllegalArgumentException if the given values do not add up to the sum
     */
    private void sumCheck(double sum, List<Number> values) {
        double result = values.stream().mapToDouble(Number::doubleValue).sum();

        if (result != sum) {
            throw new IllegalArgumentException(Error.ERROR_TAG + " The given values should add up to " + sum +
                    "but equal: " + result);
        }
    }

    //---------------------------- Simulator action ----------------------------

    /**
     * Sample the behaviour distribution.
     *
     * @return a behaviour
     */
    public Behaviour sample() {
        return behaveDist.sample();
    }

    //---------------------------- Getters & Setters ----------------------------

    /**
     * Getter for {@link #normalProp}.
     *
     * @return {@link #normalProp}
     */
    public double getNormalProp() {
        return normalProp.get();
    }

    /**
     * Getter for {@link #normalProp} property.
     *
     * @return {@link #normalProp} property
     */
    public DoubleProperty normalPropProperty() {
        return normalProp;
    }

    /**
     * Getter for {@link #distancingProp}.
     *
     * @return {@link #distancingProp}
     */
    public double getDistancingProp() {
        return distancingProp.get();
    }

    /**
     * Getter for {@link #distancingProp} property.
     *
     * @return {@link #distancingProp} property
     */
    public DoubleProperty distancingPropProperty() {
        return distancingProp;
    }

    /**
     * Getter for {@link #avoidantProp}.
     *
     * @return {@link #avoidantProp}
     */
    public double getAvoidantProp() {
        return avoidantProp.get();
    }

    /**
     * Getter for {@link #avoidantProp} property.
     *
     * @return {@link #avoidantProp} property
     */
    public DoubleProperty avoidantPropProperty() {
        return avoidantProp;
    }

}

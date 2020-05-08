package org.epi.model;

import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.distribution.EnumeratedDistribution;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.List;

import static org.epi.model.Behaviour.AVOIDANT;
import static org.epi.model.Behaviour.DISTANCING;
import static org.epi.model.Behaviour.NORMAL;

/**
 * Utilise a behaviour distribution when creating a human population.
 */
public class BehaviourDistribution {

    /** The behaviour distribution.*/
    private EnumeratedDistribution<Behaviour> behaveDist;

    /** The probability of normal behaviour.*/
    private final DoubleProperty normalProp;

    /** The probability of social distancing behaviour.*/
    private final DoubleProperty distancingProp;

    /** The probability of avoidant behaviour.*/
    private final DoubleProperty avoidantProp;

    //---------------------------- Constructor ----------------------------

    /**
     * Create a behaviour distribution. Inputs normalises proportions to probabilities.
     *
     * @param normalProp the proportion of normal behaviour
     * @param distancingProp the proportion of social distancing behaviour
     * @param avoidantProp The proportion of avoidant behaviour
     * @throws org.apache.commons.math3.exception.NotPositiveException if any of the proportions are negative.
     * @throws org.apache.commons.math3.exception.NotFiniteNumberException if any of the proportions are infinite.
     * @throws org.apache.commons.math3.exception.NotANumberException if any of the proportions are NaN.
     * @throws org.apache.commons.math3.exception.MathArithmeticException all of the proportions are 0.
     */
    public BehaviourDistribution(double normalProp, double distancingProp, double avoidantProp) {
        this.normalProp = new SimpleDoubleProperty(normalProp);
        this.distancingProp = new SimpleDoubleProperty(distancingProp);
        this.avoidantProp = new SimpleDoubleProperty(avoidantProp);

        Pair<Behaviour, Double> normal = new Pair<>(NORMAL, this.normalProp.get());
        Pair<Behaviour, Double> distancing = new Pair<>(DISTANCING, this.distancingProp.get());
        Pair<Behaviour, Double> avoidant = new Pair<>(AVOIDANT, this.avoidantProp.get());

        this.behaveDist = new EnumeratedDistribution<>(List.of(normal, distancing, avoidant));
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
     * Setter for {@link #normalProp}.
     *
     * @param normalProp {@link #normalProp}
     * @throws org.apache.commons.math3.exception.NotPositiveException if the given parameter is negative.
     * @throws org.apache.commons.math3.exception.NotFiniteNumberException if the given parameter is infinite.
     * @throws org.apache.commons.math3.exception.NotANumberException if the given parameter is NaN.
     * @throws org.apache.commons.math3.exception.MathArithmeticException if all of the proportions are 0.
     */
    public void setNormalProp(double normalProp) {
        this.normalProp.set(normalProp);
        setBehaveDist();
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
     * Setter for {@link #distancingProp}.
     *
     * @param distancingProp {@link #distancingProp}
     * @throws org.apache.commons.math3.exception.NotPositiveException if the given parameter is negative.
     * @throws org.apache.commons.math3.exception.NotFiniteNumberException if the given parameter is infinite.
     * @throws org.apache.commons.math3.exception.NotANumberException if the given parameter is NaN.
     * @throws org.apache.commons.math3.exception.MathArithmeticException if all of the probabilities are 0.
     */
    public void setDistancingProp(double distancingProp) {
        this.distancingProp.set(distancingProp);
        setBehaveDist();
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

    /**
     * Setter for {@link #avoidantProp}.
     *
     * @param avoidantProp {@link #avoidantProp}
     * @throws org.apache.commons.math3.exception.NotPositiveException if the given parameter is negative.
     * @throws org.apache.commons.math3.exception.NotFiniteNumberException if the given parameter is infinite.
     * @throws org.apache.commons.math3.exception.NotANumberException if the given parameter is NaN.
     * @throws org.apache.commons.math3.exception.MathArithmeticException if all of the probabilities are 0.
     */
    public void setAvoidantProp(double avoidantProp) {
        this.avoidantProp.set(avoidantProp);
        setBehaveDist();
    }

    /**
     * Set the behaviour distribution to reflect new values for the proportions.
     *
     * @throws org.apache.commons.math3.exception.NotPositiveException if any of the probabilities are negative.
     * @throws org.apache.commons.math3.exception.NotFiniteNumberException if any of the probabilities are infinite.
     * @throws org.apache.commons.math3.exception.NotANumberException if any of the probabilities are NaN.
     * @throws org.apache.commons.math3.exception.MathArithmeticException all of the probabilities are 0.
     */
    public void setBehaveDist() {
        Pair<Behaviour, Double> normal = new Pair<>(NORMAL, normalProp.get());
        Pair<Behaviour, Double> distancing = new Pair<>(DISTANCING, distancingProp.get());
        Pair<Behaviour, Double> avoidant = new Pair<>(AVOIDANT, avoidantProp.get());

        this.behaveDist = new EnumeratedDistribution<>(List.of(normal, distancing, avoidant));
    }

}

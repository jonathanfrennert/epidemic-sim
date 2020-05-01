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
 * A utility class for distributing behaviour when creating a human population.
 */
public class BehaviourDistribution {

    /** The behaviour distribution.*/
    private EnumeratedDistribution<Behaviour> behaveDist;

    /** The probability of normal behaviour.*/
    private final DoubleProperty normalProb;

    /** The probability of social distancing behaviour.*/
    private final DoubleProperty distancingProb;

    /** The probability of avoidant behaviour.*/
    private final DoubleProperty avoidantProb;

    //---------------------------- Constructor ----------------------------

    /**
     * Create a behaviour distribution. Distribution is relative to the given probabilities.
     *
     * @param normalProp the probability of normal behaviour
     * @param distancingProp the probability of social distancing behaviour
     * @param avoidantProp The probability of avoidant behaviour
     * @throws org.apache.commons.math3.exception.NotPositiveException if any of the probabilities are negative.
     * @throws org.apache.commons.math3.exception.NotFiniteNumberException if any of the probabilities are infinite.
     * @throws org.apache.commons.math3.exception.NotANumberException if any of the probabilities are NaN.
     * @throws org.apache.commons.math3.exception.MathArithmeticException all of the probabilities are 0.
     */
    public BehaviourDistribution(double normalProp, double distancingProp, double avoidantProp) {
        this.normalProb = new SimpleDoubleProperty(normalProp);
        this.distancingProb = new SimpleDoubleProperty(distancingProp);
        this.avoidantProb = new SimpleDoubleProperty(avoidantProp);

        Pair<Behaviour, Double> normal = new Pair<>(NORMAL, normalProb.get());
        Pair<Behaviour, Double> distancing = new Pair<>(DISTANCING, distancingProb.get());
        Pair<Behaviour, Double> avoidant = new Pair<>(AVOIDANT, avoidantProb.get());

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
     * Getter for {@link #normalProb}.
     *
     * @return {@link #normalProb}
     */
    public double getNormalProb() {
        return normalProb.get();
    }

    /**
     * Getter for {@link #normalProb} property.
     *
     * @return {@link #normalProb} property
     */
    public DoubleProperty normalProbProperty() {
        return normalProb;
    }

    /**
     * Setter for {@link #normalProb}.
     *
     * @param normalProb {@link #normalProb}
     * @throws org.apache.commons.math3.exception.NotPositiveException if the given parameter is negative.
     * @throws org.apache.commons.math3.exception.NotFiniteNumberException if the given parameter is infinite.
     * @throws org.apache.commons.math3.exception.NotANumberException if the given parameter is NaN.
     * @throws org.apache.commons.math3.exception.MathArithmeticException if all of the probabilities are 0.
     */
    public void setNormalProb(double normalProb) {
        this.normalProb.set(normalProb);
        setBehaveDist();
    }

    /**
     * Getter for {@link #distancingProb}.
     *
     * @return {@link #distancingProb}
     */
    public double getDistancingProb() {
        return distancingProb.get();
    }

    /**
     * Getter for {@link #distancingProb} property.
     *
     * @return {@link #distancingProb} property
     */
    public DoubleProperty distancingProbProperty() {
        return distancingProb;
    }

    /**
     * Setter for {@link #distancingProb}.
     *
     * @param distancingProb {@link #distancingProb}
     * @throws org.apache.commons.math3.exception.NotPositiveException if the given parameter is negative.
     * @throws org.apache.commons.math3.exception.NotFiniteNumberException if the given parameter is infinite.
     * @throws org.apache.commons.math3.exception.NotANumberException if the given parameter is NaN.
     * @throws org.apache.commons.math3.exception.MathArithmeticException if all of the probabilities are 0.
     */
    public void setDistancingProb(double distancingProb) {
        this.distancingProb.set(distancingProb);
        setBehaveDist();
    }

    /**
     * Getter for {@link #avoidantProb}.
     *
     * @return {@link #avoidantProb}
     */
    public double getAvoidantProb() {
        return avoidantProb.get();
    }

    /**
     * Getter for {@link #avoidantProb} property.
     *
     * @return {@link #avoidantProb} property
     */
    public DoubleProperty avoidantProbProperty() {
        return avoidantProb;
    }

    /**
     * Setter for {@link #avoidantProb}.
     *
     * @param avoidantProb {@link #avoidantProb}
     * @throws org.apache.commons.math3.exception.NotPositiveException if the given parameter is negative.
     * @throws org.apache.commons.math3.exception.NotFiniteNumberException if the given parameter is infinite.
     * @throws org.apache.commons.math3.exception.NotANumberException if the given parameter is NaN.
     * @throws org.apache.commons.math3.exception.MathArithmeticException if all of the probabilities are 0.
     */
    public void setAvoidantProb(double avoidantProb) {
        this.avoidantProb.set(avoidantProb);
        setBehaveDist();
    }

    /**
     * Set the behaviour distribution to reflect new values for the probabilities.
     * @throws org.apache.commons.math3.exception.NotPositiveException if any of the probabilities are negative.
     * @throws org.apache.commons.math3.exception.NotFiniteNumberException if any of the probabilities are infinite.
     * @throws org.apache.commons.math3.exception.NotANumberException if any of the probabilities are NaN.
     * @throws org.apache.commons.math3.exception.MathArithmeticException all of the probabilities are 0.
     */
    public void setBehaveDist() {
        Pair<Behaviour, Double> normal = new Pair<>(NORMAL, normalProb.get());
        Pair<Behaviour, Double> distancing = new Pair<>(DISTANCING, distancingProb.get());
        Pair<Behaviour, Double> avoidant = new Pair<>(AVOIDANT, avoidantProb.get());

        this.behaveDist = new EnumeratedDistribution<>(List.of(normal, distancing, avoidant));
    }

}

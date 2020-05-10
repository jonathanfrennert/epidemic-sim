package org.epi.model;

import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.distribution.EnumeratedDistribution;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.List;

import static org.epi.model.Behaviour.AVOIDANT;
import static org.epi.model.Behaviour.INERT;
import static org.epi.model.Behaviour.NORMAL;

/**
 * Utilise a behaviour distribution when creating a human population.
 */
public class BehaviourDistribution {

    /** The behaviour distribution.*/
    private EnumeratedDistribution<Behaviour> behaviourDistribution;

    /** The proportion of normal behaviour.*/
    private final DoubleProperty normalProportion;

    /** The proportion of social distancing behaviour.*/
    private final DoubleProperty inertProportion;

    /** The proportion of avoidant behaviour.*/
    private final DoubleProperty avoidantProportion;

    //---------------------------- Constructor ----------------------------

    /**
     * Create a behaviour distribution. Inputs normalises proportions to probabilities.
     *
     * @param normalProp the proportion of normal behaviour
     * @param inertProp the proportion of social distancing behaviour
     * @param avoidantProp The proportion of avoidant behaviour
     * @throws org.apache.commons.math3.exception.NotPositiveException if any of the proportions are negative.
     * @throws org.apache.commons.math3.exception.NotFiniteNumberException if any of the proportions are infinite.
     * @throws org.apache.commons.math3.exception.NotANumberException if any of the proportions are NaN.
     * @throws org.apache.commons.math3.exception.MathArithmeticException all of the proportions are 0.
     */
    public BehaviourDistribution(double normalProp, double inertProp, double avoidantProp) {
        this.normalProportion = new SimpleDoubleProperty(normalProp);
        this.inertProportion = new SimpleDoubleProperty(inertProp);
        this.avoidantProportion = new SimpleDoubleProperty(avoidantProp);

        Pair<Behaviour, Double> normal = new Pair<>(NORMAL, this.normalProportion.get());
        Pair<Behaviour, Double> distancing = new Pair<>(INERT, this.inertProportion.get());
        Pair<Behaviour, Double> avoidant = new Pair<>(AVOIDANT, this.avoidantProportion.get());

        this.behaviourDistribution = new EnumeratedDistribution<>(List.of(normal, distancing, avoidant));
    }

    //---------------------------- Simulator action ----------------------------

    /**
     * Sample the behaviour distribution.
     *
     * @return a behaviour
     */
    public Behaviour sample() {
        return behaviourDistribution.sample();
    }

    //---------------------------- Getters & Setters ----------------------------

    /**
     * Getter for {@link #normalProportion}.
     *
     * @return {@link #normalProportion}
     */
    public double getNormalProportion() {
        return normalProportion.get();
    }

    /**
     * Getter for {@link #normalProportion} property.
     *
     * @return {@link #normalProportion} property
     */
    public DoubleProperty normalProportionProperty() {
        return normalProportion;
    }

    /**
     * Setter for {@link #normalProportion}.
     *
     * @param normalProportion {@link #normalProportion}
     * @throws org.apache.commons.math3.exception.NotPositiveException if the given parameter is negative.
     * @throws org.apache.commons.math3.exception.NotFiniteNumberException if the given parameter is infinite.
     * @throws org.apache.commons.math3.exception.NotANumberException if the given parameter is NaN.
     * @throws org.apache.commons.math3.exception.MathArithmeticException if all of the proportions are 0.
     */
    public void setNormalProportion(double normalProportion) {
        this.normalProportion.set(normalProportion);
        setBehaviourDistribution();
    }

    /**
     * Getter for {@link #inertProportion}.
     *
     * @return {@link #inertProportion}
     */
    public double getInertProportion() {
        return inertProportion.get();
    }

    /**
     * Getter for {@link #inertProportion} property.
     *
     * @return {@link #inertProportion} property
     */
    public DoubleProperty inertProportionProperty() {
        return inertProportion;
    }

    /**
     * Setter for {@link #inertProportion}.
     *
     * @param inertProportion {@link #inertProportion}
     * @throws org.apache.commons.math3.exception.NotPositiveException if the given parameter is negative.
     * @throws org.apache.commons.math3.exception.NotFiniteNumberException if the given parameter is infinite.
     * @throws org.apache.commons.math3.exception.NotANumberException if the given parameter is NaN.
     * @throws org.apache.commons.math3.exception.MathArithmeticException if all of the probabilities are 0.
     */
    public void setInertProportion(double inertProportion) {
        this.inertProportion.set(inertProportion);
        setBehaviourDistribution();
    }

    /**
     * Getter for {@link #avoidantProportion}.
     *
     * @return {@link #avoidantProportion}
     */
    public double getAvoidantProportion() {
        return avoidantProportion.get();
    }

    /**
     * Getter for {@link #avoidantProportion} property.
     *
     * @return {@link #avoidantProportion} property
     */
    public DoubleProperty avoidantProportionProperty() {
        return avoidantProportion;
    }

    /**
     * Setter for {@link #avoidantProportion}.
     *
     * @param avoidantProportion {@link #avoidantProportion}
     * @throws org.apache.commons.math3.exception.NotPositiveException if the given parameter is negative.
     * @throws org.apache.commons.math3.exception.NotFiniteNumberException if the given parameter is infinite.
     * @throws org.apache.commons.math3.exception.NotANumberException if the given parameter is NaN.
     * @throws org.apache.commons.math3.exception.MathArithmeticException if all of the probabilities are 0.
     */
    public void setAvoidantProportion(double avoidantProportion) {
        this.avoidantProportion.set(avoidantProportion);
        setBehaviourDistribution();
    }

    /**
     * Set the behaviour distribution to reflect new values for the proportions.
     *
     * @throws org.apache.commons.math3.exception.NotPositiveException if any of the probabilities are negative.
     * @throws org.apache.commons.math3.exception.NotFiniteNumberException if any of the probabilities are infinite.
     * @throws org.apache.commons.math3.exception.NotANumberException if any of the probabilities are NaN.
     * @throws org.apache.commons.math3.exception.MathArithmeticException all of the probabilities are 0.
     */
    public void setBehaviourDistribution() {
        Pair<Behaviour, Double> normal = new Pair<>(NORMAL, normalProportion.get());
        Pair<Behaviour, Double> distancing = new Pair<>(INERT, inertProportion.get());
        Pair<Behaviour, Double> avoidant = new Pair<>(AVOIDANT, avoidantProportion.get());

        this.behaviourDistribution = new EnumeratedDistribution<>(List.of(normal, distancing, avoidant));
    }

}

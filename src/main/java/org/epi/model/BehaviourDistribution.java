package org.epi.model;

import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.distribution.EnumeratedDistribution;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.List;

import static org.epi.model.Behaviour.CONTACT_TRACING;
import static org.epi.model.Behaviour.SOCIAL_DISTANCING;
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
    private final DoubleProperty socialDistancingProportion;

    /** The proportion of contact tracing behaviour.*/
    private final DoubleProperty contactTracingProportion;

    //---------------------------- Constructor ----------------------------

    /**
     * Create a behaviour distribution. Inputs normalises proportions to probabilities.
     *
     * @param normalProp the proportion of normal behaviour
     * @param socialDistancingProp the proportion of social distancing behaviour
     * @param contactTracingProp The proportion of contact tracing behaviour
     * @throws org.apache.commons.math3.exception.NotPositiveException if any of the proportions are negative.
     * @throws org.apache.commons.math3.exception.NotFiniteNumberException if any of the proportions are infinite.
     * @throws org.apache.commons.math3.exception.NotANumberException if any of the proportions are NaN.
     * @throws org.apache.commons.math3.exception.MathArithmeticException all of the proportions are 0.
     */
    public BehaviourDistribution(double normalProp, double socialDistancingProp, double contactTracingProp) {
        this.normalProportion = new SimpleDoubleProperty(normalProp);
        this.socialDistancingProportion = new SimpleDoubleProperty(socialDistancingProp);
        this.contactTracingProportion = new SimpleDoubleProperty(contactTracingProp);

        Pair<Behaviour, Double> normal = new Pair<>(NORMAL, this.normalProportion.get());
        Pair<Behaviour, Double> socialDistancing = new Pair<>(SOCIAL_DISTANCING, this.socialDistancingProportion.get());
        Pair<Behaviour, Double> contactTracing = new Pair<>(CONTACT_TRACING, this.contactTracingProportion.get());

        this.behaviourDistribution = new EnumeratedDistribution<>(List.of(normal, socialDistancing, contactTracing));
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
     * Getter for {@link #socialDistancingProportion}.
     *
     * @return {@link #socialDistancingProportion}
     */
    public double getSocialDistancingProportion() {
        return socialDistancingProportion.get();
    }

    /**
     * Getter for {@link #socialDistancingProportion} property.
     *
     * @return {@link #socialDistancingProportion} property
     */
    public DoubleProperty socialDistancingProportionProperty() {
        return socialDistancingProportion;
    }

    /**
     * Setter for {@link #socialDistancingProportion}.
     *
     * @param socialDistancingProportion {@link #socialDistancingProportion}
     * @throws org.apache.commons.math3.exception.NotPositiveException if the given parameter is negative.
     * @throws org.apache.commons.math3.exception.NotFiniteNumberException if the given parameter is infinite.
     * @throws org.apache.commons.math3.exception.NotANumberException if the given parameter is NaN.
     * @throws org.apache.commons.math3.exception.MathArithmeticException if all of the probabilities are 0.
     */
    public void setSocialDistancingProportion(double socialDistancingProportion) {
        this.socialDistancingProportion.set(socialDistancingProportion);
        setBehaviourDistribution();
    }

    /**
     * Getter for {@link #contactTracingProportion}.
     *
     * @return {@link #contactTracingProportion}
     */
    public double getContactTracingProportion() {
        return contactTracingProportion.get();
    }

    /**
     * Getter for {@link #contactTracingProportion} property.
     *
     * @return {@link #contactTracingProportion} property
     */
    public DoubleProperty contactTracingProportionProperty() {
        return contactTracingProportion;
    }

    /**
     * Setter for {@link #contactTracingProportion}.
     *
     * @param contactTracingProportion {@link #contactTracingProportion}
     * @throws org.apache.commons.math3.exception.NotPositiveException if the given parameter is negative.
     * @throws org.apache.commons.math3.exception.NotFiniteNumberException if the given parameter is infinite.
     * @throws org.apache.commons.math3.exception.NotANumberException if the given parameter is NaN.
     * @throws org.apache.commons.math3.exception.MathArithmeticException if all of the probabilities are 0.
     */
    public void setContactTracingProportion(double contactTracingProportion) {
        this.contactTracingProportion.set(contactTracingProportion);
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
        Pair<Behaviour, Double> socialDistancing = new Pair<>(SOCIAL_DISTANCING, socialDistancingProportion.get());
        Pair<Behaviour, Double> contactTracing = new Pair<>(CONTACT_TRACING, contactTracingProportion.get());

        this.behaviourDistribution = new EnumeratedDistribution<>(List.of(normal, socialDistancing, contactTracing));
    }

    /**
     * Clone this behaviour distribution.
     *
     * @return a clone of this behaviour distribution
     */
    @Override
    public BehaviourDistribution clone() {
        return new BehaviourDistribution(normalProportion.get(),
                socialDistancingProportion.get(),
                contactTracingProportion.get());
    }

}

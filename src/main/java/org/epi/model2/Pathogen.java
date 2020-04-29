package org.epi.model2;

import org.epi.util.Probability;
import org.epi.util.Error;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.ListIterator;

/** A simple model of a pathogen.
 * The pathogen spreads between humans in the simulations.*/
public class Pathogen {

    /** The host for this pathogen.*/
    private Human host;

    /** The current lifetime of this pathogen in seconds.*/
    private final DoubleProperty lifetime;

    /** The lifespan of this pathogen in a host in seconds.*/
    private final DoubleProperty lifespan;

    /** The probability of a transmission occurring in effective contact.*/
    private final DoubleProperty transmissionRisk;

    /** The probability of an infected person dying from the disease.*/
    private final DoubleProperty fatalityRate;

    /** This pathogen's immunity rate.*/
    private final DoubleProperty immunityRate;

    /** The duration of this pathogen's immunity in seconds.*/
    private final DoubleProperty immunityDuration;

    //---------------------------- Constructor ----------------------------

    /**
     * Create a disease.
     *
     * @param lifespan the lifespan of this pathogen in a host in seconds
     * @param transmissionRisk the probability of a transmission occurring in effective contact
     * @param fatalityRate the probability of an infected person dying from the disease
     * @param immunityRate this pathogen's immunity rate
     * @param immunityDuration the duration of this pathogen's immunity in seconds
     * @throws IllegalArgumentException if any given durations are negative or if any probabilities given are
     *                                  less than {@value Probability#MIN_PROB} or more than {@value Probability#MAX_PROB}
     */
    public Pathogen(double lifespan,
                    double transmissionRisk,
                    double fatalityRate,
                    double immunityRate,
                    double immunityDuration) {
        Error.nonNegativeCheck(lifespan);
        Error.nonNegativeCheck(immunityDuration);
        Probability.probabilityCheck(transmissionRisk);
        Probability.probabilityCheck(fatalityRate);
        Probability.probabilityCheck(immunityRate);

        this.host = null;
        this.lifetime = new SimpleDoubleProperty(0);
        this.lifespan = new SimpleDoubleProperty(lifespan);
        this.transmissionRisk = new SimpleDoubleProperty(transmissionRisk);
        this.fatalityRate = new SimpleDoubleProperty(fatalityRate);
        this.immunityRate = new SimpleDoubleProperty(immunityRate);
        this.immunityDuration = new SimpleDoubleProperty(immunityDuration);
    }

    //---------------------------- Helper methods ----------------------------

    /**
     * Returns the hash code for this pathogen
     * dependent on the values of the non-changing instance fields.
     *
     * @return the hash code for this pathogen
     */
    @Override
    public int hashCode() {
        int result = Double.hashCode(lifespan.get());
        result = 31 * result + Double.hashCode(transmissionRisk.get());
        result = 31 *result + Double.hashCode(fatalityRate.get());
        result = 31 * result + Double.hashCode(immunityRate.get());
        result = 31 * result + Double.hashCode(immunityDuration.get());
        return result;
    }

    //---------------------------- Simulator actions ----------------------------

    /**
     * Attempt to infect all humans which are in contact with the host.
     */
    public void infect() {
        ListIterator<Human> populationItr = host.getLocation().getPopulation().listIterator();

        while (populationItr.hasNext()) {
            Human target = populationItr.next();

            boolean isHealthy = target.getPathogen() == null;
            boolean inContact = target.getModel().collidingWith(host.getModel());
            boolean effectiveTransmission = Probability.chance(transmissionRisk.get());

            if (isHealthy && inContact && effectiveTransmission) {
                Pathogen offspring = reproduce();
                populationItr.previous().setPathogen(offspring);
            }
        }
    }

    /**
     * Create a new copy of this pathogen to infect another human.
     *
     * @return create a new copy of this pathogen to infect a human.
     */
    private Pathogen reproduce()  {
        return new Pathogen(this.lifespan.get(),
                this.transmissionRisk.get(),
                this.fatalityRate.get(),
                this.immunityRate.get(),
                this.immunityDuration.get());
    }

    /**
     * Increase the lifetime as time passes and if the lifespan has has passed, die and potentially kill the host,
     * or let the host's immune system potentially learn to defend against the pathogen.
     *
     * @param elapsedSeconds the number of seconds elapsed since the immune system was last updated
     */
    public void live(double elapsedSeconds) {
        lifetime.set(lifetime.get() + elapsedSeconds);

        if (lifespan.get() <= lifetime.get()) {
            if (!killHost()) {
                host.getImmuneSystem().learn(this);
            }
            die();
        }
    }

    /**
     * Attempt to kill the host by removing them from the population.
     */
    private boolean killHost() {
        boolean isFatal = Probability.chance(fatalityRate.get());

        if (isFatal) {
            host.getLocation().getPopulation().remove(host);
        }

        return isFatal;
    }

    /**
     * Remove all references to this pathogen, so that it is deleted by the garbage collection.
     */
    public void die() {
        host.setPathogen(null);
        setHost(null);
    }

    //---------------------------- Getters & Setters ----------------------------

    /**
     * Setter for {@link #host}.
     */
    public void setHost(Human host) {
        this.host = host;
    }

    /**
     * Getter for {@link #lifetime}.
     *
     * @return {@link #lifetime}
     */
    public double getLifetime() {
        return lifetime.get();
    }

    /**
     * Getter for {@link #lifetime} property.
     *
     * @return {@link #lifetime} property
     */
    public DoubleProperty lifetimeProperty() {
        return lifetime;
    }

    /**
     * Setter for {@link #lifetime}.
     *
     * @throws IllegalArgumentException if the given parameter is negative
     */
    public void setLifetime(double lifetime) {
        Error.nonNegativeCheck(lifetime);
        this.lifetime.set(lifetime);
    }

    /**
     * Getter for {@link #lifespan}.
     *
     * @return {@link #lifespan}
     */
    public double getLifespan() {
        return lifespan.get();
    }

    /**
     * Getter for {@link #lifespan} property.
     *
     * @return {@link #lifespan} property
     */
    public DoubleProperty lifespanProperty() {
        return lifespan;
    }

    /**
     * Setter for {@link #lifespan}.
     *
     * @throws IllegalArgumentException if the given parameter is negative
     */
    public void setLifespan(double lifespan) {
        Error.nonNegativeCheck(lifespan);
        this.lifespan.set(lifespan);
    }

    /**
     * Getter for {@link #transmissionRisk}.
     *
     * @return {@link #transmissionRisk}
     */
    public double getTransmissionRisk() {
        return transmissionRisk.get();
    }

    /**
     * Getter for {@link #transmissionRisk} property.
     *
     * @return {@link #transmissionRisk} property
     */
    public DoubleProperty transmissionRiskProperty() {
        return transmissionRisk;
    }

    /**
     * Setter for {@link #transmissionRisk}.
     *
     * @throws IllegalArgumentException if the given parameter is less than {@value Probability#MIN_PROB} or
     *                                  more than {@value Probability#MAX_PROB}
     */
    public void setTransmissionRisk(double transmissionRisk) {
        Probability.probabilityCheck(transmissionRisk);
        this.transmissionRisk.set(transmissionRisk);
    }

    /**
     * Getter for {@link #fatalityRate}.
     *
     * @return {@link #fatalityRate}
     */
    public double getFatalityRate() {
        return fatalityRate.get();
    }

    /**
     * Getter for {@link #fatalityRate} property.
     *
     * @return {@link #fatalityRate} property
     */
    public DoubleProperty fatalityRateProperty() {
        return fatalityRate;
    }

    /**
     * Setter for {@link #fatalityRate}.
     *
     * @throws IllegalArgumentException if the given parameter is less than {@value Probability#MIN_PROB} or
     *                                  more than {@value Probability#MAX_PROB}
     */
    public void setFatalityRate(double fatalityRate) {
        Probability.probabilityCheck(fatalityRate);
        this.fatalityRate.set(fatalityRate);
    }

    /**
     * Getter for {@link #immunityRate}.
     *
     * @return {@link #immunityRate}
     */
    public double getImmunityRate() {
        return immunityRate.get();
    }

    /**
     * Getter for {@link #immunityRate} property.
     *
     * @return {@link #immunityRate} property
     */
    public DoubleProperty immunityRateProperty() {
        return immunityRate;
    }

    /**
     * Setter for {@link #immunityRate}.
     *
     * @throws IllegalArgumentException if the given parameter is less than {@value Probability#MIN_PROB} or
     *                                  more than {@value Probability#MAX_PROB}
     */
    public void setImmunityRate(double immunityRate) {
        Probability.probabilityCheck(immunityRate);
        this.immunityRate.set(immunityRate);
    }

    /**
     * Getter for {@link #immunityDuration}.
     *
     * @return {@link #immunityDuration}
     */
    public double getImmunityDuration() {
        return immunityDuration.get();
    }

    /**
     * Getter for {@link #immunityDuration} property.
     *
     * @return {@link #immunityDuration} property
     */
    public DoubleProperty immunityDurationProperty() {
        return immunityDuration;
    }

    /**
     * Setter for {@link #immunityDuration}.
     *
     * @throws IllegalArgumentException if the given parameter is negative
     */
    public void setImmunityDuration(double immunityDuration) {
        Error.nonNegativeCheck(immunityDuration);
        this.immunityDuration.set(immunityDuration);
    }

}

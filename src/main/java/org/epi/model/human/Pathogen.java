package org.epi.model.human;

import org.epi.util.Probability;
import org.epi.util.Error;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    /** The probability of an sick human dying from the pathogen.*/
    private final DoubleProperty fatalityRate;

    /** The probability of becoming immune.*/
    private final DoubleProperty immunityRate;

    /** The duration of this pathogen's immunity in seconds.*/
    private final DoubleProperty immunityDuration;

    //---------------------------- Constructor ----------------------------

    /**
     * Create a pathogen.
     *
     * @param lifespan the lifespan of this pathogen in a host in seconds
     * @param transmissionRisk the probability of a transmission occurring in effective contact
     * @param fatalityRate the probability of a sick human dying from the pathogen
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
     * Returns the hash code for this pathogen dependent on the values of the non-changing instance fields.
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

    /**
     * Check if the given object is the same as this pathogen. Implemented for consistency with {@link #hashCode()}.
     *
     * @param obj an object
     * @return true if the given object is a pathogen with all the same values in the non-changing fields
     *         ({@link #lifespan}, {@link #transmissionRisk}, {@link #fatalityRate}, {@link #immunityRate} and
     *         {@link #immunityDuration}), otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    //---------------------------- Simulator actions ----------------------------

    /**
     * Attempt to infect all humans which are in contact with the host.
     */
    public void infect() {
        host.getNearby().parallelStream().
                filter(Predicate.not(Human::isSick))
                .filter(x -> Probability.chance(transmissionRisk.get()))
                .filter(human -> human.getModel().inContactWith(host.getModel()))
                .collect(Collectors.toList())
                .forEach(target -> target.setPathogen(reproduce()));
    }

    /**
     * Create a new copy of this pathogen to infect another human.
     *
     * @return create a new copy of this pathogen
     */
    public Pathogen reproduce()  {
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
            if (!fatal()) {
                host.getImmuneSystem().learn(this);
            }
            die();
        }
    }

    /**
     * Attempt to kill the host by removing them from their location.
     */
    private boolean fatal() {
        boolean isFatal = Probability.chance(fatalityRate.get());

        if (isFatal) {
            host.setLocation(null);
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
     * Getter for {@link #lifespan}.
     *
     * @return {@link #lifespan}
     */
    public double getLifespan() {
        return lifespan.get();
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
     * Setter for {@link #immunityDuration}.
     *
     * @throws IllegalArgumentException if the given parameter is negative
     */
    public void setImmunityDuration(double immunityDuration) {
        Error.nonNegativeCheck(immunityDuration);
        this.immunityDuration.set(immunityDuration);
    }

}

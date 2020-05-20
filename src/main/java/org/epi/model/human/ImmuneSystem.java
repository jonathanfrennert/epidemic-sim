package org.epi.model.human;

import org.epi.util.Probability;
import org.epi.util.Error;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Objects;

/** Simple model of a immune system.
 * A human's natural defense against pathogens.*/
public class ImmuneSystem {

    /** Default value for the antigen code.*/
    public static final int DEF_ANTIGEN = 0;

    /** The backreference to the host of this immune system.*/
    private final Human host;

    /** Antigen code for the pathogen that the host has gained immunity from.*/
    private final IntegerProperty antigen;

    /** The duration for which the antigen is remembered by the immune system in seconds.*/
    private final DoubleProperty immunityDuration;

    //---------------------------- Constructor ----------------------------

    /**
     * Create an immune system for a human.
     *
     * @param host a backreference to the host of this immune system
     * @throws NullPointerException if the given parameter is null
     */
    public ImmuneSystem(Human host) {
        Objects.requireNonNull(host, Error.getNullMsg("host"));
        this.host = host;
        this.antigen = new SimpleIntegerProperty(DEF_ANTIGEN);
        this.immunityDuration = new SimpleDoubleProperty(0);
    }

    //---------------------------- Helper methods ----------------------------

    /**
     * Check if the host has a pathogen.
     *
     * @throws IllegalStateException if the given host has no pathogen
     */
    private void pathogenCheck() {
        if (host.getPathogen() == null) {
            throw new IllegalStateException(Error.ERROR_TAG + " Immune defense called without a pathogen in the host.");
        }
    }

    /**
     * Check if the host is immune to the pathogen in this simulation.
     *
     * @return true if the immune system is immune to the pathogen in the simulation, otherwise false
     */
    public boolean isImmune() {
        return antigen.get() != DEF_ANTIGEN;
    }

    //---------------------------- Simulator actions ----------------------------

    /**
     * Decrease the immunity duration as time passes and if enough time has passed, forget.
     *
     * @param elapsedSeconds the number of seconds elapsed since the immune system was last updated
     */
    public void live(double elapsedSeconds) {
        if (isImmune()){
            immunityDuration.set(immunityDuration.get() - elapsedSeconds);
            forget();
        }
    }

    /**
     * Forget the pathogen if enough time has passed.
     */
    private void forget() {
        if (immunityDuration.get() <= 0) {
            antigen.set(DEF_ANTIGEN);
            immunityDuration.set(0);
        }
    }

    /**
     * Defend the host against known pathogens.
     *
     * @throws IllegalStateException if the host has no pathogen
     */
    public void defend() {
        pathogenCheck();

        boolean pathogenIsKnown = antigen.get() == host.getPathogen().hashCode();

        if (pathogenIsKnown) {
            immunityDuration.set(host.getPathogen().getImmunityDuration());
            host.getPathogen().die();
        }

    }

    /**
     * The immune system attempts to learn the pathogen in case of future contact.
     *
     * @param pathogen a pathogen which the host has survived
     */
    public void learn(Pathogen pathogen) {
        boolean immunityIsGained = Probability.chance(pathogen.getImmunityRate());

        if (immunityIsGained) {
            antigen.set(pathogen.hashCode());
            immunityDuration.set(pathogen.getImmunityDuration());
        }
    }

}

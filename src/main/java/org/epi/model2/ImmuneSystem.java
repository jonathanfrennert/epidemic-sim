package org.epi.model2;

import org.epi.model.StatusType;
import org.epi.util.Error;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Objects;

public class ImmuneSystem {

    /** Default value for the antigen code.*/
    public static final int DEF_ANTIGEN = 0;

    /** The backreference to the host of this immune system.*/
    private final Human host;

    /** Antigen code for the pathogen that the host has gained immunity from.*/
    private IntegerProperty antigen;

    /** The duration for which the antigen is remembered by the immune system in seconds.*/
    private DoubleProperty immunityDuration;

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

        initEvents();
    }

    /**
     * Initialise all event listeners.
     */
    private void initEvents() {

        antigen.addListener((observable, oldValue, newValue) -> {
            boolean isImmune = newValue.intValue() != DEF_ANTIGEN;
            boolean isHealthy = host.getPathogen() == null;

            if (isImmune) {
                host.setStatus(StatusType.RECOVERED);
            } else if (isHealthy) {
                host.setStatus(StatusType.HEALTHY);
            } else {
                host.setStatus(StatusType.INFECTED);
            }

        });

    }

    /**
     *
     */
    public void defend() {

    }

    public void learn(Pathogen pathogen) {

    }
}

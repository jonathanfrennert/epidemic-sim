package org.epi.model2;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import org.epi.util.Error;

import java.util.Objects;

/** Graphical representation of a human in the simulator.*/
public class Model extends Circle {

    /** Default coordinate position of the model.*/
    private static final double DEF_POS = 0;
    /** Radius of a host's graphical representation in pixels.*/
    public static final double HUMAN_RADIUS = 5;

    /** The backreference to the host of this model.*/
    private final Human host;

    /** The behaviour of the host.*/
    private final Behaviour behaviour;

    /** The velocity of the host.*/
    private Point2D velocity;

    /**
     * Create a human model given the behaviour that the human will exhibit.
     *
     * @param behaviour a behaviour
     * @throws NullPointerException if any of the two given parameters are null
     */
    public Model(Human host, Behaviour behaviour) {
        Objects.requireNonNull(host, Error.getNullMsg("host"));
        Objects.requireNonNull(behaviour, Error.getNullMsg("behaviour"));

        // Set default values.
        setCenterY(DEF_POS);
        setCenterY(DEF_POS);
        setRadius(HUMAN_RADIUS);

        this.host = host;

        this.behaviour = behaviour;
        behaviour.setModel(this);

        // Indicate the status of the human.
        fill();
    }

    /**
     * Set the model fill to indicate the status type of the human.
     */
    private void fill() {
        setFill(host.getStatus().color);
    }

}

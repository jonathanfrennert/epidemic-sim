package org.epi.model2;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.epi.util.Error;

import java.util.Objects;

/** Graphical representation of a human in the simulator.*/
public class View extends Circle {

    /** Default coordinate position of the view.*/
    private static final double DEF_POS = 0;
    /** Radius of a host's graphical representation in pixels.*/
    public static final double HUMAN_RADIUS = 5;

    /** The backreference to the host of this view.*/
    private final Human host;

    /** The behaviour of the host.*/
    private final Behaviour behaviour;

    /** The velocity of the host.*/
    private Point2D velocity;

    /**
     * Create a human's view given the behaviour that the human will exhibit.
     *
     * @param behaviour a behaviour
     * @throws NullPointerException if any of the two given parameters are null
     */
    public View(Human host, Behaviour behaviour) {
        Objects.requireNonNull(host, Error.getNullMsg("host"));
        Objects.requireNonNull(behaviour, Error.getNullMsg("behaviour"));

        // Set default values.
        setCenterY(DEF_POS);
        setCenterY(DEF_POS);
        setRadius(HUMAN_RADIUS);

        this.host = host;

        this.behaviour = behaviour;
        behaviour.setView(this);
        behaviour.setVelocity();

        // Indicate the status of the human.
        fill();
    }

    /**
     * Set the view fill to indicate the status type of the human.
     */
    private void fill() {
        setFill(host.getStatus().color);
    }

}

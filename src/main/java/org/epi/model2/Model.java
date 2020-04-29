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

    //---------------------------- Constructor ----------------------------

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

    //---------------------------- Simulator actions ----------------------------

    /**
     * Set the model fill to indicate the status type of the human.
     */
    private void fill() {
        setFill(host.getStatus().color);
    }

    //---------------------------- Helper methods ----------------------------

    /**
     * Check if a model is colliding with this model.
     *
     * @param model a model
     * @return true if this model is colliding with the given model, otherwise false
     * @throws NullPointerException if the given model is null
     */
    public boolean collidingWith(Model model) {
        Objects.requireNonNull(model, Error.getNullMsg("model"));

        double deltaX = model.getCenterX() - this.getCenterX();
        double deltaY = model.getCenterY() - this.getCenterY();
        double radiusSum = 2 * HUMAN_RADIUS;

        if (deltaX * deltaX + deltaY * deltaY <= radiusSum * radiusSum) {
            return deltaX * (velocity.getX() - this.velocity.getX())
                    + deltaY * (model.velocity.getY() - this.velocity.getY()) <= 0;
        }

        return false;
    }

}

package org.epi.model;

import javafx.scene.paint.Color;
import org.epi.util.Error;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

import java.util.Objects;

/** Graphical representation of a human in the simulator.*/
public class Model extends Circle {

    /** Default coordinate position of the model.*/
    private static final double DEF_POS = 0;
    /** Radius of a host's graphical representation in pixels.*/
    public static final double HUMAN_RADIUS = 5;

    /** The diameter of a human.*/
    public static final double HUMAN_DIAMETER = 2 * HUMAN_RADIUS;

    /** The backreference to the host of this model.*/
    private final Human host;

    /** The behaviour of the host.*/
    private final Behaviour behaviour;

    /** The velocity of the host in pixels per second.*/
    private Point2D velocity;

    //---------------------------- Constructor ----------------------------

    /**
     * Create a human model given the behaviour that the human will exhibit.
     *
     * @param host a backreference to the host of this model
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

        fill();
        this.setStrokeWidth(0.4);
        this.setStroke(Color.BLACK);

        this.behaviour = behaviour;
        behaviour.initVelocity(this);
    }

    //---------------------------- Helper method ----------------------------

    /**
     * Check if a model is in contact with this model.
     *
     * @param model a model
     * @return true if this model is in contact with the given model, otherwise false
     * @throws NullPointerException if the given model is null
     */
    public boolean inContactWith(Model model) {
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

    /**
     * Find the distance between two models.
     *
     * @param m1 a model
     * @param m2 a model
     * @return the distance between the two models in pixels
     * @throws NullPointerException if any of the given parameters are null
     */
    public static double distance (Model m1, Model m2) {
        Objects.requireNonNull(m1, Error.getNullMsg("model"));
        Objects.requireNonNull(m2, Error.getNullMsg("model"));

        Point2D p1 = new Point2D(m1.getCenterX(), m1.getCenterY());
        Point2D p2 = new Point2D(m2.getCenterX(), m2.getCenterY());

        return p1.distance(p2);
    }

    //---------------------------- Simulator actions ----------------------------

    /**
     * Move the model by its velocity for a given number of seconds.
     *
     * @param elapsedSeconds the number of seconds elapsed since the model was last updated
     */
    public void move(double elapsedSeconds) {
        behaviour.adjustToOthers(this);

        setCenterX(getCenterX() + velocity.getX() * elapsedSeconds);
        setCenterY(getCenterY() + velocity.getY() * elapsedSeconds);
    }

    /**
     * Set the model fill to indicate the status type of the human.
     */
    public void fill() {
        setFill(host.getStatus().color);
    }

    //---------------------------- Getters and setters ----------------------------

    /**
     * Getter for {@link #host}
     *
     * @return {@link #host}
     */
    public Human getHost() {
        return host;
    }

    /**
     * Setter for {@link #velocity}.
     *
     * @return {@link #velocity}
     */
    public Point2D getVelocity() {
        return velocity;
    }

    /**
     * Setter for {@link #velocity}.
     *
     * @param velocityX the horizontal velocity of the model in pixels per second
     * @param velocityY the vertical velocity of the model in pixels per second
     */
    public void setVelocity(double velocityX, double velocityY) {
        this.velocity = new Point2D(velocityX, velocityY);
    }

    /**
     * Setter for {@link #velocity}
     *
     * @param velocity {@link #velocity}
     * @throws NullPointerException if the given parameter is null
     */
    public void setVelocity(Point2D velocity) {
        Objects.requireNonNull(velocity, Error.getNullMsg("velocity"));

        this.velocity = velocity;
    }

}

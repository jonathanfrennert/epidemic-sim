package org.epi.model;

import org.epi.util.Error;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Circle;

import java.util.Objects;

/** The superclass for all humans in the simulation.*/
public abstract class Human extends Circle implements Movement {

    /** The default radius of a human in pixels.*/
    public static final double RADIUS = 5;

    /** The default horizontal velocity for a human in pixels per second.*/
    private static final double VELOCITY_X = 0;

    /** The default vertical velocity for a human in pixels per second.*/
    private static final double VELOCITY_Y = 0;

    /** The horizontal velocity of this human in pixels per second.*/
    private final DoubleProperty velocityX;

    /** The vertical velocity of this human in pixels per second.*/
    private final DoubleProperty velocityY;

    /** The status of this human.*/
    private final StatusType status;

    /**
     * Constructor for a human.
     *
     * @param status the status of this human
     * @throws NullPointerException if the given parameter is null
     */
    public Human(StatusType status) {
        super(RADIUS);
        Objects.requireNonNull(status, Error.getNullMsg("status"));

        this.status = status;
        this.velocityX = new SimpleDoubleProperty(VELOCITY_X);
        this.velocityY = new SimpleDoubleProperty(VELOCITY_Y);
    }

    /**
     * Check if this human is in contact with another human.
     *
     * @param human a human
     * @return true if the given human is in contact with this human, otherwise false
     * @throws NullPointerException if the given parameter is null
     */
    public boolean isInContactWith(Human human) {
        Objects.requireNonNull(human, Error.getNullMsg("human"));

        // TODO Alexandra :)
        return false;
    }

    /**
     * Adjust the velocities of this human and the given human which it is in contact with, such that they are
     * leaving each other in their next move.
     *
     * @param human a human
     * @throws NullPointerException if the given parameter is null
     */
    public void prepareToLeave(Human human) {
        Objects.requireNonNull(human, Error.getNullMsg("human"));

        // TODO Alexandra :)
    }

    /**
     * Move a human with their velocity.
     *
     * @param elapsedSeconds the number of seconds elapsed
     * @throws IllegalArgumentException if the given parameter is negative
     */
    @Override
    public void move(double elapsedSeconds) {
        Error.nonNegativeCheck(elapsedSeconds);

        setTranslateX(elapsedSeconds * getVelocityX());
        setTranslateY(elapsedSeconds * getVelocityY());
    }

    //---------------------------- Getters & Setters ----------------------------

    /**
     * Getter for {@link Human#velocityX}
     *
     * @return {@link Human#velocityX}
     */
    public double getVelocityX() {
        return velocityX.get();
    }

    /**
     * Getter for {@link Human#velocityX} {@link DoubleProperty}
     *
     * @return {@link Human#velocityX}
     */
    public DoubleProperty velocityXProperty() {
        return velocityX;
    }

    /**
     * Setter for {@link Human#velocityX}
     *
     * @param velocityX the horizontal velocity of the human in pixels per second
     */
    public void setVelocityX(double velocityX) {
        this.velocityX.set(velocityX);
    }

    /**
     * Getter for {@link Human#velocityY}
     *
     * @return {@link Human#velocityY}
     */
    public double getVelocityY() {
        return velocityY.get();
    }

    /**
     * Getter for {@link Human#velocityY} {@link DoubleProperty}
     *
     * @return {@link Human#velocityY}
     */
    public DoubleProperty velocityYProperty() {
        return velocityY;
    }

    /**
     * Setter for {@link Human#velocityY}
     *
     * @param velocityY the vertical velocity of the human in pixels per second
     */
    public void setVelocityY(double velocityY) {
        this.velocityY.set(velocityY);
    }

    /**
     * Getter for {@link Human#status}.
     *
     * @return {@link Human#status}
     */
    public StatusType getStatus() {
        return status;
    }

}
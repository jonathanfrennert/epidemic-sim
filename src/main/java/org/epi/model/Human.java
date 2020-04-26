package org.epi.model;

import org.epi.util.Error;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Circle;

import java.util.Objects;

import static java.lang.Math.sqrt;

/** The superclass for all humans in the simulation.*/
public abstract class Human extends Circle {

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
     * Adjust the velocities of this human and the given human if they are in contact, such that they are
     * leaving each other in their next move. If they are not in contact, does not adjust velocities.
     *
     * @param that a human
     * @throws NullPointerException if the given parameter is null
     */
    public void leave(Human that) {
        Objects.requireNonNull(that, Error.getNullMsg("human"));

        double deltaX = that.getCenterX() - this.getCenterX();
        double deltaY = that.getCenterY() - this.getCenterY();

        double distance = sqrt(deltaX * deltaX + deltaY * deltaY);

        double unitContactX = deltaX / distance;
        double unitContactY = deltaY / distance;

        // velocity of this human in parallel to contact vector, same for that human.
        double u1 = this.getVelocityX() * unitContactX + this.getVelocityY() * unitContactY;
        double u2 = that.getVelocityX() * unitContactX + that.getVelocityY() * unitContactY;

        /* Components of this human velocity in direction perpendicular
        to contact vector. This doesn't change with collision.*/
        double u1PerpX = this.getVelocityX() - u1 * unitContactX;
        double u1PerpY = this.getVelocityY() - u1 * unitContactY;

        double u2PerpX = that.getVelocityX() - u2 * unitContactX;
        double u2PerpY = that.getVelocityY() - u2 * unitContactY;

        this.setVelocityX(u2 * unitContactX + u1PerpX);
        this.setVelocityY(u2 * unitContactY + u1PerpY);

        that.setVelocityX(u1 * unitContactX + u2PerpX);
        that.setVelocityY(u1 * unitContactY + u2PerpY);
    }

    /**
     * Check if this human is in contact with another human.
     *
     * @param that a human
     * @return true if the given human is in contact with this human, otherwise false
     */
    public boolean met(Human that) {
        Objects.requireNonNull(that, Error.getNullMsg("human"));

        double deltaX = that.getCenterX() - this.getCenterX();
        double deltaY = that.getCenterY() - this.getCenterY();
         double radiusSum = 2 * RADIUS;

        if (deltaX * deltaX + deltaY * deltaY <= radiusSum * radiusSum) {
            return deltaX * (that.getVelocityX() - this.getVelocityX())
                    + deltaY * (that.getVelocityY() - this.getVelocityY()) <= 0;
        }

        return false;
    }

    /**
     * Move a human with their velocity.
     *
     * @param elapsedSeconds the number of seconds elapsed
     * @throws IllegalArgumentException if the given parameter is negative
     */
    public void move(double elapsedSeconds) {
        Error.nonNegativeCheck(elapsedSeconds);

        setCenterX(getCenterX() + elapsedSeconds * getVelocityX());
        setCenterY(getCenterY() + elapsedSeconds * getVelocityY());
    }

    /**
     * Check if this human is of a given type.
     *
     * @param statusType a status type
     * @return true if this human's status type is equal to the given status type, otherwise false.
     */
    public boolean isType(StatusType statusType) {
        return statusType == status;
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
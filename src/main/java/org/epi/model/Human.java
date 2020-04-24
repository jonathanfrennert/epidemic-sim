package org.epi.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
     * Check if this human is in contact with another human.
     *
     * @param human a human
     * @return true if the given human is in contact with this human, otherwise false
     * @throws NullPointerException if the given parameter is null
     */
    public boolean isInContactWith(Human human) {
        Objects.requireNonNull(human, Error.getNullMsg("human"));

        final double deltaX = getCenterX() - human.getCenterX();
        final double deltaY = getCenterY() - human.getCenterY();
        final double radiusSum = getRadius() + human.getRadius();

        if (deltaX * deltaX + deltaY * deltaY <= radiusSum * radiusSum) {
            if (deltaX * (human.getVelocityX() - getVelocityX())
                    + deltaY * (human.getVelocityY() - getVelocityY()) <= 0) {
                return true;
            }
        }
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
        if(isInContactWith(human)){
            //TODO move(timeElapsed) to avoid sticking

            final double deltaX = getCenterX() - human.getCenterX();
            final double deltaY = getCenterY() - human.getCenterY();

            final double distance = sqrt(deltaX * deltaX + deltaY * deltaY);

            double unitContactX = deltaX / distance;
            double unitContactY = deltaY / distance;

            // velocity of ball 1 parallel to contact vector, same for ball 2
            final double u1 = getVelocityX() * unitContactX + getVelocityY() * unitContactY;
            final double u2 = human.getVelocityX() * unitContactX + human.getVelocityY() * unitContactY;

            /* Components of ball 1 velocity in direction perpendicular
            to contact vector. This doesn't change with collision */
            double u1PerpX = getVelocityX() - u1 * unitContactX;
            double u1PerpY = getVelocityY() - u1 * unitContactY;

            double u2PerpX = human.getVelocityX() - u2 * unitContactX;
            double u2PerpY = human.getVelocityY() - u2 * unitContactY;

            double initSpeedSquared1 = getVelocityX() * getVelocityX() + getVelocityY() * getVelocityY();
            double initSpeedSquared2 = human.getVelocityX() * human.getVelocityX() + human.getVelocityY() * human.getVelocityY();

            double finalSpeedSquared1 = (u2 * unitContactX + u1PerpX) * (u2 * unitContactX + u1PerpX)
                    + (u2 * unitContactY + u1PerpY) * (u2 * unitContactY + u1PerpY);
            double finalSpeedSquared2 = (u1 * unitContactX + u2PerpX) * (u1 * unitContactX + u2PerpX)
                    + (u1 * unitContactY + u2PerpY) * (u1 * unitContactY + u2PerpY);

            //normalizing factors to keep the same speed at all times
            double n1 = sqrt(initSpeedSquared1/finalSpeedSquared1);
            double n2 = sqrt(initSpeedSquared2/finalSpeedSquared2);

            setVelocityX(n1 * (u2 * unitContactX + u1PerpX));
            setVelocityY(n1 * (u2 * unitContactY + u1PerpY));
            human.setVelocityX(n2 * (u1 * unitContactX + u2PerpX));
            human.setVelocityY(n2 * (u1 * unitContactY + u2PerpY));
        }
    }

    /**
     * Move a human with their velocity.
     *
     * @param elapsedSeconds the number of seconds elapsed
     * @throws IllegalArgumentException if the given parameter is negative
     */
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
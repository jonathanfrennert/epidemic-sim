package org.epi.model;

import org.epi.util.ErrorUtil;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.Objects;

/** View representation of humans in the simulation. Circle fill is used to distinguish status types in the population.
 * Bouncy circles act have inelastic collisions, the same speed and radius. The units are measured in pixels per second.*/
public class BouncyCircle extends Circle {

    /** The speed of a bouncy circle in pixels per second.*/
    public static final double SPEED = 20;
    /** The radius of a bouncy circle in pixels.*/
    private static final double RADIUS = 5;

    /** The horizontal velocity of the bouncy circle in pixels per second.*/
    private final DoubleProperty velocityX;
    /** The vertical velocity of the bouncy circle in pixels per second.*/
    private final DoubleProperty velocityY;

    /**
     * Factory method to create a new instance of bouncy circle with a specified position, velocity and fill in a container.
     *
     * @param centerX   the horizontal position of the center of the bouncy circle in pixels
     * @param centerY   the vertical position of the center of the bouncy circle in pixels
     * @param velocityX the horizontal velocity of the bouncy circle in pixels per second
     * @param velocityY the vertical velocity of the the bouncy circle in pixels per second
     * @param fill      determines how to fill the interior of the bouncy circle
     * @throws IllegalArgumentException if the total velocity magnitude of velocityX and velocityY is not equal to
     *                                  {@value BouncyCircle#SPEED} pixels per second
     * @throws NullPointerException     if the given fill is null
     */
    public static BouncyCircle createBouncyCircle(double centerX, double centerY, double velocityX, double velocityY, Paint fill) {
        checkSpeed(velocityX, velocityY);
        Objects.requireNonNull(fill, ErrorUtil.getNullMsg("fill"));

        return new BouncyCircle(centerX, centerY, velocityX, velocityY, fill);
    }

    /**
     * Constructor for a new instance of bouncy circle with a specified position, velocity and fill.
     *
     * @param centerX   the horizontal position of the center of the bouncy circle in pixels
     * @param centerY   the vertical position of the center of the bouncy circle in pixels
     * @param velocityX the horizontal velocity of the bouncy circle in pixels per second
     * @param velocityY the vertical velocity of the the bouncy circle in pixels per second
     * @param fill      determines how to fill the interior of the bouncy circle
     */
    private BouncyCircle(double centerX, double centerY, double velocityX, double velocityY, Paint fill) {
        super(centerX, centerY, RADIUS, fill);
        this.velocityX = new SimpleDoubleProperty(velocityX);
        this.velocityY = new SimpleDoubleProperty(velocityY);
    }

    /**
     * Check that the magnitude of the total velocity is equal to {@value BouncyCircle#SPEED}.
     *
     * @param velocityX a horizontal velocity in pixels per second
     * @param velocityY a vertical velocity in pixels per second
     * @throws IllegalArgumentException if the total velocity magnitude of velocityX and velocityY is not equal to
     *                                  {@value BouncyCircle#SPEED} pixels per second
     */
    private static void checkSpeed(double velocityX, double velocityY) {
        double totalVelocity = velocityX*velocityX + velocityY*velocityY;

        if (totalVelocity != SPEED) {
            throw new IllegalArgumentException("ERROR: The total velocity of a bouncy circle must be equal to " + SPEED
                    + "but is:" + totalVelocity);
        }
    }

    /**
     * Check if this bouncy circle is colliding with another circle.
     *
     * @param circle A circle
     * @return true if the bouncy circle is colliding with the given circle, otherwise false
     * @throws NullPointerException if the given circle is null
     */
    public boolean collidingWith(Circle circle) {
        Objects.requireNonNull(circle, ErrorUtil.getNullMsg("circle"));

        // TODO Alexandra :)
        return false;
    }

    /**
     * Set the horizontal and vertical velocity resulting from a bounce between this bouncy circle and
     * another bouncy circle for both bouncy circles.
     *
     * @param other a bouncy circle
     * @throws NullPointerException if the given bouncy circle is null
     */
    public void bounceOn(BouncyCircle other) {
        Objects.requireNonNull(other, ErrorUtil.getNullMsg("bouncy circle"));

        // TODO Alexandra :)
    }

    /**
     * Move the bouncy circle by a certain amount of time.
     *
     * @param elapsedSeconds the number of seconds elapsed in the move.
     */
    public void move(double elapsedSeconds) {
        setTranslateX(elapsedSeconds * getVelocityX());
        setTranslateY(elapsedSeconds * getVelocityY());
    }

    //---------------------------- Getters & Setters ----------------------------

    /**
     * Getter for {@link BouncyCircle#velocityX}
     *
     * @return {@link BouncyCircle#velocityX}
     */
    public double getVelocityX() {
        return velocityX.get();
    }

    /**
     * Getter for {@link BouncyCircle#velocityX} {@link DoubleProperty}
     *
     * @return {@link BouncyCircle#velocityX}
     */
    public DoubleProperty velocityXProperty() {
        return velocityX;
    }

    /**
     * Setter for {@link BouncyCircle#velocityX}
     *
     * @param velocityX the horizontal velocity of the bouncy circle in pixels per second
     * @throws IllegalArgumentException if the total velocity magnitude of velocityX and velocityY is not equal to
     *                                  {@value BouncyCircle#SPEED} pixels per second
     */
    public void setVelocityX(double velocityX) {
        checkSpeed(velocityX, getVelocityY());

        this.velocityX.set(velocityX);
    }

    /**
     * Getter for {@link BouncyCircle#velocityY}
     *
     * @return {@link BouncyCircle#velocityY}
     */
    public double getVelocityY() {
        return velocityY.get();
    }

    /**
     * Getter for {@link BouncyCircle#velocityY} {@link DoubleProperty}
     *
     * @return {@link BouncyCircle#velocityY}
     */
    public DoubleProperty velocityYProperty() {
        return velocityY;
    }

    /**
     * Setter for {@link BouncyCircle#velocityY}
     *
     * @param velocityY the vertical velocity of the bouncy circle in pixels per second
     * @throws IllegalArgumentException if the total velocity magnitude of velocityX and velocityY is not equal to
     *                                  {@value BouncyCircle#SPEED} pixels per second
     */
    public void setVelocityY(double velocityY) {
        checkSpeed(getVelocityX(), velocityY);

        this.velocityY.set(velocityY);
    }

}

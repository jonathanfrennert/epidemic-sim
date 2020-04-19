package org.epi.model;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.epi.util.ErrorUtil;

import java.util.Objects;

/** View representation of humans in the simulation. Bouncy circles act have inelastic collisions and all have the same
 * speed and radius. The units are measured in pixels per {@link javafx.animation.KeyFrame}.*/
public class BouncyCircle extends Circle {

    /** The speed of a bouncy circle in pixels per KeyFrame.*/
    public static final double SPEED = 10;
    /** The radius of a bouncy circle in pixels.*/
    private static final double RADIUS = 5;

    /** The horizontal velocity of the bouncy circle in pixels per KeyFrame.*/
    private DoubleProperty velocityX;
    /** The vertical velocity of the bouncy circle in pixels per KeyFrame.*/
    private DoubleProperty velocityY;

    /**
     * Factory method to create a new instance of bouncy circle with a specified position, velocity and fill.
     *
     * @param centerX   the horizontal position of the center of the bouncy circle in pixels
     * @param centerY   the vertical position of the center of the bouncy circle in pixels
     * @param velocityX the horizontal velocity of the bouncy circle in pixels per KeyFrame
     * @param velocityY the vertical velocity of the the bouncy circle in pixels per KeyFrame
     * @param fill      determines how to fill the interior of the bouncy circle
     * @throws IllegalArgumentException if the total velocity magnitude of velocityX and velocityY is not equal to
     *                                  {@link BouncyCircle#SPEED}
     * @throws NullPointerException     if the given fill is null
     */
    public static BouncyCircle createBouncyCircle(double centerX, double centerY, double velocityX, double velocityY, Paint fill) {
        checkSpeed(velocityX, velocityY);
        Objects.requireNonNull(fill, ErrorUtil.getNullMsg("fill"));

        return new BouncyCircle(centerX, centerY, velocityX, velocityY, fill);
    }

    /**
     * Check that the magnitude of the total velocity is equal to {@link BouncyCircle#SPEED}.
     *
     * @param velocityX a horizontal velocity in pixels per KeyFrame
     * @param velocityY a vertical velocity in pixels per KeyFrame
     */
    private static void checkSpeed(double velocityX, double velocityY) {
        double totalVelocity = velocityX*velocityX + velocityY*velocityY;
        if (totalVelocity != SPEED) {
            throw new IllegalArgumentException("ERROR: The total velocity of a bouncy circle must be equal to " + SPEED
                    + "but is:" + totalVelocity);
        }
    }

    /**
     * Constructor for a new instance of bouncy circle with a specified position, velocity and fill.
     *
     * @param centerX   the horizontal position of the center of the bouncy circle in pixels
     * @param centerY   the vertical position of the center of the bouncy circle in pixels
     * @param velocityX the horizontal velocity of the bouncy circle in pixels per KeyFrame
     * @param velocityY the vertical velocity of the the bouncy circle in pixels per KeyFrame
     * @param fill      determines how to fill the interior of the bouncy circle
     */
    private BouncyCircle(double centerX, double centerY, double velocityX, double velocityY, Paint fill) {
        super(centerX, centerY, RADIUS, fill);
        this.velocityX.setValue(velocityX);
        this.velocityY.setValue(velocityY);
    }

    public double getVelocityX() {
        return velocityX.get();
    }

    public DoubleProperty velocityXProperty() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX.set(velocityX);
    }

    public double getVelocityY() {
        return velocityY.get();
    }

    public DoubleProperty velocityYProperty() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY.set(velocityY);
    }

}

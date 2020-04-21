package org.epi.model.human;

import org.epi.model.BouncyCircle;

/** Model class for humans without the disease.*/
public class HealthyHuman extends Human {

    /**
     * The constructor for healthy humans.
     *
     * @param centerX   the initial horizontal position of the center of the bouncy circle which represents this human
     *                  in pixels
     * @param centerY   the initial vertical position of the center of the bouncy circle which represents this human
     *                  in pixels
     * @param velocityX the initial horizontal velocity of the bouncy circle which represents this human in pixels per
     *                  frame
     * @param velocityY the initial vertical velocity of the the bouncy circle which represents this human in pixels
     *                  per frame
     * @throws IllegalArgumentException if the
     */
    public HealthyHuman(double centerX, double centerY, double velocityX, double velocityY) {
        super(StatusType.HEALTHY, BouncyCircle.createBouncyCircle(centerX, centerY, velocityX, velocityY, StatusType.HEALTHY.color));
    }

}

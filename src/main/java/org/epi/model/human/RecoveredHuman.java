package org.epi.model.human;

import org.epi.model.BouncyCircle;
import org.epi.model.StatusType;

/** Model class for humans who have recovered from the disease.*/
public class RecoveredHuman extends Human {

    /**
     * The constructor for recovered humans.
     *
     * @param centerX   the initial horizontal position of the center of the bouncy circle which represents this human
     *                  in pixels
     * @param centerY   the initial vertical position of the center of the bouncy circle which represents this human
     *                  in pixels
     * @param velocityX the initial horizontal velocity of the bouncy circle which represents this human in pixels per
     *                  second
     * @param velocityY the initial vertical velocity of the the bouncy circle which represents this human in pixels
     *                  per second
     * @throws NullPointerException TODO
     */
    public RecoveredHuman(double centerX, double centerY, double velocityX, double velocityY) {
        super(StatusType.RECOVERED ,BouncyCircle.createBouncyCircle(centerX, centerY, velocityX, velocityY, StatusType.RECOVERED.color));
    }

}

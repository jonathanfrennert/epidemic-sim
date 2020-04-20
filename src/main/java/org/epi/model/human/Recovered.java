package org.epi.model.human;

import org.epi.model.BouncyCircle;

import javafx.scene.paint.Paint;

/** Model class for humans who have recovered from the disease.*/
public class Recovered extends Human {

    /**
     * The constructor for recovered humans. Meant to be accessed only via {@link HumanFactory}.
     *
     * @param centerX   the initial horizontal position of the center of the bouncy circle which represents this human
     *                  in pixels
     * @param centerY   the initial vertical position of the center of the bouncy circle which represents this human
     *                  in pixels
     * @param velocityX the initial horizontal velocity of the bouncy circle which represents this human in pixels per
     *                  frame
     * @param velocityY the initial vertical velocity of the the bouncy circle which represents this human in pixels
     *                  per frame
     * @param fill      The color which indicates this status type in the simulation view
     */
    public Recovered(double centerX, double centerY, double velocityX, double velocityY, Paint fill) {
        super(BouncyCircle.createBouncyCircle(centerX, centerY, velocityX, velocityY, fill));
    }

}

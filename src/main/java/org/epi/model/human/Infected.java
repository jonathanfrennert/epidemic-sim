package org.epi.model.human;

import org.epi.model.BouncyCircle;
import org.epi.model.Disease;

import javafx.scene.paint.Paint;

/** Model class for humans infected with the disease.*/
public class Infected extends Human {

    /** The disease which this human has.*/
    private final Disease disease;

    /**
     * The constructor for infected humans. Meant to be accessed only via {@link HumanFactory}.
     *
     * @param disease   a disease
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
    public Infected(Disease disease, double centerX, double centerY, double velocityX, double velocityY, Paint fill) {
        super(BouncyCircle.createBouncyCircle(centerX, centerY, velocityX, velocityY, fill));
        this.disease = disease;
    }

}

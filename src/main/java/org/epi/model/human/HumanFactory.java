package org.epi.model.human;

import org.epi.model.BouncyCircle;
import org.epi.model.Disease;
import org.epi.util.ErrorUtil;

import javafx.scene.paint.Color;

import java.util.Objects;

/** All the status types which individuals in the population can have and their corresponding color in the
 * simulation view.*/
enum HumanFactory {

    HEALTHY (Color.DODGERBLUE) {

        /**
         * {@inheritDoc}
         */
        @Override
        public Human createHuman(Disease disease, double centerX, double centerY, double velocityX, double velocityY) {
            return new Healthy(centerX, centerY, velocityX, velocityY, color);
        }

    },

    INFECTED (Color.CRIMSON) {

        /**
         * {@inheritDoc}
         * @throws NullPointerException if the given disease is null
         */
        @Override
        public Human createHuman(Disease disease, double centerX, double centerY, double velocityX, double velocityY) {
            Objects.requireNonNull(disease, ErrorUtil.getNullMsg("disease"));

            return new Infected(disease, centerX, centerY, velocityX, velocityY, color);
        }

    },

    RECOVERED (Color.DARKORCHID) {

        /**
         * {@inheritDoc}
         */
        @Override
        public Human createHuman(Disease disease, double centerX, double centerY, double velocityX, double velocityY) {
            return new Recovered(centerX, centerY, velocityX, velocityY, color);
        }

    };

    /** The color which identifies this human's status in the simulation view.*/
    public final Color color;

    /**
     * Constructor for a human status color.
     *
     * @param color the color to identify this human's status by in the simulation view
     */
    private HumanFactory(Color color) {
        this.color = color;
    }

    /**
     * Factory method for creating a human.
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
     * @return a human
     * @throws IllegalArgumentException if the total velocity magnitude of velocityX and velocityY is not equal to
     *                                  {@value BouncyCircle#SPEED} pixels per frame
     */
    public abstract Human createHuman(Disease disease,
                                      double centerX, double centerY,
                                      double velocityX, double velocityY);

}

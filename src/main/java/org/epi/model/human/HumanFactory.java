package org.epi.model.human;

import org.epi.model.Disease;
import org.epi.util.Error;

import java.util.Objects;

/** Static factory for creating humans.*/
public final class HumanFactory {

    /** Not to be used. */
    private HumanFactory() {
        throw new UnsupportedOperationException("This constructor should never be used.");
    }

    /**
     * Create humans for the given disease dependent status type.
     *
     * @param status    status of this human
     * @param disease   a disease
     * @param centerX   the initial horizontal position of the center of the bouncy circle which represents this human
     *                  in pixels
     * @param centerY   the initial vertical position of the center of the bouncy circle which represents this human
     *                  in pixels
     * @param velocityX the initial horizontal velocity of the bouncy circle which represents this human in pixels per
     *                  second
     * @param velocityY the initial vertical velocity of the the bouncy circle which represents this human in pixels
     *                  per second
     * @return Human initialised for the given status type and initial position. If the human creation
     * failed due to an illegal argument, null will be returned.
     * @throws NullPointerException If the given status or disease is null.
     */
    public static Human createHuman(StatusType status, Disease disease, double centerX, double centerY, double velocityX, double velocityY) {
        Objects.requireNonNull(status, Error.getNullMsg("status type"));
        Objects.requireNonNull(disease, Error.getNullMsg("disease"));

        try {
            switch (status) {
                case INFECTED: return new InfectedHuman(disease, centerX, centerY, velocityX, velocityY);
                default:
                    throw new IllegalArgumentException(Error.ERROR_TAG + " Status type is not disease dependent: " + status);
            }
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    /**
     * Create human for the given status type.
     *
     * @param status    status of this human
     * @param centerX   the initial horizontal position of the center of the bouncy circle which represents this human
     *                  in pixels
     * @param centerY   the initial vertical position of the center of the bouncy circle which represents this human
     *                  in pixels
     * @param velocityX the initial horizontal velocity of the bouncy circle which represents this human in pixels per
     *                  second
     * @param velocityY the initial vertical velocity of the the bouncy circle which represents this human in pixels
     *                  per second
     * @return Human initialised for the given status type and initial position. If the human creation
     * failed due to an illegal argument, null will be returned.
     * @throws NullPointerException If the given status is null.
     */
    public static Human createHuman(StatusType status, double centerX, double centerY, double velocityX, double velocityY) {
        Objects.requireNonNull(status, Error.getNullMsg("status type"));

        try {
            switch(status) {
                case HEALTHY:   return new HealthyHuman(centerX, centerY, velocityX, velocityY);
                case RECOVERED: return new RecoveredHuman(centerX, centerY, velocityX, velocityY);
                default:
                    throw new IllegalArgumentException(Error.ERROR_TAG + " Status type is disease dependent: " + status);
            }
        } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
        }

        return null;
    }

}

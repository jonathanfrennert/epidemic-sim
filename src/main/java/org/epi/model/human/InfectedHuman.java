package org.epi.model.human;

import javafx.beans.property.SimpleDoubleProperty;
import org.epi.model.BouncyCircle;
import org.epi.model.Disease;
import org.epi.model.StatusType;
import org.epi.util.Probability;
import org.epi.util.Error;

import javafx.beans.property.DoubleProperty;

/** Model class for humans infected with the disease.*/
public class InfectedHuman extends Human {

    /** The disease which this human has.*/
    private final Disease disease;

    /** The current duration of the disease in number of seconds.*/
    private final DoubleProperty currentDuration = new SimpleDoubleProperty(0);

    /**
     * The constructor for infected humans.
     *
     * @param disease   a disease
     * @param centerX   the initial horizontal position of the center of the bouncy circle which represents this human
     *                  in pixels
     * @param centerY   the initial vertical position of the center of the bouncy circle which represents this human
     *                  in pixels
     * @param velocityX the initial horizontal velocity of the bouncy circle which represents this human in pixels per
     *                  second
     * @param velocityY the initial vertical velocity of the the bouncy circle which represents this human in pixels
     *                  per second
     * @throws NullPointerException TODO
     * @throws IllegalArgumentException TODO
     */
    public InfectedHuman(Disease disease, double centerX, double centerY, double velocityX, double velocityY) {
        super(StatusType.INFECTED, BouncyCircle.createBouncyCircle(centerX, centerY, velocityX, velocityY, StatusType.INFECTED.color));
        this.disease = disease;
    }

    /**
     * Update the current duration by a certain amount of time.
     *
     * @param elapsedSeconds the number of seconds elapsed since the previous update
     */
    public void updateCurrentDuration(double elapsedSeconds) {
        currentDuration.setValue(getCurrentDuration() + elapsedSeconds);
    }

    /**
     * Check if this person is deceased
     *
     * @return true if the total duration of the disease has passed and the chance of fatality returned true,
     *         otherwise false
     */
    public boolean isPersonDeceased(){
        if (totalDurationPassed()) {
            return Probability.chance(disease.getFatalityRate());
        }
        return false;
    }

    /**
     * Check if this person has had the disease for its total duration.
     *
     * @return true if the person has had the disease for its total duration, otherwise false
     */
    private boolean totalDurationPassed(){
        return getCurrentDuration() >= disease.getTotalDuration();
    }

    /**
     * Check if the given duration is less than the previous duration.
     *
     * @param currentDuration the current duration in seconds
     * @throws IllegalArgumentException if the given currentDuration is less than the previous currentDuration
     */
    private void checkDuration (double currentDuration) {
        if (currentDuration < getCurrentDuration()) {
            throw new IllegalArgumentException(Error.ERROR_TAG + " Previous current duration "
                    + getCurrentDuration() + "is more than the given current duration: " + currentDuration);
        }
    }

    //---------------------------- Getters & Setters ----------------------------

    /**
     * Getter for {@link InfectedHuman#currentDuration}
     *
     * @return {@link InfectedHuman#currentDuration}
     */
    public double getCurrentDuration() {
        return currentDuration.get();
    }

    /**
     * Getter for {@link InfectedHuman#currentDuration} {@link DoubleProperty}
     *
     * @return {@link InfectedHuman#currentDuration}
     */
    public DoubleProperty currentDurationProperty() {
        return currentDuration;
    }

    /**
     * Setter for {@link InfectedHuman#currentDuration}
     *
     * @param currentDuration the current duration in seconds
     * @throws IllegalArgumentException if the given currentDuration is less than the previous currentDuration
     */
    public void setCurrentDuration(double currentDuration) {
        checkDuration(currentDuration);

        this.currentDuration.set(currentDuration);
    }

}

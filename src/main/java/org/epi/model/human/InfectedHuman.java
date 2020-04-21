package org.epi.model.human;

import org.epi.model.BouncyCircle;
import org.epi.model.Disease;
import org.epi.util.Probability;
import org.epi.util.ErrorUtil;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/** Model class for humans infected with the disease.*/
public class InfectedHuman extends Human {

    /** The disease which this human has.*/
    private final Disease disease;

    /** The current duration of the disease in number of frames.*/
    private final IntegerProperty currentDuration = new SimpleIntegerProperty(0);

    /**
     * The constructor for infected humans.
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
     * @throws NullPointerException
     * @throws IllegalArgumentException
     */
    public InfectedHuman(Disease disease, double centerX, double centerY, double velocityX, double velocityY) {
        super(StatusType.INFECTED, BouncyCircle.createBouncyCircle(centerX, centerY, velocityX, velocityY, StatusType.INFECTED.color));
        this.disease = disease;
    }

    /**
     * Update the current duration by one frame.
     */
    public void updateCurrentDuration() {
        currentDuration.setValue(getCurrentDuration() + 1);
    }

    /**
     * Check if this person is deceased
     *
     * @return true if the total duration of the disease has passed and the chance of fatality returned true,
     *         otherwise false
     */
    public boolean isPersonDeceased(){
        if (diseaseHasPassed()) {
            return Probability.chance(disease.getFatalityRate());
        }
        return false;
    }

    /**
     * Check if this person has had the disease for its total duration.
     *
     * @return true if the person has had the disease for its total duration, otherwise false
     */
    private boolean diseaseHasPassed(){
        return getCurrentDuration() >= disease.getTotalDuration();
    }

    /**
     * Check if the given duration is less than the previous duration.
     *
     * @param currentDuration the current duration in number of frames
     * @throws IllegalArgumentException if the given currentDuration is less than the previous currentDuration
     */
    private void checkDuration (int currentDuration) {
        if (currentDuration < getCurrentDuration()) {
            throw new IllegalArgumentException(ErrorUtil.ERROR_TAG + " Previous current duration "
                    + getCurrentDuration() + "is more than the given current duration: " + currentDuration);
        }
    }

    //---------------------------- Getters & Setters ----------------------------

    /**
     * Getter for {@link InfectedHuman#currentDuration}
     *
     * @return {@link InfectedHuman#currentDuration}
     */
    public int getCurrentDuration() {
        return currentDuration.get();
    }

    /**
     * Getter for {@link InfectedHuman#currentDuration} {@link DoubleProperty}
     *
     * @return {@link InfectedHuman#currentDuration}
     */
    public IntegerProperty currentDurationProperty() {
        return currentDuration;
    }

    /**
     * Setter for {@link InfectedHuman#currentDuration}
     *
     * @param currentDuration the current duration in number of frames
     * @throws IllegalArgumentException if the given currentDuration is less than the previous currentDuration
     */
    public void setCurrentDuration(int currentDuration) {
        checkDuration(currentDuration);

        this.currentDuration.set(currentDuration);
    }

}

package org.epi.model.human;

import org.epi.model.Disease;
import org.epi.util.Probability;
import org.epi.util.Error;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;

import java.util.Objects;

/** Model class for humans infected with a disease.*/
public class InfectedHuman extends Human {

    /** The initial value for the current duration.*/
    private static final double INITIAL_DURATION = 0;

    /** The disease which this human has.*/
    private final Disease disease;

    /** The current duration of the disease in number of seconds.*/
    private final DoubleProperty currentDuration;

    /**
     * The constructor for infected humans.
     *
     * @param disease a disease
     * @throws NullPointerException if the given parameter is null
     */
    public InfectedHuman(Disease disease) {
        super(StatusType.INFECTED);
        Objects.requireNonNull(disease, Error.getNullMsg("disease"));

        this.disease = disease;
        this.currentDuration = new SimpleDoubleProperty(INITIAL_DURATION);
        setFill(StatusType.INFECTED.color);
    }

    /**
     * Check if this person is deceased
     *
     * @return true if the total duration of the disease has passed and the chance of fatality returned true,
     *         otherwise false
     */
    public boolean isDeceased(){
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
     * Update the current duration by a certain amount of time.
     *
     * @param elapsedSeconds the number of seconds elapsed since the previous update
     */
    public void updateCurrentDuration(double elapsedSeconds) {
        currentDuration.setValue(getCurrentDuration() + elapsedSeconds);
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

package org.epi.model.human;

import org.epi.model.BouncyCircle;
import org.epi.model.Disease;
import org.epi.util.ErrorUtil;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Paint;

import java.util.Random;

/** Model class for humans infected with the disease.*/
public class Infected extends Human {

    /** The disease which this human has.*/
    private final Disease disease;

    /** The current duration of the disease in number of frames.*/
    private final IntegerProperty currentDuration = new SimpleIntegerProperty(0);

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

    /**
     * Check if this person has had the disease for its total duration.
     *
     * @return true if the person has had the disease for its total duration, otherwise false
     */
    public boolean diseaseHasPassed(){
        return getCurrentDuration() >= disease.getTotalDuration();
    }

    public boolean isPersonDeceased(){
        if (diseaseHasPassed()) {
            new Random().nextBoolean();
        }
        return false;
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
     * Getter for {@link Infected#currentDuration}
     *
     * @return {@link Infected#currentDuration}
     */
    public int getCurrentDuration() {
        return currentDuration.get();
    }

    /**
     * Getter for {@link Infected#currentDuration} {@link DoubleProperty}
     *
     * @return {@link Infected#currentDuration}
     */
    public IntegerProperty currentDurationProperty() {
        return currentDuration;
    }

    /**
     * Setter for {@link Infected#currentDuration}
     *
     * @param currentDuration the current duration in number of frames
     * @throws IllegalArgumentException if the given currentDuration is less than the previous currentDuration
     */
    public void setCurrentDuration(int currentDuration) {
        checkDuration(currentDuration);

        this.currentDuration.set(currentDuration);
    }

}

package org.epi.model;

/** Interface for animated moving classes.*/
public interface Movement {

    /**
     * Defines the movement of this class.
     *
     * @param elapsedSeconds the number of seconds elapsed
     */
    public void move(double elapsedSeconds);

}

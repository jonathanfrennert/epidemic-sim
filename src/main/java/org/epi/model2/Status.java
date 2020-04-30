package org.epi.model2;

import javafx.scene.paint.Color;

/** All the status types which individuals in the population can have and their corresponding color.*/
public enum Status {
    HEALTHY (Color.DODGERBLUE),
    INFECTED (Color.CRIMSON),
    RECOVERED (Color.DARKORCHID);

    /** The color indicating the status type in the simulation view.*/
    public final Color color;

    /**
     * Status type constructor.
     *
     * @param color the color indicating the status type in the simulation view
     */
    private Status(Color color) {
        this.color = color;
    }

}

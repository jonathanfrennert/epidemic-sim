package org.epi.model;

import javafx.scene.paint.Color;

/** All the status types which individuals in the population can have and their corresponding color.*/
public enum StatusType {
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
    private StatusType(Color color) {
        this.color = color;
    }

}

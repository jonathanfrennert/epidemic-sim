package org.epi.model;

import javafx.scene.paint.Color;

/** All the status types which individuals in the population can have and their corresponding color in the
 * simulation view.*/
enum StatusType {
    HEALTHY (Color.DODGERBLUE),
    INFECTED (Color.CRIMSON),
    RECOVERED (Color.DARKORCHID);

    /** The color which identifies the status in the simulation view.*/
    private final Color color;

    /**
     * Constructor for a status type.
     *
     * @param color the color to identify the status by in the simulation view
     */
    private StatusType(Color color) {
        this.color = color;
    }

    /**
     * Getter for {@link StatusType#color}.
     *
     * @return the color which identifies the status in the simulation view
     */
    public Color getColor() {
        return color;
    }

}

package org.epi.model;

import javafx.beans.property.Property;
import javafx.scene.shape.Circle;

/** The person class; used in calculating the status of the person and for visualisation of the person interactions.*/
public class Person extends Circle {

    /** Standard radius of the circle representing the person, in pixels.*/
    private static final double RADIUS = 5;

    /** The health status of the person.*/
    private Property<Status> statusProperty;

    /**
     * Create a person, defining their position in the simulation plot and their status.
     *
     * @param centerX the horizontal position of the center of the circle in pixels
     * @param centerY the vertical position of the center of the circle in pixels
     */
    public Person(Double centerX, Double centerY, Status status) {
        super(centerX,centerY, RADIUS);
        this.statusProperty.setValue(status);
    }

}

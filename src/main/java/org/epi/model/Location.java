package org.epi.model;

import javafx.collections.FXCollections;
import org.epi.util.Error;

import javafx.geometry.Point2D;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;

import static org.epi.model.Model.HUMAN_RADIUS;
import static org.epi.util.Clip.clip;

/** A simple model of a location.
 * The class is used as a graphical representation of a location in the simulator.*/
public class Location {

    /** The graphical representation of this location.*/
    private final Pane area;

    /** The spatial hash of humans in the area.*/
    private final SpatialHash spatialHash;

    /** The population of this location.*/
    private final ObservableList<Human> population;

    //---------------------------- Constructor & associated helpers ----------------------------

    /**
     * Create a new location.
     *
     * @param width the width of this area in pixels
     * @param height the height of this area in pixels
     * @throws IllegalArgumentException if the given width and height are not big enough to fit a single human in the area
     */
    public Location(double width, double height) {
        layoutCheck(width);
        layoutCheck(height);

        this.area = new Pane();
        this.area.setPrefSize(width, height);
        this.area.setMinSize(width, height);
        this.area.setMaxSize(width, height);
        clip(this.area);

        this.population = FXCollections.observableArrayList();

        this.spatialHash = new SpatialHash(this);
        updateHash();

        initEvents();
    }

    /**
     * Initialise all event listeners.
     */
    private void initEvents() {
        //Listener throws an IllegalStateException if the current population count is above the maximum population count.
        population.addListener((ListChangeListener<Human>) change -> {
            while(change.next()) {

                for (Human human : change.getAddedSubList()) {
                    setPosition(human.getModel());
                    area.getChildren().add(human.getModel());
                }

                for (Human human : change.getRemoved()) {
                    area.getChildren().remove(human.getModel());
                }
            }
        });
    }

    /**
     * Set a new position for a human's model given the layout boundaries of this location's area.
     *
     * @param model a human's graphical representation
     */
    private void setPosition(Model model) {
        model.setCenterX(HUMAN_RADIUS +  Math.random() * (area.getPrefWidth() - Model.HUMAN_DIAMETER));
        model.setCenterY(HUMAN_RADIUS +  Math.random() * (area.getPrefHeight() - Model.HUMAN_DIAMETER));
    }

    /**
     * Check that the length of a layout bound is large enough to allow for a human to fit in the area.
     *
     * @param length the length of a layout bound
     * @throws NullPointerException if the given length is less than {@value Model#HUMAN_DIAMETER}.
     */
    private void layoutCheck(double length) {
        if (length < Model.HUMAN_DIAMETER) {
            throw new IllegalArgumentException(
                    Error.ERROR_TAG + " Given length will not allow for this area to hold any humans: " + length);
        }
    }

    //---------------------------- Simulator actions ----------------------------

    /**
     * Update the spatial hash.
     */
    public void updateHash() {
        spatialHash.update();
    }

    /**
     * Adjust the velocity of the population such that they do not move past the walls.
     */
    public void wallCollisions() {
        for (Human human : population) {
            Model model = human.getModel();
            Point2D velocity = model.getVelocity();

            boolean onLeftWall = model.getCenterX() - model.getRadius() <= 0 && velocity.getX() < 0;

            boolean onRightWall = model.getCenterX() + model.getRadius() >= area.getPrefWidth() && velocity.getX() > 0;

            boolean onBottomWall = model.getCenterY() - model.getRadius() <= 0 && velocity.getY() < 0;

            boolean onTopWall = model.getCenterY() + model.getRadius() >= area.getPrefHeight() && velocity.getY() > 0;

            if(onLeftWall) {
                model.setCenterX(HUMAN_RADIUS);
                model.setVelocity(- velocity.getX(), velocity.getY());
            } else if (onRightWall) {
                model.setCenterX(area.getPrefWidth() - HUMAN_RADIUS);
                model.setVelocity(- velocity.getX(), velocity.getY());
            }

            if (onBottomWall) {
                model.setCenterY(HUMAN_RADIUS);
                model.setVelocity(velocity.getX(), - velocity.getY());
            } else if (onTopWall) {
                model.setCenterY(area.getPrefHeight() - HUMAN_RADIUS);
                model.setVelocity(velocity.getX(), - velocity.getY());
            }

        }
    }

    //---------------------------- Getters ----------------------------

    /**
     * Getter for {@link #area}.
     *
     * @return {@link #area}
     */
    public Pane getArea() {
        return area;
    }

    /**
     * Getter for {@link #spatialHash}.
     *
     * @return {@link #spatialHash}
     */
    public SpatialHash getSpatialHash() {
        return spatialHash;
    }

    /**
     * Getter for {@link #population}.
     *
     * @return {@link #population}
     */
    public ObservableList<Human> getPopulation() {
        return population;
    }

}

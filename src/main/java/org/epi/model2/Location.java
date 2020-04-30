package org.epi.model2;

import javafx.geometry.Point2D;
import org.epi.util.Error;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;

import static org.epi.model2.Model.HUMAN_RADIUS;

/** A simple model of a location.
 * The class is used as a graphical representation of a location in the simulator.*/
public class Location {

    /** Maximum number of humans that can fit in the area without them overlapping.*/
    private final int MAX_POPULATION;

    /** The diameter of a human.*/
    private static final double HUMAN_DIAMETER = 2 * HUMAN_RADIUS;

    /** The width of this area in pixels.*/
    private final DoubleProperty width;

    /** The height of this area in pixels.*/
    private final DoubleProperty height;

    /** The graphical representation of this location.*/
    private final Pane area;

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

        this.MAX_POPULATION = (int) Math.ceil(width * height / (HUMAN_DIAMETER * HUMAN_DIAMETER));

        this.width = new SimpleDoubleProperty(width);
        this.height = new SimpleDoubleProperty(height);

        this.area = new Pane();
        setConstantSize();

        population = new SimpleListProperty<>();

        initEvents();
    }

    /**
     * Set the area size to be constant.
     */
    private void setConstantSize() {
        area.setPrefSize(width.get(), height.get());
        area.setMinSize(width.get(), height.get());
        area.setMaxSize(width.get(), height.get());
    }

    /**
     * Initialise all event listeners.
     */
    private void initEvents() {
        //Listener throws an IllegalStateException if the current population count is above the maximum population count.
        population.addListener((ListChangeListener<Human>) change -> {
            while(change.next()) {
                populationCheck();

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
        model.setCenterX(HUMAN_RADIUS +  Math.random() * (width.get() - HUMAN_DIAMETER));
        model.setCenterY(HUMAN_RADIUS +  Math.random() * (height.get() - HUMAN_DIAMETER));
    }

    /**
     * Check that the length of a layout bound is large enough to allow for a human to fit in the area.
     *
     * @param length the length of a layout bound
     * @throws NullPointerException if the given length is less than {@value HUMAN_DIAMETER}.
     */
    private void layoutCheck(double length) {
        if (length < HUMAN_DIAMETER) {
            throw new IllegalArgumentException(
                    Error.ERROR_TAG + " Given length will not allow for this area to hold any humans: " + length);
        }
    }

    /**
     * Check that the current population in the location is not over maximum capacity.
     *
     * @throws IllegalStateException if the current population count is above the maximum population count.
     */
    private void populationCheck() {
        if (population.size() > MAX_POPULATION) {
            throw new IllegalStateException(
                    String.format(Error.ERROR_TAG + " This location's population count is above maximum capacity (%d): %d",
                            MAX_POPULATION, population.size()));
        }
    }

    //---------------------------- Simulator actions ----------------------------

    /**
     * Adjust the velocity of the population such that they do not move past the walls.
     */
    public void wallCollisions() {
        for (Human human :population) {
            Model model = human.getModel();
            Point2D velocity = model.getVelocity();

            boolean onLeftWall = model.getCenterX() - model.getRadius() <= 0 && velocity.getX() < 0;

            boolean onRightWall = model.getCenterX() + model.getRadius() >= width.get() && velocity.getX() > 0;

            boolean onBottomWall = model.getCenterY() - model.getRadius() <= 0 && velocity.getY() < 0;

            boolean onTopWall = model.getCenterY() + model.getRadius() >= height.get() && velocity.getY() > 0;

            if (onLeftWall || onRightWall) {
                model.setVelocity(- velocity.getX(), velocity.getY());
            }

            if (onBottomWall || onTopWall) {
                model.setVelocity(velocity.getX(), - velocity.getY());
            }
        }
    }

    //---------------------------- Getters ----------------------------

    /**
     * Getter for {@link #area}
     *
     * @return {@link #area}
     */
    public Pane getArea() {
        return area;
    }

    /**
     * Getter for {@link #population}
     *
     * @return {@link #population}
     */
    public ObservableList<Human> getPopulation() {
        return population;
    }

}

package org.epi.model.world;

import org.epi.model.human.Human;
import org.epi.model.human.Model;
import org.epi.util.Error;

import javafx.collections.FXCollections;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.geometry.Point2D;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.stream.Collectors;

import static org.epi.model.human.Behaviour.CONTACT_TRACING;
import static org.epi.model.human.Model.HUMAN_RADIUS;
import static org.epi.model.human.Model.distance;
import static org.epi.util.Clip.clip;

/** A simple model of a location.
 * The class is used as a graphical representation of a location in the simulator.*/
public class Location {

    /** The graphical representation of this location.*/
    private final Pane area;

    /** The spatial hash of humans in the area.*/
    private final SpatialHash spatialHash;

    /** The contact network.*/
    private final ObservableList<Line> contactNetwork;

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

        this.contactNetwork = FXCollections.observableArrayList();

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

        contactNetwork.addListener((ListChangeListener<Line>) change -> {
            while (change.next()) {
                area.getChildren().addAll(change.getAddedSubList());
                area.getChildren().removeAll(change.getRemoved());
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

    /**
     * Update the contact tracing network.
     */
    public void updateContactNetwork() {
        contactNetwork.clear();

        List<Human> sickUsers = population.parallelStream()
                .filter(human -> human.getModel().getBehaviour() == CONTACT_TRACING)
                .filter(Human::isSick)
                .collect(Collectors.toList());

        for (Human sickUser : sickUsers) {
            sickUser.getNearby().stream()
                    .map(Human::getModel)
                    .filter(user -> distance(user, sickUser.getModel()) <= 5.5 * HUMAN_RADIUS) // Multiplier is a preference.
                    .forEach(user -> drawContact(user, sickUser.getModel()));
        }

        contactNetwork.forEach(Line::toBack);
    }

    //---------------------------- Helper methods ----------------------------

    /**
     * Draw a line between two humans in this location.
     *
     * @param human1 a human
     * @param human2 a human
     */
    private void drawContact(Model human1, Model human2) {
        Line contact = new Line(human1.getCenterX(), human1.getCenterY(), human2.getCenterX(), human2.getCenterY());
        contact.setOpacity(0.25);
        contact.setFill(Color.DIMGRAY);

        contactNetwork.add(contact);
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

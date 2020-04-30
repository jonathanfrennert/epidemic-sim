package org.epi.model2;

import org.epi.model.StatusType;
import org.epi.util.Error;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Objects;

/** A simple model of a human.
 * The main actors in the simulator.*/
public class Human {

    /** The location of this human.*/
    private final Property<Location> location;

    /** The graphical view of this human.*/
    private final Model model;

    /** The health status of this human.*/
    private StatusType status;

    /** The immune system protecting this human.*/
    private final ImmuneSystem immuneSystem;

    /** The pathogen infecting this human.*/
    private Pathogen pathogen;

    //---------------------------- Constructor ----------------------------

    /**
     * Create a human at a given location
     *
     * @param behaviour the behaviour of this human
     * @param location the location of this human
     * @throws NullPointerException if the given parameter is null
     */
    public Human(Location location, Behaviour behaviour) {
        Objects.requireNonNull(behaviour, Error.getNullMsg("behaviour"));
        Objects.requireNonNull(location, Error.getNullMsg("location"));

        this.immuneSystem = new ImmuneSystem(this);

        this.status = StatusType.HEALTHY;

        this.model = new Model(this, behaviour);

        this.location = new SimpleObjectProperty<>(location);
        location.getPopulation().add(this);

        initEvents();
    }

    /**
     * Initialise all event listeners.
     */
    private void initEvents() {
        // If the location of the human is changed, they switch population.
        location.addListener((observable, oldValue, newValue) -> {
            oldValue.getPopulation().remove(Human.this);
            newValue.getPopulation().add(Human.this);
        });
    }

    //---------------------------- Helper methods ----------------------------

    /**
     * Check if this human is infected with a pathogen.
     *
     * @return true if this human is infected, otherwise false
     */
    private boolean isInfected() {
        return pathogen != null;
    }

    /**
     * Update the health status of this human.
     */
    private void status() {
        if (immuneSystem.isImmune()) {
            status = StatusType.RECOVERED;
        } else if (isInfected()) {
            status = StatusType.INFECTED;
        } else {
            status = StatusType.HEALTHY;
        }
    }

    //---------------------------- Simulator actions ----------------------------

    /**
     * If this human is infected, spread the pathogen and let the pathogen live.
     *
     * @param elapsedSeconds the number of seconds elapsed since the pathogen was last updated
     * @throws IllegalArgumentException if the given parameter is negative
     */
    private void pathogen(double elapsedSeconds) {
        Error.nonNegativeCheck(elapsedSeconds);

        if (isInfected()) {
            pathogen.infect();

            pathogen.live(elapsedSeconds);
        }
    }

    /**
     * Let the immune system live and defend this human.
     *
     * @param elapsedSeconds the number of seconds elapsed since the immune system was last updated
     * @throws IllegalArgumentException if the given parameter is negative
     */
    private void immuneSystem(double elapsedSeconds) {
        Error.nonNegativeCheck(elapsedSeconds);

        immuneSystem.live(elapsedSeconds);

        if (isInfected()) {
            immuneSystem.defend();
        }
    }

    /**
     * Set the correct fill for the model and move the model.
     *
     * @param elapsedSeconds the number of seconds elapsed since this human's model was last updated
     * @throws IllegalArgumentException if the given parameter is negative
     */
    private void model(double elapsedSeconds) {
        Error.nonNegativeCheck(elapsedSeconds);

        status();
        model.fill();

        model.move(elapsedSeconds);
    }

    //---------------------------- Getters & Setters ----------------------------

    /**
     * Getter for {@link #immuneSystem}.
     *
     * @return {@link #immuneSystem}
     */
    public Location getLocation() {
        return location.getValue();
    }

    /**
     * Setter for {@link #location}.
     *
     * @param location {@link #location}
     * @throws NullPointerException if the given location is null
     */
    public void setLocation(Location location) {
        Objects.requireNonNull(location, Error.getNullMsg("location"));

        this.location.setValue(location);
    }

    /**
     * Getter for {@link #model}.
     *
     * @return {@link #model}
     */
    public Model getModel() {
        return model;
    }

    /**
     * Getter for {@link #status}
     *
     * @return {@link #status}
     */
    public StatusType getStatus() {
        return status;
    }

    /**
     * Getter for {@link #immuneSystem}.
     *
     * @return {@link #immuneSystem}
     */
    public ImmuneSystem getImmuneSystem() {
        return immuneSystem;
    }

    /**
     * Getter for {@link #pathogen}.
     *
     * @return {@link #pathogen}
     */
    public Pathogen getPathogen() {
        return pathogen;
    }

    /**
     * Setter for {@link #pathogen}.
     *
     * @param pathogen {@link #pathogen}
     */
    public void setPathogen(Pathogen pathogen) {
        this.pathogen = pathogen;
    }

}

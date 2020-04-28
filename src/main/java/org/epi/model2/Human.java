package org.epi.model2;

import org.epi.model.StatusType;
import org.epi.util.Error;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Objects;

/** A simple model of a human.
 * The main actors in the simulator.*/
public class Human {

    /** The graphical view of this human.*/
    private final View view;

    /** Health status of the human.*/
    private StatusType status;

    /** The location of this human.*/
    private final Property<Location> location;

    /** The immune system protecting this human.*/
    private final ImmuneSystem immuneSystem;

    /** The pathogen infecting this human.*/
    private Property<Pathogen> pathogen;

    //---------------------------- Constructor & associated helpers ----------------------------

    /**
     * Create a human at a given location
     *
     * @param behaviour the behaviour of this human
     * @param location the location of this human
     * @throws NullPointerException if the given parameter is null
     */
    public Human(Behaviour behaviour, Location location) {
        Objects.requireNonNull(behaviour, Error.getNullMsg("behaviour"));
        Objects.requireNonNull(location, Error.getNullMsg("location"));

        this.view = new View(this, behaviour);

        this.status = StatusType.HEALTHY;

        this.location = new SimpleObjectProperty<>(location);
        location.getPopulation().add(this);

        this.immuneSystem = new ImmuneSystem(this);

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

        // Immune system defense.
        pathogen.addListener((observable, oldValue, newValue) -> {
            boolean isIntruder = newValue != null;

            if (isIntruder) {
                immuneSystem.defend();
            } else {
                immuneSystem.learn(oldValue);
            }

        });

    }



    //---------------------------- Simulator actions ----------------------------



    //---------------------------- Getters & Setters ----------------------------

    /**
     * Getter for {@link #view}.
     *
     * @return {@link #view}
     */
    public View getView() {
        return view;
    }

    /**
     * Getter for {@link #status}
     *
     * @return {@link #status}
     */
    public StatusType getStatus() {

    }

    /**
     * Setter for {@link #status}
     *
     * @param status the status of this human
     * @throws NullPointerException if the given parameter is null
     */
    public void setStatus(StatusType status) {
        Objects.requireNonNull(status,Error.getNullMsg("status"));

        this.status = status;
    }

    /**
     * Getter for {@link #immuneSystem}.
     *
     * @return {@link #immuneSystem}
     */
    public Location getLocation() {
        return location.getValue();
    }

    /**
     * Getter for {@link #location} property.
     *
     * @return {@link #location} property
     */
    public Property<Location> locationProperty() {
        return location;
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
     * Setter for {@link #pathogen}.
     *
     * @throws NullPointerException if the given pathogen is null
     */
    public void setPathogen(Pathogen pathogen) {
        Objects.requireNonNull(pathogen, "pathogen");
        this.pathogen = pathogen;
    }

    /**
     * Getter for {@link #pathogen}.
     *
     * @return {@link #pathogen}
     */
    public Pathogen getPathogen() {
        return pathogen;
    }

}

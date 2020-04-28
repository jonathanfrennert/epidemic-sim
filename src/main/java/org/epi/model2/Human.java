package org.epi.model2;

import org.epi.util.Error;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Objects;

/** A simple model of a human.
 * The main actors in the simulator.*/
public class Human {

    /** The graphical view of this human.*/
    private final View view;

    /** The location of this human.*/
    private final Property<Location> location;

    /** The immune system protecting this human.*/
    private final ImmuneSystem immuneSystem;

    /** The disease afflicting this human.*/
    private Disease disease;

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

        this.view = new View(behaviour);
        view.setHost(this);

        this.location = new SimpleObjectProperty<>(location);
        location.getPopulation().add(this);

        this.immuneSystem = new ImmuneSystem();
        immuneSystem.setHost(this);

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
     * Setter for {@link #disease}.
     *
     * @throws NullPointerException if the given disease is null
     */
    public void setDisease(Disease disease) {
        Objects.requireNonNull(disease, "disease");
        this.disease = disease;
    }

    /**
     * Getter for {@link #disease}.
     *
     * @return {@link #disease}
     */
    public Disease getDisease() {
        return disease;
    }

}

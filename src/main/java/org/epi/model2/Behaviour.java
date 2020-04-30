package org.epi.model2;

import org.epi.util.Error;

import java.util.Objects;

/** State class for the behaviour of humans.*/
public abstract class Behaviour {

    /** Backreference to this behaviour's character model.*/
    private Model model;

    //---------------------------- Constructors & associated helpers ----------------------------

    /**
     * Create a behaviour.
     */
    public Behaviour() {
        this.model = null;
    }

    /** Set the initial velocity for this model.*/
    abstract void initVelocity();

    //---------------------------- Simulator action ----------------------------

    /**
     * Return this model's velocity in taking account of another human's model in the population.
     *
     * @param that a model of a human in the population
     */
    abstract void adjustToOther(Model that);

    //---------------------------- Setter ----------------------------

    /**
     * Set a backreference for the character model which this behaviour effects.
     *
     * @param model the model effected by this behaviour
     * @throws NullPointerException if the given parameter is null
     */
    public void setModel(Model model) {
        Objects.requireNonNull(model, Error.getNullMsg("model"));

        this.model = model;
    }

}

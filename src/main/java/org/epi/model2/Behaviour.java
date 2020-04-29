package org.epi.model2;

import org.epi.util.Error;

import java.util.Objects;

/** State class for the behaviour of humans.*/
public abstract class Behaviour {

    /** Backreference to this behaviour's character model.*/
    private Model model;

    /**
     * Set a backreference for the character model which this behaviour effects.
     *
     * @param model the model effected by this behaviour
     * @throws NullPointerException if the given parameter is null
     */
    public void setModel(Model model) {
        Objects.requireNonNull(model, Error.getNullMsg("model"));

        this.model = model;
        setVelocity();
    }

    /** Set the initial velocity for this model.*/
    abstract void setVelocity();

    /**
     * Return this model's velocity in taking account of another human's model in the population.
     *
     * @param model the model of a human in the population
     */
    abstract void adjustToOther(Model model);

}

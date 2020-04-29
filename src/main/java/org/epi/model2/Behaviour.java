package org.epi.model2;

/** State class for the behaviour of humans.*/
public abstract class Behaviour {

    /** Backreference to this behaviour's character model.*/
    private Model model;

    /**
     * Set a backreference for the character model which this behaviour effects.
     *
     * @param model the model effected by this behaviour
     */
    public void setModel(Model model) {
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
    abstract void adjustToOthers(Model model);

}

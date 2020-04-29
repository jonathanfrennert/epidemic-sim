package org.epi.model2;

import javafx.collections.ObservableList;

/** State class for the behaviour of humans.*/
public abstract class Behaviour {

    /** Backreference to this behaviour's view.*/
    private View view;

    /**
     * Set a backreference for the view which this behaviour effects.
     *
     * @param view the view effected by this behaviour
     */
    public void setView(View view) {
        this.view = view;
        setVelocity();
    }

    /** Set the initial velocity for this view.*/
    abstract void setVelocity();

    /**
     * Adjust the view velocity depending on the surrounding population.
     *
     * @param population the population for the host.
     */
    abstract void adjustToOthers(ObservableList<Human> population);

}

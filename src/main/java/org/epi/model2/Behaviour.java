package org.epi.model2;

import javafx.collections.ObservableList;

/** State class for the behaviour of humans.*/
public abstract class Behaviour {

    /** Backreference to this behaviours view.*/
    private View view;

    public void setView(View view) {
        this.view = view;
        setVelocity();
    }

    /** Set the initial velocity for this view.*/
    private void setVelocity() {
    }

    /**
     * Adjust the view velocity depending on the surrounding population.
     *
     * @param population the population for the host.
     */
    public void adjustToOthers(ObservableList<Human> population) {
    }

}

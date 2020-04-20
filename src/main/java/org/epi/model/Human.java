package org.epi.model;

/** The superclass for all humans in the simulation.*/
public abstract class Human {

    /** The representation of this human in the simulation view.*/
    private final BouncyCircle view;

    /** The health status this human.*/
    private final StatusType status;

    /**
     * Create a human.
     *
     * @param view
     * @param status
     */
    public Human(BouncyCircle view, StatusType status) {
        this.view = view;
        this.status = status;
    }

    /**
     * Getter for {@link Human#view}.
     *
     * @return the representation of this human in the simulation view
     */
    public BouncyCircle getView() {
        return view;
    }

    /**
     * Getter for {@link Human#status}.
     *
     * @return the health status of this human
     */
    public StatusType getStatus() {
        return status;
    }

}

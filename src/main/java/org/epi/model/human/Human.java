package org.epi.model.human;

import org.epi.model.BouncyCircle;
import org.epi.util.Error;

import java.util.Objects;

/** The superclass for all humans in the simulation.*/
public abstract class Human {

    /** The status of this human.*/
    private final StatusType status;

    /** The representation of this human in the simulation view.*/
    private final BouncyCircle view;

    /**
     * Constructor for a human
     *
     * @param status the status type of this human
     * @param view The view of this human in the simulation view
     * @throws NullPointerException if the given view is null
     */
    public Human(StatusType status, BouncyCircle view) {
        Objects.requireNonNull(status, Error.getNullMsg("status"));
        Objects.requireNonNull(view, Error.getNullMsg("view"));

        this.status = status;
        this.view = view;
    }

    /**
     * Getter for {@link Human#status}.
     *
     * @return {@link Human#status}
     */
    public StatusType getStatus() {
        return status;
    }

    /**
     * Getter for {@link Human#view}.
     *
     * @return {@link Human#view}
     */
    public BouncyCircle getView() {
        return view;
    }

}
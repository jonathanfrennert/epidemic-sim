package org.epi.model.human;

import org.epi.model.BouncyCircle;
import org.epi.util.ErrorUtil;

import java.util.Objects;

/** The superclass for all humans in the simulation.*/
public abstract class Human {

    /** The representation of this human in the simulation view.*/
    private final BouncyCircle view;

    /**
     * Constructor for a human
     *
     * @param view The view of this human in the simulation view
     * @throws NullPointerException if the given view is null
     */
    public Human(BouncyCircle view) {
        Objects.requireNonNull(view, ErrorUtil.getNullMsg("view"));

        this.view = view;
    }

    /**
     * Getter for {@link Human#view}.
     *
     * @return the representation of this human in the simulation view
     */
    public BouncyCircle getView() {
        return view;
    }

}

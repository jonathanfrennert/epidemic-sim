package org.epi.model;

/** Indicates the state of the simulation for the given simulator.*/
public enum SimulationState {
    RUN ("btn-icon-pause"),
    PAUSE ("btn-icon-run"),
    ENDED ("btn-icon-run");

    /** The style class of the play button for this state.*/
    public String styleClass;

    /**
     * Create a simulation state.
     *
     * @param styleClass the style class for the play button of this state
     */
    SimulationState(String styleClass) {
        this.styleClass = styleClass;
    }

}

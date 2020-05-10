package org.epi.model;

/** Indicates the state of the simulation for the given simulator.*/
public enum SimulationState {
    RUN ("btn-icon-run"),
    PAUSE ("btn-icon-pause"),
    ENDED ("btn-icon-pause");

    /** The style class of the play button for this state.*/
    public String styleClass;

    /**
     * Create a simulation state.
     *
     * @param styleClass the style class for the play button of this state
     */
    private SimulationState(String styleClass) {
        this.styleClass = styleClass;
    }
}

package org.epi.model;

import javafx.beans.property.*;
import org.epi.util.Error;

import javafx.scene.layout.Pane;

import java.util.Objects;

/** Utility class for getting real-time statistics from a simulator.*/
public class Statistics {

    /** Initial number of deceased humans.*/
    private static final int INIT_DECEASED = 0;

    /** Initial population count.*/
    private final IntegerProperty initPopulationCount;

    /** The number of healthy people in the given simulation.*/
    private final IntegerProperty healthyCount;

    /** The number of infected humans in the given simulation.*/
    private final IntegerProperty infectedCount;

    /** The number of recovered humans in the given simulation.*/
    private final IntegerProperty recoveredCount;

    /** The number difference between the current population count and the initial population count.*/
    private final IntegerProperty deceasedCount;

    /**
     * Constructor for simulator statistics; a real-time count for status populations.
     *
     * @param worldView a simulator world view
     * @throws NullPointerException if the given parameter is null
     */
    public Statistics(Pane worldView) {
        Objects.requireNonNull(worldView, Error.getNullMsg("world view"));

        healthyCount = new SimpleIntegerProperty(getPopulationCountOf(worldView, StatusType.HEALTHY));
        infectedCount = new SimpleIntegerProperty(getPopulationCountOf(worldView, StatusType.INFECTED));
        recoveredCount = new SimpleIntegerProperty(getPopulationCountOf(worldView, StatusType.RECOVERED));
        deceasedCount = new SimpleIntegerProperty(INIT_DECEASED);

        initPopulationCount = new SimpleIntegerProperty(healthyCount.get() + infectedCount.get() + recoveredCount.get());
    }

    /**
     * Update the population counts. Meant to be called in the handle method of simulator's world time.
     *
     * @param worldView a simulator world view
     * @throws NullPointerException if the given parameters is null
     */
    public void updateCount(Pane worldView) {
        Objects.requireNonNull(worldView, Error.getNullMsg("world view"));

        healthyCount.set(getPopulationCountOf(worldView, StatusType.HEALTHY));
        infectedCount.set(getPopulationCountOf(worldView, StatusType.INFECTED));
        recoveredCount.set(getPopulationCountOf(worldView, StatusType.RECOVERED));

        deceasedCount.set(initPopulationCount.get() - healthyCount.get() - infectedCount.get() - recoveredCount.get());
    }

    /**
     * Lists of the statistics. Currently intended for diagnostics of simulator.
     *
     * @return Populations count's for the simulator
     */
    @Override
    public String toString() {
        return "Statistics{" +
                "initPopulationCount=" + initPopulationCount +
                ", healthyCount=" + healthyCount +
                ", infectedCount=" + infectedCount +
                ", recoveredCount=" + recoveredCount +
                ", deceasedCount=" + deceasedCount +
                '}';
    }

    /**
     * Used to show population statistics in application view.
     *
     * @return Populations count's for the simulator
     */
    public ReadOnlyStringProperty textProperty() {
        return new SimpleStringProperty(this.toString());
    }

    //---------------------------- Getters ----------------------------

    /**
     * Get the number of humans in the world view who have a given status
     *
     * @param worldView the world view of a simulation
     * @param status a status
     * @return the number of humans in the world view of that status
     */
    private static int getPopulationCountOf(Pane worldView, StatusType status) {
        return worldView.getChildren().filtered(node -> node instanceof Human && ((Human) node).getStatus() == status).size();
    }

    /**
     * Getter for {@link #healthyCount}
     *
     * @return {@link #healthyCount}
     */
    public int getHealthyCount() {
        return healthyCount.get();
    }

    /**
     * Getter for {@link #healthyCount} {@link IntegerProperty}
     *
     * @return {@link #healthyCount}
     */
    public IntegerProperty healthyCountProperty() {
        return healthyCount;
    }

    /**
     * Getter for {@link #infectedCount}
     *
     * @return {@link #infectedCount}
     */
    public int getInfectedCount() {
        return infectedCount.get();
    }

    /**
     * Getter for {@link #infectedCount} {@link IntegerProperty}
     *
     * @return {@link #infectedCount}
     */
    public IntegerProperty infectedCountProperty() {
        return infectedCount;
    }

    /**
     * Getter for {@link #recoveredCount}
     *
     * @return {@link #recoveredCount}
     */
    public int getRecoveredCount() {
        return recoveredCount.get();
    }

    /**
     * Getter for {@link #recoveredCount} {@link IntegerProperty}
     *
     * @return {@link #recoveredCount}
     */
    public IntegerProperty recoveredCountProperty() {
        return recoveredCount;
    }

    /**
     * Getter for {@link #recoveredCount}
     *
     * @return {@link #recoveredCount}
     */
    public int getDeceasedCount() {
        return deceasedCount.get();
    }

    /**
     * Getter for {@link #deceasedCount} {@link IntegerProperty}
     *
     * @return {@link #deceasedCount}
     */
    public IntegerProperty deceasedCountProperty() {
        return deceasedCount;
    }

    /**
     * Getter for {@link #initPopulationCount}
     *
     * @return {@link #initPopulationCount}
     */
    public int getInitPopulationCount() {
        return initPopulationCount.get();
    }

    /**
     * Getter for {@link #initPopulationCount} {@link IntegerProperty}
     *
     * @return {@link #initPopulationCount}
     */
    public IntegerProperty initPopulationCountProperty() {
        return initPopulationCount;
    }

}

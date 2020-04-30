package org.epi.model;

import javafx.beans.property.*;
import org.epi.model2.Status;
import org.epi.util.Error;

import javafx.collections.ObservableList;

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

    /** Show the statistics. Only for diagnostics.*/
    private final ReadOnlyStringWrapper text = new ReadOnlyStringWrapper(this,
                                                                    "text",
                                                                "healthy = 99, infected = 1, recovered = 0, deceased = 0");

    /**
     * Constructor for simulator statistics; a real-time count for status populations.
     *
     * @param population a simulator's population
     * @throws NullPointerException if the given parameter is null
     */
    public Statistics(ObservableList<Human> population) {
        Objects.requireNonNull(population, Error.getNullMsg("population"));

        healthyCount = new SimpleIntegerProperty(getPopulationCountOf(population, Status.HEALTHY));
        infectedCount = new SimpleIntegerProperty(getPopulationCountOf(population, Status.INFECTED));
        recoveredCount = new SimpleIntegerProperty(getPopulationCountOf(population, Status.RECOVERED));
        deceasedCount = new SimpleIntegerProperty(INIT_DECEASED);

        initPopulationCount = new SimpleIntegerProperty(healthyCount.get() + infectedCount.get() + recoveredCount.get());
    }

    /**
     * Update the population counts. Meant to be called in the handle method of simulator's world time.
     *
     * @param population a simulator's population
     * @throws NullPointerException if the given parameters is null
     */
    public void updateCount(ObservableList<Human> population) {
        Objects.requireNonNull(population, Error.getNullMsg("world view"));

        healthyCount.set(getPopulationCountOf(population, Status.HEALTHY));
        infectedCount.set(getPopulationCountOf(population, Status.INFECTED));
        recoveredCount.set(getPopulationCountOf(population, Status.RECOVERED));

        deceasedCount.set(initPopulationCount.get() - healthyCount.get() - infectedCount.get() - recoveredCount.get());

        text.set(toString());
    }

    /**
     * Lists of the statistics. Currently intended for diagnostics of simulator.
     *
     * @return Populations count's for the simulator
     */
    @Override
    public String toString() {
        return "healthy = " + healthyCount.get() +
               ", infected = " + infectedCount.get() +
               ", recovered = " + recoveredCount.get() +
               ", deceased = " + deceasedCount.get() +
                ".";
    }

    //---------------------------- Getters ----------------------------

    /**
     * Getter for {@link #text} {@link ReadOnlyStringProperty}
     *
     * @return {@link #text}
     */
    public ReadOnlyStringProperty getTextProperty() {
        return text.getReadOnlyProperty();
    }

    /**
     * Get the number of humans of a given status in the population.
     *
     * @param population a simulator's population
     * @param status a status
     * @return the number of humans in the population of that status
     */
    private static int getPopulationCountOf(ObservableList<Human> population, Status status) {
        return population.filtered(human -> human.getStatus() == status).size();
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

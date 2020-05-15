package org.epi.model;

import org.epi.util.Error;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.Objects;

import static org.epi.model.SimulationState.ENDED;
import static org.epi.model.SimulationState.PAUSE;

/** The simulator class.
 *  Used to interface with all the simulator components.*/
public class Simulator {

    /** The state of the simulator.*/
    private final Property<SimulationState> simulationState;

    /** The simulator player.*/
    private final Player player;

    /** The world's statistics.*/
    private final Statistics statistics;

    /** The world.*/
    private final World world;

    /** The behaviour distribution for this simulator.*/
    private final BehaviourDistribution behaviourDistribution;

    /** The pathogen for this simulator.*/
    private final Pathogen pathogen;

    //---------------------------- Constructor ----------------------------

    /**
     * Initialise a simulator.
     *
     * @param world the simulated world
     * @param behaviourDistribution distribution of behaviours in the population
     * @param pathogen the simulated pathogen
     * @throws NullPointerException if the given parameters are null
     */
    public Simulator(World world, BehaviourDistribution behaviourDistribution, Pathogen pathogen) {
        Objects.requireNonNull(world, Error.getNullMsg("world"));
        Objects.requireNonNull(behaviourDistribution, Error.getNullMsg("behaviour distribution"));
        Objects.requireNonNull(pathogen, Error.getNullMsg("pathogen"));

        this.world = world;
        this.behaviourDistribution = behaviourDistribution;
        this.pathogen = pathogen;

        for (int i = 0; i < world.getSickTotal(); i++) {
            Human sick = new Human(world.getCity(), behaviourDistribution.sample());
            sick.setPathogen(pathogen.reproduce());
            sick.status();
            sick.getModel().fill();
        }

        for (int i = 0; i < world.getPopulationTotal() - world.getSickTotal(); i++) {
            new Human(world.getCity(), behaviourDistribution.sample());
        }

        this.statistics = new Statistics(world);
        this.simulationState = new SimpleObjectProperty<>(PAUSE);
        this.player = new Player(this);
    }

    //---------------------------- Simulator actions ----------------------------

    /**
     * Perform all updates for the simulator given the elapsed seconds.
     *
     * @param elapsedSeconds the number of seconds elapsed since the world was last updated.
     */
    public void update(double elapsedSeconds) {
        worldBefore(elapsedSeconds);
        pathogen(elapsedSeconds);
        immuneSystem(elapsedSeconds);
        model(elapsedSeconds);
        worldAfter();

        statistics.update();
    }

    //---------------------------- Helper methods ----------------------------

    /**
     * Perform all initial world changes in the elapsed seconds.
     *
     * @param elapsedSeconds the number of seconds elapsed since the world was last updated
     */
    private void worldBefore(double elapsedSeconds) {
        world.live(elapsedSeconds);
        world.collisions();
    }

    /**
     * Changes that happen after human and pathogen behaviour.
     */
    private void worldAfter() {
        world.contactNetwork();
    }

    /**
     * Perform all pathogen changes in the elapsed seconds.
     *
     * @param elapsedSeconds the number of seconds elapsed since the pathogen was last updated
     */
    private void pathogen(double elapsedSeconds) {
        new ArrayList<>(world.getCity().getPopulation()).stream().filter(Human::isSick).
                forEach(human -> human.pathogen(elapsedSeconds));
        new ArrayList<>(world.getQuarantine().getPopulation()).stream().filter(Human::isSick).
                forEach(human -> human.pathogen(elapsedSeconds));
    }



    /**
     * Perform all immune system changes in the elapsed seconds.
     *
     * @param elapsedSeconds the number of seconds elapsed since the human immune systems were last updated
     */
    private void immuneSystem(double elapsedSeconds) {
        world.getCity().getPopulation().parallelStream().forEach(human -> human.immuneSystem(elapsedSeconds));
        world.getQuarantine().getPopulation().parallelStream().forEach(human -> human.immuneSystem(elapsedSeconds));
    }

    /**
     * Perform all model changes in the elapsed seconds.
     *
     * @param elapsedSeconds the number of seconds elapsed since the human models were last updated
     */
    private void model(double elapsedSeconds) {
        world.getCity().getPopulation().forEach(human -> human.model(elapsedSeconds));
        world.getQuarantine().getPopulation().forEach(human -> human.model(elapsedSeconds));
    }

    /**
     * Check whether the world view has reached any end conditions.
     */
    public boolean ended() {
        boolean isPathogenGone = statistics.getSick() == 0;
        boolean isHumanityGone = statistics.getDeceased() == world.getPopulationTotal();

        return isPathogenGone || isHumanityGone;
    }

    /**
     * Reset the simulator to its initial state, with new positions for all the humans.
     *
     * @return a reset version of this simulator
     */
    public Simulator reset() {
        World world = new World(this.world.getPopulationTotal(),
                this.world.getSickTotal(),
                this.world.getQuarantineCapacity(),
                this.world.getDetectionRate(),
                this.world.getTestingFrequency());
        return new Simulator(world, behaviourDistribution, pathogen);
    }

    //---------------------------- Getters & Setters ----------------------------

    /**
     * Getter for {@link #simulationState}
     *
     * @return {@link #simulationState}
     */
    public SimulationState getSimulationState() {
        return simulationState.getValue();
    }

    /**
     * Getter for {@link #simulationState} property
     *
     * @return {@link #simulationState} property
     */
    public Property<SimulationState> getSimulationStateProperty() {
        return simulationState;
    }

    /**
     * Setter for {@link #simulationState}
     *
     * @param simulationState a simulation state
     * @throws NullPointerException if the given parameter is null
     */
    public void setSimulationState(SimulationState simulationState) {
        Objects.requireNonNull(simulationState, Error.getNullMsg("simulation state"));

        if (this.simulationState.getValue() == ENDED) {
            return;
        }

        this.simulationState.setValue(simulationState);
    }

    /**
     * Getter for {@link #player}.
     *
     * @return {@link #player}
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Getter for {@link #statistics}.
     *
     * @return {@link #statistics}
     */
    public Statistics getStatistics() {
        return statistics;
    }

    /**
     * Getter for {@link #world}.
     *
     * @return {@link #world}
     */
    public World getWorld() {
        return world;
    }

    /**
     * Getter for {@link #behaviourDistribution}.
     *
     * @return {@link #behaviourDistribution}
     */
    public BehaviourDistribution getBehaviourDistribution() {
        return behaviourDistribution;
    }

    /**
     * Getter for {@link #pathogen}.
     *
     * @return {@link #pathogen}
     */
    public Pathogen getPathogen() {
        return pathogen;
    }
}


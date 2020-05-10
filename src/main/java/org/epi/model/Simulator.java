package org.epi.model;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import org.epi.util.Error;

import java.util.ArrayList;
import java.util.Objects;

import static org.epi.model.SimulationState.*;

/** The simulator class.
 *  Used to interface with all the simulator components.*/
public class Simulator {

    /** The minimum population required for the simulation to run.*/
    private static final int MIN_POPULATION = 1;
    /** Maximum number of humans that can be handled without frame performance
     * issues and humans going over location edges.*/
    private static final int MAX_POPULATION = 300;

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

    /** The population total for this simulator.*/
    private final int populationTotal;

    /** The infected total for this simulator.*/
    private final int infectedTotal;

    //---------------------------- Constructor ----------------------------

    /**
     * Initialise a simulator.
     *
     * @param world the simulated world
     * @param behaviourDistribution distribution of behaviours in the population
     * @param pathogen the simulated pathogen
     * @param populationTotal the population total
     * @param infectedTotal the number of infected in the population
     * @throws NullPointerException if the world, behaviour distribution, or pathogen is null
     * @throws IllegalArgumentException if the population total is less than {@value MIN_POPULATION}
     *                                  or larger than the {@value MAX_POPULATION} or if the infected total is less than
     *                                  {@value MIN_POPULATION} or more than the population total
     */
    public Simulator(World world, BehaviourDistribution behaviourDistribution, Pathogen pathogen,
                     int populationTotal, int infectedTotal) {
        Objects.requireNonNull(world, Error.getNullMsg("world"));
        Objects.requireNonNull(behaviourDistribution, Error.getNullMsg("behaviour distribution"));
        Objects.requireNonNull(pathogen, Error.getNullMsg("pathogen"));
        Error.intervalCheck("population", MIN_POPULATION, MAX_POPULATION, populationTotal);
        Error.intervalCheck("infected population", MIN_POPULATION, populationTotal, infectedTotal);

        this.world = world;
        this.behaviourDistribution = behaviourDistribution;
        this.pathogen = pathogen;
        this.populationTotal = populationTotal;
        this.infectedTotal = infectedTotal;

        for (int i = 0; i < infectedTotal; i++) {
            Human infected = new Human(world.getCity(), behaviourDistribution.sample());
            infected.setPathogen(pathogen.reproduce());
            infected.status();
        }

        for (int i = 0; i < populationTotal - infectedTotal; i++) {
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
        world(elapsedSeconds);
        pathogen(elapsedSeconds);
        immuneSystem(elapsedSeconds);
        model(elapsedSeconds);
        statistics.update();
    }

    //---------------------------- Helper methods ----------------------------

    /**
     * Perform all world changes in the elapsed seconds.
     *
     * @param elapsedSeconds the number of seconds elapsed since the world was last updated
     */
    private void world(double elapsedSeconds) {
        world.live(elapsedSeconds);
        world.collisions();
    }

    /**
     * Perform all pathogen changes in the elapsed seconds.
     *
     * @param elapsedSeconds the number of seconds elapsed since the pathogen was last updated
     */
    private void pathogen(double elapsedSeconds) {
        new ArrayList<>(world.getCity().getPopulation()).forEach(human -> human.pathogen(elapsedSeconds));
        new ArrayList<>(world.getQuarantine().getPopulation()).forEach(human -> human.pathogen(elapsedSeconds));
    }

    /**
     * Perform all immune system changes in the elapsed seconds.
     *
     * @param elapsedSeconds the number of seconds elapsed since the human immune systems were last updated
     */
    private void immuneSystem(double elapsedSeconds) {
        world.getCity().getPopulation().forEach(human -> human.immuneSystem(elapsedSeconds));
        world.getQuarantine().getPopulation().forEach(human -> human.immuneSystem(elapsedSeconds));
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
        boolean isPathogenGone = statistics.getInfected() == 0;
        boolean isHumanityGone = statistics.getDeceased() == statistics.getInitPop();

        return isPathogenGone || isHumanityGone;
    }

    /**
     * Reset the simulator to its initial state, with new positions for all the humans.
     *
     * @return a reset version of this simulator
     */
    public Simulator reset() {
        World world = new World(this.world.getDetectionRate(), this.world.getTestingFrequency());
        return new Simulator(world, behaviourDistribution, pathogen, populationTotal, infectedTotal);
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
     * @param state a state
     * @throws NullPointerException if the given parameter is null
     */
    public void setSimulationState(SimulationState state) {
        Objects.requireNonNull(state, Error.getNullMsg("state"));

        if (simulationState.getValue() == ENDED) {
            return;
        }

        this.simulationState.setValue(state);
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
     * Getter for {@link #world}.
     *
     * @return {@link #world}
     */
    public World getWorld() {
        return world;
    }

    /**
     * Getter for {@link #statistics}.
     *
     * @return {@link #statistics}
     */
    public Statistics getStatistics() {
        return statistics;
    }

}


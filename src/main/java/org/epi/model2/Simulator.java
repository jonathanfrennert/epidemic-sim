package org.epi.model2;

import org.epi.util.Error;

import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

import java.util.Objects;

/** The simulator class.
 *  Used to interface with all the simulator components.*/
public class Simulator {

    /** The minimum population required for the simulation to run.*/
    private static final int MIN_POPULATION = 1;

    /** The simulator timer.*/
    private final AnimationTimer timer = new AnimationTimer() {

        /** The order magnitude of nano units.*/
        private static final double NANO = 1 / 1000_000_000.00;

        /** Last time world view was updated.*/
        final LongProperty lastUpdateTime = new SimpleLongProperty(0);

        /**
         * Handles all actions that happen in each frame.
         *
         * @param timestamp The timestamp of the current frame given in nanoseconds. This value will be the same for all
         *                  AnimationTimers called during one frame.
         */
        @Override
        public void handle(long timestamp) {
            if (lastUpdateTime.get() > 0) {
                double elapsedSeconds = (timestamp - lastUpdateTime.get()) * NANO;

                world.live(elapsedSeconds);
            }

            if (endingIsReached()) {
                timer.stop();
            }

            lastUpdateTime.set(timestamp);
        }

    };

    /** The world.*/
    private final World world;

    /** The world statistics.*/
    private final Statistics statistics;

    //---------------------------- Constructor ----------------------------

    /**
     * Initialise a simulator.
     *
     * @param popTotal the population total
     * @param world the simulated world
     * @param behaveDist distribution of behaviour in the population
     * @param pathogen the simulated pathogen
     * @throws NullPointerException if the world, behaviour distribution, or pathogen is null
     * @throws IllegalArgumentException if the population total is less than {@value MIN_POPULATION} or larger than the maximum capacity for the city
     */
    public Simulator(double popTotal, World world, BehaviourDistribution behaveDist, Pathogen pathogen) {
        Error.intervalCheck("population", MIN_POPULATION, world.getCity().getMaxPop(), popTotal);
        Objects.requireNonNull(world, Error.getNullMsg("world"));
        Objects.requireNonNull(behaveDist, Error.getNullMsg("behaviour distribution"));
        Objects.requireNonNull(pathogen, Error.getNullMsg("pathogen"));

        this.world = world;

        this.statistics = new Statistics(world);
    }

    /**
     * Check whether the world view has reached any end conditions
     */
    private boolean endingIsReached() {
        boolean isDiseaseGone = statistics.getInfected() == 0;
        boolean isHumanityGone = statistics.getDeceased() == statistics.getInitPop();

        return isDiseaseGone || isHumanityGone;
    }

    //---------------------------- Getters ----------------------------

    /**
     * Getter for {@link #timer}.
     *
     * @return {@link #timer}
     */
    public AnimationTimer getTimer() {
        return timer;
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


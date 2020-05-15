package org.epi.model;

import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import org.epi.util.Error;

import java.util.Objects;

import static org.epi.model.SimulationState.ENDED;
import static org.epi.model.SimulationState.RUN;

/** Timer for the simulator.*/
public class Player extends AnimationTimer {

    /** The order magnitude of nano units.*/
    private static final double NANO = 1 / 1000_000_000.00;

    /** Backreference to the simulator for this player.*/
    private final Simulator simulator;

    /** Last time the simulator was updated.*/
    public final LongProperty lastUpdateTime = new SimpleLongProperty(0);

    /**
     * Create a simulator player.
     *
     * @param simulator the simulator for this player
     * @throws NullPointerException if the given parameter is null
     */
    public Player(Simulator simulator) {
        Objects.requireNonNull(simulator, Error.getNullMsg("simulator"));
        this.simulator = simulator;
        this.start();
    }

    /**
     * Handles all actions that happen in each frame.
     *
     * @param timestamp The timestamp of the current frame given in nanoseconds. This value will be the same for all
     *                  AnimationTimers called during one frame.
     */
    @Override
    public void handle(long timestamp) {
        boolean simRunning = simulator.getSimulationState() == RUN;
        boolean timeChanged = lastUpdateTime.get() > 0;

        if (simRunning && timeChanged) {
            double elapsedSeconds = (timestamp - lastUpdateTime.get()) * NANO;
            simulator.update(elapsedSeconds);

            if (simulator.ended()) {
                simulator.update(0);
                simulator.setSimulationState(ENDED);
                this.stop();
            }
        }

        lastUpdateTime.set(timestamp);
    }

}

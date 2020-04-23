package org.epi.model;

import org.epi.util.Error;

import javafx.scene.layout.Pane;
import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** The main class for the simulator, acting as a facade for the rest of the simulation components.*/
public class Simulator {

    /** The simulation view.*/
    private final Pane simulationView = new Pane();

    /** The animation timer for the simulation view.*/
    private final AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {

        }
    };

    /** The human population being simulated.*/
    private final List<Human> population;

    /** The disease being simulated.*/
    private final Disease disease;

    /**
     * Initialise a simulation with object wrapped parameters (disease and world).
     *
     * @param disease the disease to be simulated
     * @param world the world to be simulated
     * @throws NullPointerException if any of the two parameters are null
     */
    public Simulator(World world, Disease disease) {
        Objects.requireNonNull(disease, Error.getNullMsg("disease"));
        Objects.requireNonNull(world, Error.getNullMsg("world"));

        population = new ArrayList<>();

        for (int i = 0; i < world.getPopulationCount(); i++) {
            double centerX = 0;
            double centerY = 0;
            double velocityX = 0;
            double velocityY = 0;

        }

        this.disease = disease;
    }

    /**
     * Getter for {@link Simulator#population}.
     *
     * @return {@link Simulator#population}
     */
    public List<Human> getPopulation() {
        return population;
    }

    // TODO EVERYTHING

}


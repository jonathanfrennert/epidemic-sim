package org.epi.model;

import org.epi.model.human.Human;
import org.epi.util.ErrorUtil;

import javafx.scene.layout.Pane;
import javafx.animation.AnimationTimer;

import java.util.List;
import java.util.Objects;

/** The main class for the simulator, acting as a facade for the rest of the simulation components.*/
public class Simulator {

    /** The simulation view.*/
    private final Pane simulationView = new Pane();

    /** The animation timer for the simulation view.*/
    private final AnimationTimer timer;

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
    public Simulator(Disease disease, World world) {
        Objects.requireNonNull(disease, ErrorUtil.getNullMsg("disease"));
        Objects.requireNonNull(world, ErrorUtil.getNullMsg("world"));

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

}

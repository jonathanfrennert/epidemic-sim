package org.epi.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import org.epi.model.human.InfectedHuman;
import org.epi.model.human.RecoveredHuman;
import org.epi.util.Error;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.animation.AnimationTimer;

import java.util.List;
import java.util.Objects;

/** The main simulator class; a facade to the rest of the simulator components.*/
public class Simulator {

    /** The default view width in pixels.*/
    public static final double WORLD_WIDTH = 500;

    /** The default view height in pixels.*/
    public static final double WORLD_HEIGHT = 500;

    //---------------------------- AnimationTimer ----------------------------

    /** The timer for the world.*/
    private final AnimationTimer worldTime = new AnimationTimer() {

        /** The order magnitude of nano units.*/
        private static final double NANO = 10^-9;

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

                // Adjust velocities and statuses of humans.
                wallCollisions();
                humanInteractions();

                // Update the world view given the time that has passed.
                statusEffects(elapsedSeconds);
                movement(elapsedSeconds);

                worldStats.updateCount(worldView);
            }

            if (endingIsReached()) {
                worldTime.stop();
            }

            lastUpdateTime.set(timestamp);
        }

    };

    //---------------------------- World ----------------------------

    /** The world view.*/
    private final Pane worldView;

    /** The statistics of the world.*/
    private final Statistics worldStats;

    /**
     * Initialise a simulation with object wrapped parameters (disease and world).
     *
     * @param world the world to be simulated
     * @param disease the disease to be simulated
     * @throws NullPointerException if any of the two given parameters are null
     */
    public Simulator(World world, Disease disease) {
        Objects.requireNonNull(world, Error.getNullMsg("world"));
        Objects.requireNonNull(disease, Error.getNullMsg("disease"));

        worldView = new Pane();
        worldView.setPrefSize(WORLD_WIDTH, WORLD_HEIGHT);

        // Populate world.
        HumanFactory.createHuman(worldView, world, disease, StatusType.INFECTED);
        for (int i = 0; i < world.getPopulationCount() - 1; i++) {
            HumanFactory.createHuman(worldView, world, StatusType.HEALTHY);
        }

       this.worldStats = new Statistics(worldView);
    }

    /**
     * Find all humans that are colliding with walls and adjust their velocities.
     */
    private void wallCollisions() {
        throw new UnsupportedOperationException("Alexandra look over here!");
        // TODO Alexandra :)
    }

    /**
     * Adjust the velocities of all interacting humans such that they leave their friends and those infected spread
     * the disease.
     */
    private void humanInteractions() {
        throw new UnsupportedOperationException("Alexandra look over here!");
        // TODO Alexandra :)
    }

    /**
     * Check whether the transmitter can potentially spread a disease to the receiver.
     *
     * @param transmitter a human
     * @param receiver a human
     * @return true if the transmitter can spread a disease to the receiver, otherwise false
     */
    private boolean isTransmissibleInteraction (Human transmitter, Human receiver) {
        return transmitter.getStatus() == StatusType.INFECTED && receiver.getStatus() == StatusType.HEALTHY;
    }

    /**
     * Swap a human for a newly infected human in the world view
     *
     * @param disease a disease
     * @param human a human
     */
    private void infect(Disease disease, Human human) {
        Human infected = new InfectedHuman(disease);

        infected.setCenterX(human.getCenterX());
        infected.setCenterY(human.getCenterY());

        infected.setVelocityX(human.getVelocityX());
        infected.setVelocityY(human.getVelocityY());

         worldView.getChildren().remove(human);
         worldView.getChildren().add(infected);

         swap(human, infected);
    }

    /**
     * Swap a human for a recovered human in the world view
     *
     * @param human a human
     */
    private void recover(Human human) {
        Human recovered = new RecoveredHuman();

        recovered.setCenterX(human.getCenterX());
        recovered.setCenterY(human.getCenterY());

        recovered.setVelocityX(human.getVelocityX());
        recovered.setVelocityY(human.getVelocityY());

        swap(human, recovered);
    }

    /**
     * Swap out humans from the world view.
     *
     * @param replaced the human who will be replaced
     * @param replacement the human who will be replacement
     */
    private void swap(Human replaced, Human replacement) {
        worldView.getChildren().remove(replaced);
        worldView.getChildren().add(replacement);
    }

    /**
     * Apply status effects on population.
     *
     * @param elapsedSeconds the number of seconds that have passed since the last update
     */
    private void statusEffects(double elapsedSeconds) {
        List<Node> infectedPopulation = getPopulationOf(StatusType.INFECTED);

        infectedPopulation.forEach(human -> ((InfectedHuman) human).updateCurrentDuration(elapsedSeconds));
        infectedPopulation.removeIf(human -> ((InfectedHuman) human).isDeceased());
    }

    /**
     * Move all humans in the world given the amount of time that has passed.
     *
     * @param elapsedSeconds the number of seconds that have passed since the last update
     */
    private void movement(double elapsedSeconds) {
        for (Node node : worldView.getChildren()) {
            if (node instanceof Human) {
                ((Human) node).move(elapsedSeconds);
            }
        }
    }

    /**
     * Check whether the world view has reached any end conditions
     */
    private boolean endingIsReached() {
        boolean isDiseaseGone = worldStats.getInfectedCount() == 0;
        boolean isHumanityGone = worldStats.getDeceasedCount() == worldStats.getInitPopulationCount();

        return isDiseaseGone || isHumanityGone;
    }

    //---------------------------- Getters ----------------------------

    /**
     * Get the population of a certain status from the world view. Humans will be returned as {@link Node}
     * as the worldView children cannot be type-cast directly to a human list.
     *
     * @param status the status by which to filter the population
     * @return population in world view with the given status
     */
    private List<Node> getPopulationOf(StatusType status) {
        return worldView.getChildren().filtered(node -> node instanceof Human && ((Human) node).getStatus() == status);
    }

    /**
     * Getter for {@link #worldTime}.
     *
     * @return {@link #worldTime}
     */
    public AnimationTimer getWorldTime() {
        return worldTime;
    }

    /**
     * Getter for {@link #worldView}.
     *
     * @return {@link #worldView}
     */
    public Pane getWorldView() {
        return worldView;
    }

    /**
     * Getter for {@link #worldStats}.
     *
     * @return {@link #worldStats}
     */
    public Statistics getWorldStats() {
        return worldStats;
    }

}


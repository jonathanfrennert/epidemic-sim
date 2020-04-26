package org.epi.model;

import org.epi.model.human.HealthyHuman;
import org.epi.model.human.InfectedHuman;
import org.epi.util.Error;

import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;

import java.util.ListIterator;
import java.util.Objects;

import static org.epi.model.Human.RADIUS;
import static org.epi.util.Probability.chance;

/** The main simulator class; a facade to the rest of the simulator components.*/
public class Simulator {

    /** The default view width in pixels.*/
    public static final double WORLD_WIDTH = 500;

    /** The default view height in pixels.*/
    public static final double WORLD_HEIGHT = 500;

    /** The maximum population; equal to the maximum number of humans that can fit in the view without any overlap.*/
    public static final int MAX_POPULATION = (int) Math.floor((WORLD_WIDTH * WORLD_HEIGHT) / (4 * RADIUS * RADIUS));

    //---------------------------- AnimationTimer ----------------------------

    /** The timer for the world.*/
    private final AnimationTimer worldTime = new AnimationTimer() {

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

                // Adjust velocities and statuses of humans.
                wallCollisions();
                humanInteractions();

                // Update the world view given the time that has passed.
                statusEffects(elapsedSeconds);
                movement(elapsedSeconds);
            }

            worldStats.updateCount(population);

            if (endingIsReached()) {
                worldTime.stop();
            }

            lastUpdateTime.set(timestamp);
        }

    };

    //---------------------------- Population ----------------------------

    /** Human container variable. Any humans in this list have according changes in the world view.
     * This field is used to get around the fact that world view only returns a list of nodes.*/
    private final ObservableList<Human> population;

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

        population = FXCollections.observableArrayList();

        // Makes so that all changes to population are reflected in the world view.
        population.addListener( (ListChangeListener<Human>) change -> {
            while (change.next()) {
                for (Human human : change.getAddedSubList()) {
                    worldView.getChildren().add(human);
                }

                for (Human human : change.getRemoved()) {
                    worldView.getChildren().remove(human);
                }
            }
        });

        // Add population.
        HumanFactory.createHuman(population, world, disease, StatusType.INFECTED);
        for (int i = 0; i < world.getPopulationCount() - 1; i++) {
            HumanFactory.createHuman(population, world, StatusType.HEALTHY);
        }

       this.worldStats = new Statistics(population);
    }

    /**
     * Find all humans that are colliding with walls and adjust their velocities.
     */
    private void wallCollisions() {
        for (Human human : population) {
            boolean onLeftWall = human.getCenterX() - human.getRadius() <= 0 && human.getVelocityX() < 0;
            boolean onRightWall = human.getCenterX() + human.getRadius() >= WORLD_WIDTH && human.getVelocityX() > 0;

            boolean onBottomWall = human.getCenterY() - human.getRadius() <= 0 && human.getVelocityY() < 0;
            boolean onTopWall = human.getCenterY() + human.getRadius() >= WORLD_HEIGHT && human.getVelocityY() > 0;

            if (onLeftWall || onRightWall) {
                human.setVelocityX(- human.getVelocityX());
            }

            if (onBottomWall || onTopWall) {
                human.setVelocityY(- human.getVelocityY());
            }

        }
    }

    /**
     * Adjust the velocities of all interacting humans such that they leave their friends and those infected spread
     * the disease.
     */
    private void humanInteractions() {
        ListIterator<Human> totalPopulationItr = population.listIterator();
        while (totalPopulationItr.hasNext()) {

            Human human1 = totalPopulationItr.next();

            ListIterator<Human> lesserPopulationItr = population.listIterator(totalPopulationItr.nextIndex());
            while (lesserPopulationItr.hasNext()) {

                Human human2 = lesserPopulationItr.next();

                if (human1.met(human2)) {
                    human1.leave(human2);

                    if (isEffectiveContact(human1, human2)) {
                        Disease disease = ((InfectedHuman) human1).getDisease();

                        if (chance(disease.getTransmissionRisk())) {
                            lesserPopulationItr.set(HumanFactory.healthyToInfected(disease, (HealthyHuman) human2));
                        }

                    } else if (isEffectiveContact(human2, human1)) {
                        Disease disease = ((InfectedHuman) human2).getDisease();

                        if (chance(disease.getTransmissionRisk())) {
                            totalPopulationItr.set(HumanFactory.healthyToInfected(disease, (HealthyHuman) human1));
                        }
                    }

                }

            }
        }
    }

    /**
     * Check whether the first given human can potentially spread a disease to the other.
     *
     * @param human1 a human
     * @param human2 a human
     * @return true if the first given human is infected and the other is healthy, otherwise false
     */
    private boolean isEffectiveContact(Human human1, Human human2) {
        return human1.isType(StatusType.INFECTED) && human2.isType(StatusType.HEALTHY);
    }

    /**
     * Apply status effects on population.
     *
     * @param elapsedSeconds the number of seconds that have passed since the last update
     */
    private void statusEffects(double elapsedSeconds) {
        for (ListIterator<Human> populationItr = population.listIterator(); populationItr.hasNext();) {
            Human human = populationItr.next();
            StatusType status = human.getStatus();

            switch(status) {
                case HEALTHY:
                    break;
                case INFECTED:
                    InfectedHuman infected = (InfectedHuman) human;

                    if (infected.isDeceased()) {
                        populationItr.remove();
                    } else if (infected.totalDurationPassed()) {
                        populationItr.set(HumanFactory.infectedToRecovered(infected));
                    } else {
                        infected.updateCurrentDuration(elapsedSeconds);
                    }

                    break;
                case RECOVERED:
            }
        }
    }

    /**
     * Move all humans in the world given the amount of time that has passed.
     *
     * @param elapsedSeconds the number of seconds that have passed since the last update
     */
    private void movement(double elapsedSeconds) {
        for (Human human : population) {
            human.move(elapsedSeconds);
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


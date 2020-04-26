package org.epi.model;

import javafx.collections.ObservableList;
import org.epi.model.human.HealthyHuman;
import org.epi.model.human.InfectedHuman;
import org.epi.model.human.RecoveredHuman;
import org.epi.util.Error;

import java.util.Objects;
import java.util.Random;

import static java.lang.Math.*;
import static org.epi.model.Simulator.*;
import static org.epi.util.Probability.chance;

/** Static factory for creating humans.*/
public final class HumanFactory {

    /** The initial speed of a human in pixels per second.*/
    public static final double SPEED = 70;

    public static final double MIN_SPEED = 50;

    /** Not to be used. */
    private HumanFactory() {
        throw new UnsupportedOperationException("This constructor should never be used.");
    }

    //---------------------------- Factory Methods ----------------------------

    /**
     * Create humans for the given disease dependent status type and add them to the population.
     *
     * @param population the population of the given world
     * @param world     a world
     * @param disease   a disease
     * @param status    a status
     * @return if the human of the given status was initialised in the population, otherwise false
     * @throws NullPointerException if any of the given parameters are null
     */
    public static boolean createHuman(ObservableList<Human> population, World world, Disease disease, StatusType status) {
        Objects.requireNonNull(population, Error.getNullMsg("population"));
        Objects.requireNonNull(world, Error.getNullMsg("world"));
        Objects.requireNonNull(disease, Error.getNullMsg("disease"));
        Objects.requireNonNull(status, Error.getNullMsg("status type"));

        Human human;

        try {
            if (status == StatusType.INFECTED) {
                human = new InfectedHuman(disease);
            } else {
                throw new IllegalArgumentException(Error.ERROR_TAG + " Status type is not disease dependent: " + status);
            }

            setPosition(population, human);
            setVelocity(world, human);

        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.err.println("Human was not added to the population.");
            return false;
        }

        population.add(human);
        return true;
    }

    /**
     * Create human for the given status type and add them to the population.
     *
     * @param population the population of the given world
     * @param world     a world
     * @param status    a status
     * @return true if the human of the given status was initialised in the population, otherwise false
     * @throws NullPointerException If any of the given parameters are null
     */
    public static boolean createHuman(ObservableList<Human> population, World world, StatusType status) {
        Objects.requireNonNull(population, Error.getNullMsg("population"));
        Objects.requireNonNull(world, Error.getNullMsg("world"));
        Objects.requireNonNull(status, Error.getNullMsg("status type"));

        Human human;

        try {
            switch(status) {
                case HEALTHY:
                    human = new HealthyHuman();
                    break;
                case RECOVERED:
                    human = new RecoveredHuman();
                    break;
                default:
                    throw new IllegalArgumentException(Error.ERROR_TAG + " Status type is disease dependent: " + status);
            }

            setPosition(population, human);
            setVelocity(world, human);

        } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
                System.err.println("Human was not added to the population.");
                return false;
        }

        population.add(human);
        return true;
    }

    //---------------------------- Converter methods ----------------------------

    /**
     * Get a infected version of a healthy human.
     *
     * @param disease a disease
     * @param healthy a healthy human
     * @return a newly infected human with the same position and same velocity as the given human
     * @throws NullPointerException if the given parameters are null
     */
    public static InfectedHuman healthyToInfected(Disease disease, HealthyHuman healthy) {
        Objects.requireNonNull(disease, Error.getNullMsg("disease"));
        Objects.requireNonNull(healthy, Error.getNullMsg("healthy human"));

        InfectedHuman infected = new InfectedHuman(disease);

        infected.setCenterX(healthy.getCenterX());
        infected.setCenterY(healthy.getCenterY());

        infected.setVelocityX(healthy.getVelocityX());
        infected.setVelocityY(healthy.getVelocityY());

        return infected;
    }

    /**
     * Get a recovered version of an infected human
     *
     * @param infected an infected human
     * @return a newly recovered human with the same position and same velocity as the given human
     * @throws NullPointerException if the given parameter is null
     */
    public static RecoveredHuman infectedToRecovered(InfectedHuman infected) {
        Objects.requireNonNull(infected, Error.getNullMsg("infected human"));

        RecoveredHuman recovered = new RecoveredHuman();

        recovered.setCenterX(infected.getCenterX());
        recovered.setCenterY(infected.getCenterY());

        recovered.setVelocityX(infected.getVelocityX());
        recovered.setVelocityY(infected.getVelocityY());

        return recovered;
    }

    //---------------------------- Factory Workers ----------------------------

    /**
     * Set the position of the given human in the population.
     *
     * @param population a population
     * @param human a human
     * @throws IllegalArgumentException if the given population is over capacity
     */
    private static void setPosition(ObservableList<Human> population, Human human) {
        populationCheck(population);

        human.setCenterX(WORLD_WIDTH * Math.random());
        human.setCenterY(WORLD_HEIGHT * Math.random());
    }

    /**
     * Checks if the number of humans in the view pane is larger than the max population
     *
     * @param population a population
     * @throws IllegalArgumentException if the number of humans is larger than {@link Simulator#MAX_POPULATION}
     */
    private static void populationCheck(ObservableList<Human> population) {
        if(population.size() > MAX_POPULATION){
            throw new IllegalArgumentException(Error.ERROR_TAG + " Given population is over capacity: " + population);
        }
    }

    /**
     * Set the velocities of the given human dependent on the world.
     *
     * @param world a world
     * @param human a human
     */
    private static void setVelocity(World world, Human human) {
        if (!chance(world.getSocialDistProb())) {
            setRandomVelocities(human);
        }
    }

    /**
     * Set random horizontal and vertical velocity normalised to a speed of {@value SPEED} for this human.
     *
     * @param human a human
     */
    private static void setRandomVelocities(Human human) {
        Random rand = new Random();

        final double speed = MIN_SPEED + (SPEED - MIN_SPEED) * rand.nextDouble();
        final double angle = 2 * PI * rand.nextDouble();

        human.setVelocityX(speed * cos(angle));
        human.setVelocityY(speed * sin(angle));
    }

}

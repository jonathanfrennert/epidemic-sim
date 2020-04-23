package org.epi.model;

import org.epi.model.human.HealthyHuman;
import org.epi.model.human.InfectedHuman;
import org.epi.model.human.RecoveredHuman;
import org.epi.util.Error;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

import java.util.Objects;

import static org.epi.util.Probability.chance;

/** Static factory for creating humans.*/
public final class HumanFactory {

    /** The initial speed of a human in pixels per second.*/
    public static final double SPEED = 20;

    /** Not to be used. */
    private HumanFactory() {
        throw new UnsupportedOperationException("This constructor should never be used.");
    }

    //---------------------------- Factory Methods ----------------------------

    /**
     * Create humans for the given disease dependent status type.
     *
     * @param world     a world
     * @param disease   a disease
     * @param view      a view pane
     * @param status    a status
     * @return a human initialised for the given status type and initial position. If the human creation
     *         failed due to an illegal argument or the view is over capacity, null will be returned
     * @throws NullPointerException if any of the given parameters are null
     */
    public static Human createHuman(World world, Disease disease, Pane view , StatusType status) {
        Objects.requireNonNull(status, Error.getNullMsg("world"));
        Objects.requireNonNull(disease, Error.getNullMsg("disease"));
        Objects.requireNonNull(world, Error.getNullMsg("view"));
        Objects.requireNonNull(status, Error.getNullMsg("status type"));

        Human human = null;

        try {
            if (status == StatusType.INFECTED) {
                human = new InfectedHuman(disease);
            } else {
                throw new IllegalArgumentException(Error.ERROR_TAG + " Status type is not disease dependent: " + status);
            }

            setPosition(view, human);
            setVelocity(world, human);

        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

        return human;
    }

    /**
     * Create human for the given status type.
     *
     * @param world     a world
     * @param view      a view pane
     * @param status    a status
     * @return a human initialised for the given status type. If the human creation
     *         failed due to an illegal argument or the view is over capacity, null will be returned
     * @throws NullPointerException If any of the given parameters are null
     */
    public static Human createHuman(World world, Pane view, StatusType status) {
        Objects.requireNonNull(status, Error.getNullMsg("world"));
        Objects.requireNonNull(world, Error.getNullMsg("view"));
        Objects.requireNonNull(status, Error.getNullMsg("status type"));

        Human human = null;

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

            setPosition(view, human);
            setVelocity(world, human);

        } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
        }

        return human;
    }

    //---------------------------- Converter methods ----------------------------

    /**
     * Convert any human to an infected human
     *
     * @param disease a disease
     * @param human a human
     * @return a infected human with the same position and velocity as the given human
     * @throws NullPointerException if the given parameters are null
     */
    public static Human infect(Disease disease, Human human) {
        Objects.requireNonNull(disease, Error.getNullMsg("disease"));
        Objects.requireNonNull(human, Error.getNullMsg("human"));

        Human infected = new InfectedHuman(disease);

        infected.setCenterX(human.getCenterX());
        infected.setCenterY(human.getCenterY());

        infected.setVelocityX(human.getVelocityX());
        infected.setVelocityY(human.getVelocityY());

        return infected;
    }

    /**
     * Convert any human to a recovered human
     *
     * @param human a human
     * @return a recovered human with the same position and velocity as the given human
     * @throws NullPointerException if the given parameter is null
     */
    public static Human recover(Human human) {
        Objects.requireNonNull(human, Error.getNullMsg("human"));

        Human recovered = new RecoveredHuman();

        recovered.setCenterX(human.getCenterX());
        recovered.setCenterY(human.getCenterY());

        recovered.setVelocityX(human.getVelocityX());
        recovered.setVelocityY(human.getVelocityY());

        return recovered;
    }

    //---------------------------- Factory Workers ----------------------------

    /**
     * Set the position of the given human in the view pane.
     *
     * @param view a view pane
     * @param human a human
     * @throws IllegalArgumentException if the given view is over capacity
     */
    private static void setPosition(Pane view, Human human) {
        // TODO positioning
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
        Point2D totalVelocity = new Point2D(Math.random(), Math.random()).normalize();

        human.setVelocityX(totalVelocity.getX() * SPEED);
        human.setVelocityY(totalVelocity.getY() * SPEED);
    }

}

package org.epi.model;

import org.epi.model.human.HealthyHuman;
import org.epi.model.human.InfectedHuman;
import org.epi.model.human.RecoveredHuman;
import org.epi.util.Error;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

import java.util.Objects;
import java.util.Random;

import static org.epi.model.Human.RADIUS;
import static org.epi.model.Simulator.WORLD_HEIGHT;
import static org.epi.model.Simulator.WORLD_WIDTH;
import static org.epi.util.Probability.chance;
import static org.epi.util.Probability.probabilityCheck;
import static org.epi.model.World.MAX_POPULATION;

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
     * Create humans for the given disease dependent status type and place them in the view.
     *
     * @param world     a world
     * @param disease   a disease
     * @param view      a view pane
     * @param status    a status
     * @return if the human of the given status was initialised in the view, otherwise false
     * @throws NullPointerException if any of the given parameters are null
     */
    public static boolean createHuman(Pane view , World world, Disease disease, StatusType status) {
        Objects.requireNonNull(world, Error.getNullMsg("view"));
        Objects.requireNonNull(status, Error.getNullMsg("world"));
        Objects.requireNonNull(disease, Error.getNullMsg("disease"));
        Objects.requireNonNull(status, Error.getNullMsg("status type"));

        Human human;

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
            System.err.println("Human was not added to the view.");
            return false;
        }

        view.getChildren().add(human);
        return true;
    }

    /**
     * Create human for the given status type and place them in the view.
     *
     * @param world     a world
     * @param view      a view pane
     * @param status    a status
     * @return true if the human of the given status was initialised in the view, otherwise false
     * @throws NullPointerException If any of the given parameters are null
     */
    public static boolean createHuman(Pane view, World world, StatusType status) {
        Objects.requireNonNull(world, Error.getNullMsg("view"));
        Objects.requireNonNull(status, Error.getNullMsg("world"));
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

            setPosition(view, human);
            setVelocity(world, human);

        } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
                System.err.println("Human was not added to the view.");
                return false;
        }

        view.getChildren().add(human);
        return true;
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
        if(view.getChildren().size() > MAX_POPULATION){
            throw new IllegalArgumentException("Pane must not be larger than 500x500!");
        }

        final Random rand = new Random();

        final double initialX = view.getWidth() * rand.nextDouble();
        final double initialY = view.getHeight() * rand.nextDouble();

        human.setCenterX(initialX);
        human.setCenterY(initialY);
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

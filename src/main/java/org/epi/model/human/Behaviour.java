package org.epi.model.human;

import org.epi.util.Error;

import javafx.geometry.Point2D;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.epi.model.human.Model.HUMAN_RADIUS;

/** State class for the behaviour of humans.*/
public enum Behaviour{
    NORMAL {

        /** {@inheritDoc} */
        @Override
        public void initVelocity(Model model) {
            Model.requireNonNull(model);

            final double angle = 2 * PI * Math.random();
            model.setVelocity(new Point2D(cos(angle), sin(angle)).multiply(SPEED));
        }

        /**
         * {@inheritDoc}
         * Normal behaviour is to move around like normal (constant).
         */
        @Override
        public void adjustToOthers(Model model) {
        }

    },
    SOCIAL_DISTANCING {

        /** {@inheritDoc} */
        @Override
        public void initVelocity(Model model) {
            Model.requireNonNull(model);
            model.setVelocity(0,0);
        }

        /**
         * {@inheritDoc}
         * Social distancing behaviour is to stay at home (constant).
         */
        @Override
        public void adjustToOthers(Model model) {
        }

    },
    CONTACT_TRACING {

        /** {@inheritDoc} */
        @Override
        public void initVelocity(Model model) {
            Model.requireNonNull(model);

            final double angle = 2 * PI * Math.random();
            model.setVelocity(new Point2D(cos(angle), sin(angle)).multiply(SPEED));
        }

        /**
         * {@inheritDoc}
         * Contact tracing behaviour is to actively avoid others.
         * @throws NullPointerException if the given parameter is null
         */
        @Override
        public void adjustToOthers(Model model) {
            Model.requireNonNull(model);

            Set<Human> nearby = model.getHost().getNearby();

            if (!model.getHost().isSick()) {
                nearby.removeIf(human -> human.getModel().getBehaviour() != CONTACT_TRACING);
                nearby.removeIf(Predicate.not(Human::isSick));
            }

            Point2D velocity = new Point2D(0,0);

            for (Human other : nearby) {
                double distance = Model.distance(model, other.getModel());

                if (distance <= 3 * HUMAN_RADIUS) {
                    double deltaX = model.getCenterX() - other.getModel().getCenterX();
                    double deltaY = model.getCenterY() - other.getModel().getCenterY();
                    Point2D direction = new Point2D(deltaX, deltaY).normalize();

                    velocity = direction.multiply(1 / distance).add(velocity);
                }
            }

            if (velocity.magnitude() > 0) {
                model.setVelocity(velocity.normalize().multiply(SPEED));
            }
        }

    };

    /** The initial speed of a human in pixels per second.*/
    public static final double SPEED = 60;

    /**
     * Initialise the velocity of the given model with this behaviour.
     *
     * @param model the model with this behaviour
     * @throws NullPointerException if the given parameter is null
     */
    public abstract void initVelocity(Model model);

    /**
     * Adjust the velocity of the model given its behaviour.
     *
     * @param model the model with this behaviour
     */
    public abstract void adjustToOthers(Model model);

    /**
     * Check if the given behaviour is null
     *
     * @param behaviour a behaviour
     * @throws NullPointerException if the given behaviour is null
     */
    public static void requireNonNull(Behaviour behaviour) {
        Objects.requireNonNull(behaviour, Error.getNullMsg("behaviour"));
    }

}

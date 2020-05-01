package org.epi.model;

import org.epi.util.Error;

import java.util.Objects;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/** State class for the behaviour of humans.*/
public enum Behaviour{
    NORMAL {

        /**
         * {@inheritDoc}
         *
         * Normal behaviour is to move around like normal.
         */
        @Override
        public void initVelocity(Model model) {
            Objects.requireNonNull(model, Error.getNullMsg("model"));

            final double angle = 2 * PI * Math.random();

            model.setVelocity(SPEED * cos(angle),SPEED * sin(angle));
        }

    },
    DISTANCING {

        /**
         * {@inheritDoc}
         *
         * Social distancing behaviour is to stay at home.
         */
        @Override
        public void initVelocity(Model model) {
            Objects.requireNonNull(model, Error.getNullMsg("model"));
            model.setVelocity(0,0);
        }

    },
    AVOIDANT {
        /**
         * {@inheritDoc}
         *
         * Avoidant behaviour is to actively avoid others.
         */
        @Override
        public void initVelocity(Model model) {
            Objects.requireNonNull(model, Error.getNullMsg("model"));

            final double angle = 2 * PI * Math.random();

            model.setVelocity(SPEED * cos(angle),SPEED * sin(angle));
        }

    };

    /** The initial speed of a human in pixels per second.*/
    public static final double SPEED = 70;

    /**
     * Initialise the velocity of the given model with this behaviour.
     *
     * @param model the model with this behaviour
     * @throws NullPointerException if the given parameter is null
     */
    public abstract void initVelocity(Model model);

}

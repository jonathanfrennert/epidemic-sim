package org.epi.util;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.Arrays;
import java.util.List;

/** Utility class with helper methods for handling probability.*/
public class Probability {

    /** The minimum probability.*/
    public static final double MIN_PROB = 0;
    /** The maximum probability.*/
    public static final double MAX_PROB = 1;

    /**
     * Sample a probability distribution for a boolean given a certain chance.
     *
     * @param chance the chance of success
     * @return true if the sampling was within the chance of the success, otherwise false
     * @throws NullPointerException if the given chance is less than {@value Probability#MIN_PROB} or more than
     *                              {@value Probability#MAX_PROB}
     */
    public static boolean chance(double chance) {
        probabilityCheck(chance);

        List<Pair<Boolean,Double>> outcomes = Arrays.asList(Pair.create(true, chance), Pair.create(false, 1 - chance));

        return new EnumeratedDistribution<>(outcomes).sample();
    }

    /**
     * Check if the given probability is more than {@value MIN_PROB} and less than {@value MAX_PROB}.
     *
     * @param probability A probability
     * @throws IllegalArgumentException if the given probability is less than {@value MIN_PROB} or more than
     *                                  {@value MAX_PROB}
     */
    public static void probabilityCheck(double probability) {
        Error.intervalCheck("probability", MIN_PROB, MAX_PROB, probability);
    }
}

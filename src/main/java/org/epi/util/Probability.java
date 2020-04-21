package org.epi.util;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.util.Arrays;
import java.util.List;

/** Utility class with helper methods for handling probability.*/
public class Probability {

    /**
     * Sample a probability distribution for a boolean given a certain chance.
     *
     * @param chance the chance of success
     * @return true if the sampling was within the chance of the success, otherwise false
     * @throws NullPointerException if the given chance is less than {@value ErrorUtil#MIN_PROB} or more than
     *                              {@value ErrorUtil#MAX_PROB}
     */
    public static boolean chance(double chance) {
        ErrorUtil.probabilityCheck(chance);

        List<Pair<Boolean,Double>> outcomes = Arrays.asList(Pair.create(true, chance), Pair.create(false, 1 - chance));

        return new EnumeratedDistribution<>(outcomes).sample();
    }

}

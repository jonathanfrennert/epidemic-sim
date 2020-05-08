package org.epi.model;

import org.epi.util.Error;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A utility class for spatial hashing of populations.
 */
public class SpatialHash {

    /** The area of each cell in pixels.*/
    private static final double CELL_SIZE = 25;

    /** a backreference to the location.*/
    private final Location location;

    /** Spatial hashing for wall collisions and human contact detection.*/
    private final Map<Integer, Set<Human>> spatialHash;

    /** Conversion factor for spatial hashcode.*/
    private final double convertFactor;

    /** The multiples of the human radius used to calculate
     * the max and min values of the square for which a human model is inscribed.*/
    private static final double[] multipleArray = {Model.HUMAN_RADIUS,- Model.HUMAN_RADIUS};

    //---------------------------- Constructor ----------------------------

    /**
     * Create a spatial map for a location.
     *
     * @param location a location
     * @throws NullPointerException if the given parameter is null
     */
    public SpatialHash(Location location) {
        Objects.requireNonNull(location, Error.getNullMsg("location"));

        this.location = location;

        this.convertFactor = 1 / ((double) CELL_SIZE);

        this.spatialHash = new HashMap<>();
        update();
    }

    //---------------------------- Main methods ----------------------------

    /**
     * Update the spatial map with the current position of the models.
     */
    public void update() {
        spatialHash.clear();

        for (Human human : location.getPopulation()) {
            for (double a : multipleArray) {
                for (double b : multipleArray) {
                    int hash = hashcode(human.getModel().getCenterX() + a, human.getModel().getCenterY() + b);
                    addToSpatialHash(hash, human);
                }
            }
        }
    }

    /**
     * Get all nearby humans in this location.
     *
     * @param human a human in this location
     */
    public Set<Human> getNearby(Human human) {
        Set<Human> result = new HashSet<>();

        for (Map.Entry<Integer, Set<Human>> cell  : spatialHash.entrySet()) {
            if (cell.getValue().contains(human)) {
                Set<Human> nearby = new HashSet<>(cell.getValue());
                nearby.remove(human);

                result.addAll(nearby);
            }
        }

        return result;
    }

    //---------------------------- Helper methods ----------------------------

    /**
     * Adds the given human to the spatial hash.
     *
     * @param human a human
     * @param key the human's spatial hashcode
     */
    private void addToSpatialHash(int key, Human human) {
        if(spatialHash.containsKey(key)) {
            spatialHash.get(key).add(human);
        } else {
            HashSet<Human> cellSet = new HashSet<>();
            cellSet.add(human);

            spatialHash.put(key, cellSet);
        }
    }

    /**
     * get the hashcode in the spatial hash mapping for the given coordinate.
     *
     * @param x a x-coordinate in the location area
     * @param y a y-coordinate in the location area
     * @return hashcode for the given coordinate in the pane
     */
    private int hashcode(double x, double y) {
        return (int) (x * convertFactor) + (int) (y * convertFactor);
    }

}

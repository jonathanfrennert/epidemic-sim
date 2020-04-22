package org.epi.util;

import static org.epi.model.BouncyCircle.RADIUS;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.Objects;

/** Utility class for positioning humans in the view such that they do not overlap. Utilizes Poisson Disc-sampling.*/
public class Positioner {

    /** Grid placement value for indicating no sample.*/
    private static final int NO_SAMPLE = 0;

    /** Grid placement value for indicating a sample.*/
    private static final int CONTAINS_SAMPLE = 1;

    /**
     * Place a circle in a random position in the view such that it does not overlap with any already placed circles.
     *
     * @param view a view pane
     * @param circle a circle
     * @return true if the circle was placed successfully, otherwise false
     * @throws NullPointerException if any of the two given parameters are null
     * @throws IllegalArgumentException if the given view contains children that are not circles
     */
    public static boolean position(Pane view, Circle circle) {
        Objects.requireNonNull(view, Error.getNullMsg("view"));
        Objects.requireNonNull(circle, Error.getNullMsg("circle"));
        contentCheck(view);

        //TODO Currently working on Quadtree.

        boolean success = false;

        int gridWidth = (int) (view.getWidth() / RADIUS);
        int gridHeight = (int) (view.getHeight() / RADIUS);
        int[][] placementGrid = new int[gridWidth][gridHeight];

        for (Node placed : view.getChildren()) {
            Circle placedCircle = (Circle) placed;

            int gridPosX = (int) placedCircle.getCenterX();
            int gridPosY = (int) placedCircle.getCenterY();
            placementGrid[gridPosX][gridPosY] = CONTAINS_SAMPLE;
        }

        System.err.println(Error.ERROR_TAG + " View is already full.");

        return success;
    }

    /**
     * Check if the given pane only has circles as children.
     *
     * @param pane a pane
     * @throws IllegalArgumentException if the given pane contains children that are not circles
     */
    private static void contentCheck(Pane pane) {
        for (Node child : pane.getChildren()) {
            if (!(child instanceof Circle)) {
                throw new IllegalArgumentException(Error.ERROR_TAG + " Given pane contains more than circles:" + child);
            }
        }
    }

}

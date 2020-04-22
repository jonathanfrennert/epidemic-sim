package org.epi.util;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;;

import java.util.Objects;

import static org.epi.model.BouncyCircle.RADIUS;
import static org.epi.model.Simulator.VIEW_HEIGHT;
import static org.epi.model.Simulator.VIEW_WIDTH;

/** Utility class for positioning humans in the view such that they do not overlap. Utilizes Poisson Disc-sampling.*/
public class Positioner {

    /** The limit of samples to choose before rejection.*/
    private static final int SAMPLE_LIMIT = 30;

    /** Grid placement value for indicating no sample.*/
    private static final int NO_SAMPLE = 0;

    /** Grid placement value for indicating a sample.*/
    private static final int CONTAINS_SAMPLE = 1;

    /** The view in which the new circles are to be placed*/
    private final Pane view;

    /**
     * Constructor for a positioner given the view in which circles are to be placed.
     *
     * @param view the view in which the circles are to be placed
     * @throws NullPointerException if the given parameters is null
     */
    public Positioner(Pane view) {
        Objects.requireNonNull(view, Error.getNullMsg("view"));

        this.view = view;
    }


    /**
     * Place a circle in a random position in the view such that it does not overlap with any already placed circles.
     *
     * @param circle a circle
     * @return true if the circle was placed successfully, otherwise false
     * @throws NullPointerException if the given parameter is null
     */
    public boolean placeInView(Circle circle) {

        //TODO Currently working on quadtree.

        Objects.requireNonNull(circle, Error.getNullMsg("circle"));

        boolean success = false;

        int gridWidth = (int) (VIEW_WIDTH / RADIUS);
        int gridHeight = (int) (VIEW_HEIGHT / RADIUS);
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


}

package org.epi.util;

import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

/** Utility class for providing clipping in JavaFx UI.*/
public class Clip {

    /**
     * Clips the children of the specified {@link Region} to its current size.
     * This requires attaching a change listener to the region’s layout bounds,
     * as JavaFX does not currently provide any built-in way to clip children.
     *
     * @author Christoph Nahr
     *
     * @param region the {@link Region} whose children to clip
     * @throws NullPointerException if the given parameter is null
     */
    public static void clip(Region region) {
        Objects.requireNonNull(region, "region");

        final Rectangle outputClip = new Rectangle();
        outputClip.setWidth(region.getWidth());
        outputClip.setHeight(region.getHeight());
        region.setClip(outputClip);

        region.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
            outputClip.setWidth(newValue.getWidth());
            outputClip.setHeight(newValue.getHeight());
        });
    }

}

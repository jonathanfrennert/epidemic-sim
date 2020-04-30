package org.epi.model.human;

import org.epi.model.Human;
import org.epi.model2.Status;

/** Model class for healthy humans.*/
public class HealthyHuman extends Human {

    /**
     * The constructor for healthy humans.
     */
    public HealthyHuman() {
        super(Status.HEALTHY);
        setFill(Status.HEALTHY.color);
    }

}

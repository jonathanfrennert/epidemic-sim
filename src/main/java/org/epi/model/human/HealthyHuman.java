package org.epi.model.human;

import org.epi.model.Human;
import org.epi.model.StatusType;

/** Model class for healthy humans.*/
public class HealthyHuman extends Human {

    /**
     * The constructor for healthy humans.
     */
    public HealthyHuman() {
        super(StatusType.HEALTHY);
        setFill(StatusType.HEALTHY.color);
    }

}

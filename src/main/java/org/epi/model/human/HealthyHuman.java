package org.epi.model.human;

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

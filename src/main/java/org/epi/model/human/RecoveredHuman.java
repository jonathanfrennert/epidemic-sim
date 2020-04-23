package org.epi.model.human;

/** Model class for humans who have recovered from the disease.*/
public class RecoveredHuman extends Human {

    /**
     * The constructor for recovered humans.
     */
    public RecoveredHuman() {
        super(StatusType.RECOVERED);
        setFill(StatusType.RECOVERED.color);
    }

}

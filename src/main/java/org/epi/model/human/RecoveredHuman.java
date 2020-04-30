package org.epi.model.human;

import org.epi.model.Human;
import org.epi.model2.Status;

/** Model class for humans who have recovered from the disease.*/
public class RecoveredHuman extends Human {

    /**
     * The constructor for recovered humans.
     */
    public RecoveredHuman() {
        super(Status.RECOVERED);
        setFill(Status.RECOVERED.color);
    }

}

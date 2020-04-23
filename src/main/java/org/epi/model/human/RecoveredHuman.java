package org.epi.model.human;

import org.epi.model.Human;
import org.epi.model.StatusType;

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

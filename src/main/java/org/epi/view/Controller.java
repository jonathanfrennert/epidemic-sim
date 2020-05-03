package org.epi.view;

import org.epi.MainApp;
import org.epi.util.Error;

import java.util.Objects;

/** Abstract class for FXML controllers.*/
public abstract class Controller {

    /** Reference to the main application.*/
    private MainApp mainApp;

    /**
     * Set the main application reference for this controller.
     *
     * @param mainApp the main application
     * @throws NullPointerException if the given parameter is null
     */
    public void setMainApp(MainApp mainApp) {
        Objects.requireNonNull(mainApp, Error.getNullMsg("main application"));

        this.mainApp = mainApp;
    }

}

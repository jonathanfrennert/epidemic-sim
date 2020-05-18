package org.epi.view;

import javafx.fxml.FXML;

/**
 * The controller for the root layout.
 *
 * The root layout provides a menu bar and space where other JavaFX elements are placed.
 */
public class RootLayoutController extends Controller {

    @FXML
    private void handleLink() {
        getMainApp().getHostServices().showDocument("https://github.com/J0HNN7G/EpiSim");
    }

}

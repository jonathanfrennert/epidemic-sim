package org.epi.view;

import javafx.scene.layout.GridPane;
import org.epi.MainApp;
import org.epi.model.Simulator;
import org.epi.model.Statistics;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import static org.epi.util.Clip.clip;

public class SimulatorController extends Controller {

    @FXML
    private VBox simulationBox;
    @FXML
    private HBox statisticsBox;
    @FXML
    private GridPane parameterBox;
    @FXML
    private HBox playerBox;

    @FXML
    private Pane cityPane;
    @FXML
    private Pane quarantinePane;

    @FXML
    private Label deceasedLabel;
    @FXML
    private Label recoveredLabel;
    @FXML
    private Label healthyLabel;
    @FXML
    private Label infectedLabel;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public SimulatorController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        clipBoxes();
    }

    /**
     * Clip the boxes in the UI.
     */
    private void clipBoxes() {
        clip(simulationBox);
        clip(parameterBox);
        clip(statisticsBox);
        clip(playerBox);
    }

    /**
     * {@inheritDoc}
     * Assign the simulator to the corresponding holders.
     */
    @Override
    public void setMainApp(MainApp mainApp) {
        super.setMainApp(mainApp);
        showSimulation();
    }

    /**
     * Fills the panes with the city and quarantine from the main application's simulator as well
     * as assigns statistics labels.
     */
    private void showSimulation() {
        Simulator simulator = getMainApp().getSimulator();
        Statistics statistics = simulator.getStatistics();

        Pane city = simulator.getWorld().getCity().getArea();
        Pane quarantine = simulator.getWorld().getQuarantine().getArea();

        clip(city);
        clip(quarantine);

        this.cityPane.getChildren().add(city);
        this.quarantinePane.getChildren().add(quarantine);

        this.deceasedLabel.textProperty().bind(statistics.deceasedProperty().asString());
        this.recoveredLabel.textProperty().bind(statistics.recoveredProperty().asString());
        this.healthyLabel.textProperty().bind(statistics.healthyProperty().asString());
        this.infectedLabel.textProperty().bind(statistics.infectedProperty().asString());
    }

}

package org.epi.view;

import com.jfoenix.controls.JFXButton;
import org.epi.model.SimulationState;
import org.epi.model.Simulator;
import org.epi.model.Statistics;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.epi.util.Error;

import static org.epi.model.SimulationState.ENDED;
import static org.epi.model.SimulationState.PAUSE;
import static org.epi.model.SimulationState.RUN;

public class SimulatorController extends Controller {

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

    @FXML
    private JFXButton playButton;

    /**
     * Fills the panes with the city and quarantine from the main application's simulator as well
     * as assigns statistics labels.
     */
    public void showSimulation() {
        Simulator simulator = getMainApp().getSimulator();
        Statistics statistics = simulator.getStatistics();

        this.cityPane.getChildren().add(simulator.getWorld().getCity().getArea());
        this.quarantinePane.getChildren().add(simulator.getWorld().getQuarantine().getArea());

        this.deceasedLabel.textProperty().bind(statistics.deceasedProperty().asString());
        this.recoveredLabel.textProperty().bind(statistics.recoveredProperty().asString());
        this.healthyLabel.textProperty().bind(statistics.healthyProperty().asString());
        this.infectedLabel.textProperty().bind(statistics.infectedProperty().asString());

        this.playButton.getStyleClass().add(simulator.getSimulationState().styleClass);
        initEvents();
    }

    /**
     * Initialise event listeners
     */
    private void initEvents() {
        // Switch the style class of the play button.
        getMainApp().getSimulator().getSimulationStateProperty().addListener((observable, oldValue, newValue) -> {
            styleSwitch(newValue);

            if (newValue == ENDED) {
                playButton.setDisable(true);
            }
        });

    }

    /**
     * Switch the style class of the play button to reflect the simulation state.
     *
     * @param simulationState the simulation state being switched to
     */
    private void styleSwitch(SimulationState simulationState) {
        for (SimulationState simState : SimulationState.values()) {
            playButton.getStyleClass().remove(simState.styleClass);
        }

        playButton.getStyleClass().add(simulationState.styleClass);
    }

    //---------------------------- User actions ----------------------------

    /**
     * Switch the simulation state.
     *
     * @throws IllegalStateException if the current simulator's state is invalid
     */
    @FXML
    private void handlePlay() {
        Simulator simulator = getMainApp().getSimulator();
        SimulationState result;

        switch (simulator.getSimulationState()) {
            case RUN:
                result = PAUSE;
                break;
            case PAUSE:
                result = RUN;
                break;
            default:
                throw new IllegalStateException(Error.ERROR_TAG + " Simulator state is invalid: " + simulator.getSimulationState());
        }

        simulator.setSimulationState(result);
    }

    /**
     * Reset the simulator.
     */
    @FXML
    private void handleReset() {
        Simulator simulator = getMainApp().getSimulator();

        Pane city = simulator.getWorld().getCity().getArea();
        Pane quarantine = simulator.getWorld().getQuarantine().getArea();

        this.cityPane.getChildren().remove(city);
        this.quarantinePane.getChildren().remove(quarantine);

        getMainApp().setSimulator(simulator.reset());

        playButton.setDisable(false);
        showSimulation();
    }

}

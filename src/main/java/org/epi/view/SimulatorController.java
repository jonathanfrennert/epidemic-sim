package org.epi.view;

import org.epi.model.SimulationState;
import org.epi.model.Simulator;
import org.epi.model.Statistics;
import org.epi.util.Error;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.epi.model.SimulationState.*;

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

    @FXML
    private JFXTextField NormalPropTextField;
    @FXML
    private JFXTextField InertPropTextField;
    @FXML
    private JFXTextField AvoidantProportionTextField;
    @FXML
    private JFXTextField LifespanTextField;
    @FXML
    private JFXTextField ImmunityDurationTextField;

    @FXML
    private StackedAreaChart statsChart;

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

        //TODO fix unsafe operations

        statsChart.createSymbolsProperty().setValue(false);
        statsChart.setLegendVisible(false);
        statsChart.setAnimated(true);

        statsChart.getData().addAll(statistics.getDataSeriesDeceased(),
                statistics.getDataSeriesRecovered(),
                statistics.getDataSeriesHealthy(),
                statistics.getDataSeriesInfected());

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

        List<String> buttonStyles = Arrays.stream(SimulationState.values()).map(x -> x.styleClass).collect(Collectors.toList());
        playButton.getStyleClass().removeIf(buttonStyles::contains);
        playButton.setDisable(false);

        showSimulation();
    }

}

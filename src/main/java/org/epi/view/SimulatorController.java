package org.epi.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.epi.model.SimulationState;
import org.epi.model.Simulator;
import org.epi.model.Statistics;
import org.epi.util.Error;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
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
    private StackedAreaChart<Double, Integer> areaChart;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private NumberAxis xAxis;

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

    //---------------------------- Construction methods ----------------------------

    /**
     * Fills the panes with the city and quarantine from the main application's simulator as well
     * as assigns statistics labels.
     */
    public void showSimulation() {
        Simulator simulator = getMainApp().getSimulator();
        Statistics statistics = simulator.getStatistics();

        cityPane.getChildren().add(simulator.getWorld().getCity().getArea());
        quarantinePane.getChildren().add(simulator.getWorld().getQuarantine().getArea());

        deceasedLabel.textProperty().bind(statistics.deceasedProperty().asString());
        recoveredLabel.textProperty().bind(statistics.recoveredProperty().asString());
        healthyLabel.textProperty().bind(statistics.healthyProperty().asString());
        infectedLabel.textProperty().bind(statistics.infectedProperty().asString());

        ObservableList<XYChart.Series<Double,Integer>> chartData = FXCollections.observableArrayList();
        chartData.add(statistics.getDataSeriesInfected());
        chartData.add(statistics.getDataSeriesHealthy());
        chartData.add(statistics.getDataSeriesRecovered());
        chartData.add(statistics.getDataSeriesDeceased());

        areaChart.setData(chartData);
        yAxis.setUpperBound(statistics.getInitialPopulation());

        playButton.getStyleClass().add(simulator.getSimulationState().styleClass);

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
                pauseXAxis();
            }
        });

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
                pauseXAxis();
                result = PAUSE;
                break;
            case PAUSE:
                xAxis.setAutoRanging(true);
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
        resetPanes();
        resetAreaChart();
        resetPlayButton();
        resetSimulator();

        showSimulation();
    }

    //---------------------------- Helper methods ----------------------------

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

    /**
     * Reset the city and quarantine panes.
     */
    private void resetPanes() {
        Simulator simulator = getMainApp().getSimulator();

        Pane city = simulator.getWorld().getCity().getArea();
        Pane quarantine = simulator.getWorld().getQuarantine().getArea();

        cityPane.getChildren().remove(city);
        quarantinePane.getChildren().remove(quarantine);
    }

    /**
     * Reset the area chart.
     */
    private void resetAreaChart() {
        areaChart.setData(FXCollections.emptyObservableList());
        areaChart.setAnimated(true);
        xAxis.setAutoRanging(true);
    }

    private void pauseXAxis() {
        xAxis.setAutoRanging(false);
        xAxis.setUpperBound(getMainApp().getSimulator().getWorld().getTotalElapsedSeconds());
    }

    /**
     * Reset the play button.
     */
    private void resetPlayButton() {
        List<String> buttonStyles = Arrays.stream(SimulationState.values()).map(x -> x.styleClass).collect(Collectors.toList());
        playButton.getStyleClass().removeIf(buttonStyles::contains);
        playButton.setDisable(false);
    }

    private void resetSimulator() {
        getMainApp().setSimulator(getMainApp().getSimulator().reset());
    }

}

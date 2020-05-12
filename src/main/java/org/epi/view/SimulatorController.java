package org.epi.view;

import com.jfoenix.controls.JFXSlider;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.epi.model.SimulationState;
import org.epi.model.Simulator;
import org.epi.model.Statistics;
import org.epi.util.Error;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.epi.model.SimulationState.ENDED;
import static org.epi.model.SimulationState.PAUSE;
import static org.epi.model.SimulationState.RUN;

public class SimulatorController extends Controller {

    /** Symbol for seconds.*/
    private static final String SEC_EXT = "s";
    /** Symbol of percentages (repetition due to Java convention).*/
    private static final String PERCENT_EXT = "%%";

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
    private Label totalPopulationLabel;
    @FXML
    private JFXSlider totalPopulationSlider;
    @FXML
    private Label sickPopulationLabel;
    @FXML
    private JFXSlider sickPopulationSlider;
    @FXML
    private Label testingFrequencyLabel;
    @FXML
    private JFXSlider testingFrequencySlider;
    @FXML
    private Label detectionRateLabel;
    @FXML
    private JFXSlider detectionRateSlider;

    @FXML
    private Label normalProportionLabel;
    @FXML
    private JFXSlider normalProportionSlider;
    @FXML
    private Label inertProportionLabel;
    @FXML
    private JFXSlider inertProportionSlider;
    @FXML
    private Label avoidantProportionLabel;
    @FXML
    private JFXSlider avoidantProportionSlider;

    @FXML
    private Label lifespanLabel;
    @FXML
    private JFXSlider lifespanSlider;
    @FXML
    private Label immunityDurationLabel;
    @FXML
    private JFXSlider immunityDurationSlider;
    @FXML
    private Label immunityRateLabel;
    @FXML
    private JFXSlider immunityRateSlider;
    @FXML
    private Label transmissionRiskLabel;
    @FXML
    private JFXSlider transmissionRiskSlider;
    @FXML
    private Label fatalityRateLabel;
    @FXML
    private JFXSlider fatalityRateSlider;

    //---------------------------- Construction methods ----------------------------

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        initSliders();
    }

    /**
     * Initialise sliders for world parameters.
     */
    private void initSliders() {
        totalPopulationLabel.textProperty().bind(
                Bindings.format(extFormat(0,""), totalPopulationSlider.valueProperty()));

        sickPopulationLabel.textProperty().bind(
                Bindings.format(extFormat(0,""), sickPopulationSlider.valueProperty()));

        testingFrequencyLabel.textProperty().bind(
                Bindings.format(extFormat(1, SEC_EXT), testingFrequencySlider.valueProperty()));

        detectionRateLabel.textProperty().bind(
                Bindings.format(extFormat(1, PERCENT_EXT), detectionRateSlider.valueProperty()));

        normalProportionLabel.textProperty().bind(
                Bindings.format(extFormat(0,""), normalProportionSlider.valueProperty()));

        inertProportionLabel.textProperty().bind(
                Bindings.format(extFormat(0,""), inertProportionSlider.valueProperty()));

        avoidantProportionLabel.textProperty().bind(
                Bindings.format(extFormat(0,""), avoidantProportionSlider.valueProperty()));
    }


    /**
     * Perform all assignment details.
     */
    public void showSimulation() {
        showPanes();
        showTable();
        showChart();
        showPlayButton();
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
                runXAxis();
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
        resetChart();
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
     * Return a string pattern for double values with the given extension.
     *
     * @param precision the precision of the value shown.
     * @param extension the string extension
     * @return a string pattern for double values with the given extension
     */
    private String extFormat(int precision,  String extension) {
        return "%." + String.valueOf(precision) + "f" + extension;
    }

    /**
     * Adapt the X-axis of the area chart to a paused state.
     */
    private void pauseXAxis() {
        xAxis.setAutoRanging(false);
        xAxis.setUpperBound(getMainApp().getSimulator().getWorld().getTotalElapsedSeconds());
    }

    /**
     * Adapt the X-axis of the area chart to a running state.
     */
    private void runXAxis() {
        xAxis.setAutoRanging(true);
    }

    /**
     * Show the city and quarantines panes.
     */
    private void showPanes() {
        Simulator simulator = getMainApp().getSimulator();

        cityPane.getChildren().add(simulator.getWorld().getCity().getArea());
        quarantinePane.getChildren().add(simulator.getWorld().getQuarantine().getArea());
    }

    /**
     * Show the statistics in the table.
     */
    private void showTable() {
        Statistics statistics = getMainApp().getSimulator().getStatistics();

        deceasedLabel.textProperty().bind(statistics.deceasedProperty().asString());
        recoveredLabel.textProperty().bind(statistics.recoveredProperty().asString());
        healthyLabel.textProperty().bind(statistics.healthyProperty().asString());
        infectedLabel.textProperty().bind(statistics.infectedProperty().asString());
    }

    /**
     * Show the chart.
     */
    private void showChart() {
        Statistics statistics = getMainApp().getSimulator().getStatistics();

        ObservableList<XYChart.Series<Double,Integer>> chartData = FXCollections.observableArrayList();
        chartData.add(statistics.getDataSeriesInfected());
        chartData.add(statistics.getDataSeriesHealthy());
        chartData.add(statistics.getDataSeriesRecovered());
        chartData.add(statistics.getDataSeriesDeceased());
        areaChart.setData(chartData);
        yAxis.setUpperBound(statistics.getInitialPopulation());
    }

    /**
     * Show the play button with an icon reflective of the simulation state.
     */
    private void showPlayButton() {
        playButton.getStyleClass().add(getMainApp().getSimulator().getSimulationState().styleClass);
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
    private void resetChart() {
        areaChart.setData(FXCollections.emptyObservableList());
        areaChart.setAnimated(true);
        xAxis.setAutoRanging(true);
    }

    /**
     * Reset the play button.
     */
    private void resetPlayButton() {
        List<String> buttonStyles = Arrays.stream(SimulationState.values()).map(x -> x.styleClass).collect(Collectors.toList());
        playButton.getStyleClass().removeIf(buttonStyles::contains);
        playButton.setDisable(false);
    }

    /**
     * Assign a reset verison of the simulator to the main app.
     */
    private void resetSimulator() {
        getMainApp().setSimulator(getMainApp().getSimulator().reset());
    }

}

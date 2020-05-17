package org.epi.view;

import org.epi.model.BehaviourDistribution;
import org.epi.model.Pathogen;
import org.epi.model.SimulationState;
import org.epi.model.Simulator;
import org.epi.model.Statistics;
import org.epi.model.World;
import org.epi.util.Error;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.apache.commons.math3.util.Precision;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.epi.model.SimulationState.ENDED;
import static org.epi.model.SimulationState.PAUSE;
import static org.epi.model.SimulationState.RUN;
import static org.epi.model.World.MAX_POPULATION;
import static org.epi.model.World.MIN_POPULATION;

public class SimulatorController extends Controller {

    /** Symbol for seconds.*/
    private static final String SEC_EXT = "s";
    /** Symbol of percentages (repetition due to Java convention).*/
    private static final String PERCENT_EXT = "%%";

    /** Naturally, the maximum FPS in JavaFX is 60, from which the minimum time interval in seconds can be deduced.*/
    private static final double MIN_TIME_INTERVAL = 0.1;
    /** The maximum time interval in seconds; generally simulations tend to be 1-2 minutes.*/
    private static final double MAX_TIME_INTERVAL = 60;
    /** Minimum percentage.*/
    private static final double MIN_PERCENT = 0;
    /** Maximum percentage.*/
    private static final double MAX_PERCENT = 100;

    // Simulation panes ----------------------------

    @FXML
    private Pane cityPane;
    @FXML
    private Pane quarantinePane;

    // Statistics table ----------------------------

    @FXML
    private Label deceasedLabel;
    @FXML
    private Label recoveredLabel;
    @FXML
    private Label healthyLabel;
    @FXML
    private Label sickLabel;

    // Time chart ----------------------------

    @FXML
    private StackedAreaChart<Double, Integer> areaChart;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private NumberAxis xAxis;

    // Player buttons ----------------------------

    @FXML
    private JFXButton playButton;
    @FXML
    private JFXButton resetButton;

    // Parameter sliders ----------------------------

    @FXML
    private Label totalPopulationLabel;
    @FXML
    private JFXSlider totalPopulationSlider;
    @FXML
    private Label sickPopulationLabel;
    @FXML
    private JFXSlider sickPopulationSlider;
    @FXML
    private Label quarantineCapacityLabel;
    @FXML
    private JFXSlider quarantineCapacitySlider;

    @FXML
    private Tooltip testingFrequencyTooltip;
    @FXML
    private Label testingFrequencyLabel;
    @FXML
    private JFXSlider testingFrequencySlider;

    @FXML
    private Label detectionRateLabel;
    @FXML
    private JFXSlider detectionRateSlider;

    @FXML
    private Tooltip humanTooltip;

    @FXML
    private Label normalProportionLabel;
    @FXML
    private JFXSlider normalProportionSlider;

    @FXML
    private Tooltip socialDistancingTooltip;
    @FXML
    private Label socialDistancingProportionLabel;
    @FXML
    private JFXSlider socialDistancingProportionSlider;

    @FXML
    private Tooltip contactTracingTooltip;
    @FXML
    private Label contactTracingProportionLabel;
    @FXML
    private JFXSlider contactTracingProportionSlider;

    @FXML
    private Label lifespanLabel;
    @FXML
    private JFXSlider lifespanSlider;

    @FXML
    private Tooltip immunityDurationTooltip;
    @FXML
    private Label immunityDurationLabel;
    @FXML
    private JFXSlider immunityDurationSlider;

    @FXML
    private Label immunityRateLabel;
    @FXML
    private JFXSlider immunityRateSlider;

    @FXML
    private Tooltip transmissionRiskTooltip;
    @FXML
    private Label transmissionRiskLabel;
    @FXML
    private JFXSlider transmissionRiskSlider;

    @FXML
    private Label fatalityRateLabel;
    @FXML
    private JFXSlider fatalityRateSlider;

    @FXML
    private JFXButton generateButton;

    //---------------------------- Construction methods ----------------------------

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        initTooltips();
        initSliders();
    }

    /**
     * Perform all assignment details.
     */
    public void showSimulation() {
        showPanes();
        showTable();
        showChart();
        showPlayButton();
        showParameters();

        initEvents();
    }

    /**
     * Initialise event listeners
     */
    private void initEvents() {
        // Switch the style class of the play button.
        getMainApp().getSimulator().getSimulationStateProperty().addListener((observable, oldValue, newValue) -> {
            styleSwitch(newValue);

            resetButton.setDisable(newValue == RUN);
            generateButton.setDisable(newValue == RUN);

            if (newValue == ENDED) {
                playButton.setDisable(true);
                pauseXAxis();
            }
        });

        initParameters();
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

    /**
     * Generate a simulator with the given parameter settings
     */
    @FXML
    private void handleGenerate() {
        resetPanes();
        resetChart();
        resetPlayButton();

        getMainApp().setSimulator(new Simulator(getMainApp().getWorld().reset(),
                getMainApp().getBehaviourDistribution().copy(),
                getMainApp().getPathogen().reproduce()));

        showSimulation();
    }

    //---------------------------- Instance helper methods ----------------------------

    /**
     * Initialise parameters.
     */
    private void initParameters() {
        initWorld();
        initBehaviour();
        initPathogen();
    }

    /**
     * Initialise all tooltips
     */
    private void initTooltips() {
        initTooltip(testingFrequencyTooltip);
        initTooltip(humanTooltip);
        initTooltip(socialDistancingTooltip);
        initTooltip(contactTracingTooltip);
        initTooltip(immunityDurationTooltip);
        initTooltip(transmissionRiskTooltip);
    }

    /**
     * Initialise sliders for all parameters.
     */
    private void initSliders() {
        initWorldSliders();
        initBehaviourSliders();
        initPathogenSliders();
    }

    /**
     * Initialise world sliders.
     */
    private void initWorldSliders() {
        initSlider(totalPopulationLabel, totalPopulationSlider,
                0, "", MIN_POPULATION, MAX_POPULATION);

        // Initialise dependency to total population for certain sliders.
        totalPopulationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double value = newValue.doubleValue();
            sickPopulationSlider.setMax(value);

            if (value == MIN_POPULATION) {
                sickPopulationSlider.setDisable(true);
            } else if (sickPopulationSlider.isDisabled()) {
                sickPopulationSlider.setDisable(false);
            }
        });

        initSlider(sickPopulationLabel, sickPopulationSlider,
                0, "", MIN_POPULATION, totalPopulationSlider.valueProperty().intValue());

        initSlider(quarantineCapacityLabel, quarantineCapacitySlider,
                0, "", MIN_POPULATION, MAX_POPULATION);

        initSlider(testingFrequencyLabel, testingFrequencySlider,
                1, SEC_EXT, MIN_TIME_INTERVAL, MAX_TIME_INTERVAL);

        initSlider(detectionRateLabel, detectionRateSlider,
                1, PERCENT_EXT, MIN_PERCENT, MAX_PERCENT);
    }

    /**
     * Initialise behaviour sliders.
     */
    private void initBehaviourSliders() {
        ChangeListener<Number> distributionListener = (observable, oldValue, newValue) -> {
            boolean normalIsEmpty = normalProportionSlider.getValue() == 0;
            boolean socialDistancingIsEmpty = socialDistancingProportionSlider.getValue() == 0;
            boolean contactTracingIsEmpty = contactTracingProportionSlider.getValue() == 0;

            able(normalProportionSlider);
            able(socialDistancingProportionSlider);
            able(contactTracingProportionSlider);

            if (normalIsEmpty && socialDistancingIsEmpty) {
                disable(contactTracingProportionSlider);
            } else if (normalIsEmpty && contactTracingIsEmpty) {
                disable(socialDistancingProportionSlider);
            } else if (socialDistancingIsEmpty && contactTracingIsEmpty) {
                disable(normalProportionSlider);
            }
        };

        DoubleBinding proportionSum = Bindings.createDoubleBinding(
                () -> normalProportionSlider.getValue() + socialDistancingProportionSlider.getValue() + contactTracingProportionSlider.getValue(),
                normalProportionSlider.valueProperty(),
                socialDistancingProportionSlider.valueProperty(),
                contactTracingProportionSlider.valueProperty()
                );
        DoubleBinding normalPercent = Bindings.createDoubleBinding(
                () -> 100 * normalProportionSlider.getValue() / proportionSum.getValue(), proportionSum);
        DoubleBinding socialDistancingPercent = Bindings.createDoubleBinding(
                () -> 100 * socialDistancingProportionSlider.getValue() / proportionSum.getValue(), proportionSum);
        DoubleBinding contactTracingPercent = Bindings.createDoubleBinding(
                () -> 100 * contactTracingProportionSlider.getValue() / proportionSum.getValue(), proportionSum);

        initPropSlider(normalProportionLabel, normalProportionSlider, distributionListener, normalPercent);
        initPropSlider(socialDistancingProportionLabel, socialDistancingProportionSlider, distributionListener, socialDistancingPercent);
        initPropSlider(contactTracingProportionLabel, contactTracingProportionSlider, distributionListener, contactTracingPercent);
    }

    /**
     * Initialise pathogen sliders.
     */
    private void initPathogenSliders() {
        initSlider(lifespanLabel, lifespanSlider,
                1, SEC_EXT, MIN_TIME_INTERVAL, MAX_TIME_INTERVAL);
        initSlider(immunityDurationLabel, immunityDurationSlider,
                1, SEC_EXT, MIN_TIME_INTERVAL, MAX_TIME_INTERVAL);
        initSlider(immunityRateLabel, immunityRateSlider,
                1, PERCENT_EXT, MIN_PERCENT, MAX_PERCENT);
        initSlider(transmissionRiskLabel, transmissionRiskSlider,
                1, PERCENT_EXT, MIN_PERCENT, MAX_PERCENT);
        initSlider(fatalityRateLabel, fatalityRateSlider,
                1, PERCENT_EXT, MIN_PERCENT, MAX_PERCENT);
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
        sickLabel.textProperty().bind(statistics.sickProperty().asString());
    }

    /**
     * Show the chart.
     */
    private void showChart() {
        Statistics statistics = getMainApp().getSimulator().getStatistics();

        ObservableList<XYChart.Series<Double,Integer>> chartData = FXCollections.observableArrayList();
        chartData.add(statistics.getDataSeriesSick());
        chartData.add(statistics.getDataSeriesHealthy());
        chartData.add(statistics.getDataSeriesRecovered());
        chartData.add(statistics.getDataSeriesDeceased());
        areaChart.setData(chartData);
        yAxis.setUpperBound(getMainApp().getSimulator().getWorld().getPopulationTotal());
    }

    /**
     * Show the play button with an icon reflective of the simulation state.
     */
    private void showPlayButton() {
        playButton.getStyleClass().add(getMainApp().getSimulator().getSimulationState().styleClass);
    }

    /**
     * Show parameters values.
     */
    private void showParameters() {
        World world = getMainApp().getSimulator().getWorld();
        BehaviourDistribution behaveDist = getMainApp().getSimulator().getBehaviourDistribution();
        Pathogen pathogen = getMainApp().getSimulator().getPathogen();

        totalPopulationSlider.setValue(world.getPopulationTotal());
        sickPopulationSlider.setValue(world.getSickTotal());
        quarantineCapacitySlider.setValue(world.getQuarantineCapacity());
        testingFrequencySlider.setValue(world.getTestingFrequency());
        detectionRateSlider.setValue(MAX_PERCENT * world.getDetectionRate());

        normalProportionSlider.setValue(behaveDist.getNormalProportion());
        socialDistancingProportionSlider.setValue(behaveDist.getSocialDistancingProportion());
        contactTracingProportionSlider.setValue(behaveDist.getContactTracingProportion());

        lifespanSlider.setValue(pathogen.getLifespan());
        immunityDurationSlider.setValue(pathogen.getImmunityDuration());
        immunityRateSlider.setValue(MAX_PERCENT * pathogen.getImmunityRate());
        transmissionRiskSlider.setValue(MAX_PERCENT * pathogen.getTransmissionRisk());
        fatalityRateSlider.setValue(MAX_PERCENT * pathogen.getFatalityRate());
    }

    /**
     * Initialise world parameters.
     */
    private void initWorld() {
        World world = getMainApp().getWorld();

        totalPopulationSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                world.setPopulationTotal(newValue.intValue()));

        sickPopulationSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                world.setSickTotal(newValue.intValue()));

        quarantineCapacitySlider.valueProperty().addListener((observable, oldValue, newValue) ->
                world.setQuarantineCapacity(newValue.intValue()));

        testingFrequencySlider.valueProperty().addListener((observable, oldValue, newValue) ->
                world.setTestingFrequency(newValue.doubleValue()));

        detectionRateSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                world.setDetectionRate(newValue.doubleValue() / MAX_PERCENT));

    }

    /**
     * Initialise behaviour parameters.
     */
    private void initBehaviour() {
        BehaviourDistribution behaveDist = getMainApp().getBehaviourDistribution();

        normalProportionSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                behaveDist.setNormalProportion(newValue.doubleValue()));

        socialDistancingProportionSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                behaveDist.setSocialDistancingProportion(newValue.doubleValue()));

        contactTracingProportionSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                behaveDist.setContactTracingProportion(newValue.doubleValue()));

    }

    /**
     * Initialise pathogen parameters.
     */
    private void initPathogen() {
        Pathogen pathogen = getMainApp().getPathogen();

        lifespanSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                pathogen.setLifespan(newValue.doubleValue()));

        immunityDurationSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                pathogen.setImmunityDuration(newValue.doubleValue()));

        immunityRateSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                pathogen.setImmunityRate(newValue.doubleValue() / MAX_PERCENT));

        transmissionRiskSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                pathogen.setTransmissionRisk(newValue.doubleValue() / MAX_PERCENT));

        fatalityRateSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                pathogen.setFatalityRate(newValue.doubleValue() / MAX_PERCENT));

    }

    /**
     * Reset the city and quarantine panes.
     */
    private void resetPanes() {
        cityPane.getChildren().clear();
        quarantinePane.getChildren().clear();
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
     * Assign a reset version of the simulator to the main app.
     */
    private void resetSimulator() {
        getMainApp().setSimulator(getMainApp().getSimulator().reset());
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

    //---------------------------- Static helper methods ----------------------------

    /**
     * Initialise a slider and a label with a certain precision and bounds.
     *
     * @param label a label
     * @param slider a slider
     * @param precision the precision of the slider
     * @param extension the value extension
     * @param min the minimum slider value
     * @param max the maximum slider value
     */
    private static void initSlider(Label label, JFXSlider slider, int precision, String extension, double min, double max) {
        BindLabelToSlider(label, precision, extension, slider);
        setPrecision(slider, precision);
        slider.setMin(Precision.round(min, precision));
        slider.setMax(Precision.round(max, precision));
    }

    /**
     * Initialise a behaviour proportion slider.
     *
     * @param label a label
     * @param slider a proportion slider
     * @param distributionListener the distribution listener
     * @param proportionPercentage the proportion percentage for the given slider.
     */
    private void initPropSlider( Label label, JFXSlider slider, ChangeListener<Number> distributionListener, DoubleBinding proportionPercentage) {
        label.textProperty().bind(Bindings.format(extFormat(1, PERCENT_EXT), proportionPercentage));
        setPrecision(slider, 1);
        slider.setMin(Precision.round(MIN_PERCENT, 1));
        slider.setMax(Precision.round(MAX_PERCENT, 1));
        slider.valueProperty().addListener(distributionListener);
    }

    /**
     * Bind a label to a the value of a slider.
     *
     * @param label a label
     * @param precision the precision to show the slider's value
     * @param extension the value extension
     * @param slider a slider
     */
    private static void BindLabelToSlider(Label label, int precision, String extension, JFXSlider slider) {
        label.textProperty().bind(
                Bindings.format(extFormat(precision, extension), slider.valueProperty()));
    }

    /**
     * Set the slider to a certain precision.
     *
     * @param slider the slider
     * @param precision the number of decimal places
     */
    private static void setPrecision(JFXSlider slider, int precision) {
        slider.valueProperty().addListener((observable, oldValue, newValue) ->
                slider.setValue(Precision.round(newValue.doubleValue(), precision)));
    }

    /**
     * Return a string pattern for double values with the given extension.
     *
     * @param precision the precision of the value shown.
     * @param extension the string extension
     * @return a string pattern for double values with the given extension
     */
    private static String extFormat(int precision,  String extension) {
        return "%." + precision + "f" + extension;
    }

    /**
     * Initialise a tooltip.
     *
     * @param tooltip a tooltip
     */
    private static void initTooltip(Tooltip tooltip) {
        tooltip.setShowDelay(Duration.ZERO);
        tooltip.setShowDuration(Duration.INDEFINITE);
        tooltip.setHideDelay(Duration.ZERO);
    }

    /**
     * Make a slider usable.
     *
     * @param slider a slider
     */
    private static void able(JFXSlider slider) {
        if (slider.isDisabled()) {
            slider.setDisable(false);
        }
    }

    /**
     * Disable a slider.
     *
     * @param slider a slider
     */
    private static void disable(JFXSlider slider) {
        slider.setValue(slider.getMax());
        slider.setDisable(true);
    }

}

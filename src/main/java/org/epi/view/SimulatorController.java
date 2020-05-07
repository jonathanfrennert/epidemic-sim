package org.epi.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import org.epi.MainApp;
import org.epi.model.Simulator;
import org.epi.model.Statistics;

import java.util.Stack;

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
    private StackedAreaChart statsChart;

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

        showStackedChart(simulator);

        this.cityPane.getChildren().add(simulator.getWorld().getCity().getArea());
        this.quarantinePane.getChildren().add(simulator.getWorld().getQuarantine().getArea());

        this.deceasedLabel.textProperty().bind(statistics.deceasedProperty().asString());
        this.recoveredLabel.textProperty().bind(statistics.recoveredProperty().asString());
        this.healthyLabel.textProperty().bind(statistics.healthyProperty().asString());
        this.infectedLabel.textProperty().bind(statistics.infectedProperty().asString());

    }

    private void showStackedChart(Simulator simulator){
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time progression");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Population");

        //StackedAreaChart statsChart = new StackedAreaChart(xAxis,yAxis);

        XYChart.Series dataSeriesDeceased = new XYChart.Series();
        dataSeriesDeceased.setName("Deceased");

        dataSeriesDeceased.getData().add(new XYChart.Data(0,0));
        dataSeriesDeceased.getData().add(new XYChart.Data(2,4));

        XYChart.Series dataSeriesInfected = new XYChart.Series();
        dataSeriesInfected.setName("Infected");

        dataSeriesInfected.getData().add(new XYChart.Data(0,1));
        dataSeriesInfected.getData().add(new XYChart.Data(2,10));

        XYChart.Series dataSeriesHealthy = new XYChart.Series();
        dataSeriesHealthy.setName("Healthy");

        dataSeriesHealthy.getData().add(new XYChart.Data(0,100));
        dataSeriesHealthy.getData().add(new XYChart.Data(2,80));

        //TODO try to set 4th label in line with the others
        XYChart.Series dataSeriesRecovered = new XYChart.Series();
        dataSeriesRecovered.setName("Recovered");

        dataSeriesRecovered.getData().add(new XYChart.Data(0,0));
        dataSeriesRecovered.getData().add(new XYChart.Data(2,3));

        statsChart.getData().addAll(dataSeriesDeceased,dataSeriesHealthy,dataSeriesInfected, dataSeriesRecovered);
        

    }

}

package org.epi.view;

import javafx.scene.chart.StackedAreaChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import org.epi.model.Disease;
import org.epi.model.Simulator;
import org.epi.model.Statistics;
import org.epi.model.World;

/**
 * The controller for the simulation pane.
 */
public class SimulationController extends Controller {
    private Simulator simulator;

    private Disease diseaseParams;

    private World worldParams;

    private Pane simulatorView;

    private Statistics populationStats;

    //TODO figure out if the highlighted instance vars work as-is
    private StackedAreaChart statusChart;

    private TableView statusCounter;

    private TableColumn statusTitleColumn;

    private TableColumn statusCountColumn;

    private TableView parameters;

    private TableColumn parameterTitleColumn;

    private TableColumn parameterFields;


}

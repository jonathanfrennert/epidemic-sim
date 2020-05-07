package org.epi;

import org.epi.model.BehaviourDistribution;
import org.epi.model.Pathogen;
import org.epi.model.Simulator;
import org.epi.model.World;
import org.epi.view.RootLayoutController;
import org.epi.view.SimulatorController;

import javafx.scene.layout.AnchorPane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/** Main class for the application.
 *
 * Everything is initialised and run from this class.*/
public class MainApp extends Application {

    /** Minimum width of the application window in pixels.*/
    private static final double MIN_WIDTH = 555;
    /** Minimum height of the application window in pixels.*/
    private static final double MIN_HEIGHT = 610;
    /** Minimum width of the application window in pixels.*/
    private static final double MAX_WIDTH = 1100;
    /** Maximum height of the application window in pixels.*/
    private static final double MAX_HEIGHT = 740;
    /** Maximum width of the application window in pixels.*/
    private static final double PREF_WIDTH = 963;
    /** Maximum height of the application window in pixels.*/

    /** The main container for the application.*/
    private Stage primaryStage;

    /** The root layout for the application.*/
    private BorderPane rootLayout;

    /** Simulator currently showing its simulation.*/
    private Simulator simulator;

    /**
     * Constructor for the main application.
     */
    public MainApp() {
        // Added a sample simulation
        World world = new World(0.5,15);
        BehaviourDistribution dist = new BehaviourDistribution(1,0, 0);
        Pathogen pathogen = new Pathogen(5,0.1,0.3,0.8,10);

        simulator = new Simulator(300, world, dist, pathogen);
    }

    /**
     * Standard Java main method; used to launch the application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Epi");

        // Set the application icon.
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/Icon.png").toExternalForm()));

        //Set size specs.
        this.primaryStage.setMinWidth(MIN_WIDTH);
        this.primaryStage.setMinHeight(MIN_HEIGHT);
        this.primaryStage.setMaxWidth(MAX_WIDTH);
        this.primaryStage.setMaxHeight(MAX_HEIGHT);
        this.primaryStage.setWidth(PREF_WIDTH);
        this.primaryStage.setHeight(PREF_HEIGHT);

        initRootLayout();
        showSimulator();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load the root layout from the fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/RootLayout.fxml"));
            rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutController rootLayoutController = loader.getController();
            rootLayoutController.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the simulator inside the root layout.
     */
    public void showSimulator() {
        try {
            // Load the simulator from the fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/Simulator.fxml"));
            AnchorPane simulator = loader.load();

            // Set the simulator into the center of root layout.
            rootLayout.setCenter(simulator);

            // Give the controller access to the main app.
            SimulatorController controller = loader.getController();
            controller.setMainApp(this);

            this.simulator.getTimer().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getter for {@link #simulator}
     *
     * @return {@link #simulator}
     */
    public Simulator getSimulator() {
        return simulator;
    }

}
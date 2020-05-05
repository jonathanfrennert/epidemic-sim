package org.epi;

import javafx.scene.layout.AnchorPane;
import org.epi.model.*;
import org.epi.view.RootLayoutController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.epi.view.SimulatorController;

import java.io.IOException;

/** Main class for the application.
 *
 * Everything is initialised and run from this class.*/
public class MainApp extends Application {

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
        BehaviourDistribution dist = new BehaviourDistribution(0,0, 1);
        Pathogen pathogen = new Pathogen(5,0.3,0.3,0.8,20);

        simulator = new Simulator(200, world, dist, pathogen);
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
package org.epi;

import org.epi.model.BehaviourDistribution;
import org.epi.model.human.Pathogen;
import org.epi.model.Simulator;
import org.epi.model.world.World;
import org.epi.util.Error;
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
import java.util.Objects;

/** Main class for the application.
 *
 * Everything is initialised and run from this class.*/
public class MainApp extends Application {

    /** Minimum width of the application window in pixels.*/
    private static final double MIN_WIDTH = 614;
    /** Minimum height of the application window in pixels.*/
    private static final double MIN_HEIGHT = 700;
    /** Minimum width of the application window in pixels.*/
    private static final double MAX_WIDTH = 1920;
    /** Maximum height of the application window in pixels.*/
    private static final double MAX_HEIGHT = 1080;
    /** Maximum width of the application window in pixels.*/
    private static final double PREF_WIDTH = 1044;
    /** Maximum height of the application window in pixels.*/
    private static final double PREF_HEIGHT = MIN_HEIGHT;

    /** The main container for the application.*/
    private Stage primaryStage;

    /** The root layout for the application.*/
    private BorderPane rootLayout;

    /** Simulator currently being showed.*/
    private Simulator simulator;

    /** World edited in parameters.*/
    private World world;

    /** Behaviour distribution edited in parameters.*/
    private BehaviourDistribution behaviourDistribution;

    /** Pathogen edited in parameters.*/
    private Pathogen pathogen;

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
        primaryStage.getIcons().add(new Image(getClass().getResource("/img/icon.png").toExternalForm()));

        //Set size specifications.
        this.primaryStage.setMinWidth(MIN_WIDTH);
        this.primaryStage.setMinHeight(MIN_HEIGHT);
        this.primaryStage.setMaxWidth(MAX_WIDTH);
        this.primaryStage.setMaxHeight(MAX_HEIGHT);
        this.primaryStage.setWidth(PREF_WIDTH);
        this.primaryStage.setHeight(PREF_HEIGHT);

        newSimulator();

        initRootLayout();
        showSimulator();
    }

    /**
     * Generate a sample simulator.
     */
    private void newSimulator() {
        this.world = new World(250, 5, 100, 0.4,10);
        this.behaviourDistribution = new BehaviourDistribution(50,50, 50);
        this.pathogen = new Pathogen(10,0.1,0.1,0.7,20);

        this.simulator = new Simulator(this.world.reset(), this.behaviourDistribution.copy(), this.pathogen.reproduce());
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
            controller.showSimulation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //---------------------------- Getters & Setters ----------------------------


    /**
     * Getter for {@link #primaryStage}.
     *
     * @return {@link #primaryStage}
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Getter for {@link #simulator}.
     *
     * @return {@link #simulator}
     */
    public Simulator getSimulator() {
        return simulator;
    }

    /**
     * Setter for {@link #simulator}.
     *
     * @param simulator {@link #simulator}
     * @throws NullPointerException if the given parameter is null
     */
    public void setSimulator(Simulator simulator) {
        Objects.requireNonNull(simulator);
        this.simulator = simulator;
    }

    /**
     * Getter for {@link #world}.
     */
    public World getWorld() {
        return world;
    }

    /**
     * Setter for {@link #world}.
     *
     * @throws NullPointerException if the given parameter is null
     */
    public void setWorld(World world) {
        Objects.requireNonNull(world, Error.getNullMsg("world"));
        this.world = world;
    }

    /**
     * Getter for {@link #behaviourDistribution}.
     */
    public BehaviourDistribution getBehaviourDistribution() {
        return behaviourDistribution;
    }

    /**
     * Setter for {@link #behaviourDistribution}.
     *
     * @throws NullPointerException if the given parameter is null
     */
    public void setBehaviourDistribution(BehaviourDistribution behaviourDistribution) {
        Objects.requireNonNull(behaviourDistribution, Error.getNullMsg("behaviour distribution"));
        this.behaviourDistribution = behaviourDistribution;
    }

    /**
     * Getter for {@link #pathogen}.
     */
    public Pathogen getPathogen() {
        return pathogen;
    }

    /**
     * Setter for {@link #pathogen}.
     *
     * @throws NullPointerException if the given parameter is null
     */
    public void setPathogen(Pathogen pathogen) {
        Objects.requireNonNull(pathogen, Error.getNullMsg("pathogen"));
        this.pathogen = pathogen;
    }

}
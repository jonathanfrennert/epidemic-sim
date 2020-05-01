package org.epi;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.epi.model.Disease;
import org.epi.model.Simulator;
import org.epi.model.World;
import org.epi.view.RootLayoutController;

import java.io.IOException;

/** Main class for the application.
 *
 * Everything is initialised and run from this class.*/
public class MainApp extends Application {

    /** The main container for the application.*/
    private Stage primaryStage;

    /** The root layout for the application.*/
    private BorderPane rootLayout;

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
        primaryStage.setTitle("Epidemic Simulator");

        // Set the application icon.
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/Epi.png").toExternalForm()));

        initRootLayout();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Simulator parameters.
            World world = new World(150,0.2);
            Disease disease = new Disease(0.2,0.1,10);

            // Simulator initialisation.
            Simulator simulator = new Simulator(world, disease);
            AnimationTimer worldTime = simulator.getWorldTime();

            //TODO initRootLayout - uses RootLayoutController

            // Load the root layout from the fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            //TODO showSimulation - uses SimulationController
            
            // Set the world view.
            rootLayout.setCenter(simulator.getWorldView());

            // Set the world statistics.
            Label stats = new Label();
            stats.textProperty().bind(simulator.getWorldStats().getTextProperty());
            rootLayout.setBottom(stats);

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);

            // Give the controller access to the main app.
            RootLayoutController rootLayoutController = loader.getController();
            rootLayoutController.setMainApp(this);

            primaryStage.show();
            worldTime.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
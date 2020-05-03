package org.epi;

import org.epi.view.RootLayoutController;

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
        primaryStage.setTitle("Epi");

        // Set the application icon.
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/Epi.png").toExternalForm()));

        initRootLayout();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load the root layout from the fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

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

}
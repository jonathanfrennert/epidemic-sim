package org.epi;

import org.epi.model.Disease;
import org.epi.model.Simulator;
import org.epi.model.World;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {

        // Setting application title and icon.
        stage.setTitle("Epidemic Simulator");
        stage.getIcons().add(new Image(getClass().getResource("/images/Epi.png").toExternalForm()));

        // Simulator parameters.
        World world = new World(150,0.2);
        Disease disease = new Disease(0.2,0.1,10);

        // Simulator initialisation.
        Simulator simulator = new Simulator(world, disease);
        AnimationTimer worldTime = simulator.getWorldTime();

        // Set the main pane.
        BorderPane root = new BorderPane();

        // Set the world view.
        root.setCenter(simulator.getWorldView());

        // Set the world statistics.
        Label stats = new Label();
        stats.textProperty().bind(simulator.getWorldStats().getTextProperty());
        root.setBottom(stats);

        // Set the stage.
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);

        // Launch.
        stage.show();
        worldTime.start();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
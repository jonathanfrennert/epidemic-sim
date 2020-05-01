package org.epi;

import org.epi.model.BehaviourDistribution;
import org.epi.model.Pathogen;
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
        World world = new World(0.8,10);
        Pathogen pathogen = new Pathogen(5,0.2,0.9,0.4,20);
        BehaviourDistribution behaveDist = new BehaviourDistribution(1,0,0);

        // Simulator initialisation.
        Simulator simulator = new Simulator(150, world, behaveDist, pathogen);
        AnimationTimer worldTime = simulator.getTimer();

        // Set the main pane.
        BorderPane root = new BorderPane();

        // Set the world view.
        root.setLeft(simulator.getWorld().getCity().getArea());
        root.setRight(simulator.getWorld().getQuarantine().getArea());

        // Set the world statistics.
        Label stats = new Label();
        stats.textProperty().bind(simulator.getStatistics().getTextProperty());
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
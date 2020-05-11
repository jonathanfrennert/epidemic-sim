package org.epi.model;

import org.epi.util.Error;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.chart.XYChart;

import java.util.Objects;

/** Get real-time statistics for a simulator.*/
public class Statistics {

    /** The back reference to the world for these statistics.*/
    private final World world;

    /** Initial population count.*/
    private final IntegerProperty initialPopulation;

    /** The number of healthy people in the given simulation.*/
    private final IntegerProperty healthy;

    /** {@link #healthy} data series.*/
    private final XYChart.Series<Double, Integer> dataSeriesHealthy;

    /** The number of infected humans in the given simulation.*/
    private final IntegerProperty infected;

    /** {@link #infected} data series.*/
    private final XYChart.Series<Double, Integer> dataSeriesInfected;

    /** The number of recovered humans in the given simulation.*/
    private final IntegerProperty recovered;

    /** {@link #recovered} data series.*/
    private final XYChart.Series<Double, Integer> dataSeriesRecovered;

    /** The number difference between the current population count and the initial population count.*/
    private final IntegerProperty deceased;

    /** {@link #deceased} data series.*/
    private final XYChart.Series<Double, Integer> dataSeriesDeceased;


    //---------------------------- Constructor ----------------------------

    /**
     * Create statistics for a world
     *
     * @param world a world
     * @throws NullPointerException if the given parameter is null
     */
    public Statistics(World world) {
        Objects.requireNonNull(world, Error.getNullMsg("world"));

        this.world = world;

        this.healthy = new SimpleIntegerProperty(getStatusCount(Status.HEALTHY));
        this.infected = new SimpleIntegerProperty(getStatusCount(Status.INFECTED));
        this.recovered = new SimpleIntegerProperty(getStatusCount(Status.RECOVERED));
        this.deceased = new SimpleIntegerProperty(0);

        this.initialPopulation = new SimpleIntegerProperty(healthy.get() + infected.get() + recovered.get());

        this.dataSeriesHealthy = new XYChart.Series<>();
        this.dataSeriesDeceased = new XYChart.Series<>();
        this.dataSeriesInfected = new XYChart.Series<>();
        this.dataSeriesRecovered = new XYChart.Series<>();
    }

    //---------------------------- Helper methods ----------------------------

    /**
     * Get the number of humans of a given status in the world.
     *
     * @param status a status
     * @return the number of humans in the world with the given status
     */
    private int getStatusCount(Status status) {
        int cityCount = world.getCity().getPopulation().filtered(human -> human.getStatus() == status).size();
        int quarantineCount = world.getQuarantine().getPopulation().filtered(human -> human.getStatus() == status).size();

        return cityCount + quarantineCount;
    }

    //---------------------------- Simulator actions ----------------------------

    /**
     * Update the population counts for the given world.
     *
     * @throws NullPointerException if the given parameters is null
     */
    public void update() {
        healthy.set(getStatusCount(Status.HEALTHY));
        infected.set(getStatusCount(Status.INFECTED));
        recovered.set(getStatusCount(Status.RECOVERED));
        deceased.set(initialPopulation.get() - healthy.get() - infected.get() - recovered.get());

        double time = world.getTotalElapsedSeconds();

        dataSeriesHealthy.getData().add(new XYChart.Data<>(time, healthy.get()));
        dataSeriesInfected.getData().add(new XYChart.Data<>(time, infected.get()));
        dataSeriesRecovered.getData().add(new XYChart.Data<>(time, recovered.get()));
        dataSeriesDeceased.getData().add(new XYChart.Data<>(time, recovered.get()));
    }

    //---------------------------- Getters & Setters ----------------------------

    /**
     * Getter for {@link #healthy}.
     *
     * @return {@link #healthy}
     */
    public int getHealthy() {
        return healthy.get();
    }

    /**
     * Getter for {@link #healthy} {@link IntegerProperty}.
     *
     * @return {@link #healthy}
     */
    public IntegerProperty healthyProperty() {
        return healthy;
    }

    /**
     * Getter for {@link #infected}.
     *
     * @return {@link #infected}
     */
    public int getInfected() {
        return infected.get();
    }


    /**
     * Getter for {@link #infected} {@link IntegerProperty}.
     *
     * @return {@link #infected}
     */
    public IntegerProperty infectedProperty() {
        return infected;
    }

    /**
     * Getter for {@link #recovered}.
     *
     * @return {@link #recovered}
     */
    public int getRecovered() {
        return recovered.get();
    }

    /**
     * Getter for {@link #recovered} {@link IntegerProperty}.
     *
     * @return {@link #recovered}
     */
    public IntegerProperty recoveredProperty() {
        return recovered;
    }

    /**
     * Getter for {@link #recovered}.
     *
     * @return {@link #recovered}
     */
    public int getDeceased() {
        return deceased.get();
    }

    /**
     * Getter for {@link #deceased} {@link IntegerProperty}.
     *
     * @return {@link #deceased}
     */
    public IntegerProperty deceasedProperty() {
        return deceased;
    }

    /**
     * Getter for {@link #initialPopulation}.
     *
     * @return {@link #initialPopulation}
     */
    public int getInitialPopulation() {
        return initialPopulation.get();
    }

    /**
     * Getter for {@link #initialPopulation} {@link IntegerProperty}.
     *
     * @return {@link #initialPopulation}
     */
    public IntegerProperty initialPopulationProperty() {
        return initialPopulation;
    }

    public XYChart.Series<Double,Integer> getDataSeriesRecovered(){
        return dataSeriesRecovered;
    }

    public XYChart.Series<Double,Integer> getDataSeriesInfected(){
        return dataSeriesInfected;
    }

    public XYChart.Series<Double,Integer> getDataSeriesHealthy(){
        return dataSeriesHealthy;
    }

    public XYChart.Series<Double,Integer> getDataSeriesDeceased(){
        return dataSeriesDeceased;
    }

}

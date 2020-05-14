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

    /** The number of healthy people in the given simulation.*/
    private final IntegerProperty healthy;

    /** {@link #healthy} data series.*/
    private final XYChart.Series<Double, Integer> dataSeriesHealthy;

    /** The number of sick humans in the given simulation.*/
    private final IntegerProperty sick;

    /** {@link #sick} data series.*/
    private final XYChart.Series<Double, Integer> dataSeriesSick;

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
        this.sick = new SimpleIntegerProperty(getStatusCount(Status.SICK));
        this.recovered = new SimpleIntegerProperty(getStatusCount(Status.RECOVERED));
        this.deceased = new SimpleIntegerProperty(0);

        this.dataSeriesHealthy = new XYChart.Series<>();
        this.dataSeriesHealthy.getData().add(new XYChart.Data<>(0.0, healthy.get()));

        this.dataSeriesSick = new XYChart.Series<>();
        this.dataSeriesSick.getData().add(new XYChart.Data<>(0.0, sick.get()));

        this.dataSeriesRecovered = new XYChart.Series<>();
        this.dataSeriesRecovered.getData().add(new XYChart.Data<>(0.0, recovered.get()));

        this.dataSeriesDeceased = new XYChart.Series<>();
        this.dataSeriesDeceased.getData().add(new XYChart.Data<>(0.0, deceased.get()));
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
        sick.set(getStatusCount(Status.SICK));
        recovered.set(getStatusCount(Status.RECOVERED));
        deceased.set(world.getPopulationTotal() - healthy.get() - sick.get() - recovered.get());

        double time = world.getTotalElapsedSeconds();

        dataSeriesHealthy.getData().add(new XYChart.Data<>(time, healthy.get()));
        dataSeriesSick.getData().add(new XYChart.Data<>(time, sick.get()));
        dataSeriesRecovered.getData().add(new XYChart.Data<>(time, recovered.get()));
        dataSeriesDeceased.getData().add(new XYChart.Data<>(time, deceased.get()));
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
     * Getter for {@link #sick}.
     *
     * @return {@link #sick}
     */
    public int getSick() {
        return sick.get();
    }


    /**
     * Getter for {@link #sick} {@link IntegerProperty}.
     *
     * @return {@link #sick}
     */
    public IntegerProperty sickProperty() {
        return sick;
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
     * Getter for {@link #dataSeriesHealthy}.
     *
     * @return {@link #dataSeriesHealthy}
     */
    public XYChart.Series<Double,Integer> getDataSeriesHealthy(){
        return dataSeriesHealthy;
    }

    /**
     * Getter for {@link #dataSeriesSick}.
     *
     * @return {@link #dataSeriesSick}
     */
    public XYChart.Series<Double,Integer> getDataSeriesSick(){
        return dataSeriesSick;
    }

    /**
     * Getter for {@link #dataSeriesRecovered}.
     *
     * @return {@link #dataSeriesRecovered}
     */
    public XYChart.Series<Double,Integer> getDataSeriesRecovered(){
        return dataSeriesRecovered;
    }

    /**
     * Getter for {@link #dataSeriesDeceased}.
     *
     * @return {@link #dataSeriesDeceased}
     */
    public XYChart.Series<Double,Integer> getDataSeriesDeceased(){
        return dataSeriesDeceased;
    }

}

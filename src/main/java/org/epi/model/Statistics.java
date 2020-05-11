package org.epi.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.Rectangle;
import org.epi.util.Error;

import java.util.Objects;

/** Get real-time statistics for a simulator.*/
public class Statistics {

    /** The back reference to the world for these statistics.*/
    private final World world;

    /** Initial population count.*/
    private final IntegerProperty initPop;

    /** The number of healthy people in the given simulation.*/
    private final IntegerProperty healthy;

    /** The number of infected humans in the given simulation.*/
    private final IntegerProperty infected;

    /** The number of recovered humans in the given simulation.*/
    private final IntegerProperty recovered;

    /** The number difference between the current population count and the initial population count.*/
    private final IntegerProperty deceased;

    /** Show the statistics in a label.*/
    private final ReadOnlyStringWrapper text = new ReadOnlyStringWrapper(this, "text",
            "healthy = 0, infected = 0, recovered = 0, deceased = 0.");

    private XYChart.Series<Double, Integer> dataSeriesDeceased;

    private XYChart.Series<Double, Integer> dataSeriesInfected;

    private XYChart.Series<Double, Integer> dataSeriesHealthy;

    private XYChart.Series<Double, Integer> dataSeriesRecovered;

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

        this.initPop = new SimpleIntegerProperty(healthy.get() + infected.get() + recovered.get());

        this.dataSeriesHealthy = new XYChart.Series<>();
        this.dataSeriesDeceased = new XYChart.Series<>();
        this.dataSeriesInfected = new XYChart.Series<>();
        this.dataSeriesRecovered = new XYChart.Series<>();

        //TODO move these values elsewhere
        dataSeriesRecovered.getData().add(new XYChart.Data<>(0.0,0));
        dataSeriesInfected.getData().add(new XYChart.Data<>(0.0,1));
        dataSeriesDeceased.getData().add(new XYChart.Data<>(0.0,0));
        dataSeriesHealthy.getData().add(new XYChart.Data<>(0.0,getInitPop()));
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

    /**
     * Lists of the statistics. Currently intended for diagnostics of simulator.
     *
     * @return Populations count's for the simulator
     */
    @Override
    public String toString() {
        return "healthy = " + healthy.get() +
                ", infected = " + infected.get() +
                ", recovered = " + recovered.get() +
                ", deceased = " + deceased.get() +
                ".";
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

        deceased.set(initPop.get() - healthy.get() - infected.get() - recovered.get());

        text.set(toString());

        double time = world.getTotalElapsedSeconds();

        dataSeriesRecovered.getData().add(new XYChart.Data<>(time, getRecovered()));
        dataSeriesInfected.getData().add(new XYChart.Data<>(time, getInfected()));
        dataSeriesHealthy.getData().add(new XYChart.Data<>(time, getHealthy()));
        dataSeriesDeceased.getData().add(new XYChart.Data<>(time, getDeceased()));
    }

    //---------------------------- Getters and Setters ----------------------------

    /**
     * Getter for {@link #healthy}
     *
     * @return {@link #healthy}
     */
    public int getHealthy() {
        return healthy.get();
    }

    /**
     * Getter for {@link #healthy} {@link IntegerProperty}
     *
     * @return {@link #healthy}
     */
    public IntegerProperty healthyProperty() {
        return healthy;
    }

    /**
     * Getter for {@link #infected}
     *
     * @return {@link #infected}
     */
    public int getInfected() {
        return infected.get();
    }


    /**
     * Getter for {@link #infected} {@link IntegerProperty}
     *
     * @return {@link #infected}
     */
    public IntegerProperty infectedProperty() {
        return infected;
    }

    /**
     * Getter for {@link #recovered}
     *
     * @return {@link #recovered}
     */
    public int getRecovered() {
        return recovered.get();
    }

    /**
     * Getter for {@link #recovered} {@link IntegerProperty}
     *
     * @return {@link #recovered}
     */
    public IntegerProperty recoveredProperty() {
        return recovered;
    }

    /**
     * Getter for {@link #recovered}
     *
     * @return {@link #recovered}
     */
    public int getDeceased() {
        return deceased.get();
    }

    /**
     * Getter for {@link #deceased} {@link IntegerProperty}
     *
     * @return {@link #deceased}
     */
    public IntegerProperty deceasedProperty() {
        return deceased;
    }

    /**
     * Getter for {@link #initPop}
     *
     * @return {@link #initPop}
     */
    public int getInitPop() {
        return initPop.get();
    }

    /**
     * Getter for {@link #initPop} {@link IntegerProperty}
     *
     * @return {@link #initPop}
     */
    public IntegerProperty initPopProperty() {
        return initPop;
    }

    /**
     * Getter for {@link #text} {@link ReadOnlyStringProperty}
     *
     * @return {@link #text}
     */
    public ReadOnlyStringProperty getTextProperty() {
        return text.getReadOnlyProperty();
    }

    public void setDataSeriesRecovered(XYChart.Series<Double, Integer> dataSeriesRecovered) {
        this.dataSeriesRecovered = dataSeriesRecovered;
    }

    public void setDataSeriesHealthy(XYChart.Series<Double, Integer> dataSeriesHealthy) {
        this.dataSeriesHealthy = dataSeriesHealthy;
    }

    public void setDataSeriesInfected(XYChart.Series<Double, Integer> dataSeriesInfected) {
        this.dataSeriesInfected = dataSeriesInfected;
    }

    public void setDataSeriesDeceased(XYChart.Series<Double, Integer> dataSeriesDeceased) {
        this.dataSeriesDeceased = dataSeriesDeceased;
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

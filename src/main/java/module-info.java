module EpiSim {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.epi to javafx.fxml;
    exports org.epi;
}
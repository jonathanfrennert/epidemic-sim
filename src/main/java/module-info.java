module EpiSim {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens org.epi to javafx.fxml;
    exports org.epi;
}
module Epi {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.math3;

    opens org.epi.view to javafx.fxml;
    exports org.epi;
}
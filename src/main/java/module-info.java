module Epi {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.math3;
    requires com.jfoenix;
    
    opens org.epi.view to javafx.fxml, com.jfoenix;
    exports org.epi;
}
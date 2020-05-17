module Epi {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.math3;
    requires com.jfoenix;
    
    opens org.epi.view to javafx.fxml, com.jfoenix;

    exports org.epi.model.human;
    exports org.epi.model.world;
    exports org.epi.model;
    exports org.epi.view;
    exports org.epi;
}
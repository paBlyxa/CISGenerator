module com.we.cisgenerator {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.prefs;
    requires com.healthmarketscience.jackcess;


    opens com.we.cisgenerator to javafx.fxml;
    opens com.we.cisgenerator.view.controller to javafx.fxml;
    exports com.we.cisgenerator;
    exports com.we.cisgenerator.view.controller;
}
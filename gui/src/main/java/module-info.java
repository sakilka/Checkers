module hellofx {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.sample.checkers to javafx.fxml;
    opens org.sample.checkers.controller to javafx.fxml;
    exports org.sample.checkers;
    exports org.sample.checkers.controller;
}
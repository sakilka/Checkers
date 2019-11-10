module hellofx {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.sample.checkers to javafx.fxml;
    exports org.sample.checkers;
}
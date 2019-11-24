module hellofx {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.sample.checkers to javafx.fxml;
    opens org.sample.checkers.controller to javafx.fxml;
    opens org.sample.checkers.earth to javafx.fxml;
    opens org.sample.checkers.basic to javafx.fxml;
    opens org.sample.checkers.mesh to javafx.fxml;
    opens org.sample.checkers.mesh.components to javafx.fxml;
    exports org.sample.checkers;
    exports org.sample.checkers.controller;
    exports org.sample.checkers.earth;
    exports org.sample.checkers.basic;
    exports org.sample.checkers.mesh;
    exports org.sample.checkers.mesh.components;
}
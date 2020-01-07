module hellofx {
    requires javafx.controls;
    requires javafx.fxml;
//    requires jimObjModelImporterJFX;

    opens org.sample.checkers to javafx.fxml;
    opens org.sample.checkers.menu.controller to javafx.fxml;
    opens org.sample.checkers.earth to javafx.fxml;
    opens org.sample.checkers.basic to javafx.fxml;
    opens org.sample.checkers.mesh to javafx.fxml;
    opens org.sample.checkers.mesh.components to javafx.fxml;
    opens org.sample.checkers.board to javafx.fxml;
    opens org.sample.checkers.board.model to javafx.fxml;
    exports org.sample.checkers;
    exports org.sample.checkers.menu.controller;
    exports org.sample.checkers.earth;
    exports org.sample.checkers.basic;
    exports org.sample.checkers.mesh;
    exports org.sample.checkers.mesh.components;
    exports org.sample.checkers.board;
    exports org.sample.checkers.board.model;
}
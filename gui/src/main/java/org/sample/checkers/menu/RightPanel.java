package org.sample.checkers.menu;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class RightPanel extends FlowPane {

    public RightPanel() {
        setPadding(new Insets(5, 0, 5, 0));
        setVgap(4);
        setHgap(4);
        setPrefWrapLength(200);
        setMinWidth(0);
        getChildren().add(new Label("test"));
        setStyle("-fx-background-color: DAE6F3;");
    }
}

package org.sample.checkers.menu;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class RightPanel extends BorderPane {

    private double buttonWidth = 0;

    public RightPanel(DoubleProperty splitPaneDividerPosition, ReadOnlyDoubleProperty heightProperty,
                      ReadOnlyDoubleProperty sceneWidth, BooleanProperty shown) {
        setMinWidth(0);

        ToggleButton toggleButton = new ToggleButton();
        toggleButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("left-arrow.png"),
                10, 10, false, false)));
        HBox buttonBox = new HBox(0, toggleButton);

        toggleButton.prefHeightProperty().bind(heightProperty);
        shown.bindBidirectional(toggleButton.selectedProperty());

        BorderPane panelPane = new BorderPane();
        Button testButton = new Button("test");
        testButton.setMinWidth(0);
        panelPane.setCenter(testButton);
        panelPane.setMinWidth(0);
        panelPane.setStyle("-fx-background-color: red;");

        toggleButton.setOnAction(event -> {
            KeyValue end;
            buttonWidth = toggleButton.getWidth();
            if (toggleButton.isSelected()) {
                setCenter(panelPane);
                toggleButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("right-arrow.png"),
                        10, 10, false, false)));
                end = new KeyValue(splitPaneDividerPosition, 1 - (200 / sceneWidth.doubleValue()));
            } else {
                getChildren().remove(panelPane);
                toggleButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("left-arrow.png"),
                        10, 10, false, false)));
                end = new KeyValue(splitPaneDividerPosition, 1 - ((toggleButton.getWidth()) / sceneWidth.doubleValue()));
            }
            new Timeline(new KeyFrame(Duration.seconds(0.5), end)).play();
        });

        toggleButton.setSelected(true);
        setLeft(buttonBox);
        setCenter(panelPane);
        setStyle("-fx-background-color: DAE6F3;");
    }

    public double getButtonWidth() {
        return buttonWidth;
    }
}

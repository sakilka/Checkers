package org.sample.checkers.menu;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import org.sample.checkers.board.model.BoardPosition;

public class PositionPanel extends GridPane {

    public PositionPanel(BoardPosition boardPosition) {
        Label angleX = new Label("angleX : " + boardPosition.getAngleX());
        angleX.setMinWidth(0);
        boardPosition.angleXProperty().addListener((obs, oldVal, newVal) -> {
            angleX.setText("angleX : " + boardPosition.getAngleX());
        });

        Label angleY = new Label("angleY : " + boardPosition.getAngleY());
        angleY.setMinWidth(0);
        boardPosition.angleYProperty().addListener((obs, oldVal, newVal) -> {
            angleY.setText("angleY : " + boardPosition.getAngleY());
        });

        Label deltaX = new Label("deltaX : " + boardPosition.getDeltaX());
        deltaX.setMinWidth(0);
        boardPosition.deltaXProperty().addListener((obs, oldVal, newVal) -> {
            deltaX.setText("deltaX : " + boardPosition.getDeltaX());
        });

        Label deltaY = new Label("deltaY : " + boardPosition.getDeltaY());
        deltaY.setMinWidth(0);
        boardPosition.deltaYProperty().addListener((obs, oldVal, newVal) -> {
            deltaY.setText("deltaY : " + boardPosition.getDeltaY());
        });

        Slider sliderZ = new Slider();
        sliderZ.setMin(-100);
        sliderZ.setMax(100);
        sliderZ.setValue(0);
        sliderZ.setShowTickLabels(true);
        sliderZ.setShowTickMarks(true);
        sliderZ.setMajorTickUnit(50);
        sliderZ.setMinorTickCount(5);
        sliderZ.setBlockIncrement(10);
        sliderZ.valueProperty().bindBidirectional(boardPosition.translateZProperty());
        Label positionZ = new Label("positionZ : " + boardPosition.getTranslateZ());
        positionZ.setMinWidth(0);
        boardPosition.translateZProperty().addListener((obs, oldVal, newVal) -> {
            positionZ.setText("positionZ : " + boardPosition.getTranslateZ());
        });

        add(angleX, 0, 0);
        add(angleY, 0, 1);
        add(positionZ, 1, 2);
        add(sliderZ, 0, 2);
        add(deltaX, 0, 3);
        add(deltaY, 0, 4);
    }
}

package org.sample.checkers.chess;

import javafx.beans.property.SimpleDoubleProperty;

import static org.sample.checkers.config.game.GamePropertyUtil.getBoardConfig;

public class BoardPosition {

    private SimpleDoubleProperty angleX;
    private SimpleDoubleProperty angleY;
    private SimpleDoubleProperty deltaX;
    private SimpleDoubleProperty deltaZ;
    private SimpleDoubleProperty translateZ;

    public BoardPosition() {
        this.angleX = new SimpleDoubleProperty(getBoardConfig().getAngleX());
        this.angleY = new SimpleDoubleProperty(getBoardConfig().getAngleY());
        this.deltaX = new SimpleDoubleProperty(getBoardConfig().getDeltaX());
        this.deltaZ = new SimpleDoubleProperty(getBoardConfig().getDeltaZ());
        this.translateZ = new SimpleDoubleProperty(getBoardConfig().getTranslateZ());
    }

    public double getAngleX() {
        return angleX.get();
    }

    public SimpleDoubleProperty angleXProperty() {
        return angleX;
    }

    public double getAngleY() {
        return angleY.get();
    }

    public SimpleDoubleProperty angleYProperty() {
        return angleY;
    }

    public double getDeltaX() {
        return deltaX.get();
    }

    public SimpleDoubleProperty deltaXProperty() {
        return deltaX;
    }

    public double getDeltaZ() {
        return deltaZ.get();
    }

    public SimpleDoubleProperty deltaZProperty() {
        return deltaZ;
    }

    public double getTranslateZ() {
        return translateZ.get();
    }

    public SimpleDoubleProperty translateZProperty() {
        return translateZ;
    }
}

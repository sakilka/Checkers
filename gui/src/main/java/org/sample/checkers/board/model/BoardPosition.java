package org.sample.checkers.board.model;

import javafx.beans.property.SimpleDoubleProperty;

public class BoardPosition {

    private SimpleDoubleProperty angleX;
    private SimpleDoubleProperty angleY;
    private SimpleDoubleProperty deltaX;
    private SimpleDoubleProperty deltaY;
    private SimpleDoubleProperty translateZ;

    public BoardPosition() {
        this.angleX = new SimpleDoubleProperty();
        this.angleY = new SimpleDoubleProperty();
        this.deltaX = new SimpleDoubleProperty();
        this.deltaY = new SimpleDoubleProperty();
        this.translateZ = new SimpleDoubleProperty();
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

    public double getDeltaY() {
        return deltaY.get();
    }

    public SimpleDoubleProperty deltaYProperty() {
        return deltaY;
    }

    public double getTranslateZ() {
        return translateZ.get();
    }

    public SimpleDoubleProperty translateZProperty() {
        return translateZ;
    }
}

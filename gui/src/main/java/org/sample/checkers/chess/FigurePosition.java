package org.sample.checkers.chess;

public class FigurePosition {

    private double translateX;
    private double translateY;
    private double translateZ;

    public FigurePosition(double translateX, double translateY, double translateZ) {
        this.translateX = translateX;
        this.translateY = translateY;
        this.translateZ = translateZ;
    }

    public double getTranslateX() {
        return translateX;
    }

    public void setTranslateX(double translateX) {
        this.translateX = translateX;
    }

    public double getTranslateY() {
        return translateY;
    }

    public void setTranslateY(double translateY) {
        this.translateY = translateY;
    }

    public double getTranslateZ() {
        return translateZ;
    }

    public void setTranslateZ(double translateZ) {
        this.translateZ = translateZ;
    }
}

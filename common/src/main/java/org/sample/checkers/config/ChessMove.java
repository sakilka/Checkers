package org.sample.checkers.config;


import com.sun.javafx.geom.Dimension2D;

public class ChessMove {

    public static final Dimension2D FIRST_MOVE = new Dimension2D(0,0);

    private Dimension2D position;

    private Dimension2D previousPosition;

    public ChessMove(Dimension2D position, Dimension2D previousPosition) {
        this.position = position;
        this.previousPosition = previousPosition;
    }

    public Dimension2D getPosition() {
        return position;
    }

    public void setPosition(Dimension2D position) {
        this.position = position;
    }

    public Dimension2D getPreviousPosition() {
        return previousPosition;
    }

    public void setPreviousPosition(Dimension2D previousPosition) {
        this.previousPosition = previousPosition;
    }
}

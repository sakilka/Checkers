package org.sample.checkers.config.chess;


import com.sun.javafx.geom.Dimension2D;

public class ChessMovePosition {

    private Dimension2D position;

    private Dimension2D previousPosition;

    private boolean firstMove;

    public ChessMovePosition(Dimension2D position, Dimension2D previousPosition) {
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

    public boolean isFirstMove() {
        return firstMove;
    }

    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }
}

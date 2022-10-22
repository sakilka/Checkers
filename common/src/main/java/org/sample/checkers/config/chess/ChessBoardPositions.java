package org.sample.checkers.config.chess;

public class ChessBoardPositions {

    private ChessFigure[][] positions;
    private ChessSide[][] sides;

    public ChessBoardPositions(ChessFigure[][] positions, ChessSide[][] sides) {
        this.positions = positions;
        this.sides = sides;
    }

    public ChessFigure[][] getPositions() {
        return positions;
    }

    public void setPositions(ChessFigure[][] positions) {
        this.positions = positions;
    }

    public ChessSide[][] getSides() {
        return sides;
    }

    public void setSides(ChessSide[][] sides) {
        this.sides = sides;
    }
}

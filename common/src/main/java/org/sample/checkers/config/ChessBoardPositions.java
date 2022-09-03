package org.sample.checkers.config;

public class ChessBoardPositions {

    private ChessFigure[][] positions;

    public ChessBoardPositions(ChessFigure[][] positions) {
        this.positions = positions;
    }

    public ChessFigure[][] getPositions() {
        return positions;
    }

    public void setPositions(ChessFigure[][] positions) {
        this.positions = positions;
    }
}

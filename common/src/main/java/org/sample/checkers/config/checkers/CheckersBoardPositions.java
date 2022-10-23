package org.sample.checkers.config.checkers;

public class CheckersBoardPositions {
    private CheckersFigure[][] positions;
    private CheckersSide[][] sides;

    public CheckersBoardPositions(CheckersFigure[][] positions, CheckersSide[][] sides) {
        this.positions = positions;
        this.sides = sides;
    }

    public CheckersFigure[][] getPositions() {
        return positions;
    }

    public void setPositions(CheckersFigure[][] positions) {
        this.positions = positions;
    }

    public CheckersSide[][] getSides() {
        return sides;
    }

    public void setSides(CheckersSide[][] sides) {
        this.sides = sides;
    }
}

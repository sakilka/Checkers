package org.sample.checkers.ticktacktoe.ui.heuristic.numeric;

import org.sample.checkers.config.ticktacktoe.ToeSide;

public class NumericPosition {

    private int turn;
    private int width;
    private int height;

    public NumericPosition(int width, int height, int turn) {
        this.turn = turn;
        this.width = width;
        this.height = height;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

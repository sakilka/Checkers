package org.sample.checkers.ticktacktoe.ui;

import org.sample.checkers.config.ticktacktoe.ToeSide;

public class Position {

    private ToeSide turn;
    private int width;
    private int height;

    public Position(int width, int height, ToeSide turn) {
        this.turn = turn;
        this.width = width;
        this.height = height;
    }

    public ToeSide getTurn() {
        return turn;
    }

    public void setTurn(ToeSide turn) {
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

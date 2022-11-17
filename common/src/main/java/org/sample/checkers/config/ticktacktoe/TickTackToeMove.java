package org.sample.checkers.config.ticktacktoe;

import com.sun.javafx.geom.Dimension2D;

public class TickTackToeMove {

    private Dimension2D position;
    private ToeSide side;

    public TickTackToeMove(Dimension2D position, ToeSide side) {
        this.position = position;
        this.side = side;
    }

    public Dimension2D getPosition() {
        return position;
    }

    public void setPosition(Dimension2D position) {
        this.position = position;
    }

    public ToeSide getSide() {
        return side;
    }

    public void setSide(ToeSide side) {
        this.side = side;
    }
}

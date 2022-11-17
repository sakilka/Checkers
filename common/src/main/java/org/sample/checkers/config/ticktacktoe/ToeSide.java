package org.sample.checkers.config.ticktacktoe;

public enum ToeSide {
    CROSS, CIRCLE;

    public ToeSide oposite(){
        return this == CROSS ? CIRCLE : CROSS;
    }
}

package org.sample.checkers.config.ticktacktoe;

public enum ToeSide {
    CROSS, CIRCLE;

    public ToeSide opposite(){
        return this == CROSS ? CIRCLE : CROSS;
    }
}

package org.sample.checkers.ticktacktoe.ui.minimax.techniques;

public class MinimaxMove {

    public int position;

    public MinimaxMove(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return String.valueOf(position);
    }
}
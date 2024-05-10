package org.sample.checkers.ticktacktoe.ui.minimax.techniques;

import org.sample.checkers.config.ticktacktoe.ToeSide;

public class Record {

    Long key;
    MinimaxMove move;
    Integer score;
    ToeSide player;

    public Record(Long key, MinimaxMove move, Integer score, ToeSide player) {
        this.key = key;
        this.move = move;
        this.score = score;
        this.player = player;
    }

    public Integer getScore() {
        return score;
    }
}

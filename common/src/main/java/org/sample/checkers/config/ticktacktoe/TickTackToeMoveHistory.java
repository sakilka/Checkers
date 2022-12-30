package org.sample.checkers.config.ticktacktoe;

import java.util.List;

import static org.sample.checkers.config.ticktacktoe.ToeSide.CIRCLE;
import static org.sample.checkers.config.ticktacktoe.ToeSide.CROSS;

public class TickTackToeMoveHistory {

    private List<TickTackToeMove> moves;
    private ToeSide onMove;
    private int width;
    private int height;

    public TickTackToeMoveHistory(List<TickTackToeMove> moves, int width, int height, ToeSide onMove) {
        this.moves = moves;
        this.width = width;
        this.height = height;
        this.onMove = onMove;
    }

    public void addMove(TickTackToeMove move) {
        moves.add(move);
        onMove = onMove == CIRCLE ? CROSS : CIRCLE;
    }

    public ToeSide[][] getCurrentBoardFromHistory() {
        ToeSide [][] currentBoard = new ToeSide[height][width];

        for (TickTackToeMove move: moves) {
            currentBoard[(int) move.getPosition().height][(int) move.getPosition().width] = move.getSide();
        }

        return currentBoard;
    }

    public ToeSide getOnMove() {
        return onMove;
    }
}

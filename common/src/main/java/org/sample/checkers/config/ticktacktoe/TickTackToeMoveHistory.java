package org.sample.checkers.config.ticktacktoe;

import org.sample.checkers.config.checkers.CheckersMovePosition;

import java.util.List;

import static org.sample.checkers.config.checkers.CheckersSide.BLACK;
import static org.sample.checkers.config.checkers.CheckersSide.WHITE;
import static org.sample.checkers.config.ticktacktoe.ToeSide.CIRCLE;
import static org.sample.checkers.config.ticktacktoe.ToeSide.CROSS;

public class TickTackToeMoveHistory {

    private List<TickTackToeMove> moves;
    private ToeSide onMove;
    private int width;
    private int height;

    public TickTackToeMoveHistory(List<TickTackToeMove> moves, int width, int height) {
        this.moves = moves;
        this.width = width;
        this.height = height;
        this.onMove = CIRCLE;
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

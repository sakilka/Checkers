package org.sample.checkers.ticktacktoe.ui.negamax;

import com.sun.javafx.geom.Dimension2D;
import org.sample.checkers.config.ticktacktoe.TickTackToeMove;
import org.sample.checkers.config.ticktacktoe.TickTackToeMoveHistory;
import org.sample.checkers.config.ticktacktoe.ToeSide;
import org.sample.checkers.ticktacktoe.ui.TickTackToeUi;
import org.sample.checkers.ticktacktoe.ui.heuristic.ToeHeuristic;
import org.sample.checkers.ticktacktoe.ui.heuristic.WinCombinationsCounter;
import org.springframework.stereotype.Service;

@Service
public class NegaMax implements TickTackToeUi {

    private static final int SEARCH_DEPTH = 3;
    private final ToeHeuristic toeHeuristic;
    private ToeSide side;

    public NegaMax() {
        this.toeHeuristic = new ToeHeuristic();
    }

    @Override
    public TickTackToeMove computeNextMove(TickTackToeMoveHistory history) {
        ToeSide[][] baseState = history.getCurrentBoardFromHistory();
        int boardWidth = baseState.length;
        int boardHeight = baseState[0].length;
        side = history.getOnMove();

        int bestMove = Integer.MIN_VALUE;
        Dimension2D move = null;

        for (int width = 0; width < boardWidth; width++) {
            for (int height = 0; height < boardHeight; height ++) {
                ToeSide [][] child = turn(baseState, width, height, history.getOnMove());

                if(child != null) {
                    if(canWin(child, history.getOnMove())) {
                        return new TickTackToeMove(new Dimension2D(height, width), history.getOnMove());
                    }
                    int evaluation = - negamax(child, SEARCH_DEPTH, false, history.getOnMove().oposite());
                    if (evaluation >= bestMove) {
                        move = new Dimension2D(height, width);
                        bestMove = evaluation;
                    }
                }
            }
        }

        return new TickTackToeMove(move, history.getOnMove());
    }

    private int negamax(ToeSide [][] state, int depth, boolean max, ToeSide side) {
        int boardWidth = state.length;
        int boardHeight = state[0].length;

        if(canWin(state, side)) {
            return Integer.MIN_VALUE;
        }

        if(depth == 0 || anyNextMove(state)) {
            return evaluateBoard(state, side);
        }

        int score = Integer.MIN_VALUE;

        for (int width = 0; width < boardWidth; width++) {
            for (int height = 0; height < boardHeight; height ++) {
                ToeSide [][] child = turn(state, width, height, side);

                if(child != null) {
                    score = Math.max(score, - negamax(child, depth-1, true, side.oposite()));
                }
            }
        }

        return score;
    }

    private boolean anyNextMove(ToeSide[][] state) {
        for (ToeSide[] toeSides : state) {
            for (ToeSide toeSide :toeSides) {
                if (toeSide == null) return false;
            }
        }

        return true;
    }

    private ToeSide [][] turn(ToeSide [][] state, int w, int h, ToeSide side){
        if(state[w][h] != null) {
            return null;
        }

        int boardWidth = state.length;
        int boardHeight = state[0].length;
        ToeSide [][] child = new ToeSide[state.length][state[0].length];

        for (int width = 0; width < boardWidth; width++) {
            for (int height = 0; height < boardHeight; height ++) {
                child[width][height] = state[width][height];
                if(width == w && height == h) {
                    child[width][height] = side;
                }
            }
        }

        return child;
    }

    private boolean canWin(ToeSide[][] currentBoard, ToeSide side) {
        return  WinCombinationsCounter.countWiningCombinations(currentBoard, 0, side) != 0;
    }

    private int evaluateBoard(ToeSide [][] state, ToeSide side){
        return toeHeuristic.evaluateBoardState(state, this.side == side ? side : side.oposite());
    }
}

package org.sample.checkers.ticktacktoe.ui.negamax;

import com.sun.javafx.geom.Dimension2D;
import org.sample.checkers.config.ticktacktoe.TickTackToeMove;
import org.sample.checkers.config.ticktacktoe.TickTackToeMoveHistory;
import org.sample.checkers.config.ticktacktoe.ToeSide;
import org.sample.checkers.ticktacktoe.ui.TickTackToeUi;
import org.sample.checkers.ticktacktoe.ui.heuristic.base.ToeHeuristic;
import org.sample.checkers.ticktacktoe.ui.heuristic.base.WinCombinationsCounter;
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

        if(canWin(baseState, history.getOnMove(), 1)) {
            Dimension2D winTurn = getWinningMove(baseState, history.getOnMove());
            return new TickTackToeMove(winTurn, history.getOnMove());
        }

        if(canWin(baseState, history.getOnMove().opposite(), 1)) {
            Dimension2D blockTurn = getWinningMove(baseState, history.getOnMove().opposite());
            return new TickTackToeMove(blockTurn, history.getOnMove());
        }

        int boardWidth = baseState.length;
        int boardHeight = baseState[0].length;
        side = history.getOnMove();

        int bestMove = Integer.MIN_VALUE;
        Dimension2D move = null;

        for (int width = 0; width < boardWidth; width++) {
            for (int height = 0; height < boardHeight; height ++) {
                ToeSide [][] child = turn(baseState, width, height, history.getOnMove());

                if(child != null) {
                    int evaluation = - negamax(child, SEARCH_DEPTH, history.getOnMove().opposite());
                    if (evaluation >= bestMove) {
                        move = new Dimension2D(height, width);
                        bestMove = evaluation;
                    }
                }
            }
        }

        return new TickTackToeMove(move, history.getOnMove());
    }

    private int negamax(ToeSide [][] state, int depth, ToeSide side) {
        int boardWidth = state.length;
        int boardHeight = state[0].length;

        if(canWin(state, side, 0)) {
            return Integer.MIN_VALUE;
        }

        if(canWin(state, side.opposite(), 0)) {
            return Integer.MAX_VALUE;
        }

        if(depth == 0 || anyNextMove(state)) {
            return evaluateBoard(state, side);
        }

        int score = Integer.MIN_VALUE;

        for (int width = 0; width < boardWidth; width++) {
            for (int height = 0; height < boardHeight; height ++) {
                ToeSide [][] child = turn(state, width, height, side);

                if(child != null) {
                    score = Math.max(score, - negamax(child, depth-1, side.opposite()));
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

    private boolean canWin(ToeSide[][] currentBoard, ToeSide side, int needed) {
        return WinCombinationsCounter.countWiningCombinations(currentBoard, needed, side) != 0;
    }

    private Dimension2D getWinningMove(ToeSide[][] state, ToeSide side) {
        int boardWidth = state.length;
        int boardHeight = state[0].length;

        for (int width = 0; width < boardWidth; width++) {
            for (int height = 0; height < boardHeight; height ++) {
                if(state[width][height] != null) {
                    continue;
                }

                state[width][height] = side;
                if(canWin(state, side, 0)) {
                    state[width][height] = null;
                    return new Dimension2D(height, width);
                }

                state[width][height] = null;
            }
        }

        throw new RuntimeException("No turn find for winner!");
    }

    private int evaluateBoard(ToeSide [][] state, ToeSide side){
        int value = toeHeuristic.evaluateBoardState(state, this.side == side ? side : side.opposite());
        return this.side == side ? value : - value;
    }
}

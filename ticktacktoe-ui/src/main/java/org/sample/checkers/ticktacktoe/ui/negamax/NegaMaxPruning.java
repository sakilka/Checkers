package org.sample.checkers.ticktacktoe.ui.negamax;

import com.sun.javafx.geom.Dimension2D;
import org.sample.checkers.config.ticktacktoe.TickTackToeMove;
import org.sample.checkers.config.ticktacktoe.TickTackToeMoveHistory;
import org.sample.checkers.config.ticktacktoe.ToeSide;
import org.sample.checkers.ticktacktoe.ui.TickTackToeUi;
import org.sample.checkers.ticktacktoe.ui.heuristic.ToeHeuristic;
import org.sample.checkers.ticktacktoe.ui.heuristic.WinCombinationsCounter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class NegaMaxPruning implements TickTackToeUi {

    private static final int SEARCH_DEPTH = 3;
    private final ToeHeuristic toeHeuristic;
    private ToeSide side;
    private static final int INFINITY = Integer.MAX_VALUE;

    public NegaMaxPruning() {
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
                if(baseState[width][height] != null) {
                    continue;
                }

                baseState[width][height] = history.getOnMove();

                int evaluation = - negamaxPruning(baseState, SEARCH_DEPTH, - INFINITY, INFINITY,
                        history.getOnMove().opposite());

                if (evaluation >= bestMove) {
                    move = new Dimension2D(height, width);
                    bestMove = evaluation;
                }

                baseState[width][height] = null;
            }
        }

        return new TickTackToeMove(move, history.getOnMove());
    }

    private int negamaxPruning(ToeSide [][] state, int depth, int alpha, int beta, ToeSide side) {
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

        for(Turn possibleTurn : generatePossibleTurns(state)) {
            state[possibleTurn.width][possibleTurn.height] = side;
            score = Math.max(score, - negamaxPruning(state, depth-1, - beta, - alpha, side.opposite()));
            state[possibleTurn.width][possibleTurn.height] = null;

            alpha = Math.max(alpha, score);

            if(score >= beta) {
                return score;
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

    private Collection<Turn> generatePossibleTurns(ToeSide [][] state) {
        Collection<Turn> possibleTurns = new ArrayList<>();
        int boardWidth = state.length;
        int boardHeight = state[0].length;

        for (int width = 0; width < boardWidth; width++) {
            for (int height = 0; height < boardHeight; height ++) {
                if (state[width][height] != null) {
                    continue;
                }

                possibleTurns.add(new Turn(width, height));
            }
        }

        return possibleTurns;
    }

    private static class Turn {
        private final int width;
        private final int height;

        public Turn(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }
}


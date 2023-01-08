package org.sample.checkers.ticktacktoe.ui.alphabetapruning;

import com.sun.javafx.geom.Dimension2D;
import org.sample.checkers.config.ticktacktoe.TickTackToeMove;
import org.sample.checkers.config.ticktacktoe.TickTackToeMoveHistory;
import org.sample.checkers.config.ticktacktoe.ToeSide;
import org.sample.checkers.ticktacktoe.ui.TickTackToeUi;
import org.sample.checkers.ticktacktoe.ui.heuristic.ToeHeuristic;
import org.sample.checkers.ticktacktoe.ui.heuristic.WinCombinationsCounter;
import org.springframework.stereotype.Service;

@Service
public class AlphaBetaPruning implements TickTackToeUi {

    private static final int SEARCH_DEPTH = 3;
    private final ToeHeuristic toeHeuristic;

    public AlphaBetaPruning() {
        this.toeHeuristic = new ToeHeuristic();
    }

    @Override
    public TickTackToeMove computeNextMove(TickTackToeMoveHistory history) {
        ToeSide [][] baseState = history.getCurrentBoardFromHistory();

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

        int bestMove = Integer.MIN_VALUE;
        Dimension2D move = null;

        for (int width = 0; width < boardWidth; width++) {
            for (int height = 0; height < boardHeight; height ++) {
                if (baseState[width][height] != null) {
                    continue;
                }

                baseState[width][height] = history.getOnMove();

                int evaluation = alphaBeta(baseState, SEARCH_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false,
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

    private int alphaBeta(ToeSide [][] state, int depth, int alpha, int beta, boolean max, ToeSide side) {
        int boardWidth = state.length;
        int boardHeight = state[0].length;

        if(depth == 0) {
            return toeHeuristic.evaluateBoardState(state, side);
        }

        if(canWin(state, side, 0)) {
            return Integer.MIN_VALUE;
        }

        if(canWin(state, side.opposite(), 0)) {
            return Integer.MAX_VALUE;
        }

        if(anyNextMove(state)) {
            return toeHeuristic.evaluateBoardState(state, side);
        }

        if(max) {
            int score = Integer.MIN_VALUE;

            for (int width = 0; width < boardWidth; width++) {
                for (int height = 0; height < boardHeight; height ++) {

                    if (state[width][height] != null) {
                        continue;
                    }

                    state[width][height] = side;
                    score = Math.max(score, alphaBeta(state, depth-1, alpha, beta, false, side.opposite()));
                    state[width][height] = null;

                    alpha = Math.max(alpha, score);
                    if(score >= beta) {
                        return  score;
                    }
                }
            }

            return score;
        } else {
            int score = Integer.MAX_VALUE;

            for (int width = 0; width < boardWidth; width++) {
                for (int height = 0; height < boardHeight; height ++) {
                    if (state[width][height] != null) {
                        continue;
                    }

                    state[width][height] = side;
                    score = Math.min(score, alphaBeta(state, depth-1, alpha, beta, true, side.opposite()));
                    state[width][height] = null;

                    beta = Math.min(beta, score);

                    if(score <= alpha) {
                        return score;
                    }
                }
            }

            return score;
        }
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
}

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

                if(canWin(baseState, history.getOnMove())) {
                    return new TickTackToeMove(new Dimension2D(height, width), history.getOnMove());
                }

                if(canWin(baseState, history.getOnMove().oposite())) {
                    return new TickTackToeMove(new Dimension2D(height, width), history.getOnMove());
                }

                int evaluation = alphaBeta(baseState, SEARCH_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false,
                        history.getOnMove().oposite());
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

        if(canWin(state, side)) {
            return Integer.MIN_VALUE;
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
                    score = Math.max(score, alphaBeta(state, depth-1, alpha, beta, false, side.oposite()));
                    state[width][height] = null;

                    alpha = Math.max(alpha, score);

                    if(beta <= alpha) {
                        break;
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
                    score = Math.min(score, alphaBeta(state, depth-1, alpha, beta, true, side.oposite()));
                    state[width][height] = null;

                    beta = Math.min(beta, score);

                    if(beta <= alpha) {
                        break;
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

    private boolean canWin(ToeSide[][] currentBoard, ToeSide side) {
        return  WinCombinationsCounter.countWiningCombinations(currentBoard, 0, side) != 0;
    }
}

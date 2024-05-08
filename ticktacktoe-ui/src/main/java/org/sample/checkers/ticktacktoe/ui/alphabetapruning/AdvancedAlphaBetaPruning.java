package org.sample.checkers.ticktacktoe.ui.alphabetapruning;

import com.sun.javafx.geom.Dimension2D;
import org.sample.checkers.config.ticktacktoe.TickTackToeMove;
import org.sample.checkers.config.ticktacktoe.TickTackToeMoveHistory;
import org.sample.checkers.config.ticktacktoe.ToeSide;
import org.sample.checkers.ticktacktoe.ui.TickTackToeUi;
import org.sample.checkers.ticktacktoe.ui.heuristic.numeric.NumericToeHeuristic;
import org.sample.checkers.ticktacktoe.ui.heuristic.numeric.NumericWinCombinationsCounter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

import static org.sample.checkers.ticktacktoe.ui.heuristic.numeric.NumericToeHeuristic.convertSide;
import static org.sample.checkers.ticktacktoe.ui.heuristic.numeric.NumericToeHeuristic.convertState;

@Service
public class AdvancedAlphaBetaPruning implements TickTackToeUi {

    private static final int SEARCH_DEPTH = 3;
    private final NumericToeHeuristic toeHeuristic;
    private int side;
    int ev;

    public AdvancedAlphaBetaPruning() {
        this.toeHeuristic = new NumericToeHeuristic();
    }

    @Override
    public TickTackToeMove computeNextMove(TickTackToeMoveHistory history) {
        long start = System.currentTimeMillis();
        int[][] baseState = convertState(history.getCurrentBoardFromHistory());

        if(canWin(baseState, convertSide(history.getOnMove()), 1)) {
            Dimension2D winTurn = getWinningMove(baseState, convertSide(history.getOnMove()));
            return new TickTackToeMove(winTurn, history.getOnMove());
        }

        if(canWin(baseState, convertSide(history.getOnMove().opposite()), 1)) {
            Dimension2D blockTurn = getWinningMove(baseState, convertSide(history.getOnMove().opposite()));
            return new TickTackToeMove(blockTurn, history.getOnMove());
        }

        int boardWidth = baseState.length;
        int boardHeight = baseState[0].length;
        side = convertSide(history.getOnMove());

        int bestMove = Integer.MIN_VALUE;
        Dimension2D move = null;

        for (int width = 0; width < boardWidth; width++) {
            for (int height = 0; height < boardHeight; height ++) {
                if (baseState[width][height] != 0) {
                    continue;
                }

                baseState[width][height] = convertSide(history.getOnMove());

                int evaluation = alphaBeta(baseState, SEARCH_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false,
                        convertSide(history.getOnMove().opposite()));

                if (evaluation >= bestMove) {
                    move = new Dimension2D(height, width);
                    bestMove = evaluation;
                }

                baseState[width][height] = 0;
            }
        }
        System.out.println("Duration: " + (System.currentTimeMillis() - start) / 1000.0 + " ev: " + ev);
        ev = 0;
        return new TickTackToeMove(move, history.getOnMove());
    }

    private int alphaBeta(int [][] state, int depth, int alpha, int beta, boolean max, int side) {

        if(depth == 0) {
            return evaluateBoard(state, sideOpposite(side));
        }

        if(canWin(state, side, 0)) {
            return Integer.MIN_VALUE;
        }

        if(canWin(state, sideOpposite(side), 0)) {
            return Integer.MAX_VALUE;
        }

        if(anyNextMove(state)) {
            return evaluateBoard(state, side);
        }

        if(max) {
            int score = Integer.MIN_VALUE;

            for(Turn possibleTurn : generatePossibleTurns(state, side, depth)) {

                state[possibleTurn.width][possibleTurn.height] = side;
                score = Math.max(score, alphaBeta(state, depth-1, alpha, beta, false, sideOpposite(side)));
                state[possibleTurn.width][possibleTurn.height]  = 0;

                alpha = Math.max(alpha, score);
                if(score >= beta) {
                    return  score;
                }
            }

            return score;
        } else {
            int score = Integer.MAX_VALUE;

            for(Turn possibleTurn : generatePossibleTurns(state, side, depth)) {

                state[possibleTurn.width][possibleTurn.height] = side;
                score = Math.min(score, alphaBeta(state, depth-1, alpha, beta, true, sideOpposite(side)));
                state[possibleTurn.width][possibleTurn.height] = 0;

                beta = Math.min(beta, score);

                if(score <= alpha) {
                    return score;
                }

            }

            return score;
        }
    }

    private Collection<Turn> generatePossibleTurns(int [][] state, int side, int depth) {
        Collection<EvaluatedTurn> possibleTurns = new ArrayList<>();
        int boardWidth = state.length;
        int boardHeight = state[0].length;

        for (int width = 0; width < boardWidth; width++) {
            for (int height = 0; height < boardHeight; height ++) {
                if (state[width][height] != 0) {
                    continue;
                }

                state[width][height] = side;
                int score = evaluateBoard(state, side);
                ev--;
                state[width][height] = 0;
                possibleTurns.add(new EvaluatedTurn(width, height, score));
            }
        }

        return possibleTurns
                .stream()
                .sorted(Comparator.comparing(EvaluatedTurn::getScore).reversed())
                .limit(SEARCH_DEPTH == depth ? 2
                        : SEARCH_DEPTH == depth - 1 ? 2 : 1)
                //.sorted(Comparator.comparing(EvaluatedTurn::getScore))
                .collect(Collectors.toList());
    }

    private boolean anyNextMove(int[][] state) {
        for (int[] toeSides : state) {
            for (int toeSide :toeSides) {
                if (toeSide == 0) return false;
            }
        }

        return true;
    }

    private boolean canWin(int[][] currentBoard, int side, int needed) {
        return NumericWinCombinationsCounter.countWiningCombinations(currentBoard, needed, side) != 0;
    }

    private Dimension2D getWinningMove(int[][] state, int side) {
        int boardWidth = state.length;
        int boardHeight = state[0].length;

        for (int width = 0; width < boardWidth; width++) {
            for (int height = 0; height < boardHeight; height ++) {
                if(state[width][height] != 0) {
                    continue;
                }

                state[width][height] = side;
                if(canWin(state, side, 0)) {
                    state[width][height] = 0;
                    return new Dimension2D(height, width);
                }

                state[width][height] = 0;
            }
        }

        throw new RuntimeException("No turn find for winner!");
    }

    private static class Turn {
        private final int width;
        private final int height;

        public Turn(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    private static class EvaluatedTurn extends Turn {
        private final int score;

        public EvaluatedTurn(int width, int height, int score) {
            super(width, height);
            this.score = score;
        }

        public int getScore() {
            return score;
        }
    }

    private static void print(ToeSide [][] state) {
        int boardHeight = state[0].length;
        System.out.println();
        for (ToeSide[] toeSides : state) {
            for (int height = 0; height < boardHeight; height++) {
                System.out.print(toeSides[height] == null ? " ? "
                        : toeSides[height] == ToeSide.CROSS ? " X " : " O ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private int evaluateBoard(int [][] state, int side){
        ev++;
        return toeHeuristic.evaluateBoardState(state, this.side == 1 ? 1 : 2);
    }

    private static int sideOpposite(int side) {
        switch (side) {
            case 1:
                return 2;
            case 2:
                return 1;
            default:
                return 0;
        }
    }
}


package org.sample.checkers.ticktacktoe.ui.heuristic.numeric;

import org.sample.checkers.config.ticktacktoe.ToeSide;

import static org.sample.checkers.config.ticktacktoe.ToeSide.CIRCLE;

/**
 * TOE SIDE
 *
 * CROSS - 1
 * CIRCLE - 2
 * EMPTY - 0
 */
public class NumericToeHeuristic {

    public final static int WIN_LENGTH = 5;
    private static final int[] winCombinationsCoefficient = {20, 7, 3, 1, 1};
    private static final int[] defeatCombinationsCoefficient = {30, 9, 5, 3, 1};

    public static int convertSide(ToeSide side) {
        return side == null ? 0 : side == CIRCLE ? 2 : 1;
    }

    public static int[][] convertState(ToeSide[][] currentBoard) {
        int[][] converted = new int[currentBoard.length][currentBoard[0].length];

        for (int width = 0; width < currentBoard.length; width++){
            for (int height = 0; height< currentBoard[0].length; height++) {
                converted[width][height] = convertSide(currentBoard[width][height]);
            }
        }

        return converted;
    }

    public static int evaluateBoardState(ToeSide[][] currentBoard, ToeSide side) {
        return evaluateBoardState(convertState(currentBoard), convertSide(side));
    }

    public static int evaluateBoardState(int[][] currentBoard, int side) {
        int score = 0;

        if(canWin(currentBoard,side)) {
            return Integer.MAX_VALUE;
        }

        for(int neededTurns = WIN_LENGTH; neededTurns >0; neededTurns--) {
            score += winCombinationsCoefficient[neededTurns - 1] * NumericWinCombinationsCounter
                    .countWiningCombinations(currentBoard, neededTurns, side);
            score -= defeatCombinationsCoefficient[neededTurns - 1] * NumericWinCombinationsCounter
                    .countWiningCombinations(currentBoard, neededTurns, side == 1 ? 2 : 1);
        }
        return score;
    }

    private static boolean canWin(int[][] currentBoard, int side) {
        return  NumericWinCombinationsCounter.countWiningCombinations(currentBoard, 0, side) != 0;
    }
}

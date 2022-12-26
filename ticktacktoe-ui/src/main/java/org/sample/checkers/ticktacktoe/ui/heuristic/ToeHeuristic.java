package org.sample.checkers.ticktacktoe.ui.heuristic;

import org.sample.checkers.config.ticktacktoe.ToeSide;

import static org.sample.checkers.config.ticktacktoe.ToeSide.CIRCLE;
import static org.sample.checkers.config.ticktacktoe.ToeSide.CROSS;

public class ToeHeuristic {

    public final static int WIN_LENGTH = 5;
    private int[] winCombinationsCoefficient = {20, 7, 3, 1, 1};
    private int[] defeatCombinationsCoefficient = {30, 9, 5, 3, 1};

    public int evaluateBoardState(ToeSide[][] currentBoard, ToeSide side) {
        int score = 0;

        if(canWin(currentBoard,side)) {
            return Integer.MAX_VALUE;
        }

        for(int neededTurns = WIN_LENGTH; neededTurns >0; neededTurns--) {
            score += winCombinationsCoefficient[neededTurns - 1] * WinCombinationsCounter.countWiningCombinations(currentBoard, neededTurns, side);
            score -= defeatCombinationsCoefficient[neededTurns - 1] * WinCombinationsCounter.countWiningCombinations(currentBoard, neededTurns, side == CROSS ? CIRCLE : CROSS);
        }
        return score;
    }

    private boolean canWin(ToeSide[][] currentBoard, ToeSide side) {
        return  WinCombinationsCounter.countWiningCombinations(currentBoard, 0, side) != 0;
    }
}

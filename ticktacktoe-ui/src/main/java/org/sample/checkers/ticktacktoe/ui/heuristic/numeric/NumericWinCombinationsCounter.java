package org.sample.checkers.ticktacktoe.ui.heuristic.numeric;

import static org.sample.checkers.ticktacktoe.ui.heuristic.base.ToeHeuristic.WIN_LENGTH;

public class NumericWinCombinationsCounter {

    public static int countWiningCombinations(int[][] currentBoard, int neededTurns, int side) {
        return countHorizontalWinning(currentBoard, neededTurns, side) +
                countVerticalWinning(currentBoard, neededTurns, side) +
                countLeftDiagonal(currentBoard, neededTurns, side) +
                countRightDiagonal(currentBoard, neededTurns, side);
    }

    private static int countHorizontalWinning(int[][] currentBoard, int neededTurns, int side) {
        int horizontalCount = 0;

        for (int width = 0; width <= (currentBoard[0].length - WIN_LENGTH); width++) {
            for (int height = 0; height < currentBoard.length; height++) {
                NumericPosition position = new NumericPosition(width, height, side);
                int stateCount = 0;
                int add = 0;
                while (add < WIN_LENGTH &&
                        (currentBoard[position.getHeight()][position.getWidth() + add] == side
                                || currentBoard[position.getHeight()][position.getWidth() + add] == 0)) {
                    if (currentBoard[position.getHeight()][position.getWidth() + add] == side) {
                        stateCount++;
                    }
                    add++;
                }

                if (add == WIN_LENGTH && (neededTurns >= (WIN_LENGTH - stateCount))) {
                    horizontalCount++;
                }
            }
        }
        return horizontalCount;
    }

    private static int countVerticalWinning(int[][] currentBoard, int neededTurns, int side) {
        int verticalCount = 0;

        for (int width = 0; width < currentBoard[0].length; width++) {
            for (int height = 0; height <= (currentBoard.length - WIN_LENGTH); height++) {
                NumericPosition position = new NumericPosition(width, height, side);
                int stateCount = 0;
                int add = 0;
                while (add < WIN_LENGTH &&
                        (currentBoard[position.getHeight() + add][position.getWidth()] == side
                                || currentBoard[position.getHeight() + add][position.getWidth()] == 0)) {
                    if (currentBoard[position.getHeight() + add][position.getWidth()] == side) {
                        stateCount++;
                    }
                    add++;
                }

                if (add == WIN_LENGTH && (neededTurns >= (WIN_LENGTH - stateCount))) {
                    verticalCount++;
                }
            }
        }
        return verticalCount;
    }

    private static int countLeftDiagonal(int[][] currentBoard, int neededTurns, int side) {
        int diagonalCount = 0;

        for (int width = 0; width <= (currentBoard[0].length - WIN_LENGTH); width++) {
            for (int height = 0; height <= (currentBoard.length - WIN_LENGTH); height++) {
                NumericPosition position = new NumericPosition(width, height, side);
                int stateCount = 0;
                int add = 0;
                while (add < WIN_LENGTH &&
                        (currentBoard[position.getHeight() + add][position.getWidth() + add] == side
                                || currentBoard[position.getHeight() + add][position.getWidth() + add] == 0)) {
                    if (currentBoard[position.getHeight() + add][position.getWidth() + add] == side) {
                        stateCount++;
                    }
                    add++;
                }

                if (add == WIN_LENGTH && (neededTurns >= (WIN_LENGTH - stateCount))) {
                    diagonalCount++;
                }
            }
        }
        return diagonalCount;
    }

    private static int countRightDiagonal(int[][] currentBoard, int neededTurns, int side) {
        int diagonalCount = 0;

        for (int width = WIN_LENGTH - 1; width < currentBoard[0].length; width++) {
            for (int height = 0; height <= (currentBoard.length - WIN_LENGTH); height++) {
                NumericPosition position = new NumericPosition(width, height, side);
                int stateCount = 0;
                int add = 0;
                while (add < WIN_LENGTH &&
                        (currentBoard[position.getHeight() + add][position.getWidth() - add] == side
                                || currentBoard[position.getHeight() + add][position.getWidth() - add] == 0)) {
                    if (currentBoard[position.getHeight() + add][position.getWidth() - add] == side) {
                        stateCount++;
                    }
                    add++;
                }

                if (add == WIN_LENGTH && (neededTurns >= (WIN_LENGTH - stateCount))) {
                    diagonalCount++;
                }
            }
        }
        return diagonalCount;
    }
}

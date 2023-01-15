package org.sample.checkers.ticktacktoe.ui.heuristic.base;

import org.sample.checkers.config.ticktacktoe.ToeSide;
import org.sample.checkers.ticktacktoe.ui.Position;

import static org.sample.checkers.ticktacktoe.ui.heuristic.base.ToeHeuristic.WIN_LENGTH;

public class WinCombinationsCounter {


    public static int countWiningCombinations(ToeSide[][] currentBoard, int neededTurns, ToeSide side) {
        return countHorizontalWinning(currentBoard, neededTurns, side) +
                countVerticalWinning(currentBoard, neededTurns, side) +
                countLeftDiagonal(currentBoard, neededTurns, side) +
                countRightDiagonal(currentBoard, neededTurns, side);
    }

    private static int countHorizontalWinning(ToeSide[][] currentBoard, int neededTurns, ToeSide side) {
        int horizontalCount = 0;

        for (int width = 0; width <= (currentBoard[0].length - WIN_LENGTH); width++) {
            for (int height = 0; height < currentBoard.length; height++) {
                Position position = new Position(width, height, side);
                int stateCount = 0;
                int add = 0;
                while (add < WIN_LENGTH &&
                        (currentBoard[position.getHeight()][position.getWidth() + add] == side
                                || currentBoard[position.getHeight()][position.getWidth() + add] == null)) {
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

    private static int countVerticalWinning(ToeSide[][] currentBoard, int neededTurns, ToeSide side) {
        int verticalCount = 0;

        for (int width = 0; width < currentBoard[0].length; width++) {
            for (int height = 0; height <= (currentBoard.length - WIN_LENGTH); height++) {
                Position position = new Position(width, height, side);
                int stateCount = 0;
                int add = 0;
                while (add < WIN_LENGTH &&
                        (currentBoard[position.getHeight() + add][position.getWidth()] == side
                                || currentBoard[position.getHeight() + add][position.getWidth()] == null)) {
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

    private static int countLeftDiagonal(ToeSide[][] currentBoard, int neededTurns, ToeSide side) {
        int diagonalCount = 0;

        for (int width = 0; width <= (currentBoard[0].length - WIN_LENGTH); width++) {
            for (int height = 0; height <= (currentBoard.length - WIN_LENGTH); height++) {
                Position position = new Position(width, height, side);
                int stateCount = 0;
                int add = 0;
                while (add < WIN_LENGTH &&
                        (currentBoard[position.getHeight() + add][position.getWidth() + add] == side
                                || currentBoard[position.getHeight() + add][position.getWidth() + add] == null)) {
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

    private static int countRightDiagonal(ToeSide[][] currentBoard, int neededTurns, ToeSide side) {
        int diagonalCount = 0;

        for (int width = WIN_LENGTH - 1; width < currentBoard[0].length; width++) {
            for (int height = 0; height <= (currentBoard.length - WIN_LENGTH); height++) {
                Position position = new Position(width, height, side);
                int stateCount = 0;
                int add = 0;
                while (add < WIN_LENGTH &&
                        (currentBoard[position.getHeight() + add][position.getWidth() - add] == side
                                || currentBoard[position.getHeight() + add][position.getWidth() - add] == null)) {
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

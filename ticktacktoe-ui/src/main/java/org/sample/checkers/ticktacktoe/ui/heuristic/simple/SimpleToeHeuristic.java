package org.sample.checkers.ticktacktoe.ui.heuristic.simple;

import static org.sample.checkers.ticktacktoe.ui.heuristic.base.ToeHeuristic.WIN_LENGTH;

/**
 * WIN = + INFINITY
 * LOSE = - INFINITY
 * OTHER COUNT
 */
public class SimpleToeHeuristic {

    private static final int INFINITY = Integer.MAX_VALUE;
    public final static int WIN_LENGTH = 5;

    public static int evaluateBoardState(int[][] currentBoard, int side) {
        int count = 0;

        //TODO

        for (int width = 0; width < currentBoard.length; width++) {
            for(int height = 0; height < currentBoard[0].length; height++) {
                if(currentBoard[width][height] == side) {
                    count ++;
                }
            }
        }

        return count;
    }

    public static int countWinning(int[][] currentBoard, int side) {
        int count = 0;
        int widthBorder = currentBoard.length;
        int heightBorder = currentBoard[0].length;

        for (int width = 0; width < (widthBorder); width++) {
            for (int height = 0; height < (currentBoard[0].length); height++) {
                if(currentBoard[width][height] != side) {
                    continue;
                }

                boolean horizontal = true;
                boolean vertical = true;
                boolean diagonal = true;
                boolean diagonal2 = true;

                for (int i= 1; i < WIN_LENGTH; i++) {

                    if(diagonal &&
                            ((((width + WIN_LENGTH) > widthBorder) || ((height + WIN_LENGTH) > heightBorder))
                                    || currentBoard[width + i][height + i] != side)) {
                        diagonal = false;
                    }
                    if(diagonal2 &&
                            ((((width - WIN_LENGTH) < 0) || ((height + i) >= heightBorder))
                                    || currentBoard[(width) - i][height + i] != side)) {
                        diagonal2 = false;
                    }
                    if(vertical &&
                            (((width + WIN_LENGTH) > widthBorder)
                             || currentBoard[width + i][height] != side)) {
                        vertical = false;
                    }
                    if(horizontal &&
                            (((height + WIN_LENGTH) > heightBorder)
                                    || currentBoard[width][height+ i] != side)) {
                        horizontal = false;
                    }

                    if(!diagonal && !diagonal2 && !vertical && !horizontal) {
                        break;
                    }

                    if(i == WIN_LENGTH - 1) {
                        if(vertical) {
                            count++;
                        }
                        if(diagonal) {
                            count++;
                        }
                        if(diagonal2) {
                            count++;
                        }
                        if(horizontal) {
                            count++;
                        }
                    }
                }
            }
        }

        return count;
    }
}

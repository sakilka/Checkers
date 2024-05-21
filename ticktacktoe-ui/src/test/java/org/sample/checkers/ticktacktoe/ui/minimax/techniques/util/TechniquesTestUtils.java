package org.sample.checkers.ticktacktoe.ui.minimax.techniques.util;

import org.sample.checkers.config.ticktacktoe.ToeSide;
import org.sample.checkers.ticktacktoe.ui.Position;
import org.sample.checkers.ticktacktoe.ui.heuristic.base.WinCombinationsCounter;
import org.sample.checkers.ticktacktoe.ui.minimax.techniques.BitboardsUtil;

import java.math.BigInteger;

import static org.sample.checkers.config.ticktacktoe.ToeSide.CIRCLE;
import static org.sample.checkers.config.ticktacktoe.ToeSide.CROSS;
import static org.sample.checkers.ticktacktoe.ui.heuristic.base.ToeHeuristic.WIN_LENGTH;

public abstract class TechniquesTestUtils extends BitboardsUtil {

    private int[] winCombinationsCoefficientTest = {20, 7, 3, 1, 1};
    private int[] defeatCombinationsCoefficientTest = {30, 9, 5, 3, 1};

    public TechniquesTestUtils() {
        super(10, 15, 5);
    }

    protected static ToeSide[][] loadBoard(String board, int width, int height){
        ToeSide [][] result = new ToeSide[width][height];

        int pointer = 0;
        String part;

        for (int h = 0; h < height; h++){
            for (int w = 0; w < width; w++){
                part = board.substring(pointer, pointer+2);
                pointer += 2;

                if("O".equals(part.substring(0, 1))) {
                    result[w][h] = ToeSide.CIRCLE;
                } else if ("X".equals(part.substring(0, 1))) {
                    result[w][h] = ToeSide.CROSS;
                } else {
                    result[w][h] = null;
                }
            }
        }

        return result;
    }

    protected static void printBoard(ToeSide[][] board){
        for (int h = 0; h < board[0].length; h++){
            for (int w = 0; w < board.length; w++){
                String side = board[w][h] == ToeSide.CROSS ? "X" : board[w][h] == ToeSide.CIRCLE ? "O" : ".";
                System.out.print(side + " ");
            }
            System.out.println();
        }
    }

    protected static void printBitBoard(long[] bitBoard) {
        System.out.println();
        System.out.print(" ... ");
        int space = 1;
        for (int pointer = bitBoard.length; pointer > 0; pointer--) {
            System.out.print(bitBoard[pointer-1]);
            if(space == 7) {
                System.out.print(" ");
                space=1;
            } else {
                space++;
            }
        }
        System.out.println();
    }

    protected static String getBitboardAsString(ToeSide [][] board, ToeSide side) {
        String bitboard = "";
        for (int col = 0; col < board.length; col++) {
            for (int h = board[0].length - 1; h >= 0; h--) {
                bitboard = (board[col][h] == side ? 1 : 0) + bitboard;
            }
            bitboard = "0" + bitboard;
        }

        return bitboard;
    }

    protected static long[] getBitboardAsArrayFor(ToeSide [][] board, ToeSide side) {
        int pointer = 0;
        long[] bitboard = new long[Math.multiplyExact(board.length, board[0].length) +  board.length];
        for (int col = 0; col < board.length; col++) {
            for (int h = board[0].length - 1; h >= 0; h--) {
                bitboard[pointer++] = board[col][h] == side ? 1 : 0;
            }
            pointer++;
        }
        return bitboard;
    }

    protected static long getBitboardLong(ToeSide [][] board, ToeSide side) {
        String bitboard = "";
        for (int col = 0; col < board.length; col++) {
            for (int h = board[0].length - 1; h >= 0; h--) {
                bitboard = (board[col][h] == side ? 1 : 0) + bitboard;
            }
            bitboard = "0" + bitboard;
        }

        return parseLong(bitboard, 2);
    }

    private static long parseLong(String s, int base) {
        return new BigInteger(s, base).longValue();
    }


    public static BigInteger getBitboardDecimal(ToeSide [][] board, ToeSide side) {
        String bitboard = "";
        for (int col = 0; col < board.length; col++) {
            for (int h = board[0].length - 1; h >= 0; h--) {
                bitboard = (board[col][h] == side ? 1 : 0) + bitboard;
            }
            bitboard = "0" + bitboard;
        }

        return parseBigDecimal(bitboard, 2);
    }

    private static BigInteger parseBigDecimal(String s, int base) {
        return new BigInteger(s, base);
    }

    //    public static boolean isWin(long bitboard) {
//        if (bitboard & (bitboard >> 6) & (bitboard >> 12) & (bitboard >> 18) != 0) return true; // diagonal \
//        if (bitboard & (bitboard >> 8) & (bitboard >> 16) & (bitboard >> 24) != 0) return true; // diagonal /
//        if (bitboard & (bitboard >> 7) & (bitboard >> 14) & (bitboard >> 21) != 0) return true; // horizontal
//        if (bitboard & (bitboard >> 1) & (bitboard >>  2) & (bitboard >>  3) != 0) return true; // vertical
//        return false;
//    }

    protected static boolean isWin(long bitboard) {
        int[] directions = {1, 7, 6, 8};
        long bb;
        for(int direction : directions) {
            bb = bitboard & (bitboard >> direction);
            if ((bb & (bb >> (2 * direction))) != 0) return true;
        }
        return false;
    }

    protected void printBitBoard(BigInteger[] bitBoard) {
        System.out.println();
        int nBit = 0;
        ToeSide [][] board = new ToeSide[column][height];

        for (int col = 0; col < column; col++) {
            for(int h = height; h >= 0; h--) {
                if(bitBoard[0].testBit(nBit)) board[col][h-1] = ToeSide.CROSS;
                if(bitBoard[1].testBit(nBit)) board[col][h-1] = ToeSide.CIRCLE;
                nBit++;
            }
        }

        printBoard(board);
        System.out.println();
    }

    protected ToeSide[][] getBoardFromBitBoard(BigInteger[] bitBoard) {
        System.out.println();
        int nBit = 0;
        ToeSide [][] board = new ToeSide[column][height];

        for (int col = 0; col < column; col++) {
            for(int h = height; h >= 0; h--) {
                if(bitBoard[0].testBit(nBit)) board[col][h-1] = ToeSide.CROSS;
                if(bitBoard[1].testBit(nBit)) board[col][h-1] = ToeSide.CIRCLE;
                nBit++;
            }
        }

        return board;
    }

    protected static int countWiningCombinations(ToeSide[][] currentBoard, int neededTurns, ToeSide side) {
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

    protected Integer countInRowWithoutBlock(int inRow, int side) {
        int block = side == 0 ? 1 : 0;

        int count = 0;
        int[] directions = {1, height + 1, height + 2, height};

        for (int direction : directions) {

            BigInteger mask1 = BigInteger.ONE.shiftLeft(256).subtract(BigInteger.ONE);
            BigInteger mask2 = BigInteger.ONE.shiftLeft(256).subtract(BigInteger.ONE);
            BigInteger mask3 = BigInteger.ONE.shiftLeft(256).subtract(BigInteger.ONE);
            BigInteger mask4 = BigInteger.ONE.shiftLeft(256).subtract(BigInteger.ONE);

            for (int col = 1; col <= (column + 1); col++){
                mask1 = mask1.clearBit(clearBit(col,height, 0, direction));
                mask1 = mask1.clearBit(clearBit(col,height, 1, direction));

                mask2 = mask2.clearBit(clearBit(col,height, 0, direction));
                mask2 = mask2.clearBit(clearBit(col,height, 1, direction));
                mask2 = mask2.clearBit(clearBit(col,height, 2, direction));

                mask3 = mask3.clearBit(clearBit(col,height, 0, direction));
                mask3 = mask3.clearBit(clearBit(col,height, 1, direction));
                mask3 = mask3.clearBit(clearBit(col,height, 2, direction));
                mask3 = mask3.clearBit(clearBit(col,height, 3, direction));

                mask4 = mask4.clearBit(clearBit(col,height, 0, direction));
                mask4 = mask4.clearBit(clearBit(col,height, 1, direction));
                mask4 = mask4.clearBit(clearBit(col,height, 2, direction));
                mask4 = mask4.clearBit(clearBit(col,height, 3, direction));
                mask4 = mask4.clearBit(clearBit(col,height, 4, direction));
            }

            System.out.println("mask: " + direction);
            printBitBoard(new BigInteger[]{mask4, BigInteger.ZERO});
            BigInteger possible5 = bitboard[side]
                    .or(mask1.and(bitboard[side].shiftRight(direction)));

            BigInteger blockBoard = bitboard[block]
                    .or(mask1.and(bitboard[block].shiftRight(direction)))
                    .or(mask2.and(bitboard[block].shiftRight(2 * direction)))
                    .or(mask3.and(bitboard[block].shiftRight(3 * direction)))
                    .or(mask4.and(bitboard[block].shiftRight(4 * direction)));

            System.out.println("direction: " + direction);
            printBitBoard(new BigInteger[]{BigInteger.ZERO, blockBoard});

            possible5 = possible5
                    .or(mask2.and(bitboard[side].shiftRight((2 * direction))))
                    .or(mask3.and(bitboard[side].shiftRight((3 * direction))))
                    .or(mask4.and(bitboard[side].shiftRight((4 * direction))));

            System.out.println("direction: " + direction);
            printBitBoard(new BigInteger[]{possible5, BigInteger.ZERO});

            BigInteger xor = possible5.xor(blockBoard);
            BigInteger result2 = xor.and(possible5);
            BigInteger result = xor.and(possible5).and(mask4.and(bitboard[side].shiftRight((4 * direction))));

            System.out.println("direction xor: " + direction);
            printBitBoard(new BigInteger[]{xor, BigInteger.ZERO});
            System.out.println("direction xor and: " + direction + " side: " + side);
            printBitBoard(new BigInteger[]{result2, BigInteger.ZERO});
            System.out.println("direction xor and: " + direction + " side: " + side);
            printBitBoard(new BigInteger[]{result, BigInteger.ZERO});
            System.out.println("maska o 4: " + direction);
            printBitBoard(new BigInteger[]{mask4, BigInteger.ZERO});
            System.out.println("posunute o 4: " + direction);
            printBitBoard(new BigInteger[]{mask4.and(bitboard[side].shiftRight((4 * direction))), BigInteger.ZERO});

            count += result.bitCount();
        }

        return count;
//        for (int direction : directions) {
//            BigInteger bb = bitboard[side].and(bitboard[side].shiftRight(direction));
//            BigInteger blockBoard = BigInteger.ZERO;
//
//            switch (inRow) {
//                case 1:
//                    blockBoard = bitboard[block];
//                    count += bitboard[side].xor(blockBoard).and(bitboard[side]).bitCount();
//                    break;
//                case 2:
//                    blockBoard = bitboard[block]
//                            .or(mask1.and(bitboard[block].shiftRight(direction)));
//                    count += bb.xor(blockBoard).and(bb).bitCount();
//                    break;
//                case 3:
//                    blockBoard = bitboard[block]
//                            .or(mask1.and(bitboard[block].shiftRight(direction)))
//                            .or(mask2.and(bitboard[block].shiftRight(2 * direction)));
//                    bb = bb.and((bitboard[side].shiftRight((2 * direction))));
//                    count += bb.xor(blockBoard).and(bb).bitCount();
//                    break;
//                case 4:
//                    blockBoard = bitboard[block]
//                            .or(mask1.and(bitboard[block].shiftRight(direction)))
//                            .or(mask2.and(bitboard[block].shiftRight(2 * direction)))
//                            .or(mask3.and(bitboard[block].shiftRight(3 * direction)));
//                    bb = bb.and(bb.shiftRight(2 * direction));
//                    count += bb.xor(blockBoard).and(bb).bitCount();
//                    break;
//                default:
//                case 5:
//                    blockBoard = bitboard[block]
//                            .or(mask1.and(bitboard[block].shiftRight(direction)))
//                            .or(mask2.and(bitboard[block].shiftRight(2 * direction)))
//                            .or(mask3.and(bitboard[block].shiftRight(3 * direction)))
//                            .or(mask4.and(bitboard[block].shiftRight(4 * direction)));
//                    bb = bb.and(bb.shiftRight(2 * direction))
//                            .and(bitboard[side].shiftRight((4 * direction)));
//                    count += bb.xor(blockBoard).and(bb).bitCount();
//                    break;
//            }
//        }
//
//        return inRow == 1 ? count/4 : count;
    }

    private static int clearBit(int col, int height, int diff, int direction){
        return ((col * (height + 1) - 1) - (diff * direction)) > 0 ?
                ((col * (height + 1) - 1) - (diff * direction)) :
                ((col * (height + 1) - 1));
    }

    public int evaluateBoardState(ToeSide[][] currentBoard, ToeSide side) {
        int score = 0;

        if(canWin(currentBoard,side)) {
            return Integer.MAX_VALUE;
        }

        for(int neededTurns = WIN_LENGTH; neededTurns >0; neededTurns--) {
            score += winCombinationsCoefficientTest[neededTurns - 1] * countWiningCombinations(currentBoard, neededTurns, side);
            score -= defeatCombinationsCoefficientTest[neededTurns - 1] * countWiningCombinations(currentBoard, neededTurns, side == CROSS ? CIRCLE : CROSS);
        }
        return score;
    }

    private boolean canWin(ToeSide[][] currentBoard, ToeSide side) {
        return  WinCombinationsCounter.countWiningCombinations(currentBoard, 0, side) != 0;
    }
}

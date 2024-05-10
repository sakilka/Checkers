package org.sample.checkers.ticktacktoe.ui.minimax.techniques.util;

import org.sample.checkers.config.ticktacktoe.ToeSide;
import org.sample.checkers.ticktacktoe.ui.minimax.techniques.BitboardsUtil;

import java.math.BigInteger;

public abstract class TechniquesTestUtils extends BitboardsUtil {

    public TechniquesTestUtils() {
        super(10, 15);
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
                if(bitBoard[0].testBit(nBit)) board[col][h] = ToeSide.CROSS;
                if(bitBoard[1].testBit(nBit)) board[col][h] = ToeSide.CIRCLE;
                nBit++;
            }
        }

        printBoard(board);
        System.out.println();
    }
}

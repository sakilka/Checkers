package org.sample.checkers.ticktacktoe.ui.minimax.techniques;

import org.sample.checkers.config.ticktacktoe.ToeSide;

import java.math.BigInteger;

public class BitboardsUtil {

    public static long getBitboard(ToeSide [][] board, ToeSide side) {
        String bitboard = "";
        for (int col = 0; col < board.length; col++) {
            for (int h = board[0].length - 1; h >= 0; h--) {
                bitboard = (board[col][h] == side ? 1 : 0) + bitboard;
            }
            bitboard = "0" + bitboard;
        }

       return parseLong(bitboard, 2);
    }

//    public static boolean isWin(long bitboard) {
//        if (bitboard & (bitboard >> 6) & (bitboard >> 12) & (bitboard >> 18) != 0) return true; // diagonal \
//        if (bitboard & (bitboard >> 8) & (bitboard >> 16) & (bitboard >> 24) != 0) return true; // diagonal /
//        if (bitboard & (bitboard >> 7) & (bitboard >> 14) & (bitboard >> 21) != 0) return true; // horizontal
//        if (bitboard & (bitboard >> 1) & (bitboard >>  2) & (bitboard >>  3) != 0) return true; // vertical
//        return false;
//    }

    public static boolean isWin(long bitboard) {
        int[] directions = {1, 7, 6, 8};
        long bb;
        for(int direction : directions) {
            bb = bitboard & (bitboard >> direction);
            if ((bb & (bb >> (2 * direction))) != 0) return true;
        }
        return false;
    }

    private static long parseLong(String s, int base) {
        return new BigInteger(s, base).longValue();
    }
}

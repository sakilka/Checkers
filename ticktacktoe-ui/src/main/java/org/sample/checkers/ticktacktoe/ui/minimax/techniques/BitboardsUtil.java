package org.sample.checkers.ticktacktoe.ui.minimax.techniques;

import org.sample.checkers.config.ticktacktoe.ToeSide;

import java.math.BigInteger;

public class BitboardsUtil {

    public static BigInteger getBitboard(ToeSide [][] board, ToeSide side) {
        BigInteger bitboard = BigInteger.ZERO;
        int nBit = 0;

        for (int col = 0; col < board.length; col++) {
            for (int h = board[0].length - 1; h >= 0; h--) {
                if(board[col][h] == side) {
                    bitboard = bitboard.setBit(nBit);
                }
                nBit++;
            }
            nBit++;
        }

        return bitboard;
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

    public static boolean isWin(BigInteger bitboard, int winSize) {
        if(winSize<1 || winSize>5) {
            throw new RuntimeException("Win size must be between 1 and 5!");
        }

        int[] directions = {1, 11, 12, 10};
        BigInteger mask = BigInteger.ONE.shiftLeft(256).subtract(BigInteger.ONE);

        if(winSize<4) {
            for (int direction : directions) {
                if (!bitboard
                        .and(bitboard.shiftRight((direction)))
                        .and(winSize < 3 ? mask : (bitboard.shiftRight((2 * direction))))
                        .and(winSize < 4 ? mask : (bitboard.shiftRight((3 * direction))))
                        .and(winSize < 5 ? mask : (bitboard.shiftRight((4 * direction)))).equals(BigInteger.ZERO)) {
                    return true;
                }
            }
        } else {
            BigInteger bb;
            for (int direction : directions) {
                bb = bitboard.and(bitboard.shiftRight(direction));
                if (!(bb.and(bb.shiftRight(2 * direction))
                        .and(winSize < 5 ? mask : bitboard.shiftRight((4 * direction)))).equals(BigInteger.ZERO))
                    return true;
            }
        }
        return false;
    }
}

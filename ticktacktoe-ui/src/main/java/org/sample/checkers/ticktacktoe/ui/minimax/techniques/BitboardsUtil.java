package org.sample.checkers.ticktacktoe.ui.minimax.techniques;

import org.sample.checkers.config.ticktacktoe.ToeSide;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that represents a connect-four game
 *
 * @param [bitboard] array of 2 bitboards
 * @param [counter] number of already played moves
 */
public abstract class BitboardsUtil {

    protected final int height;
    protected final int column;
    protected final int winSize;
    protected int counter;
    protected int[] moves;
    protected BigInteger bitboard[];

    private int[] winCombinationsCoefficient = {1, 1, 3, 7, 20};
    private int[] defeatCombinationsCoefficient = {1, 3, 5, 9, 30};

    public BitboardsUtil(int height, int column, int winSize) {
        this.height = height;
        this.column = column;
        this.winSize = winSize;
        this.counter = 0;
        this.moves = new int[1000000];
        this.bitboard = new BigInteger[2];
    }

    public BitboardsUtil(int height, int column, int winSize, BigInteger[] bitboard) {
        this.height = height;
        this.column = column;
        this.winSize = winSize;
        this.counter = 0;
        this.moves = new int[1000000];
        this.bitboard = bitboard;
    }

    protected BigInteger getBitboard(ToeSide [][] board, ToeSide side) {
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

    protected boolean isWin(BigInteger bitboard, int winSize, int height) {
        if(winSize<1 || winSize>5) {
            throw new RuntimeException("Win size must be between 1 and 5!");
        }

        int[] directions = {1, height + 1, height + 2, height};
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

    protected void makeMove(int position) {
        BigInteger move = BigInteger.ONE.shiftLeft(position);
        bitboard[counter & 1] = bitboard[counter & 1].xor(move);
        moves[counter++] = position;
//
//        long move = 1L << height[col]++; // (1)  vyberie z pamete vysku v danom stlpci a zvysi o jedna a nastavy bit
//                                         //      nastavy height +1
//        bitboard[counter & 1] ^= move;   // (2)  vyberie bitboard pre dany tah
//                                         //      nastavy zmenu do bitboard cez xor
//        moves[counter++] = col;          // (3)  ulozi tah do historie
    }

    protected void undoMove() {
        int position = moves[--counter];
        BigInteger move = BigInteger.ONE.shiftLeft(position);
        bitboard[counter & 1] = bitboard[counter & 1].xor(move);
        //    int col = moves[--counter];     // reverses (3)
        //    long move = 1L << --height[col]; // reverses (1)
        //    bitboard[counter & 1] ^= move;  // reverses (2)
    }

    public List<Integer> listMoves(boolean shuffle) {
        List<Integer> possibleMoves = listMoves();
        if(shuffle) {
            Collections.shuffle(possibleMoves);
        }
        return possibleMoves;
    }

    protected List<Integer> listMoves() {
        List<Integer> moves = new ArrayList<>();

        BigInteger TOP = BigInteger.ZERO;

        for (int col = 1; col <= (column + 1); col++){
            TOP = TOP.setBit((col * (height + 1) - 1));
        }

        BigInteger bb = bitboard[0].or(bitboard[1]).or(TOP);

        for(int nBit = 0; nBit < (column * (height + 1)); nBit++) {
            if (!bb.testBit(nBit)) moves.add(nBit);
        }

        return moves;
    }

    public boolean hasWinner(ToeSide player) {
        if(ToeSide.CROSS == player) {
            return isWin(bitboard[0], winSize, height);
        } else if(ToeSide.CIRCLE == player) {
            return isWin(bitboard[1], winSize, height);
        } else {
            return isWin(bitboard[0], winSize, height) || isWin(bitboard[1], winSize, height);
        }
    }

    public Integer evaluate() {
        if(isWin(bitboard[(counter +1) & 1], winSize, height)) {
            return - Integer.MAX_VALUE;
        }

        int score = 0;

        for(int inRow = 1; inRow < winSize; inRow++) {
            score += winCombinationsCoefficient[inRow - 1] * countInRow(inRow, 0);
            score -= defeatCombinationsCoefficient[inRow - 1] * countInRow(inRow, 1);
        }
        return score;
    }

    protected Integer countInRow(int inRow, int side) {

        int count = 0;
        int[] directions = {1, height + 1, height + 2, height};

        for (int direction : directions) {
            BigInteger bb = bitboard[side].and(bitboard[side].shiftRight(direction));

            switch (inRow) {
                case 1:
                    count += bitboard[side].bitCount();
                    break;
                case 2:
                    count += bb.bitCount();
                    break;
                case 3:
                    bb = bb.and((bitboard[side].shiftRight((2 * direction))));
                    count += bb.bitCount();
                    break;
                case 4:
                    bb = bb.and(bb.shiftRight(2 * direction));
                    count += bb.bitCount();
                    break;
                default:
                case 5:
                    bb = bb.and(bb.shiftRight(2 * direction))
                            .and(bitboard[side].shiftRight((4 * direction)));
                    count += bb.bitCount();
                    break;
            }
        }

        return inRow == 1 ? count/4 : count;
    }

    public int getHeight() {
        return height;
    }

    public int getColumn() {
        return column;
    }

    public int getWinSize() {
        return winSize;
    }
}

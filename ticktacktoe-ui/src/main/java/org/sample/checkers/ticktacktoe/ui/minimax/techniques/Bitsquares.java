package org.sample.checkers.ticktacktoe.ui.minimax.techniques;

import org.sample.checkers.config.ticktacktoe.ToeSide;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Bitsquares extends BitboardsUtil {

    private static final double REWARD_POWER = 1.75;
    private final BigInteger MASK_BOARD = BigInteger.ONE.shiftLeft(height * column).subtract(BigInteger.ONE);

    private final BigInteger GAME_OVERS[];

    public Bitsquares(int height, int column, int winSize) {
        super(height, column, winSize);
        this.GAME_OVERS = getGameOvers();
    }

    public Bitsquares(int height, int column, int winSize, BigInteger[] bitboard) {
        super(height, column, winSize, bitboard);
        this.GAME_OVERS = getGameOvers();
    }

    public double bitsquareHeuristic(BigInteger[] bitboard, ToeSide player) {
        return bitsquareHeuristic(bitboard, player, REWARD_POWER);
    }

    public double bitsquareHeuristic(BigInteger[] bitboard, ToeSide player, Double rewardPower) {
        BigInteger[][][] playableLines = getPlayableLinesByLength(bitboard);
        double [] scores = new double[2];

        for (int play = 0; play <=1; play++) {
            for (int n = 1; n <= (winSize); n++) {
                scores[play] += playableLines[play][n].length * (Math.pow(n,rewardPower));
            }
        }

        double score = player == ToeSide.CROSS ? (scores[0] - scores[1]) : (scores[1] - scores[0]);
        return  score;
    }

    public BigInteger[][][] getPlayableLinesByLength(BigInteger[] tokens) {
        BigInteger[] playerTokens = new BigInteger[]{tokens[1], tokens[0]};
        BigInteger[][][] outputs = new BigInteger[2][winSize + 1][];
        BigInteger[] bitboards = new BigInteger[]{playerTokens[0].or(playerTokens[1]), playerTokens[1]};
        BigInteger[][] playableLines = getPlayableLines(bitboards);
        BigInteger[][] playedBits = new BigInteger[][]{
                Stream.of(playableLines[0]).map(playable -> playable.and(playerTokens[0])).toArray(BigInteger[]::new),
                Stream.of(playableLines[1]).map(playable -> playable.and(playerTokens[1])).toArray(BigInteger[]::new)
        };

        BigInteger[] bitcountMask = getBitcountMask();

        for (int player = 0; player <=1; player++) {
            Boolean [] isGameover = andArray(playableLines[player], playedBits[player]);
            Boolean [] isSingleBit = Stream.of(playedBits[player])
                    .map(playedBit -> (log2(playedBit) % 1) == 0).toArray(Boolean[]::new);
            Boolean [] isMultiBit = andArray(
                    Stream.of(isGameover).map(Boolean.FALSE::equals).toArray(Boolean[]::new),
                    Stream.of(isSingleBit).map(Boolean.FALSE::equals).toArray(Boolean[]::new));

            long [] bitCounts = new long[playableLines[player].length];

            for (int n = 0; n < playableLines[player].length; n++) {
                int finalN = n;
                int finalPlayer = player;
                int finalPlayer1 = player;

                if(isMultiBit[n]) {
                    bitCounts[n] = Stream.of(Stream.of(bitcountMask)
                                    .map(mask -> mask.and(playableLines[finalPlayer][finalN])).toArray(BigInteger[]::new))
                            .map(num -> num.and(playerTokens[finalPlayer1]))
                            .filter(bitCount -> !bitCount.equals(BigInteger.ZERO)).count();
                } else {
                    bitCounts[n] = 0;
                }
            }

            outputs[player][1] = andArray(playableLines[player], isSingleBit);
            outputs[player][winSize] = andArray(playableLines[player], isGameover);
            for (int n = 2; n < winSize; n++){
                outputs[player][n]  = andArray(playableLines[player],  equalArray(bitCounts, n));
            }
        }

        return outputs;
    }

    public static double log2(BigInteger val) {
        // Get the minimum number of bits necessary to hold this value.
        int n = val.bitLength();

        // Calculate the double-precision fraction of this number; as if the
        // binary point was left of the most significant '1' bit.
        // (Get the most significant 53 bits and divide by 2^53)
        long mask = 1L << 52; // mantissa is 53 bits (including hidden bit)
        long mantissa = 0;
        int j = 0;
        for (int i = 1; i < 54; i++)
        {
            j = n - i;
            if (j < 0) break;

            if (val.testBit(j)) mantissa |= mask;
            mask >>>= 1;
        }
        // Round up if next bit is 1.
        if (j > 0 && val.testBit(j - 1)) mantissa++;

        double f = mantissa / (double)(1L << 52);

        // Add the logarithm to the number of bits, and subtract 1 because the
        // number of bits is always higher than necessary for a number
        // (ie. log2(val)<n for every val).
        return (n - 1 + Math.log(f) * 1.44269504088896340735992468100189213742664595415298D);
        // Magic number converts from base e to base 2 before adding. For other
        // bases, correct the result, NOT this number!
    }

    private BigInteger[] getBitcountMask(){
        BigInteger[] bitcountMask = new BigInteger[column * height];
        for (int i= 0; i < bitcountMask.length; i++) {
            bitcountMask[i] = BigInteger.ONE.shiftLeft(i);
        }

        return bitcountMask;
    }

    private BigInteger[][] getPlayableLines(BigInteger[] bitboard) {
        BigInteger playedSquares = bitboard[0];
        BigInteger emptySquares = MASK_BOARD.andNot(bitboard[0]);
        BigInteger p1Tokens = bitboard[0].andNot(bitboard[1]);
        BigInteger p2Tokens = bitboard[0].and(bitboard[1]);

        BigInteger[] p1PlayableLines = Stream.of(GAME_OVERS)
                .map(gameOver -> gameOver.and(p1Tokens.andNot(p2Tokens).or(emptySquares))).toArray(BigInteger[]::new);
        BigInteger[] p2PlayableLines = Stream.of(GAME_OVERS)
                .map(gameOver -> gameOver.and(p2Tokens.andNot(p1Tokens).or(emptySquares))).toArray(BigInteger[]::new);

        Boolean[] p1IsValid = andArray(andArray(GAME_OVERS, p1PlayableLines) , (notEqualArray(Stream.of(p1PlayableLines).map(p1Playable -> p1Playable.and(playedSquares)).toArray(BigInteger[]::new), BigInteger.ZERO)));
        Boolean[] p2IsValid = andArray(andArray(GAME_OVERS, p2PlayableLines) , (notEqualArray(Stream.of(p2PlayableLines).map(p2Playable -> p2Playable.and(playedSquares)).toArray(BigInteger[]::new), BigInteger.ZERO)));

        p1PlayableLines = andArray(p1PlayableLines, p1IsValid);
        p2PlayableLines = andArray(p2PlayableLines, p2IsValid);

        return new BigInteger[][] {p1PlayableLines, p2PlayableLines};
    }

    public static Boolean[] andArray (BigInteger[] a, BigInteger[] b) {
        if (a.length != b.length) {
            return null;
        }

        Boolean [] result = new Boolean[b.length];

        for (int i = 0; i < a.length; i++) {
            result[i] = (a[i].equals(b[i]));
        }

        return result;
    }

    public static Boolean[] andArray (Boolean[] a, Boolean[] b) {
        if (a.length != b.length) {
            return null;
        }

        Boolean [] result = new Boolean[b.length];

        for (int i = 0; i < a.length; i++) {
            result[i] = ((Boolean.TRUE == a[i]) && (Boolean.TRUE  == b[i]));
        }

        return result;
    }

    public static BigInteger[] andArray (BigInteger[] a, Boolean[] b) {
        if (a.length != b.length) {
            return null;
        }

        List<BigInteger> result = new ArrayList<>();

        for (int i = 0; i < a.length; i++) {
            if(b[i]) result.add(a[i]);
        }

        return result.toArray(new BigInteger[0]);
    }

    public static Boolean[] equalArray (BigInteger[] a, BigInteger value) {
        Boolean [] result = new Boolean[a.length];

        for (int i = 0; i < a.length; i++) {
            result[i] = (a[i].equals(value));
        }

        return result;
    }

    public static Boolean[] equalArray (long[] a, int value) {
        Boolean [] result = new Boolean[a.length];

        for (int i = 0; i < a.length; i++) {
            result[i] = (a[i] == value);
        }

        return result;
    }

    public static Boolean[] notEqualArray (BigInteger[] a, BigInteger value) {
        Boolean [] result = new Boolean[a.length];

        for (int i = 0; i < a.length; i++) {
            result[i] = (!a[i].equals(value));
        }

        return result;
    }

    private BigInteger [] getGameOvers() {
        List<BigInteger> gameOvers = new ArrayList<>();

        BigInteger mask_horizontal  = BigInteger.ZERO;
        BigInteger mask_vertical  = BigInteger.ZERO;
        BigInteger mask_diagonal_dl  = BigInteger.ZERO;
        BigInteger mask_diagonal_ul  = BigInteger.ZERO;

        for(int n = 0; n < winSize; n++) {
            mask_horizontal = mask_horizontal.or(BigInteger.ONE.shiftLeft(n));
            mask_vertical = mask_vertical.or(BigInteger.ONE.shiftLeft(n * column));
            mask_diagonal_dl = mask_diagonal_dl.or(BigInteger.ONE.shiftLeft((n * column) + n));
            mask_diagonal_ul = mask_diagonal_ul.or(BigInteger.ONE.shiftLeft((n * column) + (winSize - 1 - n)));
        }

        int row_inner = height - winSize;
        int col_inner = column - winSize;

        for (int row = 0; row < height; row++) {
            for(int col = 0; col < column; col++) {
                int offset = col + row * column;
                if(col <= col_inner) {
                    gameOvers.add(mask_horizontal.shiftLeft(offset));
                }
                if(row <= row_inner) {
                    gameOvers.add(mask_vertical.shiftLeft(offset));
                }
                if(col <= col_inner && row <= row_inner) {
                    gameOvers.add(mask_diagonal_dl.shiftLeft(offset));
                    gameOvers.add(mask_diagonal_ul.shiftLeft(offset));
                }
            }
        }

        return gameOvers.toArray(new BigInteger[0]);
    }
}

package org.sample.checkers.ticktacktoe.ui.minimax.techniques;

import org.junit.jupiter.api.Test;
import org.sample.checkers.config.ticktacktoe.ToeSide;
import org.sample.checkers.ticktacktoe.ui.minimax.techniques.util.TechniquesTestUtils;

import java.math.BigInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BitsquaresTest extends TechniquesTestUtils {

    private static final String CONNECT_FOUR_TEST_BOARD = "" +
            ". . . . . . . " +
            ". . . . . . . " +
            ". . . . . . . " +
            ". . . . . . . " +
            ". . X X X X . " +
            ". . O O O O . ";

    private static final String CONNECT_FOUR_TEST_BOARD_EMPTY = "" +
            ". . . . . . . " +
            ". . . . . . . " +
            ". . . . . . . " +
            ". . . . . . . " +
            ". . . . . . . " +
            ". . . . . . . ";

    private static final String CONNECT_FOUR_TEST_BOARD_3_SINGLE_LINES_IN_CORNER = "" +
            ". . . . . . . " +
            ". . . . . . . " +
            ". . . . . . . " +
            ". . . . . . . " +
            ". . . . . . . " +
            ". . . . . O . ";

    private static final String CONNECT_FOUR_TEST_BOARD_CIRCLE_15_10_5 = "" +
            ". . . . . . . . . . . . . . . " +
            ". . . . . . . . . . . . . . . " +
            ". . . . . . . . . . . . . . . " +
            ". . . . . . X . X . . . . . . " +
            ". . . . . . . O . . . . . . . " +
            ". . . . . . . X O . . . . . . " +
            ". . . . . . . X . O . . . . . " +
            ". . . . . . . . . . . . . . . " +
            ". . . . . . . . . . . . . . . " +
            ". . . . . . . . . . . . . . . ";

    @Test
    void testGetPlayableLinesByLength() {
        // given
        ToeSide[][] currentBoard = loadBoard(CONNECT_FOUR_TEST_BOARD, 7, 6);
        BigInteger Xs = getBitboard(currentBoard, ToeSide.CROSS);
        BigInteger Os = getBitboard(currentBoard, ToeSide.CIRCLE);
        BigInteger[] bitboards = new BigInteger[]{Xs, Os};
        Bitsquares bitsquares = new Bitsquares(6, 7, 4, bitboards);

        // when
        BigInteger[][][] playableLines = bitsquares.getPlayableLinesByLength(bitboards);

        // then
        assertThat(playableLines).isNotNull();
        int[][]lengths = new int[2][winSize];

        for (int player = 0; player < 2; player++) {
            for (int index = 0; index < playableLines[player].length; index++) {
                lengths[player][index] = playableLines[player][index] == null ? 0 : playableLines[player][index].length;
            }
        }

        assertThat(lengths).isEqualTo(new int[][] {{0,0,1,1,1},{0,9,1,1,1}});
    }

    @Test
    void testGetPlayableLinesByLengthEmpty() {
        // given
        ToeSide[][] currentBoard = loadBoard(CONNECT_FOUR_TEST_BOARD_EMPTY, 7, 6);
        BigInteger Xs = getBitboard(currentBoard, ToeSide.CROSS);
        BigInteger Os = getBitboard(currentBoard, ToeSide.CIRCLE);
        BigInteger[] bitboards = new BigInteger[]{Xs, Os};
        Bitsquares bitsquares = new Bitsquares(6, 7, 4, bitboards);

        // when
        BigInteger[][][] playableLines = bitsquares.getPlayableLinesByLength(bitboards);

        // then
        assertThat(playableLines).isNotNull();
        int[][]lengths = new int[2][winSize];

        for (int player = 0; player < 2; player++) {
            for (int index = 0; index < playableLines[player].length; index++) {
                lengths[player][index] = playableLines[player][index] == null ? 0 : playableLines[player][index].length;
            }
        }

        assertThat(lengths).isEqualTo(new int[][] {{0,0,0,0,0},{0,0,0,0,0}});
    }

    @Test
    void testBitsquareHeuristic() {
        // given
        ToeSide[][] currentBoard = loadBoard(CONNECT_FOUR_TEST_BOARD_3_SINGLE_LINES_IN_CORNER, 7, 6);
        BigInteger Xs = getBitboard(currentBoard, ToeSide.CROSS);
        BigInteger Os = getBitboard(currentBoard, ToeSide.CIRCLE);
        BigInteger[] bitboards = new BigInteger[]{Xs, Os};
        Bitsquares bitsquares = new Bitsquares(6, 7, 4, bitboards);

        // when
        double p1Score = bitsquares.bitsquareHeuristic(bitboards, ToeSide.CROSS, 2d);
        double p2Score = bitsquares.bitsquareHeuristic(bitboards, ToeSide.CIRCLE, 2d);

        // then
        assertThat(p1Score).isEqualTo(3);
        assertThat(p2Score).isEqualTo(-p1Score);
    }

    @Test
    void testBitsquareHeuristicBigBoard() {
        // given
        ToeSide[][] currentBoard = loadBoard(CONNECT_FOUR_TEST_BOARD_CIRCLE_15_10_5, 15, 10);
        BigInteger Xs = getBitboard(currentBoard, ToeSide.CROSS);
        BigInteger Os = getBitboard(currentBoard, ToeSide.CIRCLE);
        BigInteger[] bitboards = new BigInteger[]{Xs, Os};
        Bitsquares bitsquares = new Bitsquares(15, 10, 5, bitboards);

        // when
        double p1Score = bitsquares.bitsquareHeuristic(bitboards, ToeSide.CROSS, 2d);
        double p2Score = bitsquares.bitsquareHeuristic(bitboards, ToeSide.CIRCLE, 2d);

//        for(int i=0; i<100000; i++) {
//            //bitsquares.evaluate();
//            //evaluateBoardState(currentBoard, ToeSide.CROSS);
//            bitsquares.bitsquareHeuristic(bitboards, ToeSide.CROSS, 2d);
//        }

        // then
        assertThat(p1Score).isEqualTo(-17);
        assertThat(p2Score).isEqualTo(-p1Score);
    }
}

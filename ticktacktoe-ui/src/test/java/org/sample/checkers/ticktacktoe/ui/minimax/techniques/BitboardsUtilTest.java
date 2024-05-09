package org.sample.checkers.ticktacktoe.ui.minimax.techniques;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.sample.checkers.config.ticktacktoe.ToeSide;
import org.sample.checkers.ticktacktoe.ui.minimax.techniques.util.TechniquesTestUtils;

import java.math.BigInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BitboardsUtilTest extends TechniquesTestUtils {

    private static final String CONNECT_FOUR_TEST_BOARD = "" +
            ". . . . . . . " +
            ". . . . . . . " +
            ". . . . . . . " +
            ". . . O . . . " +
            ". . . X X . . " +
            ". . O X O . . ";

    private static final String CONNECT_FOUR_TEST_BOARD_WIN_CROSS = "" +
            ". . . . . . . " +
            ". . . . . . . " +
            ". . . . . . . " +
            ". . . O . . . " +
            ". . X X X X . " +
            ". . O X O . . ";

    private static final String CONNECT_FOUR_TEST_BOARD_WIN_CIRCLE = "" +
            ". . . . . . . " +
            ". . . . . . . " +
            ". . O . . . . " +
            ". . O O . . . " +
            ". . O X X . . " +
            ". . O X O . . ";

    private static final String CONNECT_FOUR_TEST_BOARD_WIN_CIRCLE_15_10_5 = "" +
            ". . . . . . . . . . . . . . . " +
            ". . . . . . . . . . . . . . . " +
            ". . . . . . . . . . . . . . . " +
            ". . . . . . X . X . . . . . . " +
            ". . . . . . . O . . . . . . . " +
            ". . . . . . . X O . . . . . . " +
            ". . . . . . . X . O . . . . . " +
            ". . . . . . . X . . O . . . . " +
            ". . . . . . . . . . . O . . . " +
            ". . . . . . . . . . . . . . . ";

    @Test
    void testLoadBoard() {
        // given
        ToeSide[][] currentBoard = loadBoard(CONNECT_FOUR_TEST_BOARD, 7, 6);

        // when
        printBoard(currentBoard);

        //then
        assertThat(currentBoard).isNotNull();
        assertThat(currentBoard).hasDimensions(7, 6);
    }

    @Test
    void testLoadBitBoard() {
        // given
        ToeSide[][] currentBoard = loadBoard(CONNECT_FOUR_TEST_BOARD, 7, 6);

        // when
        long Xs = BitboardsUtil.getBitboard(currentBoard, ToeSide.CROSS);
        long Os = BitboardsUtil.getBitboard(currentBoard, ToeSide.CIRCLE);

        //then
        assertThat(Xs).isNotNull().isNotZero();
        assertThat(Os).isNotNull().isNotZero();

        printBoard(currentBoard);

        System.out.println(getBitboardAsString(currentBoard, ToeSide.CROSS));
        System.out.println(getBitboardAsString(currentBoard, ToeSide.CIRCLE));
        System.out.println(Long.toBinaryString(Xs));
        System.out.println(Long.toBinaryString(Os));

        long[] Xss = getBitboardAsArrayFor(currentBoard, ToeSide.CROSS);
        long[] Oss = getBitboardAsArrayFor(currentBoard, ToeSide.CIRCLE);
        printBitBoard(Xss);
        printBitBoard(Oss);
    }

    @Test
    void testLoadBitBoardToArray() {
        // given
        ToeSide[][] currentBoard = loadBoard(CONNECT_FOUR_TEST_BOARD, 7, 6);

        // when
        long[] Xs = getBitboardAsArrayFor(currentBoard, ToeSide.CROSS);
        long[] Os = getBitboardAsArrayFor(currentBoard, ToeSide.CIRCLE);

        //then
        assertThat(Xs).isNotNull().isNotEmpty();
        assertThat(Os).isNotNull().isNotEmpty();

        printBoard(currentBoard);
        printBitBoard(Xs);
        printBitBoard(Os);
    }

    @Test
    void testBitShiftingRight() {
        // given
        int i = 0b10101110;

        // when
        int shifted = i>>3;

        // then
        assertThat(shifted).isEqualTo(0b00010101);
    }

    @Test
    void testBitShiftingLeft() {
        // given
        int i = 0b10101110;

        // when
        int shifted = i<<3;
        int bits = ((shifted) & 0xff);

        // then
        System.out.println(Integer.toBinaryString(shifted));
        System.out.println(Integer.toBinaryString(bits));
        assertThat(bits).isEqualTo(0b01110000);
    }

    @Test
    void testXOR() {
        // given
        int i = 0b10101110;
        int j = 0b10011111;

        // when
        int xor = i ^ j;

        // then
        System.out.println(Integer.toBinaryString(i));
        System.out.println(Integer.toBinaryString(j));
        System.out.println(Integer.toBinaryString(xor));
        assertThat(xor).isEqualTo(0b00110001);
    }

    @Test
    void testAND() {
        // given
        int i = 0b10101110;
        int j = 0b10011111;

        // when
        int xor = i & j;

        // then
        System.out.println(Integer.toBinaryString(i));
        System.out.println(Integer.toBinaryString(j));
        System.out.println(Integer.toBinaryString(xor));
        assertThat(xor).isEqualTo(0b10001110);
    }

    @Test
    void testIsWin() {
        // given
        ToeSide[][] currentBoard = loadBoard(CONNECT_FOUR_TEST_BOARD_WIN_CROSS, 7, 6);
        long Xs = BitboardsUtil.getBitboard(currentBoard, ToeSide.CROSS);
        long Os = BitboardsUtil.getBitboard(currentBoard, ToeSide.CIRCLE);

        // when
        boolean crossWin = BitboardsUtil.isWin(Xs);
        boolean circleWin = BitboardsUtil.isWin(Os);

        //then
        assertThat(crossWin).isTrue();
        assertThat(circleWin).isFalse();
    }

    @Test
    void testIsWinCircle() {
        // given
        ToeSide[][] currentBoard = loadBoard(CONNECT_FOUR_TEST_BOARD_WIN_CIRCLE, 7, 6);
        long Xs = BitboardsUtil.getBitboard(currentBoard, ToeSide.CROSS);
        long Os = BitboardsUtil.getBitboard(currentBoard, ToeSide.CIRCLE);

        // when
        boolean crossWin = BitboardsUtil.isWin(Xs);
        boolean circleWin = BitboardsUtil.isWin(Os);

        //then
        assertThat(crossWin).isFalse();
        assertThat(circleWin).isTrue();
    }

    @Test
    void testIsNotWin() {
        // given
        ToeSide[][] currentBoard = loadBoard(CONNECT_FOUR_TEST_BOARD, 7, 6);
        long Xs = BitboardsUtil.getBitboard(currentBoard, ToeSide.CROSS);
        long Os = BitboardsUtil.getBitboard(currentBoard, ToeSide.CIRCLE);

        // when
        boolean crossWin = BitboardsUtil.isWin(Xs);
        boolean circleWin = BitboardsUtil.isWin(Os);

        //then
        assertThat(crossWin).isFalse();
        assertThat(circleWin).isFalse();
    }

    @Test
    void testIsWinCircle5InRow() {
        // given
        ToeSide[][] currentBoard = loadBoard(CONNECT_FOUR_TEST_BOARD_WIN_CIRCLE_15_10_5, 15, 10);
        printBoard(currentBoard);

        long Xs = BitboardsUtil.getBitboard(currentBoard, ToeSide.CROSS);
        long Os = BitboardsUtil.getBitboard(currentBoard, ToeSide.CIRCLE);

/*
                              0 0 0 0 0 0 0 0 0 0 0 0 0 0 0  0 0 0 0 0 0 0 0 0 0 0 0 0 0 0   10 21 32 43 54 65 76 87 98 109 120 131 142 153 164
. . . . . . . . . . . . . . . 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0  0 0 0 0 0 0 0 0 0 0 0 0 0 0 0   9  20 31 42 53 64 75 86 97 108 119 130 141 152 163
. . . . . . . . . . . . . . . 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0  0 0 0 0 0 0 0 0 0 0 0 0 0 0 0   8  19 30 41 52 63 74 85 96 107 118 129 140 154 162
. . . . . . . . . . . . . . . 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0  0 0 0 0 0 0 0 0 0 0 0 0 0 0 0   7  18 29 40 51 62 73 84 95 106 117 128 139 150 161
. . . . . . X . X . . . . . . 0 0 0 0 0 0 1 0 1 0 0 0 0 0 0  0 0 0 0 0 0 0 0 0 0 0 0 0 0 0   6  17 28 39 50 61 72 83 94 105 116 127 138 149 160
. . . . . . . O . . . . . . . 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0  0 0 0 0 0 0 0 1 0 0 0 0 0 0 0   5  16 27 38 49 60 71 82 93 104 115 126 137 148 159
. . . . . . . X O . . . . . . 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0  0 0 0 0 0 0 0 0 1 0 0 0 0 0 0   4  15 26 37 48 59 70 81 92 103 114 125 136 147 158
. . . . . . . X . O . . . . . 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0  0 0 0 0 0 0 0 0 0 1 0 0 0 0 0   3  14 25 36 47 58 69 80 91 102 113 124 135 146 157
. . . . . . . X . . O . . . . 0 0 0 0 0 0 0 1 0 0 0 0 0 0 0  0 0 0 0 0 0 0 0 0 0 1 0 0 0 0   2  13 24 35 46 57 68 79 90 101 112 123 134 145 156
. . . . . . . . . . . O . . . 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0  0 0 0 0 0 0 0 0 0 0 0 1 0 0 0   1  12 23 34 45 56 67 78 89 100 111 122 133 144 155
. . . . . . . . . . . . . . . 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0  0 0 0 0 0 0 0 0 0 0 0 0 0 0 0   0  11 22 33 44 55 66 77 88  99 110 121 132 143 154
-----------------------------
0 1 2 3 4 5 6 7 8 9 1011121314

... 00000000000 00000000000 00000000000 00000000000 00000000000 00000000000 00001000000 00000011100 00001000000 000000000000 00000000000 00000000000 00000000000 00000000000 00000000000 // encoding Xs
... 00000000000 00000000000 00000000000 00000000010 00000000100 00000001000 00000010000 00000100000 00000000000 000000000000 00000000000 00000000000 00000000000 00000000000 00000000000 // encoding Os
         col 14      col 13      col 12      col 11      col 10       col 9       col 8       col 7       col 6       col 5       col 4       col 3       col 2       col 1       col 0
*/

        // when
        boolean crossWin = BitboardsUtil.isWin(Xs);
        boolean circleWin = BitboardsUtil.isWin(Os);

        //then
        assertThat(crossWin).isFalse();
        assertThat(circleWin).isTrue();
    }
}
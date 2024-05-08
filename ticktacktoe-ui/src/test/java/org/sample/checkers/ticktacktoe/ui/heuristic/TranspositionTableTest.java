package org.sample.checkers.ticktacktoe.ui.heuristic;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Stack;

import static org.sample.checkers.ticktacktoe.ui.heuristic.util.HeuristicUtil.*;

public class TranspositionTableTest {

    private static final int [][] STATE_10_X_10_EMPTY = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    private static final int [][] STATE_5_X_5_EMPTY = {
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
    };

    private static final int CROSS = 1;
    private static final int FIRST_MOVE = CROSS;
    private static final int CIRCLE = 2;
    private static final int EMPTY = 0;

    @Test
    public void testTranspositionTable() {
        // given
        int[] ternaryMax = {2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2};

        int[][] stateFull = ternaryToState(ternaryMax, STATE_10_X_10_EMPTY.length, STATE_10_X_10_EMPTY[0].length);
        int decimalMax = toDecimal(ternaryMax);
        double doubleMax = toDoubleDecimal(ternaryMax);
        BigDecimal bigDecimalMax = toBigDecimal(ternaryMax);
        System.out.println(decimalMax);
        System.out.println(bigDecimalMax);
        System.out.println(Double.MAX_VALUE);
        printState(stateFull);

        // when
        double i = 5;
        while(i<doubleMax) { //5.153775207320112E47

            int[] ternary = toTernary(i);
            int[][] state = ternaryToState(ternary, STATE_10_X_10_EMPTY.length, STATE_10_X_10_EMPTY[0].length);

            if(i % 100000000 == 0) {
                int[] ternary2 = toTernary(i);
                int[][] state2 = ternaryToState(ternary2, STATE_10_X_10_EMPTY.length, STATE_10_X_10_EMPTY[0].length);
                System.out.println(doubleMax);
                System.out.println(i);
                System.out.println(toDecimal(ternary2));
                printTernary(ternary2);
                printState(state2);
            }

            i++;
        }

        // then
        // NEVER END
    }

    @Test
    public void testTranspositionTable_5() {
        // given
        int[] ternaryMax = {2, 2, 2, 2, 2,
                2, 2, 2, 2, 2,
                2, 2, 2, 2, 2,
                2, 2, 2, 2, 2,
                2, 2, 2, 2, 2};

        int[][] stateFull = ternaryToState(ternaryMax, STATE_5_X_5_EMPTY.length, STATE_5_X_5_EMPTY[0].length);
        int decimalMax = toDecimal(ternaryMax);
        double doubleMax = toDoubleDecimal(ternaryMax);
        BigDecimal bigDecimalMax = toBigDecimal(ternaryMax);
        System.out.println(decimalMax);
        System.out.println(bigDecimalMax);
        System.out.println(Double.MAX_VALUE);
        printState(stateFull);

        double i = 5;
        while(i<doubleMax) { // 847 288 609 442

            int[] ternary = toTernary(i);
            int[][] state = ternaryToState(ternary, STATE_5_X_5_EMPTY.length, STATE_5_X_5_EMPTY[0].length);

            if(i % 1000000 == 0) {
                int[] ternary2 = toTernary(i);
                int[][] state2 = ternaryToState(ternary2, STATE_5_X_5_EMPTY.length, STATE_5_X_5_EMPTY[0].length);
                System.out.println(doubleMax);
                System.out.println(i);
                System.out.println(toDecimal(ternary2));
                printTernary(ternary2);
                printState(state2);
            }

            i++;
        }

        // then
        // NEVER END
    }
}

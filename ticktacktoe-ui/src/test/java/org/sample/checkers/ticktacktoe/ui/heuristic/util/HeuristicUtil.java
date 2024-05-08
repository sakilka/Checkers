package org.sample.checkers.ticktacktoe.ui.heuristic.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Stack;

public class HeuristicUtil {

    public static int[] toTernary(int decimal) {
        Stack<Integer> ternaryStack = new Stack<>();
        int c = decimal;
        while (c != 0) {
            int z = c%3;
            c = c/3;
            ternaryStack.push(z);
        }

        int [] ternary = new int[ternaryStack.size()];
        int index = 0;

        while (!ternaryStack.isEmpty()) {
            ternary[index++] = ternaryStack.pop();
        }

        return ternary;
    }

    public static void printTernary(int [] ternary) {
        Arrays.stream(ternary).forEach(System.out::print);
        System.out.println();
    }

    public static int[] toTernary(double decimal) {
        Stack<Integer> ternaryStack = new Stack<>();
        double c = decimal;
        while (c != 0) {
            double z = c % 3;
            c = (c-z)/3;
            ternaryStack.push((int) z);
        }

        int [] ternary = new int[ternaryStack.size()];
        int index = 0;

        while (!ternaryStack.isEmpty()) {
            ternary[index++] = ternaryStack.pop();
        }

        return ternary;
    }

    public static int toDecimal(int[] ternary) {
        int decimal = 0;

        int multiplier = ternary.length -1;

        for (int digit : ternary){
            decimal += digit * (Math.pow(3, multiplier--));
        }

        return decimal;
    }

    public static double toDoubleDecimal(int[] ternary) {
        double decimal = 0;

        int multiplier = ternary.length -1;

        for (int digit : ternary){
            decimal +=  digit * Math.pow(3, multiplier--);
        }

        return decimal;
    }

    public static BigDecimal toBigDecimal(int[] ternary) {
        BigDecimal decimal = BigDecimal.ZERO;

        int multiplier = ternary.length -1;

        for (int digit : ternary){
            decimal = decimal.add(BigDecimal.valueOf(digit).multiply((BigDecimal.valueOf(3).pow(multiplier--))));
        }

        return decimal;
    }

    public static int[][] ternaryToState(int[] ternary, int width, int height) {
        int [][] state = new int[width][height];

        for (int index = ternary.length; index > 0; index--) {
            state[(index-1) / height][(index-1) % width] = ternary[ternary.length - (index)];
        }

        return state;
    }

    public static void printState(int [][] state) {
        System.out.println();
        for(int width = 0; width < state.length; width++) {
            for (int height = 0; height < state[0].length; height++) {
                System.out.print(state[width][height] == 0 ? " ? "
                        : state[width][height] == 1 ? " X " : " O ");
            }
            System.out.println();
        }

    }
}

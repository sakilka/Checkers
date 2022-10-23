package org.sample.checkers.config.chess;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ChessPropertyUtil {

    private static ChessFiguresPositions chessFiguresPositions;
    private static ChessLoadModel chessLoadModel;

    public static ChessFiguresPositions getPositions() {
        if(chessFiguresPositions == null) {
            ApplicationContext context = new AnnotationConfigApplicationContext(ChessFiguresPositions.class);
            chessFiguresPositions = context.getBean(ChessFiguresPositions.class);
        }

        return chessFiguresPositions;
    }

    public static ChessLoadModel getModel() {
        if(chessLoadModel == null) {
            ApplicationContext context = new AnnotationConfigApplicationContext(ChessLoadModel.class);
            chessLoadModel = context.getBean(ChessLoadModel.class);
        }

        return chessLoadModel;
    }
}

package org.sample.checkers.config.checkers;

import org.sample.checkers.config.chess.BoardConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CheckersPropertyUtil {

    private static BoardConfiguration configuration;
    private static CheckersFiguresPositions checkersFiguresPositions;
    private static CheckersLoadModel checkersLoadModel;

    public static BoardConfiguration getConfig() {
        if(configuration == null) {
            ApplicationContext context = new AnnotationConfigApplicationContext(BoardConfiguration.class);
            configuration = context.getBean(BoardConfiguration.class);
        }

        return configuration;
    }

    public static CheckersFiguresPositions getPositions() {
        if(checkersFiguresPositions == null) {
            ApplicationContext context = new AnnotationConfigApplicationContext(CheckersFiguresPositions.class);
            checkersFiguresPositions = context.getBean(CheckersFiguresPositions.class);
        }

        return checkersFiguresPositions;
    }

    public static CheckersLoadModel getModel() {
        if(checkersLoadModel == null) {
            ApplicationContext context = new AnnotationConfigApplicationContext(CheckersLoadModel.class);
            checkersLoadModel = context.getBean(CheckersLoadModel.class);
        }

        return checkersLoadModel;
    }
}

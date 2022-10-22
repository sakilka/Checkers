package org.sample.checkers.config;

import org.sample.checkers.config.BoardConfiguration;
import org.sample.checkers.config.chess.FiguresPositions;
import org.sample.checkers.config.chess.LoadModel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PropertyUtil {

    private static BoardConfiguration configuration;
    private static FiguresPositions figuresPositions;
    private static LoadModel loadModel;

    public static BoardConfiguration getConfig() {
        if(configuration == null) {
            ApplicationContext context = new AnnotationConfigApplicationContext(BoardConfiguration.class);
            configuration = context.getBean(BoardConfiguration.class);
        }

        return configuration;
    }

    public static FiguresPositions getPositions() {
        if(figuresPositions == null) {
            ApplicationContext context = new AnnotationConfigApplicationContext(FiguresPositions.class);
            figuresPositions = context.getBean(FiguresPositions.class);
        }

        return figuresPositions;
    }

    public static LoadModel getModel() {
        if(loadModel == null) {
            ApplicationContext context = new AnnotationConfigApplicationContext(LoadModel.class);
            loadModel = context.getBean(LoadModel.class);
        }

        return loadModel;
    }
}

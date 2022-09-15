package org.sample.checkers.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PropertyUtil {

    private static CheckersConfiguration configuration;
    private static FiguresPositions figuresPositions;
    private static LoadModel loadModel;

    public static CheckersConfiguration getConfig() {
        if(configuration == null) {
            ApplicationContext context = new AnnotationConfigApplicationContext(CheckersConfiguration.class);
            configuration = context.getBean(CheckersConfiguration.class);
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

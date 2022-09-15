package org.sample.checkers.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PropertyUtil {

    private static CheckersConfiguration configuration;
    private static FiguresPositions figuresPositions;

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
}

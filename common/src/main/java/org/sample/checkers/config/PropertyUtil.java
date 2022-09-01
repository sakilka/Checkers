package org.sample.checkers.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PropertyUtil {

    public static CheckersConfiguration getConfig() {
        ApplicationContext context = new AnnotationConfigApplicationContext(CheckersConfiguration.class);
        return context.getBean(CheckersConfiguration.class);
    }

    public static FiguresPositions getPositions() {
        ApplicationContext context = new AnnotationConfigApplicationContext(FiguresPositions.class);
        return context.getBean(FiguresPositions.class);
    }
}

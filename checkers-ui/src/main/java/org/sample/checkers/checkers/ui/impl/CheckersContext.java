package org.sample.checkers.checkers.ui.impl;

import org.sample.checkers.config.checkers.CheckersUi;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class CheckersContext {

    private static CheckersUi ui;

    public static CheckersUi getUi() {
        if(ui == null) {
            ApplicationContext context = new AnnotationConfigApplicationContext(CheckersContext.class);
            ui = context.getBean(CheckersUi.class);
        }

        return ui;
    }
}

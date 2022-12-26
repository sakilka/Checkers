package org.sample.checkers.ticktacktoe.ui;

import org.sample.checkers.ticktacktoe.ui.simple.SimpleHeuristicUi;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class TickTackToeContext {

    private static TickTackToeUi ui;

    public static TickTackToeUi getUi() {
        if(ui == null) {
            ApplicationContext context = new AnnotationConfigApplicationContext(TickTackToeContext.class);
            ui = context.getBean(SimpleHeuristicUi.class);
        }

        return ui;
    }
}

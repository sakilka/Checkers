package org.sample.checkers.chess.ui;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class ChessContext {

    private static ChessUi ui;

    public static ChessUi getUi() {
        if(ui == null) {
            ApplicationContext context = new AnnotationConfigApplicationContext(ChessContext.class);
            ui = context.getBean(ChessUi.class);
        }

        return ui;
    }
}

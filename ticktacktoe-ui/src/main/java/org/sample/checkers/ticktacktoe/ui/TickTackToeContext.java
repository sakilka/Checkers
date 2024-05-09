package org.sample.checkers.ticktacktoe.ui;

import org.sample.checkers.ticktacktoe.ui.minimax.AdvancedMinimaxUi;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class TickTackToeContext {

    private static TickTackToeUi ui;

    public static TickTackToeUi getUi() {
        if(ui == null) {
            ApplicationContext context = new AnnotationConfigApplicationContext(TickTackToeContext.class);
            //ui = context.getBean("negaMaxPruning", TickTackToeUi.class);
            //ui = context.getBean("alphaBetaPruning", TickTackToeUi.class);
            //ui = context.getBean("miniMaxUi", TickTackToeUi.class);
            //ui = context.getBean("negaMax", TickTackToeUi.class);
            //ui = context.getBean("advancedAlphaBetaPruning", TickTackToeUi.class);
            //ui = context.getBean("simpleHeuristicUi", TickTackToeUi.class);
            ui = context.getBean("advancedMinimaxUi", AdvancedMinimaxUi.class);
        }

        return ui;
    }
}

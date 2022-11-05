package org.sample.checkers.config.ticktacktoe;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TickTackToePropertyUtil {

    private static TickTackToeConfiguration configuration;

    public static TickTackToeConfiguration getConfig() {
        if(configuration == null) {
            ApplicationContext context = new AnnotationConfigApplicationContext(TickTackToeConfiguration.class);
            configuration = context.getBean(TickTackToeConfiguration.class);
        }

        return configuration;
    }
}

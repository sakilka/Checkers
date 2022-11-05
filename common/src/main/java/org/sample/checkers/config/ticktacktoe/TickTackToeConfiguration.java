package org.sample.checkers.config.ticktacktoe;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class TickTackToeConfiguration {

    @Value("${ticktacktoe.width}")
    private int tickTackToeWidth;

    @Value("${ticktacktoe.height}")
    private int tickTackToeHeight;

    public int getTickTackToeWidth() {
        return tickTackToeWidth;
    }

    public void setTickTackToeWidth(int tickTackToeWidth) {
        this.tickTackToeWidth = tickTackToeWidth;
    }

    public int getTickTackToeHeight() {
        return tickTackToeHeight;
    }

    public void setTickTackToeHeight(int tickTackToeHeight) {
        this.tickTackToeHeight = tickTackToeHeight;
    }
}

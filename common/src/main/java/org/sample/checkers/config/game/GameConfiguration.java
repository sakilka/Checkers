package org.sample.checkers.config.game;

import org.sample.checkers.config.Game;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class GameConfiguration {

    @Value("${game.option}")
    private Game gameOption;

    public Game getGameOption() {
        return gameOption;
    }

    public void setGameOption(Game gameOption) {
        this.gameOption = gameOption;
    }
}

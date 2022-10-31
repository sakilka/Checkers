package org.sample.checkers.config.game;

import javafx.util.Duration;
import org.sample.checkers.config.Game;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

@Configuration
@PropertySource("classpath:application.properties")
public class GameConfiguration {

    @Value("${game.option}")
    private Game gameOption;

    @Value("${animation.duration}")
    private int duration;

    @Bean("gameSetup")
    @Scope("singleton")
    public GameSetup gameSetup() {
        GameSetup setup = new GameSetup();
        setup.setGame(gameOption);
        setup.setAnimationDuration(Duration.seconds(duration));
        return setup;
    }
}

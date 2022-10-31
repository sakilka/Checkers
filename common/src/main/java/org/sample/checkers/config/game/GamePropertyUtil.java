package org.sample.checkers.config.game;

import org.sample.checkers.config.chess.BoardConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class GamePropertyUtil {

    private static GameConfiguration gameConfiguration;
    private static BoardConfiguration configuration;
    private static GameSetup gameSetup;

    public static GameConfiguration getGameConfig() {
        if(gameConfiguration == null) {
            ApplicationContext context = new AnnotationConfigApplicationContext(GameConfiguration.class);
            gameConfiguration = context.getBean(GameConfiguration.class);

            if(gameSetup == null) {
                gameSetup = context.getBean(GameSetup.class);
            }
        }

        return gameConfiguration;
    }

    public static BoardConfiguration getBoardConfig() {
        if(configuration == null) {
            ApplicationContext context = new AnnotationConfigApplicationContext(BoardConfiguration.class);
            configuration = context.getBean(BoardConfiguration.class);
        }

        return configuration;
    }

    public static GameSetup getGameSetup() {
        if(gameSetup == null) {
            ApplicationContext context = new AnnotationConfigApplicationContext(GameConfiguration.class);
            gameSetup = context.getBean(GameSetup.class);

            if(gameConfiguration == null) {
                gameConfiguration = context.getBean(GameConfiguration.class);
            }
        }

        return gameSetup;
    }
}

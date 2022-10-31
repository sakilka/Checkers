package org.sample.checkers.config.game;

import org.sample.checkers.config.Game;

import java.util.Arrays;

public class GameSetup {

    private Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setGame(String game) {
        this.game = Arrays.stream(Game.values()).anyMatch(value-> value.name().equals(game)) ? Game.valueOf(game)
                : Game.CHESS;
    }
}

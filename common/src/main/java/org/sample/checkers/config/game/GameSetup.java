package org.sample.checkers.config.game;

import javafx.util.Duration;
import org.sample.checkers.config.Game;

import java.util.Arrays;

public class GameSetup {

    private Game game;
    private Duration animationDuration;
    private boolean moveFigure;

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

    public Duration getAnimationDuration() {
        return animationDuration;
    }

    public void setAnimationDuration(Duration animationDuration) {
        this.animationDuration = animationDuration;
    }

    public boolean isMoveFigure() {
        return moveFigure;
    }

    public void setMoveFigure(boolean moveFigure) {
        this.moveFigure = moveFigure;
    }
}

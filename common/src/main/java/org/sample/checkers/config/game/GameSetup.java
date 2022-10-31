package org.sample.checkers.config.game;

import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Arrays;

public class GameSetup {

    private Game game;
    private Duration animationDuration;
    private boolean moveFigure;
    private Timeline playTimeline;
    private Player player;

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
        this.playTimeline = null;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setPlayer(String player) {
        this.player = Arrays.stream(Player.values()).anyMatch(value-> value.name().equals(player))
                ? Player.valueOf(player) : Player.SINGLE_PLAYER;
    }

    public Timeline getPlayTimeline() {
        return playTimeline;
    }

    public void setPlayTimeline(Timeline playTimeline) {
        this.playTimeline = playTimeline;
    }
}

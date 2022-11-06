package org.sample.checkers.menu.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.sample.checkers.config.game.Game;
import org.sample.checkers.config.game.GameSetup;
import org.sample.checkers.config.game.Player;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

import static org.sample.checkers.Checkers.newGame;
import static org.sample.checkers.config.game.GamePropertyUtil.getGameSetup;

public class MenuController implements Initializable {

    private Stage stage;
    private final GameSetup gameSetup;

    @Autowired
    public MenuController() {
        this.gameSetup = getGameSetup();
    }

    @FXML
    private ToggleGroup toggleGroupGame;

    @FXML
    private ToggleGroup toggleGroupPlayer;

    @FXML
    private RadioMenuItem CHESS;

    @FXML
    private RadioMenuItem CHECKERS;


    @FXML
    private RadioMenuItem TICKTACKTOE;

    @FXML
    private void handleKeyInput(final InputEvent event) {
        if (event instanceof KeyEvent) {
            final KeyEvent keyEvent = (KeyEvent) event;
            System.out.println("menu action " + keyEvent.getEventType());
        }
    }

    @FXML
    private void handleAboutAction(final ActionEvent event) {
        System.out.println("menu action " + event.getEventType());
    }

    @FXML
    private void quitAction() {
        Platform.exit();
    }

    @FXML
    private void newAction() {
        gameInit();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Game selectedGame = gameSetup.getGame();
        Toggle game = toggleGroupGame.getToggles().stream()
                .filter(toggle -> ((RadioMenuItem) toggle).getId().equals(selectedGame.name())).findFirst()
                .orElseGet(() -> CHESS);
        toggleGroupGame.selectToggle(game);
        toggleGroupGame.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            gameSetup.setGame(((RadioMenuItem) newValue).getId());
            gameInit();
        });

        Player selectedPlayer = gameSetup.getPlayer();
        Toggle player = toggleGroupPlayer.getToggles().stream()
                .filter(toggle -> ((RadioMenuItem) toggle).getId().equals(selectedPlayer.name())).findFirst()
                .orElseGet(() -> CHESS);
        toggleGroupPlayer.selectToggle(player);
        toggleGroupPlayer.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            gameSetup.setPlayer(((RadioMenuItem) newValue).getId());
            gameInit();
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void gameInit() {
        newGame(stage, gameSetup.getGame());
    }
}

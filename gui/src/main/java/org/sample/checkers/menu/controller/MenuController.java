package org.sample.checkers.menu.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.sample.checkers.chess.BoardPosition;
import org.sample.checkers.config.game.Game;
import org.sample.checkers.config.game.GameSetup;
import org.sample.checkers.config.game.Player;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.util.ResourceBundle;

import static org.sample.checkers.Checkers.newGame;
import static org.sample.checkers.config.game.GamePropertyUtil.getBoardConfig;
import static org.sample.checkers.config.game.GamePropertyUtil.getGameSetup;

public class MenuController implements Initializable {

    private Stage stage;
    private BoardPosition boardPosition;
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

    public void setBoardPosition(BoardPosition boardPosition) {
        this.boardPosition = boardPosition;
    }

    private void gameInit() {
        SubScene boardScene = newGame(stage, boardPosition, gameSetup.getGame());
        Scene mainScene = stage.getScene();
        BorderPane root = (BorderPane) mainScene.getRoot();
        SplitPane splitPane = (SplitPane) root.getChildren().stream()
                .filter(node -> node instanceof SplitPane).findFirst().orElseGet(null);
        StackPane boardPane = (StackPane) splitPane.getItems().get(0);
        boardPane.getChildren().remove(0);
        boardPane.getChildren().add(boardScene);
        boardScene.widthProperty().bind(mainScene.widthProperty());
        boardScene.heightProperty().bind(mainScene.heightProperty());
        boardScene.setFill(Color.rgb(0,100,0, 1));
        stage.setScene(mainScene);
    }
}

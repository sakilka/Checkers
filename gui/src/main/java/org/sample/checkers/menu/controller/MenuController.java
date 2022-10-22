package org.sample.checkers.menu.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.SplitPane;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.sample.checkers.board.model.BoardPosition;

import java.net.URL;
import java.util.ResourceBundle;

import static org.sample.checkers.Checkers.newGame;
import static org.sample.checkers.config.PropertyUtil.getConfig;

public class MenuController implements Initializable {

    private Stage stage;
    private BoardPosition boardPosition;

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
        newGameInit();
    }

    @FXML
    private void chessInit() {
        newGameInit();
    }

    @FXML
    private void checkersInit() {
        newGameInit();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setBoardPosition(BoardPosition boardPosition) {
        this.boardPosition = boardPosition;
    }

    private void newGameInit() {
        SubScene boardScene = newGame(stage, boardPosition);
        Scene mainScene = stage.getScene();
        BorderPane root = (BorderPane) mainScene.getRoot();
        SplitPane splitPane = (SplitPane) root.getChildren().stream()
                .filter(node -> node instanceof SplitPane).findFirst().orElseGet(null);
        StackPane boardPane = (StackPane) splitPane.getItems().get(0);
        boardPane.getChildren().remove(0);
        boardPane.getChildren().add(boardScene);
        boardScene.widthProperty().bind(mainScene.widthProperty().subtract(getConfig().getRightPanelWidth()));
        boardScene.heightProperty().bind(mainScene.heightProperty());
        boardScene.setFill(Color.rgb(0,100,0, 1));
        stage.setScene(mainScene);
    }
}

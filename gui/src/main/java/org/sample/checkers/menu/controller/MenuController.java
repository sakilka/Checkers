package org.sample.checkers.menu.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.sample.checkers.Checkers.initializeScene;

public class MenuController implements Initializable {

    private Stage stage;

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
    private void newAction() throws IOException {
        initializeScene(stage);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}

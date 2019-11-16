package org.sample.checkers.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;

public class MenuController {

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
}

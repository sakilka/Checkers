package org.sample.checkers.chess;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

public class CheckDialog {

        private final Stage stage ;

        public CheckDialog(Window parent, String message) {
            Pane pane = new Pane();
            pane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

            pane.setOnMousePressed(event -> {
                this.hide();
            });

            Text text = new Text(message);
            text.setStroke(Color.SILVER);
            text.setFill(Color.WHITE);
            text.setFont(Font.font(Font.getFamilies().get(50), FontWeight.SEMI_BOLD, 50));
            text.setX(100);
            text.setY(80);

            text.setStrokeWidth(2);
            text.setStroke(Color.SILVER);

            Reflection reflection = new Reflection();
            reflection.setFraction(0.8);
            text.setEffect(reflection);

            pane.getChildren().add(text);

            Scene scene  = new Scene(pane, 500, 180);
            stage = new Stage();
            stage.initOwner(parent);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
        }

        public void showDialog() {
            Timeline idle = new Timeline( new KeyFrame( Duration.millis(1500), event -> this.hide()));
            idle.play();
            stage.showAndWait();
        }

        public void hide() {
            stage.hide();
        }
}

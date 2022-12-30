package org.sample.checkers.tictactoe;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.sample.checkers.config.chess.ChessFigure;
import org.sample.checkers.config.game.Game;
import org.sample.checkers.config.ticktacktoe.ToeSide;

import java.util.Objects;

import static org.sample.checkers.Checkers.newGame;

public class ToeWinDialog {

    private final Stage stage ;

    private ChessFigure selectedOption = null ;

    public ToeWinDialog() {
        this(null, null);
    }

    public ToeWinDialog(Window parent, ToeSide side) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fx-padding: 12px; -fx-spacing: 5px;");
        vBox.getChildren().addAll(getImage(side), createButton());

        Scene scene = new Scene(vBox);
        stage = new Stage();
        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
    }

    private Button createButton() {
        Button button = new Button("New Game");
//        button.setGraphic(getImage());
        //button.setStyle(STYLE_NORMAL);
        button.setOnAction(e -> {
            stage.close();
            newGame((Stage) stage.getOwner(), Game.TICKTACKTOE);
        });
        return button ;
    }

    public void showDialog() {
        selectedOption = null;
        stage.showAndWait();
    }

    private ImageView getImage(ToeSide side) {
        Image originalImage = new Image(Objects
                .requireNonNull(getClass().getResourceAsStream(side.name() + ".png")));
        ImageView image = new ImageView(originalImage);
        image.setFitHeight(400);
        image.setFitHeight(150);
        image.setPreserveRatio(true);
        return image;
    }
}

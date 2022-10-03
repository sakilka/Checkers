package org.sample.checkers.board;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.sample.checkers.config.ChessFigure;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class PromotionDialog {

    private static final String STYLE_NORMAL = "-fx-background-color: transparent; -fx-padding: 2, 2, 2, 2;";

    private final Stage stage ;

    private ChessFigure selectedOption = null ;

    public PromotionDialog() {
        this(null);
    }

    public PromotionDialog(Window parent) {
        HBox hBox = new HBox();
        // Real app should use an external style sheet:
        hBox.setStyle("-fx-padding: 12px; -fx-spacing: 5px;");
        Stream.of(ChessFigure.values())
                .filter(figure -> figure != ChessFigure.PAWN)
                .map(this::createButton)
                .forEach(hBox.getChildren()::add);
        Scene scene = new Scene(hBox);
        stage = new Stage();
        stage.initOwner(parent);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
    }

    private Button createButton(ChessFigure figure) {
        Button button = new Button();
        button.setGraphic(getImage(figure));
        button.setStyle(STYLE_NORMAL);
        button.setOnAction(e -> {
            selectedOption = figure;
            stage.close();
        });
        return button ;
    }

    public Optional<ChessFigure> showDialog() {
        selectedOption = null ;
        stage.showAndWait();
        return Optional.ofNullable(selectedOption);
    }

    private ImageView getImage(ChessFigure figure) {
        Image originalImage = new Image(Objects
                .requireNonNull(getClass().getResourceAsStream("figures/" + figure.name() + ".jpg")));
        ImageView image = new ImageView(originalImage);
        image.setFitHeight(400);
        image.setFitHeight(150);
        image.setPreserveRatio(true);
        return image;
    }
}

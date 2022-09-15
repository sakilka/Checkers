package org.sample.checkers.board.model;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class EffectUtils {

    public static Image createImage(Color fill, Color background) {
        Pane empty = new BorderPane();

        empty.setPrefSize(10, 10);
        empty.setBackground(new Background(new BackgroundFill(background, CornerRadii.EMPTY, Insets.EMPTY)));
        new Scene(empty);
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(fill);
        sp.setDepthBuffer(true);
        return empty.snapshot(sp, null);
    }
}

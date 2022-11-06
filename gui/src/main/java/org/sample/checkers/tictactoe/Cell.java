package org.sample.checkers.tictactoe;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import org.sample.checkers.config.ticktacktoe.ToeTurn;

public class Cell extends Pane {

    private TickTackToeScene tickTackToeScene;

    public Cell(TickTackToeScene tickTackToeScene) {
        setBorder(new Border(new BorderStroke(Color.rgb(0,0,0, 1), BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(0.02, 0.02, 0.02, 0.02, true, true, true, true))));
        setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255, 1), null, null)));
        this.setPrefSize(20000, 20000);
        this.setOnMouseClicked(e -> handleMouseClick());
        this.tickTackToeScene = tickTackToeScene;
    }

    private void handleMouseClick() {
        if(tickTackToeScene.getOnTurn() == ToeTurn.CIRCLE) {
            setCircle();
        } else {
            setCross();
        }
        tickTackToeScene.setOnTurn(tickTackToeScene.getOnTurn() == ToeTurn.CIRCLE ? ToeTurn.CROSS : ToeTurn.CIRCLE);
    }

    private void setCircle() {
        Ellipse ellipse = new Ellipse(this.getWidth() / 2,
                this.getHeight() / 2, this.getWidth() / 2 - 10,
                this.getHeight() / 2 - 10);
        ellipse.centerXProperty().bind(
                this.widthProperty().divide(2));
        ellipse.centerYProperty().bind(
                this.heightProperty().divide(2));
        ellipse.radiusXProperty().bind(
                this.widthProperty().divide(2).subtract(10));
        ellipse.radiusYProperty().bind(
                this.heightProperty().divide(2).subtract(10));
        ellipse.setStroke(Color.BLUE);
        ellipse.setFill(Color.WHITE);

        getChildren().add(ellipse);
    }

    private void setCross() {
        Line line1 = new Line(10, 10,
                this.getWidth() - 10, this.getHeight() - 10);
        line1.endXProperty().bind(this.widthProperty().subtract(10));
        line1.endYProperty().bind(this.heightProperty().subtract(10));
        Line line2 = new Line(10, this.getHeight() - 10,
                this.getWidth() - 10, 10);
        line2.startYProperty().bind(
                this.heightProperty().subtract(10));
        line2.endXProperty().bind(this.widthProperty().subtract(10));

        line1.setStroke(Color.RED);
        line2.setStroke(Color.RED);

        this.getChildren().addAll(line1, line2);
    }
}

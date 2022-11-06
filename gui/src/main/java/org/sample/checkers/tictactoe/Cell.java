package org.sample.checkers.tictactoe;

import javafx.geometry.Point2D;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Scale;
import org.sample.checkers.config.ticktacktoe.ToeTurn;

import java.util.ArrayList;
import java.util.List;

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
        Shape circle = createHandDrawnCircle(this.getWidth() / 2, this.getHeight() / 2,
                this.getWidth() / 2 - 10, 2, 5, Color.BLUE);

        Scale scale = new Scale();
        scale.setX(1);
        scale.setY(1);
        circle.getTransforms().add(scale);
        scale.xProperty().bind(this.widthProperty().divide(this.widthProperty().doubleValue()));
        scale.yProperty().bind(this.heightProperty().divide(this.heightProperty().doubleValue()));

        this.getChildren().add(circle);
    }

    private void setCross() {

        Shape line1 = createHandDrawnLine(10, 10, this.getWidth() - 10, this.getHeight() - 10, 10, Color.RED);
        Shape line2 = createHandDrawnLine(10, this.getHeight() - 10, this.getWidth() - 10, 10, 10, Color.RED);

        Scale scale = new Scale();
        scale.setX(1);
        scale.setY(1);
        line1.getTransforms().add(scale);
        line2.getTransforms().add(scale);
        scale.xProperty().bind(this.widthProperty().divide(this.widthProperty().doubleValue()));
        scale.yProperty().bind(this.heightProperty().divide(this.heightProperty().doubleValue()));

        this.getChildren().addAll(line1, line2);
    }

    public Shape createHandDrawnLine(double x1, double y1, double x2, double y2, double strokeWidth, Color color) {
        Point2D startPoint = new Point2D(x1, y1);
        Point2D endPoint = new Point2D(x2, y2);

        double wobble = Math.sqrt((endPoint.getX() - startPoint.getX()) * (endPoint.getX() - startPoint.getX())
                + (endPoint.getY() - startPoint.getY()) * (endPoint.getY() - startPoint.getY())) / 25;

        double r1 = Math.random();
        double r2 = Math.random();

        double xfactor = Math.random() > 0.5 ? wobble : -wobble;
        double yfactor = Math.random() > 0.5 ? wobble : -wobble;

        Point2D control1 = new Point2D((endPoint.getX() - startPoint.getX()) * r1 + startPoint.getX() + xfactor,
                (endPoint.getY() - startPoint.getY()) * r1 + startPoint.getY() + yfactor);
        Point2D control2 = new Point2D((endPoint.getX() - startPoint.getX()) * r2 + startPoint.getX() - xfactor,
                (endPoint.getY() - startPoint.getY()) * r2 + startPoint.getY() - yfactor);

        MoveTo startMove = new MoveTo(startPoint.getX(), startPoint.getY());
        CubicCurveTo curve = new CubicCurveTo(control1.getX(), control1.getY(),
                control2.getX(), control2.getY(),
                endPoint.getX(), endPoint.getY());

        Path path = new Path(startMove, curve);
        path.setStrokeLineCap(StrokeLineCap.ROUND);
        path.setStroke(color);
        path.setStrokeWidth(strokeWidth + (strokeWidth * (Math.random() - 0.5) / 8.0));
        path.setStrokeType(StrokeType.CENTERED);
        return path;
    }

    public Shape createHandDrawnCircle(double cx, double cy, double radius, int rounds,
                                       double strokeWidth, Color color) {
//        int cx, cy,                                                         /// the calced point
        double tol = Math.random() * (radius * 0.03) + (radius * 0.025);    ///tolerance / fluctation
        double dx = Math.random() * tol * 0.75;                             /// "bouncer" values
        double dy = Math.random() * tol * 0.75;
        double ix = (Math.random() - 1) * (radius * 0.0044);                /// speed /incremental
        double iy = (Math.random() - 1) * (radius * 0.0033);
        double rx = radius + Math.random() * tol;                           /// radius X
        double ry = (radius + Math.random() * tol) * 0.8;                   /// radius Y
        double a = 0;                                                       /// angle
        double ad = 3;                                                      /// angle delta (resolution)
        double i = 0;                                                       /// counter
        double start = Math.random() + 50;                                  /// random delta start
        double tot = 360 * rounds + Math.random() * 50 - 100;               /// end angle
        List<PathElement> points = new ArrayList<>();                            /// the points array
        double deg2rad = Math.PI / 180;                                     /// degrees to radians

        for (; i < tot; i += ad) {
            dx += ix;
            dy += iy;

            if (dx < -tol || dx > tol) ix = -ix;
            if (dy < -tol || dy > tol) iy = -iy;

            double x = cx + (rx + dx * 2) * Math.cos(i * deg2rad + start);
            double y = cy + (ry + dy * 2) * Math.sin(i * deg2rad + start);

            if(points.isEmpty()) {
                points.add(new MoveTo(x, y));
                continue;
            }

            points.add(new LineTo(x, y));
        }


        Path path = new Path(points);

        path.setStrokeLineCap(StrokeLineCap.ROUND);
        path.setStroke(color);
        path.setStrokeWidth(strokeWidth + (strokeWidth * (Math.random() - 0.5) / 8.0));
        path.setStrokeType(StrokeType.CENTERED);
        return path;
    }
}

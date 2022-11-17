package org.sample.checkers.tictactoe;

import javafx.geometry.Point2D;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Scale;
import org.sample.checkers.config.ticktacktoe.ToeSide;

import java.util.ArrayList;
import java.util.List;

import static org.sample.checkers.config.ticktacktoe.ToeSide.CIRCLE;
import static org.sample.checkers.config.ticktacktoe.ToeSide.CROSS;

public class Cell extends Pane {

    private final TickTackToeScene tickTackToeScene;
    private static final int strokeWidth = 7;
    private final int widthPosition;
    private final int heightPosition;
    private ToeSide side;
    private ToeSide onTurn;

    public Cell(TickTackToeScene tickTackToeScene, int widthPosition, int heightPosition) {
        setBorder(new Border(new BorderStroke(Color.rgb(0,0,0, 1), BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY, new BorderWidths(0.02, 0.02, 0.02, 0.02, true, true, true, true))));
        setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255, 1), null, null)));
        this.setPrefSize(20000, 20000);
        this.tickTackToeScene = tickTackToeScene;
        this.widthPosition = widthPosition;
        this.heightPosition = heightPosition;
        this.setOnMouseClicked(e -> tickTackToeScene.handleMouseClick(this));
    }

    public void setCircle() {
        Shape circle = createHandDrawnCircle(this.getWidth() / 2, this.getHeight() / 2,
                this.getWidth() / 2 - 10, 2, strokeWidth, Color.rgb(0,0,255, 1));

        Scale scale = new Scale();
        scale.setX(1);
        scale.setY(1);
        circle.getTransforms().add(scale);
        scale.xProperty().bind(this.widthProperty().divide(this.widthProperty().doubleValue()));
        scale.yProperty().bind(this.heightProperty().divide(this.heightProperty().doubleValue()));
        circle.strokeWidthProperty().bind(this.widthProperty().divide(this.widthProperty().doubleValue())
                .multiply(strokeWidth));

        this.getChildren().add(circle);
        this.side = CIRCLE;
    }

    public void setCross() {
        Shape line1 = createHandDrawnLine(10, 10, this.getWidth() - 10, this.getHeight() - 10,
                strokeWidth, Color.rgb(255,0,0, 1));
        Shape line2 = createHandDrawnLine(10, this.getHeight() - 10, this.getWidth() - 10, 10,
                strokeWidth, Color.rgb(255,0,0, 1));

        Scale scale = new Scale();
        scale.setX(1);
        scale.setY(1);
        line1.getTransforms().add(scale);
        line2.getTransforms().add(scale);
        scale.xProperty().bind(this.widthProperty().divide(this.widthProperty().doubleValue()));
        scale.yProperty().bind(this.heightProperty().divide(this.heightProperty().doubleValue()));
        line1.strokeWidthProperty().bind(this.widthProperty().divide(this.widthProperty().doubleValue())
                .multiply(strokeWidth));
        line2.strokeWidthProperty().bind(this.widthProperty().divide(this.widthProperty().doubleValue())
                .multiply(strokeWidth));

        this.getChildren().addAll(line1, line2);
        this.side = CROSS;
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

        double tol = Math.random() * (radius * 0.03) + (radius * 0.025);
        double bounceX = Math.random() * tol * 0.75;
        double bounceY = Math.random() * tol * 0.75;
        double ix = (Math.random() - 1) * (radius * 0.0033);
        double iy = (Math.random() - 1) * (radius * 0.0033);
        double radiusX = radius + Math.random() * tol;
        double radiusY = (radius + Math.random() * tol) * 1;
        double step = 3;
        double start = Math.random() + 50;
        double tot = 360 * rounds + Math.random() * 50 - 100;
        double deg2rad = Math.PI / 180;

        List<PathElement> points = new ArrayList<>();

        for (int i=0; i < tot; i += step) {
            bounceX += ix;
            bounceY += iy;

            if (bounceX < -tol || bounceX > tol) ix = -ix;
            if (bounceY < -tol || bounceY > tol) iy = -iy;

            double x = cx + (radiusX + bounceX * 2) * Math.cos(i * deg2rad + start);
            double y = cy + (radiusY + bounceY * 2) * Math.sin(i * deg2rad + start);

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

    public ToeSide getSide() {
        return side;
    }

    public int getWidthPosition() {
        return widthPosition;
    }

    public int getHeightPosition() {
        return heightPosition;
    }
}

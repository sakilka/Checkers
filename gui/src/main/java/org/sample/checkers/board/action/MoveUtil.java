package org.sample.checkers.board.action;

import com.sun.javafx.geom.Dimension2D;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import org.sample.checkers.board.model.Cube;
import org.sample.checkers.board.model.Figure;

import java.util.List;
import java.util.stream.Stream;

import static org.sample.checkers.config.FiguresPositions.getAbsolutePositionX;
import static org.sample.checkers.config.FiguresPositions.getAbsolutePositionY;

public class MoveUtil {

    public static void handlePrimaryClick(MouseEvent event, Cube[][] board, List<Figure> figures, float fieldWidth) {
        EventTarget target = event.getTarget();

        PhongMaterial blueMaterial = new PhongMaterial(Color.BLUE);
        blueMaterial.setSpecularColor(Color.WHITE);
        blueMaterial.setSpecularPower(32);

        if(target instanceof  Node){
            Node targetNode = (Node) target;
            if(targetNode instanceof Cube) {
                Cube targetCube = (Cube) targetNode;
                if(isField(board, targetCube)){
                    targetCube.setMaterial(blueMaterial);
                    Dimension2D position = getFieldPosition(board, targetCube);
                    Figure targetFigure = getFigureForPosition(figures, position, fieldWidth);
                    if (targetFigure != null) {
                        targetFigure.setMaterial(new PhongMaterial(Color.BLUE));
                    }
                }
            } else if (targetNode instanceof Figure) {
                Figure targetFigure = (Figure) targetNode;
                targetFigure.setMaterial(new PhongMaterial(Color.BLUE));
                Dimension2D position = getFigurePosition(figures, targetFigure, fieldWidth);
                if (position != null) {
                    board[(int) position.width - 1][(int) position.height - 1].setMaterial(blueMaterial);
                }
            }
        }
    }

    private static boolean isField(Cube[][] board, Cube targetCube) {
        return Stream.of(board)
                .flatMap(Stream::of)
                .anyMatch(cube -> cube.equals(targetCube));
    }

    private static Dimension2D getFieldPosition(Cube[][] board, Cube targetCube) {
        int x;
        int y;

        for (y = 0; y < 8; y++) {
            for (x = 0; x < 8; x++) {
                if(board[x][y].equals(targetCube)) {
                    return new Dimension2D(x + 1, y + 1);
                }
            }
        }

        return null;
    }

    private static Dimension2D getFigurePosition(List<Figure> figures, Figure targetFigure, float fieldWidth) {

        for (Figure figure : figures) {
            if(figure.equals(targetFigure)) {
                long x = getRelativePositionX(targetFigure.getTranslateX(), fieldWidth);
                long y = getRelativePositionY(targetFigure.getTranslateZ(), fieldWidth);
                return new Dimension2D(x, y);
            }
        }

        return null;
    }

    public static long getRelativePositionX(double positionX, float fieldWidth) {
        return Math.round(((positionX - (fieldWidth/2)) / fieldWidth) + 5);
    }

    public static long getRelativePositionY(double positionY, float fieldWidth) {
        return Math.round(((positionY - (fieldWidth/2)) / fieldWidth) + 5);
    }

    private static Figure getFigureForPosition(List<Figure> figures, Dimension2D position, float fieldWidth) {

        for (Figure figure : figures) {
            if(figure.getTranslateX() == getAbsolutePositionX((int) position.width, fieldWidth)
            && figure.getTranslateZ() == getAbsolutePositionY((int) position.height, fieldWidth)) {
                return figure;
            }
        }

        return null;
    }
}

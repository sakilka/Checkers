package org.sample.checkers.board.action;

import com.sun.javafx.geom.Dimension2D;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventTarget;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.util.Duration;
import org.sample.checkers.board.model.Cube;
import org.sample.checkers.board.model.Figure;
import org.sample.checkers.config.*;

import java.util.List;
import java.util.stream.Stream;

import static org.sample.checkers.config.FiguresPositions.getAbsolutePositionX;
import static org.sample.checkers.config.FiguresPositions.getAbsolutePositionY;

public class MoveUtil {

    public static Dimension2D handlePrimaryClick(MouseEvent event, Cube[][] board, List<Figure> figures, float fieldWidth,
                                          Dimension2D marked, Dimension2D highlight, ChessBoardPositions currentBoard,
                                                 MoveHistory moveHistory) {
        EventTarget target = event.getTarget();

        if (marked == null) {
            PhongMaterial blueMaterial = new PhongMaterial(Color.BLUE);
            blueMaterial.setSpecularColor(Color.WHITE);
            blueMaterial.setSpecularPower(32);

            if (target instanceof Node) {
                Node targetNode = (Node) target;
                if (targetNode instanceof Cube) {
                    Cube targetCube = (Cube) targetNode;
                    if (isField(board, targetCube)) {
                        Dimension2D position = getFieldPosition(board, targetCube);
                        Figure targetFigure = getFigureForPosition(figures, position, fieldWidth);

                        if (targetFigure != null) {
                            targetCube.setMaterial(blueMaterial);
                            targetFigure.setMaterial(new PhongMaterial(Color.BLUE));
                            highlightPotentialMoves(position, targetFigure.getChessFigure(), targetFigure.getChessSide(),
                                    currentBoard, board, moveHistory);
                            return position;
                        }
                    }
                } else if (targetNode instanceof Figure) {
                    Figure targetFigure = (Figure) targetNode;
                    Dimension2D position = getFigurePosition(figures, targetFigure, fieldWidth);

                    if (position != null) {
                        targetFigure.setMaterial(new PhongMaterial(Color.BLUE));
                        board[(int) position.width - 1][(int) position.height - 1].setMaterial(blueMaterial);
                    }

                    highlightPotentialMoves(position, targetFigure.getChessFigure(), targetFigure.getChessSide(),
                            currentBoard, board, moveHistory);
                    return position;
                }
            }
            return marked;
        } else {
            Figure targetFigure = getFigureForPosition(figures, marked, fieldWidth);

            if (targetFigure != null && highlight != null && highlight.width != 0 && highlight.height != 0) {
                List<Dimension2D> potentialMoves = CheckersMoves.potentialMoves(targetFigure.getChessFigure(),
                        targetFigure.getChessSide(), moveHistory, new Dimension2D(marked.width-1, marked.height-1),
                        currentBoard);

                if(potentialMoves.stream().anyMatch(position -> (position.width +1) == highlight.width
                        && (position.height + 1) == highlight.height)) {
                    moveFigure(targetFigure, highlight, fieldWidth);
                }
            }
            changeBackAll(figures, fieldWidth, board);
            return null;
        }
    }

    private static void changeBackAll(List<Figure> figures, float fieldWidth, Cube[][] board) {
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                changeBackForPosition(new Dimension2D(i+1,j+1), figures, fieldWidth, board);
            }
        }
    }

    private static void highlightPotentialMoves(Dimension2D position, ChessFigure chessFigure, ChessSide chessSide,
                                                ChessBoardPositions currentBoard, Cube[][] board, MoveHistory moveHistory) {
        PhongMaterial redMaterial = new PhongMaterial(Color.RED);
        redMaterial.setSpecularColor(Color.WHITE);
        redMaterial.setSpecularPower(32);

        List<Dimension2D> potentialMoves = CheckersMoves.potentialMoves(chessFigure, chessSide, moveHistory,
                new Dimension2D(position.width-1, position.height-1), currentBoard);

        for(Dimension2D potentialMove : potentialMoves) {
            board[(int) potentialMove.width][(int) potentialMove.height].setMaterial(redMaterial);
        }
    }

    private static void moveFigure(Figure targetFigure, Dimension2D highlight, float fieldWidth) {
        Timeline animation = createTimeline(
                new Point3D(targetFigure.getTranslateX(),targetFigure.getTranslateY(), targetFigure.getTranslateZ()),
                new Point3D(getAbsolutePositionX((int) highlight.width, fieldWidth), 0, getAbsolutePositionY((int) highlight.height, fieldWidth)),
                targetFigure);
        animation.play();
    }

    private static Timeline createTimeline(Point3D p1, Point3D p2, Node figure) {
        Timeline t = new Timeline();
        KeyValue keyX = new KeyValue(figure.translateXProperty(), p2.getX());
        KeyValue keyY = new KeyValue(figure.translateYProperty(), p2.getY());
        KeyValue keyZ = new KeyValue(figure.translateZProperty(), p2.getZ());
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), keyX, keyY, keyZ);
        t.getKeyFrames().add(keyFrame);
        return t;
    }

    public static Dimension2D handleMarkedMove(MouseEvent event, Cube[][] board, List<Figure> figures, float fieldWidth,
                                        Dimension2D marked, Dimension2D highlight) {
        EventTarget target = event.getTarget();

        PhongMaterial yellowMaterial = new PhongMaterial(Color.YELLOW);
        yellowMaterial.setSpecularColor(Color.WHITE);
        yellowMaterial.setSpecularPower(32);

        if (target instanceof Node) {
            Node targetNode = (Node) target;
            if (targetNode instanceof Cube) {
                Cube targetCube = (Cube) targetNode;
                if (isField(board, targetCube)) {
                    Dimension2D position = getFieldPosition(board, targetCube);

                    if (isSamePosition(position, marked) || isSamePosition(position, highlight)) {
                        if(isSamePosition(position, marked) ) {
                            changeBackForPosition(highlight, figures, fieldWidth, board);
                            return null;
                        }
                        return highlight;
                    }

                    targetCube.setMaterial(yellowMaterial);
                    Figure targetFigure = getFigureForPosition(figures, position, fieldWidth);
                    if (targetFigure != null) {
                        targetFigure.setMaterial(new PhongMaterial(Color.YELLOW));
                    }

                    changeBackForPosition(highlight, figures, fieldWidth, board);

                    return position;
                }
            } else if (targetNode instanceof Figure) {
                Figure targetFigure = (Figure) targetNode;
                Dimension2D position = getFigurePosition(figures, targetFigure, fieldWidth);

                if (isSamePosition(position, marked) || isSamePosition(position, highlight)) {
                    if(isSamePosition(position, marked)) {
                        changeBackForPosition(highlight, figures, fieldWidth, board);
                        return null;
                    }
                    return highlight;
                }

                targetFigure.setMaterial(new PhongMaterial(Color.YELLOW));
                if (position != null) {
                    board[(int) position.width - 1][(int) position.height - 1].setMaterial(yellowMaterial);
                }

                changeBackForPosition(highlight, figures, fieldWidth, board);

                return position;
            }
        }

        return highlight;
    }

    private static void changeBackForPosition(Dimension2D position, List<Figure> figures, float fieldWidth,
                                              Cube[][] board) {
        if(position == null || position.width == 0 || position.height == 0) {
            return;
        }

        Figure targetFigure = getFigureForPosition(figures, position, fieldWidth);
        if (targetFigure != null) {
            targetFigure.changeMaterialToDefault();
        }
        board[(int) position.width - 1][(int) position.height - 1].setMaterialToDefault();
    }

    private static boolean isSamePosition(Dimension2D position1, Dimension2D position2) {
        if(position1 == null || position2 == null) {
            return false;
        }

        return position1.height == position2.height && position1.width == position2.width;
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

    public static ChessFigure getBoardFigure(Cube[][] board, List<Figure> figures, Dimension2D position, float fieldWidth) {
        Figure targetFigure = getFigureForPosition(figures, position, fieldWidth);

        return targetFigure == null ? null : targetFigure.getChessFigure();
    }
}

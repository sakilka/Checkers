package org.sample.checkers.checkers;

import com.sun.javafx.geom.Dimension2D;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventTarget;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.sample.checkers.chess.*;
import org.sample.checkers.config.checkers.*;
import org.sample.checkers.config.chess.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import static org.sample.checkers.config.checkers.CheckersFigure.PAWN;
import static org.sample.checkers.config.checkers.CheckersFigure.QUEEN;
import static org.sample.checkers.config.checkers.CheckersSide.BLACK;
import static org.sample.checkers.config.checkers.CheckersSide.WHITE;
import static org.sample.checkers.config.chess.ChessFiguresPositions.getAbsolutePositionX;
import static org.sample.checkers.config.chess.ChessFiguresPositions.getAbsolutePositionY;;

public class CheckersMoveUtil {

    private static Color shineColor = Color.rgb(229,206,0, 0.3);
    private static Color highlightColor = Color.rgb(62,177,90, 0.6);
    private static Color moveColor = Color.rgb(229,1,0, 0.8);

    public static Dimension2D handlePrimaryClick(MouseEvent event, Cube[][] board, List<CheckersFigureModel> checkersFigures, float fieldWidth,
                                                 Dimension2D marked, Dimension2D highlight, CheckersBoardPositions currentBoard,
                                                 CheckersMoveHistory checkersMoveHistory, Group boardSceneGroup, Stage mainStage) {
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
                        CheckersFigureModel targetChessFigure = getFigureForPosition(checkersFigures, position, fieldWidth);

                        if (targetChessFigure != null && targetChessFigure.getCheckersSide() == checkersMoveHistory.getOnMove()) {
                            targetCube.highlightField(true, shineColor);
                            targetChessFigure.highlightFigure(true, shineColor);
                            highlightPotentialMoves(position, targetChessFigure.getCheckersFigure(), targetChessFigure.getCheckersSide(),
                                    currentBoard, board, checkersMoveHistory);
                            return position;
                        }
                    }
                } else if (targetNode instanceof CheckersFigureModel) {
                    CheckersFigureModel targetChessFigure = (CheckersFigureModel) targetNode;

                    if(targetChessFigure.getCheckersSide() == checkersMoveHistory.getOnMove()) {

                        Dimension2D position = getFigurePosition(checkersFigures, targetChessFigure, fieldWidth);

                        if (position != null) {
                            targetChessFigure.highlightFigure(true, shineColor);
                            board[(int) position.width - 1][(int) position.height - 1].highlightField(true, shineColor);
                        }

                        highlightPotentialMoves(position, targetChessFigure.getCheckersFigure(), targetChessFigure.getCheckersSide(),
                                currentBoard, board, checkersMoveHistory);
                        return position;
                    }
                }
            }
            return marked;
        } else {
            CheckersFigureModel targetCheckersFigure = getFigureForPosition(checkersFigures, marked, fieldWidth);

            if (targetCheckersFigure != null && highlight != null && highlight.width != 0 && highlight.height != 0) {
                List<Dimension2D> potentialMoves = CheckersMoveFactory.getMove(targetCheckersFigure.getCheckersFigure())
                        .potentialMoves(targetCheckersFigure.getCheckersSide(), checkersMoveHistory,
                                new Dimension2D(marked.width-1, marked.height-1), currentBoard);

                if(potentialMoves.stream().anyMatch(position -> (position.width +1) == highlight.width
                        && (position.height + 1) == highlight.height)) {
                    promoteFigure(targetCheckersFigure, currentBoard, highlight, checkersFigures, fieldWidth, mainStage, boardSceneGroup);
                    captureFigure(currentBoard, highlight, checkersFigures, fieldWidth, boardSceneGroup);
                    moveFigure(targetCheckersFigure, highlight, fieldWidth);
                    checkersMoveHistory.addMove(new CheckersMovePosition(highlight, marked));
                }
            }
            changeBackAll(checkersFigures, fieldWidth, board);
            return null;
        }
    }

    private static void changeBackAll(List<CheckersFigureModel> checkersFigures, float fieldWidth, Cube[][] board) {
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                changeBackForPosition(new Dimension2D(i+1,j+1), checkersFigures, fieldWidth, board);
            }
        }
    }

    private static void highlightPotentialMoves(Dimension2D position, CheckersFigure checkersFigure, CheckersSide checkersSide,
                                                CheckersBoardPositions currentBoard, Cube[][] board, CheckersMoveHistory checkersMoveHistory) {

        List<Dimension2D> potentialMoves = CheckersMoveFactory.getMove(checkersFigure)
                .potentialMoves(checkersSide, checkersMoveHistory,  new Dimension2D(position.width-1, position.height-1),
                        currentBoard);

        for(Dimension2D potentialMove : potentialMoves) {
            board[(int) potentialMove.width][(int) potentialMove.height].highlightField(true, highlightColor);
        }
    }

    private static void promoteFigure(CheckersFigureModel targetChessFigure, CheckersBoardPositions currentBoard, Dimension2D highlight,
                                      List<CheckersFigureModel> checkersFigures, float fieldWidth, Stage mainStage, Group boardSceneGroup){
        if(targetChessFigure.getCheckersFigure() == PAWN) {
            if(highlight.height == 8 && targetChessFigure.getCheckersSide() == WHITE) {
                Optional<org.sample.checkers.config.chess.ChessFigure> promote = new PromotionDialog(mainStage).showDialog();
                CheckersFigure checkersFigure = CheckersFigure.QUEEN;
                PhongMaterial promotedMaterial = (PhongMaterial) targetChessFigure.getMaterial();
                boardSceneGroup.getChildren().remove(targetChessFigure);
                checkersFigures.remove(targetChessFigure);
                CheckersFigureModel promotedChessFigure = new CheckersFigureModel(checkersFigure.name().toLowerCase(Locale.ROOT),
                        new FigurePosition(
                                getAbsolutePositionX((int) highlight.width, fieldWidth), 0,
                                getAbsolutePositionY((int) highlight.height, fieldWidth)),
                        promotedMaterial, checkersFigure, WHITE);
                boardSceneGroup.getChildren().add(promotedChessFigure);
                checkersFigures.add(promotedChessFigure);
            }

            if(highlight.height == 1 && targetChessFigure.getCheckersSide() == BLACK) {
                //Optional<CheckersFigure> promote = new PromotionDialog(mainStage).showDialog();
                CheckersFigure promote = QUEEN;
                CheckersFigure checkersFigure = promote;
                PhongMaterial promotedMaterial = (PhongMaterial) targetChessFigure.getMaterial();
                boardSceneGroup.getChildren().remove(targetChessFigure);
                checkersFigures.remove(targetChessFigure);
                CheckersFigureModel promotedCheckersFigure = new CheckersFigureModel(checkersFigure.name().toLowerCase(Locale.ROOT),
                        new FigurePosition(
                                getAbsolutePositionX((int) highlight.width, fieldWidth), 0,
                                getAbsolutePositionY((int) highlight.height, fieldWidth)),
                        promotedMaterial, checkersFigure, BLACK);
                boardSceneGroup.getChildren().add(promotedCheckersFigure);
                checkersFigures.add(promotedCheckersFigure);
            }
        }
    }

    private static void captureFigure(CheckersBoardPositions currentBoard, Dimension2D highlight, List<CheckersFigureModel> checkersFigures,
                                      float fieldWidth, Group boardSceneGroup){
        if(currentBoard.getPositions()[(int) highlight.width - 1][(int)highlight.height - 1] != null) {
            CheckersFigureModel captured = getFigureForPosition(checkersFigures, highlight, fieldWidth);
            boardSceneGroup.getChildren().remove(captured);
            checkersFigures.remove(captured);
        }
    }

    private static void moveFigure(CheckersFigureModel targetChessFigure, Dimension2D highlight, float fieldWidth) {
        Timeline animation = createTimeline(
                new Point3D(targetChessFigure.getTranslateX(), targetChessFigure.getTranslateY(), targetChessFigure.getTranslateZ()),
                new Point3D(getAbsolutePositionX((int) highlight.width, fieldWidth), 0, getAbsolutePositionY((int) highlight.height, fieldWidth)),
                targetChessFigure);
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

    public static Dimension2D handleMarkedMove(MouseEvent event, Cube[][] board, List<CheckersFigureModel> checkersFigures, float fieldWidth,
                                               Dimension2D marked, Dimension2D highlight, CheckersBoardPositions currentBoard,
                                               CheckersMoveHistory checkersMoveHistory) {
        EventTarget target = event.getTarget();

        if (target instanceof Node) {
            Node targetNode = (Node) target;
            if (targetNode instanceof Cube) {
                Cube targetCube = (Cube) targetNode;
                if (isField(board, targetCube)) {
                    Dimension2D position = getFieldPosition(board, targetCube);

                    if (isSamePosition(position, marked) || isSamePosition(position, highlight)) {
                        if(isSamePosition(position, marked) ) {
                            highlightBack(checkersFigures, marked, fieldWidth, checkersMoveHistory, currentBoard, highlight, board, highlightColor);
                            return null;
                        }
                        return highlight;
                    }

                    targetCube.highlightField(true, moveColor);
                    CheckersFigureModel targetCheckersFigure = getFigureForPosition(checkersFigures, position, fieldWidth);
                    if (targetCheckersFigure != null) {
                        targetCheckersFigure.highlightFigure(true, moveColor);
                    }

                    highlightBack(checkersFigures, marked, fieldWidth, checkersMoveHistory, currentBoard, highlight, board, highlightColor);

                    return position;
                }
            } else if (targetNode instanceof CheckersFigureModel) {
                CheckersFigureModel targetCheckersFigure = (CheckersFigureModel) targetNode;
                Dimension2D position = getFigurePosition(checkersFigures, targetCheckersFigure, fieldWidth);

                if (isSamePosition(position, marked) || isSamePosition(position, highlight)) {
                    if(isSamePosition(position, marked)) {
                        highlightBack(checkersFigures, marked, fieldWidth, checkersMoveHistory, currentBoard, highlight, board, highlightColor);
                        return null;
                    }
                    return highlight;
                }

                targetCheckersFigure.highlightFigure(true, moveColor);
                if (position != null) {
                    board[(int) position.width - 1][(int) position.height - 1].highlightField(true, moveColor);
                }

                highlightBack(checkersFigures, marked, fieldWidth, checkersMoveHistory, currentBoard, highlight, board, highlightColor);

                return position;
            }
        }

        return highlight;
    }

    private static void highlightBackForPosition(Dimension2D position, List<CheckersFigureModel> checkersFigures, float fieldWidth,
                                                 Cube[][] board, Color highlightColor) {
        if(position == null || position.width == 0 || position.height == 0) {
            return;
        }

        CheckersFigureModel targetChessFigure = getFigureForPosition(checkersFigures, position, fieldWidth);
        if (targetChessFigure != null) {
            targetChessFigure.highlightFigure(true, highlightColor);
        }
        board[(int) position.width - 1][(int) position.height - 1].highlightField(true, highlightColor);
    }

    private static void changeBackForPosition(Dimension2D position, List<CheckersFigureModel> checkersFigures, float fieldWidth,
                                              Cube[][] board) {
        if(position == null || position.width == 0 || position.height == 0) {
            return;
        }

        CheckersFigureModel targetCheckersFigure = getFigureForPosition(checkersFigures, position, fieldWidth);
        if (targetCheckersFigure != null) {
            targetCheckersFigure.changeMaterialToDefault();
            targetCheckersFigure.highlightFigure(false, shineColor);
        }
        board[(int) position.width - 1][(int) position.height - 1].highlightField(false, shineColor);
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

    private static Dimension2D getFigurePosition(List<CheckersFigureModel> checkersFigures, CheckersFigureModel targetCheckersFigure, float fieldWidth) {

        for (CheckersFigureModel checkersFigure : checkersFigures) {
            if(checkersFigure.equals(targetCheckersFigure)) {
                long x = getRelativePositionX(targetCheckersFigure.getTranslateX(), fieldWidth);
                long y = getRelativePositionY(targetCheckersFigure.getTranslateZ(), fieldWidth);
                return new Dimension2D(x, y);
            }
        }

        return null;
    }

    private static long getRelativePositionX(double positionX, float fieldWidth) {
        return Math.round(((positionX - (fieldWidth/2)) / fieldWidth) + 5);
    }

    private static long getRelativePositionY(double positionY, float fieldWidth) {
        return Math.round(((positionY - (fieldWidth/2)) / fieldWidth) + 5);
    }

    private static CheckersFigureModel getFigureForPosition(List<CheckersFigureModel> checkersFigures, Dimension2D position, float fieldWidth) {

        for (CheckersFigureModel checkersFigure : checkersFigures) {
            if(checkersFigure.getTranslateX() == getAbsolutePositionX((int) position.width, fieldWidth)
                    && checkersFigure.getTranslateZ() == getAbsolutePositionY((int) position.height, fieldWidth)) {
                return checkersFigure;
            }
        }

        return null;
    }

    private static void highlightBack(List<CheckersFigureModel> checkersFigures, Dimension2D marked, float fieldWidth, CheckersMoveHistory checkersMoveHistory,
                                      CheckersBoardPositions currentBoard, Dimension2D highlight, Cube[][] board,
                                      Color highlightColor){
        CheckersFigureModel markedChessFigure = getFigureForPosition(checkersFigures, marked, fieldWidth);
        if (markedChessFigure != null) {
            List<Dimension2D> potentialMoves = CheckersMoveFactory.getMove(markedChessFigure.getCheckersFigure())
                    .potentialMoves(markedChessFigure.getCheckersSide(), checkersMoveHistory,
                            new Dimension2D(marked.width-1, marked.height-1), currentBoard);

            if(potentialMoves.stream().noneMatch(move -> highlight != null && move.height +1 == highlight.height
                    && move.width + 1 == highlight.width)) {
                changeBackForPosition(highlight, checkersFigures, fieldWidth, board);
            } else {
                highlightBackForPosition(highlight, checkersFigures, fieldWidth, board, highlightColor);
            }
        }
    }

    public static CheckersFigure getBoardFigure(List<CheckersFigureModel> chessFigures, Dimension2D position, float fieldWidth) {
        CheckersFigureModel targetChessFigure = getFigureForPosition(chessFigures, position, fieldWidth);

        return targetChessFigure == null ? null : targetChessFigure.getCheckersFigure();
    }

    public static CheckersSide getBoardSide(List<CheckersFigureModel> chessFigures, Dimension2D position, float fieldWidth) {
        CheckersFigureModel targetChessFigure = getFigureForPosition(chessFigures, position, fieldWidth);

        return targetChessFigure == null ? null : targetChessFigure.getCheckersSide();
    }
}

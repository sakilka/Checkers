package org.sample.checkers.board.action;

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
import org.sample.checkers.board.PromotionDialog;
import org.sample.checkers.board.model.Cube;
import org.sample.checkers.board.model.Figure;
import org.sample.checkers.board.model.FigurePosition;
import org.sample.checkers.config.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import static org.sample.checkers.config.ChessFigure.*;
import static org.sample.checkers.config.ChessSide.BLACK;
import static org.sample.checkers.config.ChessSide.WHITE;
import static org.sample.checkers.config.FiguresPositions.getAbsolutePositionX;
import static org.sample.checkers.config.FiguresPositions.getAbsolutePositionY;

public class MoveUtil {

    private static Color shineColor = Color.rgb(229,206,0, 0.3);
    private static Color highlightColor = Color.rgb(62,177,90, 0.6);
    private static Color moveColor = Color.rgb(229,1,0, 0.8);

    public static Dimension2D handlePrimaryClick(MouseEvent event, Cube[][] board, List<Figure> figures, float fieldWidth,
                                                 Dimension2D marked, Dimension2D highlight, ChessBoardPositions currentBoard,
                                                 MoveHistory moveHistory, Group boardSceneGroup, Stage mainStage) {
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

                        if (targetFigure != null && targetFigure.getChessSide() == moveHistory.getOnMove()) {
                            targetCube.highlightField(true, shineColor);
                            targetFigure.highlightFigure(true, shineColor);
                            highlightPotentialMoves(position, targetFigure.getChessFigure(), targetFigure.getChessSide(),
                                    currentBoard, board, moveHistory);
                            return position;
                        }
                    }
                } else if (targetNode instanceof Figure) {
                    Figure targetFigure = (Figure) targetNode;

                    if(targetFigure.getChessSide() == moveHistory.getOnMove()) {

                        Dimension2D position = getFigurePosition(figures, targetFigure, fieldWidth);

                        if (position != null) {
                            targetFigure.highlightFigure(true, shineColor);
                            board[(int) position.width - 1][(int) position.height - 1].highlightField(true, shineColor);
                        }

                        highlightPotentialMoves(position, targetFigure.getChessFigure(), targetFigure.getChessSide(),
                                currentBoard, board, moveHistory);
                        return position;
                    }
                }
            }
            return marked;
        } else {
            Figure targetFigure = getFigureForPosition(figures, marked, fieldWidth);

            if (targetFigure != null && highlight != null && highlight.width != 0 && highlight.height != 0) {
                List<Dimension2D> potentialMoves = CheckersMoveFactory.getMove(targetFigure.getChessFigure())
                        .potentialMoves(targetFigure.getChessSide(), moveHistory,
                                new Dimension2D(marked.width-1, marked.height-1), currentBoard);

                if(potentialMoves.stream().anyMatch(position -> (position.width +1) == highlight.width
                        && (position.height + 1) == highlight.height)) {
                    promoteFigure(targetFigure, currentBoard, highlight, figures, fieldWidth, mainStage, boardSceneGroup);
                    captureFigure(currentBoard, highlight, figures, fieldWidth, boardSceneGroup);
                    moveFigure(targetFigure, highlight, fieldWidth);
                    moveHistory.addMove(new ChessMove(highlight, marked));
                    castling(targetFigure, currentBoard, highlight, figures, fieldWidth, mainStage, boardSceneGroup,
                            moveHistory);
                    enPassant(targetFigure, currentBoard, highlight, figures, fieldWidth, mainStage, boardSceneGroup,
                            moveHistory);
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

        List<Dimension2D> potentialMoves = CheckersMoveFactory.getMove(chessFigure)
                .potentialMoves(chessSide, moveHistory,  new Dimension2D(position.width-1, position.height-1),
                        currentBoard);

        for(Dimension2D potentialMove : potentialMoves) {
            board[(int) potentialMove.width][(int) potentialMove.height].highlightField(true, highlightColor);
        }
    }

    private static void promoteFigure(Figure targetFigure, ChessBoardPositions currentBoard, Dimension2D highlight,
                                      List<Figure> figures, float fieldWidth, Stage mainStage, Group boardSceneGroup){
        if(targetFigure.getChessFigure() == PAWN) {
            if(highlight.height == 8 && targetFigure.getChessSide() == WHITE) {
                Optional<ChessFigure> promote = new PromotionDialog(mainStage).showDialog();
                ChessFigure chessFigure = promote.orElse(ChessFigure.QUEEN);
                PhongMaterial promotedMaterial = (PhongMaterial) targetFigure.getMaterial();
                boardSceneGroup.getChildren().remove(targetFigure);
                figures.remove(targetFigure);
                Figure promotedFigure = new Figure(chessFigure.name().toLowerCase(Locale.ROOT),
                        new FigurePosition(
                        getAbsolutePositionX((int) highlight.width, fieldWidth), 0,
                        getAbsolutePositionY((int) highlight.height, fieldWidth)),
                        promotedMaterial, chessFigure, WHITE);
                boardSceneGroup.getChildren().add(promotedFigure);
                figures.add(promotedFigure);
            }

            if(highlight.height == 1 && targetFigure.getChessSide() == BLACK) {
                Optional<ChessFigure> promote = new PromotionDialog(mainStage).showDialog();
                ChessFigure chessFigure = promote.orElse(ChessFigure.QUEEN);
                PhongMaterial promotedMaterial = (PhongMaterial) targetFigure.getMaterial();
                boardSceneGroup.getChildren().remove(targetFigure);
                figures.remove(targetFigure);
                Figure promotedFigure = new Figure(chessFigure.name().toLowerCase(Locale.ROOT),
                        new FigurePosition(
                                getAbsolutePositionX((int) highlight.width, fieldWidth), 0,
                                getAbsolutePositionY((int) highlight.height, fieldWidth)),
                        promotedMaterial, chessFigure, BLACK);
                boardSceneGroup.getChildren().add(promotedFigure);
                figures.add(promotedFigure);
            }
        }
    }

    private static void captureFigure(ChessBoardPositions currentBoard, Dimension2D highlight, List<Figure> figures,
            float fieldWidth, Group boardSceneGroup){
        if(currentBoard.getPositions()[(int) highlight.width - 1][(int)highlight.height - 1] != null) {
            Figure captured = getFigureForPosition(figures, highlight, fieldWidth);
            boardSceneGroup.getChildren().remove(captured);
            figures.remove(captured);
        }
    }

    private static void moveFigure(Figure targetFigure, Dimension2D highlight, float fieldWidth) {
        Timeline animation = createTimeline(
                new Point3D(targetFigure.getTranslateX(),targetFigure.getTranslateY(), targetFigure.getTranslateZ()),
                new Point3D(getAbsolutePositionX((int) highlight.width, fieldWidth), 0, getAbsolutePositionY((int) highlight.height, fieldWidth)),
                targetFigure);
        animation.play();
    }

    private static void castling(Figure targetFigure, ChessBoardPositions currentBoard, Dimension2D highlight,
                                 List<Figure> figures, float fieldWidth, Stage mainStage, Group boardSceneGroup,
                                 MoveHistory moveHistory) {
        if(targetFigure.getChessFigure() == KING &&
                ((targetFigure.getChessSide() == WHITE && !moveHistory.isCastlingWhiteDone()) ||
                        (targetFigure.getChessSide() == BLACK && !moveHistory.isCastlingBlackDone()))) {

            long x = getRelativePositionX(targetFigure.getTranslateX(), fieldWidth);
            ChessSide side = targetFigure.getChessSide();

            if(Math.abs(highlight.width - x) > 1) {
                if(highlight.width == 3){
                    Dimension2D position = side == WHITE ? new Dimension2D(1,1) : new Dimension2D(1,8);
                    Dimension2D target = side == WHITE ? new Dimension2D(4,1) : new Dimension2D(4,8);
                    Figure castleFigure = getFigureForPosition(figures, position, fieldWidth);

                    moveFigure(castleFigure, target, fieldWidth);
                    moveHistory.addMove(new ChessMove(target, position));
                } else {
                    Dimension2D position = side == WHITE ? new Dimension2D(8,1) : new Dimension2D(8,8);
                    Dimension2D target = side == WHITE ? new Dimension2D(6,1) : new Dimension2D(6,8);
                    Figure castleFigure = getFigureForPosition(figures, position, fieldWidth);

                    moveFigure(castleFigure, target, fieldWidth);
                    moveHistory.addMove(new ChessMove(target, position));
                }
            }

            if(side == WHITE) {
                moveHistory.setCastlingWhiteDone(true);
                moveHistory.setOnMove(BLACK);
            } else {
                moveHistory.setCastlingBlackDone(true);
                moveHistory.setOnMove(WHITE);
            }
        }
    }

    private static void enPassant(Figure targetFigure, ChessBoardPositions currentBoard, Dimension2D highlight,
                                  List<Figure> figures, float fieldWidth, Stage mainStage, Group boardSceneGroup,
                                  MoveHistory moveHistory) {
        if(targetFigure.getChessFigure() == PAWN) {
            ChessMove lastMove = moveHistory.getMoves().get(moveHistory.getMoves().size() -1);

            if(Math.abs(lastMove.getPosition().width - lastMove.getPreviousPosition().width) == 1
            && Math.abs(lastMove.getPosition().height - lastMove.getPreviousPosition().height) == 1){
                if(currentBoard.getPositions()[(int) highlight.width - 1][(int)highlight.height - 1] == null) {
                    captureFigure(currentBoard, new Dimension2D(lastMove.getPosition().width,
                            lastMove.getPreviousPosition().height), figures, fieldWidth, boardSceneGroup);
                }
            }
        }
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
                                        Dimension2D marked, Dimension2D highlight, ChessBoardPositions currentBoard,
                                               MoveHistory moveHistory) {
        EventTarget target = event.getTarget();

        if (target instanceof Node) {
            Node targetNode = (Node) target;
            if (targetNode instanceof Cube) {
                Cube targetCube = (Cube) targetNode;
                if (isField(board, targetCube)) {
                    Dimension2D position = getFieldPosition(board, targetCube);

                    if (isSamePosition(position, marked) || isSamePosition(position, highlight)) {
                        if(isSamePosition(position, marked) ) {
                            highlightBack(figures, marked, fieldWidth, moveHistory, currentBoard, highlight, board, highlightColor);
                            return null;
                        }
                        return highlight;
                    }

                    targetCube.highlightField(true, moveColor);
                    Figure targetFigure = getFigureForPosition(figures, position, fieldWidth);
                    if (targetFigure != null) {
                        targetFigure.highlightFigure(true, moveColor);
                    }

                    highlightBack(figures, marked, fieldWidth, moveHistory, currentBoard, highlight, board, highlightColor);

                    return position;
                }
            } else if (targetNode instanceof Figure) {
                Figure targetFigure = (Figure) targetNode;
                Dimension2D position = getFigurePosition(figures, targetFigure, fieldWidth);

                if (isSamePosition(position, marked) || isSamePosition(position, highlight)) {
                    if(isSamePosition(position, marked)) {
                        highlightBack(figures, marked, fieldWidth, moveHistory, currentBoard, highlight, board, highlightColor);
                        return null;
                    }
                    return highlight;
                }

                targetFigure.highlightFigure(true, moveColor);
                if (position != null) {
                    board[(int) position.width - 1][(int) position.height - 1].highlightField(true, moveColor);
                }

                highlightBack(figures, marked, fieldWidth, moveHistory, currentBoard, highlight, board, highlightColor);

                return position;
            }
        }

        return highlight;
    }

    private static void highlightBackForPosition(Dimension2D position, List<Figure> figures, float fieldWidth,
                                                 Cube[][] board, Color highlightColor) {
        if(position == null || position.width == 0 || position.height == 0) {
            return;
        }

        Figure targetFigure = getFigureForPosition(figures, position, fieldWidth);
        if (targetFigure != null) {
            targetFigure.highlightFigure(true, highlightColor);
        }
        board[(int) position.width - 1][(int) position.height - 1].highlightField(true, highlightColor);
    }

    private static void changeBackForPosition(Dimension2D position, List<Figure> figures, float fieldWidth,
                                              Cube[][] board) {
        if(position == null || position.width == 0 || position.height == 0) {
            return;
        }

        Figure targetFigure = getFigureForPosition(figures, position, fieldWidth);
        if (targetFigure != null) {
            targetFigure.changeMaterialToDefault();
            targetFigure.highlightFigure(false, shineColor);
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

    private static long getRelativePositionX(double positionX, float fieldWidth) {
        return Math.round(((positionX - (fieldWidth/2)) / fieldWidth) + 5);
    }

    private static long getRelativePositionY(double positionY, float fieldWidth) {
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

    private static void highlightBack(List<Figure> figures, Dimension2D marked, float fieldWidth, MoveHistory moveHistory,
                                      ChessBoardPositions currentBoard, Dimension2D highlight, Cube[][] board,
                                      Color highlightColor){
        Figure markedFigure = getFigureForPosition(figures, marked, fieldWidth);
        if (markedFigure != null) {
            List<Dimension2D> potentialMoves = CheckersMoveFactory.getMove(markedFigure.getChessFigure())
                    .potentialMoves(markedFigure.getChessSide(), moveHistory,
                            new Dimension2D(marked.width-1, marked.height-1), currentBoard);

            if(potentialMoves.stream().noneMatch(move -> highlight != null && move.height +1 == highlight.height
                    && move.width + 1 == highlight.width)) {
                changeBackForPosition(highlight, figures, fieldWidth, board);
            } else {
                highlightBackForPosition(highlight, figures, fieldWidth, board, highlightColor);
            }
        }
    }

    public static ChessFigure getBoardFigure(List<Figure> figures, Dimension2D position, float fieldWidth) {
        Figure targetFigure = getFigureForPosition(figures, position, fieldWidth);

        return targetFigure == null ? null : targetFigure.getChessFigure();
    }

    public static ChessSide getBoardSide(List<Figure> figures, Dimension2D position, float fieldWidth) {
        Figure targetFigure = getFigureForPosition(figures, position, fieldWidth);

        return targetFigure == null ? null : targetFigure.getChessSide();
    }
}

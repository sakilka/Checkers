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
import org.sample.checkers.config.game.GameSetup;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static org.sample.checkers.config.checkers.CheckersFigure.PAWN;
import static org.sample.checkers.config.checkers.CheckersFigure.QUEEN;
import static org.sample.checkers.config.checkers.CheckersSide.BLACK;
import static org.sample.checkers.config.checkers.CheckersSide.WHITE;
import static org.sample.checkers.config.chess.ChessFiguresPositions.getAbsolutePositionX;
import static org.sample.checkers.config.chess.ChessFiguresPositions.getAbsolutePositionY;
import static org.sample.checkers.config.game.GamePropertyUtil.getGameSetup;;

public class CheckersMoveUtil {

    private static Color shineColor = Color.rgb(229,206,0, 0.3);
    private static Color highlightColor = Color.rgb(62,177,90, 0.6);
    private static Color moveColor = Color.rgb(229,1,0, 0.8);

    private static final GameSetup gameSetup = getGameSetup();

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
                                    currentBoard, board);
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

                        highlightPotentialMoves(position, targetChessFigure.getCheckersFigure(),
                                targetChessFigure.getCheckersSide(), currentBoard, board);
                        return position;
                    }
                }
            }
            return marked;
        } else {
            CheckersFigureModel targetCheckersFigure = getFigureForPosition(checkersFigures, marked, fieldWidth);

            if (targetCheckersFigure != null && highlight != null && highlight.width != 0 && highlight.height != 0) {
                List<Dimension2D> potentialMoves = CheckersMoveFactory.getMove(targetCheckersFigure.getCheckersFigure())
                        .potentialMoves(targetCheckersFigure.getCheckersSide(), new Dimension2D(marked.width-1, marked.height-1), currentBoard);

                if(potentialMoves.stream().anyMatch(position -> (position.width +1) == highlight.width
                        && (position.height + 1) == highlight.height)) {
                    jumpFigure(targetCheckersFigure, checkersMoveHistory, highlight, checkersFigures, fieldWidth, boardSceneGroup, currentBoard);
                    promoteFigure(targetCheckersFigure, highlight, checkersFigures, fieldWidth, boardSceneGroup);
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
                                                CheckersBoardPositions currentBoard, Cube[][] board) {

        List<Dimension2D> potentialMoves = CheckersMoveFactory.getMove(checkersFigure)
                .potentialMoves(checkersSide, new Dimension2D(position.width-1, position.height-1),
                        currentBoard);

        for(Dimension2D potentialMove : potentialMoves) {
            board[(int) potentialMove.width][(int) potentialMove.height].highlightField(true, highlightColor);
        }
    }

    private static void promoteFigure(CheckersFigureModel targetChessFigure, Dimension2D highlight,
                                      List<CheckersFigureModel> checkersFigures, float fieldWidth, Group boardSceneGroup){
        if(targetChessFigure.getCheckersFigure() == PAWN) {
            if(highlight.height == 8 && targetChessFigure.getCheckersSide() == WHITE) {
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
                CheckersFigure checkersFigure = QUEEN;
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

    private static void jumpFigure(CheckersFigureModel targetCheckersFigure, CheckersMoveHistory checkersMoveHistory,
                                   Dimension2D highlight, List<CheckersFigureModel> checkersFigures, float fieldWidth,
                                   Group boardSceneGroup, CheckersBoardPositions currentBoard){
        if(targetCheckersFigure.getCheckersFigure() == PAWN) {
            Dimension2D position = getFigurePosition(checkersFigures, targetCheckersFigure, fieldWidth);
            if (Math.abs(position.width - highlight.width) == 2 && Math.abs(position.height - highlight.height) == 2) {
                Dimension2D jumpPosition = new Dimension2D((position.width + ((highlight.width - position.width)/2)),
                        (position.height + ((highlight.height - position.height)/2)));
                CheckersFigureModel jumped = getFigureForPosition(checkersFigures, jumpPosition, fieldWidth);
                boardSceneGroup.getChildren().remove(jumped);
                checkersFigures.remove(jumped);

                CheckersBoardPositions potentialBoard = checkersMoveHistory
                        .getCurrentBoardFromHistoryAndWithMove(new CheckersMovePosition(highlight,position));

                List<Dimension2D> nextMoves = CheckersMoveFactory.getMove(PAWN)
                        .potentialMoves(targetCheckersFigure.getCheckersSide(), new Dimension2D(highlight.width-1,
                                highlight.height-1), potentialBoard);
                if(nextMoves.size() > 0) {
                    for (Dimension2D potentialJump : nextMoves) {
                        if (Math.abs(highlight.width - (potentialJump.width + 1)) == 2
                                && Math.abs(highlight.height - (potentialJump.height+1)) == 2) {
                            checkersMoveHistory.setOnMove(targetCheckersFigure.getCheckersSide().oposite());
                        }
                    }
                }
            }
        } else {
            Dimension2D position = getFigurePosition(checkersFigures, targetCheckersFigure, fieldWidth);
            if (currentQueenJump(position, highlight, currentBoard, targetCheckersFigure.getCheckersSide())) {
                Dimension2D jumpPosition = getCurrentQueenJumpPosition(position, highlight, currentBoard,
                        targetCheckersFigure.getCheckersSide());
                CheckersFigureModel jumped = getFigureForPosition(checkersFigures, jumpPosition, fieldWidth);
                boardSceneGroup.getChildren().remove(jumped);
                checkersFigures.remove(jumped);

                CheckersBoardPositions potentialBoard = checkersMoveHistory
                        .getCurrentBoardFromHistoryAndWithMove(new CheckersMovePosition(highlight,position));

                List<Dimension2D> nextMoves = CheckersMoveFactory.getMove(QUEEN)
                        .potentialMoves(targetCheckersFigure.getCheckersSide(), new Dimension2D(highlight.width-1,
                                highlight.height-1), potentialBoard);
                if(nextMoves.size() > 0) {
                    for (Dimension2D potentialJump : nextMoves) {
                        if (currentQueenJump(highlight, potentialJump, potentialBoard, targetCheckersFigure.getCheckersSide())) {
                            checkersMoveHistory.setOnMove(targetCheckersFigure.getCheckersSide().oposite());
                        }
                    }
                }
            }
        }
    }

//    //TODo remove
//    private static void printChessSides(CheckersBoardPositions potentialBoard) {
//        for(int height = 7; height>=0; height--) {
//            for(int width=0; width<8; width++) {
//                System.out.printf("%1$7s",potentialBoard.getSides()[width][height]);
//            }
//            System.out.println();
//        }
//    }
//
//    //TODo remove
//    private static void printChessFigures(CheckersBoardPositions potentialBoard) {
//        for(int height = 7; height>=0; height--) {
//            for(int width=0; width<8; width++) {
//                System.out.printf("%1$7s",potentialBoard.getPositions()[width][height]);
//            }
//            System.out.println();
//        }
//    }

    private static boolean currentQueenJump(Dimension2D current, Dimension2D target, CheckersBoardPositions currentBoard,
                                            CheckersSide side) {
        int widthDirection = current.width > target.width ? -1 : 1;
        int heightDirection = current.height > target.height ? -1 : 1;

        for(int shift = 1; (current.width + (widthDirection * shift) <= 8
                && current.width + (widthDirection * shift) > 0
                && current.height + (heightDirection * shift) <= 8
                && current.height + (heightDirection * shift) > 0); shift++) {
            if(currentBoard.getSides()[(int) (current.width + (widthDirection * shift)) -1]
                    [(int) (current.height + (heightDirection * shift)) - 1] == side.oposite()){
                return true;
            }
        }

        return false;
    }

    private static Dimension2D getCurrentQueenJumpPosition(Dimension2D current, Dimension2D target, CheckersBoardPositions currentBoard,
                                            CheckersSide side) {
        int widthDirection = current.width > target.width ? -1 : 1;
        int heightDirection = current.height > target.height ? -1 : 1;

        for(int shift = 1; (current.width + (widthDirection * shift) <= 8
                && current.width + (widthDirection * shift) > 0
                && current.height + (heightDirection * shift) <= 8
                && current.height + (heightDirection * shift) > 0); shift++) {
            if(currentBoard.getSides()[(int) (current.width + (widthDirection * shift)) - 1]
                    [(int) (current.height + (heightDirection * shift)) - 1] == side.oposite()){
                return new Dimension2D((int) (current.width + (widthDirection * shift)),
                        (int) (current.height + (heightDirection * shift)));
            }
        }

        return null;
    }

    private static void moveFigure(CheckersFigureModel targetChessFigure, Dimension2D highlight, float fieldWidth) {
        Timeline animation = createTimeline(
                new Point3D(getAbsolutePositionX((int) highlight.width, fieldWidth), 0,
                        getAbsolutePositionY((int) highlight.height, fieldWidth)),
                targetChessFigure);
        animation.play();
    }

    private static Timeline createTimeline(Point3D p2, Node figure) {
        Timeline t = new Timeline();
        KeyValue keyX = new KeyValue(figure.translateXProperty(), p2.getX());
        KeyValue keyY = new KeyValue(figure.translateYProperty(), p2.getY());
        KeyValue keyZ = new KeyValue(figure.translateZProperty(), p2.getZ());
        KeyFrame keyFrame = new KeyFrame(gameSetup.getAnimationDuration(), keyX, keyY, keyZ);
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
                    .potentialMoves(markedChessFigure.getCheckersSide(), new Dimension2D(marked.width-1, marked.height-1), currentBoard);

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

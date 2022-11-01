package org.sample.checkers.chess;

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
import org.sample.checkers.config.chess.*;
import org.sample.checkers.config.game.GameSetup;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import static org.sample.checkers.config.chess.ChessFigure.KING;
import static org.sample.checkers.config.chess.ChessFigure.PAWN;
import static org.sample.checkers.config.chess.ChessFiguresPositions.getAbsolutePositionX;
import static org.sample.checkers.config.chess.ChessFiguresPositions.getAbsolutePositionY;
import static org.sample.checkers.config.chess.ChessSide.BLACK;
import static org.sample.checkers.config.chess.ChessSide.WHITE;
import static org.sample.checkers.config.game.GamePropertyUtil.getGameSetup;

public class ChessMoveUtil {

    private static Color shineColor = Color.rgb(229,206,0, 0.3);
    private static Color highlightColor = Color.rgb(62,177,90, 0.6);
    private static Color moveColor = Color.rgb(229,1,0, 0.8);
    private static final GameSetup gameSetup = getGameSetup();

    public static Dimension2D handlePrimaryClick(MouseEvent event, Cube[][] board, List<ChessFigureModel> chessFigureModels, float fieldWidth,
                                                 Dimension2D marked, Dimension2D highlight, ChessBoardPositions currentBoard,
                                                 ChessMoveHistory chessMoveHistory, Group boardSceneGroup, Stage mainStage) {
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
                        ChessFigureModel targetChessFigureModel = getFigureForPosition(chessFigureModels, position, fieldWidth);

                        if (targetChessFigureModel != null && targetChessFigureModel.getChessSide() == chessMoveHistory.getOnMove()) {
                            targetCube.highlightField(true, shineColor);
                            targetChessFigureModel.highlightFigure(true, shineColor);
                            highlightPotentialMoves(position, targetChessFigureModel.getChessFigure(), targetChessFigureModel.getChessSide(),
                                    currentBoard, board, chessMoveHistory);
                            return position;
                        }
                    }
                } else if (targetNode instanceof ChessFigureModel) {
                    ChessFigureModel targetChessFigureModel = (ChessFigureModel) targetNode;

                    if(targetChessFigureModel.getChessSide() == chessMoveHistory.getOnMove()) {

                        Dimension2D position = getFigurePosition(chessFigureModels, targetChessFigureModel, fieldWidth);

                        if (position != null) {
                            targetChessFigureModel.highlightFigure(true, shineColor);
                            board[(int) position.width - 1][(int) position.height - 1].highlightField(true, shineColor);
                        }

                        highlightPotentialMoves(position, targetChessFigureModel.getChessFigure(), targetChessFigureModel.getChessSide(),
                                currentBoard, board, chessMoveHistory);
                        return position;
                    }
                }
            }
            return marked;
        } else {
            ChessFigureModel targetChessFigureModel = getFigureForPosition(chessFigureModels, marked, fieldWidth);

            if (targetChessFigureModel != null && highlight != null && highlight.width != 0 && highlight.height != 0) {
                List<Dimension2D> potentialMoves = ChessMoveFactory.getMove(targetChessFigureModel.getChessFigure())
                        .potentialMoves(targetChessFigureModel.getChessSide(), chessMoveHistory,
                                new Dimension2D(marked.width-1, marked.height-1), currentBoard);

                if(potentialMoves.stream().anyMatch(position -> (position.width +1) == highlight.width
                        && (position.height + 1) == highlight.height)) {
                    promoteFigure(targetChessFigureModel, currentBoard, highlight, chessFigureModels, fieldWidth, mainStage, boardSceneGroup);
                    captureFigure(currentBoard, highlight, chessFigureModels, fieldWidth, boardSceneGroup);
                    moveFigure(targetChessFigureModel, highlight, fieldWidth);
                    chessMoveHistory.addMove(new ChessMovePosition(highlight, marked));
                    castling(targetChessFigureModel, currentBoard, highlight, chessFigureModels, fieldWidth, mainStage, boardSceneGroup,
                            chessMoveHistory);
                    enPassant(targetChessFigureModel, currentBoard, highlight, chessFigureModels, fieldWidth, mainStage, boardSceneGroup,
                            chessMoveHistory);
                    checkAndCheckmate(chessMoveHistory, mainStage);
                }
            }
            changeBackAll(chessFigureModels, fieldWidth, board);
            return null;
        }
    }

    private static void changeBackAll(List<ChessFigureModel> chessFigureModels, float fieldWidth, Cube[][] board) {
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                changeBackForPosition(new Dimension2D(i+1,j+1), chessFigureModels, fieldWidth, board);
            }
        }
    }

    private static void highlightPotentialMoves(Dimension2D position, org.sample.checkers.config.chess.ChessFigure chessFigure, ChessSide chessSide,
                                                ChessBoardPositions currentBoard, Cube[][] board, ChessMoveHistory chessMoveHistory) {

        List<Dimension2D> potentialMoves = ChessMoveFactory.getMove(chessFigure)
                .potentialMoves(chessSide, chessMoveHistory,  new Dimension2D(position.width-1, position.height-1),
                        currentBoard);

        for(Dimension2D potentialMove : potentialMoves) {
            board[(int) potentialMove.width][(int) potentialMove.height].highlightField(true, highlightColor);
        }
    }

    private static void promoteFigure(ChessFigureModel targetChessFigureModel, ChessBoardPositions currentBoard, Dimension2D highlight,
                                      List<ChessFigureModel> chessFigureModels, float fieldWidth, Stage mainStage, Group boardSceneGroup){
        if(targetChessFigureModel.getChessFigure() == PAWN) {
            if(highlight.height == 8 && targetChessFigureModel.getChessSide() == WHITE) {
                Optional<org.sample.checkers.config.chess.ChessFigure> promote = new PromotionDialog(mainStage).showDialog();
                org.sample.checkers.config.chess.ChessFigure chessFigure = promote.orElse(org.sample.checkers.config.chess.ChessFigure.QUEEN);
                PhongMaterial promotedMaterial = (PhongMaterial) targetChessFigureModel.getMaterial();
                boardSceneGroup.getChildren().remove(targetChessFigureModel);
                chessFigureModels.remove(targetChessFigureModel);
                ChessFigureModel promotedChessFigureModel = new ChessFigureModel(chessFigure.name().toLowerCase(Locale.ROOT),
                        new FigurePosition(
                        getAbsolutePositionX((int) highlight.width, fieldWidth), 0,
                        getAbsolutePositionY((int) highlight.height, fieldWidth)),
                        promotedMaterial, chessFigure, WHITE);
                boardSceneGroup.getChildren().add(promotedChessFigureModel);
                chessFigureModels.add(promotedChessFigureModel);
            }

            if(highlight.height == 1 && targetChessFigureModel.getChessSide() == BLACK) {
                Optional<org.sample.checkers.config.chess.ChessFigure> promote = new PromotionDialog(mainStage).showDialog();
                org.sample.checkers.config.chess.ChessFigure chessFigure = promote.orElse(org.sample.checkers.config.chess.ChessFigure.QUEEN);
                PhongMaterial promotedMaterial = (PhongMaterial) targetChessFigureModel.getMaterial();
                boardSceneGroup.getChildren().remove(targetChessFigureModel);
                chessFigureModels.remove(targetChessFigureModel);
                ChessFigureModel promotedChessFigureModel = new ChessFigureModel(chessFigure.name().toLowerCase(Locale.ROOT),
                        new FigurePosition(
                                getAbsolutePositionX((int) highlight.width, fieldWidth), 0,
                                getAbsolutePositionY((int) highlight.height, fieldWidth)),
                        promotedMaterial, chessFigure, BLACK);
                boardSceneGroup.getChildren().add(promotedChessFigureModel);
                chessFigureModels.add(promotedChessFigureModel);
            }
        }
    }

    private static void captureFigure(ChessBoardPositions currentBoard, Dimension2D highlight, List<ChessFigureModel> chessFigureModels,
            float fieldWidth, Group boardSceneGroup){
        if(currentBoard.getPositions()[(int) highlight.width - 1][(int)highlight.height - 1] != null) {
            ChessFigureModel captured = getFigureForPosition(chessFigureModels, highlight, fieldWidth);
            boardSceneGroup.getChildren().remove(captured);
            chessFigureModels.remove(captured);
        }
    }

    private static void moveFigure(ChessFigureModel targetChessFigureModel, Dimension2D highlight, float fieldWidth) {
        Timeline animation = createTimeline(
                new Point3D(targetChessFigureModel.getTranslateX(), targetChessFigureModel.getTranslateY(), targetChessFigureModel.getTranslateZ()),
                new Point3D(getAbsolutePositionX((int) highlight.width, fieldWidth), 0, getAbsolutePositionY((int) highlight.height, fieldWidth)),
                targetChessFigureModel);

        if(gameSetup.getPlayTimeline() != null) {
            Timeline runningTimeline = gameSetup.getPlayTimeline();
            runningTimeline.setOnFinished(event -> {
                animation.play();
            });
        } else {
            animation.play();
        }

        animation.setOnFinished(event -> gameSetup.setMoveFigure(false));
        gameSetup.setMoveFigure(true);
        gameSetup.setPlayTimeline(animation);

        animation.setOnFinished(event -> gameSetup.setMoveFigure(false));
        gameSetup.setMoveFigure(true);
        gameSetup.setPlayTimeline(animation);
    }

    private static void castling(ChessFigureModel targetChessFigureModel, ChessBoardPositions currentBoard, Dimension2D highlight,
                                 List<ChessFigureModel> chessFigureModels, float fieldWidth, Stage mainStage, Group boardSceneGroup,
                                 ChessMoveHistory chessMoveHistory) {
        if(targetChessFigureModel.getChessFigure() == KING &&
                ((targetChessFigureModel.getChessSide() == WHITE && !chessMoveHistory.isCastlingWhiteDone()) ||
                        (targetChessFigureModel.getChessSide() == BLACK && !chessMoveHistory.isCastlingBlackDone()))) {

            long x = getRelativePositionX(targetChessFigureModel.getTranslateX(), fieldWidth);
            ChessSide side = targetChessFigureModel.getChessSide();

            if(Math.abs(highlight.width - x) > 1) {
                if(highlight.width == 3){
                    Dimension2D position = side == WHITE ? new Dimension2D(1,1) : new Dimension2D(1,8);
                    Dimension2D target = side == WHITE ? new Dimension2D(4,1) : new Dimension2D(4,8);
                    ChessFigureModel castleChessFigureModel = getFigureForPosition(chessFigureModels, position, fieldWidth);

                    moveFigure(castleChessFigureModel, target, fieldWidth);
                    chessMoveHistory.addMove(new ChessMovePosition(target, position));
                } else {
                    Dimension2D position = side == WHITE ? new Dimension2D(8,1) : new Dimension2D(8,8);
                    Dimension2D target = side == WHITE ? new Dimension2D(6,1) : new Dimension2D(6,8);
                    ChessFigureModel castleChessFigureModel = getFigureForPosition(chessFigureModels, position, fieldWidth);

                    moveFigure(castleChessFigureModel, target, fieldWidth);
                    chessMoveHistory.addMove(new ChessMovePosition(target, position));
                }
            }

            if(side == WHITE) {
                chessMoveHistory.setCastlingWhiteDone(true);
                chessMoveHistory.setOnMove(BLACK);
            } else {
                chessMoveHistory.setCastlingBlackDone(true);
                chessMoveHistory.setOnMove(WHITE);
            }
        }
    }

    private static void enPassant(ChessFigureModel targetChessFigureModel, ChessBoardPositions currentBoard, Dimension2D highlight,
                                  List<ChessFigureModel> chessFigureModels, float fieldWidth, Stage mainStage, Group boardSceneGroup,
                                  ChessMoveHistory chessMoveHistory) {
        if(targetChessFigureModel.getChessFigure() == PAWN) {
            ChessMovePosition lastMove = chessMoveHistory.getMoves().get(chessMoveHistory.getMoves().size() -1);

            if(Math.abs(lastMove.getPosition().width - lastMove.getPreviousPosition().width) == 1
            && Math.abs(lastMove.getPosition().height - lastMove.getPreviousPosition().height) == 1){
                if(currentBoard.getPositions()[(int) highlight.width - 1][(int)highlight.height - 1] == null) {
                    captureFigure(currentBoard, new Dimension2D(lastMove.getPosition().width,
                            lastMove.getPreviousPosition().height), chessFigureModels, fieldWidth, boardSceneGroup);
                }
            }
        }
    }

    //Kontrola a mat
    //Keď je kráľ pod bezprostredným útokom, hovorí sa, že je pod kontrolou.
    // Ťah v reakcii na kontrolu je legálny iba vtedy, ak vedie k pozícii, v ktorej kráľ už nie je v šachu.
    // To môže zahŕňať zachytenie kontrolného kusu;
    // vloženie figúry medzi kontrolnú figúrku a kráľa
    // (čo je možné len vtedy, ak je útočiacou figúrkou dáma, veža alebo strelec a medzi ňou a kráľom je pole);
    // alebo presunúť kráľa na pole, kde nie je napadnutý. Rošáda nie je prípustnou odpoveďou na šek.[1]
    //
    //Cieľom hry je dať súperovi mat; k tomu dochádza, keď je súperov kráľ v šachu a neexistuje žiadny legálny spôsob,
    // ako ho dostať mimo kontrolu. Nikdy nie je legálne, aby hráč urobil ťah,
    // ktorý postaví alebo nechá vlastného kráľa hráča v šachu.
    // V príležitostných hrách je bežné oznámiť „check“ pri uvedení súperovho kráľa do šachu,
    // ale pravidlá šachu to nevyžadujú a na turnajoch sa to zvyčajne nerobí.[2]
    private static void checkAndCheckmate(ChessMoveHistory chessMoveHistory, Stage mainStage) {
        ChessBoardPositions currentBoard = chessMoveHistory.getCurrentBoardFromHistory();

        boolean check = false;
        boolean checkmate = false;
        ChessSide side = BLACK;

        for(int width = 0; width <8 ; width++) {
            for (int heigth = 0; heigth < 8; heigth++) {
                if(currentBoard.getPositions()[width][heigth] == KING){
                    ChessSide kingSide = currentBoard.getSides()[width][heigth];

                    if(checkmate(kingSide, currentBoard, chessMoveHistory, new Dimension2D(width, heigth))) {
                        checkmate = true;
                        side = kingSide;
                    }

                    if (check(kingSide, currentBoard, chessMoveHistory, new Dimension2D(width, heigth))) {
                        check = true;
                        side = kingSide;
                    }
                }
            }
        }

        if(checkmate) {
            new CheckDialog(mainStage, "Checkmate " + side).showDialog();
        } else if (check) {
            new CheckDialog(mainStage, "Check " + side).showDialog();
        }
    }

    private static boolean check(ChessSide side, ChessBoardPositions currentBoard, ChessMoveHistory chessMoveHistory,
                                 Dimension2D currentPosition){
        for(int width = 0; width <8 ; width++) {
            for (int heigth = 0; heigth <8; heigth++) {
                if(side.oposite() == currentBoard.getSides()[width][heigth]) {
                    org.sample.checkers.config.chess.ChessFigure chessFigure = currentBoard.getPositions()[width][heigth];
                    Dimension2D checkPosition = new Dimension2D(width, heigth);

                    if(chessFigure == org.sample.checkers.config.chess.ChessFigure.KING && kingInStartPosition(width, heigth, side.oposite()) ){
                        continue;
                    }

                    List<Dimension2D> potentialMoves = ChessMoveFactory
                            .getMove(chessFigure).potentialMoves(side.oposite(), chessMoveHistory, checkPosition, currentBoard);
                    if(potentialMoves.stream().anyMatch(move -> move.width == currentPosition.width &&
                            move.height == currentPosition.height)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static boolean checkmate(ChessSide side, ChessBoardPositions currentBoard, ChessMoveHistory chessMoveHistory,
                                 Dimension2D currentPosition){
        for(int width = 0; width <8 ; width++) {
            for (int heigth = 0; heigth <8; heigth++) {
                if(side.oposite() == currentBoard.getSides()[width][heigth]) {
                    org.sample.checkers.config.chess.ChessFigure chessFigure = currentBoard.getPositions()[width][heigth];
                    Dimension2D checkPosition = new Dimension2D(width, heigth);

                    if(chessFigure == org.sample.checkers.config.chess.ChessFigure.KING && kingInStartPosition(width, heigth, side.oposite()) ){
                        continue;
                    }

                    List<Dimension2D> potentialMoves = ChessMoveFactory
                            .getMove(chessFigure).potentialMoves(side.oposite(), chessMoveHistory, checkPosition, currentBoard);
                    if(potentialMoves.stream().anyMatch(move -> move.width == currentPosition.width &&
                            move.height == currentPosition.height)) {

                        if(!existTurnFromCheck(side.oposite(), currentBoard, chessMoveHistory, currentPosition)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private static boolean existTurnFromCheck(ChessSide side, ChessBoardPositions currentBoard,
                                              ChessMoveHistory chessMoveHistory, Dimension2D kingPosition) {
        for(int width = 0; width <8 ; width++) {
            for (int heigth = 0; heigth <8; heigth++) {
                if(side.oposite() == currentBoard.getSides()[width][heigth]) {
                    org.sample.checkers.config.chess.ChessFigure chessFigure = currentBoard.getPositions()[width][heigth];
                    Dimension2D helpPosition = new Dimension2D(width, heigth);

                    List<Dimension2D> potentialMoves = ChessMoveFactory
                            .getMove(chessFigure).potentialMoves(side.oposite(), chessMoveHistory, helpPosition, currentBoard);

                    for(Dimension2D potentialMove : potentialMoves) {
                        ChessBoardPositions potentialBoard = chessMoveHistory
                                .getCurrentBoardFromHistoryAndWithMove(new ChessMovePosition(new Dimension2D(width +1, heigth +1),
                                new Dimension2D(potentialMove.width +1, potentialMove.height +1)));
                        if(!stillInCheck(side.oposite(), potentialBoard, chessMoveHistory, kingPosition)){
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private static boolean stillInCheck(ChessSide side, ChessBoardPositions currentBoard, ChessMoveHistory chessMoveHistory,
                                        Dimension2D kingPosition) {
        for(int width = 0; width <8 ; width++) {
            for (int heigth = 0; heigth <8; heigth++) {
                if(side.oposite() == currentBoard.getSides()[width][heigth]) {
                    org.sample.checkers.config.chess.ChessFigure chessFigure = currentBoard.getPositions()[width][heigth];
                    Dimension2D checkPosition = new Dimension2D(width, heigth);

                    if(chessFigure == org.sample.checkers.config.chess.ChessFigure.KING && kingInStartPosition(width, heigth, side.oposite()) ){
                        continue;
                    }

                    List<Dimension2D> potentialMoves = ChessMoveFactory
                            .getMove(chessFigure).potentialMoves(side.oposite(), chessMoveHistory, checkPosition, currentBoard);
                    if(potentialMoves.stream().anyMatch(move -> move.width == kingPosition.width &&
                            move.height == kingPosition.height)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static boolean kingInStartPosition(int width, int height, ChessSide side) {
        if(side == BLACK){
            return width == 4 && height == 7;
        } else {
            return width == 4 && height == 0;
        }
    }

    private static Timeline createTimeline(Point3D p1, Point3D p2, Node figure) {
        Timeline t = new Timeline();
        KeyValue keyX = new KeyValue(figure.translateXProperty(), p2.getX());
        KeyValue keyY = new KeyValue(figure.translateYProperty(), p2.getY());
        KeyValue keyZ = new KeyValue(figure.translateZProperty(), p2.getZ());
        KeyFrame keyFrame = new KeyFrame(gameSetup.getAnimationDuration(), keyX, keyY, keyZ);
        t.getKeyFrames().add(keyFrame);
        return t;
    }

    public static Dimension2D handleMarkedMove(MouseEvent event, Cube[][] board, List<ChessFigureModel> chessFigureModels, float fieldWidth,
                                               Dimension2D marked, Dimension2D highlight, ChessBoardPositions currentBoard,
                                               ChessMoveHistory chessMoveHistory) {
        EventTarget target = event.getTarget();

        if (target instanceof Node) {
            Node targetNode = (Node) target;
            if (targetNode instanceof Cube) {
                Cube targetCube = (Cube) targetNode;
                if (isField(board, targetCube)) {
                    Dimension2D position = getFieldPosition(board, targetCube);

                    if (isSamePosition(position, marked) || isSamePosition(position, highlight)) {
                        if(isSamePosition(position, marked) ) {
                            highlightBack(chessFigureModels, marked, fieldWidth, chessMoveHistory, currentBoard, highlight, board, highlightColor);
                            return null;
                        }
                        return highlight;
                    }

                    targetCube.highlightField(true, moveColor);
                    ChessFigureModel targetChessFigureModel = getFigureForPosition(chessFigureModels, position, fieldWidth);
                    if (targetChessFigureModel != null) {
                        targetChessFigureModel.highlightFigure(true, moveColor);
                    }

                    highlightBack(chessFigureModels, marked, fieldWidth, chessMoveHistory, currentBoard, highlight, board, highlightColor);

                    return position;
                }
            } else if (targetNode instanceof ChessFigureModel) {
                ChessFigureModel targetChessFigureModel = (ChessFigureModel) targetNode;
                Dimension2D position = getFigurePosition(chessFigureModels, targetChessFigureModel, fieldWidth);

                if (isSamePosition(position, marked) || isSamePosition(position, highlight)) {
                    if(isSamePosition(position, marked)) {
                        highlightBack(chessFigureModels, marked, fieldWidth, chessMoveHistory, currentBoard, highlight, board, highlightColor);
                        return null;
                    }
                    return highlight;
                }

                targetChessFigureModel.highlightFigure(true, moveColor);
                if (position != null) {
                    board[(int) position.width - 1][(int) position.height - 1].highlightField(true, moveColor);
                }

                highlightBack(chessFigureModels, marked, fieldWidth, chessMoveHistory, currentBoard, highlight, board, highlightColor);

                return position;
            }
        }

        return highlight;
    }

    private static void highlightBackForPosition(Dimension2D position, List<ChessFigureModel> chessFigureModels, float fieldWidth,
                                                 Cube[][] board, Color highlightColor) {
        if(position == null || position.width == 0 || position.height == 0) {
            return;
        }

        ChessFigureModel targetChessFigureModel = getFigureForPosition(chessFigureModels, position, fieldWidth);
        if (targetChessFigureModel != null) {
            targetChessFigureModel.highlightFigure(true, highlightColor);
        }
        board[(int) position.width - 1][(int) position.height - 1].highlightField(true, highlightColor);
    }

    private static void changeBackForPosition(Dimension2D position, List<ChessFigureModel> chessFigureModels, float fieldWidth,
                                              Cube[][] board) {
        if(position == null || position.width == 0 || position.height == 0) {
            return;
        }

        ChessFigureModel targetChessFigureModel = getFigureForPosition(chessFigureModels, position, fieldWidth);
        if (targetChessFigureModel != null) {
            targetChessFigureModel.changeMaterialToDefault();
            targetChessFigureModel.highlightFigure(false, shineColor);
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

    private static Dimension2D getFigurePosition(List<ChessFigureModel> chessFigureModels, ChessFigureModel targetChessFigureModel, float fieldWidth) {

        for (ChessFigureModel chessFigureModel : chessFigureModels) {
            if(chessFigureModel.equals(targetChessFigureModel)) {
                long x = getRelativePositionX(targetChessFigureModel.getTranslateX(), fieldWidth);
                long y = getRelativePositionY(targetChessFigureModel.getTranslateZ(), fieldWidth);
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

    private static ChessFigureModel getFigureForPosition(List<ChessFigureModel> chessFigureModels, Dimension2D position, float fieldWidth) {

        for (ChessFigureModel chessFigureModel : chessFigureModels) {
            if(chessFigureModel.getTranslateX() == getAbsolutePositionX((int) position.width, fieldWidth)
            && chessFigureModel.getTranslateZ() == getAbsolutePositionY((int) position.height, fieldWidth)) {
                return chessFigureModel;
            }
        }

        return null;
    }

    private static void highlightBack(List<ChessFigureModel> chessFigureModels, Dimension2D marked, float fieldWidth, ChessMoveHistory chessMoveHistory,
                                      ChessBoardPositions currentBoard, Dimension2D highlight, Cube[][] board,
                                      Color highlightColor){
        ChessFigureModel markedChessFigureModel = getFigureForPosition(chessFigureModels, marked, fieldWidth);
        if (markedChessFigureModel != null) {
            List<Dimension2D> potentialMoves = ChessMoveFactory.getMove(markedChessFigureModel.getChessFigure())
                    .potentialMoves(markedChessFigureModel.getChessSide(), chessMoveHistory,
                            new Dimension2D(marked.width-1, marked.height-1), currentBoard);

            if(potentialMoves.stream().noneMatch(move -> highlight != null && move.height +1 == highlight.height
                    && move.width + 1 == highlight.width)) {
                changeBackForPosition(highlight, chessFigureModels, fieldWidth, board);
            } else {
                highlightBackForPosition(highlight, chessFigureModels, fieldWidth, board, highlightColor);
            }
        }
    }

    public static org.sample.checkers.config.chess.ChessFigure getBoardFigure(List<ChessFigureModel> chessFigureModels, Dimension2D position, float fieldWidth) {
        ChessFigureModel targetChessFigureModel = getFigureForPosition(chessFigureModels, position, fieldWidth);

        return targetChessFigureModel == null ? null : targetChessFigureModel.getChessFigure();
    }

    public static ChessSide getBoardSide(List<ChessFigureModel> chessFigureModels, Dimension2D position, float fieldWidth) {
        ChessFigureModel targetChessFigureModel = getFigureForPosition(chessFigureModels, position, fieldWidth);

        return targetChessFigureModel == null ? null : targetChessFigureModel.getChessSide();
    }

    public static ChessFigureModel getFigureForChessMove(ChessMovePosition movePosition, float fieldWidth,
                                                               List<ChessFigureModel> chessFigureModels) {
        Dimension2D position = movePosition.getPreviousPosition();

        for (ChessFigureModel figure : chessFigureModels) {
            long x = getRelativePositionX(figure.getTranslateX(), fieldWidth);
            long y = getRelativePositionY(figure.getTranslateZ(), fieldWidth);

            if(position.width == x && position.height == y) {
                return figure;
            }
        }

        throw new RuntimeException("Cannot find figure for move!");
    }
}

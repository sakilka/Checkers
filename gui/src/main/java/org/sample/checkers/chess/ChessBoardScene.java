package org.sample.checkers.chess;

import com.sun.javafx.geom.Dimension2D;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.sample.checkers.board.model.ChessBoard;
import org.sample.checkers.board.model.CubeFace;
import org.sample.checkers.board.model.CubeMaterial;
import org.sample.checkers.board.components.SmartGroup;
import org.sample.checkers.checkers.CheckersFigureModel;
import org.sample.checkers.chess.ui.ChessUi;
import org.sample.checkers.config.checkers.CheckersMovePosition;
import org.sample.checkers.config.chess.ChessBoardPositions;
import org.sample.checkers.config.chess.ChessMoveHistory;
import org.sample.checkers.config.chess.ChessMovePosition;
import org.sample.checkers.config.chess.ChessSide;
import org.sample.checkers.config.game.GameSetup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.StrictMath.round;
import static javafx.scene.transform.Rotate.Y_AXIS;
import static org.sample.checkers.chess.ChessMoveUtil.*;
import static org.sample.checkers.chess.ui.ChessContext.getUi;
import static org.sample.checkers.config.chess.ChessFigure.*;
import static org.sample.checkers.config.chess.ChessFiguresPositions.getAbsolutePositionX;
import static org.sample.checkers.config.chess.ChessFiguresPositions.getAbsolutePositionY;
import static org.sample.checkers.config.chess.ChessSide.BLACK;
import static org.sample.checkers.config.chess.ChessSide.WHITE;
import static org.sample.checkers.config.chess.ChessPropertyUtil.getPositions;
import static org.sample.checkers.config.game.GamePropertyUtil.getBoardConfig;
import static org.sample.checkers.config.game.GamePropertyUtil.getGameSetup;
import static org.sample.checkers.config.game.Player.SINGLE_PLAYER;

public class ChessBoardScene extends SubScene implements ChessBoard {

    private double anchorX;
    private double anchorY;

    private double anchorAngleX;
    private double anchorAngleY;

    private DoubleProperty angleX;
    private DoubleProperty angleY;

    private DoubleProperty translateZ;

    private double distX;
    private double distZ;

    private double anchorDistX;
    private double anchorDistZ;

    private final DoubleProperty deltaX;
    private final DoubleProperty deltaZ;

    private final Stage mainStage;
    private final Group boardSceneGroup;

    private final float fieldWidth;
    private final float fieldHeight;
    private final float fieldDepth;
    private final float fieldGap;
    private final float fieldGapShift;
    private final float widthShift;
    private final float depthShift;

    private Dimension2D marked;
    private Dimension2D highlight;

    private final Cube[][] board;
    private final List<ChessFigureModel> chessFigureModels;
    private ChessMoveHistory chessMoveHistory;

    private GameSetup gameSetup = getGameSetup();
    private ChessUi ui = getUi();

    public ChessBoardScene(Stage stage, SmartGroup root, double width, double height, boolean depthBuffer,
                           SceneAntialiasing antiAliasing, BoardPosition boardPosition) {
        super(root, width, height, depthBuffer, antiAliasing);

        this.angleX = new SimpleDoubleProperty(getBoardConfig().getAngleX());
        this.angleY = new SimpleDoubleProperty(getBoardConfig().getAngleY());
        this.anchorAngleX = 0;
        this.anchorAngleY = 0;
        this.anchorDistX = 0;
        this.anchorDistZ = 0;
        this.marked = null;
        this.deltaX = new SimpleDoubleProperty(getBoardConfig().getDeltaX());
        this.deltaZ = new SimpleDoubleProperty(getBoardConfig().getDeltaZ());
        this.translateZ = new SimpleDoubleProperty(getBoardConfig().getTranslateZ());
        this.fieldWidth = getBoardConfig().getFieldWidth();
        this.fieldHeight = getBoardConfig().getFieldHeight();
        this.fieldDepth = getBoardConfig().getFieldDepth();
        this.fieldGap = getBoardConfig().getFieldGap();
        this.fieldGapShift = getBoardConfig().getFieldGapShift();
        this.widthShift =  4 * fieldWidth;
        this.depthShift = 4 * fieldWidth;

        this.board = new Cube[8][8];
        this.chessFigureModels = new ArrayList<>();
        this.chessMoveHistory = new ChessMoveHistory(new ArrayList<>());

        mainStage = stage;
        boardSceneGroup = root;

        initCheckersBoard(boardPosition);
        initMouseControl(this);
    }

    @Override
    public void initCheckersBoard(BoardPosition boardPosition) {

        initializeBoard();
        initializeFigures();

        boardPosition.deltaXProperty().bindBidirectional(deltaX);
        boardPosition.deltaZProperty().bindBidirectional(deltaZ);
        boardPosition.angleXProperty().bindBidirectional(angleX);
        boardPosition.angleYProperty().bindBidirectional(angleY);
        boardPosition.translateZProperty().bindBidirectional(boardSceneGroup.translateZProperty());
        boardPosition.translateZProperty().bindBidirectional(translateZ);

        this.setCamera(initializeCamera());
        this.setFill(Color.SILVER);
    }

    private void initializeBoard() {
        PhongMaterial blackMaterial = new PhongMaterial(Color.BLACK);
        blackMaterial.setSpecularColor(Color.WHITE);
        blackMaterial.setSpecularPower(32);
        PhongMaterial borderMaterial = new PhongMaterial(Color.BLACK);
        PhongMaterial whiteMaterial = new PhongMaterial(Color.WHITE);
        whiteMaterial.setSpecularColor(Color.WHITE);
        whiteMaterial.setSpecularPower(32);
        PhongMaterial gapMaterial = new PhongMaterial(Color.WHITE);
        PhongMaterial fieldDown = new PhongMaterial(Color.BLACK);
        String borderLetters = "ABCDEFGH";
        String borderNumbers = "12345678";

        Cube[][][] fieldGaps = new Cube[8][8][4];
        Cube[][] boardBorder = new Cube[8][4];

        for (int fieldZ = 0; fieldZ < 8; fieldZ++) {

            PhongMaterial current = (fieldZ % 2) == 0 ? blackMaterial : whiteMaterial;

            for (int fieldX = 0; fieldX < 8; fieldX++) {

                Cube field = new Cube(fieldWidth - fieldGap, fieldHeight, fieldDepth - fieldGap);

                field.setTranslateX((fieldX * fieldWidth) - widthShift);
                field.setTranslateY(0);
                field.setTranslateZ((fieldZ * fieldDepth) - depthShift);

                field.setMaterial(current);
                field.setDefaultMaterial(current, current == whiteMaterial);
                board[fieldX][fieldZ] = field;

                current = current == whiteMaterial ? blackMaterial : whiteMaterial;

                Cube leftGap = new Cube(fieldGap / 2, fieldHeight - fieldGapShift, fieldDepth);
                leftGap.setTranslateX(((fieldX * fieldWidth) - fieldGap/2) - widthShift);
                leftGap.setTranslateY(fieldGapShift/2);
                leftGap.setTranslateZ(((fieldZ * fieldDepth) - fieldGap/2) - depthShift);
                leftGap.setMaterial(gapMaterial);
                fieldGaps[fieldX][fieldZ][0] = leftGap;

                Cube backGap = new Cube(fieldWidth, fieldHeight - fieldGapShift, fieldGap / 2);
                backGap.setTranslateX(((fieldX * fieldWidth)- fieldGap/2) - widthShift);
                backGap.setTranslateY(fieldGapShift/2);
                backGap.setTranslateZ(((fieldZ * fieldDepth) - fieldGap) - depthShift);
                backGap.setMaterial(gapMaterial);
                fieldGaps[fieldX][fieldZ][1] = backGap;

                Cube rightGap = new Cube(fieldGap / 2, fieldHeight - fieldGapShift, fieldDepth);
                rightGap.setTranslateX(((fieldX * fieldWidth) - fieldGap) - widthShift);
                rightGap.setTranslateY(fieldGapShift/2);
                rightGap.setTranslateZ(((fieldZ * fieldDepth) - fieldGap/2) - depthShift);
                rightGap.setMaterial(gapMaterial);
                fieldGaps[fieldX][fieldZ][2] = rightGap;

                Cube frontGap = new Cube(fieldWidth, fieldHeight - fieldGapShift, fieldGap / 2);
                frontGap.setTranslateX(((fieldX * fieldWidth) - fieldGap/2) - widthShift);
                frontGap.setTranslateY(fieldGapShift/2);
                frontGap.setTranslateZ(((fieldZ * fieldDepth) - fieldGap/2) - depthShift);
                frontGap.setMaterial(gapMaterial);
                fieldGaps[fieldX][fieldZ][3] = frontGap;
            }
        }

        for(int border = 0; border < 8; border++) {
            Cube frontBorder = new Cube(fieldWidth, fieldHeight, fieldDepth/2);
            frontBorder.setTranslateX((fieldWidth * border) - widthShift - fieldGap/2);
            frontBorder.setTranslateY(0);
            frontBorder.setTranslateZ((- fieldDepth/2) - depthShift - fieldGapShift);
            Text text = new Text("        " + borderLetters.charAt(border) + "        ");
            text.setStroke(Color.WHITE);
            text.setFill(Color.WHITE);
            text.setFont(Font.font(Font.getFamilies().get(50), FontWeight.SEMI_BOLD, 100));
            frontBorder
                    .setMaterial(new CubeMaterial(borderMaterial.getDiffuseColor(), text, CubeFace.UP))
                    .setMaterial(new CubeMaterial(borderMaterial.getDiffuseColor(), null, CubeFace.LEFT))
                    .setMaterial(new CubeMaterial(borderMaterial.getDiffuseColor(), null, CubeFace.RIGHT))
                    .setMaterial(new CubeMaterial(borderMaterial.getDiffuseColor(), null, CubeFace.FRONT))
                    .setMaterial(new CubeMaterial(borderMaterial.getDiffuseColor(), null, CubeFace.BACK))
                    .setMaterial(new CubeMaterial(fieldDown.getDiffuseColor(), null, CubeFace.DOWN))
                    .initMaterial();
            boardBorder[border][0] = frontBorder;
        }

        for(int border = 0; border < 8; border++) {
            Cube frontBorder = new Cube(fieldWidth, fieldHeight, fieldDepth/2);
            frontBorder.setTranslateX((fieldWidth * border) - widthShift - fieldGap/2);
            frontBorder.setTranslateY(0);
            frontBorder.setTranslateZ(depthShift + fieldGapShift);
            Text text = new Text("        " + borderLetters.charAt(border) + "        ");
            text.setStroke(Color.WHITE);
            text.setFill(Color.WHITE);
            text.setFont(Font.font(Font.getFamilies().get(50), FontWeight.SEMI_BOLD, 100));
            frontBorder
                    .setMaterial(new CubeMaterial(borderMaterial.getDiffuseColor(), text, CubeFace.UP))
                    .setMaterial(new CubeMaterial(borderMaterial.getDiffuseColor(), null, CubeFace.LEFT))
                    .setMaterial(new CubeMaterial(borderMaterial.getDiffuseColor(), null, CubeFace.RIGHT))
                    .setMaterial(new CubeMaterial(borderMaterial.getDiffuseColor(), null, CubeFace.FRONT))
                    .setMaterial(new CubeMaterial(borderMaterial.getDiffuseColor(), null, CubeFace.BACK))
                    .setMaterial(new CubeMaterial(fieldDown.getDiffuseColor(), null, CubeFace.DOWN))
                    .initMaterial();
            boardBorder[border][1] = frontBorder;
        }

        for(int border = 0; border < 8; border++) {
            Cube frontBorder = new Cube(fieldWidth/2, fieldHeight, fieldDepth);
            frontBorder.setTranslateX(-fieldWidth/2 - widthShift - fieldGap);
            frontBorder.setTranslateY(0);
            frontBorder.setTranslateZ((fieldDepth * border) - depthShift - fieldGap/2);
            Text text = new Text("\n   " + borderNumbers.charAt(border) + "   \n");
            text.setStroke(Color.WHITE);
            text.setFill(Color.WHITE);
            text.setFont(Font.font(Font.getFamilies().get(50), FontWeight.SEMI_BOLD, 100));
            frontBorder
                    .setMaterial(new CubeMaterial(borderMaterial.getDiffuseColor(), text, CubeFace.UP))
                    .setMaterial(new CubeMaterial(borderMaterial.getDiffuseColor(), null, CubeFace.LEFT))
                    .setMaterial(new CubeMaterial(borderMaterial.getDiffuseColor(), null, CubeFace.RIGHT))
                    .setMaterial(new CubeMaterial(borderMaterial.getDiffuseColor(), null, CubeFace.FRONT))
                    .setMaterial(new CubeMaterial(borderMaterial.getDiffuseColor(), null, CubeFace.BACK))
                    .setMaterial(new CubeMaterial(fieldDown.getDiffuseColor(), null, CubeFace.DOWN))
                    .initMaterial();
            boardBorder[border][2] = frontBorder;
        }

        for(int border = 0; border < 8; border++) {
            Cube frontBorder = new Cube(fieldWidth/2, fieldHeight, fieldDepth);
            frontBorder.setTranslateX(widthShift + fieldGap);
            frontBorder.setTranslateY(0);
            frontBorder.setTranslateZ((fieldDepth * border) - depthShift - fieldGap/2);
            Text text = new Text("\n   " + borderNumbers.charAt(border) + "   \n");
            text.setStroke(Color.WHITE);
            text.setFill(Color.WHITE);
            text.setFont(Font.font(Font.getFamilies().get(50), FontWeight.SEMI_BOLD, 100));
            frontBorder
                    .setMaterial(new CubeMaterial(borderMaterial.getDiffuseColor(), text, CubeFace.UP))
                    .setMaterial(new CubeMaterial(borderMaterial.getDiffuseColor(), null, CubeFace.LEFT))
                    .setMaterial(new CubeMaterial(borderMaterial.getDiffuseColor(), null, CubeFace.RIGHT))
                    .setMaterial(new CubeMaterial(borderMaterial.getDiffuseColor(), null, CubeFace.FRONT))
                    .setMaterial(new CubeMaterial(borderMaterial.getDiffuseColor(), null, CubeFace.BACK))
                    .setMaterial(new CubeMaterial(fieldDown.getDiffuseColor(), null, CubeFace.DOWN))
                    .initMaterial();
            boardBorder[border][3] = frontBorder;
        }

        for (int fieldZ = 0; fieldZ < 8; fieldZ++) {
            for (int fieldX = 0; fieldX < 8; fieldX++) {
                boardSceneGroup.getChildren().add(board[fieldX][fieldZ]);
                boardSceneGroup.getChildren().add(fieldGaps[fieldX][fieldZ][0]);
                boardSceneGroup.getChildren().add(fieldGaps[fieldX][fieldZ][1]);
                boardSceneGroup.getChildren().add(fieldGaps[fieldX][fieldZ][2]);
                boardSceneGroup.getChildren().add(fieldGaps[fieldX][fieldZ][3]);
            }
            boardSceneGroup.getChildren().add(boardBorder[fieldZ][0]);
            boardSceneGroup.getChildren().add(boardBorder[fieldZ][1]);
            boardSceneGroup.getChildren().add(boardBorder[fieldZ][2]);
            boardSceneGroup.getChildren().add(boardBorder[fieldZ][3]);
        }

        Box bottom = new Box((9 * fieldWidth) + fieldGap*2, fieldHeight/2, (9 * fieldDepth) + fieldGap*2);
        bottom.setTranslateX(0);
        bottom.setTranslateY(fieldHeight + fieldGap);
        bottom.setTranslateZ(0);
        bottom.setMaterial(blackMaterial);
        boardSceneGroup.getChildren().add(bottom);

        Cube corner1 = new Cube(fieldWidth/2, fieldHeight, fieldDepth/2);
        corner1.setMaterial(blackMaterial);
        corner1.setTranslateX(widthShift + fieldGap);
        corner1.setTranslateY(0);
        corner1.setTranslateZ((- fieldDepth/2) - depthShift - fieldGapShift);
        boardSceneGroup.getChildren().add(corner1);

        Cube corner11 = new Cube(fieldGap*2, fieldHeight, fieldDepth/2);
        corner11.setMaterial(blackMaterial);
        corner11.setTranslateX(widthShift-0.1);
        corner11.setTranslateY(0);
        corner11.setTranslateZ((- fieldDepth/2) - depthShift - fieldGapShift);
        boardSceneGroup.getChildren().add(corner11);

        Cube corner12 = new Cube(fieldWidth/2, fieldHeight, fieldGap);
        corner12.setMaterial(blackMaterial);
        corner12.setTranslateX(widthShift + fieldGap);
        corner12.setTranslateY(0);
        corner12.setTranslateZ(- depthShift - fieldGapShift);
        boardSceneGroup.getChildren().add(corner12);

        Cube corner2 = new Cube(fieldWidth/2, fieldHeight, fieldDepth/2);
        corner2.setMaterial(blackMaterial);
        corner2.setTranslateX(-fieldWidth/2 - widthShift - fieldGap);
        corner2.setTranslateY(0);
        corner2.setTranslateZ((- fieldDepth/2) - depthShift - fieldGapShift);
        boardSceneGroup.getChildren().add(corner2);

        Cube corner21 = new Cube(fieldGap*2, fieldHeight, fieldDepth/2);
        corner21.setMaterial(blackMaterial);
        corner21.setTranslateX(- widthShift - fieldGap);
        corner21.setTranslateY(0);
        corner21.setTranslateZ((- fieldDepth/2) - depthShift - fieldGapShift);
        boardSceneGroup.getChildren().add(corner21);

        Cube corner22 = new Cube(fieldWidth/2, fieldHeight, fieldGap);
        corner22.setMaterial(blackMaterial);
        corner22.setTranslateX(-fieldWidth/2 - widthShift - fieldGap);
        corner22.setTranslateY(0);
        corner22.setTranslateZ(- depthShift - fieldGapShift);
        boardSceneGroup.getChildren().add(corner22);

        Cube corner3 = new Cube(fieldWidth/2, fieldHeight, fieldDepth/2);
        corner3.setMaterial(blackMaterial);
        corner3.setTranslateX(widthShift + fieldGap);
        corner3.setTranslateY(0);
        corner3.setTranslateZ(depthShift + fieldGapShift);
        boardSceneGroup.getChildren().add(corner3);

        Cube corner31 = new Cube(fieldGap*2, fieldHeight, fieldDepth/2);
        corner31.setMaterial(blackMaterial);
        corner31.setTranslateX(- widthShift - fieldGap);
        corner31.setTranslateY(0);
        corner31.setTranslateZ(depthShift + fieldGapShift);
        boardSceneGroup.getChildren().add(corner31);

        Cube corner32 = new Cube(fieldWidth/2, fieldHeight, fieldGap*2);
        corner32.setMaterial(blackMaterial);
        corner32.setTranslateX(-fieldWidth/2 - widthShift - fieldGap);
        corner32.setTranslateY(0);
        corner32.setTranslateZ(depthShift - fieldGapShift);
        boardSceneGroup.getChildren().add(corner32);

        Cube corner4 = new Cube(fieldWidth/2, fieldHeight, fieldDepth/2);
        corner4.setMaterial(blackMaterial);
        corner4.setTranslateX(-fieldWidth/2 - widthShift - fieldGap);
        corner4.setTranslateY(0);
        corner4.setTranslateZ(depthShift + fieldGapShift);
        boardSceneGroup.getChildren().add(corner4);

        Cube corner41 = new Cube(fieldGap*2, fieldHeight, fieldDepth/2);
        corner41.setMaterial(blackMaterial);
        corner41.setTranslateX(widthShift - fieldGap);
        corner41.setTranslateY(0);
        corner41.setTranslateZ(depthShift + fieldGapShift);
        boardSceneGroup.getChildren().add(corner41);

        Cube corner42 = new Cube(fieldWidth/2, fieldHeight, fieldGap*2);
        corner42.setMaterial(blackMaterial);
        corner42.setTranslateX(widthShift + fieldGapShift);
        corner42.setTranslateY(0);
        corner42.setTranslateZ(depthShift - fieldGapShift);
        boardSceneGroup.getChildren().add(corner42);
    }

    private void initializeFigures() {
        //white
        Color whiteColor = Color.WHITE;

        ChessFigureModel whiteBishopFirst = new ChessFigureModel("bishop", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessWhiteBishopFirstY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessWhiteBishopFirstX(), fieldWidth)),
                new PhongMaterial(whiteColor), BISHOP, WHITE);
        ChessFigureModel whiteBishopSecond = new ChessFigureModel("bishop", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessWhiteBishopSecondY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessWhiteBishopSecondX(), fieldWidth)),
                new PhongMaterial(whiteColor), BISHOP, WHITE);
        ChessFigureModel whiteKnightFirst = new ChessFigureModel("knight", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessWhiteKnightFirstY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessWhiteKnightFirstX(), fieldWidth)),
                new PhongMaterial(whiteColor), KNIGHT, WHITE);
        ChessFigureModel whiteKnightSecond = new ChessFigureModel("knight", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessWhiteKnightSecondY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessWhiteKnightSecondX(), fieldWidth)),
                new PhongMaterial(whiteColor), KNIGHT, WHITE);
        ChessFigureModel whiteRookFirst = new ChessFigureModel("rook", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessWhiteRookFirstY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessWhiteRookFirstX(), fieldWidth)),
                new PhongMaterial(whiteColor), ROOK, WHITE);
        ChessFigureModel whiteRookSecond = new ChessFigureModel("rook", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessWhiteRookSecondY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessWhiteRookSecondX(), fieldWidth)),
                new PhongMaterial(whiteColor), ROOK, WHITE);
        ChessFigureModel whiteKing = new ChessFigureModel("king", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessWhiteKingY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessWhiteKingX(), fieldWidth)),
                new PhongMaterial(whiteColor), KING, WHITE);
        ChessFigureModel whiteQueen = new ChessFigureModel("queen", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessWhiteQueenY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessWhiteQueenX(), fieldWidth)),
                new PhongMaterial(whiteColor), QUEEN, WHITE);
        ChessFigureModel whitePawnOne = new ChessFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessWhitePawnFirstY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessWhitePawnFirstX(), fieldWidth)),
                new PhongMaterial(whiteColor), PAWN, WHITE);
        ChessFigureModel whitePawnTwo = new ChessFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessWhitePawnSecondY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessWhitePawnSecondX(), fieldWidth)),
                new PhongMaterial(whiteColor), PAWN, WHITE);
        ChessFigureModel whitePawnThree = new ChessFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessWhitePawnThirdY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessWhitePawnThirdX(), fieldWidth)),
                new PhongMaterial(whiteColor), PAWN, WHITE);
        ChessFigureModel whitePawnFour = new ChessFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessWhitePawnFourthY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessWhitePawnFourthX(), fieldWidth)),
                new PhongMaterial(whiteColor), PAWN, WHITE);
        ChessFigureModel whitePawnFive = new ChessFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessWhitePawnFifthY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessWhitePawnFifthX(), fieldWidth)),
                new PhongMaterial(whiteColor), PAWN, WHITE);
        ChessFigureModel whitePawnSix = new ChessFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessWhitePawnSixthY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessWhitePawnSixthX(), fieldWidth)),
                new PhongMaterial(whiteColor), PAWN, WHITE);
        ChessFigureModel whitePawnSeven = new ChessFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessWhitePawnSeventhY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessWhitePawnSeventhX(), fieldWidth)),
                new PhongMaterial(whiteColor), PAWN, WHITE);
        ChessFigureModel whitePawnEight = new ChessFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessWhitePawnEighthY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessWhitePawnEighthX(), fieldWidth)),
                new PhongMaterial(whiteColor), PAWN, WHITE);

        boardSceneGroup.getChildren().addAll(whiteBishopFirst, whiteBishopSecond, whiteKnightFirst, whiteKnightSecond,
                whiteRookFirst, whiteRookSecond, whiteKing, whiteQueen, whitePawnOne, whitePawnTwo, whitePawnThree,
                whitePawnFour, whitePawnFive, whitePawnSix, whitePawnSeven, whitePawnEight);

        chessFigureModels.addAll(Stream.of(whiteBishopFirst, whiteBishopSecond, whiteKnightFirst, whiteKnightSecond,
                whiteRookFirst, whiteRookSecond, whiteKing, whiteQueen, whitePawnOne, whitePawnTwo, whitePawnThree,
                whitePawnFour, whitePawnFive, whitePawnSix, whitePawnSeven, whitePawnEight).collect(Collectors.toList()));

        //black
        Color blackColor = Color.DIMGRAY;

        ChessFigureModel blackBishopFirst = new ChessFigureModel("bishop", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessBlackBishopFirstY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessBlackBishopFirstX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, BISHOP, BLACK);
        ChessFigureModel blackBishopSecond = new ChessFigureModel("bishop", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessBlackBishopSecondY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessBlackBishopSecondX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, BISHOP, BLACK);
        ChessFigureModel blackKnightFirst = new ChessFigureModel("knight", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessBlackKnightFirstY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessBlackKnightFirstX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, KNIGHT, BLACK);
        ChessFigureModel blackKnightSecond = new ChessFigureModel("knight", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessBlackKnightSecondY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessBlackKnightSecondX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, KNIGHT, BLACK);
        ChessFigureModel blackRookFirst = new ChessFigureModel("rook", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessBlackRookFirstY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessBlackRookFirstX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, ROOK, BLACK);
        ChessFigureModel blackRookSecond = new ChessFigureModel("rook", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessBlackRookSecondY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessBlackRookSecondX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, ROOK, BLACK);
        ChessFigureModel blackKing = new ChessFigureModel("king", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessBlackKingY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessBlackKingX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, KING, BLACK);
        ChessFigureModel blackQueen = new ChessFigureModel("queen", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessBlackQueenY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessBlackQueenX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, QUEEN, BLACK);
        ChessFigureModel blackPawnOne = new ChessFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessBlackPawnFirstY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessBlackPawnFirstX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, PAWN, BLACK);
        ChessFigureModel blackPawnTwo = new ChessFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessBlackPawnSecondY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessBlackPawnSecondX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, PAWN, BLACK);
        ChessFigureModel blackPawnThree = new ChessFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessBlackPawnThirdY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessBlackPawnThirdX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, PAWN, BLACK);
        ChessFigureModel blackPawnFour = new ChessFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessBlackPawnFourthY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessBlackPawnFourthX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, PAWN, BLACK);
        ChessFigureModel blackPawnFive = new ChessFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessBlackPawnFifthY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessBlackPawnFifthX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, PAWN, BLACK);
        ChessFigureModel blackPawnSix = new ChessFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessBlackPawnSixthY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessBlackPawnSixthX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, PAWN, BLACK);
        ChessFigureModel blackPawnSeven = new ChessFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessBlackPawnSeventhY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessBlackPawnSeventhX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, PAWN, BLACK);
        ChessFigureModel blackPawnEight = new ChessFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getChessBlackPawnEighthY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getChessBlackPawnEighthX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, PAWN, BLACK);

        boardSceneGroup.getChildren().addAll(blackBishopFirst, blackBishopSecond, blackKnightFirst, blackKnightSecond,
                blackRookFirst, blackRookSecond, blackKing, blackQueen, blackPawnOne, blackPawnTwo, blackPawnThree,
                blackPawnFour, blackPawnFive, blackPawnSix, blackPawnSeven, blackPawnEight);

        chessFigureModels.addAll(Stream.of(blackBishopFirst, blackBishopSecond, blackKnightFirst, blackKnightSecond,
                blackRookFirst, blackRookSecond, blackKing, blackQueen, blackPawnOne, blackPawnTwo, blackPawnThree,
                blackPawnFour, blackPawnFive, blackPawnSix, blackPawnSeven, blackPawnEight).collect(Collectors.toList()));
    }

    public Camera initializeCamera() {
        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(10000);
        camera.translateZProperty().set(-150);

        return camera;
    }

    private void initMouseControl(SubScene boardScene) {
        Rotate xRotate;
        Rotate yRotate;
        Translate translate;

        boardSceneGroup.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS),
                translate = new Translate(distX, distZ)
        );

        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);
        translate.xProperty().bind(deltaX);
        translate.zProperty().bind(deltaZ);

        boardScene.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                anchorX = event.getSceneX();
                anchorY = event.getSceneY();
                anchorAngleX = angleX.get();
                anchorAngleY = angleY.get();
            } else if (event.getButton() == MouseButton.SECONDARY) {
                distX = event.getSceneX();
                distZ = event.getSceneY();
                anchorDistX = deltaX.get();
                anchorDistZ = deltaZ.get();
            } else if(event.getButton() == MouseButton.PRIMARY && !gameSetup.isMoveFigure()) {
                marked = handlePrimaryClick(event, board, chessFigureModels, fieldWidth, marked, highlight, getCurrentBoard(),
                        chessMoveHistory, boardSceneGroup, mainStage);

                if(marked == null && gameSetup.isMoveFigure() && gameSetup.getPlayer() == SINGLE_PLAYER){
                    executeUIMovement(event);
                }
            }
        });

        boardScene.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
                angleY.set(anchorAngleY - (anchorX - event.getSceneX()));
            } else if (event.getButton() == MouseButton.SECONDARY) {
                deltaX.set(anchorDistX - (distX - event.getSceneX()));
                deltaZ.set(anchorDistZ + (distZ - event.getSceneY()));
            }
        });

        boardScene.setOnMouseMoved(event -> {
            if(!gameSetup.isMoveFigure()) {
                if (marked != null) {
                    highlight = handleMarkedMove(event, board, chessFigureModels, fieldWidth, marked, highlight, getCurrentBoard(),
                            chessMoveHistory);
                }
            }
        });

        mainStage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = round(((event.getDeltaY() * 30) / 100.0));
            boardSceneGroup.translateZProperty().set(boardSceneGroup.getTranslateZ() - delta);
        });
    }

    private ChessBoardPositions getCurrentBoard() {
        ChessBoardPositions chessBoardPositions = new ChessBoardPositions(new org.sample.checkers.config.chess.ChessFigure[8][8], new ChessSide[8][8]);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessBoardPositions.getPositions()[i][j] = getBoardFigure(chessFigureModels, new Dimension2D(i+1,j+1),
                        fieldWidth);
                chessBoardPositions.getSides()[i][j] = getBoardSide(chessFigureModels, new Dimension2D(i+1,j+1), fieldWidth);
            }
        }

        return chessBoardPositions;
    }

    public void executeUIMovement(MouseEvent event) {
        ChessMovePosition nextMove = ui.computeNextMove(chessMoveHistory);
        ChessFigureModel figureToMove = getFigureForChessMove(nextMove, fieldWidth, chessFigureModels);
        MouseEvent markEvent = event.copyFor(event.getSource(), figureToMove);
        marked = handlePrimaryClick(markEvent, board, chessFigureModels, fieldWidth, marked, highlight, getCurrentBoard(),
                chessMoveHistory, boardSceneGroup, mainStage);
        MouseEvent moveEvent = event.copyFor(event.getSource(),
                board[(int) (nextMove.getPosition().width - 1)][(int) (nextMove.getPosition().height - 1)]);
        highlight = nextMove.getPosition();
        marked = handlePrimaryClick(moveEvent, board, chessFigureModels, fieldWidth, marked, highlight, getCurrentBoard(),
                chessMoveHistory, boardSceneGroup, mainStage);
    }
}
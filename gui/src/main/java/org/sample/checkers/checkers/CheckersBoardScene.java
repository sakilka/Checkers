package org.sample.checkers.checkers;

import com.sun.javafx.geom.Dimension2D;
import javafx.application.Platform;
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
import org.sample.checkers.board.components.SmartGroup;
import org.sample.checkers.board.model.ChessBoard;
import org.sample.checkers.board.model.CubeFace;
import org.sample.checkers.board.model.CubeMaterial;
import org.sample.checkers.chess.BoardPosition;
import org.sample.checkers.chess.Cube;
import org.sample.checkers.chess.FigurePosition;
import org.sample.checkers.config.checkers.*;
import org.sample.checkers.config.game.GameSetup;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.StrictMath.round;
import static javafx.scene.transform.Rotate.Y_AXIS;
import static org.sample.checkers.checkers.CheckersMoveUtil.*;
import static org.sample.checkers.checkers.ui.impl.CheckersContext.getUi;
import static org.sample.checkers.config.checkers.CheckersFigure.PAWN;
import static org.sample.checkers.config.checkers.CheckersFiguresPositions.getAbsolutePositionX;
import static org.sample.checkers.config.checkers.CheckersFiguresPositions.getAbsolutePositionY;
import static org.sample.checkers.config.checkers.CheckersPropertyUtil.getPositions;
import static org.sample.checkers.config.checkers.CheckersSide.BLACK;
import static org.sample.checkers.config.checkers.CheckersSide.WHITE;
import static org.sample.checkers.config.game.GamePropertyUtil.getBoardConfig;
import static org.sample.checkers.config.game.GamePropertyUtil.getGameSetup;
import static org.sample.checkers.config.game.Player.SINGLE_PLAYER;

@Controller
public class CheckersBoardScene extends SubScene implements ChessBoard {

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
    private final List<CheckersFigureModel> checkersFigureModels;
    private CheckersMoveHistory checkersMoveHistory;

    private GameSetup gameSetup = getGameSetup();
    private CheckersUi ui = getUi();

    public CheckersBoardScene(Stage stage, SmartGroup root, double width, double height, boolean depthBuffer,
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
        this.checkersFigureModels = new ArrayList<>();
        this.checkersMoveHistory = new CheckersMoveHistory(new ArrayList<>());

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
        CheckersFigureModel whitePawnOne = new CheckersFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getCheckersWhitePawnFirstY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getCheckersWhitePawnFirstX(), fieldWidth)),
                new PhongMaterial(whiteColor), PAWN, WHITE);
        CheckersFigureModel whitePawnTwo = new CheckersFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getCheckersWhitePawnSecondY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getCheckersWhitePawnSecondX(), fieldWidth)),
                new PhongMaterial(whiteColor), PAWN, WHITE);
        CheckersFigureModel whitePawnThree = new CheckersFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getCheckersWhitePawnThirdY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getCheckersWhitePawnThirdX(), fieldWidth)),
                new PhongMaterial(whiteColor), PAWN, WHITE);
        CheckersFigureModel whitePawnFour = new CheckersFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getCheckersWhitePawnFourthY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getCheckersWhitePawnFourthX(), fieldWidth)),
                new PhongMaterial(whiteColor), PAWN, WHITE);
        CheckersFigureModel whitePawnFive = new CheckersFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getCheckersWhitePawnFifthY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getCheckersWhitePawnFifthX(), fieldWidth)),
                new PhongMaterial(whiteColor), PAWN, WHITE);
        CheckersFigureModel whitePawnSix = new CheckersFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getCheckersWhitePawnSixthY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getCheckersWhitePawnSixthX(), fieldWidth)),
                new PhongMaterial(whiteColor), PAWN, WHITE);
        CheckersFigureModel whitePawnSeven = new CheckersFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getCheckersWhitePawnSeventhY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getCheckersWhitePawnSeventhX(), fieldWidth)),
                new PhongMaterial(whiteColor), PAWN, WHITE);
        CheckersFigureModel whitePawnEight = new CheckersFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getCheckersWhitePawnEightY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getCheckersWhitePawnEightX(), fieldWidth)),
                new PhongMaterial(whiteColor), PAWN, WHITE);

        boardSceneGroup.getChildren().addAll(whitePawnOne, whitePawnTwo, whitePawnThree,
                whitePawnFour, whitePawnFive, whitePawnSix, whitePawnSeven, whitePawnEight);

        checkersFigureModels.addAll(Stream.of(whitePawnOne, whitePawnTwo, whitePawnThree,
                whitePawnFour, whitePawnFive, whitePawnSix, whitePawnSeven, whitePawnEight).collect(Collectors.toList()));

        //black
        Color blackColor = Color.DIMGRAY;

        CheckersFigureModel blackPawnOne = new CheckersFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getCheckersBlackPawnFirstY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getCheckersBlackPawnFirstX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, PAWN, BLACK);
        CheckersFigureModel blackPawnTwo = new CheckersFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getCheckersBlackPawnSecondY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getCheckersBlackPawnSecondX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, PAWN, BLACK);
        CheckersFigureModel blackPawnThree = new CheckersFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getCheckersBlackPawnThirdY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getCheckersBlackPawnThirdX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, PAWN, BLACK);
        CheckersFigureModel blackPawnFour = new CheckersFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getCheckersBlackPawnFourthY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getCheckersBlackPawnFourthX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, PAWN, BLACK);
        CheckersFigureModel blackPawnFive = new CheckersFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getCheckersBlackPawnFifthY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getCheckersBlackPawnFifthX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, PAWN, BLACK);
        CheckersFigureModel blackPawnSix = new CheckersFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getCheckersBlackPawnSixthY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getCheckersBlackPawnSixthX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, PAWN, BLACK);
        CheckersFigureModel blackPawnSeven = new CheckersFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getCheckersBlackPawnSeventhY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getCheckersBlackPawnSeventhX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, PAWN, BLACK);
        CheckersFigureModel blackPawnEight = new CheckersFigureModel("pawn", new FigurePosition(
                getAbsolutePositionX(getPositions().getCheckersBlackPawnEightY(), fieldWidth), 0,
                getAbsolutePositionY(getPositions().getCheckersBlackPawnEightX(), fieldWidth)),
                new PhongMaterial(blackColor), Y_AXIS, 180, PAWN, BLACK);

        boardSceneGroup.getChildren().addAll(blackPawnOne, blackPawnTwo, blackPawnThree,
                blackPawnFour, blackPawnFive, blackPawnSix, blackPawnSeven, blackPawnEight);

        checkersFigureModels.addAll(Stream.of(blackPawnOne, blackPawnTwo, blackPawnThree,
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
                marked = handlePrimaryClick(event, board, checkersFigureModels, fieldWidth, marked, highlight, getCurrentBoard(),
                        checkersMoveHistory, boardSceneGroup, mainStage);

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
                    highlight = handleMarkedMove(event, board, checkersFigureModels, fieldWidth, marked, highlight, getCurrentBoard(),
                            checkersMoveHistory);
                }
            }
        });

        mainStage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = round(((event.getDeltaY() * 30) / 100.0));
            boardSceneGroup.translateZProperty().set(boardSceneGroup.getTranslateZ() - delta);
        });
    }

    private CheckersBoardPositions getCurrentBoard() {
        CheckersBoardPositions chessBoardPositions = new CheckersBoardPositions(new CheckersFigure[8][8], new CheckersSide[8][8]);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chessBoardPositions.getPositions()[i][j] = getBoardFigure(checkersFigureModels, new Dimension2D(i+1,j+1),
                        fieldWidth);
                chessBoardPositions.getSides()[i][j] = getBoardSide(checkersFigureModels, new Dimension2D(i+1,j+1), fieldWidth);
            }
        }

        return chessBoardPositions;
    }

    public void executeUIMovement(MouseEvent event) {
        CheckersMovePosition nextMove = ui.computeNextMove(checkersMoveHistory);
        CheckersFigureModel figureToMove = getFigureForCheckersMove(nextMove, fieldWidth, checkersFigureModels);
        MouseEvent markEvent = event.copyFor(event.getSource(), figureToMove);
        marked = handlePrimaryClick(markEvent, board, checkersFigureModels, fieldWidth, marked, highlight, getCurrentBoard(),
                checkersMoveHistory, boardSceneGroup, mainStage);
        MouseEvent moveEvent = event.copyFor(event.getSource(),
                board[(int) (nextMove.getPosition().width - 1)][(int) (nextMove.getPosition().height - 1)]);
        highlight = nextMove.getPosition();
        marked = handlePrimaryClick(moveEvent, board, checkersFigureModels, fieldWidth, marked, highlight, getCurrentBoard(),
                checkersMoveHistory, boardSceneGroup, mainStage);
    }
}

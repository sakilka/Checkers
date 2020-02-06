package org.sample.checkers.board;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.input.MouseButton;
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
import org.sample.checkers.board.model.*;
import org.sample.checkers.mesh.components.SmartGroup;

import java.util.Random;

import static java.lang.StrictMath.round;

public class ChessBoardScene extends SubScene implements ChessBoard {

    private double anchorX;
    private double anchorY;

    private double anchorAngleX = 0;
    private double anchorAngleY = 0;

    private DoubleProperty angleX = new SimpleDoubleProperty(0);
    private DoubleProperty angleY = new SimpleDoubleProperty(0);

    private double distX;
    private double distY;

    private double anchorDistX = 0;
    private double anchorDistY = 0;

    private DoubleProperty deltaX = new SimpleDoubleProperty(0);
    private DoubleProperty deltaY = new SimpleDoubleProperty(0);

    private Stage mainStage;
    private Group boardSceneGroup;

    private float fieldWidth = 8;
    private float fieldHeight = 1;
    private float fieldDepth = 8;
    private float fieldGap = 0.2f;
    private float fieldGapShift = 0.2f;
    private float widthShift =  4 * fieldWidth;
    private float depthShift = 4 * fieldWidth;

    public ChessBoardScene(Stage stage, SmartGroup root, double width, double height, boolean depthBuffer,
                           SceneAntialiasing antiAliasing, BoardPosition boardPosition) {
        super(root, width, height, depthBuffer, antiAliasing);

        mainStage = stage;
        boardSceneGroup = root;

        initCheckersBoard(boardPosition);
        initMouseControl(this);
    }

    @Override
    public void initCheckersBoard(BoardPosition boardPosition) {

        initializeBoard();
        initializeFigures();

        boardPosition.deltaXProperty().bind(deltaX);
        boardPosition.deltaYProperty().bind(deltaY);
        boardPosition.angleXProperty().bind(angleX);
        boardPosition.angleYProperty().bind(angleY);
        boardPosition.translateZProperty().bindBidirectional(boardSceneGroup.translateZProperty());

        this.setCamera(initializeCamera());
        this.setFill(Color.SILVER);
    }

    private void initializeBoard() {
        PhongMaterial blackMaterial = new PhongMaterial(Color.BLACK);
        PhongMaterial borderMaterial = new PhongMaterial(Color.BLACK);
        PhongMaterial whiteMaterial = new PhongMaterial(Color.WHITE);
        PhongMaterial gapMaterial = new PhongMaterial(Color.WHITE);
        PhongMaterial fieldDown = new PhongMaterial(Color.BLACK);
        String borderLetters = "ABCDEFGH";
        String borderNumbers = "12345678";

        Cube[][] board = new Cube[8][8];
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
            frontBorder.setTranslateX(-fieldWidth/2 - widthShift - fieldGap/2);
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
        bottom.setMaterial(whiteMaterial);
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

        Cube corner3 = new Cube(fieldWidth/2, fieldHeight, fieldDepth/2);
        corner3.setMaterial(blackMaterial);
        corner3.setTranslateX(widthShift + fieldGap);
        corner3.setTranslateY(0);
        corner3.setTranslateZ(depthShift + fieldGapShift);
        boardSceneGroup.getChildren().add(corner3);

        Cube corner4 = new Cube(fieldWidth/2, fieldHeight, fieldDepth/2);
        corner4.setMaterial(blackMaterial);
        corner4.setTranslateX(-fieldWidth/2 - widthShift - fieldGap);
        corner4.setTranslateY(0);
        corner4.setTranslateZ(depthShift + fieldGapShift);
        boardSceneGroup.getChildren().add(corner4);
    }

    private void initializeFigures() {
        Figure pawn = new Figure("pawn.obj", new FigurePosition(0, 0, 0),
                new PhongMaterial(Color.WHITE));
        Figure queen = new Figure("queen.obj", new FigurePosition(fieldWidth * 4, 0, 0),
                new PhongMaterial(Color.WHITE));
        Figure king = new Figure("king.obj", new FigurePosition(fieldWidth * 5, 0, 0),
                new PhongMaterial(Color.WHITE));

        boardSceneGroup.getChildren().add(pawn);
        boardSceneGroup.getChildren().add(queen);
        boardSceneGroup.getChildren().add(king);
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
                translate = new Translate(distX, distY)
        );

        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);
        translate.xProperty().bind(deltaX);
        translate.yProperty().bind(deltaY);

        boardScene.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                anchorX = event.getSceneX();
                anchorY = event.getSceneY();
                anchorAngleX = angleX.get();
                anchorAngleY = angleY.get();
            } else if (event.getButton() == MouseButton.SECONDARY) {
                distX = event.getSceneX();
                distY = event.getSceneY();
                anchorDistX = deltaX.get();
                anchorDistY = deltaY.get();
            }
        });

        boardScene.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.MIDDLE) {
                angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
                angleY.set(anchorAngleY - (anchorX - event.getSceneX()));
            } else if (event.getButton() == MouseButton.SECONDARY) {
                deltaX.set(anchorDistX - (distX - event.getSceneX()));
                deltaY.set(anchorDistY - (distY - event.getSceneY()));
            }
        });

        mainStage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = round(((event.getDeltaY() * 30) / 100.0));
            boardSceneGroup.translateZProperty().set(boardSceneGroup.getTranslateZ() - delta);
        });
    }
}

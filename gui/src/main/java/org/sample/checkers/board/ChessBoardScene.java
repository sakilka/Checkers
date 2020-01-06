package org.sample.checkers.board;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.sample.checkers.board.model.Figure;
import org.sample.checkers.board.model.FigurePosition;
import org.sample.checkers.mesh.components.SmartGroup;

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

    private double fieldWidth = 8;
    private double fieldHeight = 1;
    private double fieldDepth = 8;
    private double fieldShift = 0.5;
    private double fieldGap = 0.2;
    private double fieldGapShift = 0.2;

    public ChessBoardScene(Stage stage, SmartGroup root, double width, double height, boolean depthBuffer,
                           SceneAntialiasing antiAliasing) {
        super(root, width, height, depthBuffer, antiAliasing);

        mainStage = stage;
        boardSceneGroup = root;

        initCheckersBoard();
        initMouseControl(this);
    }

    @Override
    public void initCheckersBoard() {

        initializeBoard();
        initializeFigures();

        this.setCamera(initializeCamera());
        this.setFill(Color.SILVER);
    }

    private void initializeBoard() {
        PhongMaterial blackMaterial = new PhongMaterial(Color.BLACK);
        PhongMaterial whiteMaterial = new PhongMaterial(Color.WHITE);
        PhongMaterial gapMaterial = new PhongMaterial(Color.WHITE);

        Box[][] board = new Box[8][8];
        Box[][][] boardBorder = new Box[8][8][4];

        for (int fieldZ = 0; fieldZ < 8; fieldZ++) {

            PhongMaterial current = (fieldZ % 2) == 0 ? blackMaterial : whiteMaterial;

            for (int fieldX = 0; fieldX < 8; fieldX++) {

                Box field = new Box(fieldWidth - fieldGap, fieldHeight, fieldDepth - fieldGap);

                field.setTranslateX(fieldX * fieldWidth);
                field.setTranslateY(fieldShift);
                field.setTranslateZ(fieldZ * fieldDepth);

                field.setMaterial(current);
                board[fieldX][fieldZ] = field;
                current = current == whiteMaterial ? blackMaterial : whiteMaterial;

                Box borderLeft = new Box(fieldGap / 2, fieldHeight - fieldGapShift, fieldDepth);
                borderLeft.setTranslateX((fieldX * fieldWidth) - ((fieldWidth - fieldGap) / 2) - (fieldGap / 4));
                borderLeft.setTranslateY(fieldShift + (fieldGapShift / 2));
                borderLeft.setTranslateZ(fieldZ * fieldDepth);
                borderLeft.setMaterial(gapMaterial);
                boardBorder[fieldX][fieldZ][0] = borderLeft;

                Box borderUp = new Box(fieldWidth, fieldHeight - fieldGapShift, fieldGap / 2);
                borderUp.setTranslateX(fieldX * fieldWidth);
                borderUp.setTranslateY(fieldShift + (fieldGapShift / 2));
                borderUp.setTranslateZ((fieldZ * fieldDepth) + ((fieldDepth - fieldGap) / 2) + (fieldGap / 4));
                borderUp.setMaterial(gapMaterial);
                boardBorder[fieldX][fieldZ][1] = borderUp;

                Box borderRight = new Box(fieldGap / 2, fieldHeight - fieldGapShift, fieldDepth);
                borderRight.setTranslateX((fieldX * fieldWidth) + ((fieldWidth - fieldGap) / 2) + (fieldGap / 4));
                borderRight.setTranslateY(fieldShift + (fieldGapShift / 2));
                borderRight.setTranslateZ(fieldZ * fieldDepth);
                borderRight.setMaterial(gapMaterial);
                boardBorder[fieldX][fieldZ][2] = borderRight;

                Box borderDown = new Box(fieldWidth, fieldHeight - fieldGapShift, fieldGap / 2);
                borderDown.setTranslateX(fieldX * fieldWidth);
                borderDown.setTranslateY(fieldShift + (fieldGapShift / 2));
                borderDown.setTranslateZ((fieldZ * fieldDepth) - ((fieldDepth - fieldGap) / 2) - (fieldGap / 4));
                borderDown.setMaterial(gapMaterial);
                boardBorder[fieldX][fieldZ][3] = borderDown;
            }
        }

        for (int fieldZ = 0; fieldZ < 8; fieldZ++) {
            for (int fieldX = 0; fieldX < 8; fieldX++) {
                boardSceneGroup.getChildren().add(board[fieldX][fieldZ]);
                boardSceneGroup.getChildren().add(boardBorder[fieldX][fieldZ][0]);
                boardSceneGroup.getChildren().add(boardBorder[fieldX][fieldZ][1]);
                boardSceneGroup.getChildren().add(boardBorder[fieldX][fieldZ][2]);
                boardSceneGroup.getChildren().add(boardBorder[fieldX][fieldZ][3]);
            }
        }
    }

    private void initializeFigures() {
        Figure pawn = new Figure("pawn.obj", new FigurePosition(fieldWidth, 0, fieldDepth),
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
            double delta = event.getDeltaY();
            boardSceneGroup.translateZProperty().set(boardSceneGroup.getTranslateZ() + delta);
        });
    }
}

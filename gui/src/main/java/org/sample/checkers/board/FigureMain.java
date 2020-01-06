package org.sample.checkers.board;


import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.sample.checkers.board.model.LoadObject;
import org.sample.checkers.mesh.components.SmartGroup;

import java.io.File;

public class FigureMain extends Application {

    private static final float WIDTH = 800;
    private static final float HEIGHT = 600;

    private double anchorX;
    private double anchorY;

    private double anchorAngleX = 0;
    private double anchorAngleY = 0;

    private DoubleProperty angleX = new SimpleDoubleProperty(0);
    private DoubleProperty angleY = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage primaryStage) {
        SmartGroup group = new SmartGroup();

        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(10000);
        camera.translateZProperty().set(-150);

//        Cube cube = new Cube(100, 100, 100);
//
//        cube.setTranslateX(0);
//        cube.setTranslateY(0);
//        cube.setTranslateZ(0);
//        cube.setMaterial(new PhongMaterial(Color.GREEN));

        double fieldWidth = 8;
        double fieldHeight = 1;
        double fieldDepth = 8;
        double fieldShift = 0.5;
        double fieldGap = 0.2;
        double fieldGapShift = 0.2;

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

        LoadObject pawn = new LoadObject(new File((getClass().getResource("pawn.obj").getFile())), 1);
        pawn.setMaterial(new PhongMaterial(Color.WHITE));
        pawn.setTranslateX(0);
        pawn.setTranslateY(0);
        pawn.setTranslateZ(0);

        LoadObject knight = new LoadObject(new File((getClass().getResource("knight.obj").getFile())), 1);
        knight.setMaterial(new PhongMaterial(Color.DARKGOLDENROD));
        knight.setTranslateX(fieldWidth);
        knight.setTranslateY(0);
        knight.setTranslateZ(0);

        LoadObject queen = new LoadObject(new File((getClass().getResource("queen.obj").getFile())), 1);
        PhongMaterial silver = new PhongMaterial(Color.SILVER);
        silver.setSpecularColor(Color.WHITE);
        silver.setSpecularPower(15);
        queen.setMaterial(silver);
        queen.setTranslateX(fieldWidth * 2);
        queen.setTranslateY(0);
        queen.setTranslateZ(0);

        LoadObject king = new LoadObject(new File((getClass().getResource("king.obj").getFile())), 1);
        PhongMaterial gold = new PhongMaterial(Color.GOLD);
        gold.setSpecularColor(Color.WHITE);
        gold.setSpecularPower(15);
        king.setMaterial(gold);
        king.setTranslateX(fieldWidth * 3);
        king.setTranslateY(0);
        king.setTranslateZ(0);

        LoadObject bishop = new LoadObject(new File((getClass().getResource("bishop.obj").getFile())), 1);
        bishop.setMaterial(new PhongMaterial(Color.WHITE));
        bishop.setTranslateX(fieldWidth * 4);
        bishop.setTranslateY(0);
        bishop.setTranslateZ(0);

        LoadObject rook = new LoadObject(new File((getClass().getResource("rook.obj").getFile())), 1);
        rook.setMaterial(new PhongMaterial(Color.DARKGOLDENROD));
        rook.setTranslateX(fieldWidth * 5);
        rook.setTranslateY(0);
        rook.setTranslateZ(0);
//        LoadObject loadObject = new LoadObject(new File((getClass().getResource("pawn2.obj").getFile())), 100);
//        PhongMaterial whiteMaterial = new PhongMaterial();
////        whiteMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("whiteWood.jpg")));
////        whiteMaterial.setSpecularColor(Color.BLACK);
//        loadObject.setMaterial(new PhongMaterial(Color.WHITE));

        for (int fieldZ = 0; fieldZ < 8; fieldZ++) {
            for (int fieldX = 0; fieldX < 8; fieldX++) {
                group.getChildren().add(board[fieldX][fieldZ]);
                group.getChildren().add(boardBorder[fieldX][fieldZ][0]);
                group.getChildren().add(boardBorder[fieldX][fieldZ][1]);
                group.getChildren().add(boardBorder[fieldX][fieldZ][2]);
                group.getChildren().add(boardBorder[fieldX][fieldZ][3]);
            }
        }

        group.getChildren().add(pawn);
        group.getChildren().add(knight);
        group.getChildren().add(queen);
        group.getChildren().add(king);
        group.getChildren().add(bishop);
        group.getChildren().add(rook);

        Scene root = new Scene(group, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED);
        root.setFill(Color.SILVER);
        root.setCamera(camera);

        initMouseControl(group, root, primaryStage);

        primaryStage.setTitle("Checkers");
        primaryStage.setScene(root);
        primaryStage.show();
    }

    private void initMouseControl(SmartGroup group, Scene boardScene, Stage stage) {
        Rotate xRotate;
        Rotate yRotate;

        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );

        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        boardScene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        boardScene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY - (anchorX - event.getSceneX()));
        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ() + delta);
        });
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX
     * application. main() serves only as fallback in case the
     * application can not be launched through deployment artifacts,
     * e.g., in IDEs with limited FX support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}


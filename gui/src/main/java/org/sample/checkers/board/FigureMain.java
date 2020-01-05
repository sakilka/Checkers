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

        Box board = new Box(5, 1, 5);
        board.setTranslateX(0);
        board.setTranslateY(0.5);
        board.setTranslateZ(0);
        board.setMaterial(new PhongMaterial(Color.BLACK));

        Box board2 = new Box(5, 1, 5);
        board2.setTranslateX(-5);
        board2.setTranslateY(0.5);
        board2.setTranslateZ(0);
        board2.setMaterial(new PhongMaterial(Color.WHITE));

        Box board3 = new Box(5, 1, 5);
        board3.setTranslateX(-10);
        board3.setTranslateY(0.5);
        board3.setTranslateZ(0);
        board3.setMaterial(new PhongMaterial(Color.BLACK));

        Box board4 = new Box(5, 1, 5);
        board4.setTranslateX(-15);
        board4.setTranslateY(0.5);
        board4.setTranslateZ(0);
        board4.setMaterial(new PhongMaterial(Color.WHITE));

        Box board5 = new Box(5, 1, 5);
        board5.setTranslateX(-20);
        board5.setTranslateY(0.5);
        board5.setTranslateZ(0);
        board5.setMaterial(new PhongMaterial(Color.BLACK));

        Box board6 = new Box(5, 1, 5);
        board6.setTranslateX(-25);
        board6.setTranslateY(0.5);
        board6.setTranslateZ(0);
        board6.setMaterial(new PhongMaterial(Color.WHITE));
//        Figure figure = new Figure(250, 10);
//
//        figure.setTranslateX(0);
//        figure.setTranslateY(0);
//        figure.setTranslateZ(0);
//        figure.setMaterial(new PhongMaterial(Color.RED));

        LoadObject pawn = new LoadObject(new File((getClass().getResource("pawn.obj").getFile())), 1);
        pawn.setMaterial(new PhongMaterial(Color.WHITE));
        pawn.setTranslateX(0);
        pawn.setTranslateY(0);
        pawn.setTranslateZ(0);

        LoadObject knight = new LoadObject(new File((getClass().getResource("knight.obj").getFile())), 1);
        knight.setMaterial(new PhongMaterial(Color.DARKGOLDENROD));
        knight.setTranslateX(-5);
        knight.setTranslateY(0);
        knight.setTranslateZ(0);

        LoadObject queen = new LoadObject(new File((getClass().getResource("queen.obj").getFile())), 1);
        PhongMaterial silver = new PhongMaterial(Color.SILVER);
        silver.setSpecularColor(Color.WHITE);
        silver.setSpecularPower(15);
        queen.setMaterial(silver);
        queen.setTranslateX(-10);
        queen.setTranslateY(0);
        queen.setTranslateZ(0);

        LoadObject king = new LoadObject(new File((getClass().getResource("king.obj").getFile())), 1);
        PhongMaterial gold = new PhongMaterial(Color.GOLD);
        gold.setSpecularColor(Color.WHITE);
        gold.setSpecularPower(15);
        king.setMaterial(gold);
        king.setTranslateX(-15);
        king.setTranslateY(0);
        king.setTranslateZ(0);

        LoadObject bishop = new LoadObject(new File((getClass().getResource("bishop.obj").getFile())), 1);
        bishop.setMaterial(new PhongMaterial(Color.WHITE));
        bishop.setTranslateX(-20);
        bishop.setTranslateY(0);
        bishop.setTranslateZ(0);

        LoadObject rook = new LoadObject(new File((getClass().getResource("rook.obj").getFile())), 1);
        rook.setMaterial(new PhongMaterial(Color.DARKGOLDENROD));
        rook.setTranslateX(-25);
        rook.setTranslateY(0);
        rook.setTranslateZ(0);
//        LoadObject loadObject = new LoadObject(new File((getClass().getResource("pawn2.obj").getFile())), 100);
//        PhongMaterial whiteMaterial = new PhongMaterial();
////        whiteMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("whiteWood.jpg")));
////        whiteMaterial.setSpecularColor(Color.BLACK);
//        loadObject.setMaterial(new PhongMaterial(Color.WHITE));

        group.getChildren().add(board);
        group.getChildren().add(board2);
        group.getChildren().add(board3);
        group.getChildren().add(board4);
        group.getChildren().add(board5);
        group.getChildren().add(board6);
        group.getChildren().add(pawn);
        group.getChildren().add(knight);
        group.getChildren().add(queen);
        group.getChildren().add(king);
        group.getChildren().add(bishop);
        group.getChildren().add(rook);
//        group.getChildren().add(figure);
//        group.getChildren().add(loadObject);

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


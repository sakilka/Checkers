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
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.sample.checkers.board.model.Cube;
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
        camera.translateZProperty().set(-1500);

        Cube cube = new Cube(100, 100, 100);

        cube.setTranslateX(0);
        cube.setTranslateY(0);
        cube.setTranslateZ(0);
        cube.setMaterial(new PhongMaterial(Color.GREEN));

//        Figure figure = new Figure(250, 10);
//
//        figure.setTranslateX(0);
//        figure.setTranslateY(0);
//        figure.setTranslateZ(0);
//        figure.setMaterial(new PhongMaterial(Color.RED));

        LoadObject pawn = new LoadObject(new File((getClass().getResource("pawn.obj").getFile())), 10);
        pawn.setMaterial(new PhongMaterial(Color.WHITE));
        pawn.setTranslateX(0);
        pawn.setTranslateY(0);
        pawn.setTranslateZ(0);

        LoadObject knight = new LoadObject(new File((getClass().getResource("knight.obj").getFile())), 10);
        knight.setMaterial(new PhongMaterial(Color.DARKGOLDENROD));
        knight.setTranslateX(-50);
        knight.setTranslateY(0);
        knight.setTranslateZ(0);

        LoadObject queen = new LoadObject(new File((getClass().getResource("queen.obj").getFile())), 10);
        queen.setMaterial(new PhongMaterial(Color.WHITE));
        queen.setTranslateX(-100);
        queen.setTranslateY(0);
        queen.setTranslateZ(0);

        LoadObject king = new LoadObject(new File((getClass().getResource("king.obj").getFile())), 10);
        king.setMaterial(new PhongMaterial(Color.DARKGOLDENROD));
        king.setTranslateX(-150);
        king.setTranslateY(0);
        king.setTranslateZ(0);

        LoadObject bishop = new LoadObject(new File((getClass().getResource("bishop.obj").getFile())), 10);
        bishop.setMaterial(new PhongMaterial(Color.WHITE));
        bishop.setTranslateX(-200);
        bishop.setTranslateY(0);
        bishop.setTranslateZ(0);

        LoadObject rook = new LoadObject(new File((getClass().getResource("rook.obj").getFile())), 10);
        rook.setMaterial(new PhongMaterial(Color.DARKGOLDENROD));
        rook.setTranslateX(-250);
        rook.setTranslateY(0);
        rook.setTranslateZ(0);
//        LoadObject loadObject = new LoadObject(new File((getClass().getResource("pawn2.obj").getFile())), 100);
//        PhongMaterial whiteMaterial = new PhongMaterial();
////        whiteMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("whiteWood.jpg")));
////        whiteMaterial.setSpecularColor(Color.BLACK);
//        loadObject.setMaterial(new PhongMaterial(Color.WHITE));

        group.getChildren().add(cube);
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


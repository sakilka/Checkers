package org.sample.checkers.mesh;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableFloatArray;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.sample.checkers.mesh.components.SmartGroup;
import org.sample.checkers.mesh.mesh.OktaHedron;
import org.sample.checkers.mesh.mesh.Pyramid;

public class Mesh extends Application {

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
        camera.translateZProperty().set(-1000);

        Cylinder cylinder = new Cylinder(5, 200);

        cylinder.setTranslateX(100);
        cylinder.setTranslateY(0);
        cylinder.setTranslateZ(0);

        Pyramid pyramid = new Pyramid(150, 300);

        pyramid.setTranslateX(-150);
        pyramid.setTranslateY(-100);
        pyramid.setTranslateZ(150);
        pyramid.setMaterial(new PhongMaterial(Color.GREEN));

        OktaHedron oktaHedron = new OktaHedron(50);

        oktaHedron.setTranslateX(0);
        oktaHedron.setTranslateY(0);
        oktaHedron.setTranslateZ(100);
        oktaHedron.setMaterial(new PhongMaterial(Color.BLUE));

        ObjModelImporter importer = new ObjModelImporter();
        importer.read("C:/Users/Ivan/Documents/customObject.obj");

        MeshView[] customObject = importer.getImport();
        customObject[0].setTranslateX(0);
        customObject[0].setTranslateY(0);
        customObject[0].setTranslateZ(0);
        customObject[0].setMaterial(new PhongMaterial(Color.RED));

        ObservableFloatArray points = ((TriangleMesh) customObject[0].getMesh()).getPoints();
        float[] floats = new float[points.size()];
        float[] resized = new float[points.size()];
        int p = 0;
        for (float f : points.toArray(floats)) {
            System.out.println(f);
            resized[p] = f * 100;
            p++;
        }
        ((TriangleMesh) customObject[0].getMesh()).getPoints().setAll(resized);
        //((TriangleMesh) customObject[0].getMesh()).getPoints().addAll(resized);

        group.getChildren().add(cylinder);
        group.getChildren().add(pyramid);
        group.getChildren().addAll(customObject);
        group.getChildren().addAll(oktaHedron);

        Scene root = new Scene(group, WIDTH, HEIGHT, true, SceneAntialiasing.DISABLED);
        root.setFill(Color.SILVER);
        root.setCamera(camera);

        initMouseControl(group, root, primaryStage);

        primaryStage.setTitle("Mesh");
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

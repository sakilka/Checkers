package org.sample.checkers;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Checkers extends Application {

    final BorderPane root = new BorderPane();

    private static final float WIDTH = 800;
    private static final float HEIGHT = 600;

    private double anchorX;
    private double anchorY;

    private double anchorAngleX = 0;
    private double anchorAngleY = 0;

    private DoubleProperty angleX = new SimpleDoubleProperty(0);
    private DoubleProperty angleY = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Checkers");
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        MenuBar menu = (MenuBar) loadFXML("menu");
        root.setTop(menu);

        Group boardGroup = new Group();
        SubScene boardScene = new SubScene(boardGroup, WIDTH, HEIGHT, true, SceneAntialiasing.DISABLED);
        boardScene.setFill(Color.SILVER);

        root.setCenter(boardScene);
        stage.setScene(scene);
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Checkers.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
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

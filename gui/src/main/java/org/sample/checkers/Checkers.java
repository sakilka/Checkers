package org.sample.checkers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.sample.checkers.board.ChessBoardScene;
import org.sample.checkers.menu.RightPanel;
import org.sample.checkers.mesh.components.SmartGroup;

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
        Scene scene = new Scene(root/*, WIDTH, HEIGHT*/);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        MenuBar menu = (MenuBar) loadFXML("menu");
        root.setTop(menu);

        SplitPane content = new SplitPane();

        SubScene boardScene = new ChessBoardScene(stage, new SmartGroup(), WIDTH, HEIGHT, true,
                SceneAntialiasing.BALANCED);

        TitledPane titledPane = new TitledPane("Options", new Label("An option"));
        VBox settingsPane = new VBox(titledPane);
        settingsPane.setMinWidth(0);
        content.getItems().addAll(new BorderPane(boardScene), new RightPanel());
        CheckMenuItem settings = (CheckMenuItem) menu.getMenus().get(2).getItems().get(0);

        DoubleProperty splitPaneDividerPosition = content.getDividers().get(0).positionProperty();

        splitPaneDividerPosition.addListener((obs, oldPos, newPos) ->
                settings.setSelected(newPos.doubleValue() < 0.95));
        splitPaneDividerPosition.set(0.5);

        settings.setOnAction(event -> {
            KeyValue end;
            if (settings.isSelected()) {
                end = new KeyValue(splitPaneDividerPosition, 0.8);
            } else {
                end = new KeyValue(splitPaneDividerPosition, 1.0);
            }
            new Timeline(new KeyFrame(Duration.seconds(0.5), end)).play();
        });

        root.setCenter(content);
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

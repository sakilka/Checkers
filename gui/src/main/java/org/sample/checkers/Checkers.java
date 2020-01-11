package org.sample.checkers;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.sample.checkers.board.ChessBoardScene;
import org.sample.checkers.menu.RightPanel;
import org.sample.checkers.mesh.components.SmartGroup;

import java.io.IOException;

public class Checkers extends Application {

    final BorderPane root = new BorderPane();

    private static final float WIDTH = 800;
    private static final float HEIGHT = 600;
    private static final int RIGHT_PANEL_WIDTH = 200;

    private double anchorX;
    private double anchorY;

    private double anchorAngleX = 0;
    private double anchorAngleY = 0;

    private DoubleProperty angleX = new SimpleDoubleProperty(0);
    private DoubleProperty angleY = new SimpleDoubleProperty(0);

    private boolean mouseDragOnDivider = false;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Checkers");
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        MenuBar menu = (MenuBar) loadFXML("menu");
        root.setTop(menu);

        SplitPane content = new SplitPane();

        SubScene boardScene = new ChessBoardScene(stage, new SmartGroup(), WIDTH - RIGHT_PANEL_WIDTH, HEIGHT, true,
                SceneAntialiasing.BALANCED);

        StackPane boardPane = new StackPane(boardScene);
        boardPane.setStyle("-fx-background-color: green;");
//        boardPane.prefWidthProperty().bindBidirectional(content.prefWidthProperty());
//        boardScene.widthProperty().bindBidirectional(boardPane.prefWidthProperty());
//        boardPane.prefWidthProperty()
//                .bind(Bindings.createDoubleBinding((() -> content.widthProperty().doubleValue() * 0.8)));
//        boardScene.widthProperty().bind(Bindings.createDoubleBinding((() -> scene.widthProperty().doubleValue())));
        DoubleProperty splitPaneDividerPosition = new SimpleDoubleProperty();
        RightPanel rightPanel = new RightPanel(splitPaneDividerPosition, content.heightProperty());
        boardScene.widthProperty().bind(scene.widthProperty().subtract(RIGHT_PANEL_WIDTH));
        boardScene.heightProperty().bind(scene.heightProperty());
//        boardScene.heightProperty().bind(boardPane.heightProperty());

        content.getItems().addAll(boardPane, rightPanel);
//        content.getItems().addAll(boardScene, new BorderPane(new Label("test")));
        content.getDividers().get(0).positionProperty().bindBidirectional(splitPaneDividerPosition);

        splitPaneDividerPosition.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (mouseDragOnDivider) content.getDividers().get(0).setPosition(0);
            }
        });

        root.setCenter(content);
        stage.setScene(scene);
        stage.show();

        content.requestLayout();
        content.applyCss();
        for (Node node : content.lookupAll(".split-pane-divider")) {
            node.setOnMousePressed(evMousePressed -> mouseDragOnDivider = true);
            node.setOnMouseReleased(evMouseReleased -> mouseDragOnDivider = false);
        }
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

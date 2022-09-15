package org.sample.checkers;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.sample.checkers.board.ChessBoardScene;
import org.sample.checkers.board.model.BoardPosition;
import org.sample.checkers.config.PropertyUtil;
import org.sample.checkers.menu.RightPanel;
import org.sample.checkers.mesh.components.SmartGroup;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.time.Instant;

import static org.sample.checkers.config.PropertyUtil.getConfig;

@ComponentScan
public class Checkers extends Application {

    final BorderPane root = new BorderPane();

    private double anchorX;
    private double anchorY;

    private double anchorAngleX = 0;
    private double anchorAngleY = 0;

    private DoubleProperty angleX = new SimpleDoubleProperty(0);
    private DoubleProperty angleY = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Checkers");
        Scene scene = new Scene(root, getConfig().getWidth(), getConfig().getHeight());
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        MenuBar menu = (MenuBar) loadFXML("menu");
        root.setTop(menu);

        SplitPane content = new SplitPane();

        BoardPosition boardPosition = new BoardPosition();
        SubScene boardScene = new ChessBoardScene(stage, new SmartGroup(),
                getConfig().getWidth() - getConfig().getRightPanelWidth(), getConfig().getHeight(), true,
                SceneAntialiasing.BALANCED, boardPosition);

        StackPane boardPane = new StackPane(boardScene);
        boardPane.setStyle("-fx-background-color: silver;");
        DoubleProperty splitPaneDividerPosition = new SimpleDoubleProperty();
        splitPaneDividerPosition.set((scene.getWidth() - getConfig().getRightPanelWidth()) / scene.getWidth());
        BooleanProperty shownRightPanel = new SimpleBooleanProperty();
        RightPanel rightPanel = new RightPanel(splitPaneDividerPosition, scene.heightProperty(), scene.widthProperty(),
                shownRightPanel, boardScene, boardPosition);
        boardScene.widthProperty().bind(scene.widthProperty().subtract(getConfig().getRightPanelWidth()));
        boardScene.heightProperty().bind(scene.heightProperty());

        content.getItems().addAll(boardPane, rightPanel);
        content.getDividers().get(0).positionProperty().bindBidirectional(splitPaneDividerPosition);

        splitPaneDividerPosition.addListener((observable, oldValue, newValue) -> {
            if (rightPanel.isMouseDragOnDivider()) {
                if (shownRightPanel.getValue()) {
                    splitPaneDividerPosition.set(1 - (getConfig().getRightPanelWidth() / scene.widthProperty().doubleValue()));
                } else {
                    splitPaneDividerPosition.set(1 - (rightPanel.getButtonWidth() / scene.widthProperty().doubleValue()));
                }
                content.getDividers().get(0).positionProperty().set(splitPaneDividerPosition.doubleValue());
            }
        });

        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (shownRightPanel.getValue()) {
                rightPanel.setMinWidth(getConfig().getRightPanelWidth());
                splitPaneDividerPosition.set(1 - (getConfig().getRightPanelWidth() / newVal.doubleValue()));
            } else {
                rightPanel.minWidth(rightPanel.getButtonWidth());
                splitPaneDividerPosition.set(1 - (rightPanel.getButtonWidth() / newVal.doubleValue()));
            }
            content.getDividers().get(0).positionProperty().set(splitPaneDividerPosition.doubleValue());
        });

        root.setCenter(content);
        rightPanel.disableDrag(content);

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

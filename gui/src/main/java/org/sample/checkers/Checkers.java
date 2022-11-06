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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.sample.checkers.chess.BoardPosition;
import org.sample.checkers.config.game.Game;
import org.sample.checkers.config.game.GameSetup;
import org.sample.checkers.menu.RightPanel;
import org.sample.checkers.menu.controller.MenuController;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.sample.checkers.config.game.GamePropertyUtil.getBoardConfig;
import static org.sample.checkers.config.game.GamePropertyUtil.getGameSetup;

@ComponentScan
public class Checkers extends Application {

    private final GameSetup gameSetup;

    public Checkers() {
        this.gameSetup = getGameSetup();
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Checkers");
        initializeScene(stage, gameSetup.getGame());
        stage.show();
    }

    private static void initializeScene(Stage stage, Game game) throws IOException {
        final BorderPane root = new BorderPane();
        Scene scene = new Scene(root, getBoardConfig().getWidth(), getBoardConfig().getHeight());
        scene.getStylesheets().add(Checkers.class.getResource("styles.css").toExternalForm());

        SplitPane content = new SplitPane();

        BoardPosition boardPosition = new BoardPosition();
        SubScene boardScene = BoardFactory.getBoardScene(game, stage,
                getBoardConfig().getWidth(), getBoardConfig().getHeight(), true,
                SceneAntialiasing.BALANCED, boardPosition);

        StackPane boardPane = new StackPane(boardScene);
        boardPane.setBackground(new Background(new BackgroundFill(Color.rgb(0,100,0, 1), null, null)));
        DoubleProperty splitPaneDividerPosition = new SimpleDoubleProperty();
        splitPaneDividerPosition.set((scene.getWidth() - getBoardConfig().getRightPanelWidth()) / scene.getWidth());
        BooleanProperty shownRightPanel = new SimpleBooleanProperty();
        RightPanel rightPanel = new RightPanel(splitPaneDividerPosition, scene.heightProperty(), scene.widthProperty(),
                shownRightPanel, boardScene, boardPosition);
        boardScene.widthProperty().bind(scene.widthProperty().subtract(rightPanel.getButtonWidth()));
        boardScene.heightProperty().bind(scene.heightProperty());
        boardScene.setFill(Color.rgb(0,100,0, 1));

        content.getItems().addAll(boardPane, rightPanel);
        content.getDividers().get(0).positionProperty().bindBidirectional(splitPaneDividerPosition);

        content.setOnMouseMoved(event -> {
            double x = event.getSceneX();
            SplitPane pane = (SplitPane) event.getSource();
            double width = pane.getWidth();
            double widthPercentage = x / (width/100);

            if(!shownRightPanel.getValue()) {
                if (widthPercentage > 95) {
                    double opacity = ((100 - widthPercentage) * 20) / 100;
                    splitPaneDividerPosition.set(1 - (rightPanel.getButtonWidth() / scene.widthProperty().doubleValue()));
                    rightPanel.getToggleButton().setOpacity(Math.max(opacity, 0.5));
                    rightPanel.getToggleButton().getGraphic().setOpacity(1 - opacity);
                } else {
                    splitPaneDividerPosition.set(1 - (rightPanel.getButtonWidth() / scene.widthProperty().doubleValue()));
                    rightPanel.getToggleButton().setOpacity(1);
                    rightPanel.getToggleButton().getGraphic().setOpacity(0);
                }
            }
        });

        MenuBar menu = (MenuBar) loadFXML("menu", stage);
        menu.setPrefHeight(getBoardConfig().getMenuHeight());
        root.setTop(menu);

        splitPaneDividerPosition.addListener((observable, oldValue, newValue) -> {
            if (rightPanel.isMouseDragOnDivider()) {
                if (shownRightPanel.getValue()) {
                    splitPaneDividerPosition.set(1 - (getBoardConfig().getRightPanelWidth() / scene.widthProperty().doubleValue()));
                } else {
                    splitPaneDividerPosition.set(1 - (rightPanel.getButtonWidth() / scene.widthProperty().doubleValue()));
                }
                content.getDividers().get(0).positionProperty().set(splitPaneDividerPosition.doubleValue());
            }
        });

        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (shownRightPanel.getValue()) {
                rightPanel.setMinWidth(getBoardConfig().getRightPanelWidth());
                splitPaneDividerPosition.set(1 - (getBoardConfig().getRightPanelWidth() / newVal.doubleValue()));
            } else {
                rightPanel.minWidth(rightPanel.getButtonWidth());
                splitPaneDividerPosition.set(1 - (rightPanel.getButtonWidth() / newVal.doubleValue()));
            }
            content.getDividers().get(0).positionProperty().set(splitPaneDividerPosition.doubleValue());
        });

        AtomicReference<Number> initialWidth = new AtomicReference<>();

        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            if(Double.isNaN(oldVal.doubleValue())) {
                initialWidth.set(newVal);
            } else {
                if(newVal.doubleValue() > 200) {
                    double ratio = (initialWidth.get().doubleValue() / newVal.doubleValue());
                    double zoom = 7.30395 * (Math.pow(ratio, 2)) - 110.62 * ratio + 125.623;

                    boardPosition.translateZProperty().setValue(-zoom);
                }
            }
        });

        root.setCenter(content);
        rightPanel.disableDrag(content);
        stage.setScene(scene);
    }

    public static void newGame(Stage stage, Game game)  {
        try {
            initializeScene(stage, game);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Parent loadFXML(String fxml, Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Checkers.class.getResource(fxml + ".fxml"));
        fxmlLoader.setControllerFactory(controllerClass -> {
            if (controllerClass == MenuController.class) {
                MenuController controller = new MenuController();
                controller.setStage(stage);
                return controller;
            } else {
                try {
                    return controllerClass.newInstance();
                } catch (Exception exc) {
                    throw new RuntimeException(exc);
                }
            }
        });
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

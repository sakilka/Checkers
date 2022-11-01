package org.sample.checkers.menu;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.sample.checkers.chess.BoardPosition;

import static org.sample.checkers.config.game.GamePropertyUtil.getBoardConfig;

public class RightPanel extends BorderPane {

    private double buttonWidth = 0;
    private boolean mouseDragOnDivider = false;

    public RightPanel(DoubleProperty splitPaneDividerPosition, ReadOnlyDoubleProperty heightProperty,
                      ReadOnlyDoubleProperty sceneWidth, BooleanProperty shown, SubScene boardScene,
                      BoardPosition boardPosition) {
        setMinWidth(0);
        buttonWidth = 26;

        ToggleButton toggleButton = new ToggleButton();
        toggleButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("left-arrow.png"),
                10, 10, false, false)));
        HBox buttonBox = new HBox(0, toggleButton);

        toggleButton.prefHeightProperty().bind(heightProperty);
        shown.bindBidirectional(toggleButton.selectedProperty());

        TabPane panelPane = new TabPane();

        panelPane.getTabs().add(new Tab("Border position", (new PositionTab(boardPosition))));
        panelPane.setMinWidth(0);
        panelPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        panelPane.setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255, 1), null, null)));

        toggleButton.setOnAction(event -> {
            KeyValue end;
            setMinWidth(0);
            if (toggleButton.isSelected()) {
                setCenter(panelPane);
                toggleButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("right-arrow.png"),
                        10, 10, false, false)));
                end = new KeyValue(splitPaneDividerPosition, 1 - (getBoardConfig().getRightPanelWidth() / sceneWidth.doubleValue()));
                boardScene.widthProperty().bind(sceneWidth.subtract(getBoardConfig().getRightPanelWidth()));
            } else {
                getChildren().remove(panelPane);
                toggleButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("left-arrow.png"),
                        10, 10, false, false)));
                end = new KeyValue(splitPaneDividerPosition, 1);
            }
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), end));
            timeline.play();
            timeline.setOnFinished(eventTimeline -> boardScene.widthProperty().
                    bind(toggleButton.isSelected() ? sceneWidth.subtract(getBoardConfig().getRightPanelWidth()) :
                            sceneWidth.subtract(toggleButton.getWidth())));
        });

        toggleButton.setSelected(false);
        setLeft(buttonBox);
        setCenter(panelPane);
        setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255, 1), null, null)));
    }

    public double getButtonWidth() {
        return buttonWidth;
    }

    public void disableDrag(SplitPane content) {
        content.requestLayout();
        content.applyCss();
        for (Node node : content.lookupAll(".split-pane-divider")) {
            node.setOnMousePressed(evMouseReleased -> mouseDragOnDivider = true);
            node.setOnMouseReleased(evMouseReleased -> mouseDragOnDivider = false);
        }
    }

    public boolean isMouseDragOnDivider() {
        return mouseDragOnDivider;
    }
}

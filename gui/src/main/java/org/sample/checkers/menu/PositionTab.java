package org.sample.checkers.menu;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.sample.checkers.chess.BoardPosition;

import static org.sample.checkers.config.game.GamePropertyUtil.getBoardConfig;

public class PositionTab extends ScrollPane {

    public PositionTab(BoardPosition boardPosition) {
        VBox content = new VBox();
        PositionPanel panelAngleX = new PositionPanel(boardPosition.angleXProperty(), -100, 100,
                - boardPosition.angleXProperty().get(),
                getBoardConfig().getRightPanelWidth() -20, 20, "angleX", FontAwesomeIcon.ROTATE_LEFT,
                FontAwesomeIcon.ROTATE_RIGHT, 1);
        content.getChildren().add(panelAngleX);
        PositionPanel panelAngleY = new PositionPanel(boardPosition.angleYProperty(), -100, 100,
                -boardPosition.angleYProperty().get(),
                getBoardConfig().getRightPanelWidth() -20, 20, "angleY", FontAwesomeIcon.ROTATE_LEFT,
                FontAwesomeIcon.ROTATE_RIGHT, 1);
        content.getChildren().add(panelAngleY);
        PositionPanel panelDeltaX = new PositionPanel(boardPosition.deltaXProperty(), -100, 100,
                - boardPosition.deltaXProperty().get(),
                getBoardConfig().getRightPanelWidth() -20, 20, "deltaX", FontAwesomeIcon.ARROW_RIGHT,
                FontAwesomeIcon.ARROW_LEFT, -1);
        content.getChildren().add(panelDeltaX);
        PositionPanel panelDeltaY = new PositionPanel(boardPosition.deltaZProperty(), -100, 100,
                - boardPosition.deltaZProperty().get(),
                getBoardConfig().getRightPanelWidth() -20, 20, "deltaZ", FontAwesomeIcon.ARROW_DOWN,
                FontAwesomeIcon.ARROW_UP, 1);
        content.getChildren().add(panelDeltaY);
        PositionPanel panelZoom = new PositionPanel(boardPosition.translateZProperty(), -100, 100,
                - boardPosition.translateZProperty().get(),
                getBoardConfig().getRightPanelWidth() -20, 20, "zoom", FontAwesomeIcon.SEARCH_PLUS,
                FontAwesomeIcon.SEARCH_MINUS, 1);
        content.getChildren().add(panelZoom);
        setContent(content);
    }
}

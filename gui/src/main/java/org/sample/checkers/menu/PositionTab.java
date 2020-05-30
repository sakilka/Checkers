package org.sample.checkers.menu;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import org.sample.checkers.board.model.BoardPosition;

import static org.sample.checkers.Checkers.RIGHT_PANEL_WIDTH;

public class PositionTab extends ScrollPane {

    public PositionTab(BoardPosition boardPosition) {
        VBox content = new VBox();
        PositionPanel panelAngleX = new PositionPanel(boardPosition.angleXProperty(), -100, 100, 0,
                RIGHT_PANEL_WIDTH -20, 20, "angleX", FontAwesomeIcon.ROTATE_LEFT,
                FontAwesomeIcon.ROTATE_RIGHT, 1);
        content.getChildren().add(panelAngleX);
        PositionPanel panelAngleY = new PositionPanel(boardPosition.angleYProperty(), -100, 100, 0,
                RIGHT_PANEL_WIDTH -20, 20, "angleY", FontAwesomeIcon.ROTATE_LEFT,
                FontAwesomeIcon.ROTATE_RIGHT, 1);
        content.getChildren().add(panelAngleY);
        PositionPanel panelDeltaX = new PositionPanel(boardPosition.deltaXProperty(), -100, 100, 0,
                RIGHT_PANEL_WIDTH -20, 20, "deltaX", FontAwesomeIcon.ARROW_RIGHT,
                FontAwesomeIcon.ARROW_LEFT, -1);
        content.getChildren().add(panelDeltaX);
        PositionPanel panelDeltaY = new PositionPanel(boardPosition.deltaYProperty(), -100, 100, 0,
                RIGHT_PANEL_WIDTH -20, 20, "deltaZ", FontAwesomeIcon.ARROW_DOWN,
                FontAwesomeIcon.ARROW_UP, 1);
        content.getChildren().add(panelDeltaY);
        PositionPanel panelZoom = new PositionPanel(boardPosition.translateZProperty(), -100, 100, 0,
                RIGHT_PANEL_WIDTH -20, 20, "zoom", FontAwesomeIcon.SEARCH_PLUS,
                FontAwesomeIcon.SEARCH_MINUS, 1);
        content.getChildren().add(panelZoom);
        setContent(content);
//        setAlignment(Pos.TOP_CENTER);
//        setHgap(2);
//        setVgap(2);
//        setPadding(new Insets(5, 5, 5, 5));

//        Label angleX = new Label("angleX : " + boardPosition.getAngleX());
//        angleX.setMinWidth(0);
//        boardPosition.angleXProperty().addListener((obs, oldVal, newVal) -> {
//            angleX.setText("angleX : " + boardPosition.getAngleX());
//        });
//
//        Label angleY = new Label("angleY : " + boardPosition.getAngleY());
//        angleY.setMinWidth(0);
//        boardPosition.angleYProperty().addListener((obs, oldVal, newVal) -> {
//            angleY.setText("angleY : " + boardPosition.getAngleY());
//        });
//
//        Label deltaX = new Label("deltaX : " + boardPosition.getDeltaX());
//        deltaX.setMinWidth(0);
//        boardPosition.deltaXProperty().addListener((obs, oldVal, newVal) -> {
//            deltaX.setText("deltaX : " + boardPosition.getDeltaX());
//        });
//
//        Label deltaY = new Label("deltaY : " + boardPosition.getDeltaY());
//        deltaY.setMinWidth(0);
//        boardPosition.deltaYProperty().addListener((obs, oldVal, newVal) -> {
//            deltaY.setText("deltaY : " + boardPosition.getDeltaY());
//        });

//        Slider sliderZ = new Slider(-100, 100, 0);
//        sliderZ.setShowTickLabels(true);
//        sliderZ.setSnapToTicks(true);
//        sliderZ.setShowTickMarks(true);
//        sliderZ.setMajorTickUnit(10);
//        sliderZ.setMinorTickCount(1);
//        sliderZ.setBlockIncrement(1);
//        sliderZ.valueProperty().addListener((observableValue, oldValue, newValue) ->
//                boardPosition.translateZProperty().setValue(newValue.doubleValue() *-1));
//        boardPosition.translateZProperty().addListener((observableValue, oldValue, newValue) ->
//                sliderZ.valueProperty().setValue(newValue.doubleValue() *-1));
//        sliderZ.setMaxWidth(250);
//        sliderZ.setMinWidth(250);
//
//        Button increaseZoom = new Button();
//
////        GlyphIcon icon = GlyphsBuilder.create(FontAwesomeIconView.class).icon(FontAwesomeIcon.ADJUST)
////                .size("12em").style("-fx-fill: linear-gradient(#70b4e5 0%, #247cbc 70%, #2c85c1 85%,)").build();
//
//        GlyphIcon searchPlus = GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.SEARCH_PLUS)
//                .size("2em").style("-fx-color: black").build();
//        increaseZoom.setGraphic(searchPlus);
//
////        increaseZoom.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("zoom-in-512.jpg"),
////                16, 16, true, true)));
//        increaseZoom.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                boardPosition.translateZProperty().set(boardPosition.getTranslateZ() - 1);
//            }
//        });
//
//        Timeline timelineIncreaseZoom = new Timeline(
//                new KeyFrame(Duration.millis(100), e -> {
//                    boardPosition.translateZProperty().set(boardPosition.getTranslateZ() - 1);
//                })
//        );
//        timelineIncreaseZoom.setCycleCount(Animation.INDEFINITE);
//        timelineIncreaseZoom.setDelay(Duration.millis(300));
//
//        increaseZoom.pressedProperty().addListener((obs, wPressed, pressed) -> {
//            if (pressed) {
//                timelineIncreaseZoom.play();
//            } else {
//                timelineIncreaseZoom.stop();
//            }
//        });
//
//        Button decreaseZoom = new Button();
//
//        GlyphIcon searchMinus = GlyphsBuilder.create(FontAwesomeIconView.class).glyph(FontAwesomeIcon.SEARCH_MINUS)
//                .size("2em").style("-fx-color: black").build();
//        decreaseZoom.setGraphic(searchMinus);
//
//        decreaseZoom.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                boardPosition.translateZProperty().set(boardPosition.getTranslateZ() + 1);
//            }
//        });
//
//        Timeline timelineDecreaseZoom = new Timeline(
//                new KeyFrame(Duration.millis(100), e -> {
//                    boardPosition.translateZProperty().set(boardPosition.getTranslateZ() + 1);
//                })
//        );
//        timelineDecreaseZoom.setCycleCount(Animation.INDEFINITE);
//
//        decreaseZoom.pressedProperty().addListener((obs, wPressed, pressed) -> {
//            if (pressed) {
//                timelineDecreaseZoom.play();
//            } else {
//                timelineDecreaseZoom.stop();
//            }
//        });
//
//        Label zoom = new Label("zoom");
//        Label zoomValue = new Label(String.valueOf(round(boardPosition.getTranslateZ())));
//
//        boardPosition.translateZProperty().addListener((obs, oldVal, newVal) -> {
//            zoomValue.setText(String.valueOf(
//                    round(boardPosition.translateZProperty().multiply(-1).doubleValue())));
//        });

//        add(angleX, 0, 0);
//        add(angleY, 0, 2);
//        add(zoom, 0, 4);
//        add(zoomValue, 1, 4);
//        add(decreaseZoom, 2, 4);
//        add(increaseZoom, 3, 4);
//        add(sliderZ, 0, 5, 4, 1);
//        add(deltaX, 0, 6);
//        add(deltaY, 0, 8);
    }
}

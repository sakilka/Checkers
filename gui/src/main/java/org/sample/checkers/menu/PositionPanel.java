package org.sample.checkers.menu;

import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.GlyphsBuilder;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;
import org.sample.checkers.board.model.BoardPosition;

import static java.lang.StrictMath.round;

public class PositionPanel extends GridPane {

    public PositionPanel(SimpleDoubleProperty property, double min, double max, double def, double preWidth,
                         double prefHeight, String labelText, FontAwesomeIcon icon1, FontAwesomeIcon icon2,
                         int diffValue) {
        Slider slider = initializeSlider(property, min, max, def);
        Button buttonIncrease = initializeButton(property, icon1, +diffValue);
        Button buttonDecrease = initializeButton(property, icon2, -diffValue);
        Label value = initializeValueLabel(property);
        Label label = new Label(labelText);

        add(buttonIncrease, 4, 0);
        add(buttonDecrease, 1, 0);
        add(label, 2, 0);
        add(value, 3, 0);
        add(slider, 1, 1, 4, 1);

        GridPane.setHalignment(this, HPos.CENTER);
        GridPane.setValignment(this, VPos.CENTER);
        setAlignment(Pos.CENTER);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(0);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(25);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(25);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(25);
        ColumnConstraints col5 = new ColumnConstraints();
        col4.setPercentWidth(25);
        getColumnConstraints().addAll(col1, col2, col3, col4, col5);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(50);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(50);
        getRowConstraints().addAll(row1, row2);

        setMinSize(preWidth, prefHeight);
    }

    private Label initializeValueLabel(SimpleDoubleProperty property) {
        Label value = new Label(String.valueOf(round(-property.doubleValue())));

        property.addListener((obs, oldVal, newVal) -> {
            value.setText(String.valueOf(
                    round(property.multiply(-1).doubleValue())));
        });
        return value;
    }

    private Button initializeButton(SimpleDoubleProperty property, FontAwesomeIcon icon, int addition) {
        Button button = new Button();
        GlyphIcon searchPlus = GlyphsBuilder.create(FontAwesomeIconView.class).glyph(icon)
                .size("2em").style("-fx-color: black").build();
        button.setGraphic(searchPlus);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                property.set(property.doubleValue() - addition);
            }
        });

        Timeline timelineIncreaseZoom = new Timeline(
                new KeyFrame(Duration.millis(100), e -> {
                    property.set(property.doubleValue() - addition);
                })
        );
        timelineIncreaseZoom.setCycleCount(Animation.INDEFINITE);
        timelineIncreaseZoom.setDelay(Duration.millis(300));

        button.pressedProperty().addListener((obs, wPressed, pressed) -> {
            if (pressed) {
                timelineIncreaseZoom.play();
            } else {
                timelineIncreaseZoom.stop();
            }
        });

        return button;
    }

    private Slider initializeSlider(SimpleDoubleProperty property, double min, double max, double def) {
        Slider slider = new Slider(min, max, def);
        slider.setShowTickLabels(true);
        slider.setSnapToTicks(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(10);
        slider.setMinorTickCount(1);
        slider.setBlockIncrement(1);
        slider.valueProperty()
                .addListener((observableValue, oldValue, newValue) -> property.setValue(newValue.doubleValue() *-1));
        property
                .addListener((observableValue, oldValue, newValue) -> slider.valueProperty().setValue(newValue.doubleValue() *-1));
        slider.setMaxWidth(250);
        slider.setMinWidth(250);
        return slider;
    }
}

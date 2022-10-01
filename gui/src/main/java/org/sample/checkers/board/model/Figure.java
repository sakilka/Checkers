package org.sample.checkers.board.model;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import org.sample.checkers.config.ChessFigure;
import org.sample.checkers.config.ChessSide;

import static javafx.scene.transform.Rotate.Y_AXIS;
import static org.sample.checkers.board.model.EffectUtils.createImage;

public class Figure extends LoadObject {

    private static final double NO_ROTATE = 0;

    private final PhongMaterial DEFAULT_MATERIAL;
    private final ChessFigure chessFigure;
    private final ChessSide chessSide;

    public Figure(String modelName, FigurePosition position, PhongMaterial material, ChessFigure chessFigure, ChessSide chessSide) {
        this(modelName, 1, position, material, Y_AXIS, NO_ROTATE, chessFigure, chessSide);
    }

    public Figure(String modelName, FigurePosition position, PhongMaterial material, Point3D axis, double rotation, ChessFigure chessFigure, ChessSide chessSide) {
        this(modelName, 1, position, material, axis, rotation, chessFigure, chessSide);
    }

    public Figure(String modelName, int size, FigurePosition position, PhongMaterial material, Point3D axis, double rotation, ChessFigure chessFigure, ChessSide chessSide) {
        super(modelName, size);

        DEFAULT_MATERIAL = material;
        this.chessFigure = chessFigure;
        this.chessSide = chessSide;

        this.setMaterial(material);
        this.setTranslateX(position.getTranslateX());
        this.setTranslateY(position.getTranslateY());
        this.setTranslateZ(position.getTranslateZ());
        if (NO_ROTATE != rotation) {
            this.setRotationAxis(axis);
            this.setRotate(rotation);
        }
    }

    public void highlightFigure(boolean on, Color color){
        double power = on ? 0.9 : 1.0;
        Image texture = createImage(Color.rgb(0,0,0, power), on ? color : Color.BLACK);
        ((PhongMaterial) this.getMaterial()).setSelfIlluminationMap(texture);
    }

    public void changeMaterialToDefault() {
        this.setMaterial(DEFAULT_MATERIAL);
    }

    public ChessFigure getChessFigure() {
        return chessFigure;
    }

    public ChessSide getChessSide() {
        return chessSide;
    }
}

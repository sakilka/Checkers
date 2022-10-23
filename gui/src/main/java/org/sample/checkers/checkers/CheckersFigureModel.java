package org.sample.checkers.checkers;

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import org.sample.checkers.chess.FigurePosition;
import org.sample.checkers.config.checkers.CheckersFigure;
import org.sample.checkers.config.checkers.CheckersSide;

import static javafx.scene.transform.Rotate.Y_AXIS;
import static org.sample.checkers.chess.EffectUtils.createImage;

public class CheckersFigureModel extends CheckersLoadObject {

    private static final double NO_ROTATE = 0;

    private final PhongMaterial DEFAULT_MATERIAL;
    private final CheckersFigure checkersFigure;
    private final CheckersSide checkersSide;

    public CheckersFigureModel(String modelName, FigurePosition position, PhongMaterial material, CheckersFigure checkersFigure, CheckersSide checkersSide) {
        this(modelName, 1, position, material, Y_AXIS, NO_ROTATE, checkersFigure, checkersSide);
    }

    public CheckersFigureModel(String modelName, FigurePosition position, PhongMaterial material, Point3D axis, double rotation, CheckersFigure checkersFigure, CheckersSide checkersSide) {
        this(modelName, 1, position, material, axis, rotation, checkersFigure, checkersSide);
    }

    public CheckersFigureModel(String modelName, int size, FigurePosition position, PhongMaterial material, Point3D axis, double rotation, CheckersFigure checkersFigure, CheckersSide checkersSide) {
        super(modelName, size);

        DEFAULT_MATERIAL = material;
        this.checkersFigure = checkersFigure;
        this.checkersSide = checkersSide;

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

    public CheckersFigure getCheckersFigure() {
        return checkersFigure;
    }

    public CheckersSide getCheckersSide() {
        return checkersSide;
    }
}

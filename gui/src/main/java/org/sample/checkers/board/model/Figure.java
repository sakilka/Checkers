package org.sample.checkers.board.model;

import javafx.geometry.Point3D;
import javafx.scene.paint.PhongMaterial;
import org.sample.checkers.config.ChessFigure;
import org.sample.checkers.config.ChessSide;

import java.io.File;

import static javafx.scene.transform.Rotate.Y_AXIS;

public class Figure extends LoadObject {

    private static final double NO_ROTATE = 0;

    private final PhongMaterial DEFAULT_MATERIAL;
    private final ChessFigure chessFigure;
    private final ChessSide chessSide;

    public Figure(String path, FigurePosition position, PhongMaterial material, ChessFigure chessFigure, ChessSide chessSide) {
        this(new File((Figure.class.getResource(path).getFile())), 1, position, material, Y_AXIS, NO_ROTATE, chessFigure, chessSide);
    }

    public Figure(String path, FigurePosition position, PhongMaterial material, Point3D axis, double rotation, ChessFigure chessFigure, ChessSide chessSide) {
        this(new File((Figure.class.getResource(path).getFile())), 1, position, material, axis, rotation, chessFigure, chessSide);
    }

    public Figure(File f, FigurePosition position, PhongMaterial material, ChessFigure chessFigure, ChessSide chessSide) {
        this(f, 1, position, material, Y_AXIS, 0, chessFigure, chessSide);
    }

    public Figure(File f, int size, FigurePosition position, PhongMaterial material, Point3D axis, double rotation, ChessFigure chessFigure, ChessSide chessSide) {
        super(f, size);

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

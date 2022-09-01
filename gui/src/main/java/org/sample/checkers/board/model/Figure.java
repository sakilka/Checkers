package org.sample.checkers.board.model;

import javafx.geometry.Point3D;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Rotate;

import java.io.File;

import static javafx.scene.transform.Rotate.Y_AXIS;

public class Figure extends LoadObject {

    private static final double NO_ROTATE = 0;

    public Figure(String path, FigurePosition position, PhongMaterial material) {
        this(new File((Figure.class.getResource(path).getFile())), 1, position, material, Y_AXIS, NO_ROTATE);
    }

    public Figure(String path, FigurePosition position, PhongMaterial material, Point3D axis, double rotation) {
        this(new File((Figure.class.getResource(path).getFile())), 1, position, material, axis, rotation);
    }

    public Figure(File f, FigurePosition position, PhongMaterial material) {
        this(f, 1, position, material, Y_AXIS, 0);
    }

    public Figure(File f, int size, FigurePosition position, PhongMaterial material, Point3D axis, double rotation) {
        super(f, size);

        this.setMaterial(material);
        this.setTranslateX(position.getTranslateX());
        this.setTranslateY(position.getTranslateY());
        this.setTranslateZ(position.getTranslateZ());
        if (NO_ROTATE != rotation) {
            this.setRotationAxis(axis);
            this.setRotate(rotation);
        }
    }
}

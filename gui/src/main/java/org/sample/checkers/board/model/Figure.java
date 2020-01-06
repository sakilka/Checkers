package org.sample.checkers.board.model;

import javafx.scene.paint.PhongMaterial;

import java.io.File;

public class Figure extends LoadObject {

    public Figure(String path, FigurePosition position, PhongMaterial material) {
        this(new File((Figure.class.getResource(path).getFile())), 1, position, material);
    }

    public Figure(File f, FigurePosition position, PhongMaterial material) {
        this(f, 1, position, material);
    }

    public Figure(File f, int size, FigurePosition position, PhongMaterial material) {
        super(f, size);

        this.setMaterial(material);
        this.setTranslateX(position.getTranslateX());
        this.setTranslateY(position.getTranslateY());
        this.setTranslateZ(position.getTranslateZ());
    }
}

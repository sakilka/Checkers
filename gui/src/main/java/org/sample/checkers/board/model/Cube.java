package org.sample.checkers.board.model;

import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class Cube extends MeshView {

    private static final float DEFAULT_HEIGHT = 150;
    private static final float DEFAULT_WIDTH = 300;
    private static final float DEFAULT_TIGHT = 250;

    public Cube() {
        this(DEFAULT_HEIGHT, DEFAULT_WIDTH, DEFAULT_TIGHT);
    }

    public Cube(float height, float width, float tight) {
        TriangleMesh cubeMesh = new TriangleMesh();
        setTextureCoordinates(cubeMesh);
        definePoints(cubeMesh, height, width, tight);
        defineFaces(cubeMesh);

        super.setMesh(cubeMesh);
        super.setDrawMode(DrawMode.FILL);
    }

    private void setTextureCoordinates(TriangleMesh cubeMesh) {
        cubeMesh.getTexCoords().addAll(
                0, 0
        );
    }

    private void definePoints(TriangleMesh cubeMesh, float height, float width, float tight) { // body
        cubeMesh.getPoints().addAll(
                0, 0, 0,
                1 * width, 0, 0,
                1 * width, 0, 1 * tight,
                0, 0, 1 * tight,
                0, 1 * height, 0,
                1 * width, 1 * height, 0,
                1 * width, 1 * height, 1 * tight,
                0, 1 * height, 1 * tight
        );
    }

    private void defineFaces(TriangleMesh cubeMesh) { // steny
        cubeMesh.getFaces().addAll(
                0, 0, 1, 0, 2, 0, //VRCH
                0, 0, 2, 0, 3, 0,

                4, 0, 0, 0, 3, 0, //LAVA
                4, 0, 3, 0, 7, 0,

                6, 0, 7, 0, 3, 0, //ZADNA
                6, 0, 3, 0, 2, 0,

                1, 0, 5, 0, 6, 0, //PRAVA
                1, 0, 6, 0, 2, 0,

                0, 0, 4, 0, 5, 0, //PREDOK
                1, 0, 0, 0, 5, 0,

                7, 0, 5, 0, 4, 0, //SPODOK
                7, 0, 6, 0, 5, 0
        );
    }
}
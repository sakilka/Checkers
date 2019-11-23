package org.sample.checkers.mesh.mesh;

import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class Pyramid extends MeshView {

    private static final float DEFAULT_HEIGHT = 150;                    // Height
    private static final float DEFAULT_SIDE = 300;                    // Side

    public Pyramid() {
        this(DEFAULT_HEIGHT, DEFAULT_SIDE);
    }

    public Pyramid(float height, float side) {
        TriangleMesh pyramidMesh = new TriangleMesh();
        setTextureCoordinates(pyramidMesh);
        definePoints(pyramidMesh, height, side);
        defineFaces(pyramidMesh, height, side);

        super.setMesh(pyramidMesh);
        super.setDrawMode(DrawMode.FILL);
    }

    private void setTextureCoordinates(TriangleMesh pyramidMesh) {
        pyramidMesh.getTexCoords().addAll(0, 0);
    }

    private void definePoints(TriangleMesh pyramidMesh, float height, float side) {
        pyramidMesh.getPoints().addAll(
                0, 0, 0,                // Point 0 - Top
                0, height, -side / 2,           // Point 1 - Front
                -side / 2, height, 0,           // Point 2 - Left
                side / 2, height, 0,            // Point 3 - Back
                0, height, side / 2             // Point 4 - Right
        );
    }

    private void defineFaces(TriangleMesh pyramidMesh, float height, float side) {
        pyramidMesh.getFaces().addAll(
                0, 0, 2, 0, 1, 0,          // Front left face
                0, 0, 1, 0, 3, 0,          // Front right face
                0, 0, 3, 0, 4, 0,          // Back right face
                0, 0, 4, 0, 2, 0,          // Back left face
                4, 0, 1, 0, 2, 0,          // Bottom rear face
                4, 0, 3, 0, 1, 0           // Bottom front face
        );
    }
}

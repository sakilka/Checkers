package org.sample.checkers.mesh.mesh;

import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class OktaHedron extends MeshView {

    public OktaHedron(float side) {
        TriangleMesh oktaHedron = new TriangleMesh();
        setTextureCoordinates(oktaHedron);
        definePoints(oktaHedron, side);
        defineFaces(oktaHedron);
        defineTextures(oktaHedron);

        super.setMesh(oktaHedron);
        super.setDrawMode(DrawMode.FILL);
    }

    private void setTextureCoordinates(TriangleMesh pyramidMesh) {
        pyramidMesh.getTexCoords().addAll(0, 0);
    }

    private void definePoints(TriangleMesh oktaHedron, float side) { // body
        oktaHedron.getPoints().addAll(
                0f * side, 0f * side, -0.951057f * side,
                0f * side, 0f * side, 0.951057f * side,
                -0.850651f * side, 0f * side, -0.425325f * side,
                0.850651f * side, 0f * side, 0.425325f * side,
                0.688191f * side, -0.5f * side, -0.425325f * side,
                0.688191f * side, 0.5f * side, -0.425325f * side,
                -0.688191f * side, -0.5f * side, 0.425325f * side,
                -0.688191f * side, 0.5f * side, 0.425325f * side,
                -0.262866f * side, -0.809017f * side, -0.425325f * side,
                -0.262866f * side, 0.809017f * side, -0.425325f * side,
                0.262866f * side, -0.809017f * side, 0.425325f * side,
                0.262866f * side, 0.809017f * side, 0.425325f * side
        );
    }

    private void defineFaces(TriangleMesh oktaHedron) { // steny
        oktaHedron.getFaces().addAll(
                1, 6, 11, 5, 7, 0,
                1, 12, 7, 11, 6, 5,
                1, 7, 6, 6, 10, 1,
                1, 13, 10, 12, 3, 6,
                1, 8, 3, 7, 11, 2,
                4, 14, 8, 13, 0, 7,
                5, 9, 4, 8, 0, 3,
                9, 15, 5, 14, 0, 8,
                2, 10, 9, 9, 0, 4,
                8, 16, 2, 15, 0, 9,
                11, 5, 9, 6, 7, 12,
                7, 11, 2, 12, 6, 17,
                6, 6, 8, 7, 10, 13,
                10, 12, 4, 13, 3, 18,
                3, 7, 5, 8, 11, 14,
                4, 13, 10, 14, 8, 19,
                5, 8, 3, 9, 4, 15,
                9, 14, 11, 15, 5, 20,
                2, 9, 7, 10, 9, 16,
                8, 15, 6, 16, 2, 21
        );
    }

    private void defineTextures(TriangleMesh oktaHedron) {
        oktaHedron.getTexCoords().addAll(
                0.181818f, 0f,
                0.363636f, 0f,
                0.545455f, 0f,
                0.727273f, 0f,
                0.909091f, 0f,
                0.0909091f, 0.333333f,
                0.272727f, 0.333333f,
                0.454545f, 0.333333f,
                0.636364f, 0.333333f,
                0.818182f, 0.333333f,
                1f, 0.333333f,
                0f, 0.666667f,
                0.181818f, 0.666667f,
                0.363636f, 0.666667f,
                0.545455f, 0.666667f,
                0.727273f, 0.666667f,
                0.909091f, 0.666667f,
                0.0909091f, 1f,
                0.272727f, 1f,
                0.454545f, 1f,
                0.636364f, 1f,
                0.818182f, 1f
        );
    }
}

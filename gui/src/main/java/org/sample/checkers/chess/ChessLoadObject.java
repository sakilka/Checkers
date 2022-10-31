package org.sample.checkers.chess;

import javafx.scene.CacheHint;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import org.sample.checkers.config.chess.ChessLoadModel;
import org.sample.checkers.config.chess.ChessPropertyUtil;
import org.sample.checkers.loader.model.Model;

public class ChessLoadObject extends MeshView {

    public ChessLoadObject(String modelName, int size) {

        Model model = null;

        ChessLoadModel chessLoadModel = ChessPropertyUtil.getModel();
        model = chessLoadModel.getModel().get(modelName);

        TriangleMesh mesh = new TriangleMesh();

        setTextureCoordinates(mesh);

        definePoints(mesh, model, size);
        defineFaces(mesh, model);
        //defineSmoothingGroups(mesh); used only for small face size

        super.setMesh(mesh);
        super.setDrawMode(DrawMode.FILL);
        super.setCache(true);
        super.setCacheHint(CacheHint.SPEED);
    }

    private void setTextureCoordinates(TriangleMesh pyramidMesh) {
        pyramidMesh.getTexCoords().addAll(0, 0);
    }

    private void definePoints(TriangleMesh pyramidMesh, Model model, int size) { // body

        float[] vertexArray = new float[model.vertices.size() * 3];

        for (int i = 0; i < model.vertices.size(); i++) {
            vertexArray[i * 3] = model.vertices.get(i).x * size;
            vertexArray[(i * 3) + 1] = model.vertices.get(i).y * size;
            vertexArray[(i * 3) + 2] = model.vertices.get(i).z * size;
        }

        pyramidMesh.getPoints().addAll(
                vertexArray
        );
    }

    private void defineFaces(TriangleMesh pyramidMesh, Model model) { // steny

        int[] faces = new int[model.faces.size() * 6];

        for (int i = 0; i < model.faces.size(); i++) {
            faces[i * 6] = (int) model.faces.get(i).vertex.x - 1;
            faces[(i * 6) + 1] = 0;
            faces[(i * 6) + 2] = (int) model.faces.get(i).vertex.y - 1;
            faces[(i * 6) + 3] = 0;
            faces[(i * 6) + 4] = (int) model.faces.get(i).vertex.z - 1;
            faces[(i * 6) + 5] = 0;
        }

        pyramidMesh.getFaces().addAll(
                faces
        );
    }

    private void defineSmoothingGroups(TriangleMesh mesh) {
        int [] smoothing = new int[mesh.getFaces().size() / mesh.getFaceElementSize()];
        mesh.getFaceSmoothingGroups().addAll(smoothing);
    }
}

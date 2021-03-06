package org.sample.checkers.board.model;

import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import org.sample.checkers.loader.ObjLoader;
import org.sample.checkers.loader.model.Model;

import java.io.File;
import java.io.IOException;

public class LoadObject extends MeshView {

    public LoadObject(File f, int size) {

        Model model = null;
        try {
            model = ObjLoader.loadModel(f);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        TriangleMesh mesh = new TriangleMesh();

        setTextureCoordinates(mesh);

        definePoints(mesh, model, size);
        defineFaces(mesh, model);
        //defineSmoothingGroups(mesh); used only for small face size

        super.setMesh(mesh);
        super.setDrawMode(DrawMode.FILL);
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

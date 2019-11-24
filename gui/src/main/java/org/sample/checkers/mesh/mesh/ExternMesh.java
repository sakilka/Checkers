package org.sample.checkers.mesh.mesh;

import javafx.collections.ObservableFloatArray;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class ExternMesh extends MeshView {

    public ExternMesh(String path, int size) {
//        ObjModelImporter importer = new ObjModelImporter();
//        importer.read(path);
//
//        MeshView customObject = importer.getImport()[0];
//
//        resize(size, (TriangleMesh) customObject.getMesh());
//        setMesh(customObject.getMesh());
    }

    private void resize(int size, TriangleMesh mesh) {
        ObservableFloatArray points = (mesh).getPoints();
        float[] floats = new float[points.size()];
        float[] resized = new float[points.size()];
        int p = 0;
        for (float f : points.toArray(floats)) {
            resized[p] = f * size;
            p++;
        }
        (mesh).getPoints().setAll(resized);
    }
}

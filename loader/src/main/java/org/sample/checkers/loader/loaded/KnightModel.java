package org.sample.checkers.loader.loaded;

import org.sample.checkers.loader.model.Model;

public class KnightModel extends Model {

    public KnightModel() {
        super("knight");

        this.vertices = loadVertices();
        this.normals = loadNormals();
        this.faces = loadFaces();
    }
}

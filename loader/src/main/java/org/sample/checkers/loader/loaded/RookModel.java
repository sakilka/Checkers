package org.sample.checkers.loader.loaded;

import org.sample.checkers.loader.model.Model;

public class RookModel extends Model {

    public RookModel() {
        super("rook");

        this.vertices = loadVertices();
        this.normals = loadNormals();
        this.faces = loadFaces();
    }
}

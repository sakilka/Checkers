package org.sample.checkers.loader.loaded;

import org.sample.checkers.loader.model.Model;

public class QueenModel extends Model {

    public QueenModel() {
        super("queen");

        this.vertices = loadVertices();
        this.normals = loadNormals();
        this.faces = loadFaces();
    }
}

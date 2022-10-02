package org.sample.checkers.loader.loaded;

import org.sample.checkers.loader.model.Model;

public class KingModel extends Model {

    public KingModel() {
        super("king");

        this.vertices = loadVertices();
        this.normals = loadNormals();
        this.faces = loadFaces();
    }
}

package org.sample.checkers.loader.loaded;

import org.sample.checkers.loader.model.Model;

public class BishopModel extends Model {

    public BishopModel() {
        super("bishop");

        this.vertices = loadVertices();
        this.normals = loadNormals();
        this.faces = loadFaces();
    }
}

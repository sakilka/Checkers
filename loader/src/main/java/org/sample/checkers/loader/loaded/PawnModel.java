package org.sample.checkers.loader.loaded;

import org.sample.checkers.loader.model.Model;

public class PawnModel extends Model {

    public PawnModel() {
        super("pawn");

        this.vertices = loadVertices();
        this.normals = loadNormals();
        this.faces = loadFaces();
    }
}
package org.sample.checkers.loader.model;

import java.io.Serializable;

public class Face implements Serializable {

    //default serialVersion id
    private static final long serialVersionUID = 1L;

    public Vector3f vertex = new Vector3f();
    public Vector3f normal = new Vector3f();

    public Face(Vector3f vertex, Vector3f normal) {
        this.vertex = vertex;
        this.normal = normal;
    }
}

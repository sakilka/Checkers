package org.sample.checkers.loader.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Model implements Serializable {

    //default serialVersion id
    private static final long serialVersionUID = 1L;
    private static final String LOAD = "/org/sample/checkers/board/data/";

    public String modelName;

    public List<Vector3f> vertices = new ArrayList<Vector3f>();
    public List<Vector3f> normals = new ArrayList<Vector3f>();
    public List<Face> faces = new ArrayList<Face>();

    public Model(String modelName) {
        this.modelName = modelName;
    }

    protected List<Vector3f> loadVertices() {
        InputStream inputStream = null;
        try {
            List<Vector3f> vector3fs = new ArrayList<>();
            inputStream = this.getClass().getResourceAsStream(LOAD + modelName + "_vertices");

            if(inputStream == null) {
                return null;
            }

            DataInputStream dataInputStr = new DataInputStream(inputStream);

            while (dataInputStr.available() > 0) {
                vector3fs.add(new Vector3f(dataInputStr.readFloat(), dataInputStr.readFloat(), dataInputStr.readFloat()));
            }

            return vector3fs;
        } catch (IOException e) {
            return null;
        }
    }

    protected List<Vector3f> loadNormals() {
        InputStream inputStream = null;
        try {
            List<Vector3f> vector3fs = new ArrayList<>();
            inputStream = this.getClass().getResourceAsStream(LOAD + modelName + "_normals");

            if(inputStream == null) {
                return null;
            }

            DataInputStream dataInputStr = new DataInputStream(inputStream);

            while (dataInputStr.available() > 0) {
                vector3fs.add(new Vector3f(dataInputStr.readFloat(), dataInputStr.readFloat(), dataInputStr.readFloat()));
            }

            return vector3fs;
        } catch (IOException e) {
            return null;
        }
    }

    protected List<Face> loadFaces() {
        InputStream inputStream = null;
        try {
            List<Face> faces = new ArrayList<>();
            inputStream = this.getClass().getResourceAsStream(LOAD + modelName + "_faces");

            if(inputStream == null) {
                return null;
            }

            DataInputStream dataInputStr = new DataInputStream(inputStream);

            while (dataInputStr.available() > 0) {
                faces.add(new Face(new Vector3f(dataInputStr.readFloat(), dataInputStr.readFloat(), dataInputStr.readFloat()),
                        new Vector3f(dataInputStr.readFloat(), dataInputStr.readFloat(), dataInputStr.readFloat())));
            }

            return faces;
        } catch (IOException e) {
            return null;
        }
    }
}

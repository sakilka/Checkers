package org.sample.checkers.board.model;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.DepthTest;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class Cube extends MeshView {

    private static final float DEFAULT_HEIGHT = 150;
    private static final float DEFAULT_WIDTH = 300;
    private static final float DEFAULT_DEPTH = 250;

    private Material DEFAULT_MATERIAL = null;

    public Cube() {
        this(DEFAULT_HEIGHT, DEFAULT_WIDTH, DEFAULT_DEPTH);
    }

    private float height;
    private float width;
    private float depth;

    private CubeMaterial[] facesMaterial = new CubeMaterial[6];

    public Cube(float width, float height, float depth) {
        this.height = height;
        this.width = width;
        this.depth = depth;

        for(int i = 0; i< facesMaterial.length ; i++) {
            facesMaterial[i] = new CubeMaterial(Color.SILVER, null, CubeFace.values()[i]);
        }

        TriangleMesh cubeMesh = new TriangleMesh();
        setTextureCoordinates(cubeMesh);
        definePoints(cubeMesh, height, width, depth);
        defineFaces(cubeMesh);
        defineNormals(cubeMesh);
        defineSmoothingGroups(cubeMesh);

        super.setMesh(cubeMesh);
        super.setDrawMode(DrawMode.FILL);
        super.setDepthTest(DepthTest.ENABLE);
    }

    public void setDefaultMaterial(Material value) {
        this.DEFAULT_MATERIAL = value;
    }

    public void highlightField(boolean on, Color color) {
        double power = on ? 0.9 : 1.0;
        Image texture = EffectUtils.createImage(Color.rgb(0,0,0, power), on ? color : Color.BLACK);
        PhongMaterial phongMaterial = (PhongMaterial) this.getMaterial();
        PhongMaterial shine = new PhongMaterial(phongMaterial.getDiffuseColor());
        shine.setSelfIlluminationMap(texture);
        this.setMaterial(shine);
    }

    public Cube setMaterial(CubeMaterial material) {
        for(int i = 0; i< 6; i++){
            if(material.getCubeFace() == facesMaterial[i].getCubeFace()) {
                facesMaterial[i] = material;
            }
        }
        return this;
    }

    public void setMaterialToDefault() {
        this.setMaterial(DEFAULT_MATERIAL);
    }

    public void initMaterial() {
        PhongMaterial cubeMaterial = new PhongMaterial();
        cubeMaterial.setDiffuseMap(createImage());
        cubeMaterial.setSpecularColor(Color.WHITE);
        cubeMaterial.setSpecularPower(32);
        super.setMaterial(cubeMaterial);
    }

    private void setTextureCoordinates(TriangleMesh cubeMesh) {

        float w = (2*depth) + (2*width);
        float h = (2*depth) + (2*height);

        cubeMesh.getTexCoords().addAll(
                (depth /w), 0,       //T0
                ((depth + width) /w), 0,        //T1
                0, (depth/h),       //T2
                (depth /w), (depth/h),   //T3
                ((depth + width) /w), (depth/h),    //T4
                ((2 * depth + width) /w), (depth/h),   //T5
                1, (depth/h),       //T6
                0, ((depth + height)/h),        //T7
                (depth /w), ((depth + height)/h),    //T8
                ((depth + width) /w), ((depth + height)/h),     //T9
                ((2 * depth + width) /w), ((depth + height)/h),    //T10
                1, ((depth + height)/h),        //T11
                (depth /w), ((2 * depth + height)/h),   //T12
                ((depth + width) /w), ((2 * depth + height)/h)    //T13
        );
    }

    private void definePoints(TriangleMesh cubeMesh, float height, float width, float tight) { // body
        cubeMesh.getPoints().addAll(
                0, 0, 0,                 //P0
                1 * width, 0, 0,                    //P1
                1 * width, 0, 1 * tight,            //P2
                0, 0, 1 * tight,                    //P3
                0, 1 * height, 0,                   //P4
                1 * width, 1 * height, 0,           //P5
                1 * width, 1 * height, 1 * tight,   //P6
                0, 1 * height, 1 * tight            //P7
        );
    }

    private void defineFaces(TriangleMesh cubeMesh) { // steny
        cubeMesh.getFaces().addAll(
                0, 3, 1, 4, 3, 0, //VRCH
                1, 4, 2, 1, 3, 0,

                4, 8, 0, 3, 3, 2, //LAVA
                4, 8, 3, 2, 7, 7,

                6, 10, 7, 11, 2, 5, //ZADNA
                7, 11, 3, 6, 2, 5,

                1, 4, 5, 9, 6, 10, //PRAVA
                1, 4, 6, 10, 2, 5,

                0, 3, 4, 8, 5, 9, //PREDOK
                1, 4, 0, 3, 5, 9,

                6, 13, 5, 9, 4, 8, //SPODOK
                6, 13, 4, 8, 7, 12
        );
    }

    private void defineNormals(TriangleMesh cubeMesh) {
        cubeMesh.getNormals().addAll(
                1f,  0f,  0f,
                -1f,  0f,  0f,
                0f,  1f,  0f,
                0f, -1f,  0f,
                0f,  0f,  1f,
                0f,  0f, -1f
        );
    }

    private void defineSmoothingGroups(TriangleMesh cubeMesh) {
        cubeMesh.getFaceSmoothingGroups().addAll(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    private WritableImage createImage() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        for(CubeMaterial face : facesMaterial){
            BorderPane backgroundPane = new BorderPane();
            if(face.getFaceColor() != null) {
                backgroundPane.setBackground(new Background(new BackgroundFill(face.getFaceColor(),
                        CornerRadii.EMPTY, Insets.EMPTY)));
            }
            if(face.getFaceText() != null){
                backgroundPane.setCenter(face.getFaceText());
            }
            GridPane.setHalignment(backgroundPane, HPos.CENTER);

            switch (face.getCubeFace()) {
                    case UP:
                        grid.add(backgroundPane,1,0);
                        break;
                    case BACK:
                        grid.add(backgroundPane,3,1);
                        break;
                    case DOWN:
                        grid.add(backgroundPane,1,2);
                        break;
                    case LEFT:
                        grid.add(backgroundPane,0,1);
                        break;
                    case FRONT:
                        grid.add(backgroundPane,1,1);
                        break;
                    case RIGHT:
                        grid.add(backgroundPane,2,1);
                        break;
                    default:
                        break;
                }
        }

        final double W = 2 * depth + 2 * width;
        final double H = 2 * depth + 2 * height;

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth((depth - 0.1) * 100 / W);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth((width + 0.2) * 100 / W);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(depth * 100 / W);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(width * 100 / W);

        grid.getColumnConstraints().addAll(col1, col2, col3, col4);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(depth * 100 / H);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(height * 100 / H);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(depth * 100 / H);
        RowConstraints row4 = new RowConstraints();
        row4.setPercentHeight(height * 100 / H);

        grid.getRowConstraints().addAll(row1, row2, row3, row4);

        grid.setPrefSize(W, H);
        new Scene(grid);
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.WHITE);
        sp.setDepthBuffer(true);
        return grid.snapshot(sp, null);
    }
}

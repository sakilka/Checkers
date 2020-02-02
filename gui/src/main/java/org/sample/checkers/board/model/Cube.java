package org.sample.checkers.board.model;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Random;

public class Cube extends MeshView {

    private static final float DEFAULT_HEIGHT = 150;
    private static final float DEFAULT_WIDTH = 300;
    private static final float DEFAULT_TIGHT = 250;

    public Cube() {
        this(DEFAULT_HEIGHT, DEFAULT_WIDTH, DEFAULT_TIGHT);
    }

    private float height;
    private float width;
    private float tight;

    public Cube(float height, float width, float tight) {
        this.height = height;
        this.width = width;
        this.tight = tight;

        TriangleMesh cubeMesh = new TriangleMesh();
        setTextureCoordinates(cubeMesh);
        definePoints(cubeMesh, height, width, tight);
        defineFaces(cubeMesh);

        super.setMesh(cubeMesh);
        super.setDrawMode(DrawMode.FILL);
    }

    public void setMaterial() {
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(createImage());
        super.setMaterial(material);
    }

    private void setTextureCoordinates(TriangleMesh cubeMesh) {

        float w = (2*tight) + (2*width);
        float h = (2*tight) + (2*height);

        cubeMesh.getTexCoords().addAll(
                (tight /w), 0,       //T0
                ((tight + width) /w), 0,        //T1
                0, (tight/h),       //T2
                (tight /w), (tight/h),   //T3
                ((tight + width) /w), (tight/h),    //T4
                ((2 * tight + width) /w), (tight/h),   //T5
                1, (tight/h),       //T6
                0, ((tight + height)/h),        //T7
                (tight /w), ((tight + height)/h),    //T8
                ((tight + width) /w), ((tight + height)/h),     //T9
                ((2 * tight + width) /w), ((tight + height)/h),    //T10
                1, ((tight + height)/h),        //T11
                (tight /w), ((2 * tight + height)/h),   //T12
                ((tight + width) /w), ((2 * tight + height)/h)    //T13
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

    private WritableImage createImage() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        Text front = new Text("Front");
        front.setFont(javafx.scene.text.Font
                .font(javafx.scene.text.Font.getFamilies().get(new Random().nextInt(Font.getFamilies().size())),
                        60));
        front.setStroke(Color.DARKGOLDENROD);
        front.setFill(Color.DARKGOLDENROD);

        GridPane.setHalignment(front, HPos.CENTER);

        grid.add(front, 1, 1);

        Text up = new Text("Up");
        up.setFont(javafx.scene.text.Font
                .font(javafx.scene.text.Font.getFamilies().get(new Random().nextInt(Font.getFamilies().size())),
                        60));
        up.setStroke(Color.DARKGOLDENROD);
        up.setFill(Color.DARKGOLDENROD);

        grid.add(up, 1, 0);

        GridPane.setHalignment(up, HPos.CENTER);

        final double W = 2 * tight + 2 * width;
        final double H = 2 * tight + 2 * height;

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(tight * 100 / W);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(width * 100 / W);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(tight * 100 / W);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(width * 100 / W);
        grid.getColumnConstraints().addAll(col1, col2, col3, col4);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(tight * 100 / H);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(height * 100 / H);
        RowConstraints row3 = new RowConstraints();
        row3.setPercentHeight(tight * 100 / H);
        RowConstraints row4 = new RowConstraints();
        row4.setPercentHeight(height * 100 / H);
        grid.getRowConstraints().addAll(row1, row2, row3, row4);
        grid.setPrefSize(W, H);
        grid.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        new Scene(grid);
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.GOLD);
        return grid.snapshot(sp, null);
    }
}

package org.sample.checkers.board.model;

import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

public class Figure extends MeshView {

    private static final int DEFAULT_SIZE = 150;
    private static final int DEFAULT_GRANULARITY = 10;
    private static final int ANGLE_RANGE = 360;
    private static final int END_TOP = 90;
    private static final double PRECISION = 1000000.0;

    public Figure() {
        this(DEFAULT_SIZE, DEFAULT_GRANULARITY);
    }

    public Figure(int size, int granularity) {
        TriangleMesh pyramidMesh = new TriangleMesh();

        setTextureCoordinates(pyramidMesh);
        definePoints(pyramidMesh, size, granularity);
        defineFaces(pyramidMesh, granularity);

        super.setMesh(pyramidMesh);
        super.setDrawMode(DrawMode.FILL);
    }

    private void setTextureCoordinates(TriangleMesh pyramidMesh) {
        pyramidMesh.getTexCoords().addAll(0, 0);
    }

    private void definePoints(TriangleMesh pyramidMesh, int size, int granularity) { // body
        pyramidMesh.getPoints().addAll(
                calculateTopPoints(size, granularity)
        );
    }

    private void defineFaces(TriangleMesh pyramidMesh, int granularity) { // steny

        List<Integer> faces = calculateTopFaces(granularity);

        pyramidMesh.getFaces().addAll(
                faces.stream().mapToInt(i -> i).toArray()
        );
    }

    private List<Integer> calculateTopFaces(int granularity) {
        List<Integer> faces = new ArrayList<>();

        for (int point = 1; point <= (ANGLE_RANGE / granularity); point++) {
            faces.add(0);
            faces.add(0);
            faces.add(point);
            faces.add(0);
            faces.add((point % (ANGLE_RANGE / granularity)) + 1);
            faces.add(0);
        }

        for (int level = 1; level < (((((ANGLE_RANGE / 4.0) + END_TOP) / granularity)) - 1); level++) {
            for (int point = 1; point <= (ANGLE_RANGE / granularity); point++) {
                faces.add(((level - 1) * (ANGLE_RANGE / granularity)) + point);
                faces.add(0);
                faces.add((level * (ANGLE_RANGE / granularity)) + point);
                faces.add(0);
                faces.add((level * (ANGLE_RANGE / granularity)) + (point % (ANGLE_RANGE / granularity)) + 1);
                faces.add(0);

                faces.add(((level - 1) * (ANGLE_RANGE / granularity)) + point);
                faces.add(0);
                faces.add((level * (ANGLE_RANGE / granularity)) + (point % (ANGLE_RANGE / granularity)) + 1);
                faces.add(0);
                faces.add(((level - 1) * (ANGLE_RANGE / granularity)) + (point % (ANGLE_RANGE / granularity)) + 1);
                faces.add(0);
            }
        }

//        for (int i = 0; i < faces.size(); i += 6) {
//            System.out.println(i / 6 + ". [" + faces.get(i) + "," + faces.get(i + 1) + "," + +faces.get(i + 2) + ","
//                    + faces.get(i + 3) + "," + faces.get(i + 4) + "," + faces.get(i + 5) + "]");
//        }
        return faces;
    }

    private float[] calculateTopPoints(int size, int granularity) {
        List<float[]> points = new ArrayList<>();

        for (double angle = (ANGLE_RANGE / 4.0); angle >= -END_TOP; angle -= granularity) {

            double y = (1 - sin(Math.toRadians(angle))) * size;
            double radius = cos(Math.toRadians(angle)) * size;

            for (int number = 0; number < ANGLE_RANGE; number += granularity) {
                double x = cos(Math.toRadians(number)) * radius;
                double z = sin(Math.toRadians(number)) * radius;
                points.add(new float[]{(float) (round(x * PRECISION) / PRECISION),
                        (float) (round(y * PRECISION) / PRECISION),
                        (float) (round(z * PRECISION) / PRECISION)
                });
            }
        }

        points = removeDuplicates(points);

//        int i = 0;
//        for (float[] point : points) {
//            System.out.println(i++ + ". [" + point[0] + "," + point[1] + "," + point[2] + "]");
//        }

        float[] result = new float[points.size() * 3];
        for (int p = 0; p < points.size(); p++) {
            result[p * 3] = points.get(p)[0];
            result[p * 3 + 1] = points.get(p)[1];
            result[p * 3 + 2] = points.get(p)[2];
        }

        return result;
    }

    private List<float[]> removeDuplicates(List<float[]> original) {
        List<float[]> filtered = new ArrayList<>();

        for (float[] point : original) {

            boolean duplicate = false;

            for (float[] compare : filtered) {
                if (compare[0] == point[0] && compare[1] == point[1] && compare[2] == point[2]) {
                    duplicate = true;
                    break;
                }
            }

            if (!duplicate) {
                filtered.add(point);
            }
        }

        return filtered;
    }
}

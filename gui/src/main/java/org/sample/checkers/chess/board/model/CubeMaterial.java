package org.sample.checkers.chess.board.model;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class CubeMaterial {

    private Color faceColor;
    private Text faceText;
    private CubeFace cubeFace;

    public CubeMaterial(Color faceColor, Text faceText, CubeFace cubeFace) {
        this.faceColor = faceColor;
        this.faceText = faceText;
        this.cubeFace = cubeFace;
    }

    public Color getFaceColor() {
        return faceColor;
    }

    public void setFaceColor(Color faceColor) {
        this.faceColor = faceColor;
    }

    public Text getFaceText() {
        return faceText;
    }

    public void setFaceText(Text faceText) {
        this.faceText = faceText;
    }

    public CubeFace getCubeFace() {
        return cubeFace;
    }

    public void setCubeFace(CubeFace cubeFace) {
        this.cubeFace = cubeFace;
    }
}

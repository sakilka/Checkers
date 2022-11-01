package org.sample.checkers.tictactoe;

import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.sample.checkers.board.model.GameBoard;
import org.sample.checkers.chess.BoardPosition;

public class TickTackToeScene extends SubScene implements GameBoard {

    private final Stage mainStage;
    private final GridPane boardSceneGroup;

    public TickTackToeScene(Stage stage, GridPane root, double width, double height, boolean depthBuffer,
                            SceneAntialiasing antiAliasing, BoardPosition boardPosition) {
        super(root, width, height, depthBuffer, antiAliasing);

//        this.angleX = new SimpleDoubleProperty(getBoardConfig().getAngleX());
//        this.angleY = new SimpleDoubleProperty(getBoardConfig().getAngleY());
//        this.anchorAngleX = 0;
//        this.anchorAngleY = 0;
//        this.anchorDistX = 0;
//        this.anchorDistZ = 0;
//        this.marked = null;
//        this.deltaX = new SimpleDoubleProperty(getBoardConfig().getDeltaX());
//        this.deltaZ = new SimpleDoubleProperty(getBoardConfig().getDeltaZ());
//        this.translateZ = new SimpleDoubleProperty(getBoardConfig().getTranslateZ());
//        this.fieldWidth = getBoardConfig().getFieldWidth();
//        this.fieldHeight = getBoardConfig().getFieldHeight();
//        this.fieldDepth = getBoardConfig().getFieldDepth();
//        this.fieldGap = getBoardConfig().getFieldGap();
//        this.fieldGapShift = getBoardConfig().getFieldGapShift();
//        this.widthShift =  4 * fieldWidth;
//        this.depthShift = 4 * fieldWidth;

//        this.board = new Cube[8][8];
//        this.chessFigureModels = new ArrayList<>();
//        this.chessMoveHistory = new ChessMoveHistory(new ArrayList<>());

        mainStage = stage;
        boardSceneGroup = root;

        initBoard(boardPosition);
        initMouseControl(this);
    }

    @Override
    public void initBoard(BoardPosition boardPosition) {
        initializeBoard();

//        boardPosition.deltaXProperty().bindBidirectional(deltaX);
//        boardPosition.deltaZProperty().bindBidirectional(deltaZ);
//        boardPosition.angleXProperty().bindBidirectional(angleX);
//        boardPosition.angleYProperty().bindBidirectional(angleY);
//        boardPosition.translateZProperty().bindBidirectional(boardSceneGroup.translateZProperty());
//        boardPosition.translateZProperty().bindBidirectional(translateZ);

        //this.setCamera(initializeCamera());
        this.setFill(Color.SILVER);
    }

    public Camera initializeCamera() {
        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(10000);
        camera.translateZProperty().set(-150);

        return camera;
    }

    private void initializeBoard() {
        int width = 15;
        int height = 10;

        GridPane pane = new GridPane();
        Cell[][] cell =  new Cell[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pane.add(cell[i][j] = new Cell(), j, i);
            }
        }

        boardSceneGroup.getChildren().add(pane);
    }

    @Override
    public void initMouseControl(SubScene boardScene) {

    }
}

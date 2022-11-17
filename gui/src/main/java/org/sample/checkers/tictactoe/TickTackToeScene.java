package org.sample.checkers.tictactoe;

import com.sun.javafx.geom.Dimension2D;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.sample.checkers.board.model.GameBoard;
import org.sample.checkers.checkers.CheckersFigureModel;
import org.sample.checkers.chess.BoardPosition;
import org.sample.checkers.config.checkers.CheckersMovePosition;
import org.sample.checkers.config.game.GameSetup;
import org.sample.checkers.config.ticktacktoe.TickTackToeConfiguration;
import org.sample.checkers.config.ticktacktoe.TickTackToeMove;
import org.sample.checkers.config.ticktacktoe.TickTackToeMoveHistory;
import org.sample.checkers.config.ticktacktoe.ToeSide;
import org.sample.checkers.ticktacktoe.ui.TickTackToeUi;

import java.util.ArrayList;

import static org.sample.checkers.checkers.CheckersMoveUtil.getFigureForCheckersMove;
import static org.sample.checkers.checkers.CheckersMoveUtil.handlePrimaryClick;
import static org.sample.checkers.config.game.GamePropertyUtil.getBoardConfig;
import static org.sample.checkers.config.game.GamePropertyUtil.getGameSetup;
import static org.sample.checkers.config.game.Player.MULTI_PLAYER;
import static org.sample.checkers.config.game.Player.SINGLE_PLAYER;
import static org.sample.checkers.config.ticktacktoe.TickTackToePropertyUtil.getConfig;
import static org.sample.checkers.config.ticktacktoe.ToeSide.CIRCLE;
import static org.sample.checkers.ticktacktoe.ui.TickTackToeContext.getUi;

public class TickTackToeScene extends SubScene implements GameBoard {

    private final Stage mainStage;
    private final StackPane boardSceneGroup;

    private static TickTackToeConfiguration tickTackToeConfiguration;

    private TickTackToeUi ui = getUi();
    private GameSetup gameSetup = getGameSetup();

    private final Cell[][] cells;
    private ToeSide onTurn;
    private ToeSide uiTurn;
    private TickTackToeMoveHistory tickTackToeMoveHistory;

    public TickTackToeScene(Stage stage, StackPane root, double width, double height, boolean depthBuffer,
                            SceneAntialiasing antiAliasing, BoardPosition boardPosition) {
        super(root, width, height, depthBuffer, antiAliasing);

        mainStage = stage;
        boardSceneGroup = root;
        boardSceneGroup.setBackground(new Background(new BackgroundFill(Color.rgb(0,100,0, 1), null, null)));

        tickTackToeConfiguration = getConfig();
        cells = new Cell[tickTackToeConfiguration.getTickTackToeHeight()][tickTackToeConfiguration.getTickTackToeWidth()];
        onTurn = CIRCLE;
        uiTurn = gameSetup.getPlayer() == MULTI_PLAYER ? null : onTurn.oposite();
        this.tickTackToeMoveHistory = new TickTackToeMoveHistory(new ArrayList<>(),
                tickTackToeConfiguration.getTickTackToeWidth(),tickTackToeConfiguration.getTickTackToeHeight());

        initBoard(boardPosition);
    }

    @Override
    public void initBoard(BoardPosition boardPosition) {
        initializeBoard();
    }

    private void initializeBoard() {
        int width = tickTackToeConfiguration.getTickTackToeWidth();
        int height = tickTackToeConfiguration.getTickTackToeHeight();

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(0, 0, getBoardConfig().getMenuHeight(), 0));

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                gridPane.add(cells[i][j] = new Cell(this, j, i), j, i);
                cells[i][j].prefWidthProperty().bind(Bindings.min(boardSceneGroup.widthProperty().divide(width),
                        boardSceneGroup.heightProperty().divide(height)));
                cells[i][j].prefHeightProperty().bind(Bindings.min(boardSceneGroup.widthProperty().divide(width),
                        boardSceneGroup.heightProperty().divide(height)));
            }
        }

        boardSceneGroup.setAlignment(Pos.CENTER);
        boardSceneGroup.getChildren().add(gridPane);
    }

    @Override
    public void initMouseControl(SubScene boardScene) {
    }

    public ToeSide getOnTurn() {
        return this.onTurn;
    }

    public void setOnTurn(ToeSide turn) {
        this.onTurn = turn;
    }

    public void handleMouseClick(Cell cell) {
        if(cell.getSide() != null) {
            return;
        }

        if(onTurn == CIRCLE) {
            cell.setCircle();
        } else {
            cell.setCross();
        }

        onTurn = onTurn.oposite();
        tickTackToeMoveHistory.addMove(new TickTackToeMove(new Dimension2D(cell.getWidthPosition(),
                cell.getHeightPosition()), cell.getSide()));

        if(gameSetup.getPlayer() == SINGLE_PLAYER && onTurn == uiTurn) {
            executeUIMovement();
        }
    }

    public void executeUIMovement() {
        TickTackToeMove nextMove = ui.computeNextMove(tickTackToeMoveHistory);
        if(uiTurn == CIRCLE) {
            cells[(int) nextMove.getPosition().height][(int) nextMove.getPosition().width].setCircle();
        } else {
            cells[(int) nextMove.getPosition().height][(int) nextMove.getPosition().width].setCross();
        }

        onTurn = onTurn.oposite();
        tickTackToeMoveHistory.addMove(new TickTackToeMove(nextMove.getPosition(), nextMove.getSide()));
    }
}

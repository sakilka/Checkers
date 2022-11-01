package org.sample.checkers;

import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.sample.checkers.checkers.CheckersBoardScene;
import org.sample.checkers.chess.BoardPosition;
import org.sample.checkers.chess.ChessBoardScene;
import org.sample.checkers.board.components.SmartGroup;
import org.sample.checkers.config.game.Game;
import org.sample.checkers.tictactoe.TickTackToeScene;

public class BoardFactory {

    public static SubScene getBoardScene(Game game, Stage stage, double width, double height,
                                         boolean depthBuffer, SceneAntialiasing antiAliasing, BoardPosition boardPosition){

        switch (game){
            case TICKTACKTOE:
                return new TickTackToeScene(stage,new GridPane(), width, height, depthBuffer, antiAliasing, boardPosition);
            case CHESS:
                return new ChessBoardScene(stage, new SmartGroup(), width, height, depthBuffer, antiAliasing, boardPosition);
            case CHECKERS:
            default:
                return new CheckersBoardScene(stage, new SmartGroup(), width, height, depthBuffer, antiAliasing, boardPosition);
        }
    }
}

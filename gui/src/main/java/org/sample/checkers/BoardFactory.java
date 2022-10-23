package org.sample.checkers;

import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.stage.Stage;
import org.sample.checkers.checkers.CheckersBoardScene;
import org.sample.checkers.chess.BoardPosition;
import org.sample.checkers.chess.ChessBoardScene;
import org.sample.checkers.chess.components.SmartGroup;
import org.sample.checkers.config.Game;

public class BoardFactory {

    public static SubScene getBoardScene(Game game, Stage stage, SmartGroup root, double width, double height,
                                         boolean depthBuffer, SceneAntialiasing antiAliasing, BoardPosition boardPosition){

        switch (game){
            case CHESS:
                return new ChessBoardScene(stage, root, width, height, depthBuffer, antiAliasing, boardPosition);
            case CHECKERS:
            default:
                return new CheckersBoardScene(stage, root, width, height, depthBuffer, antiAliasing, boardPosition);
        }
    }
}

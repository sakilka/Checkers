package org.sample.checkers.board.model;

import javafx.scene.SubScene;
import org.sample.checkers.chess.BoardPosition;

public interface GameBoard {

    void initBoard(BoardPosition boardPosition);

    void initMouseControl(SubScene boardScene);
}

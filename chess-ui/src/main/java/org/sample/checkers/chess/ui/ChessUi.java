package org.sample.checkers.chess.ui;

import org.sample.checkers.config.chess.ChessMoveHistory;
import org.sample.checkers.config.chess.ChessMovePosition;

public interface ChessUi {

    ChessMovePosition computeNextMove(ChessMoveHistory history);
}

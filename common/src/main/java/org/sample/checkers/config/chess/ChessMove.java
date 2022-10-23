package org.sample.checkers.config.chess;

import com.sun.javafx.geom.Dimension2D;
import org.sample.checkers.config.checkers.CheckersBoardPositions;
import org.sample.checkers.config.checkers.CheckersMoveHistory;
import org.sample.checkers.config.checkers.CheckersSide;

import java.util.List;

public interface ChessMove {

    List<Dimension2D> potentialMoves(ChessSide side, ChessMoveHistory chessMoveHistory, Dimension2D currentPosition,
                                     ChessBoardPositions currentBoard);
}

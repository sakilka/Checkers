package org.sample.checkers.config;

import com.sun.javafx.geom.Dimension2D;

import java.util.List;

public interface CheckersMove {

    List<Dimension2D> potentialMoves( ChessSide side, MoveHistory moveHistory, Dimension2D currentPosition,
                                      ChessBoardPositions currentBoard);
}

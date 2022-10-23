package org.sample.checkers.config.checkers;

import com.sun.javafx.geom.Dimension2D;

import java.util.List;

public interface CheckersMove {

    List<Dimension2D> potentialMoves(CheckersSide side, CheckersMoveHistory checkersMoveHistory, Dimension2D currentPosition,
                                     CheckersBoardPositions currentBoard);
}

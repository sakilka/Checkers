package org.sample.checkers.chess.ui.impl;

import com.sun.javafx.geom.Dimension2D;
import org.sample.checkers.chess.ui.ChessUi;
import org.sample.checkers.config.checkers.CheckersMovePosition;
import org.sample.checkers.config.chess.ChessMoveHistory;
import org.sample.checkers.config.chess.ChessMovePosition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ChessUiImpl implements ChessUi {

    private static List<ChessMovePosition> testPositions = Stream.of(
                    new ChessMovePosition(new Dimension2D(8,6), new Dimension2D(8,7)),
                    new ChessMovePosition(new Dimension2D(7,6), new Dimension2D(7,7)),
                    new ChessMovePosition(new Dimension2D(6,6), new Dimension2D(6,7)),
                    new ChessMovePosition(new Dimension2D(5,6), new Dimension2D(5,7)),
                    new ChessMovePosition(new Dimension2D(4,6), new Dimension2D(4,7)),
                    new ChessMovePosition(new Dimension2D(3,6), new Dimension2D(3,7)),
                    new ChessMovePosition(new Dimension2D(2,6), new Dimension2D(2,7)),
                    new ChessMovePosition(new Dimension2D(1,6), new Dimension2D(1,7))
            ).collect(Collectors.toList());

    private int move = 0;

    @Override
    public ChessMovePosition computeNextMove(ChessMoveHistory history) {
        return testPositions.get(move++);
    }
}

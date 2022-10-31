package org.sample.checkers.checkers.ui.impl;

import com.sun.javafx.geom.Dimension2D;
import org.sample.checkers.config.checkers.CheckersUi;
import org.sample.checkers.config.checkers.CheckersMoveHistory;
import org.sample.checkers.config.checkers.CheckersMovePosition;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CheckersUiImpl implements CheckersUi {

    private static List<CheckersMovePosition> testPositions = Stream.of(
                    new CheckersMovePosition(new Dimension2D(8,6), new Dimension2D(7,7)),
                    new CheckersMovePosition(new Dimension2D(6,6), new Dimension2D(5,7)),
                    new CheckersMovePosition(new Dimension2D(4,6), new Dimension2D(3,7)),
                    new CheckersMovePosition(new Dimension2D(2,6), new Dimension2D(1,7)),
                    new CheckersMovePosition(new Dimension2D(7,7), new Dimension2D(8,8)),
                    new CheckersMovePosition(new Dimension2D(5,7), new Dimension2D(6,8)),
                    new CheckersMovePosition(new Dimension2D(3,7), new Dimension2D(4,8)),
                    new CheckersMovePosition(new Dimension2D(1,7), new Dimension2D(2,8))
            )
            .collect(Collectors.toList());

    private int move = 0;

    @Override
    public CheckersMovePosition computeNextMove(CheckersMoveHistory history) {
        return testPositions.get(move++);
    }
}

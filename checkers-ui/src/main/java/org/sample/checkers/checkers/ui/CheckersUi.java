package org.sample.checkers.checkers.ui;

import org.sample.checkers.config.checkers.CheckersMoveHistory;
import org.sample.checkers.config.checkers.CheckersMovePosition;

public interface CheckersUi {

    CheckersMovePosition computeNextMove(CheckersMoveHistory history);
}

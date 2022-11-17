package org.sample.checkers.ticktacktoe.ui;

import org.sample.checkers.config.ticktacktoe.TickTackToeMove;
import org.sample.checkers.config.ticktacktoe.TickTackToeMoveHistory;

public interface TickTackToeUi {

    TickTackToeMove computeNextMove(TickTackToeMoveHistory history);
}

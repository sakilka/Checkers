package org.sample.checkers.config.checkers;

import org.sample.checkers.config.chess.ChessSide;

public enum CheckersSide {
    BLACK, WHITE;

    public CheckersSide oposite() {
        return this == BLACK ? WHITE : BLACK;
    }
}

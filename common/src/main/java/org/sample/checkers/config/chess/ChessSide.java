package org.sample.checkers.config.chess;

public enum ChessSide {
    BLACK, WHITE;

    public ChessSide oposite() {
        return this == BLACK ? WHITE : BLACK;
    }
}

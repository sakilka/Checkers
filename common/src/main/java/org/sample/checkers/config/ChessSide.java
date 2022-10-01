package org.sample.checkers.config;

public enum ChessSide {
    BLACK, WHITE;

    public ChessSide oposite() {
        return this == BLACK ? WHITE : BLACK;
    }
}

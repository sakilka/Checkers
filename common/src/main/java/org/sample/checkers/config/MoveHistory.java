package org.sample.checkers.config;

import java.util.List;

public class MoveHistory {

    private List<ChessMove> moves;

    private boolean castlingDone;

    public MoveHistory(List<ChessMove> moves) {
        this.moves = moves;
        this.castlingDone = false;
    }

    public List<ChessMove> getMoves() {
        return moves;
    }

    public void setMoves(List<ChessMove> moves) {
        this.moves = moves;
    }

    public void addMove(ChessMove move) {
        moves.add(move);
    }

    public boolean isCastlingDone() {
        return castlingDone;
    }

    public void setCastlingDone(boolean castlingDone) {
        this.castlingDone = castlingDone;
    }
}

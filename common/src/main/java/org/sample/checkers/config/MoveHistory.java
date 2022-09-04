package org.sample.checkers.config;

import java.util.List;

public class MoveHistory {

    private List<ChessMove> moves;

    private boolean castlingDone;
    private ChessSide onMove;

    public MoveHistory(List<ChessMove> moves) {
        this.moves = moves;
        this.castlingDone = false;
        this.onMove = ChessSide.WHITE;
    }

    public List<ChessMove> getMoves() {
        return moves;
    }

    public void setMoves(List<ChessMove> moves) {
        this.moves = moves;
    }

    public void addMove(ChessMove move) {
        move.setFirstMove(isFirstMove(move));
        moves.add(move);
        onMove = onMove == ChessSide.WHITE ? ChessSide.BLACK : ChessSide.WHITE;
    }

    public boolean isCastlingDone() {
        return castlingDone;
    }

    public void setCastlingDone(boolean castlingDone) {
        this.castlingDone = castlingDone;
    }

    public ChessSide getOnMove() {
        return onMove;
    }

    public void setOnMove(ChessSide whiteMove) {
        this.onMove = whiteMove;
    }

    private boolean isFirstMove(ChessMove move) {
        return moves.stream().anyMatch(previousMove ->
                move.getPreviousPosition().width == previousMove.getPosition().width &&
                        move.getPreviousPosition().height == previousMove.getPosition().height);
    }
}

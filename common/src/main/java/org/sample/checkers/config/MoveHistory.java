package org.sample.checkers.config;

import java.util.List;

public class MoveHistory {

    private List<ChessMove> moves;

    private boolean castlingWhiteDone;
    private boolean castlingBlackDone;
    private ChessSide onMove;

    public MoveHistory(List<ChessMove> moves) {
        this.moves = moves;
        this.castlingWhiteDone = false;
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

    public boolean isCastlingWhiteDone() {
        return castlingWhiteDone;
    }

    public void setCastlingWhiteDone(boolean castlingWhiteDone) {
        this.castlingWhiteDone = castlingWhiteDone;
    }

    public boolean isCastlingBlackDone() {
        return castlingBlackDone;
    }

    public void setCastlingBlackDone(boolean castlingBlackDone) {
        this.castlingBlackDone = castlingBlackDone;
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

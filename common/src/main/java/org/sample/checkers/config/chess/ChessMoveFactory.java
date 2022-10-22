package org.sample.checkers.config.chess;

public class ChessMoveFactory {

    public static ChessMove getMove(ChessFigure figure){

        switch (figure){
            case KING:
                return new KingMove();
            case BISHOP:
                return new BishopMove();
            case KNIGHT:
                return new KnightMove();
            case PAWN:
                return new PawnMove();
            case QUEEN:
                return new QueenMove();
            case ROOK:
            default:
                return new RookMove();
        }
    }
}

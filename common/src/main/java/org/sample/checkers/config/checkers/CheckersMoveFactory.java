package org.sample.checkers.config.checkers;

import org.sample.checkers.config.chess.*;

public class CheckersMoveFactory {

    public static CheckersMove getMove(CheckersFigure figure){

        switch (figure){
            case QUEEN:
                return new QueenMove();
            case PAWN:
            default:
                return new PawnMove();
        }
    }
}

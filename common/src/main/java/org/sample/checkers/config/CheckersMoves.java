package org.sample.checkers.config;

import com.sun.javafx.geom.Dimension2D;

import java.util.ArrayList;
import java.util.List;

import static org.sample.checkers.config.ChessMove.FIRST_MOVE;

public class CheckersMoves {

    public static List<Dimension2D> potentialMoves(ChessFigure figure, ChessSide side, MoveHistory moveHistory,
                                                   Dimension2D currentPosition, ChessBoardPositions currentBoard){
        switch (figure){
            case KING:
                return potentialKingPosition(moveHistory, currentPosition, currentBoard);
            case QUEEN:
                return potentialQueenPosition(moveHistory, currentPosition, currentBoard);
            case ROOK:
                return potentialRookPosition(moveHistory, currentPosition, currentBoard);
            case BISHOP:
                return potentialBishopPosition(moveHistory, currentPosition, currentBoard);
            case KNIGHT:
                return potentialKnightPosition(moveHistory, currentPosition, currentBoard);
            case PAWN:
                return potentialPawnPosition(moveHistory, currentPosition, currentBoard, side);
            default:
                return new ArrayList<>();
        }
    }

    //Pešiaci majú najzložitejšie pravidlá pohybu:
    //Pešiak sa pohne o jedno pole priamo vpred, ak je toto pole prázdne.
    // Ak sa pešiak ešte nepohol, má tiež možnosť pohnúť sa o dve polia priamo vpred, ak sú obe polia prázdne.
    // Pešiaci sa nemôžu pohybovať dozadu.

    //Pešiak, na rozdiel od iných figúrok, vyhadzuje inak, ako sa pohybuje.
    // Pešiak môže zajať nepriateľskú figúrku na ktoromkoľvek z dvoch polí diagonálne pred pešiakom.
    // Nemôže sa presunúť na tieto polia, keď sú prázdne, okrem prípadov, keď je zajatý en passant.

    //Pešiak je tiež zapojený do dvoch špeciálnych ťahov en passant a povýšenia
    private static List<Dimension2D> potentialPawnPosition(MoveHistory moveHistory, Dimension2D currentPosition,
                                                           ChessBoardPositions currentBoard, ChessSide side) {
        List<Dimension2D> potentialMoves = new ArrayList<>();

        if(side == ChessSide.WHITE) {

            if (isFirstMove(moveHistory, currentPosition)) {
                if ((((int) currentPosition.height + 1) < 8) &&
                        (currentBoard.getPositions()[(int) currentPosition.width][(int) currentPosition.height + 1] == null)) {
                    potentialMoves.add(new Dimension2D(currentPosition.width, currentPosition.height + 1));

                    if ((((int) currentPosition.height + 2) < 8) &&
                            (currentBoard.getPositions()[(int) currentPosition.width][(int) currentPosition.height + 2] == null)) {
                        potentialMoves.add(new Dimension2D(currentPosition.width, currentPosition.height + 2));
                    }
                }
            } else {
                if ((((int) currentPosition.height + 1) < 8) &&
                        (currentBoard.getPositions()[(int) currentPosition.width][(int) currentPosition.height + 1] == null)) {
                    potentialMoves.add(new Dimension2D(currentPosition.width, currentPosition.height + 1));
                }
            }
        } else {
            if (isFirstMove(moveHistory, currentPosition)) {
                if (((currentPosition.height - 1) >= 0) &&
                        (currentBoard.getPositions()[(int) currentPosition.width][(int) currentPosition.height - 1] == null)) {
                    potentialMoves.add(new Dimension2D(currentPosition.width, currentPosition.height - 1));

                    if (((currentPosition.height - 2) >= 0) &&
                            (currentBoard.getPositions()[(int) currentPosition.width][(int) currentPosition.height - 2] == null)) {
                        potentialMoves.add(new Dimension2D(currentPosition.width, currentPosition.height - 2));
                    }
                }
            } else {
                if (((currentPosition.height - 1) >= 0) &&
                        (currentBoard.getPositions()[(int) currentPosition.width][(int) currentPosition.height - 1] == null)) {
                    potentialMoves.add(new Dimension2D(currentPosition.width, currentPosition.height - 1));
                }
            }
        }

        return potentialMoves;
    }

    //Kráľ sa pohne presne o jedno políčko horizontálne, vertikálne alebo diagonálne.
    // Špeciálny ťah s kráľom známy ako rošáda je povolený iba raz na hráča, na hru (pozri nižšie).
    private static List<Dimension2D> potentialKingPosition(MoveHistory moveHistory, Dimension2D currentPosition,
                                                           ChessBoardPositions currentBoard) {
        List<Dimension2D> potentialMoves = new ArrayList<>();

        if(!moveHistory.isCastlingDone()) {
            //TODO
        }

        if(((currentPosition.height-1) >= 0) &&
                (currentBoard.getPositions()[(int) currentPosition.width][(int) currentPosition.height-1] == null)){
            potentialMoves.add(new Dimension2D(currentPosition.width, currentPosition.height-1));
        }
        if(((currentPosition.height+1) < 8) &&
                (currentBoard.getPositions()[(int) currentPosition.width][(int) currentPosition.height+1] == null)){
            potentialMoves.add(new Dimension2D(currentPosition.width, currentPosition.height+1));
        }
        if(((currentPosition.width-1) >= 0) &&
                (currentBoard.getPositions()[(int) currentPosition.width-1][(int) currentPosition.height] == null)){
            potentialMoves.add(new Dimension2D(currentPosition.width-1, currentPosition.height));
        }
        if(((currentPosition.width+1) <8) &&
                (currentBoard.getPositions()[(int) currentPosition.width+1][(int) currentPosition.height] == null)){
            potentialMoves.add(new Dimension2D(currentPosition.width+1, currentPosition.height));
        }
        if(((currentPosition.width-1) >= 0) && ((currentPosition.height+1) < 8) &&
                (currentBoard.getPositions()[(int) currentPosition.width-1][(int) currentPosition.height+1] == null)){
            potentialMoves.add(new Dimension2D(currentPosition.width-1, currentPosition.height+1));
        }
        if(((currentPosition.width-1) >= 0) && ((currentPosition.height-1) >= 0) &&
                currentBoard.getPositions()[(int) currentPosition.width-1][(int) currentPosition.height-1] == null){
            potentialMoves.add(new Dimension2D(currentPosition.width-1, currentPosition.height-1));
        }
        if(((currentPosition.width+1) < 8) && ((currentPosition.height+1) < 8) &&
                currentBoard.getPositions()[(int) currentPosition.width+1][(int) currentPosition.height+1] == null){
            potentialMoves.add(new Dimension2D(currentPosition.width+1, currentPosition.height+1));
        }
        if(((currentPosition.width+1) < 8) && ((currentPosition.height-1) >= 0) &&
                currentBoard.getPositions()[(int) currentPosition.width+1][(int) currentPosition.height-1] == null){
            potentialMoves.add(new Dimension2D(currentPosition.width+1, currentPosition.height-1));
        }

        return potentialMoves;
    }

    //Dáma sa pohybuje o ľubovoľný počet voľných polí vodorovne, zvisle alebo diagonálne.
    private static List<Dimension2D> potentialQueenPosition(MoveHistory moveHistory, Dimension2D currentPosition,
                                                            ChessBoardPositions currentBoard) {
        List<Dimension2D> potentialMoves = new ArrayList<>();

        for(int shift = 1; (currentPosition.width + shift) < 8; shift ++) {

            if(currentBoard.getPositions()[(int) currentPosition.width+shift][(int) currentPosition.height] != null) {
                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width+shift, currentPosition.height));
        }

        for(int shift = 1; (currentPosition.width - shift) >= 0; shift ++) {

            if(currentBoard.getPositions()[(int) currentPosition.width-shift][(int) currentPosition.height] != null) {
                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width-shift, currentPosition.height));
        }

        for(int shift = 1; (currentPosition.height + shift) < 8; shift ++) {

            if(currentBoard.getPositions()[(int) currentPosition.width][(int) currentPosition.height+shift] != null) {
                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width, currentPosition.height+shift));
        }

        for(int shift = 1; (currentPosition.height - shift) >= 0; shift ++) {

            if(currentBoard.getPositions()[(int) currentPosition.width][(int) currentPosition.height- shift] != null) {
                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width, currentPosition.height - shift));
        }

        for(int shift = 1; (((currentPosition.width + shift) < 8) && ((currentPosition.height + shift) < 8)); shift ++) {
            if (currentBoard.getPositions()[(int) currentPosition.width + shift]
                    [(int) currentPosition.height + shift] != null) {
                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width + shift, currentPosition.height + shift));
        }

        for(int shift = 1; (((currentPosition.width - shift) >= 0) && ((currentPosition.height - shift) >= 0)); shift ++) {
            if (currentBoard.getPositions()[(int) currentPosition.width - shift]
                    [(int) currentPosition.height - shift] != null) {
                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width - shift, currentPosition.height - shift));
        }

        for(int shift = 1; (((currentPosition.width + shift) < 8) && ((currentPosition.height - shift) >= 0)); shift ++) {
            if (currentBoard.getPositions()[(int) currentPosition.width + shift]
                    [(int) currentPosition.height - shift] != null) {
                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width + shift, currentPosition.height - shift));
        }

        for(int shift = 1; (((currentPosition.width - shift) >= 0) && ((currentPosition.height + shift) < 8)); shift ++) {
            if (currentBoard.getPositions()[(int) currentPosition.width - shift]
                    [(int) currentPosition.height + shift] != null) {
                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width - shift, currentPosition.height + shift));
        }

        return potentialMoves;
    }

    //Veža sa pohybuje o ľubovoľný počet voľných polí vodorovne alebo zvisle. Pohybuje sa aj pri rošádovaní.
    private static List<Dimension2D> potentialRookPosition(MoveHistory moveHistory, Dimension2D currentPosition,
                                                           ChessBoardPositions currentBoard) {

        List<Dimension2D> potentialMoves = new ArrayList<>();

        for(int shift = 1; (currentPosition.width + shift) < 8; shift ++) {

            if(currentBoard.getPositions()[(int) currentPosition.width+shift][(int) currentPosition.height] != null) {
                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width+shift, currentPosition.height));
        }

        for(int shift = 1; (currentPosition.width - shift) > 0; shift ++) {

            if(currentBoard.getPositions()[(int) currentPosition.width-shift][(int) currentPosition.height] != null) {
                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width-shift, currentPosition.height));
        }

        for(int shift = 1; (currentPosition.height + shift) < 8; shift ++) {

            if(currentBoard.getPositions()[(int) currentPosition.width][(int) currentPosition.height+shift] != null) {
                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width, currentPosition.height+shift));
        }

        for(int shift = 1; (currentPosition.height - shift) > 0; shift ++) {

            if(currentBoard.getPositions()[(int) currentPosition.width][(int) currentPosition.height- shift] != null) {
                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width, currentPosition.height- shift));
        }

        return potentialMoves;
    }

    //Strelec posúva ľubovoľný počet voľných polí diagonálne
    private static List<Dimension2D> potentialBishopPosition(MoveHistory moveHistory, Dimension2D currentPosition,
                                                             ChessBoardPositions currentBoard) {
        List<Dimension2D> potentialMoves = new ArrayList<>();

        for(int shift = 1; (((currentPosition.width + shift) < 8) && ((currentPosition.height + shift) < 8)); shift ++) {
            if (currentBoard.getPositions()[(int) currentPosition.width + shift]
                    [(int) currentPosition.height + shift] != null) {
                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width + shift, currentPosition.height + shift));
        }

        for(int shift = 1; (((currentPosition.width - shift) >= 0) && ((currentPosition.height - shift) >= 0)); shift ++) {
            if (currentBoard.getPositions()[(int) currentPosition.width - shift]
                    [(int) currentPosition.height - shift] != null) {
                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width - shift, currentPosition.height - shift));
        }

        for(int shift = 1; (((currentPosition.width + shift) < 8) && ((currentPosition.height - shift) >= 0)); shift ++) {
            if (currentBoard.getPositions()[(int) currentPosition.width + shift]
                    [(int) currentPosition.height - shift] != null) {
                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width + shift, currentPosition.height - shift));
        }

        for(int shift = 1; (((currentPosition.width - shift) >= 0) && ((currentPosition.height + shift) < 8)); shift ++) {
            if (currentBoard.getPositions()[(int) currentPosition.width - shift]
                    [(int) currentPosition.height + shift] != null) {
                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width - shift, currentPosition.height + shift));
        }

        return potentialMoves;
    }

    //Rytier sa presunie na jedno z najbližších polí, ktoré nemá rovnakú hodnosť, súbor alebo uhlopriečku.
    // (To si možno predstaviť ako pohyb o dve políčka horizontálne, potom jedno políčko zvislo,
    // alebo pohyb o jedno políčko vodorovne a potom dve políčka zvislo – t. j. vo vzore „L“.)
    // Jazdec nie je blokovaný inými figúrkami; preskočí na nové miesto.
    private static List<Dimension2D> potentialKnightPosition(MoveHistory moveHistory, Dimension2D currentPosition,
                                                             ChessBoardPositions currentBoard) {
        List<Dimension2D> potentialMoves = new ArrayList<>();

        if((currentPosition.width - 2 >= 0) && ((currentPosition.height + 1) < 8) &&
                currentBoard.getPositions()[(int) (currentPosition.width - 2)][(int) (currentPosition.height + 1)] == null) {
            potentialMoves.add(new Dimension2D(currentPosition.width - 2, currentPosition.height + 1));
        }

        if((currentPosition.width - 1 >= 0) && ((currentPosition.height + 2) < 8) &&
                currentBoard.getPositions()[(int) (currentPosition.width - 1)][(int) (currentPosition.height + 2)] == null) {
            potentialMoves.add(new Dimension2D(currentPosition.width - 1, currentPosition.height + 2));
        }

        if((currentPosition.width + 1 < 8) && ((currentPosition.height + 2) < 8) &&
                currentBoard.getPositions()[(int) (currentPosition.width + 1)][(int) (currentPosition.height + 2)] == null) {
            potentialMoves.add(new Dimension2D(currentPosition.width + 1, currentPosition.height + 2));
        }

        if((currentPosition.width + 2 < 8) && ((currentPosition.height + 1) < 8) &&
                currentBoard.getPositions()[(int) (currentPosition.width + 2)][(int) (currentPosition.height + 1)] == null) {
            potentialMoves.add(new Dimension2D(currentPosition.width + 2, currentPosition.height + 1));
        }

        if((currentPosition.width + 2 < 8) && ((currentPosition.height - 1) >= 0) &&
                currentBoard.getPositions()[(int) (currentPosition.width + 2)][(int) (currentPosition.height - 1)] == null) {
            potentialMoves.add(new Dimension2D(currentPosition.width + 2, currentPosition.height - 1));
        }

        if((currentPosition.width + 1 < 8) && ((currentPosition.height - 2) >= 0) &&
                currentBoard.getPositions()[(int) (currentPosition.width + 1)][(int) (currentPosition.height - 2)] == null) {
            potentialMoves.add(new Dimension2D(currentPosition.width + 1, currentPosition.height - 2));
        }

        if((currentPosition.width - 2 >= 0) && ((currentPosition.height - 1) >= 0) &&
                currentBoard.getPositions()[(int) (currentPosition.width - 2)][(int) (currentPosition.height - 1)] == null) {
            potentialMoves.add(new Dimension2D(currentPosition.width - 2, currentPosition.height - 1));
        }

        if((currentPosition.width - 1 >= 0) && ((currentPosition.height - 2) >= 0) &&
                currentBoard.getPositions()[(int) (currentPosition.width - 1)][(int) (currentPosition.height - 2)] == null) {
            potentialMoves.add(new Dimension2D(currentPosition.width - 1, currentPosition.height - 2));
        }

        return potentialMoves;
    }

    private static boolean isFirstMove(MoveHistory moveHistory, Dimension2D currentPosition) {
        for(ChessMove move : moveHistory.getMoves()) {
            if(isSamePosition(move.getPosition(), currentPosition) && move.getPreviousPosition() != FIRST_MOVE){
                return false;
            }
        }

        return true;
    }

    private static boolean isSamePosition(Dimension2D position1, Dimension2D position2) {
        if(position1 == null || position2 == null) {
            return false;
        }

        return position1.height == position2.height && position1.width == position2.width;
    }

}

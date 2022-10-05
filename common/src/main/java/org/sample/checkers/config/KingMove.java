package org.sample.checkers.config;

import com.sun.javafx.geom.Dimension2D;

import java.util.ArrayList;
import java.util.List;

import static org.sample.checkers.config.ChessSide.BLACK;
import static org.sample.checkers.config.ChessSide.WHITE;

//Kráľ sa pohne presne o jedno políčko horizontálne, vertikálne alebo diagonálne.
// Špeciálny ťah s kráľom známy ako rošáda je povolený iba raz na hráča, na hru (pozri nižšie).
public class KingMove  implements CheckersMove {

    @Override
    public List<Dimension2D> potentialMoves(ChessSide side, MoveHistory moveHistory, Dimension2D currentPosition, ChessBoardPositions currentBoard) {
        List<Dimension2D> potentialMoves = new ArrayList<>();

        //Raz za hru môže každý kráľ vykonať ťah známy ako rošáda. Rošáda spočíva v posunutí kráľa o dve polia smerom k veži rovnakej farby na rovnakej úrovni a potom v umiestnení veže na pole, ktoré kráľ prešiel.
        //
        //Rošáda je prípustná, ak sú splnené tieto podmienky:[1]
        //
        //Ani kráľ, ani veža sa predtým počas hry nepohli.
        //Medzi kráľom a vežou nie sú žiadne figúrky.
        //Kráľ nie je pod kontrolou a neprechádza ani nepristáva na žiadne pole napadnuté nepriateľskou figúrkou.
        //Rošáda je stále povolená, ak je veža pod útokom, alebo ak veža prekročí napadnuté pole.x
        if((side == WHITE && !moveHistory.isCastlingWhiteDone())
                || (side == BLACK && !moveHistory.isCastlingBlackDone())) {

            if(!kingNotMove(side, moveHistory, currentPosition)
                    || (!leftRookNotMove(side, moveHistory, currentBoard)
                    && !rightRookNotMove(side, moveHistory, currentBoard))) {
                if(side == WHITE){
                    moveHistory.setCastlingWhiteDone(true);
                } else {
                    moveHistory.setCastlingBlackDone(true);
                }
            }

            if(kingNotMove(side, moveHistory, currentPosition)
                    && leftRookNotMove(side, moveHistory, currentBoard)
                    && betweenLeftEmptyFields(side, currentBoard)
                    && kingNotCheck(side, currentBoard, moveHistory, currentPosition)
                    && !leftFieldsInAttack(side, currentBoard, moveHistory, currentPosition)) {
                potentialMoves.add(side == WHITE ? new Dimension2D(2,0) : new Dimension2D(2,7));
            }

            if(kingNotMove(side, moveHistory, currentPosition)
                    && rightRookNotMove(side, moveHistory, currentBoard)
                    && betweenRightEmptyFields(side, currentBoard)
                    && kingNotCheck(side, currentBoard, moveHistory, currentPosition)
                    && !rightFieldsInAttack(side, currentBoard, moveHistory, currentPosition)) {
                potentialMoves.add(side == WHITE ? new Dimension2D(6,0) : new Dimension2D(6,7));
            }
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

        if(((currentPosition.height-1) >= 0) &&
                (currentBoard.getSides()[(int) currentPosition.width][(int) currentPosition.height-1] == side.oposite())){
            potentialMoves.add(new Dimension2D(currentPosition.width, currentPosition.height-1));
        }
        if(((currentPosition.height+1) < 8) &&
                (currentBoard.getSides()[(int) currentPosition.width][(int) currentPosition.height+1] == side.oposite())){
            potentialMoves.add(new Dimension2D(currentPosition.width, currentPosition.height+1));
        }
        if(((currentPosition.width-1) >= 0) &&
                (currentBoard.getSides()[(int) currentPosition.width-1][(int) currentPosition.height] == side.oposite())){
            potentialMoves.add(new Dimension2D(currentPosition.width-1, currentPosition.height));
        }
        if(((currentPosition.width+1) <8) &&
                (currentBoard.getSides()[(int) currentPosition.width+1][(int) currentPosition.height] == side.oposite())){
            potentialMoves.add(new Dimension2D(currentPosition.width+1, currentPosition.height));
        }
        if(((currentPosition.width-1) >= 0) && ((currentPosition.height+1) < 8) &&
                (currentBoard.getSides()[(int) currentPosition.width-1][(int) currentPosition.height+1] == side.oposite())){
            potentialMoves.add(new Dimension2D(currentPosition.width-1, currentPosition.height+1));
        }
        if(((currentPosition.width-1) >= 0) && ((currentPosition.height-1) >= 0) &&
                currentBoard.getSides()[(int) currentPosition.width-1][(int) currentPosition.height-1] == side.oposite()){
            potentialMoves.add(new Dimension2D(currentPosition.width-1, currentPosition.height-1));
        }
        if(((currentPosition.width+1) < 8) && ((currentPosition.height+1) < 8) &&
                currentBoard.getSides()[(int) currentPosition.width+1][(int) currentPosition.height+1] == side.oposite()){
            potentialMoves.add(new Dimension2D(currentPosition.width+1, currentPosition.height+1));
        }
        if(((currentPosition.width+1) < 8) && ((currentPosition.height-1) >= 0) &&
                currentBoard.getSides()[(int) currentPosition.width+1][(int) currentPosition.height-1] == side.oposite()){
            potentialMoves.add(new Dimension2D(currentPosition.width+1, currentPosition.height-1));
        }

        return potentialMoves;
    }

    private boolean kingNotMove(ChessSide side, MoveHistory moveHistory, Dimension2D currentPosition){
        if(side == WHITE) {
            if(currentPosition.width == 4 && currentPosition.height == 0) {
                return moveHistory.getMoves().stream().noneMatch(move -> move.getPreviousPosition().width == 5
                        &&  move.getPreviousPosition().height == 1);
            }
        } else {
            if(currentPosition.width == 4 && currentPosition.height == 7) {
                return moveHistory.getMoves().stream().noneMatch(move -> move.getPreviousPosition().width == 5
                        &&  move.getPreviousPosition().height == 8);
            }
        }

        return false;
    }

    private boolean leftRookNotMove(ChessSide side, MoveHistory moveHistory, ChessBoardPositions currentBoard){
        if(side == WHITE) {
            if(currentBoard.getPositions()[0][0] == ChessFigure.ROOK) {
                return moveHistory.getMoves().stream().noneMatch(move -> move.getPreviousPosition().width == 1
                        &&  move.getPreviousPosition().height == 1);
            }
        } else {
            if(currentBoard.getPositions()[0][7] == ChessFigure.ROOK) {
                return moveHistory.getMoves().stream().noneMatch(move -> move.getPreviousPosition().width == 8
                        &&  move.getPreviousPosition().height == 1);
            }
        }

        return false;
    }

    private boolean rightRookNotMove(ChessSide side, MoveHistory moveHistory, ChessBoardPositions currentBoard){
        if(side == WHITE) {
            if(currentBoard.getPositions()[7][0] == ChessFigure.ROOK) {
                return moveHistory.getMoves().stream().noneMatch(move -> move.getPreviousPosition().width ==8
                        &&  move.getPreviousPosition().height == 1);
            }
        } else {
            if(currentBoard.getPositions()[7][7] == ChessFigure.ROOK) {
                return moveHistory.getMoves().stream().noneMatch(move -> move.getPreviousPosition().width == 8
                        &&  move.getPreviousPosition().height == 8);
            }
        }

        return false;
    }

    private boolean betweenLeftEmptyFields(ChessSide side, ChessBoardPositions currentBoard){
        if(side == WHITE) {
            return currentBoard.getPositions()[1][0] == null && currentBoard.getPositions()[2][0] == null
                    && currentBoard.getPositions()[3][0] == null;
        } else {
            return currentBoard.getPositions()[1][7] == null && currentBoard.getPositions()[2][7] == null
                    && currentBoard.getPositions()[3][7] == null;
        }
    }

    private boolean betweenRightEmptyFields(ChessSide side, ChessBoardPositions currentBoard){
        if(side == WHITE) {
            return currentBoard.getPositions()[5][0] == null && currentBoard.getPositions()[6][0] == null;
        } else {
            return currentBoard.getPositions()[5][7] == null && currentBoard.getPositions()[6][7] == null;
        }
    }

    private boolean kingNotCheck(ChessSide side, ChessBoardPositions currentBoard, MoveHistory moveHistory,
                                 Dimension2D currentPosition){
        for(int width = 0; width <8 ; width++) {
            for (int heigth = 0; heigth <8; heigth++) {
                if(side.oposite() == currentBoard.getSides()[width][heigth]) {
                    ChessFigure figure = currentBoard.getPositions()[width][heigth];
                    Dimension2D checkPosition = new Dimension2D(width, heigth);

                    if(figure == ChessFigure.KING && kingInStartPosition(width, heigth, side.oposite()) ){
                        continue;
                    }

                    List<Dimension2D> potentialMoves = CheckersMoveFactory
                            .getMove(figure).potentialMoves(side.oposite(), moveHistory, checkPosition, currentBoard);
                    if(potentialMoves.stream().noneMatch(move -> move.width == currentPosition.width &&
                            move.height == currentPosition.height)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static boolean kingInStartPosition(int width, int height, ChessSide side) {
        if(side == BLACK){
            return width == 4 && height == 7;
        } else {
            return width == 4 && height == 0;
        }
    }

    private boolean leftFieldsInAttack(ChessSide side, ChessBoardPositions currentBoard, MoveHistory moveHistory,
                                       Dimension2D currentPosition){

        for(int width = 0; width <8 ; width++) {
            for (int heigth = 0; heigth <8; heigth++) {
                if(side.oposite() == currentBoard.getSides()[width][heigth]) {
                    ChessFigure figure = currentBoard.getPositions()[width][heigth];
                    Dimension2D checkPosition = new Dimension2D(width, heigth);

                    if(figure == ChessFigure.KING && kingInStartPosition(width, heigth, side.oposite()) ){
                        continue;
                    }

                    List<Dimension2D> potentialMoves = CheckersMoveFactory
                            .getMove(figure).potentialMoves(side.oposite(), moveHistory, checkPosition, currentBoard);
                    if(side == WHITE && potentialMoves.stream().anyMatch(move -> (move.width == 2 &&
                            move.height == 1) || (move.width == 3 &&
                            move.height == 1) || (move.width == 4 &&
                            move.height == 1))) {
                        return true;
                    } else if (side == BLACK && potentialMoves.stream().anyMatch(move -> (move.width == 2 &&
                            move.height == 8) || (move.width == 3 &&
                            move.height == 8) || (move.width == 4 &&
                            move.height == 8))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean rightFieldsInAttack(ChessSide side, ChessBoardPositions currentBoard, MoveHistory moveHistory,
                                        Dimension2D currentPosition){
        for(int width = 0; width <8 ; width++) {
            for (int heigth = 0; heigth <8; heigth++) {
                if(side.oposite() == currentBoard.getSides()[width][heigth]) {
                    ChessFigure figure = currentBoard.getPositions()[width][heigth];
                    Dimension2D checkPosition = new Dimension2D(width, heigth);

                    if(figure == ChessFigure.KING && kingInStartPosition(width, heigth, side.oposite())){
                        continue;
                    }

                    List<Dimension2D> potentialMoves = CheckersMoveFactory
                            .getMove(figure).potentialMoves(side.oposite(), moveHistory, checkPosition, currentBoard);
                    if(side == WHITE && potentialMoves.stream().anyMatch(move -> (move.width == 6 &&
                            move.height == 1) || (move.width == 7 && move.height == 1))) {
                        return true;
                    } else if (side == BLACK && potentialMoves.stream().anyMatch(move -> (move.width == 6 &&
                            move.height == 8) || (move.width == 7 && move.height == 8))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}

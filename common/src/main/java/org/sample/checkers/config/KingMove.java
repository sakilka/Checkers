package org.sample.checkers.config;

import com.sun.javafx.geom.Dimension2D;

import java.util.ArrayList;
import java.util.List;

//Kráľ sa pohne presne o jedno políčko horizontálne, vertikálne alebo diagonálne.
// Špeciálny ťah s kráľom známy ako rošáda je povolený iba raz na hráča, na hru (pozri nižšie).
public class KingMove  implements CheckersMove {

    @Override
    public List<Dimension2D> potentialMoves(ChessSide side, MoveHistory moveHistory, Dimension2D currentPosition, ChessBoardPositions currentBoard) {
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
}

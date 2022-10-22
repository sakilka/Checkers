package org.sample.checkers.config.chess;

import com.sun.javafx.geom.Dimension2D;

import java.util.ArrayList;
import java.util.List;

public class QueenMove implements ChessMove {

    //Dáma sa pohybuje o ľubovoľný počet voľných polí vodorovne, zvisle alebo diagonálne.
    @Override
    public List<Dimension2D> potentialMoves(ChessSide side, ChessMoveHistory chessMoveHistory, Dimension2D currentPosition,
                                            ChessBoardPositions currentBoard) {
        List<Dimension2D> potentialMoves = new ArrayList<>();

        for(int shift = 1; (currentPosition.width + shift) < 8; shift ++) {

            if(currentBoard.getPositions()[(int) currentPosition.width+shift][(int) currentPosition.height] != null) {
                if(currentBoard.getSides()[(int) currentPosition.width+shift][(int) currentPosition.height] == side.oposite()){
                    potentialMoves.add(new Dimension2D(currentPosition.width+shift, currentPosition.height));
                }

                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width+shift, currentPosition.height));
        }

        for(int shift = 1; (currentPosition.width - shift) >= 0; shift ++) {

            if(currentBoard.getPositions()[(int) currentPosition.width-shift][(int) currentPosition.height] != null) {
                if(currentBoard.getSides()[(int) currentPosition.width-shift][(int) currentPosition.height] == side.oposite()){
                    potentialMoves.add(new Dimension2D(currentPosition.width-shift, currentPosition.height));
                }

                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width-shift, currentPosition.height));
        }

        for(int shift = 1; (currentPosition.height + shift) < 8; shift ++) {

            if(currentBoard.getPositions()[(int) currentPosition.width][(int) currentPosition.height+shift] != null) {
                if(currentBoard.getSides()[(int) currentPosition.width][(int) currentPosition.height+shift] == side.oposite()){
                    potentialMoves.add(new Dimension2D(currentPosition.width, currentPosition.height+shift));
                }

                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width, currentPosition.height+shift));
        }

        for(int shift = 1; (currentPosition.height - shift) >= 0; shift ++) {

            if(currentBoard.getPositions()[(int) currentPosition.width][(int) currentPosition.height- shift] != null) {
                if(currentBoard.getSides()[(int) currentPosition.width][(int) currentPosition.height- shift] == side.oposite()){
                    potentialMoves.add(new Dimension2D(currentPosition.width, currentPosition.height - shift));
                }

                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width, currentPosition.height - shift));
        }

        for(int shift = 1; (((currentPosition.width + shift) < 8) && ((currentPosition.height + shift) < 8)); shift ++) {
            if (currentBoard.getPositions()[(int) currentPosition.width + shift]
                    [(int) currentPosition.height + shift] != null) {

                if(currentBoard.getSides()[(int) currentPosition.width + shift]
                        [(int) currentPosition.height + shift] == side.oposite()){
                    potentialMoves.add(new Dimension2D(currentPosition.width + shift, currentPosition.height + shift));
                }

                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width + shift, currentPosition.height + shift));
        }

        for(int shift = 1; (((currentPosition.width - shift) >= 0) && ((currentPosition.height - shift) >= 0)); shift ++) {
            if (currentBoard.getPositions()[(int) currentPosition.width - shift]
                    [(int) currentPosition.height - shift] != null) {

                if(currentBoard.getSides()[(int) currentPosition.width - shift]
                        [(int) currentPosition.height - shift] == side.oposite()){
                    potentialMoves.add(new Dimension2D(currentPosition.width - shift, currentPosition.height - shift));
                }

                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width - shift, currentPosition.height - shift));
        }

        for(int shift = 1; (((currentPosition.width + shift) < 8) && ((currentPosition.height - shift) >= 0)); shift ++) {
            if (currentBoard.getPositions()[(int) currentPosition.width + shift]
                    [(int) currentPosition.height - shift] != null) {

                if(currentBoard.getSides()[(int) currentPosition.width + shift]
                        [(int) currentPosition.height - shift] == side.oposite()){
                    potentialMoves.add(new Dimension2D(currentPosition.width + shift, currentPosition.height - shift));
                }

                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width + shift, currentPosition.height - shift));
        }

        for(int shift = 1; (((currentPosition.width - shift) >= 0) && ((currentPosition.height + shift) < 8)); shift ++) {
            if (currentBoard.getPositions()[(int) currentPosition.width - shift]
                    [(int) currentPosition.height + shift] != null) {

                if(currentBoard.getSides()[(int) currentPosition.width - shift]
                        [(int) currentPosition.height + shift] == side.oposite()){
                    potentialMoves.add(new Dimension2D(currentPosition.width - shift, currentPosition.height + shift));
                }

                break;
            }

            potentialMoves.add(new Dimension2D(currentPosition.width - shift, currentPosition.height + shift));
        }

        return potentialMoves;
    }
}

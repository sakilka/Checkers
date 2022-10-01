package org.sample.checkers.config;

import com.sun.javafx.geom.Dimension2D;

import java.util.ArrayList;
import java.util.List;

public class KnightMove implements CheckersMove {

    //Rytier sa presunie na jedno z najbližších polí, ktoré nemá rovnakú hodnosť, súbor alebo uhlopriečku.
    // (To si možno predstaviť ako pohyb o dve políčka horizontálne, potom jedno políčko zvislo,
    // alebo pohyb o jedno políčko vodorovne a potom dve políčka zvislo – t. j. vo vzore „L“.)
    // Jazdec nie je blokovaný inými figúrkami; preskočí na nové miesto.
    @Override
    public List<Dimension2D> potentialMoves(ChessSide side, MoveHistory moveHistory, Dimension2D currentPosition, ChessBoardPositions currentBoard) {
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

        if((currentPosition.width - 2 >= 0) && ((currentPosition.height + 1) < 8) &&
                currentBoard.getSides()[(int) (currentPosition.width - 2)][(int) (currentPosition.height + 1)] == side.oposite()) {
            potentialMoves.add(new Dimension2D(currentPosition.width - 2, currentPosition.height + 1));
        }

        if((currentPosition.width - 1 >= 0) && ((currentPosition.height + 2) < 8) &&
                currentBoard.getSides()[(int) (currentPosition.width - 1)][(int) (currentPosition.height + 2)] == side.oposite()) {
            potentialMoves.add(new Dimension2D(currentPosition.width - 1, currentPosition.height + 2));
        }

        if((currentPosition.width + 1 < 8) && ((currentPosition.height + 2) < 8) &&
                currentBoard.getSides()[(int) (currentPosition.width + 1)][(int) (currentPosition.height + 2)] == side.oposite()) {
            potentialMoves.add(new Dimension2D(currentPosition.width + 1, currentPosition.height + 2));
        }

        if((currentPosition.width + 2 < 8) && ((currentPosition.height + 1) < 8) &&
                currentBoard.getSides()[(int) (currentPosition.width + 2)][(int) (currentPosition.height + 1)] == side.oposite()) {
            potentialMoves.add(new Dimension2D(currentPosition.width + 2, currentPosition.height + 1));
        }

        if((currentPosition.width + 2 < 8) && ((currentPosition.height - 1) >= 0) &&
                currentBoard.getSides()[(int) (currentPosition.width + 2)][(int) (currentPosition.height - 1)] == side.oposite()) {
            potentialMoves.add(new Dimension2D(currentPosition.width + 2, currentPosition.height - 1));
        }

        if((currentPosition.width + 1 < 8) && ((currentPosition.height - 2) >= 0) &&
                currentBoard.getSides()[(int) (currentPosition.width + 1)][(int) (currentPosition.height - 2)] == side.oposite()) {
            potentialMoves.add(new Dimension2D(currentPosition.width + 1, currentPosition.height - 2));
        }

        if((currentPosition.width - 2 >= 0) && ((currentPosition.height - 1) >= 0) &&
                currentBoard.getSides()[(int) (currentPosition.width - 2)][(int) (currentPosition.height - 1)] == side.oposite()) {
            potentialMoves.add(new Dimension2D(currentPosition.width - 2, currentPosition.height - 1));
        }

        if((currentPosition.width - 1 >= 0) && ((currentPosition.height - 2) >= 0) &&
                currentBoard.getSides()[(int) (currentPosition.width - 1)][(int) (currentPosition.height - 2)] == side.oposite()) {
            potentialMoves.add(new Dimension2D(currentPosition.width - 1, currentPosition.height - 2));
        }

        return potentialMoves;
    }
}

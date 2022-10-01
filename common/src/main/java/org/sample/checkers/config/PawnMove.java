package org.sample.checkers.config;

import com.sun.javafx.geom.Dimension2D;

import java.util.ArrayList;
import java.util.List;

public class PawnMove implements CheckersMove {

    //Pešiaci majú najzložitejšie pravidlá pohybu:
    //Pešiak sa pohne o jedno pole priamo vpred, ak je toto pole prázdne.
    // Ak sa pešiak ešte nepohol, má tiež možnosť pohnúť sa o dve polia priamo vpred, ak sú obe polia prázdne.
    // Pešiaci sa nemôžu pohybovať dozadu.

    //Pešiak, na rozdiel od iných figúrok, vyhadzuje inak, ako sa pohybuje.
    // Pešiak môže zajať nepriateľskú figúrku na ktoromkoľvek z dvoch polí diagonálne pred pešiakom.
    // Nemôže sa presunúť na tieto polia, keď sú prázdne, okrem prípadov, keď je zajatý en passant.

    //Pešiak je tiež zapojený do dvoch špeciálnych ťahov en passant a povýšenia
    @Override
    public List<Dimension2D> potentialMoves(ChessSide side, MoveHistory moveHistory, Dimension2D currentPosition, ChessBoardPositions currentBoard) {
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

        if(currentPosition.width >=0 &&
                currentBoard.getSides()[(int) currentPosition.width-1][(int) currentPosition.height + 1]
                        == side.oposite())  {
            potentialMoves.add(new Dimension2D(currentPosition.width-1, currentPosition.height + 1));
        }

        if(currentPosition.width < 8 &&
                currentBoard.getSides()[(int) currentPosition.width+1][(int) currentPosition.height + 1]
                        == side.oposite())  {
            potentialMoves.add(new Dimension2D(currentPosition.width+1, currentPosition.height + 1));
        }

        return potentialMoves;
    }

    private static boolean isFirstMove(MoveHistory moveHistory, Dimension2D currentPosition) {
        for(ChessMove move : moveHistory.getMoves()) {
            if(isSamePosition(move.getPosition(), currentPosition)){
                return false;
            }
        }

        return true;
    }

    private static boolean isSamePosition(Dimension2D position1, Dimension2D position2) {
        if(position1 == null || position2 == null) {
            return false;
        }

        return position1.height == position2.height + 1 && position1.width == position2.width + 1;
    }
}

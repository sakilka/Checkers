package org.sample.checkers.config.chess;

import com.sun.javafx.geom.Dimension2D;

import java.util.ArrayList;
import java.util.List;

import static org.sample.checkers.config.chess.ChessSide.WHITE;

public class PawnMove implements ChessMove {

    //Pešiaci majú najzložitejšie pravidlá pohybu:
    //Pešiak sa pohne o jedno pole priamo vpred, ak je toto pole prázdne.
    // Ak sa pešiak ešte nepohol, má tiež možnosť pohnúť sa o dve polia priamo vpred, ak sú obe polia prázdne.
    // Pešiaci sa nemôžu pohybovať dozadu.

    //Pešiak, na rozdiel od iných figúrok, vyhadzuje inak, ako sa pohybuje.
    // Pešiak môže zajať nepriateľskú figúrku na ktoromkoľvek z dvoch polí diagonálne pred pešiakom.
    // Nemôže sa presunúť na tieto polia, keď sú prázdne, okrem prípadov, keď je zajatý en passant.

    //Pešiak je tiež zapojený do dvoch špeciálnych ťahov en passant a povýšenia
    @Override
    public List<Dimension2D> potentialMoves(ChessSide side, ChessMoveHistory chessMoveHistory, Dimension2D currentPosition, ChessBoardPositions currentBoard) {
        List<Dimension2D> potentialMoves = new ArrayList<>();

        enPassant(potentialMoves, side, chessMoveHistory, currentBoard, currentPosition);

        if(side == WHITE) {

            if (isFirstMove(chessMoveHistory, currentPosition)) {
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

            if(currentPosition.width > 0 &&
                    currentBoard.getSides()[(int) currentPosition.width-1][(int) currentPosition.height + 1]
                            == side.oposite())  {
                potentialMoves.add(new Dimension2D(currentPosition.width-1, currentPosition.height + 1));
            }

            if(currentPosition.width < 7 &&
                    currentBoard.getSides()[(int) currentPosition.width+1][(int) currentPosition.height + 1]
                            == side.oposite())  {
                potentialMoves.add(new Dimension2D(currentPosition.width+1, currentPosition.height + 1));
            }
        } else {
            if (isFirstMove(chessMoveHistory, currentPosition)) {
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

            if(currentPosition.width > 0 &&
                    currentBoard.getSides()[(int) currentPosition.width-1][(int) currentPosition.height - 1]
                            == side.oposite())  {
                potentialMoves.add(new Dimension2D(currentPosition.width-1, currentPosition.height - 1));
            }

            if(currentPosition.width < 7 &&
                    currentBoard.getSides()[(int) currentPosition.width+1][(int) currentPosition.height - 1]
                            == side.oposite())  {
                potentialMoves.add(new Dimension2D(currentPosition.width+1, currentPosition.height - 1));
            }
        }

        return potentialMoves;
    }

    // Keď pešiak urobí dvojkrokový postup zo svojej východiskovej pozície a na poli vedľa cieľového poľa v susednom súbore je súperov pešiak,
    // potom ho môže súperov pešiak zajať en passant ("prechádzajúc") a posunúť sa na pole,
    // cez ktoré pešiak prešiel. Toto je možné vykonať iba na ťahu bezprostredne nasledujúcom po postupe nepriateľského pešiaka o dve políčka;
    // inak právo na to zaniká.
    private void enPassant(List<Dimension2D> potentialMoves, ChessSide side, ChessMoveHistory chessMoveHistory, ChessBoardPositions currentBoard, Dimension2D currentPosition) {
        if(side == WHITE) {
            if(currentPosition.height == 4
                    && lastMoveOppositePawnTwoStepsAndIsNear(side, chessMoveHistory, currentBoard, currentPosition)){
                potentialMoves.add(getEnPassant(side, chessMoveHistory));
            }
        } else {
            if(currentPosition.height == 3
                    && lastMoveOppositePawnTwoStepsAndIsNear(side, chessMoveHistory, currentBoard, currentPosition)){
                potentialMoves.add(getEnPassant(side, chessMoveHistory));
            }
        }
    }

    private Dimension2D getEnPassant(ChessSide side, ChessMoveHistory chessMoveHistory) {
        ChessMovePosition lastMove = chessMoveHistory.getMoves().get(chessMoveHistory.getMoves().size() - 1);

        if(side == WHITE) {
            return new Dimension2D(lastMove.getPosition().width -1, lastMove.getPosition().height);
        } else {
            return new Dimension2D(lastMove.getPosition().width -1, lastMove.getPosition().height - 2);
        }
    }

    private boolean lastMoveOppositePawnTwoStepsAndIsNear(ChessSide side, ChessMoveHistory chessMoveHistory,
                                                          ChessBoardPositions currentBoard, Dimension2D currentPosition) {
        ChessMovePosition lastMove = chessMoveHistory.getMoves().get(chessMoveHistory.getMoves().size() - 1);
        if(currentBoard.getPositions()[(int) lastMove.getPosition().width-1][(int)lastMove.getPosition().height-1]
                == ChessFigure.PAWN && currentBoard.getSides()[(int) lastMove.getPosition().width-1]
                [(int)lastMove.getPosition().height-1] == side.oposite()) {
            if(Math.abs(lastMove.getPreviousPosition().height - lastMove.getPosition().height) == 2){
                return ((currentPosition.height+1) == (lastMove.getPosition().height))
                        && (Math.abs((currentPosition.width + 1) - lastMove.getPosition().width) == 1);
            }
        }

        return false;
    }

    private static boolean isFirstMove(ChessMoveHistory chessMoveHistory, Dimension2D currentPosition) {
        for(ChessMovePosition move : chessMoveHistory.getMoves()) {
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

package org.sample.checkers.config.checkers;

import com.sun.javafx.geom.Dimension2D;

import java.util.ArrayList;
import java.util.List;

public class QueenMove implements CheckersMove {

    //Kameny se pohybují po diagonálách po černých políčkách, vpřed, ale ne vzad, a nemohou přeskakovat kameny vlastní barvy.
    //Pokud obyčejný kámen dojde na druhou stranu šachovnice, přemění se v dámu.
    //Dáma se pohybuje diagonálně dopředu a dozadu o libovolný počet polí.
    //Jestliže se kámen nachází na diagonále v sousedství soupeřovy figury, za kterou je volné pole, je povinen ji přeskočit, obsadit toto volné pole a odstranit přeskočenou soupeřovu figuru z desky.
    //Skákání je povinné. Může-li hráč skákat jak dámou, tak obyčejným kamenem, musí skákat dámou. V případě, že může jedním kamenem provést více variant skoku, je na něm, kterou si vybere, musí ovšem variantu doskákat (např. když může jedním kamenem skočit jeden, nebo tři kameny, skočí tedy jeden, nebo tři. Nelze skočit jen dva z druhé varianty.)
    //Při vícenásobném skoku se kameny odstraní až po dokončení celé sekvence. Přes jeden kámen nelze skákat vícekrát.
    //Hráč, který je na tahu a nemůže hrát (nemá kameny, nebo má všechny zablokované), prohrál. Partie končí remízou tehdy, když je teoreticky nemožné vzít soupeři při pozorné hře žádnou další figuru.
    //Jestliže některý z hráčů zahraje tah v rozporu s pravidly (např. opomenutí skákání), je na jeho protihráči, jestli bude vyžadovat opravu tahu, nebo ne.
    @Override
    public List<Dimension2D> potentialMoves(CheckersSide side, Dimension2D currentPosition, CheckersBoardPositions currentBoard) {
        List<Dimension2D> potentialMoves = new ArrayList<>();

        if(queenPositionMustJump(side, currentPosition, currentBoard)) {

            for(int shift = 1; (((currentPosition.width + shift) < 8) && ((currentPosition.height + shift) < 8)); shift ++) {
                if(currentBoard.getSides()[(int) (currentPosition.width + shift)][(int) (currentPosition.height + shift)]
                        == side.oposite()
                        && (currentPosition.width + shift + 1) < 8
                        && (currentPosition.height + shift + 1) < 8
                        && currentBoard.getSides()[(int) (currentPosition.width + shift + 1)]
                        [(int) (currentPosition.height + shift + 1)] == null) {
                    for(shift = shift +1; (((currentPosition.width + shift) < 8)
                            && ((currentPosition.height + shift) < 8)); shift++) {
                        if(currentBoard.getSides()[(int) (currentPosition.width + shift)][(int) (currentPosition.height + shift)]
                                != null) {
                            break;
                        }
                        potentialMoves.add(new Dimension2D(currentPosition.width + shift, currentPosition.height + shift));
                    }
                }
            }

            for(int shift = 1; (((currentPosition.width - shift) >= 0) && ((currentPosition.height- shift) >= 0)); shift ++) {
                if(currentBoard.getSides()[(int) (currentPosition.width - shift)][(int) (currentPosition.height - shift)]
                        == side.oposite()
                        && (currentPosition.width - shift - 1) >= 0
                        && (currentPosition.height - shift - 1) >= 0
                        && currentBoard.getSides()[(int) (currentPosition.width - shift - 1)]
                        [(int) (currentPosition.height - shift - 1)] == null) {
                    for(shift = shift + 1; (((currentPosition.width - shift) >= 0)
                            && ((currentPosition.height - shift) >= 0)); shift++) {
                        if(currentBoard.getSides()[(int) (currentPosition.width - shift)][(int) (currentPosition.height - shift)]
                                != null) {
                            break;
                        }
                        potentialMoves.add(new Dimension2D(currentPosition.width - shift, currentPosition.height - shift));
                    }

                    break;
                }
            }

            for(int shift = 1; (((currentPosition.width + shift) < 8) && ((currentPosition.height - shift) >= 0)); shift ++) {
                if(currentBoard.getSides()[(int) (currentPosition.width + shift)][(int) (currentPosition.height - shift)]
                        == side.oposite()
                        && (currentPosition.width + shift + 1) < 8
                        && (currentPosition.height - shift - 1) >= 0
                        && currentBoard.getSides()[(int) (currentPosition.width + shift + 1)]
                        [(int) (currentPosition.height - shift - 1)] == null) {
                    for(shift = shift +1; (((currentPosition.width + shift) < 8)
                            && ((currentPosition.height - shift) >= 0)); shift++) {
                        if(currentBoard.getSides()[(int) (currentPosition.width + shift)][(int) (currentPosition.height - shift)]
                                != null) {
                            break;
                        }
                        potentialMoves.add(new Dimension2D(currentPosition.width + shift, currentPosition.height - shift));
                    }

                    break;
                }
            }

            for(int shift = 1; (((currentPosition.width - shift) >= 0) && ((currentPosition.height + shift) < 8)); shift ++) {
                if(currentBoard.getSides()[(int) (currentPosition.width - shift)][(int) (currentPosition.height + shift)]
                        == side.oposite()
                        && (currentPosition.width - shift - 1) >= 0
                        && (currentPosition.height + shift + 1) < 8
                        && currentBoard.getSides()[(int) (currentPosition.width - shift - 1)]
                        [(int) (currentPosition.height + shift + 1)] == null) {
                    for(shift = shift + 1; (((currentPosition.width - shift) >= 0)
                            && ((currentPosition.height + shift) < 8)); shift++) {
                        if(currentBoard.getSides()[(int) (currentPosition.width - shift)][(int) (currentPosition.height + shift)]
                                != null) {
                            break;
                        }
                        potentialMoves.add(new Dimension2D(currentPosition.width - shift, currentPosition.height + shift));
                    }

                    break;
                }
            }

            return potentialMoves;
        }

        if(queenMustJump(side, currentPosition, currentBoard)
                && !queenPositionMustJump(side, currentPosition, currentBoard)) {
            return new ArrayList<>();
        }

        if(mustJumpAnotherFigure(side, currentPosition, currentBoard)){
            return new ArrayList<>();
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

    private boolean queenMustJump(CheckersSide side, Dimension2D currentPosition, CheckersBoardPositions currentBoard) {
        for (int width = 0; width < 8; width++) {
            for (int height = 0; height < 8; height++) {
                if(currentBoard.getSides()[width][height] == side
                        && currentBoard.getPositions()[width][height] == CheckersFigure.QUEEN
                        && !(currentPosition.width == width && currentPosition.height == height)){
                    if(queenPositionMustJump(side, new Dimension2D(width, height), currentBoard)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean queenPositionMustJump(CheckersSide side, Dimension2D currentPosition,
                                          CheckersBoardPositions currentBoard) {

        for(int shift = 1; (((currentPosition.width + shift) < 8) && ((currentPosition.height + shift) < 8)); shift ++) {
            if(currentBoard.getSides()[(int) (currentPosition.width + shift)][(int) (currentPosition.height + shift)]
                    == side.oposite()
                    && (currentPosition.width + shift + 1) < 8
                    && (currentPosition.height + shift + 1) < 8
                    && currentBoard.getSides()[(int) (currentPosition.width + shift + 1)]
                    [(int) (currentPosition.height + shift + 1)] == null) {
                return true;
            }
        }

        for(int shift = 1; (((currentPosition.width - shift) >= 0) && ((currentPosition.height- shift) >= 0)); shift ++) {
            if(currentBoard.getSides()[(int) (currentPosition.width - shift)][(int) (currentPosition.height - shift)]
                    == side.oposite()
                    && (currentPosition.width - shift - 1) >= 0
                    && (currentPosition.height - shift - 1) >= 0
                    && currentBoard.getSides()[(int) (currentPosition.width - shift - 1)]
                    [(int) (currentPosition.height - shift - 1)] == null) {
                return true;
            }
        }

        for(int shift = 1; (((currentPosition.width + shift) < 8) && ((currentPosition.height - shift) >= 0)); shift ++) {
            if(currentBoard.getSides()[(int) (currentPosition.width + shift)][(int) (currentPosition.height - shift)]
                    == side.oposite()
                    && (currentPosition.width + shift + 1) < 8
                    && (currentPosition.height - shift - 1) >= 0
                    && currentBoard.getSides()[(int) (currentPosition.width + shift + 1)]
                    [(int) (currentPosition.height - shift - 1)] == null) {
                return true;
            }
        }

        for(int shift = 1; (((currentPosition.width - shift) >= 0) && ((currentPosition.height + shift) < 8)); shift ++) {
            if(currentBoard.getSides()[(int) (currentPosition.width - shift)][(int) (currentPosition.height + shift)]
                    == side.oposite()
                    && (currentPosition.width - shift - 1) >= 0
                    && (currentPosition.height + shift + 1) < 8
                    && currentBoard.getSides()[(int) (currentPosition.width - shift - 1)]
                    [(int) (currentPosition.height + shift + 1)] == null) {
                return true;
            }
        }

        return false;
    }

    private boolean mustJumpAnotherFigure(CheckersSide side, Dimension2D currentPosition, CheckersBoardPositions currentBoard) {

        if(queenPositionMustJump(side, currentPosition, currentBoard)) {
            return false;
        }

        for (int width = 0; width < 8; width++) {
            for (int height = 0; height < 8; height++) {
                if(currentBoard.getSides()[width][height] == side
                        && currentBoard.getPositions()[width][height] == CheckersFigure.PAWN
                        && !(currentPosition.width == width && currentPosition.height == height)){
                    if(currentPositionMustJump(side, new Dimension2D(width, height), currentBoard)){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean currentPositionMustJump(CheckersSide side, Dimension2D currentPosition,
                                            CheckersBoardPositions currentBoard) {
        if(side == CheckersSide.WHITE) {
            if ((currentPosition.width -2) >=0 && (currentPosition.height +2) <8 &&
                    (currentBoard.getSides()[(int) currentPosition.width -1][(int) currentPosition.height +1]
                            == side.oposite()) && (currentBoard.getSides()[(int) currentPosition.width -2]
                    [(int) currentPosition.height +2] == null)){
                return true;
            }
            if ((currentPosition.width +2) <8 && (currentPosition.height +2) <8 &&
                    (currentBoard.getSides()[(int) currentPosition.width +1][(int) currentPosition.height +1]
                            == side.oposite()) &&
                    (currentBoard.getSides()[(int) currentPosition.width +2][(int) currentPosition.height +2] == null)){
                return true;
            }

        } else {
            if ((currentPosition.width -2) >=0 && (currentPosition.height -2) >=0 &&
                    (currentBoard.getSides()[(int) currentPosition.width -1][(int) currentPosition.height -1]
                            == side.oposite()) && (currentBoard.getSides()[(int) currentPosition.width -2]
                    [(int) currentPosition.height -2] == null)){
                return true;
            }
            if ((currentPosition.width +2) <8 && (currentPosition.height -2) >=0 &&
                    (currentBoard.getSides()[(int) currentPosition.width +1][(int) currentPosition.height -1]
                            == side.oposite()) && (currentBoard.getSides()[(int) currentPosition.width +2]
                    [(int) currentPosition.height -2] == null)){
                return true;
            }
        }

        return false;
    }
}

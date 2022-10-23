package org.sample.checkers.config.checkers;

import com.sun.javafx.geom.Dimension2D;

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
        return null;
    }
}

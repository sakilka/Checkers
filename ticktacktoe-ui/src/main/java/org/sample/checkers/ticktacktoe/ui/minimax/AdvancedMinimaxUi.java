package org.sample.checkers.ticktacktoe.ui.minimax;

import com.sun.javafx.geom.Dimension2D;
import javafx.util.Pair;
import org.sample.checkers.config.ticktacktoe.TickTackToeMove;
import org.sample.checkers.config.ticktacktoe.TickTackToeMoveHistory;
import org.sample.checkers.config.ticktacktoe.ToeSide;
import org.sample.checkers.ticktacktoe.ui.TickTackToeUi;
import org.sample.checkers.ticktacktoe.ui.minimax.techniques.BitboardGame;
import org.sample.checkers.ticktacktoe.ui.minimax.techniques.BitboardsUtil;
import org.sample.checkers.ticktacktoe.ui.minimax.techniques.MinimaxMove;
import org.sample.checkers.ticktacktoe.ui.minimax.techniques.Record;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class AdvancedMinimaxUi implements TickTackToeUi {

    private static final int DIFFICULTY = 5;

    @Override
    public TickTackToeMove computeNextMove(TickTackToeMoveHistory history) {
        ToeSide[][] currentBoard = history.getCurrentBoardFromHistory();

        Dimension2D randomFreeMove = getRandomFreeMove(currentBoard);

        BitboardGame game = new BitboardGame(currentBoard[0].length, currentBoard.length, 5);
        //Record move = minimax(game, DIFFICULTY, DIFFICULTY, ToeSide.CROSS, true);

        return new TickTackToeMove(randomFreeMove, history.getOnMove());
    }

    private Record minimax(BitboardGame game, int depth, int currentDepth, ToeSide currentPlayer, boolean maximize) {
        // Recursion anchor -> Evaluate board
        if (currentDepth == 0 || game.isGameOver(currentPlayer)) {
            return new Record(null, null, game.evaluate(), currentPlayer);
        }

        List<Integer> possibleMoves = game.listMoves(true);

        /**
         * If there's a move which results in a win for the current player we immediately return this move.
         * This way we don't have to evaluate other possible moves -> Performance + 1 :)
         *
         * We add the currentDepth, otherwise the AI plays randomly if it will lose definitely.
         * This way the AI tries to "survive" as long as possible, even if it can't win anymore.
         */
        for(Integer position : possibleMoves) {
            MinimaxMove move = new MinimaxMove(position);
            BitboardGame tmpGame = new BitboardGame(game.getHeight(), game.getColumn(), game.getWinSize())
                    .move(move, 0L);

            if(tmpGame.hasWinner(currentPlayer)){
                return new Record(game.getStorageRecordPrimaryKey(), move, 0 , game.getCurrentPlayer());
            }
        }

        // Call recursively from here on for each move to find best one
        Pair<MinimaxMove, Integer> minOrMax = new Pair<>(null, maximize ? Integer.MIN_VALUE : Integer.MAX_VALUE);

        for(Integer position : possibleMoves) {
            MinimaxMove move = new MinimaxMove(position);
            BitboardGame newGame = game.move(move, 0L);
            Integer moveScore = minimax(newGame, depth, currentDepth -1, currentPlayer.opposite(), !maximize)
                    .getScore();

            if((maximize && moveScore > minOrMax.getValue())|| (!maximize && moveScore < minOrMax.getValue())){
                minOrMax = new Pair(move, moveScore);
            }
        }

        Record finalMove = new Record(game.getStorageRecordPrimaryKey(), minOrMax.getKey(), minOrMax.getValue(), currentPlayer);

        // Add board evaluation temporary to storage
        // For data seeding we don't add the final move because it's added inside the seed method.
//        if (game.getStorageIndex() >= 0 && !(depth == currentDepth))
//            Storage.doStorageLookup<Move>(game.storageIndex).map[game.storageRecordPrimaryKey] = finalMove

        return finalMove;
    }

    /*
    function minimax(node, depth, maximizingPlayer) is
    if depth = 0 or node is a terminal node then
        return the heuristic value of node
    if maximizingPlayer then
        value := −∞
        for each child of node do
            value := max(value, minimax(child, depth − 1, FALSE))
        return value
    else (* minimizing player *)
        value := +∞
        for each child of node do
            value := min(value, minimax(child, depth − 1, TRUE))
        return value
     */

    /*
    (* Initial call *)
        minimax(origin, depth, TRUE)
     */



    private Dimension2D getRandomFreeMove(ToeSide[][] currentBoard) {
        Random random = new Random(System.currentTimeMillis());

        int randomX = random.nextInt(currentBoard.length);
        int randomY = random.nextInt(currentBoard[0].length);

        while(currentBoard[randomX][randomY] != null) {
            randomX = random.nextInt(currentBoard.length);
            randomY = random.nextInt(currentBoard[0].length);
        }

        return new Dimension2D(randomX, randomY);
    }
}

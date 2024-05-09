package org.sample.checkers.ticktacktoe.ui.minimax;

import com.sun.javafx.geom.Dimension2D;
import org.sample.checkers.config.ticktacktoe.TickTackToeMove;
import org.sample.checkers.config.ticktacktoe.TickTackToeMoveHistory;
import org.sample.checkers.config.ticktacktoe.ToeSide;
import org.sample.checkers.ticktacktoe.ui.TickTackToeUi;
import org.sample.checkers.ticktacktoe.ui.minimax.techniques.AdvancedMove;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class AdvancedMinimaxUi implements TickTackToeUi {

    @Override
    public TickTackToeMove computeNextMove(TickTackToeMoveHistory history) {
        ToeSide[][] currentBoard = history.getCurrentBoardFromHistory();

        Dimension2D randomFreeMove = getRandomFreeMove(currentBoard);

        return new TickTackToeMove(randomFreeMove, history.getOnMove());
    }

    private TickTackToeMove minimax(ToeSide[][] currentBoard,
                                    int startDepth,
                                    int maxDepth,
                                    boolean maximize,
                                    boolean seeding) {

        // Recursion anchor -> Evaluate board TODO MONTE CARLO
        //if (currentDepth == 0 || game.isGameOver(-game.currentPlayer))
        //    return Storage.Record(null, null, game.evaluate(currentDepth), game.currentPlayer)

        // Check if board exists in storage TODO TRANSPOSITION TABLES
        //val storedMove = game.searchBestMoveInStorage()
        //if (storedMove != null) return storedMove

        //AdvancedMove possibleMoves = game.getPossibleMoves(true)
        return null;
    }

    /**
     * Get list of all possible moves
     *
     * @param shuffle shuffle list
     * @return list of possible moves
     */
    List<AdvancedMove> getPossibleMoves(Boolean shuffle, int height){
        List<AdvancedMove> possibleMoves = new ArrayList<>();
        Long top = 0b1000000_1000000_1000000_1000000_1000000_1000000_1000000L;

//        for (int col = 0; col<=6; col++) {
//            if(top && (1L << col) == 0L){
//                possibleMoves.add(new AdvancedMove(col,height));
//            }
//        }

        if(shuffle) {
            Collections.shuffle(possibleMoves);
        }

        return possibleMoves;
//
//            for (col in 0..6)
//            if (top and (1L shl this.heights[col]) == 0L)
//            possibleMoves.add(Move(col))
//
//            return if (shuffle) possibleMoves.shuffled() else possibleMoves.toList()

    }

    /*
     **
     * Minimax algorithm that finds best move
     *
     * @param [game] game to apply minimax on
     * @param [startingDepth] tree depth on start
     * @param [currentDepth] maximal tree depth (maximal number of moves to anticipate)
     * @param [maximize] maximize or minimize
     * @param [seeding] is algorithm used for storage seeding
     * @return storage record
     *
    fun minimax(
            game: Minimax<Board, Move> = this,
            startingDepth: Int = this.difficulty,
            currentDepth: Int = startingDepth,
            maximize: Boolean = game.currentPlayer == 1,
            seeding: Boolean = false
    ): Storage.Record<Move> {
        // Recursion anchor -> Evaluate board
        if (currentDepth == 0 || game.isGameOver(-game.currentPlayer))
            return Storage.Record(null, null, game.evaluate(currentDepth), game.currentPlayer)

        // Check if board exists in storage
        val storedMove = game.searchBestMoveInStorage()
        if (storedMove != null) return storedMove

        val possibleMoves = game.getPossibleMoves(true)


         * If there's a move which results in a win for the current player we immediately return this move.
         * This way we don't have to evaluate other possible moves -> Performance + 1 :)
         *
         * We add the currentDepth, otherwise the AI plays randomly if it will lose definitely.
         * This way the AI tries to "survive" as long as possible, even if it can't win anymore.

        possibleMoves.forEach { move ->
                val tmpGame = game.move(move)
            if (tmpGame.hasWinner(game.currentPlayer))
                return Storage.Record(
                        game.storageRecordPrimaryKey,
                        move,
                        game.currentPlayer * (1_000_000F + currentDepth),
                        game.currentPlayer
                )
        }

        // Call recursively from here on for each move to find best one
        var minOrMax: Pair<Move?, Float> = Pair(null, if (maximize) Float.NEGATIVE_INFINITY else Float.POSITIVE_INFINITY)

        for (move in possibleMoves) {
            val newGame = game.move(move)
            val moveScore = minimax(newGame, startingDepth, currentDepth - 1, !maximize, seeding).score

            // Check for maximum or minimum
            if ((maximize && moveScore > minOrMax.second) || (!maximize && moveScore < minOrMax.second))
                minOrMax = Pair(move, moveScore)
        }

        val finalMove = Storage.Record(game.storageRecordPrimaryKey, minOrMax.first, minOrMax.second, game.currentPlayer)

        // Add board evaluation temporary to storage
        // For data seeding we don't add the final move because it's added inside the seed method.
        if (game.storageIndex >= 0 && !(startingDepth == currentDepth && seeding))
            Storage.doStorageLookup<Move>(game.storageIndex).map[game.storageRecordPrimaryKey] = finalMove

        return finalMove
    }
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

package org.sample.checkers.ticktacktoe.ui.minimax.techniques;

import org.sample.checkers.config.ticktacktoe.ToeSide;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a connect-four game
 *
 * @param [currentPlayer] current player
 * @param [difficulty] AI strength
 * @param [storageIndex] Storage index for current board.
 * @param [storageRecordPrimaryKey] key under which the board will be saved to storage
 * @param [heights] current height of board columns
 * @param [history] history of boards
 * @param [moveDuration] duration of bestMove calculation
 */
public class BitboardGame extends BitboardsUtil {

    private ToeSide currentPlayer;
    private Integer difficulty;
    private Integer storageIndex;
    private Long storageRecordPrimaryKey;
    protected List<BigInteger[]> history;
    protected Long moveDuration;

    public BitboardGame(int height, int column, int winSize) {
        super(height, column, winSize);
        this.currentPlayer = ToeSide.CROSS;
        this.difficulty = 5;
        this.storageIndex = 0;
        this.storageRecordPrimaryKey = 0L;
        this.history = new ArrayList<>();
        this.moveDuration = 0L;
    }

    public BitboardGame(int height, int column, int winSize, BigInteger[] bitboards, ToeSide currentPlayer, int difficulty,
                        long storageRecordPrimaryKey, List<BigInteger[]> history, Long moveDuration) {
        super(height, column, winSize, bitboards);
        this.currentPlayer = currentPlayer;
        this.difficulty = difficulty;
        this.storageRecordPrimaryKey = storageRecordPrimaryKey;
        this.history = history;
        this.moveDuration = moveDuration;
    }

    public BitboardGame move(MinimaxMove move, Long duration) {
        if(!this.isValidMove(move)) {
            throw new RuntimeException("Invalid move!");
        }
        // Update current player's board
        makeMove(move.position);

        // Calculate new zobrist-hash
//        val cellPlacedAt = this.heights[move.column]
//        val newZobristHash = this.storageRecordPrimaryKey xor Minimax.Storage.getZobristHash(cellPlacedAt, this.currentPlayer)

        return new BitboardGame(height, column, winSize, bitboard, this.currentPlayer, this.difficulty, 0L, history, duration);
    }

    /**
     * Check if given move is valid
     *
     * @param [move] move to check
     * @return is move valid
     */
    private boolean isValidMove(MinimaxMove move){
        return listMoves().contains(move.position);
    }

    public boolean isGameOver(ToeSide player) {
        return hasWinner(player) || (counter == column * height);
    }

    public Long getStorageRecordPrimaryKey() {
        return storageRecordPrimaryKey;
    }

    public ToeSide getCurrentPlayer() {
        return currentPlayer;
    }

    public Integer getStorageIndex() {
        return storageIndex;
    }
}

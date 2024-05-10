package org.sample.checkers.ticktacktoe.ui.minimax;

import com.sun.javafx.geom.Dimension2D;
import org.sample.checkers.config.ticktacktoe.TickTackToeMove;
import org.sample.checkers.config.ticktacktoe.TickTackToeMoveHistory;
import org.sample.checkers.config.ticktacktoe.ToeSide;
import org.sample.checkers.ticktacktoe.ui.TickTackToeUi;
import org.sample.checkers.ticktacktoe.ui.minimax.techniques.BitboardsUtil;
import org.sample.checkers.ticktacktoe.ui.minimax.techniques.Record;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AdvancedMinimaxUi extends BitboardsUtil implements TickTackToeUi {

    private static final int DIFFICULTY = 5;

    public AdvancedMinimaxUi(int height, int column, int winSize) {
        super(height, column, winSize);
    }

    @Override
    public TickTackToeMove computeNextMove(TickTackToeMoveHistory history) {
        ToeSide[][] currentBoard = history.getCurrentBoardFromHistory();

        Dimension2D randomFreeMove = getRandomFreeMove(currentBoard);

        Record move = minimax(currentBoard, DIFFICULTY, ToeSide.CROSS);

        return new TickTackToeMove(randomFreeMove, history.getOnMove());
    }

    private Record minimax(ToeSide[][] currentBoard, int depth, ToeSide currentPlayer) {
        if (depth == 0 || isGameOver(currentPlayer))
            return new Record(null, null, evaluate(), currentPlayer);

        return null;
    }

    boolean isGameOver(ToeSide player) {
        return hasWinner(player) || (counter == column * height);
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

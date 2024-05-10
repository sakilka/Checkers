package org.sample.checkers.ticktacktoe.ui.minimax;

import com.sun.javafx.geom.Dimension2D;
import org.sample.checkers.config.ticktacktoe.TickTackToeMove;
import org.sample.checkers.config.ticktacktoe.TickTackToeMoveHistory;
import org.sample.checkers.config.ticktacktoe.ToeSide;
import org.sample.checkers.ticktacktoe.ui.TickTackToeUi;
import org.sample.checkers.ticktacktoe.ui.minimax.techniques.BitboardsUtil;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AdvancedMinimaxUi extends BitboardsUtil implements TickTackToeUi {

    public AdvancedMinimaxUi(int height, int column) {
        super(height, column);
    }

    @Override
    public TickTackToeMove computeNextMove(TickTackToeMoveHistory history) {
        ToeSide[][] currentBoard = history.getCurrentBoardFromHistory();

        Dimension2D randomFreeMove = getRandomFreeMove(currentBoard);

        return new TickTackToeMove(randomFreeMove, history.getOnMove());
    }

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

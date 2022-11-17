package org.sample.checkers.ticktacktoe.ui.impl;

import com.sun.javafx.geom.Dimension2D;
import org.sample.checkers.config.ticktacktoe.TickTackToeMove;
import org.sample.checkers.config.ticktacktoe.TickTackToeMoveHistory;
import org.sample.checkers.config.ticktacktoe.ToeSide;
import org.sample.checkers.ticktacktoe.ui.TickTackToeUi;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class TickTackToeUiImpl implements TickTackToeUi {

    private Random random = new SecureRandom();

    @Override
    public TickTackToeMove computeNextMove(TickTackToeMoveHistory history) {
        ToeSide [][] currentBoard = history.getCurrentBoardFromHistory();

        int x;
        int y;

        do{
            x = random.nextInt(currentBoard.length);
            y = random.nextInt(currentBoard[0].length);
        }
        while(currentBoard[x][y] != null);

        return new TickTackToeMove(new Dimension2D(y, x), history.getOnMove());
    }
}

package org.sample.checkers.ticktacktoe.ui.simple;

import com.sun.javafx.geom.Dimension2D;
import org.sample.checkers.config.ticktacktoe.TickTackToeMove;
import org.sample.checkers.config.ticktacktoe.TickTackToeMoveHistory;
import org.sample.checkers.config.ticktacktoe.ToeSide;
import org.sample.checkers.ticktacktoe.ui.Position;
import org.sample.checkers.ticktacktoe.ui.TickTackToeUi;
import org.sample.checkers.ticktacktoe.ui.heuristic.ToeHeuristic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class SimpleHeuristicUi implements TickTackToeUi {

    private ToeHeuristic heuristic;

    public SimpleHeuristicUi() {
        this.heuristic = new ToeHeuristic();
    }

    @Override
    public TickTackToeMove computeNextMove(TickTackToeMoveHistory history) {
        ToeSide[][] currentBoard = history.getCurrentBoardFromHistory();
        HashMap<Position, Integer> scoreMap = evaluateAllPossibleTurns(currentBoard, history.getOnMove());
        return findBestTurn(scoreMap);
    }

    private HashMap<Position, Integer> evaluateAllPossibleTurns(ToeSide[][] currentBoard, ToeSide side) {
        HashMap<Position, Integer> evaluation = new HashMap<>();
        for (int height = 0; height < currentBoard.length; height++) {
            for (int width = 0; width < currentBoard[0].length; width++) {
                if (currentBoard[height][width] == null) {
                    ToeSide[][] copy = copyArray(currentBoard);
                    copy[height][width] = side;
                    int score = heuristic.evaluateBoardState(copy, side);
                    evaluation.put(new Position(width, height, side), score);
                }
            }
        }
        return evaluation;
    }

    private TickTackToeMove findBestTurn(HashMap<Position, Integer> scoreMap) {
        int bestScore = scoreMap.entrySet().iterator().next().getValue();

        for (Map.Entry<Position, Integer> entry : scoreMap.entrySet()) {
            if (entry.getValue() >= bestScore) {
                bestScore = entry.getValue();
            }
        }

        ArrayList<Position> bestScores = new ArrayList<>();
        for (Map.Entry<Position, Integer> entry : scoreMap.entrySet()) {
            if (entry.getValue() == bestScore) {
                bestScores.add(entry.getKey());
            }
        }

        Collections.shuffle(bestScores);

        return new TickTackToeMove(new Dimension2D(bestScores.get(0).getWidth(), bestScores.get(0).getHeight()),
                bestScores.get(0).getTurn());
    }

    private ToeSide[][] copyArray(ToeSide[][] array) {
        ToeSide[][] copy = new ToeSide[array.length][array[0].length];
        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[0].length; y++) {
                copy[x][y] = array[x][y];
            }
        }
        return copy;
    }
}

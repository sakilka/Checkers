package org.sample.checkers.config.checkers;

import com.sun.javafx.geom.Dimension2D;

import java.util.List;

import static org.sample.checkers.config.checkers.CheckersFigure.*;
import static org.sample.checkers.config.checkers.CheckersSide.BLACK;
import static org.sample.checkers.config.checkers.CheckersSide.WHITE;

public class CheckersMoveHistory {
        private List<CheckersMovePosition> moves;

        private boolean castlingWhiteDone;
        private boolean castlingBlackDone;
        private CheckersSide onMove;

        public CheckersMoveHistory(List<CheckersMovePosition> moves) {
            this.moves = moves;
            this.castlingWhiteDone = false;
            this.onMove = WHITE;
        }

        public List<CheckersMovePosition> getMoves() {
            return moves;
        }

        public void setMoves(List<CheckersMovePosition> moves) {
            this.moves = moves;
        }

        public void addMove(CheckersMovePosition move) {
            move.setFirstMove(isFirstMove(move));
            moves.add(move);
            onMove = onMove == WHITE ? BLACK : WHITE;
        }

        public boolean isCastlingWhiteDone() {
            return castlingWhiteDone;
        }

        public void setCastlingWhiteDone(boolean castlingWhiteDone) {
            this.castlingWhiteDone = castlingWhiteDone;
        }

        public boolean isCastlingBlackDone() {
            return castlingBlackDone;
        }

        public void setCastlingBlackDone(boolean castlingBlackDone) {
            this.castlingBlackDone = castlingBlackDone;
        }

        public CheckersSide getOnMove() {
            return onMove;
        }

        public void setOnMove(CheckersSide whiteMove) {
            this.onMove = whiteMove;
        }

        private boolean isFirstMove(CheckersMovePosition move) {
            return moves.stream().anyMatch(previousMove ->
                    move.getPreviousPosition().width == previousMove.getPosition().width &&
                            move.getPreviousPosition().height == previousMove.getPosition().height);
        }

        public CheckersBoardPositions getCurrentBoardFromHistory() {
            CheckersFigure[][] initialFigures = new CheckersFigure[][]{
                    { PAWN, null, null, null, null, null, PAWN, null },
                    { null, PAWN, null, null, null, null, null, PAWN },
                    { PAWN, null, null, null, null, null, PAWN, null },
                    { null, PAWN, null, null, null, null, null, PAWN },
                    { PAWN, null, null, null, null, null, PAWN, null },
                    { null, PAWN, null, null, null, null, null, PAWN },
                    { PAWN, null, null, null, null, null, PAWN, null },
                    { null, PAWN, null, null, null, null, null, PAWN }
            };

            CheckersSide[][] initialSides = new CheckersSide[][]{
                    { WHITE, null, null, null, null, null, BLACK, null },
                    { null, WHITE, null, null, null, null, null, BLACK },
                    { WHITE, null, null, null, null, null, BLACK, null },
                    { null, WHITE, null, null, null, null, null, BLACK },
                    { WHITE, null, null, null, null, null, BLACK, null },
                    { null, WHITE, null, null, null, null, null, BLACK },
                    { WHITE, null, null, null, null, null, BLACK, null },
                    { null, WHITE, null, null, null, null, null, BLACK }
            };

            for (CheckersMovePosition move : moves) {
                Dimension2D from = move.getPreviousPosition();
                Dimension2D to = move.getPosition();

                CheckersFigure figure = initialFigures[(int) from.width-1][(int) from.height-1];
                initialFigures[(int) from.width-1][(int) from.height-1] = null;
                initialFigures[(int) to.width-1][(int) to.height-1] = figure;

                CheckersSide side = initialSides[(int) from.width-1][(int) from.height-1];
                initialSides[(int) from.width-1][(int) from.height-1] = null;
                initialSides[(int) to.width-1][(int) to.height-1] = side;
            }

            return new CheckersBoardPositions(initialFigures, initialSides);
        }

        public CheckersBoardPositions getCurrentBoardFromHistoryAndWithMove(CheckersMovePosition move) {
            CheckersBoardPositions checkersBoardPositions = getCurrentBoardFromHistory();
            CheckersFigure[][] figures = checkersBoardPositions.getPositions();
            CheckersSide[][] sides = checkersBoardPositions.getSides();

            Dimension2D from = move.getPreviousPosition();
            Dimension2D to = move.getPosition();

            CheckersFigure figure = figures[(int) from.width-1][(int) from.height-1];
            figures[(int) from.width-1][(int) from.height-1] = null;
            figures[(int) to.width-1][(int) to.height-1] = figure;

            CheckersSide side = sides[(int) from.width-1][(int) from.height-1];
            sides[(int) from.width-1][(int) from.height-1] = null;
            sides[(int) to.width-1][(int) to.height-1] = side;

            return new CheckersBoardPositions(figures, sides);
        }
}

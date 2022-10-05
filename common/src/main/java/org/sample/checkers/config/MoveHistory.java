package org.sample.checkers.config;

import com.sun.javafx.geom.Dimension2D;

import java.util.List;

import static org.sample.checkers.config.ChessFigure.*;
import static org.sample.checkers.config.ChessSide.BLACK;
import static org.sample.checkers.config.ChessSide.WHITE;

public class MoveHistory {

    private List<ChessMove> moves;

    private boolean castlingWhiteDone;
    private boolean castlingBlackDone;
    private ChessSide onMove;

    public MoveHistory(List<ChessMove> moves) {
        this.moves = moves;
        this.castlingWhiteDone = false;
        this.onMove = WHITE;
    }

    public List<ChessMove> getMoves() {
        return moves;
    }

    public void setMoves(List<ChessMove> moves) {
        this.moves = moves;
    }

    public void addMove(ChessMove move) {
        move.setFirstMove(isFirstMove(move));
        moves.add(move);
        onMove = onMove == WHITE ? ChessSide.BLACK : WHITE;
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

    public ChessSide getOnMove() {
        return onMove;
    }

    public void setOnMove(ChessSide whiteMove) {
        this.onMove = whiteMove;
    }

    private boolean isFirstMove(ChessMove move) {
        return moves.stream().anyMatch(previousMove ->
                move.getPreviousPosition().width == previousMove.getPosition().width &&
                        move.getPreviousPosition().height == previousMove.getPosition().height);
    }

    public ChessBoardPositions getCurrentBoardFromHistory() {
        ChessFigure[][] initialFigures = new ChessFigure[][]{
                { ROOK, PAWN, null, null, null, null, PAWN, ROOK },
                { KNIGHT, PAWN, null, null, null, null, PAWN, KNIGHT },
                { BISHOP, PAWN, null, null, null, null, PAWN, BISHOP },
                { QUEEN, PAWN, null, null, null, null, PAWN, QUEEN },
                { KING, PAWN, null, null, null, null, PAWN, KING },
                { BISHOP, PAWN, null, null, null, null, PAWN, BISHOP },
                { KNIGHT, PAWN, null, null, null, null, PAWN, KNIGHT },
                { ROOK, PAWN, null, null, null, null, PAWN, ROOK }
        };

        ChessSide[][] initialSides = new ChessSide[][]{
                { WHITE, WHITE, null, null, null, null, BLACK, BLACK },
                { WHITE, WHITE, null, null, null, null, BLACK, BLACK },
                { WHITE, WHITE, null, null, null, null, BLACK, BLACK },
                { WHITE, WHITE, null, null, null, null, BLACK, BLACK },
                { WHITE, WHITE, null, null, null, null, BLACK, BLACK },
                { WHITE, WHITE, null, null, null, null, BLACK, BLACK },
                { WHITE, WHITE, null, null, null, null, BLACK, BLACK },
                { WHITE, WHITE, null, null, null, null, BLACK, BLACK }
        };

        for (ChessMove move : moves) {
            Dimension2D from = move.getPreviousPosition();
            Dimension2D to = move.getPosition();

            ChessFigure figure = initialFigures[(int) from.width-1][(int) from.height-1];
            initialFigures[(int) from.width-1][(int) from.height-1] = null;
            initialFigures[(int) to.width-1][(int) to.height-1] = figure;

            ChessSide side = initialSides[(int) from.width-1][(int) from.height-1];
            initialSides[(int) from.width-1][(int) from.height-1] = null;
            initialSides[(int) to.width-1][(int) to.height-1] = side;
        }

        return new ChessBoardPositions(initialFigures, initialSides);
    }

    public ChessBoardPositions getCurrentBoardFromHistoryAndWithMove(ChessMove move) {
        ChessBoardPositions chessBoardPositions = getCurrentBoardFromHistory();
        ChessFigure[][] figures = chessBoardPositions.getPositions();
        ChessSide[][] sides = chessBoardPositions.getSides();

        Dimension2D from = move.getPreviousPosition();
        Dimension2D to = move.getPosition();

        ChessFigure figure = figures[(int) from.width-1][(int) from.height-1];
        figures[(int) from.width-1][(int) from.height-1] = null;
        figures[(int) to.width-1][(int) to.height-1] = figure;

        ChessSide side = sides[(int) from.width-1][(int) from.height-1];
        sides[(int) from.width-1][(int) from.height-1] = null;
        sides[(int) to.width-1][(int) to.height-1] = side;

        return new ChessBoardPositions(figures, sides);
    }
}

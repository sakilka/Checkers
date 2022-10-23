package org.sample.checkers.config.chess;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class ChessFiguresPositions {

    @Value("${chess.white.knight.first.x}")
    private int chessWhiteKnightFirstX;

    @Value("${chess.white.knight.first.y}")
    private int chessWhiteKnightFirstY;

    @Value("${chess.white.knight.second.x}")
    private int chessWhiteKnightSecondX;

    @Value("${chess.white.knight.second.y}")
    private int chessWhiteKnightSecondY;

    @Value("${chess.white.king.x}")
    private int chessWhiteKingX;

    @Value("${chess.white.king.y}")
    private int chessWhiteKingY;

    @Value("${chess.white.queen.x}")
    private int chessWhiteQueenX;

    @Value("${chess.white.queen.y}")
    private int chessWhiteQueenY;

    @Value("${chess.white.bishop.first.x}")
    private int chessWhiteBishopFirstX;

    @Value("${chess.white.bishop.first.y}")
    private int chessWhiteBishopFirstY;

    @Value("${chess.white.bishop.second.x}")
    private int chessWhiteBishopSecondX;

    @Value("${chess.white.bishop.second.y}")
    private int chessWhiteBishopSecondY;

    @Value("${chess.white.rook.first.x}")
    private int chessWhiteRookFirstX;

    @Value("${chess.white.rook.first.y}")
    private int chessWhiteRookFirstY;

    @Value("${chess.white.rook.second.x}")
    private int chessWhiteRookSecondX;

    @Value("${chess.white.rook.second.y}")
    private int chessWhiteRookSecondY;

    @Value("${chess.white.pawn.first.x}")
    private int chessWhitePawnFirstX;

    @Value("${chess.white.pawn.first.y}")
    private int chessWhitePawnFirstY;

    @Value("${chess.white.pawn.second.x}")
    private int chessWhitePawnSecondX;

    @Value("${chess.white.pawn.second.y}")
    private int chessWhitePawnSecondY;

    @Value("${chess.white.pawn.third.x}")
    private int chessWhitePawnThirdX;

    @Value("${chess.white.pawn.third.y}")
    private int chessWhitePawnThirdY;

    @Value("${chess.white.pawn.fourth.x}")
    private int chessWhitePawnFourthX;

    @Value("${chess.white.pawn.fourth.y}")
    private int chessWhitePawnFourthY;

    @Value("${chess.white.pawn.fifth.x}")
    private int chessWhitePawnFifthX;

    @Value("${chess.white.pawn.fifth.y}")
    private int chessWhitePawnFifthY;

    @Value("${chess.white.pawn.sixth.x}")
    private int chessWhitePawnSixthX;

    @Value("${chess.white.pawn.sixth.y}")
    private int chessWhitePawnSixthY;

    @Value("${chess.white.pawn.seventh.x}")
    private int chessWhitePawnSeventhX;

    @Value("${chess.white.pawn.seventh.y}")
    private int chessWhitePawnSeventhY;

    @Value("${chess.white.pawn.eighth.x}")
    private int chessWhitePawnEighthX;

    @Value("${chess.white.pawn.eighth.y}")
    private int chessWhitePawnEighthY;

    @Value("${chess.black.knight.first.x}")
    private int chessBlackKnightFirstX;

    @Value("${chess.black.knight.first.y}")
    private int chessBlackKnightFirstY;

    @Value("${chess.black.knight.second.x}")
    private int chessBlackKnightSecondX;

    @Value("${chess.black.knight.second.y}")
    private int chessBlackKnightSecondY;

    @Value("${chess.black.king.x}")
    private int chessBlackKingX;

    @Value("${chess.black.king.y}")
    private int chessBlackKingY;

    @Value("${chess.black.queen.x}")
    private int chessBlackQueenX;

    @Value("${chess.black.queen.y}")
    private int chessBlackQueenY;

    @Value("${chess.black.bishop.first.x}")
    private int chessBlackBishopFirstX;

    @Value("${chess.black.bishop.first.y}")
    private int chessBlackBishopFirstY;

    @Value("${chess.black.bishop.second.x}")
    private int chessBlackBishopSecondX;

    @Value("${chess.black.bishop.second.y}")
    private int chessBlackBishopSecondY;

    @Value("${chess.black.rook.first.x}")
    private int chessBlackRookFirstX;

    @Value("${chess.black.rook.first.y}")
    private int chessBlackRookFirstY;

    @Value("${chess.black.rook.second.x}")
    private int chessBlackRookSecondX;

    @Value("${chess.black.rook.second.y}")
    private int chessBlackRookSecondY;

    @Value("${chess.black.pawn.first.x}")
    private int chessBlackPawnFirstX;

    @Value("${chess.black.pawn.first.y}")
    private int chessBlackPawnFirstY;

    @Value("${chess.black.pawn.second.x}")
    private int chessBlackPawnSecondX;

    @Value("${chess.black.pawn.second.y}")
    private int chessBlackPawnSecondY;

    @Value("${chess.black.pawn.third.x}")
    private int chessBlackPawnThirdX;

    @Value("${chess.black.pawn.third.y}")
    private int chessBlackPawnThirdY;

    @Value("${chess.black.pawn.fourth.x}")
    private int chessBlackPawnFourthX;

    @Value("${chess.black.pawn.fourth.y}")
    private int chessBlackPawnFourthY;

    @Value("${chess.black.pawn.fifth.x}")
    private int chessBlackPawnFifthX;

    @Value("${chess.black.pawn.fifth.y}")
    private int chessBlackPawnFifthY;

    @Value("${chess.black.pawn.sixth.x}")
    private int chessBlackPawnSixthX;

    @Value("${chess.black.pawn.sixth.y}")
    private int chessBlackPawnSixthY;

    @Value("${chess.black.pawn.seventh.x}")
    private int chessBlackPawnSeventhX;

    @Value("${chess.black.pawn.seventh.y}")
    private int chessBlackPawnSeventhY;

    @Value("${chess.black.pawn.eighth.x}")
    private int chessBlackPawnEighthX;

    @Value("${chess.black.pawn.eighth.y}")
    private int chessBlackPawnEighthY;

    public static float getAbsolutePositionX(int positionX, float fieldWidth) {
        return (fieldWidth * (positionX-1)) - (fieldWidth * 4) + (fieldWidth/2);
    }

    public static float getAbsolutePositionY(int positionY, float fieldWidth) {
        return (fieldWidth * (positionY-1)) - (fieldWidth * 4) + (fieldWidth/2);
    }

    public int getChessWhiteKnightFirstX() {
        return chessWhiteKnightFirstX;
    }

    public void setChessWhiteKnightFirstX(int chessWhiteKnightFirstX) {
        this.chessWhiteKnightFirstX = chessWhiteKnightFirstX;
    }

    public int getChessWhiteKnightFirstY() {
        return chessWhiteKnightFirstY;
    }

    public void setChessWhiteKnightFirstY(int chessWhiteKnightFirstY) {
        this.chessWhiteKnightFirstY = chessWhiteKnightFirstY;
    }

    public int getChessWhiteKnightSecondX() {
        return chessWhiteKnightSecondX;
    }

    public void setChessWhiteKnightSecondX(int chessWhiteKnightSecondX) {
        this.chessWhiteKnightSecondX = chessWhiteKnightSecondX;
    }

    public int getChessWhiteKnightSecondY() {
        return chessWhiteKnightSecondY;
    }

    public void setChessWhiteKnightSecondY(int chessWhiteKnightSecondY) {
        this.chessWhiteKnightSecondY = chessWhiteKnightSecondY;
    }

    public int getChessWhiteKingX() {
        return chessWhiteKingX;
    }

    public void setChessWhiteKingX(int chessWhiteKingX) {
        this.chessWhiteKingX = chessWhiteKingX;
    }

    public int getChessWhiteKingY() {
        return chessWhiteKingY;
    }

    public void setChessWhiteKingY(int chessWhiteKingY) {
        this.chessWhiteKingY = chessWhiteKingY;
    }

    public int getChessWhiteQueenX() {
        return chessWhiteQueenX;
    }

    public void setChessWhiteQueenX(int chessWhiteQueenX) {
        this.chessWhiteQueenX = chessWhiteQueenX;
    }

    public int getChessWhiteQueenY() {
        return chessWhiteQueenY;
    }

    public void setChessWhiteQueenY(int chessWhiteQueenY) {
        this.chessWhiteQueenY = chessWhiteQueenY;
    }

    public int getChessWhiteBishopFirstX() {
        return chessWhiteBishopFirstX;
    }

    public void setChessWhiteBishopFirstX(int chessWhiteBishopFirstX) {
        this.chessWhiteBishopFirstX = chessWhiteBishopFirstX;
    }

    public int getChessWhiteBishopFirstY() {
        return chessWhiteBishopFirstY;
    }

    public void setChessWhiteBishopFirstY(int chessWhiteBishopFirstY) {
        this.chessWhiteBishopFirstY = chessWhiteBishopFirstY;
    }

    public int getChessWhiteBishopSecondX() {
        return chessWhiteBishopSecondX;
    }

    public void setChessWhiteBishopSecondX(int chessWhiteBishopSecondX) {
        this.chessWhiteBishopSecondX = chessWhiteBishopSecondX;
    }

    public int getChessWhiteBishopSecondY() {
        return chessWhiteBishopSecondY;
    }

    public void setChessWhiteBishopSecondY(int chessWhiteBishopSecondY) {
        this.chessWhiteBishopSecondY = chessWhiteBishopSecondY;
    }

    public int getChessWhiteRookFirstX() {
        return chessWhiteRookFirstX;
    }

    public void setChessWhiteRookFirstX(int chessWhiteRookFirstX) {
        this.chessWhiteRookFirstX = chessWhiteRookFirstX;
    }

    public int getChessWhiteRookFirstY() {
        return chessWhiteRookFirstY;
    }

    public void setChessWhiteRookFirstY(int chessWhiteRookFirstY) {
        this.chessWhiteRookFirstY = chessWhiteRookFirstY;
    }

    public int getChessWhiteRookSecondX() {
        return chessWhiteRookSecondX;
    }

    public void setChessWhiteRookSecondX(int chessWhiteRookSecondX) {
        this.chessWhiteRookSecondX = chessWhiteRookSecondX;
    }

    public int getChessWhiteRookSecondY() {
        return chessWhiteRookSecondY;
    }

    public void setChessWhiteRookSecondY(int chessWhiteRookSecondY) {
        this.chessWhiteRookSecondY = chessWhiteRookSecondY;
    }

    public int getChessWhitePawnFirstX() {
        return chessWhitePawnFirstX;
    }

    public void setChessWhitePawnFirstX(int chessWhitePawnFirstX) {
        this.chessWhitePawnFirstX = chessWhitePawnFirstX;
    }

    public int getChessWhitePawnFirstY() {
        return chessWhitePawnFirstY;
    }

    public void setChessWhitePawnFirstY(int chessWhitePawnFirstY) {
        this.chessWhitePawnFirstY = chessWhitePawnFirstY;
    }

    public int getChessWhitePawnSecondX() {
        return chessWhitePawnSecondX;
    }

    public void setChessWhitePawnSecondX(int chessWhitePawnSecondX) {
        this.chessWhitePawnSecondX = chessWhitePawnSecondX;
    }

    public int getChessWhitePawnSecondY() {
        return chessWhitePawnSecondY;
    }

    public void setChessWhitePawnSecondY(int chessWhitePawnSecondY) {
        this.chessWhitePawnSecondY = chessWhitePawnSecondY;
    }

    public int getChessWhitePawnThirdX() {
        return chessWhitePawnThirdX;
    }

    public void setChessWhitePawnThirdX(int chessWhitePawnThirdX) {
        this.chessWhitePawnThirdX = chessWhitePawnThirdX;
    }

    public int getChessWhitePawnThirdY() {
        return chessWhitePawnThirdY;
    }

    public void setChessWhitePawnThirdY(int chessWhitePawnThirdY) {
        this.chessWhitePawnThirdY = chessWhitePawnThirdY;
    }

    public int getChessWhitePawnFourthX() {
        return chessWhitePawnFourthX;
    }

    public void setChessWhitePawnFourthX(int chessWhitePawnFourthX) {
        this.chessWhitePawnFourthX = chessWhitePawnFourthX;
    }

    public int getChessWhitePawnFourthY() {
        return chessWhitePawnFourthY;
    }

    public void setChessWhitePawnFourthY(int chessWhitePawnFourthY) {
        this.chessWhitePawnFourthY = chessWhitePawnFourthY;
    }

    public int getChessWhitePawnFifthX() {
        return chessWhitePawnFifthX;
    }

    public void setChessWhitePawnFifthX(int chessWhitePawnFifthX) {
        this.chessWhitePawnFifthX = chessWhitePawnFifthX;
    }

    public int getChessWhitePawnFifthY() {
        return chessWhitePawnFifthY;
    }

    public void setChessWhitePawnFifthY(int chessWhitePawnFifthY) {
        this.chessWhitePawnFifthY = chessWhitePawnFifthY;
    }

    public int getChessWhitePawnSixthX() {
        return chessWhitePawnSixthX;
    }

    public void setChessWhitePawnSixthX(int chessWhitePawnSixthX) {
        this.chessWhitePawnSixthX = chessWhitePawnSixthX;
    }

    public int getChessWhitePawnSixthY() {
        return chessWhitePawnSixthY;
    }

    public void setChessWhitePawnSixthY(int chessWhitePawnSixthY) {
        this.chessWhitePawnSixthY = chessWhitePawnSixthY;
    }

    public int getChessWhitePawnSeventhX() {
        return chessWhitePawnSeventhX;
    }

    public void setChessWhitePawnSeventhX(int chessWhitePawnSeventhX) {
        this.chessWhitePawnSeventhX = chessWhitePawnSeventhX;
    }

    public int getChessWhitePawnSeventhY() {
        return chessWhitePawnSeventhY;
    }

    public void setChessWhitePawnSeventhY(int chessWhitePawnSeventhY) {
        this.chessWhitePawnSeventhY = chessWhitePawnSeventhY;
    }

    public int getChessWhitePawnEighthX() {
        return chessWhitePawnEighthX;
    }

    public void setChessWhitePawnEighthX(int chessWhitePawnEighthX) {
        this.chessWhitePawnEighthX = chessWhitePawnEighthX;
    }

    public int getChessWhitePawnEighthY() {
        return chessWhitePawnEighthY;
    }

    public void setChessWhitePawnEighthY(int chessWhitePawnEighthY) {
        this.chessWhitePawnEighthY = chessWhitePawnEighthY;
    }

    public int getChessBlackKnightFirstX() {
        return chessBlackKnightFirstX;
    }

    public void setChessBlackKnightFirstX(int chessBlackKnightFirstX) {
        this.chessBlackKnightFirstX = chessBlackKnightFirstX;
    }

    public int getChessBlackKnightFirstY() {
        return chessBlackKnightFirstY;
    }

    public void setChessBlackKnightFirstY(int chessBlackKnightFirstY) {
        this.chessBlackKnightFirstY = chessBlackKnightFirstY;
    }

    public int getChessBlackKnightSecondX() {
        return chessBlackKnightSecondX;
    }

    public void setChessBlackKnightSecondX(int chessBlackKnightSecondX) {
        this.chessBlackKnightSecondX = chessBlackKnightSecondX;
    }

    public int getChessBlackKnightSecondY() {
        return chessBlackKnightSecondY;
    }

    public void setChessBlackKnightSecondY(int chessBlackKnightSecondY) {
        this.chessBlackKnightSecondY = chessBlackKnightSecondY;
    }

    public int getChessBlackKingX() {
        return chessBlackKingX;
    }

    public void setChessBlackKingX(int chessBlackKingX) {
        this.chessBlackKingX = chessBlackKingX;
    }

    public int getChessBlackKingY() {
        return chessBlackKingY;
    }

    public void setChessBlackKingY(int chessBlackKingY) {
        this.chessBlackKingY = chessBlackKingY;
    }

    public int getChessBlackQueenX() {
        return chessBlackQueenX;
    }

    public void setChessBlackQueenX(int chessBlackQueenX) {
        this.chessBlackQueenX = chessBlackQueenX;
    }

    public int getChessBlackQueenY() {
        return chessBlackQueenY;
    }

    public void setChessBlackQueenY(int chessBlackQueenY) {
        this.chessBlackQueenY = chessBlackQueenY;
    }

    public int getChessBlackBishopFirstX() {
        return chessBlackBishopFirstX;
    }

    public void setChessBlackBishopFirstX(int chessBlackBishopFirstX) {
        this.chessBlackBishopFirstX = chessBlackBishopFirstX;
    }

    public int getChessBlackBishopFirstY() {
        return chessBlackBishopFirstY;
    }

    public void setChessBlackBishopFirstY(int chessBlackBishopFirstY) {
        this.chessBlackBishopFirstY = chessBlackBishopFirstY;
    }

    public int getChessBlackBishopSecondX() {
        return chessBlackBishopSecondX;
    }

    public void setChessBlackBishopSecondX(int chessBlackBishopSecondX) {
        this.chessBlackBishopSecondX = chessBlackBishopSecondX;
    }

    public int getChessBlackBishopSecondY() {
        return chessBlackBishopSecondY;
    }

    public void setChessBlackBishopSecondY(int chessBlackBishopSecondY) {
        this.chessBlackBishopSecondY = chessBlackBishopSecondY;
    }

    public int getChessBlackRookFirstX() {
        return chessBlackRookFirstX;
    }

    public void setChessBlackRookFirstX(int chessBlackRookFirstX) {
        this.chessBlackRookFirstX = chessBlackRookFirstX;
    }

    public int getChessBlackRookFirstY() {
        return chessBlackRookFirstY;
    }

    public void setChessBlackRookFirstY(int chessBlackRookFirstY) {
        this.chessBlackRookFirstY = chessBlackRookFirstY;
    }

    public int getChessBlackRookSecondX() {
        return chessBlackRookSecondX;
    }

    public void setChessBlackRookSecondX(int chessBlackRookSecondX) {
        this.chessBlackRookSecondX = chessBlackRookSecondX;
    }

    public int getChessBlackRookSecondY() {
        return chessBlackRookSecondY;
    }

    public void setChessBlackRookSecondY(int chessBlackRookSecondY) {
        this.chessBlackRookSecondY = chessBlackRookSecondY;
    }

    public int getChessBlackPawnFirstX() {
        return chessBlackPawnFirstX;
    }

    public void setChessBlackPawnFirstX(int chessBlackPawnFirstX) {
        this.chessBlackPawnFirstX = chessBlackPawnFirstX;
    }

    public int getChessBlackPawnFirstY() {
        return chessBlackPawnFirstY;
    }

    public void setChessBlackPawnFirstY(int chessBlackPawnFirstY) {
        this.chessBlackPawnFirstY = chessBlackPawnFirstY;
    }

    public int getChessBlackPawnSecondX() {
        return chessBlackPawnSecondX;
    }

    public void setChessBlackPawnSecondX(int chessBlackPawnSecondX) {
        this.chessBlackPawnSecondX = chessBlackPawnSecondX;
    }

    public int getChessBlackPawnSecondY() {
        return chessBlackPawnSecondY;
    }

    public void setChessBlackPawnSecondY(int chessBlackPawnSecondY) {
        this.chessBlackPawnSecondY = chessBlackPawnSecondY;
    }

    public int getChessBlackPawnThirdX() {
        return chessBlackPawnThirdX;
    }

    public void setChessBlackPawnThirdX(int chessBlackPawnThirdX) {
        this.chessBlackPawnThirdX = chessBlackPawnThirdX;
    }

    public int getChessBlackPawnThirdY() {
        return chessBlackPawnThirdY;
    }

    public void setChessBlackPawnThirdY(int chessBlackPawnThirdY) {
        this.chessBlackPawnThirdY = chessBlackPawnThirdY;
    }

    public int getChessBlackPawnFourthX() {
        return chessBlackPawnFourthX;
    }

    public void setChessBlackPawnFourthX(int chessBlackPawnFourthX) {
        this.chessBlackPawnFourthX = chessBlackPawnFourthX;
    }

    public int getChessBlackPawnFourthY() {
        return chessBlackPawnFourthY;
    }

    public void setChessBlackPawnFourthY(int chessBlackPawnFourthY) {
        this.chessBlackPawnFourthY = chessBlackPawnFourthY;
    }

    public int getChessBlackPawnFifthX() {
        return chessBlackPawnFifthX;
    }

    public void setChessBlackPawnFifthX(int chessBlackPawnFifthX) {
        this.chessBlackPawnFifthX = chessBlackPawnFifthX;
    }

    public int getChessBlackPawnFifthY() {
        return chessBlackPawnFifthY;
    }

    public void setChessBlackPawnFifthY(int chessBlackPawnFifthY) {
        this.chessBlackPawnFifthY = chessBlackPawnFifthY;
    }

    public int getChessBlackPawnSixthX() {
        return chessBlackPawnSixthX;
    }

    public void setChessBlackPawnSixthX(int chessBlackPawnSixthX) {
        this.chessBlackPawnSixthX = chessBlackPawnSixthX;
    }

    public int getChessBlackPawnSixthY() {
        return chessBlackPawnSixthY;
    }

    public void setChessBlackPawnSixthY(int chessBlackPawnSixthY) {
        this.chessBlackPawnSixthY = chessBlackPawnSixthY;
    }

    public int getChessBlackPawnSeventhX() {
        return chessBlackPawnSeventhX;
    }

    public void setChessBlackPawnSeventhX(int chessBlackPawnSeventhX) {
        this.chessBlackPawnSeventhX = chessBlackPawnSeventhX;
    }

    public int getChessBlackPawnSeventhY() {
        return chessBlackPawnSeventhY;
    }

    public void setChessBlackPawnSeventhY(int chessBlackPawnSeventhY) {
        this.chessBlackPawnSeventhY = chessBlackPawnSeventhY;
    }

    public int getChessBlackPawnEighthX() {
        return chessBlackPawnEighthX;
    }

    public void setChessBlackPawnEighthX(int chessBlackPawnEighthX) {
        this.chessBlackPawnEighthX = chessBlackPawnEighthX;
    }

    public int getChessBlackPawnEighthY() {
        return chessBlackPawnEighthY;
    }

    public void setChessBlackPawnEighthY(int chessBlackPawnEighthY) {
        this.chessBlackPawnEighthY = chessBlackPawnEighthY;
    }
}

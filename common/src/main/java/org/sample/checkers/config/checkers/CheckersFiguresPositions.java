package org.sample.checkers.config.checkers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class CheckersFiguresPositions {

    @Value("${checkers.white.pawn.first.x}")
    private int checkersWhitePawnFirstX;

    @Value("${checkers.white.pawn.first.y}")
    private int checkersWhitePawnFirstY;

    @Value("${checkers.white.pawn.second.x}")
    private int checkersWhitePawnSecondX;

    @Value("${checkers.white.pawn.second.y}")
    private int checkersWhitePawnSecondY;

    @Value("${checkers.white.pawn.third.x}")
    private int checkersWhitePawnThirdX;

    @Value("${checkers.white.pawn.third.y}")
    private int checkersWhitePawnThirdY;

    @Value("${checkers.white.pawn.fourth.x}")
    private int checkersWhitePawnFourthX;

    @Value("${checkers.white.pawn.fourth.y}")
    private int checkersWhitePawnFourthY;

    @Value("${checkers.white.pawn.fifth.x}")
    private int checkersWhitePawnFifthX;

    @Value("${checkers.white.pawn.fifth.y}")
    private int checkersWhitePawnFifthY;

    @Value("${checkers.white.pawn.sixth.x}")
    private int checkersWhitePawnSixthX;

    @Value("${checkers.white.pawn.sixth.y}")
    private int checkersWhitePawnSixthY;

    @Value("${checkers.white.pawn.seventh.x}")
    private int checkersWhitePawnSeventhX;

    @Value("${checkers.white.pawn.seventh.y}")
    private int checkersWhitePawnSeventhY;

    @Value("${checkers.white.pawn.eight.x}")
    private int checkersWhitePawnEightX;

    @Value("${checkers.white.pawn.eight.y}")
    private int checkersWhitePawnEightY;

    @Value("${checkers.black.pawn.first.x}")
    private int checkersBlackPawnFirstX;

    @Value("${checkers.black.pawn.first.y}")
    private int checkersBlackPawnFirstY;

    @Value("${checkers.black.pawn.second.x}")
    private int checkersBlackPawnSecondX;

    @Value("${checkers.black.pawn.second.y}")
    private int checkersBlackPawnSecondY;

    @Value("${checkers.black.pawn.third.x}")
    private int checkersBlackPawnThirdX;

    @Value("${checkers.black.pawn.third.y}")
    private int checkersBlackPawnThirdY;

    @Value("${checkers.black.pawn.fourth.x}")
    private int checkersBlackPawnFourthX;

    @Value("${checkers.black.pawn.fourth.y}")
    private int checkersBlackPawnFourthY;

    @Value("${checkers.black.pawn.fifth.x}")
    private int checkersBlackPawnFifthX;

    @Value("${checkers.black.pawn.fifth.y}")
    private int checkersBlackPawnFifthY;

    @Value("${checkers.black.pawn.sixth.x}")
    private int checkersBlackPawnSixthX;

    @Value("${checkers.black.pawn.sixth.y}")
    private int checkersBlackPawnSixthY;

    @Value("${checkers.black.pawn.seventh.x}")
    private int checkersBlackPawnSeventhX;

    @Value("${checkers.black.pawn.seventh.y}")
    private int checkersBlackPawnSeventhY;

    @Value("${checkers.black.pawn.eight.x}")
    private int checkersBlackPawnEightX;

    @Value("${checkers.black.pawn.eight.y}")
    private int checkersBlackPawnEightY;

    public static float getAbsolutePositionX(int positionX, float fieldWidth) {
        return (fieldWidth * (positionX-1)) - (fieldWidth * 4) + (fieldWidth/2);
    }

    public static float getAbsolutePositionY(int positionY, float fieldWidth) {
        return (fieldWidth * (positionY-1)) - (fieldWidth * 4) + (fieldWidth/2);
    }

    public int getCheckersWhitePawnFirstX() {
        return checkersWhitePawnFirstX;
    }

    public void setCheckersWhitePawnFirstX(int checkersWhitePawnFirstX) {
        this.checkersWhitePawnFirstX = checkersWhitePawnFirstX;
    }

    public int getCheckersWhitePawnFirstY() {
        return checkersWhitePawnFirstY;
    }

    public void setCheckersWhitePawnFirstY(int checkersWhitePawnFirstY) {
        this.checkersWhitePawnFirstY = checkersWhitePawnFirstY;
    }

    public int getCheckersWhitePawnSecondX() {
        return checkersWhitePawnSecondX;
    }

    public void setCheckersWhitePawnSecondX(int checkersWhitePawnSecondX) {
        this.checkersWhitePawnSecondX = checkersWhitePawnSecondX;
    }

    public int getCheckersWhitePawnSecondY() {
        return checkersWhitePawnSecondY;
    }

    public void setCheckersWhitePawnSecondY(int checkersWhitePawnSecondY) {
        this.checkersWhitePawnSecondY = checkersWhitePawnSecondY;
    }

    public int getCheckersWhitePawnThirdX() {
        return checkersWhitePawnThirdX;
    }

    public void setCheckersWhitePawnThirdX(int checkersWhitePawnThirdX) {
        this.checkersWhitePawnThirdX = checkersWhitePawnThirdX;
    }

    public int getCheckersWhitePawnThirdY() {
        return checkersWhitePawnThirdY;
    }

    public void setCheckersWhitePawnThirdY(int checkersWhitePawnThirdY) {
        this.checkersWhitePawnThirdY = checkersWhitePawnThirdY;
    }

    public int getCheckersWhitePawnFourthX() {
        return checkersWhitePawnFourthX;
    }

    public void setCheckersWhitePawnFourthX(int checkersWhitePawnFourthX) {
        this.checkersWhitePawnFourthX = checkersWhitePawnFourthX;
    }

    public int getCheckersWhitePawnFourthY() {
        return checkersWhitePawnFourthY;
    }

    public void setCheckersWhitePawnFourthY(int checkersWhitePawnFourthY) {
        this.checkersWhitePawnFourthY = checkersWhitePawnFourthY;
    }

    public int getCheckersWhitePawnFifthX() {
        return checkersWhitePawnFifthX;
    }

    public void setCheckersWhitePawnFifthX(int checkersWhitePawnFifthX) {
        this.checkersWhitePawnFifthX = checkersWhitePawnFifthX;
    }

    public int getCheckersWhitePawnFifthY() {
        return checkersWhitePawnFifthY;
    }

    public void setCheckersWhitePawnFifthY(int checkersWhitePawnFifthY) {
        this.checkersWhitePawnFifthY = checkersWhitePawnFifthY;
    }

    public int getCheckersWhitePawnSixthX() {
        return checkersWhitePawnSixthX;
    }

    public void setCheckersWhitePawnSixthX(int checkersWhitePawnSixthX) {
        this.checkersWhitePawnSixthX = checkersWhitePawnSixthX;
    }

    public int getCheckersWhitePawnSixthY() {
        return checkersWhitePawnSixthY;
    }

    public void setCheckersWhitePawnSixthY(int checkersWhitePawnSixthY) {
        this.checkersWhitePawnSixthY = checkersWhitePawnSixthY;
    }

    public int getCheckersWhitePawnSeventhX() {
        return checkersWhitePawnSeventhX;
    }

    public void setCheckersWhitePawnSeventhX(int checkersWhitePawnSeventhX) {
        this.checkersWhitePawnSeventhX = checkersWhitePawnSeventhX;
    }

    public int getCheckersWhitePawnSeventhY() {
        return checkersWhitePawnSeventhY;
    }

    public void setCheckersWhitePawnSeventhY(int checkersWhitePawnSeventhY) {
        this.checkersWhitePawnSeventhY = checkersWhitePawnSeventhY;
    }

    public int getCheckersWhitePawnEightX() {
        return checkersWhitePawnEightX;
    }

    public void setCheckersWhitePawnEightX(int checkersWhitePawnEightX) {
        this.checkersWhitePawnEightX = checkersWhitePawnEightX;
    }

    public int getCheckersWhitePawnEightY() {
        return checkersWhitePawnEightY;
    }

    public void setCheckersWhitePawnEightY(int checkersWhitePawnEightY) {
        this.checkersWhitePawnEightY = checkersWhitePawnEightY;
    }

    public int getCheckersBlackPawnFirstX() {
        return checkersBlackPawnFirstX;
    }

    public void setCheckersBlackPawnFirstX(int checkersBlackPawnFirstX) {
        this.checkersBlackPawnFirstX = checkersBlackPawnFirstX;
    }

    public int getCheckersBlackPawnFirstY() {
        return checkersBlackPawnFirstY;
    }

    public void setCheckersBlackPawnFirstY(int checkersBlackPawnFirstY) {
        this.checkersBlackPawnFirstY = checkersBlackPawnFirstY;
    }

    public int getCheckersBlackPawnSecondX() {
        return checkersBlackPawnSecondX;
    }

    public void setCheckersBlackPawnSecondX(int checkersBlackPawnSecondX) {
        this.checkersBlackPawnSecondX = checkersBlackPawnSecondX;
    }

    public int getCheckersBlackPawnSecondY() {
        return checkersBlackPawnSecondY;
    }

    public void setCheckersBlackPawnSecondY(int checkersBlackPawnSecondY) {
        this.checkersBlackPawnSecondY = checkersBlackPawnSecondY;
    }

    public int getCheckersBlackPawnThirdX() {
        return checkersBlackPawnThirdX;
    }

    public void setCheckersBlackPawnThirdX(int checkersBlackPawnThirdX) {
        this.checkersBlackPawnThirdX = checkersBlackPawnThirdX;
    }

    public int getCheckersBlackPawnThirdY() {
        return checkersBlackPawnThirdY;
    }

    public void setCheckersBlackPawnThirdY(int checkersBlackPawnThirdY) {
        this.checkersBlackPawnThirdY = checkersBlackPawnThirdY;
    }

    public int getCheckersBlackPawnFourthX() {
        return checkersBlackPawnFourthX;
    }

    public void setCheckersBlackPawnFourthX(int checkersBlackPawnFourthX) {
        this.checkersBlackPawnFourthX = checkersBlackPawnFourthX;
    }

    public int getCheckersBlackPawnFourthY() {
        return checkersBlackPawnFourthY;
    }

    public void setCheckersBlackPawnFourthY(int checkersBlackPawnFourthY) {
        this.checkersBlackPawnFourthY = checkersBlackPawnFourthY;
    }

    public int getCheckersBlackPawnFifthX() {
        return checkersBlackPawnFifthX;
    }

    public void setCheckersBlackPawnFifthX(int checkersBlackPawnFifthX) {
        this.checkersBlackPawnFifthX = checkersBlackPawnFifthX;
    }

    public int getCheckersBlackPawnFifthY() {
        return checkersBlackPawnFifthY;
    }

    public void setCheckersBlackPawnFifthY(int checkersBlackPawnFifthY) {
        this.checkersBlackPawnFifthY = checkersBlackPawnFifthY;
    }

    public int getCheckersBlackPawnSixthX() {
        return checkersBlackPawnSixthX;
    }

    public void setCheckersBlackPawnSixthX(int checkersBlackPawnSixthX) {
        this.checkersBlackPawnSixthX = checkersBlackPawnSixthX;
    }

    public int getCheckersBlackPawnSixthY() {
        return checkersBlackPawnSixthY;
    }

    public void setCheckersBlackPawnSixthY(int checkersBlackPawnSixthY) {
        this.checkersBlackPawnSixthY = checkersBlackPawnSixthY;
    }

    public int getCheckersBlackPawnSeventhX() {
        return checkersBlackPawnSeventhX;
    }

    public void setCheckersBlackPawnSeventhX(int checkersBlackPawnSeventhX) {
        this.checkersBlackPawnSeventhX = checkersBlackPawnSeventhX;
    }

    public int getCheckersBlackPawnSeventhY() {
        return checkersBlackPawnSeventhY;
    }

    public void setCheckersBlackPawnSeventhY(int checkersBlackPawnSeventhY) {
        this.checkersBlackPawnSeventhY = checkersBlackPawnSeventhY;
    }

    public int getCheckersBlackPawnEightX() {
        return checkersBlackPawnEightX;
    }

    public void setCheckersBlackPawnEightX(int checkersBlackPawnEightX) {
        this.checkersBlackPawnEightX = checkersBlackPawnEightX;
    }

    public int getCheckersBlackPawnEightY() {
        return checkersBlackPawnEightY;
    }

    public void setCheckersBlackPawnEightY(int checkersBlackPawnEightY) {
        this.checkersBlackPawnEightY = checkersBlackPawnEightY;
    }
}

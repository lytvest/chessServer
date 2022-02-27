package ru.lytvest.chess.net;

public class AnswerBoard {

    public String youColor = "white";
    public String username = "";
    public String enemyUsername = "";
    public String pen = "";
    public long timeWhite = 0;
    public long timeBlack = 0;
    public String message = "";
    public String move;

    AnswerBoard(){

    }

    public AnswerBoard(String youColor, String username, String enemyUsername, String pen, long timeWhite, long timeBlack, String message, String oldTurn) {
        this.youColor = youColor;
        this.username = username;
        this.enemyUsername = enemyUsername;
        this.pen = pen;
        this.timeWhite = timeWhite;
        this.timeBlack = timeBlack;
        this.message = message;
        this.move = oldTurn;
    }
}

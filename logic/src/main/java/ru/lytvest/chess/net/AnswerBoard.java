package ru.lytvest.chess.net;

//import lombok.Builder;
//
//@Builder
public class AnswerBoard {

    private String youColor = "white";
    private String username = "";
    private String enemyUsername = "";
    private String pen = "";
    private long timeWhite = 0;
    private long timeBlack = 0;
    private String message = "";
    private String move;


    public AnswerBoard(String youColor, String username, String enemyUsername, String pen, long timeWhite, long timeBlack, String message, String move) {
        this.youColor = youColor;
        this.username = username;
        this.enemyUsername = enemyUsername;
        this.pen = pen;
        this.timeWhite = timeWhite;
        this.timeBlack = timeBlack;
        this.message = message;
        this.move = move;
    }

    public AnswerBoard() {
    }

    public String getYouColor() {
        return youColor;
    }

    public void setYouColor(String youColor) {
        this.youColor = youColor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEnemyUsername() {
        return enemyUsername;
    }

    public void setEnemyUsername(String enemyUsername) {
        this.enemyUsername = enemyUsername;
    }

    public String getPen() {
        return pen;
    }

    public void setPen(String pen) {
        this.pen = pen;
    }

    public long getTimeWhite() {
        return timeWhite;
    }

    public void setTimeWhite(long timeWhite) {
        this.timeWhite = timeWhite;
    }

    public long getTimeBlack() {
        return timeBlack;
    }

    public void setTimeBlack(long timeBlack) {
        this.timeBlack = timeBlack;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }
}

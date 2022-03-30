package ru.lytvest.chess.net;



import java.util.Objects;


public class Status {
    private String status;
    private String message;
    private AnswerBoard game;
    private String login;

    public boolean isOk(){
        return Objects.equals(status, "ok");
    }

    public Status(String status, String message, AnswerBoard game, String login) {
        this.status = status;
        this.message = message;
        this.game = game;
        this.login = login;
    }
    public Status(){}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AnswerBoard getGame() {
        return game;
    }

    public void setGame(AnswerBoard game) {
        this.game = game;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}

package ru.lytvest.chess.net;



import lombok.Data;

import java.util.Objects;

@Data
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


}

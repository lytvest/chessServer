package ru.lytvest.chess.net;

public class Status {
    public String status;
    public String message;
    public AnswerBoard game;

    @Override
    public String toString() {
        return "Status{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

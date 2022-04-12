package ru.lytvest.chess.net;


import lombok.Data;

@Data
public class ContentRequest {
    public String user;
    public String pass;
    public String move;

    public ContentRequest(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    public ContentRequest(String user, String pass, String move) {
        this.user = user;
        this.pass = pass;
        this.move = move;
    }

    public ContentRequest() {
    }

}

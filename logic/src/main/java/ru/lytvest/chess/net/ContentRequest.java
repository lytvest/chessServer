package ru.lytvest.chess.net;





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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }
}

package ru.lytvest.chessserver;

import ru.lytvest.chess.net.AnswerBoard;

public class Player extends PlayerObserver {
    protected GameController game;
    public Player(String name, GameController game) {
        super(name);
        this.game = game;
    }

    public AnswerBoard move(String move){
        game.move(name, move);
        return answer;
    }

}

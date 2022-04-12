package ru.lytvest.chessserver;

import lombok.val;

import java.util.Random;

public class AIObserver implements GameObserver {

    public static final String NAME = "AI-BOT";
    private boolean meTurn = false;


    public AIObserver(GameController game) {
        this.game = game;
    }


    @Override
    public void update(String pen, String oldTurn, String nameWhite, String nameBlack, long timeWhite, long timeBlack, boolean isWhiteTurn) {
        if (nameWhite.equals(NAME)) {
            meTurn = isWhiteTurn;
        } else {
            meTurn = !isWhiteTurn;
        }
    }

    protected GameController game;

    public void move() {
        if (meTurn) {
            val move = game.getBoard().bestTurn(2);
            game.move(NAME, move.toString());
        }
    }

}

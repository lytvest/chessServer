package ru.lytvest.chessserver;

import lombok.val;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class AIObserver implements GameObserver {

    public static final String NAME = "AI-BOT";
    private boolean meTurn = false;


    public AIObserver(GameController game) {
        this.game = game;
    }

    public static void main(String[] args) {
        new AIObserver(null);
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
            val move = game.getBoard().bestTurn(3);
            if (move != null)
                game.move(NAME, move.toString());
        }
    }

}

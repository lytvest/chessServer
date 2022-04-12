package ru.lytvest.chessserver;

import ru.lytvest.chess.Board;

public interface GameObserver {

    void update(String pen, String oldTurn, String nameWhite, String nameBlack, long timeWhite, long timeBlack, boolean isWhiteTurn);
}

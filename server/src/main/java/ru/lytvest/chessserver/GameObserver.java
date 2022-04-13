package ru.lytvest.chessserver;

import ru.lytvest.chess.Board;
import ru.lytvest.chess.net.BoardResponse;

public interface GameObserver {

    void update(Board board, String oldTurn, String nameWhite, String nameBlack, long timeWhite, long timeBlack);

    String getName();
    BoardResponse getAnswer();
}

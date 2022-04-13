package ru.lytvest.chessserver;

import ru.lytvest.chess.Board;
import ru.lytvest.chess.net.BoardResponse;
import ru.lytvest.chessserver.entities.Game;

public interface ChessGame {



    Game getGame();

    BoardResponse getAnswer(String name);

    void move(String name, String move);

    Board getBoard();

    String getId();

    void addObserver(GameObserver observer);
    void removeObserver(GameObserver observer);

    boolean isEmptyObservers();
}

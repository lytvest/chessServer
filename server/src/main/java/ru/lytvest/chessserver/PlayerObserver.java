package ru.lytvest.chessserver;

import lombok.val;
import ru.lytvest.chess.Board;
import ru.lytvest.chess.net.BoardResponse;


public class PlayerObserver implements GameObserver {

    protected BoardResponse answer;
    protected final String name;
    protected Board board;
    protected ChessGame game;
    public PlayerObserver(String name, ChessGame game) {
        this.name = name;
        this.game = game;
    }

    @Override
    public void update(Board board, String oldTurn, String nameWhite, String nameBlack, long timeWhite, long timeBlack) {
        this.board = board;
        val bulder = BoardResponse.builder()
                .pen(board.toPen())
                .move(oldTurn)
                .message("moved to " + oldTurn);
        if (name.equals(nameWhite)) {
            bulder
                    .username(nameWhite)
                    .enemyUsername(nameBlack)
                    .meColor("white")
                    .meTime(timeWhite)
                    .enemyTime(timeBlack);
        } else {
            bulder
                    .username(nameBlack)
                    .enemyUsername(nameWhite)
                    .meColor("black")
                    .meTime(timeBlack)
                    .enemyTime(timeWhite);
        }
        answer = bulder.build();
        answer.setEnd(game.isEnd());
    }

    @Override
    public String getName() {
        return name;
    }

    public BoardResponse getAnswer() {
        if(game.isEnd()){
            game.removeObserver(this);
        }
        return answer;
    }

    @Override
    public String toString() {
        return name;
    }
}

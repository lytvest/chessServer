package ru.lytvest.chessserver;

import lombok.Getter;
import lombok.val;
import ru.lytvest.chess.Board;
import ru.lytvest.chess.net.BoardResponse;

public class AIObserver implements GameObserver {

    public static final String NAME = "AI-BOT";
    private boolean meTurn = false;
    @Getter
    private boolean endGame = false;

    protected ChessGame game;


    public AIObserver(ChessGame game) {
        this.game = game;
    }

    public static void main(String[] args) {
        new AIObserver(null);
    }


    @Override
    public void update(Board board, String oldTurn, String nameWhite, String nameBlack, long timeWhite, long timeBlack) {
        if (nameWhite.equals(NAME)) {
            meTurn = board.isWhite;
        } else {
            meTurn = !board.isWhite;
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public BoardResponse getAnswer() {
        return null;
    }


    public void move() {
        if (game.getBoard().isEndGame()){
            game.removeObserver(this);
            endGame = true;
        }
        if (meTurn) {
            val move = game.getBoard().bestTurn(2);
            if (move != null)
                game.move(NAME, move.toString());
        }
    }

    @Override
    public String toString() {
        return NAME;
    }
}

package ru.lytvest.chessserver;

import lombok.val;
import ru.lytvest.chess.net.AnswerBoard;


public class PlayerObserver implements GameObserver {

    protected AnswerBoard answer;
    protected final String name;


    public PlayerObserver(String name) {
        this.name = name;
    }

    @Override
    public void update(String pen, String oldTurn, String nameWhite, String nameBlack, long timeWhite, long timeBlack, boolean isWhiteTurn) {
        val bulder = AnswerBoard.builder()
                .pen(pen)
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
    }

    public AnswerBoard getAnswer() {
        return answer;
    }


}

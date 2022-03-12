package ru.lytvest.chessserver;

import lombok.val;
import ru.lytvest.chess.Board;
import ru.lytvest.chess.Move;
import ru.lytvest.chess.net.AnswerBoard;
import ru.lytvest.chessserver.entities.User;

import java.util.Objects;

public class GameWithAI implements GameInf {

    Board board = Board.START;
    long timeWhite = 0;
    long timeBlack = 0;
    long oldTurnTime = 0;
    String oldTurn = null;
    String color = "white";
    User user;

    GameWithAI(User user){
        this.user = user;
    }


    @Override
    public AnswerBoard getAnswer(User user) {
        return AnswerBoard.builder()
                .username(user.getName())
                .enemyUsername("ai-bot")
                .pen(board.toPen())
                .timeBlack(timeBlack)
                .timeWhite(timeWhite)
                .move(oldTurn)
        .build();
    }

    @Override
    public AnswerBoard move(User user, String turn) {
        val move = Move.from(turn);
        if (Objects.equals(color, "white") != board.isWhite || !board.canMove(move))
            return getAnswer(user);

        updateTime(Objects.equals(color, "white"));

        board = board.moved(move);
        oldTurnTime = System.currentTimeMillis();
        oldTurn = turn;
        val answer = getAnswer(user);
        answer.setMove(turn);



        updateTime(Objects.equals(color, "black"));




        return null;
    }

    private void updateTime(boolean isWhite) {
        if (board.numberCourse != 1 && isWhite) {
            timeWhite += System.currentTimeMillis() - oldTurnTime;
        }
        if (board.numberCourse != 1 && !isWhite) {
            timeBlack = System.currentTimeMillis() - oldTurnTime;
        }
    }
}

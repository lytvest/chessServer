package ru.lytvest.chessserver;

import lombok.val;
import ru.lytvest.chess.Board;
import ru.lytvest.chess.Move;
import ru.lytvest.chess.net.AnswerBoard;
import ru.lytvest.chessserver.entities.User;

import java.util.Arrays;

public class Game implements GameInf{
    User whiteUser;
    User blackUser;
    Board board = Board.START;
    long timeWhite = 0;
    long timeBlack = 0;
    long oldTurnTime = 0;
    String oldTurn = null;


    public Game(User whiteUser, User blackUser) {
        this.whiteUser = whiteUser;
        this.blackUser = blackUser;
    }

    public AnswerBoard getAnswer(User user) {
        val answer = AnswerBoard.builder();
        if (user.equals(whiteUser)) {
            answer.youColor("white")
                    .username(whiteUser.getName())
                    .enemyUsername(blackUser.getName());
        } else {
            answer.youColor("black")
                    .username(blackUser.getName())
                    .enemyUsername(whiteUser.getName());

        }
        return answer.pen(board.toPen())
                .timeWhite(timeWhite)
                .timeBlack(timeBlack)
                .message("get answer for " + user)
                .move(oldTurn).build();
    }

    String movies(boolean isWhite) {
        if (isWhite != board.isWhite)
            return "";
        return Arrays.toString(board.filteredMovies().stream().map(Move::toString).toArray());
    }

    public AnswerBoard move(User user, String turn) {
        val move = Move.from(turn);
        if (user.equals(whiteUser) != board.isWhite || !board.canMove(move))
            return getAnswer(user);
        if (board.numberCourse != 1 && user.equals(whiteUser)) {
            timeWhite += System.currentTimeMillis() - oldTurnTime;
        }
        if (board.numberCourse != 1 && user.equals(blackUser)) {
            timeBlack = System.currentTimeMillis() - oldTurnTime;
        }
        board = board.moved(Move.from(turn));
        oldTurnTime = System.currentTimeMillis();
        oldTurn = turn;
        val answer = getAnswer(user);
        answer.setMove(turn);
        return answer;
    }
}

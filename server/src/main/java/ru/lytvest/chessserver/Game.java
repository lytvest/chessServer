package ru.lytvest.chessserver;

import ru.lytvest.chess.Board;
import ru.lytvest.chess.Move;
import ru.lytvest.chess.net.AnswerBoard;
import ru.lytvest.chessserver.entities.User;

import java.util.Arrays;

public class Game {
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
        String color, me, enemy;
        if (user.equals(whiteUser)){
            color = "white";
            me = whiteUser.name;
            enemy = blackUser.name;
        } else {
            color = "black";
            enemy = whiteUser.name;
            me = blackUser.name;
        }
        if (user.equals(blackUser))
            color = "black";
        return new AnswerBoard(color, me, enemy, board.toPen(), timeWhite, timeBlack, movies(user.equals(whiteUser)), oldTurn);
    }

    String movies(boolean isWhite) {
        if (isWhite != board.isWhite)
            return "";
        return   Arrays.toString(board.filteredMovies().stream().map(Move::toString).toArray());
    }

    public void move(User user, String turn) {
        if(user.equals(whiteUser) != board.isWhite)
            return;
        if(board.numberCourse != 1 && user.equals(whiteUser)){
            timeWhite += System.currentTimeMillis() - oldTurnTime;
        }
        if(board.numberCourse != 1 && user.equals(blackUser)){
            timeBlack = System.currentTimeMillis() - oldTurnTime;
        }
        board = board.moved(Move.from(turn));
        oldTurnTime = System.currentTimeMillis();
        oldTurn = turn;
    }
}

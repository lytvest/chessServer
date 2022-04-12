package ru.lytvest.chessserver;

import lombok.Data;
import lombok.Value;
import lombok.val;
import ru.lytvest.chess.Board;
import ru.lytvest.chess.Move;
import ru.lytvest.chess.net.AnswerBoard;
import ru.lytvest.chessserver.entities.Game;
import ru.lytvest.chessserver.entities.User;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameController {

    Board board = Board.START;
    long timeWhite = 0;
    long timeBlack = 0;
    long oldTurnTime = 0;
    String oldTurn = null;
    Game game;

    String nameWhite;
    String nameBlack;

    List<GameObserver> observers = new ArrayList<>();

    public GameController(String nameWhite, String nameBlack) {
        this.nameWhite = nameWhite;
        this.nameBlack = nameBlack;
        this.game = new Game(nameWhite, nameBlack, new ArrayList<>(), "no win");
    }

    public void addObserver(GameObserver observer){
        observers.add(observer);
    }
    public void removeObserver(GameObserver observer){
        observers.remove(observer);
    }

    public void move(String name, String turn) {
        if (name.equals(nameWhite) == !board.isWhite) {
            System.out.println("move " + name + " " + turn + " wrong color!");
            return;
        }
        val move = Move.from(turn);
        if(!board.canMove(move))
            return;

        if (board.isWhite){
            game.moveWhite(turn);
        } else {
            game.moveBlack(turn);
        }
        if (board.numberCourse != 1 && board.isWhite) {
            timeWhite += System.currentTimeMillis() - oldTurnTime;
        }
        if (board.numberCourse != 1 && !board.isWhite) {
            timeBlack += System.currentTimeMillis() - oldTurnTime;
        }
        board = board.moved(Move.from(turn));
        oldTurnTime = System.currentTimeMillis();
        oldTurn = turn;
        for(val observer : observers){
            observer.update(board.toPen(), oldTurn, nameWhite, nameBlack, timeWhite, timeBlack, board.isWhite);
        }
    }

    public void start() {
        for(val observer : observers){
            observer.update(board.toPen(), oldTurn, nameWhite, nameBlack, timeWhite, timeBlack, board.isWhite);
        }
    }
}

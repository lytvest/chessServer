package ru.lytvest.chessserver;

import lombok.Data;
import lombok.Getter;
import lombok.val;
import ru.lytvest.chess.Board;
import ru.lytvest.chess.Move;
import ru.lytvest.chess.net.BoardResponse;
import ru.lytvest.chessserver.entities.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class ChessGameImpl implements ChessGame {

    private Board board = Board.START;
    private long timeWhite = 0;
    private long timeBlack = 0;
    private long oldTurnTime = 0;
    private String oldTurn = null;
    private Game game;

    private String nameWhite;
    private String nameBlack;

    private ConcurrentHashMap<String, GameObserver> observers = new ConcurrentHashMap<>();


    @Getter
    private String id ;

    public ChessGameImpl(String id, String nameWhite, String nameBlack) {
        this.id = id;
        this.nameWhite = nameWhite;
        this.nameBlack = nameBlack;
        this.game = new Game(nameWhite, nameBlack, new ArrayList<>(), "no win");
    }

    @Override
    public void addObserver(GameObserver observer){
        observers.put(observer.getName(), observer);
    }
    @Override
    public void removeObserver(GameObserver observer){
        observers.remove(observer.getName());
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
        for(val observer : observers.values()){
            observer.update(board, oldTurn, nameWhite, nameBlack, timeWhite, timeBlack);
        }
    }

    public void start() {
        for(val observer : observers.values()){
            observer.update(board, oldTurn, nameWhite, nameBlack, timeWhite, timeBlack);
        }
    }




    @Override
    public BoardResponse getAnswer(String name) {
        if (observers.containsKey(name)) {
            val ans = observers.get(name).getAnswer();
            return ans;
        }
        return null;
    }

    @Override
    public boolean isEmptyObservers() {
        return observers.isEmpty();
    }

    @Override
    public String toString(){
        return "" + observers.values();
    }
}

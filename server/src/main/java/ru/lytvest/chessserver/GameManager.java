package ru.lytvest.chessserver;

import lombok.val;
import ru.lytvest.chess.Board;
import ru.lytvest.chess.net.AnswerBoard;
import ru.lytvest.chessserver.entities.User;

import java.util.concurrent.ConcurrentHashMap;

public class GameManager {

    private User old;
    private ConcurrentHashMap<User, GameInf> map = new ConcurrentHashMap<>();

    synchronized public User getOld() {
        return old;
    }

    synchronized public void setOld(User old) {
        this.old = old;
    }

    public AnswerBoard findGame(User user){
        if(map.containsKey(user))
            return map.get(user).getAnswer(user);

        return null;
    }

    public AnswerBoard create(User user){
        if(map.containsKey(user))
            return null;

        if (getOld() == null || getOld().equals(user)){
            setOld(user);
            return null;
        }

        System.out.println("create game " + getOld() + " " + user);
        var game = new Game(getOld(), user);
        map.put(getOld(), game);
        map.put(user, game);
        return game.getAnswer(user);
    }

    public AnswerBoard turn(User user, String turn){
        if (!map.containsKey(user))
            return null;
        var game = map.get(user);
        return game.move(user, turn);
    }
    public AnswerBoard endGame(User user){
        var answer = map.remove(user).getAnswer(user);
        if (answer != null)
            answer.setMessage("game end!");
        return answer;
    }


    public AnswerBoard createAI(User user) {
        map.put(user, new GameWithAI(user));
        val res = map.get(user).getAnswer(user);
        res.setPen(Board.START_PEN);
        return res;
    }
}

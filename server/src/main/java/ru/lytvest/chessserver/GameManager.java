package ru.lytvest.chessserver;

import ru.lytvest.chess.net.AnswerBoard;
import ru.lytvest.chessserver.entities.User;

import java.util.concurrent.ConcurrentHashMap;

public class GameManager {

    private User old;
    private ConcurrentHashMap<User, Game> map = new ConcurrentHashMap<>();

    synchronized public User getOld() {
        return old;
    }

    synchronized public void setOld(User old) {
        this.old = old;
    }

    public AnswerBoard findGame(User user){
        if(map.containsKey(user))
            return map.get(user).getAnswer(user);

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
        if(!game.move(user, turn))
            return null;
        return game.getAnswer(user);
    }
    public AnswerBoard endGame(User user){
        var answer = map.remove(user).getAnswer(user);
        if (answer != null)
            answer.setMessage("game end!");
        return answer;
    }


}

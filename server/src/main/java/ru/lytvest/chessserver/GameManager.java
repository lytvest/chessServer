package ru.lytvest.chessserver;

import lombok.val;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.lytvest.chess.Board;
import ru.lytvest.chess.net.AnswerBoard;
import ru.lytvest.chessserver.entities.User;

import java.util.Random;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class GameManager {

    private String old;
    private ConcurrentHashMap<String, Player> map = new ConcurrentHashMap<>();
    private CopyOnWriteArraySet<AIObserver> ai = new CopyOnWriteArraySet<>();


    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    private void aiMoved(){
        for(AIObserver observer : ai){
            observer.move();
        }
    }

    synchronized public String getOld() {
        return old;
    }

    synchronized public void setOld(String old) {
        this.old = old;
    }

    public AnswerBoard findGame(String user){
        if(map.containsKey(user))
            return map.get(user).getAnswer();

        return null;
    }

    public AnswerBoard create(String user){
        if(map.containsKey(user))
            return map.get(user).getAnswer();

        if (getOld() == null || getOld().equals(user)){
            setOld(user);
            return null;
        }

        System.out.println("create game " + getOld() + " " + user);
        var game = new GameController(getOld(), user);
        val playerWhite = new Player(getOld(), game);
        val playerBlack = new Player(user, game);
        game.addObserver(playerWhite);
        game.addObserver(playerBlack);
        map.put(getOld(), playerWhite);
        map.put(user, playerBlack);
        game.start();
        return playerWhite.getAnswer();
    }

    public AnswerBoard turn(String user, String turn){
        if (!map.containsKey(user))
            return null;
        var player = map.get(user);
        return player.move(turn);
    }
    public AnswerBoard endGame(String user){
        var answer = map.remove(user).getAnswer();
        //TODO remove game
        return answer;
    }

    private static final Random random = new Random();

    public AnswerBoard createAI(String user) {
        if(map.containsKey(user))
            return map.get(user).getAnswer();

        var game = random.nextBoolean() ? new GameController(user, AIObserver.NAME) : new GameController(AIObserver.NAME, user);
        val playerWhite = new Player(user, game);
        val playerBlack = new AIObserver(game);
        map.put(user, playerWhite);
        ai.add(playerBlack);
        game.addObserver(playerWhite);
        game.addObserver(playerBlack);
        game.start();
        return playerWhite.getAnswer();
    }
}

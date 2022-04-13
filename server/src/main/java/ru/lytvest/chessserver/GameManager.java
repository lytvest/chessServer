package ru.lytvest.chessserver;

import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.lytvest.chess.net.BoardResponse;
import ru.lytvest.chessserver.service.GameService;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class GameManager {

    private String old;
    private ConcurrentHashMap<String, ChessGame> map = new ConcurrentHashMap<>();
    private CopyOnWriteArraySet<AIObserver> ai = new CopyOnWriteArraySet<>();
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private GameService gameService;


    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    private void aiMoved() {
        for (AIObserver observer : ai) {
            observer.move();
        }
    }

    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    private void saveEndRemoveEndGames(){
        val removed = new ArrayList<String>();
        for (val game: map.values()){
            if (game.isEmptyObservers()){
                log.info("remove and save game " + game.getId());
                gameService.save(game.getGame());
                removed.add(game.getId());
            }
        }
        for(val id: removed)
            map.remove(id);
    }

    synchronized public String getOld() {
        return old;
    }

    synchronized public void setOld(String old) {
        this.old = old;
    }

    public BoardResponse findGame(String idGame, String user) {
        if (map.containsKey(idGame))
            return map.get(idGame).getAnswer(user);

        return null;
    }

    public BoardResponse create(String user) {

        if (getOld() == null || getOld().equals(user)) {
            setOld(user);
            return null;
        }


        System.out.println("create game " + getOld() + " " + user);
        var game = new ChessGameImpl(getOld(), user);
        val playerWhite = new PlayerObserver(getOld(), game);
        val playerBlack = new PlayerObserver(user, game);
        game.addObserver(playerWhite);
        game.addObserver(playerBlack);
        map.put(game.getId(), game);
        game.start();
        setOld(null);
        return game.getAnswer(user);
    }

    public BoardResponse turn(String idGame, String user, String turn) {
        if (!map.containsKey(idGame))
            return null;
        val game = map.get(idGame);
        game.move(user, turn);
        return game.getAnswer(user);
    }

    private static final Random random = new Random();

    public BoardResponse createAI(String user) {
        val game = random.nextBoolean() ? new ChessGameImpl(user, AIObserver.NAME) : new ChessGameImpl(AIObserver.NAME, user);

        val playerWhite = new PlayerObserver(user, game);
        val playerBlack = new AIObserver(game);
        map.put(game.getId(), game);
        ai.add(playerBlack);
        game.addObserver(playerWhite);
        game.addObserver(playerBlack);
        game.start();

        return game.getAnswer(user);
    }
}

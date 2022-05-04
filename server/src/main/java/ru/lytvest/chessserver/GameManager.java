package ru.lytvest.chessserver;

import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.lytvest.chess.net.BoardResponse;
import ru.lytvest.chess.net.CreateResponse;
import ru.lytvest.chess.net.SearchResponse;
import ru.lytvest.chess.net.Statistic;
import ru.lytvest.chessserver.service.GameService;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Service
public class GameManager {

    private String old;
    private String oldId;
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
    synchronized public String getOldId() {
        return oldId;
    }

    synchronized public void setOldId(String old) {
        this.oldId = old;
    }

    public BoardResponse findGame(String idGame, String user) {
        if (map.containsKey(idGame))
            return map.get(idGame).getAnswer(user);

        return null;
    }

    public CreateResponse create(String user) {
        if (getOld() == null || getOld().equals(user)) {
            setOld(user);
            setOldId(UUID.randomUUID().toString());
            return new CreateResponse(getOldId());
        }
        log.info("create game " + getOld() + " " + user);
        var game = new ChessGameImpl(getOldId(), getOld(), user);
        val playerWhite = new PlayerObserver(getOld(), game);
        val playerBlack = new PlayerObserver(user, game);
        game.addObserver(playerWhite);
        game.addObserver(playerBlack);
        map.put(game.getId(), game);
        game.start();
        setOld(null);
        setOldId(null);
        return new CreateResponse(game.getId());
    }

    public SearchResponse search(String id){
        if(Objects.equals(getOldId(), id)){
            return new SearchResponse(id, false);
        }
        if (map.containsKey(id))
            return new SearchResponse(id, true);

        return new SearchResponse(id, false);
    }

    public BoardResponse turn(String idGame, String user, String turn) {
        if (!map.containsKey(idGame))
            return null;
        val game = map.get(idGame);
        game.move(user, turn);
        return game.getAnswer(user);
    }

    private static final Random random = new Random();

    public CreateResponse createAI(String user) {
        val id = UUID.randomUUID().toString();
        val game = random.nextBoolean() ? new ChessGameImpl(id, user, AIObserver.NAME) : new ChessGameImpl(id, AIObserver.NAME, user);

        val playerWhite = new PlayerObserver(user, game);
        val playerBlack = new AIObserver(game);
        map.put(game.getId(), game);
        ai.add(playerBlack);
        game.addObserver(playerWhite);
        game.addObserver(playerBlack);
        game.start();

        return new CreateResponse(id);

    }

    public Statistic getStatistic() {
        return new Statistic(map.size(), map.size() * 2 - ai.size() );
    }
}

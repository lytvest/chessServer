package ru.lytvest.chessserver.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.lytvest.chessserver.entities.Game;
import ru.lytvest.chessserver.repos.GameRepository;
import ru.lytvest.chessserver.repos.TurnRepository;

@Service
public class GameService {
    @Autowired
    GameRepository gameRepository;

    @Autowired
    TurnRepository turnRepository;

    public void save(Game game){
        gameRepository.save(game);
        turnRepository.saveAll(game.getTurns());
    }
}

package ru.lytvest.chessserver.repos;

import org.springframework.data.repository.CrudRepository;
import ru.lytvest.chessserver.entities.Game;

public interface GameRepository extends CrudRepository<Game, Long> {
}

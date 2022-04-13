package ru.lytvest.chessserver.repos;

import org.springframework.data.repository.CrudRepository;
import ru.lytvest.chessserver.entities.Game;
import ru.lytvest.chessserver.entities.Turn;

public interface TurnRepository extends CrudRepository<Turn, Long> {
}

package ru.lytvest.chessserver.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.lytvest.chessserver.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByName(String name);
    User findByNameAndPass(String name, String pass);
}

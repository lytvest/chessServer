package ru.lytvest.chessserver.entities;


import ru.lytvest.chess.net.ContentRequest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;


    public String name;

    public String pass;

    public User(){}

    public User(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }

    public User(ContentRequest login){
        name = login.user;
        pass = login.pass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(pass, user.pass);
    }

    @Override
    public int hashCode() {
        return id.intValue();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }
}
